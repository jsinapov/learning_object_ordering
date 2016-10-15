package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utils.UtilsJS;

public class ObjectInfo {

	public ArrayList<String> object_names;
	public int [] object_ids;
	public double [] object_weights;
	public double [] object_widths;
	public double [] object_heights;
	
	public ObjectInfo(String filename){
		try {
			BufferedReader BR = new BufferedReader(new FileReader(new File(filename)));
			
			//first line is header
			BR.readLine();
			
			ArrayList<String> lines = new ArrayList<String>();
			
			while (true){
				String line = BR.readLine();
				
				if (line == null)
					break;
				else 
					lines.add(line);
			}
			
			object_weights = new double[lines.size()];
			object_widths = new double[lines.size()];
			object_heights = new double[lines.size()];
			object_ids  = new int[lines.size()];
			object_names = new ArrayList<String>();
			for (int i = 0; i < lines.size(); i ++){
			
				
				String [] tokens = lines.get(i).split(",");
				
				if (tokens.length > 1){
					object_ids[i] = Integer.parseInt(tokens[0]);
					object_names.add(tokens[2]);
					
					object_heights[i]=Double.parseDouble(tokens[3]);
					object_widths[i]=Double.parseDouble(tokens[4]);
					object_weights[i]=Double.parseDouble(tokens[5]);
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String getObjectTag(int id){
		for (int i = 0; i < object_ids.length; i++){
			if (object_ids[i] == id){
				return new String("weight"+(int)object_weights[i]+"_height"+(int)object_heights[i]+"_width"+(int)object_widths[i]);
			}
		}
		
		return null;
	}
	
	public String getObjectName(int id){
		for (int i = 0; i < object_ids.length; i++){
			if (object_ids[i] == id){
				return object_names.get(i);
			}
		}
		
		return null;
	}
	
	public double getObjectPropertyValue(int object_id, String property){
		int index = UtilsJS.indexOf(object_ids, object_id);
		
		if (property == "weight"){
			return object_weights[index];
		}
		else if (property == "width"){
			return object_widths[index];
		}
		else {
			return object_heights[index];
		}
	}
	
	public void print(){
		for (int i = 0; i < object_names.size(); i++){
			System.out.println(object_ids[i]+","+object_names.get(i)+","+object_heights[i]+","+object_widths[i]+","+object_weights[i]);
		}
		
	}
}
