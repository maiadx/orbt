package engine;

import java.io.IOException;


import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFWKeyCallback.*;

import environment.BasicObject;
import environment.Light;
import environment.Planet;
import environment.Player;
import environment.Skybox;
import environment.Structure;
import graphicsEngine.Camera;
import graphicsEngine.Display;
import graphicsEngine.OBJLoader;
import graphicsEngine.OBJRender;
import graphicsEngine.Obj;
import graphicsEngine.ObjTexture;
import graphicsEngine.RawObj;
import graphicsEngine.StaticShader;
import physicsEngine.CollisionDetector;

public class Main implements Runnable {

	private int WIDTH;
	private int HEIGHT;
	private String title;
	private Display display;
	static final int FPS = 50;
	static final double time_per_update = 1000000000/FPS;
	private Thread thread;
	private Boolean running; 
	private GLFWKeyCallback keyCallback;

	OBJLoader objLoader;
	OBJRender objRender;
	StaticShader shader;
	
	ObjTexture texture;
	ObjTexture texture2;
	ArrayList<BasicObject> renderObjects = new ArrayList<BasicObject>();
	ArrayList<BasicObject> environment = new ArrayList<BasicObject>();
	ArrayList<BasicObject> physicsObjects = new ArrayList<BasicObject>();
	Skybox stars;
	Skybox stars2;
	Camera camera;
	Light starLight;
	Player player;
	Planet planet1;
	
	CollisionDetector collisionDetector;

	
	public Main(int width, int height, String name) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.title = name;
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error loading assets!!");
			System.exit(-1);
		}
		
	}
	
	public void init(){
		setup();
		running = true;
		GLFW.glfwInit();
		objLoader = new OBJLoader();		
		display = new Display(WIDTH,HEIGHT);
		shader = new StaticShader();
		objRender = new OBJRender(display,shader);
		// ----------------------------------------
		RawObj obj = objLoader.loadObjModel("jupiter");
		RawObj skyboxObj = objLoader.loadToVao(Skybox.getVertexData(), Skybox.getIndicesData(),Skybox.getNormalData(), Skybox.getTextureData());
				  
		texture = new ObjTexture(objLoader.loadTexture("Starfield"));
		texture2 = new ObjTexture(objLoader.loadTexture("jupiter_tex"));
		texture2.setReflectivity(100000);
		texture2.setShineDamper(2);
		Obj texturedSkybox = new Obj(skyboxObj, texture);
		Obj texturedObj2 = new Obj(obj, texture2);
		stars = new Skybox(texturedSkybox, new Vector3f(0,0,0), 0.0f, 0.0f, 0.0f, 19999.0f);
		player = new Player(texturedObj2, new Vector3f(0,-5,-3),0.0f,0.0f,0.0f,1f, display);
		camera = new Camera(player,display);
		starLight = new Light(new Vector3f(5000,10,-3000), new Vector3f(1,1,1));
		stars2 = new Skybox(texturedSkybox, new Vector3f(0,0,0), 90f, 180f,0,19999.0f);
		planet1 =  new Planet( texturedObj2, new Vector3f(0,-7,-100), -10,0,0,1);
		renderObjects.add(stars);
		renderObjects.add(stars2);
		renderObjects.add(planet1);
		loadMap("test");
		renderObjects.add(player);
		collisionDetector = new CollisionDetector(physicsObjects);
		//-------------------------------------------
		run();
	    }
	
	public void setup() {
		//thread = new Thread( ,"secondary");
		//thread.start();						// TODO: thread all ai and networking, and audio. have game engine run on separate thread. 
		
		
	}

	
	public synchronized void run(){

		
		
		
	while(running) {
		
		
		planet1.changeRotation(0,.04f,0);
		objRender.renderAllObjects(starLight, camera, renderObjects);
		update();

	}
	
	exit();
	}
	 	
	
	public void update() {
		player.update();
		camera.update(display);
		stars.setPosition(camera.getPosition());
		stars2.setPosition(camera.getPosition());
		physicsObjects.get(0).changePosition(0.1f, 0, 0);
		collisionDetector.update();
		
		fpsRegulator();

	}
		
	public void initHUD() {
	//	HUDTexture hudTexture = new HUDTexture(loader.loadTexture("hud1"), new Vector2f(1,1), new Vector2f(1,1));

	}
	
	public void fpsRegulator() {
		
		if(display.shouldExit) {
			running = false;
		}
		
	    double delta = 0;
	    long now;
	    long last_time = System.nanoTime();
	    long timer = 0;
	    int upts = 0;
	    double d2 = 1;
	    while(true){
	      now = System.nanoTime();
	      delta += (now - last_time)/time_per_update;
	      timer+= now-last_time;
	      last_time = now;
	      if(delta>=1){
	    	delta--;
	    	upts++;
	    	break;
	      }
	    }
	}
	
	public void loadMap(String map) {
		MapLoader mapLoader = new MapLoader();
		ArrayList<String[]> rawMapData = mapLoader.loadMapData(map);
		for(String[] data : rawMapData ) {
			RawObj rawObj = objLoader.loadObjModel(data[0]);
			ObjTexture tex = new ObjTexture(objLoader.loadTexture(data[1]));
			Obj obj = new Obj(rawObj, tex);
			float xPos = Float.parseFloat(data[2]);
			float yPos = Float.parseFloat(data[3]);
			float zPos = Float.parseFloat(data[4]);
			float rotX = Float.parseFloat(data[5]);
			float rotY = Float.parseFloat(data[6]);
			float rotZ = Float.parseFloat(data[7]);
			float scale = Float.parseFloat(data[8]);
			
			Vector3f position = new Vector3f(xPos, yPos, zPos);
			BasicObject basicObj = new BasicObject(obj, position, rotX, rotY, rotZ, scale);
			
			
		physicsObjects.add(basicObj);
		renderObjects.add(basicObj);
		
		}
		

		
		
	}
	
	
	
	public void exit() {
		shader.close();
		objLoader.clearData();
		System.exit(0);
	}
}
