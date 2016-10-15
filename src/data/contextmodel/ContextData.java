package data.contextmodel;

import java.util.ArrayList;
import java.util.HashMap;

public class ContextData {

	public String context_string;
	public ArrayList<DataPointBM> datapoints;
	public int dataDim;
	
	public HashMap<String,DataPointBM> datamap;
	boolean hasMap = false;
	
	public String getKey(String object, int trial){
		return new String(object+"_t"+trial);
	}
	
	public ArrayList<DataPointBM> getDataWithObject(String object){
		ArrayList<DataPointBM> data  = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < datapoints.size(); i++){
			if (datapoints.get(i).object == object)
				data.add(datapoints.get(i));
		}
		
		return data;
	}
	
	public ArrayList<DataPointBM> getDataWithoutObject(String object){
		ArrayList<DataPointBM> data  = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < datapoints.size(); i++){
			if (datapoints.get(i).object != object)
				data.add(datapoints.get(i));
		}
		
		return data;
	}
	
	public DataPointBM getDataPoint(String object, int trial){
		if (hasMap){
			String key = this.getKey(object,trial);
			return datamap.get(key);
		}
		else {
			for (int i = 0; i < datapoints.size();i++){
				if (datapoints.get(i).getObject().equals(object) &&
						datapoints.get(i).getTrial()==trial)
					return datapoints.get(i);
			}
		}
		
		return null;
	}
	
	public void buildMap(){
		datamap=new HashMap<String,DataPointBM>();
		for (int i = 0; i < datapoints.size(); i++){
			String key = this.getKey(datapoints.get(i).getObject(), datapoints.get(i).getTrial());
			datamap.put(key, datapoints.get(i));
		}
		hasMap=true;
	}
}
