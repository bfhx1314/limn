package share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import com.haowu.interfacetest.QQBInterface;
import com.limn.tool.common.Common;
import com.limn.tool.external.JSONControl;
import com.limn.tool.random.RandomData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import common.MyDate;


public class ShareInterface {
	
	
	private String loginKey = null;
	
	private String URL = "http://ios.haowu.com:83";;
	
	
	private int cityID = 1;
	
	private QQBInterface qqbif = new QQBInterface("ios.haowu.com", 83);
	
	private int WM = 4;
	public HashMap<String,Double> getHasMoneyByActivityID(int cityID){
		this.cityID = cityID;
		String data = getActivitiesByCity(cityID);
		HashMap<String,Object> hm = JSONControl.getMapFromJson(data);
		int size = ((JSONArray)((Map)hm.get("data")).get("content")).size();
		HashMap<String,Double> activity = new HashMap<String,Double>();
		for(int i =0;i<size;i++){
			
			double restMoney = Double.parseDouble(((JSONObject)((JSONArray)((Map)hm.get("data")).get("content")).get(i)).get("restMoney").toString());
			double shareMoney = Double.parseDouble(((JSONObject)((JSONArray)((Map)hm.get("data")).get("content")).get(i)).get("sharedMoney").toString());
			if (restMoney >0  && shareMoney >= WM) {
				String activityID = ((JSONObject)((JSONArray)((Map)hm.get("data")).get("content")).get(i)).get("activityId").toString();
				activity.put(activityID, shareMoney);
			}
			
		}
		return activity;
	}

	public String share(String username, String password,String ID){
		String info = "";
		if(login(username,password)){
			Common.wait(RandomData.getNumberRange(1, 2)/10);
			String results = shareLink(ID);
			HashMap<String,Object> hm = JSONControl.getMapFromJson(results);
//			System.out.println(((Map)hm.get("data")).get("money"));
			info = (String) hm.get("detail");
			System.out.println(info);
		}
		return info;
		
		
	}
	
	private String shareLink(String ID){
		return qqbif.shareActivites(ID);
	}
	
	
	private boolean login(String username, String password){
		return qqbif.login(username, password);
	}
	
	private String getActivitiesByCity(int cityID){
		
		return qqbif.getActivitesByCityID(String.valueOf(cityID), RandomData.getMAC());

	}
	
	

	public static void main(String[] arg){
		ShareInterface sif = new ShareInterface();
		HashMap<String,Double> cancel = new HashMap<String, Double>();
		while(true){
			
			HashMap<String, Double> a = sif.getHasMoneyByActivityID(52);
			for (String key : a.keySet()) {
		
				if(cancel.containsKey(key) && cancel.get(key).equals(a.get(key))){
					continue;
				}
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//				if(JOptionPane.showConfirmDialog(null, a.get(key), "Share", 0) == 0) {
//				if(true) {
					

					//********share****************************************/
					ShareData sd = new ShareData("D:\\user.xls");
					String[][] data = sd.getData();
					int rowLine = 1;
					
					int num = 0;
					boolean[]  bool = new boolean[data.length];
					for (; rowLine < data.length; rowLine++) {
						Common.wait(RandomData.getNumberRange(1, 50)/10);

						do {
							num = RandomData.getNumberRange(1, data.length - 1);
						} while (bool[num]);
						
						System.out.println("run:" + num);
						bool[num] = true;
						sif.share(data[num][0], data[num][1], key);
						sd.setShareTime(num, MyDate.getDate());
						
						
					}
					//*****************************************************/
//				}else{
//					cancel.put(key, a.get(key));
//				}
			}
			Common.wait(RandomData.getNumberRange(1, 2));
			System.out.println(MyDate.getDateToString("yyyy-MM-dd HH:mm:ss"));
			
		}
	}
	

}
