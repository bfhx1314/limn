package com.haowu.test.apply_workflow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Properties;

import com.haowu.exception.HaowuException;
import com.haowu.parameter.ParameterHaowu;
import com.haowu.uitest.hossweb.HossWeb;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;

public class ApplyWorkFlowCommon {

	/**
	 * 审批 必须获取Flow_No
	 * 
	 * @param username
	 *            审批人用户名
	 * @param password
	 *            审批人密码
	 * @param comments
	 *            审批意见
	 * @param submit
	 *            提交名称
	 * @throws SeleniumFindException
	 * @throws HaowuException 
	 */
	public static void apply(String username, String password, String comments,
			String submit, String Flow_NO) throws SeleniumFindException, HaowuException {
//		if (username.equals(ParameterHaowu.Area)) {
//			return;
//		}
		
		RunLog.appStepsForTextArea("审批:" + username + ",单据编号:" + Flow_NO);
		
		RunLog.printLog("审批人:" + username, 0);

		HossWeb.login(username, password);

		HossWeb.menu("审批管理");
		HossWeb.list("待办审批");

		Common.wait(2);
		RunLog.printLog("单据编号:" + Flow_NO, 0);
		HossWeb.applyApproval(Flow_NO, comments, submit);

		HossWeb.logout();
	}

	
	public static void edit(String username, String password,LinkedHashMap<String, String> data, String type, String submit, String Flow_No)
			throws SeleniumFindException, HaowuException {
		RunLog.printLog("修改人:" + username, 0);
		RunLog.appStepsForTextArea("修改:" + username + ",单据编号:" + Flow_No);
		HossWeb.login(username, password);
		HossWeb.menu("审批管理");
		HossWeb.list("已发申请");

		RunLog.printLog("单据编号:" + Flow_No, 0);
		Common.wait(2);
		HossWeb.modifyApply(Flow_No, type, data, submit);

		HossWeb.logout();
	}

	public static void defen(String username, String password, String type,	String days, String Flow_No) 
			throws SeleniumFindException, HaowuException {

		RunLog.printLog("暂缓人:" + username, 0);
		RunLog.appStepsForTextArea("暂缓:" + username + ",单据编号:" + Flow_No);
		HossWeb.login(username, password);
		HossWeb.menu("审批管理");
		HossWeb.list("待办审批");

		RunLog.printLog("单据编号:" + Flow_No, 0);
		Common.wait(2);

		HossWeb.deferApply(Flow_No, type, days);

		HossWeb.logout();

	}

	public static String create(String username, String password, String type, LinkedHashMap<String, String> data, String submit)
			throws SeleniumFindException, HaowuException {

		RunLog.printLog("创建人:" + username, 0);
		RunLog.appStepsForTextArea("创建:" + username + ",单据类型:" + type);
		// 登录
		HossWeb.login(ParameterHaowu.Manager, "111111");
	
		
		// 操作
		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");

		return HossWeb.createApply(type, data, submit);

	}

	public static void delete(String applyType, String Flow_No)
			throws SeleniumFindException, HaowuException {

		
		HossWeb.list("我的草稿");

		HossWeb.deleteApply(Flow_No, applyType);

		HossWeb.logout();

	}

	public static void apply(String username, String password, String applyType, LinkedHashMap<String, String> data, String comments, String submit, String Flow_NO)
			throws SeleniumFindException, HaowuException {
		
		RunLog.appStepsForTextArea("审批:" + username + ",单据编号:" + Flow_NO);
		
		Print.log("审批人:" + username, 0);

		HossWeb.login(username, password);

		HossWeb.menu("审批管理");
		HossWeb.list("待办审批");

		Common.wait(2);
		Print.log("单据编号:" + Flow_NO, 0);
		
		HossWeb.applyApproval(Flow_NO, applyType, data, comments, submit);

		HossWeb.logout();
	}
	
	public static void see(String username, String password , String type ,String submit,String Flow_NO)
			throws HaowuException{
		Print.log("查看人:" + username ,0);
		RunLog.appStepsForTextArea("查看:" + username + ",单据编号:" + Flow_NO);
		HossWeb.login(username, password);
		HossWeb.menu("审批管理");
		HossWeb.list("已发申请");
		Print.log("单据编号:" + Flow_NO,0);
		Common.wait(2);
		
		try {
			HossWeb.seeApply(Flow_NO, type, submit);
		} catch (SeleniumFindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HossWeb.logout();
		
	}
	
	/**
	 * 记录单据编号
	 * @param type
	 * @param NO
	 */
	public static void recordBillNO(String type,String NO){
		
		type = ParameterHaowu.ProjectName + "." + type;
		Properties props = new Properties();
		InputStreamReader isr = null;
		
		try {
			isr = new InputStreamReader(new FileInputStream(Parameter.DEFAULT_TEMP_PATH + "/apply.properties"));
			props.load(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	
		props.put(type, NO);
		
		
		try {
			props.store(new FileOutputStream(Parameter.DEFAULT_TEMP_PATH + "/apply.properties"),"单据编号");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回记录的单据编号
	 * @param type
	 * @return
	 */
	public static String getBillNO(String type){
		
		type = ParameterHaowu.ProjectName + "." + type;
		
		Properties props = new Properties();
		InputStreamReader isr = null;
		
		try {
			isr = new InputStreamReader(new FileInputStream(Parameter.DEFAULT_TEMP_PATH + "/apply.properties"));
			props.load(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return props.getProperty(type);
	}

}
