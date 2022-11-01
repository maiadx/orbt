package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class MapLoader {
	Scanner scan;
	ArrayList<String[]> allObjsData = new ArrayList<String[]>();
	
	
	public ArrayList<String[]> loadMapData(String filePath) {
		String strType = "";
		String strObjFile = "";
		String strTexFile = "";
		String strXPos = "";
		String strYPos = "";
		String strZPos = "";
		String strRotX = "";
		String strRotY = "";
		String strRotZ = "";
		String strScale = "";
		try {
			scan = new Scanner(new File("res/maps/"+filePath+".juno"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Missing game configuration file, failed to load : " + filePath);
			
		}
		while(scan.hasNextLine()) {
		String line = scan.nextLine();
		if(line.charAt(0) == '#') {
			continue;
		}
		String[] info = line.split(" ");
		if(info.length < 9) {
			continue;
		}
		
		allObjsData.add(info);
		
		
		}
		return allObjsData;
		
		
	}
	
	public int getNumObjs() {
		return allObjsData.size();
	}
	/*
	public Player loadPlayer() {
		
	}
	
	public ArrayList loadMap() {
		
	}
	
	public ArrayList loadSkybox() {
		
	}
	
	*/
	
	
}
