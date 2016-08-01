package com.limn.frame.results;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.limn.driver.Driver;
import com.limn.frame.control.ExecuteStatus;
import com.limn.frame.control.Test;
import com.limn.frame.report.GenerateCaseResultXMLSegment;
import com.limn.frame.report.LogEngine;
import com.limn.frame.report.NewDictionary;
import com.limn.frame.report.XmlEngine;
import com.limn.frame.testcase.TestCase;
import com.limn.tool.bean.RunParameter;
import com.limn.tool.common.Common;
import com.limn.tool.common.DateFormat;
import com.limn.tool.common.Print;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;


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
	 * 用例执行总数
	 */
	private int executedCase = 0;
	/**
	 * 用例通过总数
	 */
	private int successedCase = 0;
	/**
	 * 用例总数
	 */
	private int sumCaseCount = 0;
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
	 * 全部用例执行结果
	 */
	private boolean boolAllResult = true;
	/**
	 * 第一个错误的用例
	 */
	private String failCase = "";
	
	
	private TestCase tc = null;
	/**
	 * 初始化xml
	 */
	@Override
	public void init(TestCase tc){
		this.tc = tc;
		savePath = RunParameter.getResultPaht().getResultFolderWeb() + "/results.xml";
		document = DocumentHelper.createDocument();
		save();
		dicPlanInfoHead = new NewDictionary();
		addXmlHead();
		sumCaseCount = 0;
	}
	
	
	/**
	 * 添加Sheet页
	 */
	@Override
	public void addSheet(int index) {
		sheetElement = document.addElement("sheets");
		sheetElement.addAttribute("Index",String.valueOf(index));
		save();
		sumCaseCount = sumCaseCount + tc.getSheetLastRowNumber();
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
		dicCaseInfo.addItem("Case Name", RunParameter.getResultPaht().getTestCaseMoudle());
		if (null == caseNo || caseNo.equals("")){
			caseNo = String.valueOf(System.currentTimeMillis());
		}
		dicCaseInfo.addItem("No", caseNo);

		caseElement = moudleElement.addElement("TestCase");
		caseElement.addAttribute("CaseNo", caseNo);
		save();
		
		executedCase++;
//		int currentRow = Test.tc.getCurrentRow() + 1;
//		sumCaseCount = sumCaseCount -  (currentRow - executedCase) - 1;
		
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
		RunParameter.getResultPaht().setErrorLog("");
		RunParameter.getResultPaht().setErrorMessage("");
		RunParameter.getResultPaht().setErrorCapture("");
		RunParameter.getResultPaht().setCaseStatus(0);
		RunParameter.getResultPaht().setVerSnapshot("");
		RunParameter.getResultPaht().setCheckPointName("");
	}

	@Override
	public void addActualResults(String[] results) {
		Element actResultElement = null;
		for(String result:results){
			actResultElement = caseElement.addElement("ActualResult");
			actResultElement.setText(result);
		}
		save();

//		String strActual = addHtmlBr(results);
		String strActual = StringUtils.join(results,"\r\n");
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
		dicCheckPoint.addItem("CheckPoint Name", RunParameter.getResultPaht().getCheckPointName());
		dicCheckPoint.addItem("Executed Time", DateFormat.getDateToString());
//		String strExpected = addHtmlBr(results);
		String strExpected = StringUtils.join(results,"\r\n");
		dicCheckPoint.addItem("Expected Result", strExpected);
		dicCaseResult.addItem("检查点", dicCheckPoint);
		dicCaseInfo.addItem("CaseResult", dicCaseResult);
	}
	
	@Override
	public void addResult(boolean isPass){
		caseElement.addAttribute("Result", String.valueOf(isPass));
		String isPassS = "Fail";
		if (isPass){
			isPassS = "Pass";
		}
		dicCheckPoint.addItem("Status", isPassS);
		// TODO 验证截图路径
		dicCheckPoint.addItem("Snapshot", RunParameter.getResultPaht().getVerSnapshot());
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
	public void addTestCaseCount() {
		XmlEngine xmlEngine = new XmlEngine();
		RunParameter.getResultPaht().setEndTime(DateFormat.getDateToString());
		dicPlanInfoHead.addItem("EndTime", RunParameter.getResultPaht().getEndTime());
//		dicPlanInfoHead.addItem("OverallStatus", Parameter.OVERALLSTATUS);
		// 测试环境
		String url = RunParameter.getStartPaht().getURL();
		if(url == null || url.isEmpty()){
			dicPlanInfoHead.addItem("TestEnvironment", "App");
		}else{
			url = RunParameter.getStartPaht().getURL().replace("http://", "");
			url = url.split("/")[0];
			RunParameter.getResultPaht().setTestEnvironment(Common.getIP(url));
			dicPlanInfoHead.addItem("TestEnvironment", RunParameter.getResultPaht().getTestEnvironment());
		}
		
		
		sumCaseCount = tc.getAllCase();
		String rateOfExecutation = "执行率："+executedCase+"/"+sumCaseCount
										+"，"+Common.getNumPercent(executedCase, sumCaseCount)
									+"、通过率："+successedCase+"/"+sumCaseCount
										+"，"+Common.getNumPercent(successedCase, sumCaseCount)
									+"、执行通过率："+successedCase+"/"+executedCase
										+"，"+Common.getNumPercent(successedCase, executedCase)+"。";
		String resultStr = "";
		if (boolAllResult){
			resultStr = "通过。";
		}else{
			resultStr = "不通过："+failCase;
		}
		dicPlanInfoHead.addItem("OverallStatus", resultStr);
		dicPlanInfoHead.addItem("OverallResult", boolAllResult);
		dicPlanInfoHead.addItem("RateOfExecutation", rateOfExecutation);
		xmlEngine.updateAtLast(dicPlanInfoHead);
	}
	
	private void addXmlHead(){
		dicPlanInfoHead.addItem("ProductName", RunParameter.getResultPaht().getProductName());
		dicPlanInfoHead.addItem("ProductVersion", "暂时不用此字段");
		dicPlanInfoHead.addItem("RunMode", RunParameter.getStartPaht().getComputer());
		dicPlanInfoHead.addItem("TestName", RunParameter.getResultPaht().getTestName());
		dicPlanInfoHead.addItem("ExecutedOn", Parameter.getOS());
		dicPlanInfoHead.addItem("StartTime", RunParameter.getResultPaht().getStartTime());
		XmlEngine xmlEngine = new XmlEngine();
		xmlEngine.update(dicPlanInfoHead);
	}

	@Override
	public void addCaseReport() {
		// TODO 报错信息、需要增加区分错误等级
		dicCaseInfo.addItem("Error Log", RunParameter.getResultPaht().getErrorLog());
		// TODO 产品提示信息
		dicCaseInfo.addItem("Product message", RunParameter.getResultPaht().getErrorMessage());
		// TODO 单条用例执行结果，是否成功，非验证
		String result = "";
		if (RunParameter.getResultPaht().getCaseStatus() == ExecuteStatus.SUCCESS){
			successedCase++;
			result = "Pass";
		}else if(RunParameter.getResultPaht().getCaseStatus() == ExecuteStatus.FAILURE){
			result = "Error";
			if (boolAllResult){
				failCase = failCase + " " + dicCaseInfo.getValue("No").toString();
				boolAllResult = false;
			}
		}else{
			result = "Fail";
			if (boolAllResult){
				if (failCase.equals("")){
					failCase = dicCaseInfo.getValue("No").toString();
				}else{
					failCase = failCase +";"+ dicCaseInfo.getValue("No").toString();
				}
				boolAllResult = false;
			}
		}
		dicCaseInfo.addItem("Case Status", result);
		dicCaseInfo.addItem("ErrorCapture", RunParameter.getResultPaht().getErrorCapture());
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
		String logInfo = "";
		if (result == ExecuteStatus.SUCCESS){
//			logInfo = Parameter.LOGINFO;
//			Parameter.LOGINFO = "";
			if (!RegExp.findCharacters(step, "^验证")){
				result = 0;
			}
		}else{
			logInfo = RunParameter.getResultPaht().getErrorLog();
			RunParameter.getResultPaht().setErrorLog("");
		}
		String getExpessionValue = getExpValue(step);
		if (!logInfo.equals("")){
			logInfo = getExpessionValue + "\r\n" + logInfo;
		}else{
			logInfo = getExpessionValue;
		}
		logEngine.logEvent(String.valueOf(result),step,logInfo,RunParameter.getResultPaht().getLogSnapshot());
		RunParameter.getResultPaht().setErrorLog("");
	}
	
	private static String getExpValue(String content){
		String str = "";
		ArrayList<String> variableList = RegExp.matcherCharacters(content, "\\{.*?\\}");
		String varFormat = null;
		for(String var:variableList){
			varFormat = RegExp.filterString(var, "{}");
			String tempString =Variable.getExpressionValue(varFormat);
			str = str + var +"="+ tempString + ";";
		}
		return str;
	}
	
	public static void main(String[] args){
		getExpValue("表达式:{bIncrementNum}={cityName}");
	}


	@Override
	public int getCaseCount() {
		return sumCaseCount;
	}


	@Override
	public int getExecuteCaseCount() {
		return executedCase;
	}


	@Override
	public int getSucessCaseCount() {
		return successedCase;
	}
	
}
