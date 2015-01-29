package com.limn.tool.report;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlEngine {
	
	private SAXReader saxReader = new SAXReader();
	private static Document document = null;
	public XmlEngine(String strXMLPath){
		try { 
			document = saxReader.read(strXMLPath);
		} catch (DocumentException e) {
			// TODO
			e.printStackTrace();
		}
	}
	public XmlEngine(){
		// TODO
	}
	
	public void update(String strXMLPath, NewDictionary dicPlanInfo){
		Element CurrentSAPguiVersion_TextNode = (Element) document.selectSingleNode("//SAPVersion");
		Element TestEnvironment_TextNode = (Element) document.selectSingleNode("//TestEnvironment");
		Element CompanyCode_TextNode = (Element) document.selectSingleNode("//CompanyCode");
		Element RunMode_TextNode = (Element) document.selectSingleNode("//RunMode");
		Element TestName_TextNode = (Element) document.selectSingleNode("//TestName");
		Element ExecutedOn_TextNode = (Element) document.selectSingleNode("//ExecutedOn");
		Element TotalScripts_TextNode = (Element) document.selectSingleNode("//TotalScripts");
		Element TimeZone_TextNode = (Element) document.selectSingleNode("//TimeZone");
		Element TestStartTime_TextNode = (Element) document.selectSingleNode("//TestStartTime");
		Element TestEndTime_TextNode = (Element) document.selectSingleNode("//TestEndTime");
		Element ContinueExecution_TextNode = (Element) document.selectSingleNode("//ContinueExecution");
		Element QCTestSet_TextNode = (Element) document.selectSingleNode("//ALMTestSet");
		Element QCInstance_TextNode = (Element) document.selectSingleNode("//ALMInstance");
		Element OverallStatus_TextNode = (Element) document.selectSingleNode("//OverallStatus");
		
		CurrentSAPguiVersion_TextNode.setText(dicPlanInfo.getValue("CurrentSAPguiVersion").toString());
		TestEnvironment_TextNode.setText(dicPlanInfo.getValue("TestEnvironment").toString());
		CompanyCode_TextNode.setText(dicPlanInfo.getValue("Company Code").toString());
		RunMode_TextNode.setText(dicPlanInfo.getValue("RunMode").toString());
		TestName_TextNode.setText(dicPlanInfo.getValue("QCTestName").toString());
		ExecutedOn_TextNode.setText(dicPlanInfo.getValue("ExecutedOn").toString());
		TotalScripts_TextNode.setText(dicPlanInfo.getValue("TotalScripts").toString());
		TimeZone_TextNode.setText(dicPlanInfo.getValue("TimeZone").toString());
		TestStartTime_TextNode.setText(dicPlanInfo.getValue("StartTime").toString());
		TestEndTime_TextNode.setText(dicPlanInfo.getValue("EndTime").toString());
		ContinueExecution_TextNode.setText(dicPlanInfo.getValue("QCTestSet").toString());
		QCTestSet_TextNode.setText(dicPlanInfo.getValue("QCInstance").toString());
		QCInstance_TextNode.setText(dicPlanInfo.getValue("ContinueExecution").toString());
		OverallStatus_TextNode.setText(dicPlanInfo.getValue("OverallStatus").toString());
	}
	
	public static NewDictionary load(String strXMLPath){

		Element CurrentSAPguiVersion_TextNode = (Element) document.selectSingleNode("//SAPVersion");
		Element TestEnvironment_TextNode = (Element) document.selectSingleNode("//TestEnvironment");
		Element CompanyCode_TextNode = (Element) document.selectSingleNode("//CompanyCode");
		Element RunMode_TextNode = (Element) document.selectSingleNode("//RunMode");
		Element TestName_TextNode = (Element) document.selectSingleNode("//TestName");
		Element ExecutedOn_TextNode = (Element) document.selectSingleNode("//ExecutedOn");
		Element TotalScripts_TextNode = (Element) document.selectSingleNode("//TotalScripts");
		Element TimeZone_TextNode = (Element) document.selectSingleNode("//TimeZone");
		Element TestStartTime_TextNode = (Element) document.selectSingleNode("//TestStartTime");
		Element TestEndTime_TextNode = (Element) document.selectSingleNode("//TestEndTime");
		Element ContinueExecution_TextNode = (Element) document.selectSingleNode("//ContinueExecution");
		Element QCTestSet_TextNode = (Element) document.selectSingleNode("//ALMTestSet");
		Element QCInstance_TextNode = (Element) document.selectSingleNode("//ALMInstance");
		
		NewDictionary dic_ResultHead = new NewDictionary();
		dic_ResultHead.addItem("CurrentSAPguiVersion", CurrentSAPguiVersion_TextNode.getText());
		dic_ResultHead.addItem("TestEnvironment", TestEnvironment_TextNode.getText());
		dic_ResultHead.addItem("CompanyCode", CompanyCode_TextNode.getText());
		dic_ResultHead.addItem("RunMode", RunMode_TextNode.getText());
		dic_ResultHead.addItem("TestName", TestName_TextNode.getText());
		dic_ResultHead.addItem("ExecutedOn", ExecutedOn_TextNode.getText());
		dic_ResultHead.addItem("TotalScripts", TotalScripts_TextNode.getText());
		dic_ResultHead.addItem("TimeZone", TimeZone_TextNode.getText());
		dic_ResultHead.addItem("TestStartTime", TestStartTime_TextNode.getText());
		dic_ResultHead.addItem("TestEndTime", TestEndTime_TextNode.getText());
		dic_ResultHead.addItem("ContinueExecution", ContinueExecution_TextNode.getText());
		dic_ResultHead.addItem("ALMTestSet", QCTestSet_TextNode.getText());
		dic_ResultHead.addItem("ALMInstance", QCInstance_TextNode.getText());
		return dic_ResultHead;
	}
	
	public static void main(String[] args) {
		XmlEngine.load("C:\\ReportSource.xml");
		System.out.print("");
	}

}
