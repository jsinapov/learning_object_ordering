package data.orders;

public class OrderDistUtils {

	public static double averageHeightDiffNorm(MOrder a, MOrder b){
		double r = 0;
		
		int [] ids = a.object_ids;
		int c= 0;
		
		int length_a = a.num_clusters;
		int length_b = b.num_clusters;
		
		for (int i = 0; i < ids.length; i++){
			for (int j = 0; j < ids.length; j++){
				if (i!=j){
					r += Math.abs( ((double)a.getHeight(ids[i], ids[j]))/(double)length_a 
							-  ((double)b.getHeight(ids[i], ids[j])/(double)length_b));
					c++;
				}
			}
		}
		
		
		return r/(double)c;
	}
	
	public static double averageHeightDiff(MOrder a, MOrder b){
		double r = 0;
		
		int [] ids = a.object_ids;
		int c= 0;
		
		for (int i = 0; i < ids.length; i++){
			for (int j = 0; j < ids.length; j++){
				if (i!=j){
					r += Math.abs( a.getHeight(ids[i], ids[j]) - b.getHeight(ids[i], ids[j]));
					c++;
				}
			}
		}
		
		
		return r/(double)c;
	}
	
}
