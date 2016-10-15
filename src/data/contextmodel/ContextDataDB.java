package data.contextmodel;

import java.util.ArrayList;

public class ContextDataDB {
	
	int num_trials;
	ArrayList<String> contexts;
	ArrayList<ContextData> contexts_data;
	
	public ContextDataDB(){
		
	}
	
	public ArrayList<String> getContextsNames(){
		return contexts;
	}
	
	public ContextData getConextData(String context){
		return contexts_data.get(contexts.indexOf(context));
	}
	
	public int getContextFeatureDim(String context){
		int index = contexts.indexOf(context);
		return contexts_data.get(index).dataDim;
	}
	
	public void setContexts(ArrayList<String> c_list){
		contexts = new ArrayList<String>();
		contexts.addAll(c_list);
		contexts_data = new ArrayList<ContextData>();
		
		for (int c=0; c < contexts.size(); c++){
			ContextData D_c = new ContextData();
			D_c.context_string=contexts.get(c);
			D_c.datapoints=new ArrayList<DataPointBM>();
			contexts_data.add(D_c);
		}
	}
	
	public void setContextData(String context, ArrayList<DataPointBM> data, int dim){
		int index = contexts.indexOf(context);
		if (index < 0){
			System.out.println("Context "+context+" doesn't exist!");
			return;
		}
		
		for (int i = 0; i < data.size(); i++){
			contexts_data.get(index).datapoints.add(data.get(i));
			contexts_data.get(index).dataDim=dim;
		}
		
		contexts_data.get(index).buildMap();
	}
	
	public ArrayList<DataPointBM> getDataForTrial(InteractionTrial T){
		String object = T.getObject();
		int trial = T.getTrialIndex();
		
		ArrayList<DataPointBM> data = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < T.getTrialContexts().size();i++){
			int index = contexts.indexOf(T.getTrialContexts().get(i));
			data.add(contexts_data.get(index).getDataPoint(object, trial));
			
		}
		
		return data;
	}
	
	public ArrayList<DataPointBM> getDataForTrials(ArrayList<InteractionTrial> trials){
		
		ArrayList<DataPointBM> data = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < trials.size();i++){
			data.addAll(this.getDataForTrial(trials.get(i)));
		}
		
		return data;
	}
	
	public ArrayList<DataPointBM> getDataWithObject(String context, String object){
		ArrayList<DataPointBM> subset = new ArrayList<DataPointBM>();
		int index = contexts.indexOf(context);
		for (int i = 0; i < contexts_data.get(index).datapoints.size();i++){
			if (contexts_data.get(index).datapoints.get(i).getObject().equals(object))
				subset.add(contexts_data.get(index).datapoints.get(i));
			
		}
		return subset;
	}
	
	public DataPointBM getDataPoint(String context, String object, int trial){
		int index = contexts.indexOf(context);
		for (int i = 0; i < contexts_data.get(index).datapoints.size();i++){
			if (contexts_data.get(index).datapoints.get(i).getObject().equals(object) &&
					contexts_data.get(index).datapoints.get(i).getTrial()==trial)
				return contexts_data.get(index).datapoints.get(i);
		}
		
		return null;
	}
	
}
