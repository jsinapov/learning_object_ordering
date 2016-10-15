package features;

import java.util.ArrayList;

import data.raw.HapticRecord;

public interface IHapticFeatureGenerator {

	public void init(ArrayList<HapticRecord> data);
	public double [] extractFeatures(HapticRecord x);
	public int getDim();
	
}
