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
		dicCaseInfo = new NewDictionary();
		dicCaseInfo.addItem("Case Name", Parameter.TESTCASEMOUDLE);
		dicCaseInfo.addItem("No", Parameter.TESTCASENO);
		// TODO 报错信息
		dicCaseInfo.addItem("Error Log", Parameter.ERRORLOG);
		// TODO 产品提示信息
		dicCaseInfo.addItem("Product message", Parameter.PRODUCTMESSAGE);
		// TODO 报错时截图路径
		dicCaseInfo.addItem("ErrorCapture", Parameter.ERRORCAPTURE);
		// TODO 单条用例执行结果
		dicCaseInfo.addItem("Case Status", Parameter.CASESTATUS);
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

	

	@Override
	public void addActualResults(String[] results) {
		Element actResultElement = null;
		for(String result:results){
			actResultElement = caseElement.addElement("ActualResult");
			actResultElement.setText(result);
		}
		save();
	}


	@Override
	public void addExpectedResults(String[] results) {
		Element expResultElement = null;
		for(String result:results){
			expResultElement = caseElement.addElement("ExpectedResult");
			expResultElement.setText(result);
		}
		save();
	}
	
	@Override
	public void addResult(boolean isPass){
		caseElement.addAttribute("Result", String.valueOf(isPass));
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
		
	}


	@Override
	public void addCaseReport() {
		LogEngine.test();
//		NewDictionary dicCaseInfo = new NewDictionary();
		GenerateCaseResultXMLSegment.setXML(dicCaseInfo);
	}
	
}
