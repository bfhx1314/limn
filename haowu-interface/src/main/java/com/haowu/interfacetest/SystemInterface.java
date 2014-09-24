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
 * Haowu后台 System
 * 
 * 
 * @author limn
 *
 */

public class SystemInterface {
	
	HttpClient client = new HttpClient();
	
	
	/**
	 * /system/login/do_login  登录界面接口
	 * /system/counts/smsLog  查看验证码接口
	 * @param ip
	 * @param port
	 */
	public SystemInterface(String ip , int port){
		client.getHostConfiguration().setHost(ip, port);
	}
	
	
	
	/**
	 * 登录
	 * @param username 用户帐号
	 * @param password 密码
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
	 * 根据手机号码 获取验证码
	 * @param phoneNumber 查询的手机号
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
	 * 新增楼盘
	 * @param house_name 楼盘名称
	 * @param house_city 所在城市
	 * @param house_area 所在区域
	 */
	public void addHouse(String house_name,String house_city,String house_area){
        NameValuePair[] param = {
        		new NameValuePair("house_name", house_name),
                new NameValuePair("house_state", "1"),
                new NameValuePair("house_city", house_city),
                new NameValuePair("house_area", house_area),
                new NameValuePair("sub", "提交"),
                new NameValuePair("app_tuijian", "0"),
                new NameValuePair("kefu_id", "1"),
                new NameValuePair("house_tj", "0"),
                new NameValuePair("info_decoration", "毛坯")
                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/system/house/add");

		System.out.println(StructureMethod.execute(client,method));
	}
	
	/**
	 * 楼盘上下架
	 * @param houseID
	 * @param type 2上架 1下架
	 */
	public void updateHouseStatus(String houseID,String type){
        NameValuePair[] param = {
                };  
		PostMethod method = StructureMethod.getPostMethod(param, "/system/house/sj/"+houseID+"/"+type+"/1");
		System.out.println(StructureMethod.execute(client,method));

	}
	
	
	/**
	 * 实例
	 * @param args
	 */
	public static void main(String[] args){
		SystemInterface vc = new SystemInterface("172.16.10.35",90);
		vc.login("admin", "123456");
		System.out.println(vc.getCodeByPhone("13654654654"));
	}
	
}
