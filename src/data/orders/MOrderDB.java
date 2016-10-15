package data.orders;

import java.util.ArrayList;
import java.util.HashMap;

import data.ObjectInfo;
import data.contextmodel.ContextData;
import data.contextmodel.DataPointBM;

public class MOrderDB {

	ArrayList<String[]> contexts;
	ArrayList<MOrder> orders;
	HashMap<String,MOrder> order_map;
	
	public MOrderDB(){
		
	}
	
	public ArrayList<String> getContextsNames(){
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < contexts.size(); i++){
			String [] c_i = contexts.get(i);
			names.add(new String(c_i[0]+"_"+c_i[1]));
		}
		return names;
	}
	
	public ArrayList<String[]> getContexts(){
		return contexts;
	}
	
	public void setContexts(ArrayList<String[]> c_list){
		contexts = new ArrayList<String[]>();
		contexts.addAll(c_list);
		orders = new ArrayList<MOrder>();
		order_map = new HashMap<String,MOrder>();
		
	}
	
	public void loadFromPath(String path, ObjectInfo OI){
		for (int i = 0; i < contexts.size(); i++){
			String [] context_i = contexts.get(i);
			String b_i = context_i[0];
			String m_i = context_i[1];
			
			//System.out.println("Loading order for context "+context_i[0]+"-"+context_i[1]);
			
			String z_order_filename = path+""+b_i+"-"+m_i+"_order_z.txt";
			String adj_matrix_filename = path+""+b_i+"-"+m_i+"_order_adjcluster.txt";
			
			MOrder O = new MOrder(z_order_filename,adj_matrix_filename,OI);
			
			orders.add(O);
			order_map.put(new String(b_i+"-"+m_i), O);
		}
	}
	
	public MOrder getOrderForContext(String [] c){
		return order_map.get(new String(c[0]+"-"+c[1]));
	}
	
	public MOrder getOrderForContext(String c){
		return order_map.get(c);
	}
	
	
	
	
	
}
