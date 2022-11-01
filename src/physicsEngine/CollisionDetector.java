package physicsEngine;

import java.util.ArrayList;

import org.joml.Vector3f;

import environment.BasicObject;

public class CollisionDetector {

	ArrayList<BoundingSphere> boundingSpheres;
	
	public CollisionDetector(ArrayList<BasicObject> basicObjects) {
		boundingSpheres = new ArrayList<BoundingSphere>();
		for(BasicObject obj : basicObjects) {
			boundingSpheres.add(obj.getBoundingSphere());
		}
	}
	
	public void update() {
		for(int i = 0; i < boundingSpheres.size(); i++) {
			for(int j = 0; j < boundingSpheres.size()-1; j++) {
				if(i!=j) {
				if(isIntersecting(boundingSpheres.get(i),boundingSpheres.get(j))) {
					System.out.println("true");
				}
				}
			}
		}
	}
	
	
	
	public boolean isIntersecting(BoundingSphere sphere1, BoundingSphere sphere2) {
		
		float radiusDistance = sphere1.getRadius() + sphere2.getRadius();
		float centerDistance = calcTotalDistance(sphere1.getCenter(), sphere2.getCenter());
		if(centerDistance < radiusDistance) {
			return true;
		}
		
		return false;
	}
	
	private float calcTotalDistance(Vector3f a, Vector3f b){
		float dx = b.x - a.x;
		float dy = b.y - a.y;
		float dz = b.z - a.z;
		return (float) Math.sqrt((dx*dx) + (dy*dy) + dz*dz);
	
	}
	
	
	
	
}
