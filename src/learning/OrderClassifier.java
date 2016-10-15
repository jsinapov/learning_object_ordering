package learning;

import java.util.ArrayList;
import java.util.Random;

import utils.UtilsJS;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import data.ObjectInfo;
import data.orders.MOrder;
import data.orders.MOrderDB;
import data.orders.OrderDistUtils;

public class OrderClassifier {
	
	Classifier C;
	String c_string;
	Instances weka_data;

	public OrderClassifier(){
		
	}
	
	public void setClassifier(String classifier){
		c_string = classifier;
		
		if (c_string == "knn"){
			C = new IBk(10);
		}
		else if (c_string == "C4.5"){
			C = new J48();
		}
		else if (c_string == "SVMpoly"){
			C = new SMO();
			((SMO)C).setBuildLogisticModels(true);
		}
		else if (c_string == "BoostedC4.5"){
			C = new AdaBoostM1();
			((AdaBoostM1)C).setClassifier(new J48());
		}
		else if (c_string == "BoostedSVMpoly"){
			SMO C_smo = new SMO();
			C_smo.setBuildLogisticModels(true);
			
			C = new AdaBoostM1();
			((AdaBoostM1)C).setClassifier(C_smo);
		}
		else if (c_string == "BaggedC4.5"){
			C = new Bagging();
			((Bagging)C).setClassifier(new J48());
		}
		else if (c_string == "BalancedC4.5"){
			C = new BalancedEnsembleClassifier(20,0.8,new J48());
		}
	}
	
	
	public double [] generateFeatures(MOrder m_i, MOrder m_i_reversed, MOrderDB mDB){
		double [] f_i = new double[mDB.getContexts().size()];
		ArrayList<String[]> contexts = mDB.getContexts();		
		
	
		
		for (int c = 0; c < contexts.size(); c++){
			MOrder m_c = mDB.getOrderForContext(contexts.get(c)).getSubsetOrder(m_i.getIDs());
			//System.out.println("For context "+c+":");
			//m_c.print();
					
			double f_c =OrderDistUtils.averageHeightDiffNorm(m_i, m_c);
			double f_c_reversed =OrderDistUtils.averageHeightDiffNorm(m_i_reversed, m_c);
			f_i[c] = Math.min(f_c, f_c_reversed);
			
			//test reverse
			
		}
		
		return f_i;
	}
	
	public Instances getWekaData(){
		return weka_data;
	}
	
