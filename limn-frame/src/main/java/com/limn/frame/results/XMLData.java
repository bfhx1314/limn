package com.limn.frame.results;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.limn.frame.report.GenerateCaseResultXMLSegment;
import com.limn.frame.report.LogEngine;
import com.limn.frame.report.NewDictionary;
import com.limn.frame.report.XmlEngine;
import com.limn.tool.common.DateFormat;
import com.limn.tool.parameter.Parameter;


public class XMLData implements DataResults{

	//dom
	private Document document = null;
	//sheet
	private Element sheetElement = null;
	//模块
	private Element moudleElement = null;
	//用例
	private Element caseElement = null;
	//步骤
	private Element stepElement = null;
	//存放路径
	private String savePath = null;
	/**
	 * 存放报告的head部分
	 */
	private NewDictionary dicPlanInfoHead = null;
	/**
	 * 存放报告需要的所有内容
	 */
	private NewDictionary dicCaseInfo = null;
	/**
	 * 存放用例结果
	 */
	private NewDictionary dicCaseResult = null;
	/**
	 * 存放检查点信息
	 */
	private NewDictionary dicCheckPoint = null;
	/**
	 * 每条用例的详细日志
	 */
	private LogEngine logEngine = null;
	/**
	 * 初始化xml
	 */
	@Override
	public void init(){
		savePath = Parameter.RESULT_FOLDER_WEB + "/results.xml";
		document = DocumentHelper.createDocument();
		save();
		dicPlanInfoHead = new NewDictionary();
		addXmlHead();
	}
	
	
	/**
	 * 添加Sheet页
	 */
	@Override
	public void addSheet(int index) {
		sheetElement = document.addElement("sheets");
		sheetElement.addAttribute("Index",String.valueOf(index));
		save();
	}
	
	/**
	 * 添加模块
	 */
	@Override
	public void addModule(String moduleName){
		moudleElement = sheetElement.addElement("ModuleName");
		moudleElement.addAttribute("ModuleName",moduleName);
		save();
	}
	
	
	/**
	 * 添加用例
	 */
	@Override
	public void addCase(String caseNo){
		logEngine = new LogEngine();
		dicCaseInfo = new NewDictionary();
		dicCaseInfo.addItem("Case Name", Parameter.TESTCASEMOUDLE);
		if (caseNo.equals("")){
			caseNo = String.valueOf(System.currentTimeMillis());
		}
		dicCaseInfo.addItem("No", caseNo);

		caseElement = moudleElement.addElement("TestCase");
		caseElement.addAttribute("CaseNo", caseNo);
		save();
	}
	
	
	/**
	 * 添加步骤
	 */
	@Override
	public void addStep(String stepName,String result){
		
//		dicCaseInfo.addItem(key, value);
		stepElement = caseElement.addElement("Step");
		stepElement.setText(stepName);
		stepElement.addAttribute("Result", result);
		save();
	}

	/**
	 * 初始化每条用例的信息环境变量
	 */
	private void initStepInfo(){
		Parameter.ERRORLOG = "";
		Parameter.PRODUCTMESSAGE = "";
		Parameter.ERRORCAPTURE = "";
		Parameter.CASESTATUS = 0;
		Parameter.VERSNAPSHOT = "";
	}

	@Override
	public void addActualResults(String[] results) {
		Element actResultElement = null;
		for(String result:results){
			actResultElement = caseElement.addElement("ActualResult");
			actResultElement.setText(result);
		}
		save();

		String strActual = addHtmlBr(results);
		dicCheckPoint.addItem("Actual Result", strActual);
	}


	@Override
	public void addExpectedResults(String[] results) {
		Element expResultElement = null;
		for(String result:results){
			expResultElement = caseElement.addElement("ExpectedResult");
			expResultElement.setText(result);
		}
		save();
		
		dicCaseResult = new NewDictionary();
		dicCheckPoint = new NewDictionary();
		dicCheckPoint.addItem("SN", dicCaseResult.getSize()+1);
		//TODO 检查点名
		dicCheckPoint.addItem("CheckPoint Name", "检查点");
		dicCheckPoint.addItem("Executed Time", DateFormat.getDateToString());
		String strExpected = addHtmlBr(results);
		dicCheckPoint.addItem("Expected Result", strExpected);
		dicCaseResult.addItem("检查点", dicCheckPoint);
		dicCaseInfo.addItem("CaseResult", dicCaseResult);
	}
	
