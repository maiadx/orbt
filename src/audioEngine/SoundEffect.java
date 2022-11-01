package audioEngine;

public class SoundEffect implements Runnable{
// this is 2D sound only
	Thread thread;
	
	
	
	
	
	
	public void start(String soundFile) {
		thread = new Thread(this,"sound");
		thread.start();
		run();
	}






	public void run() {
		
	}
}