	public void crossValidate(){
		try {
			Evaluation EV_cv = new Evaluation(weka_data);
			EV_cv.crossValidateModel(C, weka_data, 5, new Random(1));
			System.out.println(EV_cv.toSummaryString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void train(Instances train_data){
		if (c_string == "SVMpoly"){
			PolyKernel K;
			try {
				K = new PolyKernel(train_data,250007,2.0,false);
				((SMO)C).setKernel(K);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			C.buildClassifier(train_data);
			
			if (c_string == "C4.5"){
				System.out.println(((J48)C).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void train(){
		if (c_string == "SVMpoly"){
			PolyKernel K;
			try {
				K = new PolyKernel(weka_data,250007,2.0,false);
				((SMO)C).setKernel(K);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (c_string == "BoostedSVMpoly"){
			PolyKernel K;
			try {
				K = new PolyKernel(weka_data,250007,2.0,false);
				SMO C_smo = new SMO();
				C_smo.setBuildLogisticModels(true);
				
				C_smo.setKernel(K);
				
				((AdaBoostM1)C).setClassifier(C_smo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			C.buildClassifier(weka_data);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Classifier getClassifier(){
		return C;
	}
	
	public Instance createTestInstance(int [] example_order, ObjectInfo OI, MOrderDB mDB, String label){
		MOrder m_i = new MOrder(example_order,OI);
		MOrder m_i_reversed = new MOrder(UtilsJS.reverseIntegerArray(example_order),OI);
		double [] x_i = this.generateFeatures(m_i, m_i_reversed, mDB);
	
		Instance inst_i = new DenseInstance(weka_data.numAttributes());
		inst_i.setDataset(weka_data);
		
		for (int j = 0; j < x_i.length; j++)
			inst_i.setValue(j, x_i[j]);
		inst_i.setClassValue(label);
		
		return inst_i;
	}
	

	public Instances createData(ArrayList<int[]> positive_orders, ArrayList<int[]> negative_orders, 
			ObjectInfo OI, MOrderDB mDB){
		
		ArrayList<MOrder> positive_examples = new ArrayList<MOrder>();
		ArrayList<MOrder> positive_examples_reversed = new ArrayList<MOrder>();
		for (int i = 0; i < positive_orders.size(); i++){
			MOrder m_i = new MOrder(positive_orders.get(i),OI);
			positive_examples.add(m_i);
			
			MOrder m_i_reversed = new MOrder(UtilsJS.reverseIntegerArray(positive_orders.get(i)),OI);
			positive_examples_reversed.add(m_i_reversed);
		}
		
		ArrayList<MOrder> negative_examples = new ArrayList<MOrder>();
		ArrayList<MOrder> negative_examples_reversed = new ArrayList<MOrder>();
		for (int i = 0; i < negative_orders.size(); i++){
			MOrder m_i = new MOrder(negative_orders.get(i),OI);
			negative_examples.add(m_i);
			
			MOrder m_i_reversed = new MOrder(UtilsJS.reverseIntegerArray(negative_orders.get(i)),OI);
			negative_examples_reversed.add(m_i_reversed);
		}
		
		
		//generate features
		ArrayList<double[]> feature_vectors = new ArrayList<double[]>();
		ArrayList<String> class_labels = new ArrayList<String>();
		ArrayList<String> class_label_set = new ArrayList<String>();
		class_label_set.add("-1");
		class_label_set.add("+1");
		
		for (int i = 0; i < positive_examples.size();i++){
			MOrder m_i = positive_examples.get(i);
			MOrder m_i_reversed = positive_examples_reversed.get(i);
			
			double [] f_i = this.generateFeatures(m_i, m_i_reversed, mDB);
			feature_vectors.add(f_i);
			class_labels.add("+1");
		}
		
		for (int i = 0; i < negative_examples.size();i++){
			MOrder m_i = negative_examples.get(i);
			MOrder m_i_reversed = negative_examples_reversed.get(i);
			
			double [] f_i = this.generateFeatures(m_i, m_i_reversed, mDB);
			feature_vectors.add(f_i);
			class_labels.add("-1");
		}
		
		return UtilsJS.toInstances(feature_vectors, class_labels, class_label_set);
		
		//return weka_data;
	//	System.out.println(weka_data);
	}

	public Instances setData(ArrayList<int[]> positive_orders, ArrayList<int[]> negative_orders, 
			ObjectInfo OI, MOrderDB mDB){
		
		ArrayList<MOrder> positive_examples = new ArrayList<MOrder>();
		ArrayList<MOrder> positive_examples_reversed = new ArrayList<MOrder>();
		for (int i = 0; i < positive_orders.size(); i++){
			MOrder m_i = new MOrder(positive_orders.get(i),OI);
			positive_examples.add(m_i);
			
			MOrder m_i_reversed = new MOrder(UtilsJS.reverseIntegerArray(positive_orders.get(i)),OI);
			positive_examples_reversed.add(m_i_reversed);
		}
		
		ArrayList<MOrder> negative_examples = new ArrayList<MOrder>();
		ArrayList<MOrder> negative_examples_reversed = new ArrayList<MOrder>();
		for (int i = 0; i < negative_orders.size(); i++){
			MOrder m_i = new MOrder(negative_orders.get(i),OI);
			negative_examples.add(m_i);
			
			MOrder m_i_reversed = new MOrder(UtilsJS.reverseIntegerArray(negative_orders.get(i)),OI);
			negative_examples_reversed.add(m_i_reversed);
		}
		
		
		//generate features
		ArrayList<double[]> feature_vectors = new ArrayList<double[]>();
		ArrayList<String> class_labels = new ArrayList<String>();
		ArrayList<String> class_label_set = new ArrayList<String>();
		class_label_set.add("-1");
		class_label_set.add("+1");
		
		for (int i = 0; i < positive_examples.size();i++){
			MOrder m_i = positive_examples.get(i);
			MOrder m_i_reversed = positive_examples_reversed.get(i);
			
			double [] f_i = this.generateFeatures(m_i, m_i_reversed, mDB);
			feature_vectors.add(f_i);
			class_labels.add("+1");
		}
		
		for (int i = 0; i < negative_examples.size();i++){
			MOrder m_i = negative_examples.get(i);
			MOrder m_i_reversed = negative_examples_reversed.get(i);
			
			double [] f_i = this.generateFeatures(m_i, m_i_reversed, mDB);
			feature_vectors.add(f_i);
			class_labels.add("-1");
		}
		
		//get list of contexts to serve as attribute names
	
		
		weka_data = UtilsJS.toInstances(feature_vectors, class_labels, class_label_set,mDB.getContextsNames());
		
		return weka_data;
	//	System.out.println(weka_data);
	}
	
	
}


