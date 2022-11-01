package environment;

import org.joml.Vector3f;

import graphicsEngine.Obj;
import physicsEngine.BoundingSphere;

public class BasicObject {

	private Obj texturedObj;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private BoundingSphere boundingSphere;
	
	public BasicObject(Obj texturedObj, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.texturedObj = texturedObj;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		boundingSphere = new BoundingSphere(position, scale);
		
	}
	
	public void changePosition(float dx,float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		
	}
	
	public void changeRotation(float rx, float ry, float rz) {
		rotX += rx;
		rotY += ry;
		rotZ += rz;
		
	}
		
	public BoundingSphere getBoundingSphere() {
		return boundingSphere;
	}
	
	public Obj getTexturedObj() {
		return texturedObj;
	}
	public void setTexturedObj(Obj texturedObj) {
		this.texturedObj = texturedObj;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public float getRotX() {
		return rotX;
	}
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	public float getRotY() {
		return rotY;
	}
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	
	
	
	
	
	
}
