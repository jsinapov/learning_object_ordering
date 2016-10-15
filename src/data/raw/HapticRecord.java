package data.raw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utils.UtilsJS;

public class HapticRecord {

	double [] time_stamps_array;
	double [][] joint_pos_array;//first index is time, second is joint
	double [][] joint_effort_array;//first index is time, second is joint
	double [][] finger_pos_array;
	double [][] tool_array;
	
	public String behavior;
	public int trial;
	public int object;
	
	public HapticRecord(){
		
	}
	
	public double [][] getFingers(){
		return finger_pos_array;
	}
	
	public double [][] getEfforts(){
		return joint_effort_array;
	}
	

	public double [][] getPositions(){
		return joint_pos_array;
	}
	
	public void setMetaInfo(String b, int t, int o){
		behavior = b;
		trial = t;
		object = o;
	}
	
	public void print(){
		System.out.println("Meta: "+behavior+"\t"+trial+"\t"+object);
		
		System.out.println("Timestamps:");
		UtilsJS.printArray(time_stamps_array);
		
		System.out.println("\nEfforts:");
		UtilsJS.printMatrix(joint_effort_array);
		
		System.out.println("\nPositions:");
		UtilsJS.printMatrix(joint_pos_array);
		
		System.out.println("\nFingers:");
		UtilsJS.printMatrix(finger_pos_array);
	}
	
	public void loadFromFile(String filename){
		ArrayList<Double> timestamps;
		ArrayList<double[]> joint_efforts;
		ArrayList<double[]> joint_positions;
		ArrayList<double[]> finger_positions;
		
		//7 dimensional vectors: x,y,z,q_x,q_y,q_z,q_w
		ArrayList<double[]> tool_orientations;
		
		timestamps = new ArrayList<Double>();
		joint_efforts = new ArrayList<double[]>();
		joint_positions = new ArrayList<double[]>();
		finger_positions = new ArrayList<double[]>();
		tool_orientations = new ArrayList<double[]>();
		
		
		try {
			BufferedReader BR = new BufferedReader(new FileReader(new File(filename)));
			
			//header
			String line = BR.readLine();
			
			while (true){
				line = BR.readLine();
				
				if (line == null)
					break;
				
				String [] tokens = line.split(",");
				
				if (tokens.length == 22){
					
					int c = 0;
					
					double [] j_pos = new double[6];
					double [] j_eff = new double[6];
					double [] tool_pose = new double[7];
					double [] f_pos = new double[2];
					Double time_stamp;
					
					for (int i = 0; i < 6; i ++){
						j_eff[i] = Double.parseDouble(tokens[c]);
						c++;
					}
					
					for (int i = 0; i < 6; i ++){
						j_pos[i] = Double.parseDouble(tokens[c]);
						c++;
					}
					
					for (int i = 0; i < 7; i ++){
						tool_pose[i] = Double.parseDouble(tokens[c]);
						c++;
					}
					
					for (int i = 0; i < 2; i ++){
						f_pos[i] = Double.parseDouble(tokens[c]);
						c++;
					}
					
					time_stamp = new Double(Double.parseDouble(tokens[c]));
					
					timestamps.add(time_stamp);
					joint_efforts.add(j_eff);
					joint_positions.add(j_pos);
					finger_positions.add(f_pos);
					tool_orientations.add(tool_pose);
				}
			}
			
			time_stamps_array = new double[timestamps.size()];
			joint_pos_array = new double[timestamps.size()][];
			joint_effort_array = new double[timestamps.size()][];
			finger_pos_array = new double[timestamps.size()][];
			tool_array = new double[timestamps.size()][];
			
			for (int i = 0; i < timestamps.size(); i++){
				time_stamps_array[i]=timestamps.get(i).doubleValue();
				joint_pos_array[i]=joint_positions.get(i);
				joint_effort_array[i]=joint_efforts.get(i);
				finger_pos_array[i]=finger_positions.get(i);
				tool_array[i]=tool_orientations.get(i);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
