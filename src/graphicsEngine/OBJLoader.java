package graphicsEngine;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL30.*;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;


public class OBJLoader {
	
	
	private ArrayList<Integer> vaoIDs = new ArrayList<Integer>(); 
	private ArrayList<Integer> vboIDs = new ArrayList<Integer>(); /* mem. management */
	private ArrayList<Integer> textureIDs = new ArrayList<Integer>();
	
        /* level of detail, negative is better */	
	private int LOD = 1;
	
	
	
	public RawObj loadToVao(float[] positions, int[] indices,float[] normals, float[] textureCoords ) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttribList(0,positions,3);
		storeDataInAttribList(1,textureCoords,2);
		storeDataInAttribList(2,normals,3);
		unbindVAO();
		return new RawObj(vaoID, indices.length); /* each vertex has 3 floats. */
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaoIDs.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttribList(int attribNumber, float[] data, int dimensions) {
		int vboID = GL15.glGenBuffers();
		vboIDs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer  buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW); 
		GL20.glVertexAttribPointer(attribNumber, dimensions, GL11.GL_FLOAT, false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,  0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); /* buffer needs to have order of floats reversed to be read from */
		return buffer;
	}
	
        /* indices to draw from vertex postiions, counterclockwise */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vboIDs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer,GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer; 
	}
	
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0); /* 0 unbinds the current vao */
	}
	
	public int loadTexture(String filePath) {
		try {
		 File file = new File("res/"+filePath+".png");
		  InputStream in = new FileInputStream(file);
		  PNGDecoder decoder = new PNGDecoder(in);
		  ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
		  decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
		  buffer.flip();
		  int textureID = glGenTextures();
		  glBindTexture(GL_TEXTURE_2D, textureID); 
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		  /* lowers texture resolution at distance from camera */
		  glGenerateMipmap(textureID);
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		  glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS,LOD);
		 
		  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		  
		  glBindTexture(GL_TEXTURE_2D, 0);
		  textureIDs.add(textureID);
		  return textureID;
		}catch(Exception e) {
			System.err.println("Failed to load texture : " + filePath);
			clearData();
			System.exit(-1);
			
		}
		return 1;
	}
	
	
	
	public RawObj loadObjModel(String filePath) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/"+filePath+".obj"));
			
		} catch (FileNotFoundException e) {
			System.err.println("failed to load 3D model! (.obj) : " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}
		BufferedReader reader = new BufferedReader(fr);
		String line; 
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] texturesArray = null;
		int[] indicesArray = null;
		try {
			
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")) {
					texturesArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				} 
			}
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1,indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex2,indices, textures, normals, texturesArray, normalsArray);
				processVertex(vertex3,indices, textures, normals, texturesArray, normalsArray);
				
				line = reader.readLine();
			}
			
			reader.close();
			
		}catch(Exception e) {
			System.err.println("Invalid file format for .obj texture : "+filePath);
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		int vertexPointer = 0;
		
		for(Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i<indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		return loadToVao(verticesArray, indicesArray, normalsArray,	 texturesArray);
		
	}
	
	private static void processVertex(String[] vertexData, ArrayList<Integer> indices, ArrayList<Vector2f> textures, ArrayList<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer*2] = currentTexture.x;
		textureArray[currentVertexPointer*2 + 1] = 1 - currentTexture.y;

		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer*3 + 2] = currentNorm.z;


	
	}

	
	/* called when the program shuts down to free up memory */
	public void clearData() {
		for(int vao : vaoIDs) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vboIDs) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture : textureIDs) {
			GL11.glDeleteTextures(texture);
		}
		
	}
	
}
