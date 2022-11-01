package environment;

import org.joml.Vector3f;

import graphicsEngine.Obj;

public class ActiveObject extends BasicObject{

	public ActiveObject(Obj texturedObj, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(texturedObj, position, rotX, rotY, rotZ, scale);

	}

}
