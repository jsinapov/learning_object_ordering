package learning;

import java.util.ArrayList;
import java.util.Random;

import utils.CombinationGenerator;
import utils.PermutationGenerator;
import utils.UtilsJS;
import data.ObjectInfo;

public class OrderOracle {

	
	
	
	public static ArrayList<int[]> getTrainTestSplit(int [] all_object_ids, Random r, int num_train){
		 ArrayList<int[]> result = new  ArrayList<int[]>();
		 
		 ArrayList<Integer> ids = new ArrayList<Integer>();
		 for (int i = 0; i < all_object_ids.length; i++){
			 ids.add(new Integer(all_object_ids[i]));
		 }
		 
		 ArrayList<Integer> train_ids = new ArrayList<Integer>();
		 for (int i = 0; i < num_train; i++){
			 int r_i = r.nextInt(ids.size()); 
			 int selected_object_id = ids.get(r_i).intValue();
			 train_ids.add(new Integer(selected_object_id));
			 System.out.print("adding "+selected_object_id+" r="+r_i+" ");
			 ids.remove(r_i);
		 }
		 System.out.println(train_ids);
		 
		 ArrayList<Integer> test_ids = new ArrayList<Integer>();
		 for (int i = 0; i < ids.size(); i++){
			 test_ids.add(ids.get(i));
		 }
		 
		 
		 int [] train_ids_array = new int[train_ids.size()];
		 int [] test_ids_array = new int[test_ids.size()];
		 
		 for (int i = 0; i < train_ids.size(); i++){
			 train_ids_array[i]=train_ids.get(i).intValue();
		 }
		 
		 for (int i = 0; i < test_ids.size(); i++){
			 test_ids_array[i]=test_ids.get(i).intValue();
		 }
		 
		 result.add(train_ids_array);
		 result.add(test_ids_array);
		 
		 return result;
	}
	
	public static ArrayList<int[]> generateOrders(int [] all_object_ids, int order_length){
		ArrayList<int[]> orders = new ArrayList<int[]>();
		
		CombinationGenerator CG = new CombinationGenerator(all_object_ids.length,order_length);
		//System.out.println("Num. combinations: " + CG.getNumLeft().toString());
		while (CG.hasMore()){
			int [] next_order = CG.getNext();
			int [] copy = new int[next_order.length];
			for (int i = 0;i < copy.length; i++)
				copy[i]=all_object_ids[next_order[i]];
			
			PermutationGenerator PG = new PermutationGenerator();
			PG.computePermutations(copy, copy.length);
			ArrayList<int[]> perms = PG.retrievePermutations();
			for (int i = 0; i < perms.size(); i++){
				orders.add(perms.get(i));
			}
			
			//UtilsJS.printArray(next_order);
			//orders.add(copy);
		
		}
		
		return orders;
	}
	

	public static boolean isNegativeExample(int [] order_k, String property,
			ObjectInfo OI,
			double order_threshold_diff,
			boolean trainOrderIncreasing){
		
		double current_val = OI.getObjectPropertyValue(order_k[0], property);
		int last_direction = 0;
		
		boolean accept = true;
		
		for (int i = 1;i < order_k.length; i++){
			double next_val = OI.getObjectPropertyValue(order_k[i], property);
			
			if (last_direction == 0){
				if (next_val > current_val + order_threshold_diff){
					last_direction = 1;
				}
				else if (next_val < current_val - order_threshold_diff){
					last_direction = -1;
				}
				else 
				{
					accept = false;
					break;
				}
			}
			else {
				if (next_val > current_val && last_direction == 1){
					accept = false;
					break;
				}
				else if (next_val < current_val && last_direction == -1){
					accept = false;
					break;
				}
				else {
					if (next_val > current_val + order_threshold_diff){
						last_direction = 1;
					}
					else if (next_val < current_val - order_threshold_diff){
						last_direction = -1;
					}
					else 
					{
						accept = false;
						break;
					}
				}
			}
			
			current_val = next_val;
		}
		
		return accept;
	}
	
	public static ArrayList<int[]> getNegativeExamples(ArrayList<int[]> all_train_orders,
												String property,
												ObjectInfo OI,
												double order_threshold_diff,
												boolean trainOrderIncreasing){
		
		ArrayList<int[]> negative_examples = new ArrayList<int[]>();
		
		for (int k = 0; k < all_train_orders.size(); k++){
			
			int [] order_k = all_train_orders.get(k);
			
			double current_val = OI.getObjectPropertyValue(order_k[0], property);
			int last_direction = 0;
			
			boolean accept = true;
			
			for (int i = 1;i < order_k.length; i++){
				double next_val = OI.getObjectPropertyValue(order_k[i], property);
				
				if (last_direction == 0){
					if (next_val > current_val + order_threshold_diff){
						last_direction = 1;
					}
					else if (next_val < current_val - order_threshold_diff){
						last_direction = -1;
					}
					else 
					{
						accept = false;
						break;
					}
				}
				else {
					if (next_val > current_val && last_direction == 1){
						accept = false;
						break;
					}
					else if (next_val < current_val && last_direction == -1){
						accept = false;
						break;
					}
					else {
						if (next_val > current_val + order_threshold_diff){
							last_direction = 1;
						}
						else if (next_val < current_val - order_threshold_diff){
							last_direction = -1;
						}
						else 
						{
							accept = false;
							break;
						}
					}
				}
				
				current_val = next_val;
			}
			
			if (accept){
				negative_examples.add(order_k);
			}
		}
		return negative_examples;
	}
	
