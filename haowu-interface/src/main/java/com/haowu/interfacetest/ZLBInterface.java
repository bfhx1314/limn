package com.haowu.interfacetest;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.external.JSONControl;
import com.limn.tool.httpclient.StructureMethod;

/** 
 * 助理宝app
 * /hoss-projectAssistant/hoss/projectAssistant/projectAssistantLogin.do?登录
 * /hoss-projectAssistant/hoss/projectAssistant/getGroupBuyClients.do?获取列表
 * /hoss-projectAssistant/hoss/projectAssistant/getProjectTypeList.do?获取房源类型
 * /hoss-projectAssistant/hoss/projectAssistant/commitGroupBuyClient.do?下定
 * @author xu
 */
public class ZLBInterface {
	
	HttpClient client = new HttpClient();
	String key = null;
	String projectId = null;
	String projectTypeId = null;
	String followId = null;
	String clientName = null;
	String clientPhone = null;

	// String[] projectTypeId;
	 
	 
	public ZLBInterface(String ip, int port) {
		client.getHostConfiguration().setHost(ip, port, "http");
	}

	/**
	 * 登录
	 * @param username 手机号
	 * @param password 密码
	 * hoss-projectAssistant/hoss/projectAssistant/projectAssistantLogin.do
	 */
	public void login(String username, String password) {
		String[] kp = new String[2];
		NameValuePair[] param = { new NameValuePair("username", username),
				new NameValuePair("password", password),

		};
		PostMethod postMethod = StructureMethod
				.getPostMethod(param,
						"/hoss-projectAssistant/hoss/projectAssistant/projectAssistantLogin.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		System.out.println(jsonResutls);
		HashMap<String, Object> map = JSONControl.getMapFromJson(jsonResutls);
//
		key = map.get("key").toString();
//		projectId = ((JSONArray) ((Map) map.get("data")).get("projectId")).toString();
//		String data = map.get("data").toString();
//		map = ResolveJSON.getMapFromJson(data);
//		projectId = map.get("projectId").toString();
//		kp[0] = key;
//		kp[1] = projectId;
//		return kp;

	}
	
	
	/**
	 * 获取列表
	 * @param 
	 * hoss-projectAssistant/hoss/projectAssistant/getGroupBuyClients.do
	 */
	private void list() {

		NameValuePair[] paramlist = {
				new NameValuePair("projectId", projectId),
				new NameValuePair("key", key),

		};
		PostMethod postMethod = StructureMethod
				.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/getGroupBuyClients.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		HashMap<String, Object> map = JSONControl.getMapFromJson(jsonResutls);
		followId = ((Map) ((JSONArray) ((Map) map.get("data")).get("dataList"))
				.get(0)).get("followId").toString();
		clientName = ((Map) ((JSONArray) ((Map) map.get("data"))
				.get("dataList")).get(0)).get("clientName").toString();
		clientPhone = ((Map) ((JSONArray) ((Map) map.get("data"))
				.get("dataList")).get(0)).get("clientPhone").toString();

	}
	
	
       /**
   	 * 获取房源类型
   	 * @param 
   	 * hoss-projectAssistant/hoss/projectAssistant/getProjectTypeList.do
   	 */
	private void ProjectType() {

		NameValuePair[] paramlist = {
				new NameValuePair("projectId", projectId),
				new NameValuePair("key", key),

		};
		PostMethod postMethod = StructureMethod
				.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/getProjectTypeList.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		HashMap<String, Object> map = JSONControl.getMapFromJson(jsonResutls);
		projectTypeId = ((Map) ((JSONArray) ((Map) map.get("data"))
				.get("content")).get(0)).get("projectTypeId").toString();
		System.out.println(projectTypeId);

	}
    

      /**
       * 
       * @param clientName
       * @param clientPhone
       * @param followId
       * @param projectTypeId
       * @param invoiceNo :团购发票号
       * @param contractNo :团购合同编号
       * @param contractPicId :合同照片  ：select * from document where type =2 ;
       * @param invoicePicId :收据照片 ：select * from document where type =1 ;
       * @param payWay :付款方式  ：case (现金)
       * @param receivedAmount :下定金额
       */
	public void determined(String clientName, String clientPhone, String followId, String projectTypeId, String invoiceNo, String contractNo,
			String contractPicId, String invoicePicId, String payWay,
			String receivedAmount) {

		NameValuePair[] paramlist = {
				new NameValuePair("invoiceNo", invoiceNo),
				new NameValuePair("invoiceAmount", "0"),
				new NameValuePair("posNos", null),
				new NameValuePair("contractNo", contractNo),
				new NameValuePair("contractPicId", contractPicId),
				new NameValuePair("key", key),
				new NameValuePair("projectTypeId", projectTypeId),
				new NameValuePair("invoicePicId", invoicePicId),
				new NameValuePair("payWay", payWay),
				new NameValuePair("followId", followId),
				new NameValuePair("clientPhone", clientPhone),
				new NameValuePair("clientName", clientName),
				new NameValuePair("receivedAmount", receivedAmount), };
		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/commitGroupBuyClient.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		System.out.println(jsonResutls);
		
	}
	
	

	/**
	 * 
	 * @param clientName 客户名称
	 * @param clientPhone 客户手机
	 * @param followId 
	 * @param roomNum 几栋几号
	 * @param dealArea 成交面积
	 * @param dealType 户型
	 * @param comments 备注
	 */
	public void deal(String clientName, String clientPhone, String followId, String roomNum 
			,String dealArea, String dealType, String comments){
		NameValuePair[] paramlist = {
				new NameValuePair("roomNum", roomNum),
				new NameValuePair("contractAmount", "0"),
				new NameValuePair("contractNo", null),
				new NameValuePair("key", key),
				new NameValuePair("comments", comments),
				new NameValuePair("dealArea", dealArea),
				new NameValuePair("followId", followId),
				new NameValuePair("clientPhone", clientPhone),
				new NameValuePair("clientName", clientName),
				new NameValuePair("dealType", dealType), };
		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/commitDealingClient.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		System.out.println(jsonResutls);
	}
    
	/**
	 * 报备
	 * @param type 成功 1  失败0
	 * @param followID
	 */
	public void filing(String type, String followID){
		NameValuePair[] paramlist = {
				new NameValuePair("key", key),
				new NameValuePair(type, type),
				new NameValuePair("followIds", followID) };
		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/commitFilingClients.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		System.out.println(jsonResutls);
	}


	/**
	 * 实例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ZLBInterface determined1 = new ZLBInterface("172.16.10.35", 19090);
		determined1.login("ywsy", "111111");
		determined1.list();
		determined1.ProjectType();
//		determined1.determined("535656565665565766", "5656565446565656",
//				"1000038000037", "1000038000038", "cash", "50000");

	}
}