	@Override
	public void addResult(boolean isPass){
		caseElement.addAttribute("Result", String.valueOf(isPass));
		dicCheckPoint.addItem("Status", isPass);
		// TODO 截图路径
		dicCheckPoint.addItem("Snapshot", Parameter.VERSNAPSHOT);
		dicCaseResult.addItem("检查点", dicCheckPoint);
		dicCaseInfo.addItem("CaseResult", dicCaseResult);
	}
	
	
	
	private void save() {
		try {
			Writer fileWriter = new FileWriter(savePath);
			XMLWriter xmlWriter = new XMLWriter(fileWriter);
			xmlWriter.write(document); // 写入文件中
			xmlWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	

	@Override
	public void addBitMap(String bitMapPath) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addRelatedCase(String CaseNo) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addCustom(String node, String value) {
		// TODO Auto-generated method stub
		
	} 


	@Override
	public void addTestCaseCount(String count) {
		XmlEngine xmlEngine = new XmlEngine();
		Parameter.ENDTIME = DateFormat.getDateToString();
		dicPlanInfoHead.addItem("EndTime", Parameter.ENDTIME);
		dicPlanInfoHead.addItem("OverallStatus", Parameter.OVERALLSTATUS);
		xmlEngine.update(dicPlanInfoHead);
	}
	
	private void addXmlHead(){
		dicPlanInfoHead.addItem("ProductName", Parameter.PRODUCTNAME);
		dicPlanInfoHead.addItem("ProductVersion", Parameter.PLATVERSION);
		dicPlanInfoHead.addItem("TestEnvironment", Parameter.TESTENVIRONMENT);
		dicPlanInfoHead.addItem("RunMode", Parameter.RUNMODE);
		dicPlanInfoHead.addItem("TestName", Parameter.TESTNAME);
		dicPlanInfoHead.addItem("ExecutedOn", Parameter.OS);
		dicPlanInfoHead.addItem("StartTime", Parameter.STARTTIME);
		XmlEngine xmlEngine = new XmlEngine();
		xmlEngine.update(dicPlanInfoHead);
	}

	@Override
	public void addCaseReport() {
		// TODO 报错信息、需要增加区分错误等级
		dicCaseInfo.addItem("Error Log", Parameter.ERRORLOG);
		// TODO 产品提示信息
		dicCaseInfo.addItem("Product message", Parameter.PRODUCTMESSAGE);
		// TODO 单条用例执行结果，是否成功，非验证
		dicCaseInfo.addItem("Case Status", Parameter.CASESTATUS);
		// TODO 报错时截图路径
		dicCaseInfo.addItem("ErrorCapture", Parameter.ERRORCAPTURE);
		//TODO 详细日志，需要在具体的步骤中增加

		GenerateCaseResultXMLSegment.setXML(dicCaseInfo,logEngine);
		// 详细日志
		logEngine.generateLogSegment();
		// 初始化
		initStepInfo();
	}
	
	/**
	 * 添加HTML换行符"<br></br>"
	 * @param results
	 * @return
	 */
	private String addHtmlBr(String[] results){
		int resultsLen = results.length;
		String str = "";
		if (resultsLen>1){
			str = results[0] + "<br>";
			for(int i=1;i<resultsLen-1;i++){
				str = str + results[i] + "</br><br>";
			}
			str = str + results[resultsLen-1] + "</br>";
		}else if(resultsLen == 1){
			str = results[0];
		}
		return str; 
	}


	@Override
	public void addCaseLog(String step, int result) {
		logEngine.logEvent(String.valueOf(result),step,Parameter.ERRORLOG);
		Parameter.ERRORLOG = "";
	}
}
