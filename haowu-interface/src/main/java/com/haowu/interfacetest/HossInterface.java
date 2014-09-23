package com.haowu.interfacetest;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.httpclient.StructureMethod;

public class HossInterface {
	
	HttpClient client = new HttpClient();
	
	public HossInterface(String ip, int port){
		client.getHostConfiguration().setHost(ip,port,"http");
	}
	

	
	
	/**
	 * 登录
	 * @param username 手机号
	 * @param password 密码
	 */
	public void login(String username, String password){
		
        NameValuePair[] param = {
        		new NameValuePair("password", password),
                new NameValuePair("username", username)
                };
        
        PostMethod login = StructureMethod.getPostMethod(param, "/hoss/sys/v2/Login.do"); 
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
	
	
	
	
	public void addAgentSales(String username, String phone, String projectID){
        NameValuePair[] param = {
                new NameValuePair("agentName", username),
                new NameValuePair("phone", phone),
                new NameValuePair("projectId", projectID)
                }; 
		PostMethod recommennd = StructureMethod.getPostMethod(param, "/hoss-web/hoss/sys/pa/addAgentSales.do");
		
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
		HossInterface rc = new HossInterface("172.16.10.35", 19090);
		rc.login("admin", "987654");
		rc.addAgentSales("青年", "13838384381", "12944");

	}
}
