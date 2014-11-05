package com.haowu.test.apply_workflow;


import java.util.LinkedHashMap;

import com.haowu.common.DataBase;
import com.haowu.exception.HaowuException;
import com.haowu.keyword.RunKeyWord;
import com.haowu.parameter.ParameterHaowu;
import com.haowu.uitest.hossweb.HossWeb;
import com.haowu.uitest.hossweb.Initialization;
import com.haowu.uitest.hossweb.apply.ApplyQDYJBZSQD;
import com.haowu.uitest.hossweb.apply.ApplyType;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.common.Print;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;


/**
 * 审批单流程
 * @author limn
 *
 */
public class ApplyWorkflowTest implements Runnable{
	
	private static String Flow_NO = null;
	private static String Fee_NO = null;
	private static String Goods_NO = null;
	private static String HT_NO = null;

	//审批单名称
	private static String applyType = "";
	
	//审批的标示
	private static String runType = null;
	
	//审批的需要的单据编号
	private static String tmpApplyType = null;
	
	public ApplyWorkflowTest(String[] steps){
	
		runType = steps[1];
		if(steps.length>2){
			tmpApplyType = steps[2];
		}
	}
	
	@Override
	public void run(){
		try{
			RunLog.appStepsForTextArea("分解步骤{");
			switch(runType){
			case ApplyType.QZQYS:
				test_QZQYS_wf();
				break;
			case ApplyType.YDYS:
				test_YDYS_wf();
				break;
			case ApplyType.FYSQ:
				
				test_FYSQ_wf();
				break;
			case ApplyType.FYBX:
				Fee_NO = tmpApplyType;
				test_FYBX_wf();
				break;
			case ApplyType.WPLY:
				test_WPSQ_wf();
				break;
			case ApplyType.WPHX:
				Goods_NO = tmpApplyType;
				test_WPHX_wf();
				break;
			case ApplyType.QDFJ:
				test_QDFJ_wf();
				break;
			case ApplyType.QDYJBZSQ:
				test_QDYJBZSQD_wf();
				break;
			case ApplyType.HTSP:
				Fee_NO = tmpApplyType;
				test_HTSP_wf();
				break;
			case ApplyType.HTFK:
				HT_NO = tmpApplyType;
				test_HTFK_wf();
				break;
			case ApplyType.TKSQD:
				test_XMTKD_wf();
				break;
			case ApplyType.BZJFK:
				test_XMBZJFKD_wf();
				break;
			default:
				Print.log("不存在的关键字:" + runType,2);
				break;
			}
			RunLog.appStepsForTextArea("}");
		}catch(Exception e){
			Print.debugLog(e.getMessage(), 2);
		}
		
	}
	
	public static void main(String[] args) {
		ApplyWorkflowTest a = new ApplyWorkflowTest(new String[]{"","项目全周期预算表"});
		ApplyWorkflow b = new ApplyWorkflow(false);
		a.init();
		try {
			a.test_QZQYS_wf();
//			a.test_YDYS_wf();
//
//			a.test_FYSQ_wf();
//			a.test_FYBX_wf();
//
//			a.FYSQ_wf();
			
//			Fee_NO = b.test_FYSQ_wf();
//			a.test_HTSP_wf();
//			a.test_HTFK_wf();
//
//			a.test_WPSQ_wf();
//			a.test_WPHX_wf();
//
//			a.test_QDYJBZSQD_wf();
//			a.test_QDFJ_wf();
//			a.test_XMBZJFKD_wf();
//			a.test_XMTKD_wf();
		} catch (Exception e) {
			Print.debugLog(e.getMessage(), 2);
		}
	}
	
