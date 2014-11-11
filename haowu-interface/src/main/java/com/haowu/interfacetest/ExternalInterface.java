package com.haowu.interfacetest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.httpclient.StructureMethod;


/**
 * 
 * 外部接口
 * 
 * /ajaxNew/getVerifyByType/1 好屋分享获取验证码 
 * /m/share/yuyue_add 好屋分享预约
 * @author limn
 *
 */
public class ExternalInterface {

	HttpClient client = new HttpClient();
	
	private String IP = null;
	private int Port = 0;
	private String code = null;
	public ExternalInterface(String ip, int port) {
		client.getHostConfiguration().setHost(ip, port, "http");
		IP = ip;
		Port = port;

	}

//	/**
//	 * 获取好屋分享预约验证码
//	 * 调用 SystemInterface 的短信验证码接口
//	 * @param phone 预约手机号码
//	 * @return 验证码
//	 */
//	public String getShareVerCode(String phone,String code){
//		NameValuePair[] param = { 
//				new NameValuePair("phone", phone)
//		};
//		PostMethod postMethod = StructureMethod.getPostMethod(param,"/ajaxNew/getVerifyByType/1");
//		String results = StructureMethod.execute(client, postMethod);
//		if(results.equals("1")){
//			SystemInterface sif = new SystemInterface(IP, Port);
//			sif.login("admin", "123456");
//			code = sif.getCodeByPhone(phone);
//			System.out.println(code);
//		}
//		return code;
//	}
	
	
	/**
	 * 好屋分享预约
	 * @param brokerID 经纪人ID
	 * @param haowuID 好屋ID  project_activity 里面的haowu_id 要有活动
	 * @param name 预约客户姓名
	 * @param phone 预约客户手机号
	 */
	public void shareHaoWu(String brokerID, String haowuID, String name, String phone){
		NameValuePair[] param = { 
				new NameValuePair("broker_id", brokerID),
				new NameValuePair("code", code),
				new NameValuePair("haowu_id", haowuID),
				new NameValuePair("name", name),
				new NameValuePair("phone", phone),
				new NameValuePair("way","2" )
		};
		PostMethod postMethod = StructureMethod.getPostMethod(param,"/m/share/yuyue_add");
		String results = StructureMethod.execute(client, postMethod);
		System.out.println(results);
		
	}
	
	
	public void modify(String projectID){
		NameValuePair[] param = { 
				new NameValuePair("id", projectID)
		};
		PostMethod postMethod = StructureMethod.getPostMethod(param,"/test/renewal");
		String results = StructureMethod.execute(client, postMethod);
		System.out.println(results);
	}
	
	
	/**
	 * 实例
	 * @param args
	 */
	public static void main(String[] args){
		ExternalInterface eif = new ExternalInterface("172.16.10.35", 90);
//		eif.getShareVerCode("13131313131");
		eif.shareHaoWu("1003798003797", "3857", "待定", "13131313131");
	}
	
	
	
}
