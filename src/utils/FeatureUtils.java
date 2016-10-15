package utils;

import java.util.ArrayList;

public class FeatureUtils {
	
	public static double [][] computeBinnedFeaturesNormalized(ArrayList<double[]> data, int t_bins){
		int num_joints=data.get(0).length;
		
		double [][] A = new double[data.size()][num_joints];
		for (int i = 0; i < data.size(); i++){
			double [] x_i = data.get(i);
			for (int j = 0; j < x_i.length; j++){
				A[i][j]=x_i[j];
			}
		}
		
		double [][] F = new double[t_bins][num_joints];
		
		double bin_t = (double)data.size()/(double)t_bins;
		
		for (int j = 0; j < num_joints; j++){
			double sum = 0;
			int current_bin = 0;
			double bin_counter = 0;
			
			for (int t = 0; t < A.length; t++){
				sum+= A[t][j];
				bin_counter+=1.0;
				
				if (bin_counter > bin_t || t == A.length-1){
					F[current_bin][j]=sum;
					sum=0;
					current_bin++;
					bin_counter = bin_counter-bin_t;
				}
			}
		}
		
		for (int bin = 0; bin < A.length; bin++){
			for (int i = 0; i < A[bin].length;i++){
				A[bin][i]=A[bin][i]/A[0][i];
			}
		}
		
		return F;
	}
	
	public static double [][] computeBinnedFeatures(ArrayList<double[]> data, int t_bins){
		int num_joints=data.get(0).length;
		
		double [][] A = new double[data.size()][num_joints];
		for (int i = 0; i < data.size(); i++){
			double [] x_i = data.get(i);
			for (int j = 0; j < x_i.length; j++){
				A[i][j]=x_i[j];
			}
		}
		
		double [][] F = new double[t_bins][num_joints];
		
		double bin_t = (double)data.size()/(double)t_bins;
		
		for (int j = 0; j < num_joints; j++){
			double sum = 0;
			int current_bin = 0;
			double bin_counter = 0;
			
			int additions_counter = 0;
			
			for (int t = 0; t < A.length; t++){
				sum+= A[t][j];
				additions_counter++;
				bin_counter+=1.0;
				
				if (bin_counter > bin_t || t == A.length-1){
					F[current_bin][j]=sum/((double)additions_counter);
					additions_counter=0;
					sum=0;
					current_bin++;
					bin_counter = bin_counter-bin_t;
				}
			}
		}
		
		
		return F;
	}
}