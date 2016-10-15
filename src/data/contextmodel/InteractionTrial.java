package data.contextmodel;

import java.util.ArrayList;

public class InteractionTrial {

	String object;
	int trial_id;
	ArrayList<String> trial_contexts;
	
	public InteractionTrial(String o, int t, ArrayList<String> c){
		object=o;
		trial_id = t;
		trial_contexts = c;
	}
	
	public int getTrialIndex(){
		return trial_id;
	}
	
	public String getObject(){
		return object;
	}
	
	public ArrayList<String> getTrialContexts(){
		return trial_contexts;
	}
}
