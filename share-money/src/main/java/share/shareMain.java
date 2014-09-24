package share;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.MyDate;


public class shareMain {
	
	public static void main(String[] args){
		
		ShareInterface SIF = new ShareInterface();
		Map<String,Double> activity = SIF.getHasMoneyByActivityID(52);
		List<Entry<String, Double>> sortActivity = sort(activity);
		
		ShareData sd = new ShareData("D:\\user.xls");
		String[][] data = sd.getData();
		
		int rowLine = 1;
		for (int i = 0; i < sortActivity.size(); i++) {
			String id = sortActivity.get(i).getKey();
			for(;rowLine<data.length;rowLine++){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    SIF.share(data[rowLine][0], data[rowLine][1], id);
			    sd.setShareTime(rowLine,MyDate.getDate());
//			    sd.set

			}
		}
	}
	

	
	
	
	
	
	
	private static List<Entry<String, Double>> sort(Map<String,Double> activity){
		List<Map.Entry<String, Double>> infoIds =
			    new ArrayList<Map.Entry<String, Double>>(activity.entrySet());

			//排序前
			for (int i = 0; i < infoIds.size(); i++) {
			    String id = infoIds.get(i).toString();
//			    System.out.println(id);
			}


			//排序
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Double>>() {   
			    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {      
			        return (int) (o2.getValue() - o1.getValue()); 
//			        return (o1.getKey()).toString().compareTo(o2.getKey());
			    }
			}); 

			//排序后
			for (int i = 0; i < infoIds.size(); i++) {
			    String id = infoIds.get(i).toString();
//			    System.out.println(id);
			}
			return infoIds;
		
	}
	

}
