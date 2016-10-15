package data;

import java.io.File;
import java.util.ArrayList;

import data.raw.HapticRecord;

public class DataLoader {

	
	
	
	public static ArrayList<HapticRecord> loadHapticDataset(String data_path, ObjectInfo OI,
																String behavior, int num_trials){
		ArrayList<HapticRecord> data = new ArrayList<HapticRecord>();
		
		for (int o = 0; o < OI.object_ids.length; o++){
			int object_id = OI.object_ids[o];
			
			for (int t = 1; t <= num_trials; t++){
				HapticRecord hr = loadHapticRecord(data_path,t,object_id,behavior);
				data.add(hr);
			}
		}
		
		
		return data;
	}
	
	public static HapticRecord loadHapticRecord(String path, int trial, int object, String behavior){
		HapticRecord HR = new HapticRecord();
		
		String filename = new String(path+"t"+trial+"/obj_"+object+"/trial_1/"+behavior+"/haptic_data");
		
		File F = new File(filename);
		File [] files = F.listFiles();
		
		for (int i = 0; i < files.length; i++){
			if (files[i].getName().length() > 3){
				HR.loadFromFile(files[i].getAbsolutePath());
				HR.setMetaInfo(behavior, trial, object);
				break;
			}
		}
		
		return HR;
	}
	
	

}
