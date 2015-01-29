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
	 * 存放报告需要的所有内容
	 */
	public static NewDictionary dicCaseInfo = null;
	/**
	 * 初始化xml
	 */
	@Override
	public void init(){
		savePath = Parameter.RESULT_FOLDER_WEB + "/results.xml";
		document = DocumentHelper.createDocument();
		save();
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
		caseElement = moudleElement.addElement("TestCase");
		caseElement.addAttribute("CaseNo", caseNo);
		save();
	}
	
	
	/**
	 * 添加步骤
	 */
	@Override
	public void addStep(String stepName,String result){
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
		// TODO Auto-generated method stub
	}
	
	public static void addTestCaseReport(NewDictionary dicCaseInfo){
		LogEngine.test();
//		NewDictionary dicCaseInfo = new NewDictionary();
		GenerateCaseResultXMLSegment.setXML(dicCaseInfo);
	}
	
	
}
