package exp;

import java.util.ArrayList;
import java.util.Random;

import learning.OrderClassifier;
import learning.OrderOracle;
import utils.PermutationGenerator;
import utils.UtilsJS;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import data.DataLoader;
import data.ObjectInfo;
import data.contextmodel.ContextData;
import data.contextmodel.ContextDataDB;
import data.contextmodel.DataPointBM;
import data.contextmodel.DataUtils;
import data.orders.MOrder;
import data.orders.MOrderDB;
import data.raw.HapticRecord;
import features.EffortFeatureGenerator;
import features.EndFingerPositionFeatureGenerator;
import features.FingerPositionFeatureGenerator;
import features.IHapticFeatureGenerator;

public class OrderingFeatureEXP {

	final static String object_info_file = "/home/jsinapov/research/datasets/object_ordering/objects32.csv";
	final static String data_path = "/home/jsinapov/research/datasets/object_ordering/";
	final static String feature_data_path = "/home/jsinapov/research/datasets/object_ordering/features/";
	final static String matrices_data_path = "/home/jsinapov/research/datasets/object_ordering/matrices/";
	final static String matlab_orders_path = "/home/jsinapov/research/datasets/object_ordering/matlab_orders/S4/";
	
	final static String [] behaviors = {"grasp","lift","hold","lower","drop","push","press"};
	final static String [] modalities = {"effort","fingers","endfingers","position"};
	
	final static int num_trials = 5;
	final static int num_temporal_bins = 10;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//step 1: extract feautures
		//featureExtraction();
		
		//step 2: extract relational matrices based on features
		//matrixComputation();
		
		try {
			//standard test
			
			String [] properties = {"weight","width","height"};
			double [] order_thresholds = {50,15,50};
			boolean testOrderInsertion = false;
			orderExpV2("C4.5",properties,order_thresholds,testOrderInsertion,150);
			//orderExpV2("BoostedC4.5",properties,order_thresholds,testOrderInsertion,150);
			//orderExpV2("SVMpoly",properties,order_thresholds,testOrderInsertion,150);
			
			//orderExpV2("BoostedC4.5",properties,order_thresholds,testOrderInsertion,200);
			
			//test for different values of theta_l
			
			/*double [] theta_l_weight = {0,5,10,15,20,25,30,35,40,45,50};
			double [] theta_l_width = {0,1,3,5,7,9,11,13,15};
			double [] theta_l_height = {0,5,10,15,20,25,30,35,40,45,50};
			
			double [] acc_rates_1 = new double[theta_l_weight.length];
			double [] acc_rates_2 = new double[theta_l_width.length];
			double [] acc_rates_3 = new double[theta_l_height.length];
			
			double [] stdv_rates_1 = new double[theta_l_weight.length];
			double [] stdv_rates_2 = new double[theta_l_width.length];
			double [] stdv_rates_3 = new double[theta_l_height.length];
			
			int n_t = 150;
			for (int i = 0; i < theta_l_weight.length; i++){
				String [] prop_1 = {"weight"};
				double [] order_thresholds_1 = {theta_l_weight[i]};
				double [][] r = orderExpV2("BoostedC4.5",prop_1,order_thresholds_1,false,n_t);
				
				acc_rates_1[i]=r[0][0];
				stdv_rates_1[i]=r[0][1];
			}
			
			for (int i = 0; i < theta_l_width.length; i++){
				String [] prop_1 = {"width"};
				double [] order_thresholds_2 = {theta_l_width[i]};
				double [][] r  = orderExpV2("BoostedC4.5",prop_1,order_thresholds_2,false,n_t);
				
				acc_rates_2[i]=r[0][0];
				stdv_rates_2[i]=r[0][1];
			}
			
			for (int i = 0; i < theta_l_height.length; i++){
				String [] prop_1 = {"height"};
				double [] order_thresholds_3 = {theta_l_height[i]};
				double [][] r = orderExpV2("BoostedC4.5",prop_1,order_thresholds_3,false,n_t);
				
				acc_rates_3[i]=r[0][0];
				stdv_rates_3[i]=r[0][1];
			}
			
			System.out.println("\n\n\ntheta_l vs. accuracy (weight):");
			for (int i = 0; i < theta_l_weight.length; i++){
				System.out.println(theta_l_weight[i]+","+acc_rates_1[i]+","+stdv_rates_1[i]);
			}
			
			System.out.println("\n\n\ntheta_l vs. accuracy (width):");
			for (int i = 0; i < theta_l_width.length; i++){
				System.out.println(theta_l_width[i]+","+acc_rates_2[i]+","+stdv_rates_2[i]);
			}
			
			System.out.println("\n\n\ntheta_l vs. accuracy (height):");
			for (int i = 0; i < theta_l_height.length; i++){
				System.out.println(theta_l_height[i]+","+acc_rates_3[i]+","+stdv_rates_3[i]);
			}*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*PermutationGenerator PG = new PermutationGenerator();
		int [] a = {0,1,2,3,4};
		PG.computePermutations(a, a.length);
		ArrayList<int[]> P = PG.retrievePermutations();
		for (int i = 0; i < P.size(); i++){
			UtilsJS.printArray(P.get(i));
		}*/
		
		
		/*for (int i = 0; i < 1000; i ++){
			int [] p_i = PG.next();
			UtilsJS.printArray(p_i);
		}*/
		
		
		/*ArrayList<String[]> contexts = new ArrayList<String[]>();
		ArrayList<String> contexts_string = new ArrayList<String>();
		for (int b = 0; b < behaviors.length;b++){
			for (int m = 0; m < modalities.length; m++){
				if (DataUtils.validCombination(behaviors[b], modalities[m])){
					String [] c_bm = new String[2];
					c_bm[0]=behaviors[b];
					c_bm[1]=modalities[m];
					contexts.add(c_bm);
					
					contexts_string.add(DataUtils.contextString(c_bm));
				}
			}
		}
		
		System.out.print("[");
		for (int i = 0; i < contexts_string.size(); i++){
			System.out.print("'"+contexts_string.get(i)+"'");
			if (i < contexts_string.size()-1)
				System.out.print(",");
			else System.out.print("]");
		}*/
	}
	
