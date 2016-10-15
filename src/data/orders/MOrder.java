package data.orders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import utils.UtilsJS;
import weka.core.Utils;
import data.ObjectInfo;

public class MOrder {

	//raw data stored from MATLAB
	int [] object_ids; //from 1 to 32
	int [] z_vector;//for each object, which cluster does it belong to
	int [][] cluster_adj_matrix; //the order among the clusters
	
	ArrayList<int[]> objects_per_cluster; //for each cluster in the order, the array contains the IDs that fall into it
	int num_clusters;
	
	ObjectInfo obj_I;
	
	public MOrder(String z_file, String adj_matrix_file, ObjectInfo OI){
		this.object_ids = OI.object_ids;
		this.obj_I = OI;
		
		try {
			//load z-vector file
			BufferedReader BR = new BufferedReader(new FileReader(new File(z_file)));
			String line = BR.readLine();
			StringTokenizer st = new StringTokenizer(line);
			
			z_vector = new int[object_ids.length];
			
			for (int i = 0; i < z_vector.length; i++){
				String token_i = st.nextToken();
				z_vector[i]=(int)Double.parseDouble(token_i)-1; //make them start at 0 instead of 1
			}
			
		
			
			//load matrix
			double [][] cluster_adj_matrix_double = UtilsJS.loadSquareMatrix(adj_matrix_file);
			//UtilsJS.printMatrix(cluster_adj_matrix_double);
			
			cluster_adj_matrix = new int[cluster_adj_matrix_double.length][cluster_adj_matrix_double.length];
			for (int i = 0; i < cluster_adj_matrix.length; i++){
				for (int j = 0; j < cluster_adj_matrix[i].length;j++){
					cluster_adj_matrix[i][j] = (int)cluster_adj_matrix_double[i][j];
				}
			}
			
			num_clusters = cluster_adj_matrix.length;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//store them data in the array list
		objects_per_cluster = new ArrayList<int[]>();
		for (int cluster = 0; cluster < num_clusters; cluster ++){
			
			ArrayList<Integer> ids_cluster = new ArrayList<Integer>();
			for (int i = 0; i < z_vector.length; i++){
				if (z_vector[i] == cluster){
					ids_cluster.add(new Integer(object_ids[i]));
				}
			}
			
			int [] ids_c = new int[ids_cluster.size()];
			for (int i = 0; i < ids_c.length; i++){
				ids_c[i]=ids_cluster.get(i).intValue();
			}
			
			objects_per_cluster.add(ids_c);
		}
	}
	
	public MOrder(int [] total_ordered_ids, ObjectInfo OI){
		this.object_ids=total_ordered_ids;
		this.num_clusters=total_ordered_ids.length;
		this.obj_I = OI;
		z_vector = new int[object_ids.length];
		for (int i = 0;i < object_ids.length;i++){
			z_vector[i]=i;
		}
		
		//store them data in the array list
		objects_per_cluster = new ArrayList<int[]>();
		for (int cluster = 0; cluster < num_clusters; cluster ++){
					
			ArrayList<Integer> ids_cluster = new ArrayList<Integer>();
			for (int i = 0; i < z_vector.length; i++){
				if (z_vector[i] == cluster){
					ids_cluster.add(new Integer(object_ids[i]));
				}
			}
					
			int [] ids_c = new int[ids_cluster.size()];
			for (int i = 0; i < ids_c.length; i++){
				ids_c[i]=ids_cluster.get(i).intValue();
			}
					
			objects_per_cluster.add(ids_c);
		}
	}
	
	public MOrder(int [] subset_ids, int [] subset_z, ObjectInfo OI, int num_c){
		this.obj_I=OI;
		this.object_ids = subset_ids;
		this.z_vector = subset_z;
		this.num_clusters=num_c;
		
		//store them data in the array list
				objects_per_cluster = new ArrayList<int[]>();
				for (int cluster = 0; cluster < num_clusters; cluster ++){
					
					ArrayList<Integer> ids_cluster = new ArrayList<Integer>();
					for (int i = 0; i < z_vector.length; i++){
						if (z_vector[i] == cluster){
							ids_cluster.add(new Integer(object_ids[i]));
						}
					}
					
					int [] ids_c = new int[ids_cluster.size()];
					for (int i = 0; i < ids_c.length; i++){
						ids_c[i]=ids_cluster.get(i).intValue();
					}
					
					objects_per_cluster.add(ids_c);
				}
		
		//UtilsJS.printArray(subset_ids);
		//UtilsJS.printArray(subset_z);
	}
	
	public int [] getIDs(){
		return object_ids;
	}
	
	public MOrder getSubsetOrder(int [] subset_ids){
		
		
		int [] subset_z = new int[subset_ids.length];
		for (int i = 0;i < subset_ids.length; i++){
			int index_i = UtilsJS.indexOf(object_ids, subset_ids[i]);
			
			subset_z[i]=z_vector[index_i];
		}
		
		//get unique values
		ArrayList<Integer> unique_z = new ArrayList<Integer>();
		for (int i = 0; i < subset_z.length; i++){
			Integer next_z = new Integer(subset_z[i]);
			if (!unique_z.contains(next_z)){
				unique_z.add(next_z);
			}
		}
		//System.out.println(unique_z);
		
		//reassign cluster values
		int [] new_z = new int[subset_z.length];
		for (int i =0; i < new_z.length; i++){
			Integer next_z = new Integer(subset_z[i]);
			int index = unique_z.indexOf(next_z);
			new_z[i]=index;
		}
	
		
		return new MOrder(subset_ids,new_z,obj_I,unique_z.size());
	}
	
	public int getObjectPosition(int object_id){
		int index = UtilsJS.indexOf(object_ids, object_id);
		return z_vector[index];
	}
	
	public int getHeight(int id_a, int id_b){
		int p_a = this.getObjectPosition(id_a);
		int p_b = this.getObjectPosition(id_b);
		
		return p_a-p_b;
	}
	
	public void print(){
		for (int i = 0; i < num_clusters; i++){
			System.out.println("Cluster "+i+":");
			int [] objs_i = objects_per_cluster.get(i);
			for (int j = 0; j < objs_i.length; j++){
				System.out.println("\tobject "+objs_i[j]+"\t"+obj_I.getObjectTag(objs_i[j]));
			}
		}
	}
	
}