	public static ArrayList<int[]> getPositiveExamples(ArrayList<int[]> all_train_orders,
												String property,
												ObjectInfo OI,
												double order_threshold_diff,
												boolean trainOrderIncreasing){
		
		ArrayList<int[]> positive_examples = new ArrayList<int[]>();
		
		for (int k = 0; k < all_train_orders.size(); k++){
			
			double fitness_k = OrderOracle.orderFitness(all_train_orders.get(k), 
					OI, property, order_threshold_diff, trainOrderIncreasing);

			if (fitness_k == 1.0){
				positive_examples.add(all_train_orders.get(k));
			}
			
			//System.out.println("Order "+k+":");
			//UtilsJS.printArray(all_train_orders.get(k));
			//System.out.println(fitness_k);
			
		}
		return positive_examples;
	}

	
	public static ArrayList<int[]> generateInsertionTasks(ArrayList<int[]> positive_examples, 
			ObjectInfo OI, String property, double order_threshold_diff,
			boolean trainOrderIncreasing){
		
		
		ArrayList<int[]>  results = new ArrayList<int[]> ();
		
		for (int i =0 ; i < positive_examples.size(); i++){
			int [] o_i = positive_examples.get(i);
			
			//System.out.println("Searching for an additional object to complete ");
			//UtilsJS.printArray(o_i);
			
			ArrayList<Integer> task_m_sub_list = new ArrayList<Integer>();
			for (int w = 0; w < o_i.length; w++){
				task_m_sub_list.add(new Integer(o_i[w]));
			}
			
			ArrayList<Integer> candidates_remainder_object = new ArrayList<Integer>();
			for (int k = 0; k < OI.object_ids.length; k++){
				if (!UtilsJS.contains(o_i, OI.object_ids[k])){
					candidates_remainder_object.add(new Integer(OI.object_ids[k]));
				}
			}
			
			//System.out.println("Searcihng among object:\t"+candidates_remainder_object.toString());
			
			//for each candidate remained object, see if we can make a positive examples by inserting it somewhere in the order
			for (int k = 0; k < candidates_remainder_object.size(); k++){
				//generate candidate orders
				ArrayList<int[]> candidate_orders = new ArrayList<int[]>();
				
				for (int pos = 0; pos < task_m_sub_list.size()+1; pos++){
					ArrayList<Integer> candidate_p = new ArrayList<Integer>(task_m_sub_list);
					candidate_p.add(pos, candidates_remainder_object.get(k));
					
					int [] candidate_p_array = UtilsJS.toIntegerArray(candidate_p);
					
					double fitness_k = OrderOracle.orderFitness(candidate_p_array, 
							OI, property, order_threshold_diff, trainOrderIncreasing);
					//System.out.println("\tCandidate order:\t"+candidate_p.toString()+"\tfitness = "+fitness_k);
					if (fitness_k == 1.0){
						results.add(candidate_p_array);
					}
					
				}
			}
		}
		
		
		return results;
	}
	
	public static double orderFitness(int [] object_ids_order, ObjectInfo OI, String property, double threshold, boolean increasing){
		int num_correct = 0;
		int num_total = 0;
		
		//test increasing
		if (increasing){
			double current_val = OI.getObjectPropertyValue(object_ids_order[0], property);
			for (int i = 1;i < object_ids_order.length; i++){
				double next_val = OI.getObjectPropertyValue(object_ids_order[i], property);
				
				if (next_val > current_val + threshold){ //increasing
					num_correct++;
				}
				
				num_total++;
				current_val = next_val;
			}
		}
		else {
			double current_val = OI.getObjectPropertyValue(object_ids_order[0], property);
			for (int i = 1;i < object_ids_order.length; i++){
				double next_val = OI.getObjectPropertyValue(object_ids_order[i], property);
				
				if (next_val + threshold < current_val ){ //increasing
					current_val = next_val;
					num_correct++;
				}
				
				num_total++;
				current_val = next_val;
			
			}
		}
		
		return (double)num_correct/(double)num_total;
	}
	
	public static boolean isPositiveExampleV2(int [] object_ids_order, ObjectInfo OI, String property, double order_threshold_diff, boolean trainOrderIncreasing){
		double fitness_k = OrderOracle.orderFitness(object_ids_order, 
				OI, property, order_threshold_diff, trainOrderIncreasing);
		
		if (fitness_k == 1.0){
			return true;
		}
		else return false;
	}
	
	
	/*public static boolean isPositiveExample(int [] object_ids_order, ObjectInfo OI, String property, double threshold, boolean increasing){
		
		//test increasing
		if (increasing){
			boolean isIncreasing = true;
			double current_val = OI.getObjectPropertyValue(object_ids_order[0], property);
			for (int i = 1;i < object_ids_order.length; i++){
				double next_val = OI.getObjectPropertyValue(object_ids_order[i], property);
				
				if (next_val > current_val + threshold){ //increasing
					current_val = next_val;
				}
				else {
					isIncreasing = false;
					break;
				}
			}
			
			if (isIncreasing == true)
				return true;
			else return false;
		
		}
		else {
			boolean isDecreasing = true;
			double current_val = OI.getObjectPropertyValue(object_ids_order[0], property);
			for (int i = 1;i < object_ids_order.length; i++){
				double next_val = OI.getObjectPropertyValue(object_ids_order[i], property);
				
				if (next_val + threshold < current_val ){ //increasing
					current_val = next_val;
				}
				else {
					isDecreasing = false;
					break;
				}
			}
			
			if (isDecreasing == true)
				return true;
			else return false;
		}
	}*/
	
}


