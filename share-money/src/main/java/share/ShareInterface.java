package share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import common.Common;
import common.HttpFixture;
import common.MyDate;
import common.ResolveJSON;

public class ShareInterface {
	
	
	private String loginKey = null;
	
	private String URL = "http://ios.haowu.com:83";;
	
	private HttpFixture hf = new HttpFixture();
	
	private int cityID = 1;
	
	private int WM = 4;
	public HashMap<String,Double> getHasMoneyByActivityID(int cityID){
		this.cityID = cityID;
		String data = getActivitiesByCity(cityID);
		HashMap<String,Object> hm = ResolveJSON.getMapFromJson(data);
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
			Common.sleepRandom(100, 200);
			String results = shareLink(ID);
			HashMap<String,Object> hm = ResolveJSON.getMapFromJson(results);
//			System.out.println(((Map)hm.get("data")).get("money"));
			info = (String) hm.get("detail");
			System.out.println(info);
		}
		return info;
		
		
	}
	
	private String shareLink(String ID){
		String keyURL = URL +"/hoss-society/app4/activity/sucessShare.do?activityId="
						+ ID +"&shareStatus=1&way=2&key="+ loginKey;
//		HashMap<String, Object> data = ResolveJSON.getMapFromJson(getResponse(keyURL));
		return getResponse(keyURL);
	}
	
	
	/**
	 * ��¼app
	 * @param username �ֻ�����
	 * @param password ����
	 * @return ���ص�¼��� boolean
	 */
	private boolean login(String username, String password){
		
		String keyURL = URL + "/hoss-society/app4/login/agentlogin.do?user_name="
				+ username + "&user_pass=" + password;

		HashMap<String, Object> data = ResolveJSON.getMapFromJson(getResponse(keyURL));
		
		
		if (data.get("status").equals("1")) {
			
			loginKey = (String) data.get("key");
			return true;
		}else {
			System.out.println(data.get("detail"));
			return false;
			
		}
		
	}
	
	private String getActivitiesByCity(int cityID){
		String url = URL + "/hoss-society/app4/activity/getActivities.do?cityId="
				+ cityID;
//		HashMap<String, Object> data = ResolveJSON.getMapFromJson(getResponse(url));
		return getResponse(url);
	}
	
	
	
	
	
	private String getResponse(String url) {
		hf.addHeaderValue("Content-Type", "application/json");
		hf.setUrl(url);
		hf.Get();
		return hf.getResponseBody();
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
				if(true) {
					

					//********share****************************************/
					ShareData sd = new ShareData("D:\\user.xls");
					String[][] data = sd.getData();
					int rowLine = 1;
					
					int num = 0;
					boolean[]  bool = new boolean[data.length];
					for (; rowLine < data.length; rowLine++) {
						
						Common.sleepRandom(10, 500);
						
						do {
							num = Common.random(1, data.length - 1);
						} while (bool[num]);
						
						System.out.println("run:" + num);
						bool[num] = true;
						sif.share(data[num][0], data[num][1], key);
						sd.setShareTime(num, MyDate.getDate());
						
						
					}
					//*****************************************************/
				}else{
					cancel.put(key, a.get(key));
				}
			}
			Common.sleepRandom(2000, 500);
			System.out.println(MyDate.getDateToString("yyyy-MM-dd HH:mm:ss"));
			
		}
	}
	

}