	public void init(){
		new RunLog();
		Initialization.start();
		try {
			RunKeyWord.startBroswer(1, ParameterHaowu.HAOWU_URL, null);
		} catch (HaowuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parameter.DBDRIVER = "com.mysql.jdbc.Driver";
		Parameter.DBURL = "jdbc:mysql://172.16.10.250:3306/hoss_new_20141016_bak";
		Parameter.DBUSER = "hoss_test";
		Parameter.DBPASS = ParameterHaowu.General_Password;
	}
	

	/**
	 * 测试项目全周期预算表的工作流 
	 * 第一个执行的表单
	 * 
	 * <br>
	 *  (开始) -> 新增 -> 保存草稿 -> 提交审批 -> 退回 -> 提交审批 --> <br>
	 *  退回 -> 撤销 -> 提交审批 -> 同意 -> 终止 --><br>
	 *  新增 -> 提交审批 -> 同意 -> 暂缓 -> 同意 -> 同意 -> 同意 -> 同意 -> 同意 -> (结束)
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public void test_QZQYS_wf() throws SeleniumFindException, HaowuException {
		
		//录入数据*************************************************
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		data.put("cityId", ParameterHaowu.CityName);
		data.put("projectId", ParameterHaowu.ProjectName);
		data.put("budget_set_number", "1");
		data.put("early_input_costs", "1");
		data.put("sale_rate", "1");
		//*******************************************************
		
		applyType = "项目全周期预算表";	
		Print.log(applyType, 0);
		/*审批***********************************************/
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password,  applyType, data,"保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.edit(ParameterHaowu.Manager, ParameterHaowu.General_Password, null, applyType, "提交审批",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password,applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		//暂缓
		ApplyWorkFlowCommon.defen(ParameterHaowu.City, ParameterHaowu.General_Password, applyType, "10",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见B", "同意",Flow_NO);		
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	
	


	/**
	 *月度预算
	 *
	 *
	 *<br>
	 *(开始) -> 新增 -> 提交审批 -> 终止 -> 新增 -> 保存草稿 -> 删除 -> 新增 -> 保存草稿 ->
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public void test_YDYS_wf() throws SeleniumFindException, HaowuException{
		
		
		//录入数据*************************************************
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		data.put("cityId", ParameterHaowu.CityName);
		data.put("projectId", ParameterHaowu.ProjectName);
		//******************************************************
		
		
		applyType = "项目月度预算表";
		Print.log(applyType, 0);
		//审批***********************************************
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password,  applyType, data,"提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.deleteApply(Flow_NO, applyType);
		HossWeb.logout();
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.defen(ParameterHaowu.CWZG, ParameterHaowu.General_Password, applyType, "10",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZG, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		//审批***********************************************
	}
	
	
	
	/**
	 *项目费用申请单
	 * @throws SeleniumFindException 
	 * @throws HaowuException 
	 */
	public void test_FYSQ_wf() throws SeleniumFindException, HaowuException{
		
		
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		data.put("cityId", ParameterHaowu.CityName);
		data.put("projectId", ParameterHaowu.ProjectName);
		data.put("feeTypeCode", "抢钱宝/全媒体");
		data.put("amount", "20000");
		data.put("feeName", "什么费用");

		
		
		applyType = "项目费用申请单";
		//审批***********************************************
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.deleteApply(Flow_NO, applyType);
		HossWeb.logout();
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		Fee_NO = Flow_NO;
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.defen(ParameterHaowu.City, ParameterHaowu.General_Password, applyType, "10",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	
	/**
	 * 项目费用报销单
	 * 存在费用申请才可以报销
	 * @throws SeleniumFindException 
	 */
	public void test_FYBX_wf() throws SeleniumFindException, HaowuException{
		applyType = "项目费用报销单";
		Print.log("费用申请单:" + Fee_NO, 1);
		//登录
		HossWeb.login(ParameterHaowu.Manager, ParameterHaowu.General_Password);
		//操作
		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");
		//数据录入
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		data.put("citySelect", ParameterHaowu.CityName);
		data.put("projectSelect", ParameterHaowu.ProjectName);
		data.put("fee_apply", "[click]");
		data.put("//a[@data-flowno='" + Fee_NO + "']", "[click]");
		data.put("repayInfoList.feeInfoId", "[click]");
		data.put("repayInfoList.repayAmount", "5000");
//		data.put("projectRepay.bankAccountName", "测试人员");
		data.put("projectRepay.bankName", "测试银行");
		data.put("projectRepay.bankNo", "10486486548");
		
		
		
		Flow_NO = HossWeb.createApply("项目费用报销单", data, "提交审批");
		
		HossWeb.logout();
		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	
	/**
	 * 物品领用申请单
	 * @throws SeleniumFindException 
	 */
	public void test_WPSQ_wf() throws SeleniumFindException, HaowuException{
		
		applyType = "项目物品领用申请单";
		
		HossWeb.login(ParameterHaowu.Manager, ParameterHaowu.General_Password);

		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		data.put("citySelect", ParameterHaowu.CityName);
		data.put("projectSelect", ParameterHaowu.ProjectName);
		data.put("goodsName", "什么名字");
		data.put("goodsAmount", "500");

		
		Flow_NO = HossWeb.createApply(applyType, data, "提交审批");
		
		HossWeb.logout();

		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		
		Goods_NO = Flow_NO;

		LinkedHashMap<String, String> data1 = new LinkedHashMap<String, String>();
		data1.put("unitPrice", "1000");
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, applyType, data1, "没有意见", "同意",Flow_NO);
		
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.XZFZ, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.GLZX, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	
	/**
	 * 物品领用申请单
	 * @throws SeleniumFindException 
	 */
	public void test_WPHX_wf() throws SeleniumFindException, HaowuException{
		applyType = "项目物品核销单";
		HossWeb.login(ParameterHaowu.Manager, ParameterHaowu.General_Password);

		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");

//		Goods_No = "WPSQ-140830-002";
		
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		data.put("citySelect", ParameterHaowu.CityName);
		data.put("projectSelect", ParameterHaowu.ProjectName);
		data.put("goodsApplySelectBtn", "[click]");
		
//		Goods_NO = "WPSQ-141011-008";
		String sql = "select id from cm_pro_goods_apply  where flow_no='"+ Goods_NO +"'";
		String no = DataBase.executeSQL(sql)[0][0];
		
		data.put("//a[@goodsapplyid='" + no + "']", "[click]");
		data.put("cancelAmount", "100");
		data.put("proApplyGoodsCancel.remark", "只是为了触发器!艹");
		
		
		Flow_NO = HossWeb.createApply(applyType, data, "提交审批");
		
		HossWeb.logout();

		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.XZFZ, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.GLZX, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	
	
	
	/**
	 * 渠道任务分解
	 * @throws SeleniumFindException 
	 */
	public void test_QDFJ_wf() throws SeleniumFindException, HaowuException{
		applyType = "渠道任务分解";
		HossWeb.login(ParameterHaowu.Manager, ParameterHaowu.General_Password);

		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

		data.put("projectId", ParameterHaowu.ProjectName);
		
		Flow_NO = HossWeb.createApply(applyType, data, "提交审批");
		
		HossWeb.logout();
		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意", Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Business, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	/**
	 * 项目保证金付款单
	 * @throws SeleniumFindException 
	 */
	public void test_XMBZJFKD_wf() throws SeleniumFindException, HaowuException{
		
		applyType = "项目保证金付款单";
		
		HossWeb.login(ParameterHaowu.Manager, ParameterHaowu.General_Password);

		HossWeb.menu("审批管理");
		HossWeb.list("新建申请");

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		
		data.put("citySelect", ParameterHaowu.CityName);
		data.put("projectSelect", ParameterHaowu.ProjectName);
		data.put("bankName", "什银行");
		data.put("bankIdCardNo", "479849846449");
		data.put("payAmount", "500");
		
		Flow_NO = HossWeb.createApply(applyType, data, "提交审批");
		
		HossWeb.logout();
		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
	}
	
	
	
	
	/**
	 * 项目合同审批单
	 * 存在费用申请才可以
	 * @throws SeleniumFindException 
	 */
	public void test_HTSP_wf() throws SeleniumFindException, HaowuException{
		
		applyType = "项目合同审批单";
		
		//数据录入
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		data.put("//select[@id='cityId']", ParameterHaowu.CityName);
		data.put("//select[@id='projectId']", ParameterHaowu.ProjectName);
		data.put("fee_apply", "[Click]");
		
//		Fee_No = "FYSQ-141010-007";
		
		data.put("//a[@data-flowno='" + Fee_NO + "']", "[Click]");
		
		data.put("//input[@data-feename='什么费用']", "[Click]");
		
		data.put("cmApplyContract.contractName", "aaaaa");
		data.put("cmApplyContract.contractNo", "no11111");
		data.put("cmApplyContract.otherCompany", "aaaaacompany");
		data.put("cmApplyContract.contractName", "aaaaa");
		
		data.put("cmApplyContract.beginDate", "2014-09-02");
		
		data.put("cmApplyContract.endDate", "2014-10-02");
		data.put("cmApplyContract.remark", "11");
		data.put("amount", "20000");
		data.put("payDate0", "2014-09-10");
		
		
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		
//		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");

		HossWeb.list("我的草稿");
		
		HossWeb.deleteApply(Flow_NO, applyType);
		
		HossWeb.logout();
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		
		HossWeb.list("我的草稿");
		
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		
		HossWeb.logout();
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		
		HT_NO = Flow_NO;
		
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
		
	}
	
	/**
	 * 审批合同付款
	 * @throws SeleniumFindException 
	 */
	public void test_HTFK_wf() throws SeleniumFindException, HaowuException{
		applyType = "项目合同付款单";
		
		LinkedHashMap<String,String> data = new LinkedHashMap<String, String>();
		
		data.put("//select[@id='cityId']", ParameterHaowu.CityName);
		data.put("//select[@id='projectId']", ParameterHaowu.ProjectName);
		data.put("pay_contractno", "[click]");
		
		
//		HT_no = "HTSP-140913-022";
		
		String dataPid = DataBase.executeSQL("select id from cm_apply_contract  where flow_no = '"+HT_NO+"'")[0][0];
		data.put("//a[@data-pid='" + dataPid + "']", "[click]");
		data.put("bankAccountName", "aaaa");
		data.put("bankAccountName", "aaaa");
		data.put("bankName", "aaaa");
		data.put("xiaoxie", "20000");
		data.put("bankNo", "aaaa");
		
		
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password,applyType, data, "提交审批");
		HossWeb.logout();
		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.deleteApply(Flow_NO, applyType);
		HossWeb.logout();
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
//		ApplyWorkFlowCommon.apply(ParameterHaowu.yewu, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Chairman, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
	}
	


	/**
	 * 项目退款单
	 * @throws SeleniumFindException
	 */
	public void test_XMTKD_wf() throws SeleniumFindException, HaowuException{
		
		
		applyType = "项目退款申请单";
		LinkedHashMap<String,String> data = new LinkedHashMap<String, String>();
		data.put("citySelect", ParameterHaowu.CityName);
		data.put("projectSelect", ParameterHaowu.ProjectName);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		
		//审批***********************************************
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.deleteApply(Flow_NO, applyType);
		HossWeb.logout();
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
//		ApplyWorkFlowCommon.apply(ParameterHaowu.yewu, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Business, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		//审批***********************************************
	}
	/**
	 * 渠道佣金结算标准申请单
	 * @throws SeleniumFindException 
	 */
	public void test_QDYJBZSQD_wf() throws SeleniumFindException, HaowuException{
		applyType = "渠道佣金结算标准申请单";
		
		LinkedHashMap<String,String> data = new LinkedHashMap<String, String>();
		data.put("project", ParameterHaowu.ProjectName);
		ApplyQDYJBZSQD qd = new ApplyQDYJBZSQD();
		qd.setSociology(new String[] { "3","15" });
		qd.setPartner(new String[] { "4","17" });
		qd.setIntermediaryBase(new String[] { "2","2" }, new String[] { "33","34" });
		qd.setIntermediaryOther1(new String[] { "4","4" }, new String[] { "55","56" });
		qd.setIntermediaryOther2(new String[] { "66","67" });
		
		LinkedHashMap<String,String> dataTmp = qd.getData();
		for(String key : dataTmp.keySet()){
			data.put(key, dataTmp.get(key));
		}
		
		//审批***********************************************
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		//Flow_No = "QDBZ-140907-006";
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "终止",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.deleteApply(Flow_NO, applyType);
		HossWeb.logout();
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "保存草稿");
		HossWeb.list("我的草稿");
		HossWeb.modifyApply(Flow_NO, applyType, null, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "退回",Flow_NO);
		ApplyWorkFlowCommon.see(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, "撤销",Flow_NO);
		Flow_NO = ApplyWorkFlowCommon.create(ParameterHaowu.Manager, ParameterHaowu.General_Password, applyType, data, "提交审批");
		HossWeb.logout();
		ApplyWorkFlowCommon.apply(ParameterHaowu.Director, ParameterHaowu.General_Password, "没有意见", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.ZJGL, ParameterHaowu.General_Password, "全是意见", "同意",Flow_NO); 
		ApplyWorkFlowCommon.apply(ParameterHaowu.Business, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);//
		ApplyWorkFlowCommon.apply(ParameterHaowu.City, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.Area, ParameterHaowu.General_Password, "全是意见A", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.CWZJ, ParameterHaowu.General_Password, "全是意见D", "同意",Flow_NO);
		ApplyWorkFlowCommon.apply(ParameterHaowu.FGLD, ParameterHaowu.General_Password, "全是意见C", "同意",Flow_NO);
		//审批***********************************************
		
	}
	

	
	
	

}
