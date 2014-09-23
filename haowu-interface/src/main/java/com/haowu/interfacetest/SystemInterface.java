package com.haowu.interfacetest;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



import com.limn.tool.httpclient.StructureMethod;
import com.limn.tool.regexp.RegExp;

/**
 * 
 * Haowu鍚庡彴 System
 * 
 * 
 * @author limn
 *
 */

public class SystemInterface {
	
	HttpClient client = new HttpClient();
	
	
	/**
	 * /system/login/do_login  鐧诲綍鐣岄潰鎺ュ彛
	 * /system/counts/smsLog  鏌ョ湅楠岃瘉鐮佹帴鍙�
	 * @param ip
	 * @param port
	 */
	public SystemInterface(String ip , int port){
		client.getHostConfiguration().setHost(ip, port);
	}
	
	
	
	/**
	 * 鐧诲綍
	 * @param username 鐢ㄦ埛甯愬彿
	 * @param password 瀵嗙爜
	 */
	public void login(String username, String password){
        NameValuePair[] param = {
        		new NameValuePair("submit", "1"),
                new NameValuePair("user_name", username),
                new NameValuePair("user_password", password),
                new NameValuePair("x", "19"),
                new NameValuePair("y", "13"),
                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/system/login/do_login");

		System.out.println(StructureMethod.execute(client,method));

	}
	
	
	/**
	 * 鏍规嵁鎵嬫満鍙风爜 鑾峰彇楠岃瘉鐮�
	 * @param phoneNumber 鏌ヨ鐨勬墜鏈哄彿
	 * @return
	 */
	public String getCodeByPhone(String phoneNumber) {
        NameValuePair[] param = {
                new NameValuePair("phone_number", phoneNumber)
                }; 
        
        PostMethod method = StructureMethod.getPostMethod(param, "/system/counts/smsLog");
		String res = StructureMethod.execute(client, method);

		Document doc = Jsoup.parse(res);

		Elements ele = doc.getElementsByClass("noborder");

		String codeString = ele.get(1).text();

		String code = RegExp.matcherCharacters(codeString, "\\d{4,}").get(0);
		
		return code;

	}
	/**
	 * 鏂板妤肩洏
	 * @param house_name 妤肩洏鍚嶇О
	 * @param house_city 鎵�鍦ㄥ煄甯�
	 * @param house_area 鎵�鍦ㄥ尯鍩�
	 */
	public void addHouse(String house_name,String house_city,String house_area){
        NameValuePair[] param = {
        		new NameValuePair("house_name", house_name),
                new NameValuePair("house_state", "1"),
                new NameValuePair("house_city", house_city),
                new NameValuePair("house_area", house_area),
                new NameValuePair("sub", "鎻愪氦"),
                new NameValuePair("app_tuijian", "0"),
                new NameValuePair("kefu_id", "1"),
                new NameValuePair("house_tj", "0"),
                new NameValuePair("info_decoration", "姣涘澂")
                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/system/house/add");

		System.out.println(StructureMethod.execute(client,method));
	}
	
	/**
	 * 妤肩洏涓婁笅鏋�
	 * @param houseID
	 * @param type 2涓婃灦 1涓嬫灦
	 */
	public void updateHouseStatus(String houseID,String type){
        NameValuePair[] param = {
                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/system/house/sj/"+houseID+"/"+type+"/1");
		System.out.println(StructureMethod.execute(client,method));

	}
	
	
	/**
	 * 瀹炰緥
	 * @param args
	 */
	public static void main(String[] args){
		SystemInterface vc = new SystemInterface("172.16.10.35",90);
		vc.login("admin", "123456");
		System.out.println(vc.getCodeByPhone("13654654654"));
	}
	
}
