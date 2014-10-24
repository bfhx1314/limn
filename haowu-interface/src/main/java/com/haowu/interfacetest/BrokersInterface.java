
package com.haowu.interfacetest;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.httpclient.StructureMethod;



/**
 * 
 * 
 * 机构管理平台
 * 
 * /brokers/myClient/action 推荐接口 /brokers/login/brokerLogin 登录接口
 * 
 * @param ip
 * @param port
 * 
 * @author limn
 * 
 */
public class BrokersInterface {

	HttpClient client = new HttpClient();


	public BrokersInterface(String ip, int port) {
		client.getHostConfiguration().setHost(ip, port, "http");
	}


	/**
	 * 登录
	 * @param username 手机号
	 * @param password 密码
	 */
	public void login(String username, String password) {
		NameValuePair[] param = { new NameValuePair("phone", username),
				new NameValuePair("password", password),		};

		PostMethod postMethod = StructureMethod.getPostMethod(param,
				"/brokers/login/brokerLogin");
		try {
			client.executeMethod(postMethod);
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推荐用户
	 * @param username 推荐人姓名
	 * @param phone 推荐人手机号
	 * @param city 意向房源cityID
	 * @param area 意向房源城市的 片区ID
	 * @param houses houses ID
	 */
	public void recommend(String username, String phone, String city,
			String area, String houses) {
		NameValuePair[] param = { new NameValuePair("add", "2"),
				new NameValuePair("acreage", ""),
				new NameValuePair("intentionHuXing", ""),
				new NameValuePair("intentionType", ""),
				new NameValuePair("linked", ""),
				
				new NameValuePair("linkedPhone", ""),
				new NameValuePair("price", ""),
				
				new NameValuePair("area", area),
				new NameValuePair("city", city),
				new NameValuePair("clientName", username),
				new NameValuePair("clientPhone", phone),
				new NameValuePair("houses", houses),
				new NameValuePair("type", "0") };
		PostMethod postMethod = StructureMethod.getPostMethod(param,
				"/brokers/myClient/action");
		try {
			client.executeMethod(postMethod);
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 发起带看
	 * @param followID 客户的followID SQL:select id from client_follow_up_record where client_id in
	 * 			 (select id from client where phone = 手机号 )
	 */
	public void follow(String followID){
		NameValuePair[] param = { new NameValuePair("followId", followID)	};

		PostMethod postMethod = StructureMethod.getPostMethod(param,
				"/brokers/myClient/daikan");
		try {
			client.executeMethod(postMethod);
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 实例
	 * @param args
	 */
	public static void main(String[] args) {
		BrokersInterface or = new BrokersInterface("172.16.10.35", 90);
		or.login("15140827911", "123456");
		or.recommend("adx", "15123123119", "4", "48","12959");
//		or.recommend("aaaax", "15123123110", "22", "306","12941");
	}
}
