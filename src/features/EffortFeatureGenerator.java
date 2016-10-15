package features;

import java.util.ArrayList;

import utils.FeatureUtils;
import utils.UtilsJS;
import data.raw.HapticRecord;

public class EffortFeatureGenerator implements IHapticFeatureGenerator {

	int num_joints;
	int num_temp_bins;
	
	
	public EffortFeatureGenerator(int nj, int num_temporal_bins){
		num_joints = nj;
		num_temp_bins = num_temporal_bins;
	}
	
	@Override
	public void init(ArrayList<HapticRecord> data) {
		//do nothing here
	}

	@Override
	public double[] extractFeatures(HapticRecord x) {
		
		double [][] efforts = x.getEfforts();
		
		ArrayList<double[]> data = new ArrayList<double[]>();
		for (int i = 0; i < efforts.length; i ++){
			data.add(efforts[i]);
		}
		
		double [][] st_matrix = FeatureUtils.computeBinnedFeatures(data, num_temp_bins);

		//UtilsJS.printMatrix(st_matrix);
		
		double [] f = new double[num_joints*num_temp_bins];
		int c=0;
		for (int i = 0; i < st_matrix.length; i++){
			for (int j = 0; j < st_matrix[i].length; j++){
				f[c]=st_matrix[i][j];
				c++;
			}
		}
		return f;
	}

	@Override
	public int getDim() {
		return num_joints*num_temp_bins;
	}

}
