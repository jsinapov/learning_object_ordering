package data.contextmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import data.ObjectInfo;
import utils.UtilsJS;
import weka.attributeSelection.PrincipalComponents;
import weka.core.Instances;

public class DataUtils {
	
	public static ArrayList<String> shuffleList(ArrayList<String> elements, int rSeed){
		Random R = new Random(rSeed);
		
		ArrayList<String> shuffled = new ArrayList<String>();
		ArrayList<String> list_copy = new ArrayList<String>();
		list_copy.addAll(elements);
		
		while (list_copy.size() > 0){
			int r_int = R.nextInt(list_copy.size());
			shuffled.add(list_copy.get(r_int));
			list_copy.remove(r_int);
		}
		
		return shuffled;
	}
	
	public static ArrayList<DataPointBM> getDataFromContext(ArrayList<DataPointBM> data, String context){
		ArrayList<DataPointBM> subset = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < data.size(); i++){
			if (data.get(i).context_name.equals(context))
				subset.add(data.get(i));
		}
		
		return subset;
	}
	
	public static ArrayList<DataPointBM> getContextData(ArrayList<InteractionTrial> trials, ContextData CD, String context){
		ArrayList<DataPointBM> data = new ArrayList<DataPointBM>();
		
		for (int i = 0; i < trials.size(); i++){
			if (trials.get(i).getTrialContexts().contains(context)){
				data.add(CD.getDataPoint(trials.get(i).getObject(), trials.get(i).getTrialIndex()));
			}
		}
		
		return data;
	}
	
	public static ArrayList<InteractionTrial> generateTrialsMR(ArrayList<String> objects, 
						ArrayList<String[]> contexts, int num_trials){
		ArrayList<InteractionTrial> trials = new ArrayList<InteractionTrial>();
		
		ArrayList<String> context_names = new ArrayList<String>();
		for (int c= 0; c < contexts.size(); c++){
			context_names.add(contextString(contexts.get(c)));
		}
		
		for (int t = 0; t < num_trials; t++){
			for (int o = 0; o < objects.size(); o++){
				InteractionTrial T = new InteractionTrial(objects.get(o),t,context_names);
				trials.add(T);
			}
		}
		
		
		return trials;
	}

	public static ArrayList<InteractionTrial> getTrialsWithObjects(ArrayList<InteractionTrial> trials,ArrayList<String> objects){
		ArrayList<InteractionTrial> subset = new ArrayList<InteractionTrial>();
		for (int i = 0; i < trials.size(); i++){
			if (objects.contains(trials.get(i).getObject())){
				subset.add(trials.get(i));
			}
		}
		
		return subset;
	}
	
	public static ArrayList<DataPointBM> getDataWithObject(ArrayList<DataPointBM> data, String object){
		ArrayList<DataPointBM> subset = new ArrayList<DataPointBM>();
		
		for (DataPointBM x : data){
			if (object.equals(x.getObject()))
				subset.add(x);
		}
		
		return subset;
	}
	
	public static ArrayList<DataPointBM> getDataWithObjects(ArrayList<DataPointBM> data, ArrayList<String> objects){
		ArrayList<DataPointBM> subset = new ArrayList<DataPointBM>();
		
		for (DataPointBM x : data){
			if (objects.contains(x.getObject()))
				subset.add(x);
		}
		
		return subset;
	}
	
	public static ArrayList<InteractionTrial> getTrialsWithObject(ArrayList<InteractionTrial> trials,String object){
		ArrayList<InteractionTrial> subset = new ArrayList<InteractionTrial>();
		for (int i = 0; i < trials.size(); i++){
			if (object.equals(trials.get(i).getObject())){
				subset.add(trials.get(i));
			}
		}
		
		return subset;
	}
	
	public static boolean validCombination(String b, String m){
		if (m == "effort")
			return true;
		else if (m == "position")
			return true;
		else if (m == "fingers" && b == "grasp")
			return true;
		else if (m == "endfingers" && b == "grasp")
			return true;
		else return false;
	}
	
	public static String contextString(String [] c){
		return new String(c[0]+"-"+c[1]);
	}
	
	public static ArrayList<String> getObjectsMR(){
		String [] weights = {"light","medium","heavy"};
		String [] colors = {"brown","green","blue"};
		String [] contents = {"glass","rice","beans","screws"};
		
		ArrayList<String> objects = new ArrayList<String>();
		
		for (int w = 0; w < weights.length; w++){
			for (int c = 0; c < colors.length; c++){
				for (int s = 0; s < contents.length;s++){
					String object_i = new String(weights[w]+"_"+colors[c]+"_"+contents[s]);
					objects.add(object_i);
				}
			}
		}
		
		
		
		return objects;

	}
	
	public static ContextDataDB loadDB(String path, ArrayList<String[]> contexts, 
												ObjectInfo OI, boolean doPCA, double pca_coverage){
		ContextDataDB CDB = new ContextDataDB();
		
		ArrayList<String> contexts_strings = new ArrayList<String>();
		for (int  c= 0; c < contexts.size(); c++){
			String [] context = contexts.get(c);
			String context_string = contextString(context);
			contexts_strings.add(context_string);
		}
		
		CDB.setContexts(contexts_strings);
		
		
		
		for (int  c= 0; c < contexts.size(); c++){
			String [] context = contexts.get(c);
			String filename = new String(path+""+context[0]+"_"+context[1]+".txt");
			//System.out.println(filename);
			
			int dim = -1;
			
			ArrayList<DataPointBM> data = new ArrayList<DataPointBM>();
			
			
			try {
				BufferedReader BR = new BufferedReader(new FileReader(new File(filename)));
				
				while (true){
					String line = BR.readLine();
					if (line==null)
						break;
					
					String [] tokens = line.split(",");
					if (dim == -1)
						dim = tokens.length-2;
					
					int object_id = Integer.parseInt(tokens[0]);
				
					if (OI.getObjectName(object_id) != null){
						int trial = Integer.parseInt(tokens[1]);
						double [] f = new double[dim];
						for (int i = 0; i < f.length; i++){
							f[i]=Double.parseDouble(tokens[i+2]);
						}
						
						DataPointBM dp = new DataPointBM(context,OI.getObjectName(object_id),object_id,trial,f);
						
						data.add(dp);
					}
					else {
						System.out.println("Object id "+object_id+" not in ObjectInfo");
					}
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//compute PCA
			if (doPCA){
				int start_dim = data.get(0).features.length;
				ArrayList<double[]> f_vectors = new ArrayList<double[]>();
				for (int k = 0; k < data.size(); k++){
					f_vectors.add(data.get(k).features);
				}
				Instances weka_pca_data = UtilsJS.toInstances(f_vectors);
				Instances weka_data = UtilsJS.toInstances(f_vectors);
				PrincipalComponents PCA = new PrincipalComponents();
				PCA.setVarianceCovered(pca_coverage);
				try {
					PCA.buildEvaluator(weka_pca_data);
					weka_data = PCA.transformedData(weka_data);
				//	System.out.println(weka_data.toString());
					ArrayList<double[]> feature_vectors_pca = UtilsJS.toFeatureVectors(weka_data);
					dim=feature_vectors_pca.get(0).length;
					
					System.out.println("\tPCA for "+contexts_strings.get(c)+": "+start_dim +"->"+dim);
				
					for (int k = 0; k < data.size(); k++){
						data.get(k).features=feature_vectors_pca.get(k);
						//System.out.println(data.get(k).toString());
					}
					
					
	
				} catch (Exception e) {
					//this happens with hold and flow since all values are 0
					
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			CDB.setContextData(contexts_strings.get(c), data,dim);
			
			//System.out.println("Adding "+data.size()+" datapoints for context "+contexts_strings.get(c));
		}
		
		
		return CDB;
	}
}
