package com.limn.frame.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.limn.tool.common.FileUtil;
import com.limn.tool.parameter.Parameter;

public class XmlEngine {
	
	private SAXReader saxReader = new SAXReader();
	private static Document document = null;
	public XmlEngine(){
		try { 
			document = saxReader.read(new File(Parameter.RESULT_FOLDER_REPORT + "/ReportSource.xml"));
		} catch (DocumentException e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	public void update(NewDictionary dicPlanInfo){
		Element CurrentProductName_TextNode = (Element) document.selectSingleNode("//ProductName");
//		Element CurrentProductguiVersion_TextNode = (Element) document.selectSingleNode("//ProductVersion");
		
		Element RunMode_TextNode = (Element) document.selectSingleNode("//RunMode");
		Element TestName_TextNode = (Element) document.selectSingleNode("//TestName");
		Element ExecutedOn_TextNode = (Element) document.selectSingleNode("//ExecutedOn");
		Element TestStartTime_TextNode = (Element) document.selectSingleNode("//TestStartTime");

		CurrentProductName_TextNode.setText(dicPlanInfo.getValue("ProductName").toString());
//		CurrentProductguiVersion_TextNode.setText(dicPlanInfo.getValue("ProductVersion").toString());
		RunMode_TextNode.setText(dicPlanInfo.getValue("RunMode").toString());
		TestName_TextNode.setText(dicPlanInfo.getValue("TestName").toString());
		ExecutedOn_TextNode.setText(dicPlanInfo.getValue("ExecutedOn").toString());
		TestStartTime_TextNode.setText(dicPlanInfo.getValue("StartTime").toString());

		FileUtil.setEmpty(Parameter.RESULT_FOLDER_REPORT+"/ReportSource.xml");
		saveDocument(document,Parameter.RESULT_FOLDER_REPORT + "/ReportSource.xml");
	}
	
	
	public void updateAtLast(NewDictionary dicPlanInfo){
		Element TestEnvironment_TextNode = (Element) document.selectSingleNode("//TestEnvironment");
		Element TestEndTime_TextNode = (Element) document.selectSingleNode("//TestEndTime");
//		Element ContinueExecution_TextNode = (Element) document.selectSingleNode("//ContinueExecution");
		Element OverallStatus_TextNode = (Element) document.selectSingleNode("//OverallStatus");
		Element OverallResult_TextNode = (Element) document.selectSingleNode("//OverallResult");
		Element RateOfExecutation_TextNode = (Element) document.selectSingleNode("//RateOfExecutation");
		
		TestEnvironment_TextNode.setText(dicPlanInfo.getValue("TestEnvironment").toString());
		TestEndTime_TextNode.setText(dicPlanInfo.getValue("EndTime").toString());
//		ContinueExecution_TextNode.setText(dicPlanInfo.getValue("ContinueExecution").toString());
		OverallStatus_TextNode.setText(dicPlanInfo.getValue("OverallStatus").toString());
		OverallResult_TextNode.setText(dicPlanInfo.getValue("OverallResult").toString());
		RateOfExecutation_TextNode.setText(dicPlanInfo.getValue("RateOfExecutation").toString());
		FileUtil.setEmpty(Parameter.RESULT_FOLDER_REPORT+"/ReportSource.xml");
		saveDocument(document,Parameter.RESULT_FOLDER_REPORT + "/ReportSource.xml");
	}
	
	
	public NewDictionary load(){
		
		Element CurrentProductName_TextNode = (Element) document.selectSingleNode("//ProductName");
//		Element CurrentProductguiVersion_TextNode = (Element) document.selectSingleNode("//ProductVersion");
		Element TestEnvironment_TextNode = (Element) document.selectSingleNode("//TestEnvironment");
		Element RunMode_TextNode = (Element) document.selectSingleNode("//RunMode");
		Element TestName_TextNode = (Element) document.selectSingleNode("//TestName");
		Element ExecutedOn_TextNode = (Element) document.selectSingleNode("//ExecutedOn");
		Element TestStartTime_TextNode = (Element) document.selectSingleNode("//TestStartTime");
		Element TestEndTime_TextNode = (Element) document.selectSingleNode("//TestEndTime");
		Element ContinueExecution_TextNode = (Element) document.selectSingleNode("//ContinueExecution");
		
		NewDictionary dic_ResultHead = new NewDictionary();
		dic_ResultHead.addItem("ProductName", CurrentProductName_TextNode.getText());
//		dic_ResultHead.addItem("ProductVersion", CurrentProductguiVersion_TextNode.getText());
		dic_ResultHead.addItem("TestEnvironment", TestEnvironment_TextNode.getText());
		dic_ResultHead.addItem("RunMode", RunMode_TextNode.getText());
		dic_ResultHead.addItem("TestName", TestName_TextNode.getText());
		dic_ResultHead.addItem("ExecutedOn", ExecutedOn_TextNode.getText());
		dic_ResultHead.addItem("TestStartTime", TestStartTime_TextNode.getText());
		dic_ResultHead.addItem("TestEndTime", TestEndTime_TextNode.getText());
		dic_ResultHead.addItem("ContinueExecution", ContinueExecution_TextNode.getText());
		return dic_ResultHead;
	}
    /** 
     * 将文档输出到文件保存，可指定格式化输出,可指定字符编码。 
     *  
     * @param document 
     * @param outputFile 
     */  
    public static void saveDocument(Document doc, String outputPath) {  
        // 输出文件  
        //File outputFile = new File(outputPath);  
        try {  
//            OutputFormat format = OutputFormat.createPrettyPrint();  
//             指定XML编码,不指定的话，默认为UTF-8  
//            format.setEncoding("UTF-8");  
//        	 此方法保存后中文乱码，要用"gb2312"编码
//            XMLWriter output = new XMLWriter(new FileWriter(outputFile), format);  
        	OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(outputPath),"utf-8");  
        	doc.write(output);  
            output.close();  
        } catch (IOException e) {  
            System.out.println(e.getMessage());  
        }  
    }   
	public static void main(String[] args) {
//		XmlEngine.load();
		System.out.print("");
	}

}