	public static double [][] orderExpV2(String classifier_string,
									String [] properties,
									double [] order_thresholds,
									boolean testOrderInsertion,
									int max_train_examples_per_class) throws Exception{
		ObjectInfo OI = new ObjectInfo(object_info_file);
		OI.print();
		ArrayList<String[]> contexts = getContexts();
		
		MOrderDB mDB = new MOrderDB();
		mDB.setContexts(contexts);
		mDB.loadFromPath(matlab_orders_path, OI);
		
		
		
		
		int TOTAL_RUNS =1; //100; //default should be 100
		int num_train = 32;
		int order_length = 5;
		int min_number_train_positive_orders = 50;
		int num_train_examples_per_class = -1;
		
		//double [] negative_example_fitness_range = { (1.0/(double)(order_length-1)), (double)(order_length-2)/(double)(order_length-1) };
		
		
		boolean trainOrderIncreasing = true;
		
		
		
		
		// order_thresholds should be at their max around {50,15,50}
		
		
		double [] order_thresholds_negative_ex = order_thresholds;//{50,15,50};
		
		
		
		//String [] properties = {"width"};
		//double [] order_thresholds = {10};
		//double [] order_thresholds_negative_ex = {10};
		
		
		Evaluation [] EV_prop = new Evaluation[properties.length];
		Evaluation [] EV_prop_test = new Evaluation[properties.length];
		
		int [] correct_insertions = new int[properties.length];
		int [] incorrect_insertions = new int[properties.length];
		for (int i = 0; i < correct_insertions.length; i++){
			correct_insertions[i]=0;
			incorrect_insertions[i]=0;
		}
		
		int [] correct_insertion_recognition = new int[properties.length];
		int [] incorrect_insertion_recognition = new int[properties.length];
		for (int i = 0; i < correct_insertions.length; i++){
			correct_insertion_recognition[i]=0;
			incorrect_insertion_recognition[i]=0;
		}
		
		Random r = new Random(1);
		
		double [][] acc_rates = new double[properties.length][TOTAL_RUNS];
		
		ArrayList<Integer> insertion_errors_weight = new ArrayList<Integer> ();
		ArrayList<Integer> insertion_errors_width = new ArrayList<Integer> ();
		ArrayList<Integer> insertion_errors_height = new ArrayList<Integer> ();
		
		
		for (int i = 1;i <= TOTAL_RUNS;i++){
			System.out.println("\n\nRun #"+i);
			
			Evaluation [] EV_prop_i = new Evaluation[properties.length];
			
			ArrayList<int[]> ids_split = OrderOracle.getTrainTestSplit(OI.object_ids, r, num_train);
			int [] subset_object_ids = ids_split.get(0);
			int [] test_object_ids = ids_split.get(1);
			System.out.println("subset objects:");
			UtilsJS.printArray(subset_object_ids);
			
			System.out.println("test objects:");
			UtilsJS.printArray(test_object_ids);
			
			
			ArrayList<int[]> all_train_orders = OrderOracle.generateOrders(subset_object_ids, order_length);
			System.out.println("Number of possible object orders: "+all_train_orders.size());
			
			
			ArrayList<int[]> all_test_orders = new ArrayList<int[]>();
			if (test_object_ids.length>0)
				all_test_orders = OrderOracle.generateOrders(test_object_ids, order_length);
			System.out.println("Number of possible test object orders: "+all_test_orders.size());
			
			
			ArrayList< ArrayList<int[]> > positive_examples = new ArrayList< ArrayList<int[]> >();
			ArrayList< ArrayList<int[]> > negative_examples = new ArrayList< ArrayList<int[]> >();
			OrderClassifier [] OC_p = new OrderClassifier[properties.length];
			
			

			for (int p = 0; p < properties.length; p++){
				String property = properties[p];
				double order_threshold_diff = order_thresholds[p];
				double order_threshold_diff_neg = order_thresholds_negative_ex[p];
				System.out.println("\nProperty:\t"+property);
				
				ArrayList<int[]> positive_examples_exp = OrderOracle.getPositiveExamples(
											all_train_orders, property, OI, 
											order_threshold_diff, trainOrderIncreasing);
				ArrayList<int[]> negative_examples_exp = OrderOracle.getNegativeExamples(
						all_train_orders, property, OI, 
						order_threshold_diff_neg, trainOrderIncreasing);
				

				if (positive_examples_exp.size() < min_number_train_positive_orders ){
					System.out.println("Insufficient positive train examples, re-sampling...");
				}
				else {
					System.out.println("Found "+positive_examples_exp.size() + " positive training examples for property "+property);
					System.out.println("Found "+negative_examples_exp.size() + " negative training examples for property "+property);
					
					if (positive_examples_exp.size() > max_train_examples_per_class && num_train_examples_per_class == -1){
						num_train_examples_per_class = max_train_examples_per_class;
						//System.out.println("!"+ positive_examples_exp.size()+"\t"+max_train_examples_per_class);
					}
					
					
					if (num_train_examples_per_class != -1 && positive_examples_exp.size() > num_train_examples_per_class){
					
						positive_examples_exp = UtilsJS.getRandomIntArraySubset(positive_examples_exp, num_train_examples_per_class, r);
					}
					negative_examples_exp = UtilsJS.getRandomIntArraySubset(negative_examples_exp, positive_examples_exp.size(), r);
					
					System.out.println("Using "+positive_examples_exp.size() + " positive training examples for property "+property);
					System.out.println("Using "+negative_examples_exp.size() + " negative training examples for property "+property);
					
					
					positive_examples.add(positive_examples_exp);
					negative_examples.add(negative_examples_exp);
					
					OrderClassifier OC = new OrderClassifier();
		
					//train classifier
					OC.setClassifier(classifier_string);
					
					//do cross validation on train object set
					Instances weka_data = OC.setData(positive_examples_exp, negative_examples_exp, OI, mDB);
					
					int num_folds = 5;
					
					weka_data.randomize(r);
					if (weka_data.classAttribute().isNominal()) {
				    	weka_data.stratify(num_folds);
				    }
					
					if (EV_prop[p]==null){
						EV_prop_test[p] =new Evaluation(weka_data);
						EV_prop[p] = new Evaluation(weka_data);
					}
					
					if (EV_prop_i[p] == null){
						EV_prop_i[p]=new Evaluation(weka_data);
					}
					
					System.out.println("Num. total instances:\t"+weka_data.numInstances());
				    for (int fold = 0; fold < num_folds; fold++){
				    	Instances train_f = weka_data.trainCV(num_folds, fold, r);
				    	Instances test_f = weka_data.testCV(num_folds, fold);
				    	
				    	OC.train(train_f);
				    	
				    	for (int t = 0; t < test_f.numInstances();t++){
				    		EV_prop[p].evaluateModelOnceAndRecordPrediction(OC.getClassifier(), test_f.instance(t));
				    		EV_prop_i[p].evaluateModelOnceAndRecordPrediction(OC.getClassifier(), test_f.instance(t));
				    		
				    	}
				    }
				    
				   
				    
				    //evaluate on test set
				    ArrayList<int[]> positive_examples_test = OrderOracle.getPositiveExamples(
				    		all_test_orders, property, OI, 
							order_threshold_diff, trainOrderIncreasing);
					ArrayList<int[]> negative_examples_test = OrderOracle.getNegativeExamples(
							all_test_orders, property, OI, 
							order_threshold_diff_neg, trainOrderIncreasing);
					negative_examples_test = UtilsJS.getRandomIntArraySubset(negative_examples_test, positive_examples_test.size(), r);
					System.out.println("Using "+positive_examples_test.size() + " positive test examples for property "+property);
					System.out.println("Using "+negative_examples_test.size() + " negative test examples for property "+property);
					
					//do cross validation on train object set
					/*if (positive_examples_test.size()>0){
						Instances weka_data_test = OC.setData(positive_examples_test, negative_examples_test, OI, mDB);
						OC.train(weka_data);
						for (int t = 0; t < weka_data_test.numInstances();t++){
							EV_prop_test[p].evaluateModelOnceAndRecordPrediction(OC.getClassifier(), weka_data_test.instance(t));
				    	}
					}*/
					
					System.out.println("\nCV results:");
					System.out.println(EV_prop[p].toSummaryString());
					
					//System.out.println("\nTest set results:");
					//System.out.println(EV_prop_test[p].toSummaryString());
					
					acc_rates[p][i-1]=EV_prop_i[p].pctCorrect();
					
					OC_p[p]=OC;
					
					
					//Next, we do order insertion - use last 5 tasks from positive examples
					/*if (testOrderInsertion){
						int last_n = 5;
						ArrayList<int[]> order_insertion_tasks = new ArrayList<int[]>(positive_examples_exp.subList(positive_examples_exp.size()-last_n, positive_examples_exp.size()));
					
						ArrayList<int[]> order_insertion_train_positive = new ArrayList<int[]>(positive_examples_exp.subList(0,positive_examples_exp.size()-last_n));
						ArrayList<int[]> order_insertion_train_negative = new ArrayList<int[]>(negative_examples_exp.subList(0,positive_examples_exp.size()-last_n));
						
						System.out.println("\nTesting order insertion using "+order_insertion_tasks.size() +" tasks.");
						System.out.println("Training model for order insertion using "+order_insertion_train_positive.size() +" and "+order_insertion_train_negative.size() +" positive and negative examples");
						
						
						weka_data = OC.setData(order_insertion_train_positive, order_insertion_train_negative, OI, mDB);
						OC.train(weka_data);
						
						for (int m = 0; m < order_insertion_tasks.size(); m++){
							System.out.println("\nOrder insertion task "+m);
							int [] task_m = order_insertion_tasks.get(m);
							//select which object to take out
							int test_object = task_m[r.nextInt(task_m.length)];
							
							ArrayList<Integer> task_m_sub_list = new ArrayList<Integer>();
							for (int w = 0; w < task_m.length; w++){
								if (task_m[w] != test_object)
									task_m_sub_list.add(new Integer(task_m[w]));
							}
							
							int [] task_m_sub = UtilsJS.toIntegerArray(task_m_sub_list);
							System.out.println("Task: insert object "+test_object+" into:");
							UtilsJS.printArray(task_m_sub);
							UtilsJS.printArray(task_m);
							
							//generate candidate orders
							ArrayList<int[]> candidate_orders = new ArrayList<int[]>();
							ArrayList<int[]> candidate_orders_negative = new ArrayList<int[]>(); //dummy
							for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
								ArrayList<Integer> candidate_p = new ArrayList<Integer>(task_m_sub_list);
								candidate_p.add(pos, new Integer(test_object));
								System.out.println("Candidate "+pos+":\t"+candidate_p.toString());
								candidate_orders.add(UtilsJS.toIntegerArray(candidate_p));
							}
							
							Instances candidate_weka_instances = OC.setData(candidate_orders, candidate_orders_negative, OI, mDB);
							//System.out.println(candidate_weka_instances.toString());
							
							for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
								double [] pr_pos = OC.getClassifier().distributionForInstance(candidate_weka_instances.instance(pos));
								System.out.println("Candidate "+pos+
											"\tprobs: "
											+pr_pos[0]+","+pr_pos[1]);
							}
						}
					}*/
				}
			}
			
			
			
			
			if (testOrderInsertion){
				ArrayList< ArrayList<int[]> > all_order_insertion_tasks = new ArrayList< ArrayList<int[]> >();
				
				for (int p = 0; p < properties.length; p++){
					ArrayList<int[]> positive_examples_exp = positive_examples.get(p);
					ArrayList<int[]> negative_examples_exp = negative_examples.get(p);
					
					int last_n = 5;
					ArrayList<int[]> order_insertion_tasks = new ArrayList<int[]>(positive_examples_exp.subList(positive_examples_exp.size()-last_n, positive_examples_exp.size()));
				
					ArrayList<int[]> order_insertion_train_positive = new ArrayList<int[]>(positive_examples_exp.subList(0,positive_examples_exp.size()-last_n));
					ArrayList<int[]> order_insertion_train_negative = new ArrayList<int[]>(negative_examples_exp.subList(0,negative_examples_exp.size()-last_n));
					
					//go through all train orders and select all that are positive for p and negative for other properties
					/*ArrayList<int[]> order_insertion_tasks_v2 = new ArrayList<int[]>();
					for (int l = 0; l < all_train_orders.size();l++){
						boolean positive_p = OrderOracle.isPositiveExampleV2(all_train_orders.get(l), OI, properties[p], order_thresholds[p], trainOrderIncreasing);
						if (positive_p){
							boolean negative_p2 = true;
							for (int p2 = 0; p2 < properties.length; p2++){
								if (p2 !=p){
									boolean temp = OrderOracle.isNegativeExample(all_train_orders.get(l), properties[p2], OI, order_thresholds[p2], trainOrderIncreasing);
									if (!temp)
										negative_p2 = false;
								}
							}
							
							if (negative_p2){
								order_insertion_tasks_v2.add(all_train_orders.get(l));
							}
						}
					}
					System.out.println("Found "+order_insertion_tasks_v2.size()+" tasks that are positive for "+properties[p]+" and negative for the others");
					order_insertion_tasks = order_insertion_tasks_v2;
					*/
					
					//generate a set of negative examples by looking at the positive examples for the other two properties
					/*ArrayList<int[]> insertion_train_negative_p = new ArrayList<int[]>();
					for (int p2 = 0; p2 < properties.length; p2++){
						if (p2 != p){
							ArrayList<int[]> positive_p2 = positive_examples.get(p2);
							for (int t = 0; t < positive_p2.size();t++){
								if (!OrderOracle.isPositiveExampleV2(positive_p2.get(t), OI, properties[p], order_thresholds[p], trainOrderIncreasing)){
									insertion_train_negative_p.add(positive_p2.get(t));
								}
							}
							//insertion_train_negative_p.addAll(positive_examples.get(p2));
						}
					}
					
					order_insertion_train_negative.addAll(insertion_train_negative_p);*/
					
					System.out.println("\nTesting order insertion using "+order_insertion_tasks.size() +" tasks.");
					System.out.println("Training model for order insertion using "+order_insertion_train_positive.size() +" and "+order_insertion_train_negative.size() +" positive and negative examples");
					
					all_order_insertion_tasks.add(order_insertion_tasks);
					
					Instances weka_data = OC_p[p].setData(order_insertion_train_positive, order_insertion_train_negative, OI, mDB);
					OC_p[p].train(weka_data);
				}
				
				for (int p = 0; p < properties.length; p++){
					System.out.println("\nOrder insertion task for property: "+properties[p]);
					
					ArrayList<int[]> order_insertion_tasks_p = all_order_insertion_tasks.get(p);
					
					/*ArrayList<int[]> order_insertion_tasks_p = OrderOracle.generateInsertionTasks(
							all_order_insertion_tasks.get(p), OI, properties[p], order_thresholds[p], trainOrderIncreasing);
					System.out.println("Found "+order_insertion_tasks_p_v2.size()+" insertion tasks.");
					*/
					//TO DO: only use tasks which are clearly negative examples for the other two properties
					
					//TO DO: for performance measure, use a histogram of errors in terms of position
					
					for (int m = 0; m < order_insertion_tasks_p.size(); m++){
						System.out.println("\nOrder insertion task "+m);
						int [] task_m = order_insertion_tasks_p.get(m);
												
						//select which object to take out
						int true_obj_pos = r.nextInt(task_m.length);
						int test_object = task_m[true_obj_pos];
						
						ArrayList<Integer> task_m_sub_list = new ArrayList<Integer>();
						for (int w = 0; w < task_m.length; w++){
							if (task_m[w] != test_object)
								task_m_sub_list.add(new Integer(task_m[w]));
						}
						
						int [] task_m_sub = UtilsJS.toIntegerArray(task_m_sub_list);
						
						
						
						//check to make sure the task is negative for the other two labels
						for (int p2 = 0; p2 < properties.length; p2++){
							boolean is_neg_p2 = OrderOracle.isNegativeExample(task_m_sub, properties[p2], OI, order_thresholds[p2], trainOrderIncreasing);
							System.out.println("\tis negative for "+properties[p2]+":\t"+is_neg_p2);
						}
						
						System.out.println("Task: insert object "+test_object+" into:");
						UtilsJS.printArray(task_m_sub);
						System.out.println("Ground Truth:");
						UtilsJS.printArray(task_m);
						
						
						
						
						//step 1: find out which concept best explains order
						double [] p_scores = new double[properties.length];
						for (int w = 0; w < p_scores.length; w++){
							Instance inst_w = OC_p[w].createTestInstance(task_m_sub, OI, mDB, "-1");
							double [] p_w = OC_p[w].getClassifier().distributionForInstance(inst_w);
							System.out.println("\t P("+properties[w]+") = " + p_w[1]);
							p_scores[w]=p_w[1];
						}
						
						int most_likely_prop = Utils.maxIndex(p_scores);
						int true_property = p;
						
						if (most_likely_prop == true_property){
							correct_insertion_recognition[p]++;
						}
						else {
							incorrect_insertion_recognition[p]++;
						}
						
						//generate candidate orders
						ArrayList<int[]> candidate_orders = new ArrayList<int[]>();
						ArrayList<int[]> candidate_orders_negative = new ArrayList<int[]>(); //dummy
						for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
							ArrayList<Integer> candidate_p = new ArrayList<Integer>(task_m_sub_list);
							candidate_p.add(pos, new Integer(test_object));
							System.out.println("Candidate "+pos+":\t"+candidate_p.toString());
							candidate_orders.add(UtilsJS.toIntegerArray(candidate_p));
						}
						
						//METHOD 1 : only consider recognized order
						
						/*Instances candidate_weka_instances = OC_p[most_likely_prop].setData(candidate_orders, candidate_orders_negative, OI, mDB);
						double [] c_scores = new double[task_m_sub_list.size()+1];
						for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
							double [] pr_pos = OC_p[most_likely_prop].getClassifier().distributionForInstance(candidate_weka_instances.instance(pos));
							System.out.println("Candidate "+pos+
										"\tprobs: "
										+pr_pos[0]+","+pr_pos[1]);
							c_scores[pos]=pr_pos[1];
						}
						
						int order_index = Utils.maxIndex(c_scores);*/
						
						//METHOD 2 : consdier all properties
						double top_score = Double.MIN_VALUE;
						int best_prop_index = -1;
						int best_order_index = -1;
						double [][] all_scores = new double[task_m_sub_list.size()+1][properties.length];
						for (int w = 0; w < properties.length; w++){
							Instances candidate_weka_instances_w = OC_p[w].createData(candidate_orders, candidate_orders_negative, OI, mDB);
							for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
								double [] pr_pos = OC_p[w].getClassifier().distributionForInstance(candidate_weka_instances_w.instance(pos));
					
								if (pr_pos[1] > top_score){
									best_prop_index = w;
									best_order_index = pos;
									top_score = pr_pos[1];
								}
								
								all_scores[pos][w]=pr_pos[1];
							}
						}
						int order_index = best_order_index;
						
						System.out.println("Scores matrix:");
						UtilsJS.printMatrix(all_scores);
						
						System.out.println("Selected order:\t");
						UtilsJS.printArray(candidate_orders.get(order_index));
						System.out.println("True obj. pos: "+true_obj_pos+"\tSelected:\t"+order_index);
						
						//METHOD 2
						
						
						
						
						//if (most_likely_prop == true_property){
						if (true_obj_pos == order_index){
							correct_insertions[p]++;
						}
						else {
							incorrect_insertions[p]++;
						}
					
							
						int error_position = Math.abs(true_obj_pos - order_index);
						
						if (properties[p] == "weight"){
							insertion_errors_weight.add(new Integer(error_position));
						}
						else if (properties[p] == "width"){
							insertion_errors_width.add(new Integer(error_position));
						}
						else if (properties[p] == "height"){
							insertion_errors_height.add(new Integer(error_position));
						}
					}
				}
			}
		}
		
		System.out.println("\n\nORDER RECOGNITION with "+classifier_string+":\n");
		double [][] result = new double[properties.length][2];
		
		for (int p = 0; p < properties.length; p++){
			String property = properties[p];
	
			System.out.println("\nProperty:\t"+property);
			
			System.out.println("\nCV results:");
			System.out.println(EV_prop[p].toClassDetailsString());
			System.out.println(EV_prop[p].toSummaryString());
			System.out.println(EV_prop[p].toMatrixString());
			
			System.out.println("\n"+Utils.mean(acc_rates[p])+"\t"+Math.sqrt(Utils.variance(acc_rates[p])));
			
			result[p][0]=Utils.mean(acc_rates[p]);
			result[p][1]=Math.sqrt(Utils.variance(acc_rates[p]));

			//UtilsJS.printArray(acc_rates[p]);
			/*System.out.println("\nTest set results:");
			System.out.println(EV_prop_test[p].toClassDetailsString());
			System.out.println(EV_prop_test[p].toSummaryString());
			System.out.println(EV_prop_test[p].toMatrixString());*/
			
	
		}
		
		if (testOrderInsertion){
			System.out.println("\n\nORDER INSERTION:\n");
			for (int p = 0; p < properties.length; p++){
				String property = properties[p];
				System.out.println("\nProperty:\t"+property);
				System.out.println("# correct:\t"+ correct_insertions[p]);
				System.out.println("# incorrect:\t"+ incorrect_insertions[p]);
				double acc = (double)correct_insertions[p]/((double)correct_insertions[p]+(double)incorrect_insertions[p]);
				double chance_acc = (double)1.0/(double)order_length;
				
				double kappa = (acc-chance_acc)/(1.0-chance_acc);
				System.out.println("Accuracy:\t"+acc);
				System.out.println("Kappa:\t"+kappa);
				
				
				double rec_acc = (double)correct_insertion_recognition[p]/((double)correct_insertion_recognition[p]+(double)incorrect_insertion_recognition[p]);
				System.out.println("Recognition acc:\t"+rec_acc);
	
			}
			
			int [] insertion_errors_weight_array = UtilsJS.toIntegerArray(insertion_errors_weight);
			int [] insertion_errors_width_array = UtilsJS.toIntegerArray(insertion_errors_width);
			int [] insertion_errors_height_array = UtilsJS.toIntegerArray(insertion_errors_height);
	
			UtilsJS.writeIntArrayToFile(insertion_errors_weight_array, "/home/jsinapov/research/datasets/object_ordering/ins_errors_weight.txt");
			UtilsJS.writeIntArrayToFile(insertion_errors_width_array, "/home/jsinapov/research/datasets/object_ordering/ins_errors_width.txt");
			UtilsJS.writeIntArrayToFile(insertion_errors_height_array, "/home/jsinapov/research/datasets/object_ordering/ins_errors_height.txt");
		}
		
		
		return result;
	}
	
	
	public static void orderExp() throws Exception{
		ObjectInfo OI = new ObjectInfo(object_info_file);
		OI.print();
		ArrayList<String[]> contexts = getContexts();
		
		MOrderDB mDB = new MOrderDB();
		mDB.setContexts(contexts);
		mDB.loadFromPath(matlab_orders_path, OI);
		
		
		int TOTAL_RUNS = 100;
		int num_train = 24;
		int order_length = 4;
		int min_number_train_positive_orders = 50;
		//double [] negative_example_fitness_range = { (1.0/(double)(order_length-1)), (double)(order_length-2)/(double)(order_length-1) };
		
		
		boolean trainOrderIncreasing = true;
		double order_threshold_diff = 30;
		
		boolean ev_init = false;
		Evaluation EV_p = new Evaluation(UtilsJS.dummyInstances());;
		
		for (int i = 1;i <= TOTAL_RUNS;i++){
			System.out.println("\n\nRun #"+i);
			
			Random r = new Random(i);
			
			ArrayList<int[]> ids_split = OrderOracle.getTrainTestSplit(OI.object_ids, r, num_train);
			int [] train_object_ids = ids_split.get(0);
			int [] test_object_ids = ids_split.get(1);
			
			System.out.println("Train objects:");
			UtilsJS.printArray(train_object_ids);
			System.out.println("Test objects:");
			UtilsJS.printArray(test_object_ids);
			
			ArrayList<int[]> all_train_orders = OrderOracle.generateOrders(train_object_ids, order_length);
			//System.out.println("Found "+all_train_orders.size()+" possible train orders.");
			
			//generate training examples
			String property = "weight";
			
			ArrayList<int[]> positive_examples_exp = OrderOracle.getPositiveExamples(
										all_train_orders, property, OI, 
										order_threshold_diff, trainOrderIncreasing);
			ArrayList<int[]> negative_examples_exp = OrderOracle.getNegativeExamples(
					all_train_orders, property, OI, 
					order_threshold_diff, trainOrderIncreasing);
			
			ArrayList<int[]> all_test_orders = OrderOracle.generateOrders(test_object_ids, order_length);
			//System.out.println("\nFound "+all_test_orders.size()+" possible test orders.");
			
			ArrayList<int[]> positive_examples_test = OrderOracle.getPositiveExamples(
					all_test_orders, property, OI, 
					order_threshold_diff, trainOrderIncreasing);
			ArrayList<int[]> negative_examples_test = OrderOracle.getNegativeExamples(
					all_test_orders, property, OI, 
					order_threshold_diff, trainOrderIncreasing);
			
			if (positive_examples_test.size() == 0 || positive_examples_test.size() > negative_examples_test.size()){
				System.out.println("No positive test examples, re-sampling...");
			}
			else if (positive_examples_exp.size() < min_number_train_positive_orders ){
				System.out.println("Insufficient positive train examples, re-sampling...");
			}
			else {
				System.out.println("Found "+positive_examples_exp.size() + " positive training examples for property "+property);
				System.out.println("Found "+negative_examples_exp.size() + " negative training examples for property "+property);
				
				System.out.println("Found "+positive_examples_test.size() + " positive test examples for property "+property);
				System.out.println("Found "+negative_examples_test.size() + " negative test examples for property "+property);
				
				
				/*ArrayList<int[]> positive_examples = new ArrayList<int[]>();
				ArrayList<int[]> negative_examples = new ArrayList<int[]>();
				
				for (int k = 0; k < all_train_orders.size(); k++){
					double fitness_k = OrderOracle.orderFitness(all_train_orders.get(k), 
							OI, property, order_threshold_diff, trainOrderIncreasing);
	
					if (fitness_k == 1.0){
						positive_examples.add(all_train_orders.get(k));
					}
					else if (fitness_k >= negative_example_fitness_range[0] && fitness_k <= negative_example_fitness_range[1]){
						negative_examples.add(all_train_orders.get(k));
					}
				}
				System.out.println("Found "+negative_examples.size() + " negative training examples for property "+property);
				*/
				negative_examples_exp = UtilsJS.getRandomIntArraySubset(negative_examples_exp, positive_examples_exp.size(), r);
				negative_examples_test = UtilsJS.getRandomIntArraySubset(negative_examples_test, positive_examples_test.size(), r);
				
				System.out.println("Using "+positive_examples_exp.size() + " positive training examples for property "+property);
				System.out.println("Using "+negative_examples_exp.size() + " negative training examples for property "+property);
				
				OrderClassifier OC = new OrderClassifier();
	
				//train classifier
				OC.setClassifier("SVMpoly");
				OC.setData(positive_examples_exp, negative_examples_exp, OI, mDB);
				OC.train();
				OC.crossValidate();
				
				if (!ev_init){
					EV_p = new Evaluation(OC.getWekaData());
					ev_init = true;
				}
				
				for (int t = 0; t < positive_examples_test.size(); t++){
					Instance inst_t = OC.createTestInstance(positive_examples_test.get(t), OI, mDB, "+1");
					EV_p.evaluateModelOnceAndRecordPrediction(OC.getClassifier(), inst_t);
				}
				
				for (int t = 0; t < negative_examples_test.size(); t++){
					Instance inst_t = OC.createTestInstance(negative_examples_test.get(t), OI, mDB, "-1");
					EV_p.evaluateModelOnceAndRecordPrediction(OC.getClassifier(), inst_t);
				}
				
				System.out.println(EV_p.toClassDetailsString());
				System.out.println(EV_p.toSummaryString());
				System.out.println(EV_p.toMatrixString());
			}
		}
		
		
		
		
		//int [] order_weight = {1,3,4,5};
		//System.out.println(OrderOracle.isPositiveExample(order_weight,OI,"weight",20.0,true));
		//System.out.println("Fitness: "+OrderOracle.orderFitness(order_weight, OI, "weight", 20, true));
		
		
		//int order_length = 4;
		//OrderOracle.generateOrders(OI.object_ids, order_length);
		
		/*System.out.println("Full order:");
		MOrder full_order = mDB.getOrderForContext("push-effort");
		full_order.print();
		
		int [] subset_ids = {23,19,30,7,14,15};
		MOrder subset_order = full_order.getSubsetOrder(subset_ids);
		System.out.println("\nSubset order:");
		subset_order.print();*/
	}
	
	public static ArrayList<String[]> getContexts(){
		ArrayList<String[]> contexts = new ArrayList<String[]>();
		for (int b = 0; b < behaviors.length;b++){
			for (int m = 0; m < modalities.length; m++){
				if (DataUtils.validCombination(behaviors[b], modalities[m])){
					String [] c_bm = new String[2];
					c_bm[0]=behaviors[b];
					c_bm[1]=modalities[m];
					contexts.add(c_bm);
				}
			}
		}
		return contexts;
	}
	
	/* computes the relational matrices used for ordering */
	public static void matrixComputation(){
		
		ArrayList<String[]> contexts = new ArrayList<String[]>();
		ArrayList<String> contexts_string = new ArrayList<String>();
		for (int b = 0; b < behaviors.length;b++){
			for (int m = 0; m < modalities.length; m++){
				if (DataUtils.validCombination(behaviors[b], modalities[m])){
					String [] c_bm = new String[2];
					c_bm[0]=behaviors[b];
					c_bm[1]=modalities[m];
					contexts.add(c_bm);
					
					contexts_string.add(DataUtils.contextString(c_bm));
				}
			}
		}
		
		ObjectInfo OI = new ObjectInfo(object_info_file);
		ContextDataDB cDB = DataUtils.loadDB(feature_data_path, contexts, OI,true,0.975);
		
		//Next, compute NN matrix for object pairs
		int kNN = 25;
		for (int c = 0; c < contexts_string.size();c++){
			System.out.println("Computing NN matrix for context "+contexts_string.get(c));
			ContextData D_c = cDB.getConextData(contexts_string.get(c));
			
			int [][] M = new int[OI.object_ids.length][OI.object_ids.length];
			for (int i = 0; i < M.length; i++){
				for (int j = 0; j < M[i].length;j++){
					M[i][j]=0;
				}
			}
			
			for (int o = 0; o < OI.object_ids.length; o++){
				
				int current_object = OI.object_ids[o];
				
				ArrayList<DataPointBM> data_o = D_c.getDataWithObject(OI.object_names.get(OI.object_ids[o]-1));
				ArrayList<DataPointBM> data_left = D_c.getDataWithoutObject(OI.object_names.get(OI.object_ids[o]-1));
				
				for (int i = 0; i < data_o.size();i++){
					DataPointBM dp_i = data_o.get(i);
					double [] f_i = dp_i.getFeatures();
					
					//find the nearest k neighbors
					
					//compute distances
					double [] distances = new double[data_left.size()];
					for (int j = 0; j < data_left.size(); j++){
						double [] f_j = data_left.get(j).getFeatures();
						distances[j]=UtilsJS.computeL2(f_i, f_j);
					}
					
					//find closest kNN
					int [] best_k = UtilsJS.findLargestK(distances, kNN, true);
					//UtilsJS.printArray(best_k);
					for (int k = 0; k < kNN; k++){
						int object_k = data_left.get(best_k[k]).getObjectID();
						M[current_object-1][object_k-1]++;
						
						//uncomment to make matrices symmetrics
						//M[object_k-1][current_object-1]++;
						
						//uncomment to get self edges
						//M[current_object-1][current_object-1]++;
					}
				}
				
			}
			
			UtilsJS.printMatrix(M);
			
			String m_filename = new String(matrices_data_path+""+contexts_string.get(c)+"_nn"+kNN+"_matrix.txt");
			UtilsJS.saveMatrix(M, m_filename);
		}
		
		
		//next, compute average pairwise similarity
		/*double sigma = 5;
		for (int c = 0; c < contexts_string.size();c++){
			System.out.println("Computing sim. matrix for context "+contexts_string.get(c));
			ContextData D_c = cDB.getConextData(contexts_string.get(c));
			
			double [][] M = new double[OI.object_ids.length][OI.object_ids.length];
			for (int i = 0; i < M.length; i++){
				for (int j = 0; j < M[i].length;j++){
					M[i][j]=0.0;
				}
			}
			
			for (int i = 0; i < OI.object_ids.length; i++){
				
				int object_i = OI.object_ids[i];
				ArrayList<DataPointBM> data_i = D_c.getDataWithObject(OI.object_names.get(OI.object_ids[i]-1));
				
				for (int j = 0; j < OI.object_ids.length; j++){
					int object_j = OI.object_ids[j];
					ArrayList<DataPointBM> data_j = D_c.getDataWithObject(OI.object_names.get(OI.object_ids[j]-1));
					
					ArrayList<Double> distances_ij = new ArrayList<Double>();
					for (int p = 0; p < data_i.size();p++){
						for (int q = 0; q < data_j.size(); q++){
							double d_pq = UtilsJS.computeL2(data_i.get(p).getFeatures(),data_j.get(q).getFeatures());
							distances_ij.add(new Double(d_pq));
						}
					}
					
					double [] dist_ij = new double[distances_ij.size()];
					for (int k = 0; k < distances_ij.size(); k++){
						dist_ij[k] = distances_ij.get(k).doubleValue();
					}
					
					double mean_distance_ij = Utils.mean(dist_ij);
					
					double sim_ij = Math.exp( -(mean_distance_ij*mean_distance_ij)/(2*sigma*sigma) );
					M[object_i-1][object_j-1]=sim_ij*100;
				}
			}
			
			UtilsJS.printMatrix(M);
			
			String m_filename = new String(matrices_data_path+""+contexts_string.get(c)+"_sim_matrix.txt");
			UtilsJS.saveMatrix(M, m_filename);
		}*/
		
		StringBuffer sb_weight = new StringBuffer();
		StringBuffer sb_height = new StringBuffer();
		StringBuffer sb_width = new StringBuffer();
		for (int o = 0; o < OI.object_ids.length; o++){
			sb_weight.append("\""+(int)OI.object_weights[OI.object_ids[o]-1]+"\"\n");
			sb_height.append("\""+(int)OI.object_heights[OI.object_ids[o]-1]+"\"\n");
			sb_width.append("\""+(int)OI.object_widths[OI.object_ids[o]-1]+"\"\n");
		}
		UtilsJS.writeStringToFile(sb_weight.toString(), new String(matrices_data_path+"names_weight.txt"));
		UtilsJS.writeStringToFile(sb_height.toString(), new String(matrices_data_path+"names_height.txt"));
		UtilsJS.writeStringToFile(sb_width.toString(), new String(matrices_data_path+"names_width.txt"));
		
	}
	
	public static void featureExtraction() {
		// TODO Auto-generated method stub

		ObjectInfo OI = new ObjectInfo(object_info_file);
		OI.print();
		
		
		for (int b = 0; b < behaviors.length; b++){
			ArrayList<HapticRecord> data_b = DataLoader.loadHapticDataset(data_path, OI, behaviors[b],num_trials);
			
			System.out.println("Loaded "+data_b.size()+" haptic records with behavior "+behaviors[b]);
			
			//compute effort features for this behavior and store them in a file
			IHapticFeatureGenerator FG_efforts = new EffortFeatureGenerator(6,num_temporal_bins);
			FG_efforts.init(data_b);
			
			StringBuffer file_content_b = new StringBuffer();
			
			for (int i = 0; i < data_b.size(); i++){
				double [] f_i = FG_efforts.extractFeatures(data_b.get(i));
				int object_i = data_b.get(i).object;
				int trial_i = data_b.get(i).trial;
				
				file_content_b.append(new String(object_i+","+trial_i+","));
				
				for (int j = 0; j < f_i.length; j++){
					file_content_b.append(new String(""+f_i[j]));
					if (j < f_i.length-1)
						file_content_b.append(",");
					else file_content_b.append("\n");
				}
				
				String filename_b = new String(feature_data_path+""+behaviors[b]+"_effort.txt");
				UtilsJS.writeStringToFile(file_content_b.toString(), filename_b);
			}
			
			//compute position features
			IHapticFeatureGenerator FG_position = new EffortFeatureGenerator(6,num_temporal_bins);
			FG_position.init(data_b);
			
			file_content_b = new StringBuffer();
			
			for (int i = 0; i < data_b.size(); i++){
				double [] f_i = FG_position.extractFeatures(data_b.get(i));
				int object_i = data_b.get(i).object;
				int trial_i = data_b.get(i).trial;
				
				file_content_b.append(new String(object_i+","+trial_i+","));
				
				for (int j = 0; j < f_i.length; j++){
					file_content_b.append(new String(""+f_i[j]));
					if (j < f_i.length-1)
						file_content_b.append(",");
					else file_content_b.append("\n");
				}
				
				String filename_b = new String(feature_data_path+""+behaviors[b]+"_position.txt");
				UtilsJS.writeStringToFile(file_content_b.toString(), filename_b);
			}
			
			//if grasping, compute finger features as well
			if (behaviors[b]=="grasp"){
				IHapticFeatureGenerator FG_fingers = new FingerPositionFeatureGenerator(num_temporal_bins);
				FG_fingers.init(data_b);
				
				file_content_b = new StringBuffer();
				
				for (int i = 0; i < data_b.size(); i++){
					double [] f_i = FG_fingers.extractFeatures(data_b.get(i));
					int object_i = data_b.get(i).object;
					int trial_i = data_b.get(i).trial;
					
					file_content_b.append(new String(object_i+","+trial_i+","));
					
					for (int j = 0; j < f_i.length; j++){
						file_content_b.append(new String(""+f_i[j]));
						if (j < f_i.length-1)
							file_content_b.append(",");
						else file_content_b.append("\n");
					}
					
					String filename_b = new String(feature_data_path+""+behaviors[b]+"_fingers.txt");
					UtilsJS.writeStringToFile(file_content_b.toString(), filename_b);
				}
				
				IHapticFeatureGenerator FG_endfingers = new EndFingerPositionFeatureGenerator();
				FG_endfingers.init(data_b);
				
				file_content_b = new StringBuffer();
				
				for (int i = 0; i < data_b.size(); i++){
					double [] f_i = FG_endfingers.extractFeatures(data_b.get(i));
					int object_i = data_b.get(i).object;
					int trial_i = data_b.get(i).trial;
					
					file_content_b.append(new String(object_i+","+trial_i+","));
					
					for (int j = 0; j < f_i.length; j++){
						file_content_b.append(new String(""+f_i[j]));
						if (j < f_i.length-1)
							file_content_b.append(",");
						else file_content_b.append("\n");
					}
					
					String filename_b = new String(feature_data_path+""+behaviors[b]+"_endfingers.txt");
					UtilsJS.writeStringToFile(file_content_b.toString(), filename_b);
				}
			}
		}
		
		
		
		/*HapticRecord HR = DataLoader.loadHapticRecord(data_path, 1, 1, "drop");
		HR.print();
		
		int num_t_bins = 10;
		*/
		//IHapticFeatureGenerator HFG = new FingerPositionFeatureGenerator(num_t_bins);
		//double [] f = HFG.extractFeatures(HR);
		
	}

}
