package features;

import java.util.ArrayList;

import utils.FeatureUtils;
import utils.UtilsJS;
import data.raw.HapticRecord;

public class EndFingerPositionFeatureGenerator implements IHapticFeatureGenerator {

	int num_joints;
	
	
	public EndFingerPositionFeatureGenerator(){
		num_joints = 2;
	}
	
	@Override
	public void init(ArrayList<HapticRecord> data) {
		//do nothing here
	}

	@Override
	public double[] extractFeatures(HapticRecord x) {
		
		double [][] fingers = x.getFingers();
		
		return fingers[fingers.length-1];
	}

	@Override
	public int getDim() {
		return 2;
	}

}
