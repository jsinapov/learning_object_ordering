package learning;

import java.util.ArrayList;
import java.util.Random;

import utils.UtilsJS;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.AttributeStats;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class BalancedEnsembleClassifier implements Classifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2770702027345338260L;
	Classifier C_template;
	ArrayList<Classifier> C_list;
	int ensemble_N;
	double ensemble_f;
	
	int num_class_values;
	
	boolean useAllPositive = true;
	String positiveLabel = "+1";
	
	public BalancedEnsembleClassifier(int N, double fraction, Classifier C){
		C_list = new ArrayList<Classifier>();
		C_template = C;
		ensemble_N = N;
		ensemble_f = fraction;
	}
	
	public double [] distributionForInstance(Instance input){
		double [] d = new double[num_class_values];
		for (int i =0; i < d.length; i++)
			d[i]=0.0;
		
		for (int i = 0; i < C_list.size(); i++){
			try {
				double [] d_i = C_list.get(i).distributionForInstance(input);
				
				for (int j = 0; j < d_i.length; j++)
					d[j]+=d_i[j];
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (Utils.sum(d) > 0.0)
			Utils.normalize(d);
		else {
			System.out.println("Sum is zero on instance: "+input.toString());
		}
		
		
		return d;
	}
	
	public void buildClassifier(Instances data) throws Exception {
		
		//find class values
		num_class_values = data.attribute(data.numAttributes()-1).numValues();
		ArrayList<String> class_values = new ArrayList<String>();
		for (int i = 0; i < num_class_values; i ++)
			class_values.add(data.attribute(data.numAttributes()-1).value(i));
		
		
		if (useAllPositive){
			Instances positive_data = new Instances(data);
			Instances negative_data = new Instances(data);
			positive_data.delete();
			negative_data.delete();
			
			for (int i = 0; i < data.numInstances();i++){
				Instance inst_i = data.instance(i);
				if (inst_i.stringValue(inst_i.classAttribute()).equals(positiveLabel))
					positive_data.add(inst_i);
				else
					negative_data.add(inst_i);
			}
			
			for (int i = 0; i < ensemble_N; i ++){
				
				Instances data_i = new Instances(positive_data);
				Instances negative_subset = UtilsJS.randomInstancesSubset(negative_data,data_i.numInstances()*2,i);
			
				System.out.print("\tC_"+i+" ("+data_i.numInstances()+" "+negative_subset.numInstances());
				
				for (int j = 0; j < negative_subset.numInstances(); j++){
					Instance x_j = negative_subset.instance(j);
					x_j.setDataset(data_i);
					data_i.add(x_j);
				}
				
				Classifier C_i = AbstractClassifier.makeCopy(C_template);
			//	AbstractClassifier.makeCopy(model)
				C_i.buildClassifier(data_i);
				C_list.add(C_i);
				
			}
			System.out.println();
		}
		else {
		
		
			AttributeStats S = data.attributeStats(data.numAttributes()-1);
		//	System.out.println(S.toString());
			int [] counts = S.nominalCounts;
			int total = data.numInstances();
			
			double [] class_w = new double[num_class_values];
			for (int i = 0; i < class_w.length; i++){
				class_w[i]=((1.0)/( (double) class_w.length) ) / (double)counts[i];
				//System.out.println(counts[i]+"\t"+total+"\t"+class_w[i]);
			}
			
			double [] data_weights = new double[data.numInstances()];
			for (int i = 0; i < data.numInstances(); i ++){
				int c_index = class_values.indexOf(data.instance(i).stringValue(data.numAttributes()-1));
				data_weights[i]=class_w[c_index];
			}
			
			int train_N = (int)Math.round((double)data.numInstances() * ensemble_f);
			
			for (int i = 0; i < ensemble_N; i ++){
				
				Instances resampled_i = data.resampleWithWeights(new Random(i), data_weights);
				//data_i.stratify(data_i.numInstances());
				
				//System.out.println(data_i.toString());
				
				Instances data_i = new Instances(resampled_i);
				data_i.delete();
				for (int j = 0; j < train_N; j++){
					data_i.add(resampled_i.instance(j));
				}
				
				Classifier C_i = AbstractClassifier.makeCopy(C_template);
				C_i.buildClassifier(data_i);
				C_list.add(C_i);
				
				System.out.print("\tC_ "+i+" ("+data_i.numInstances()+")");
			}
			System.out.println();
		}
	}

	@Override
	public double classifyInstance(Instance arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Capabilities getCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

}
