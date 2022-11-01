package engine;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback{

	public static boolean[] keys = new boolean[65536];
	
	
	
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action !=GLFW_RELEASE;
	}
	
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}

	
	
	
	
	
	
	
	
	
}


