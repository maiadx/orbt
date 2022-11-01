package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import graphicsEngine.Camera;

public class MathUtils {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate((float)Math.toRadians(rx),1.0f,0.0f,0.0f);
		matrix.rotate((float)Math.toRadians(ry),0.0f,1.0f,0.0f);
		matrix.rotate((float)Math.toRadians(rz),0.0f,0.0f,1.0f);
		matrix.scale(scale);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
	
		Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), 1,0,0);
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), 0,1,0);
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), 0,0,1);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(cameraPos.x,-cameraPos.y,-cameraPos.z);
        viewMatrix.translate(negativeCameraPos);
        return viewMatrix;
	}
	
	
	public static float barryCentricInterpolation(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	
	
}
