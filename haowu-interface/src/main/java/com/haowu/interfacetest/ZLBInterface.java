package com.haowu.interfacetest;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;

import com.limn.tool.common.Print;
import com.limn.tool.external.JSONReader;
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
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("助理宝登陆成功：" + username, 1);
			key = map.get("key").toString();
		}else{
			Print.log("助理宝登陆失败：" + username + "; error：" + map.get("detail"), 2);
			try {
				Print.log(postMethod.getURI().toString(), 2);
			} catch (URIException e) {
				e.printStackTrace();
			}
		}
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
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
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
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
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
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("下定成功", 1);
			key = map.get("key").toString();
		}else{
			Print.log("下定失败 ; error：" + map.get("detail"), 2);
		}
		
	}
	
	

	/**
	 * 
	 * @param clientName 客户名称
	 * @param clientPhone 客户手机
	 * @oaran contractAmount 成交金额
	 * @param followId 
	 * @param roomNum 几栋几号
	 * @param dealArea 成交面积
	 * @param dealType 户型
	 * @param comments 备注
	 */
	public void deal(String clientName, String clientPhone,String contractAmount, String followId, String roomNum 
			,String dealArea, String dealType, String comments,String contractNo){
		NameValuePair[] paramlist = {
				new NameValuePair("roomNum", roomNum),
				new NameValuePair("contractAmount", contractAmount),
				new NameValuePair("contractNo", contractNo),
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
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("成交成功", 1);
			key = map.get("key").toString();
		}else{
			Print.log("成交失败 ; error：" + map.get("detail"), 2);
		}
	}
    
	/**
	 * 报备
	 * @param type 成功 1  失败0
	 * @param followID
	 */
	public void filing(String type, String followID){
		NameValuePair[] paramlist = {
				new NameValuePair("key", key),
				new NameValuePair("type", type),
				new NameValuePair("followIds", followID) };
		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/commitFilingClients.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("报备成功", 1);
			key = map.get("key").toString();
		}else{
			Print.log("报备失败 ; error：" + map.get("detail"), 2);
		}
		
	}

	/**
	 * 到访确认
	 * @param verifyCode 验证码
	 * @param followID
	 */
	public void visit(String verifyCode,String followID){
		NameValuePair[] paramlist = {
				new NameValuePair("key", key),
				new NameValuePair("verifyCode", verifyCode),
				new NameValuePair("followId", followID) };
		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/checkVisitingClientVerifyCode.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("到访成功", 1);
			key = map.get("key").toString();
		}else{
			Print.log("到访失败 ; error：" + map.get("detail") + "verifyCode:" + verifyCode, 2);
		}
	}
	
	
	/**
	 * 
	 * @param amount
	 * @param bankCardNo
	 * @param name
	 * @param cityId
	 * @param bankName
	 */
	public void Refund(String amount, String bankCardNo,String name,String cityId, String bankName,String followId) {
		NameValuePair[] paramlist = {
				new NameValuePair("amount", amount),
				new NameValuePair("bankCardNo", bankCardNo),
				new NameValuePair("key", key),
				new NameValuePair("name", name),
				new NameValuePair("followId", followId),
				
				new NameValuePair("idCardPicId", "1000313000312"),
				new NameValuePair("handWritePicId", "1001802001808"),
				new NameValuePair("bankCardPicId", "1000772000785"),
				
				new NameValuePair("cityId", cityId),
				new NameValuePair("bankName", bankName) };

		PostMethod postMethod = StructureMethod.getPostMethod(paramlist,
						"/hoss-projectAssistant/hoss/projectAssistant/commitRefundClient.do");
		String jsonResutls = StructureMethod.execute(client, postMethod);
		
		HashMap<String, Object> map = JSONReader.getMapFromJson(jsonResutls);
		if(map.get("status").equals("1")){
			Print.log("退款成功" , 1);
			key = map.get("key").toString();
		}else{
			Print.log("退款失败; error：" + map.get("detail") , 2);
		}
		
			
			
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
