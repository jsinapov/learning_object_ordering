package data.contextmodel;

public class DataPointBM {

	String [] context;
	String context_name;
	
	String object;
	int object_id;
	int trial;
	double [] features;
	
	public DataPointBM(String [] c, String o, int o_id, int t, double [] f){
		this.context=c;
		this.object=o;
		this.object_id=o_id;
		this.trial=t;
		this.features=f;
		
		context_name = DataUtils.contextString(this.context);
	}
	
	public String getContextName(){
		return context_name;
	}
	
	public String[] getContext() {
		return context;
	}

	public String getObject() {
		return object;
	}
	
	public int getObjectID() {
		return object_id;
	}

	public int getTrial() {
		return trial;
	}

	public double[] getFeatures() {
		return features;
	}
	
	public double getFeatureAt(int i){
		return features[i];
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.object+"\t"+this.trial+"\t");
		for (int i = 0; i < features.length; i++)
			sb.append(features[i]+"\t");
		
		return sb.toString();
	}
	

}
