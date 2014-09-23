package com.haowu.interfacetest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.httpclient.StructureMethod;


/**
 * 
 * 合伙人APP
 * 
 * /hoss-org/hoss/orgMobile/login.do 登录接口
 * /hoss-org/hoss/orgMobile/addClientInfo.do 推荐客户预约楼盘
 * @author limn
 *
 */
public class HHRInterface {

	HttpClient client = new HttpClient();
	
	public HHRInterface(String ip, int port){
		client.getHostConfiguration().setHost(ip, port);
	}
	
	/**
	 * 登录
	 * @param phone 中介手机号码
	 * @param password 密码
	 */
	public void login(String phone, String password){
        NameValuePair[] param = {
        		new NameValuePair("phone", phone),
                new NameValuePair("password", password),

                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/hoss-org/hoss/orgMobile/login.do");

		System.out.println(StructureMethod.execute(client,method));
	}
	
	
	/**
	 * 推荐客户
	 * @param name 客户姓名
	 * @param phone 客户手机号码
	 * @param houseID houseID
	 * @param houseName houseName
	 */
	public void recommend(String name, String phone, String houseID, String houseName){
		
		JSONObject clientIntention = new JSONObject();
//		clientIntention.put("intentionQuyu", value);
//		clientIntention.put("intentionHuXing", value);
//		clientIntention.put("intentionMianJi", value);
//		clientIntention.put("intentionJiaWei", value);
//		clientIntention.put("intentionType", value);
//		clientIntention.put("intentionPurpose", "上海");
		
		JSONObject houseId = new JSONObject();
		
		houseId.put("houseId", houseID);
		houseId.put("houseName", houseName);
		houseId.put("type", "0");
		
		JSONArray houseBeans = new JSONArray();
		houseBeans.add(houseId);
		
		JSONObject info = new JSONObject();
		info.put("name", name);
		info.put("phone", phone);
		info.put("clientIntention", clientIntention);
		info.put("houseBeans", houseBeans);
		
		
        NameValuePair[] param = {
        		new NameValuePair("orgEntryClientRequest", info.toString()),

                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/hoss-org/hoss/orgMobile/addClientInfo.do");

		System.out.println(StructureMethod.execute(client,method));
	}

	
	public static void main(String[] args){
		HHRInterface hhrif = new HHRInterface("172.16.10.35", 19090);
		hhrif.login("15140827911", "123456");
		hhrif.recommend("中止", "15798789789", "3852", "ssss");
	}
	
}
