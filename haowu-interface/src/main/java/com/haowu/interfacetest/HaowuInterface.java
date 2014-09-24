package com.haowu.interfacetest;


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.httpclient.StructureMethod;

/**
 * 
 * Haowu主页
 * 
 * /ajax/login 登录接口
 * /ajax/sendqrd1 社会经济人推荐
 * @author limn
 *
 */
public class HaowuInterface {
	
	HttpClient client = new HttpClient();
	
	public HaowuInterface(String ip, int port){
		client.getHostConfiguration().setHost(ip,port,"http");
	}
	

	
	
	/**
	 * 登录
	 * @param username 手机号
	 * @param password 密码
	 */
	public void login(String username, String password){
		
        NameValuePair[] param = {
        		new NameValuePair("Rpass", password),
                new NameValuePair("Ruser", username)
                };
        
        PostMethod login = StructureMethod.getPostMethod(param, "/ajax/login"); 
        login.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		
		
		try {
			client.executeMethod(login);
			System.out.println(login.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 推荐用户
	 * @param mobile 推荐人手机号
	 * @param name 推荐人姓名
	 * @param houseID houseID
	 * @param houseName 推荐房源名称
	 */
	public void recommennd(String mobile, String name, String houseID, String houseName){
        NameValuePair[] param = {
                new NameValuePair("customersmobile", mobile),
                new NameValuePair("customersname", name),
                new NameValuePair("houseid", houseID),
                new NameValuePair("housename", houseName),
                new NameValuePair("yxcs", "4"),
                new NameValuePair("yxjg", "1"),
                new NameValuePair("yxqy", "48")
                }; 
		PostMethod recommennd = StructureMethod.getPostMethod(param, "/ajax/sendqrd1");
		
		try {
			client.executeMethod(recommennd);
			System.out.println(recommennd.getResponseBodyAsString());
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
	public static void main(String[] args){
		HaowuInterface rc = new HaowuInterface("172.16.10.35", 90);
		rc.login("15140902001", "a123456");
//		rc.addAgentSales("青年", "13838384381", "12944");
		rc.recommennd("15140826302", "shehui1", "12959", "limn-5");
	}

}
