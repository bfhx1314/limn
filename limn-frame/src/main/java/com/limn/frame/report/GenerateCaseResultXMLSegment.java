package com.limn.frame.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.limn.tool.bean.RunParameter;
import com.limn.tool.common.FileUtil;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class GenerateCaseResultXMLSegment {

	public static void setXML(NewDictionary dicCaseInfo,LogEngine logEngine){
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try { 
			document = saxReader.read(new File(RunParameter.getResultPaht().getResultFolderReport() + "/ReportSource.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
//		'root append
		Element Detail = (Element) document.selectSingleNode("//Detail");
		// ===create node names===
		Element newTestCaseElement = Detail.addElement("TestCase");
		newTestCaseElement.addAttribute("用例名", dicCaseInfo.getValue("Case Name").toString());
		Element new_CaseName_Element = newTestCaseElement.addElement("CaseName");
		Element new_Number_Element = newTestCaseElement.addElement("Number");
		Element new_Asset_Element = newTestCaseElement.addElement("Asset");
		Element new_ErrorLog_Element = newTestCaseElement.addElement("ErrorLog");
		Element new_ProductMessage_Element = newTestCaseElement.addElement("ProductMessage");
		Element new_CaseStatus_Element = newTestCaseElement.addElement("CaseStatus");
		Element new_ErrorSnapshot_Element = newTestCaseElement.addElement("ErrorSnapshot");
		Element new_InputDatas_Element = newTestCaseElement.addElement("InputDatas");
		Element new_OutputDatas_Element = newTestCaseElement.addElement("OutputDatas");
		Element new_CheckPoints_Element = newTestCaseElement.addElement("CheckPoints");
		Element new_ItemList_Element = newTestCaseElement.addElement("ItemList");

		// '===Head information===
		new_CaseName_Element.addText(dicCaseInfo.getValue("Case Name").toString());
		new_Number_Element.addText(dicCaseInfo.getValue("No").toString());
//		new_Asset_Element.addText(dicCaseInfo.getValue("Asset").toString());
		new_ErrorLog_Element.addText(dicCaseInfo.getValue("Error Log").toString());
		new_ProductMessage_Element.addText(dicCaseInfo.getValue("Product message").toString());
		new_CaseStatus_Element.addText(dicCaseInfo.getValue("Case Status").toString());
		String errorSnapshotPath = "";
		try{
			errorSnapshotPath = dicCaseInfo.getValue("ErrorCapture").toString();
			if (RegExp.findCharacters(errorSnapshotPath, "\\\\")){
				errorSnapshotPath = errorSnapshotPath.replaceAll("\\\\", "/");
			}

		}catch(Exception e){
			errorSnapshotPath = "";
		}
//		String errorSnapshotPath = dicCaseInfo.getValue("ErrorSnapshot").toString().replaceAll("\\", "/");
		new_ErrorSnapshot_Element.addText(errorSnapshotPath);
		
//		'===inputs===
//		'print dic_CaseInfo.item("InputData").count
/*		NewDictionary inputData = (NewDictionary) dicCaseInfo.getValue("InputData");
		int inputDataLen = inputData.getSize();
		if (inputDataLen != 0){
			for(int i=0;i<inputDataLen;i++){
				Element new_InputData_Element = new_InputDatas_Element.addElement("InputData");
				Element new_FieldName_Element = new_InputData_Element.addElement("FieldName");
				Element new_FieldValue_Element = new_InputData_Element.addElement("FieldValue");
				// 'value
				new_FieldName_Element.addText(inputData.getKey(i).toString());
				new_FieldValue_Element.addText(inputData.getValue(i).toString());
			}
		}*/
		
//		'===outputs===
//		'print dic_CaseInfo.item("OutputData").count
/*		NewDictionary outputData = (NewDictionary) dicCaseInfo.getValue("OutputData");
		int outputDataLen = outputData.getSize();
		if (outputDataLen != 0){
			for(int i=0;i<outputDataLen;i++){
				Element new_OutputData_Element = new_OutputDatas_Element.addElement("OutputData");
				Element new_FieldName_Element = new_OutputData_Element.addElement("FieldName");
				Element new_FieldValue_Element = new_OutputData_Element.addElement("FieldValue");
				// 'value
				new_FieldName_Element.addText(outputData.getKey(i).toString());
				new_FieldValue_Element.addText(outputData.getValue(i).toString());
			}
		}*/
		
		//'==ItemList======
/*		NewDictionary itemList = (NewDictionary) dicCaseInfo.getValue("ItemList");
		int itemListLen = itemList.getSize();
		if (itemListLen != 0){
			//'ColumnName
			Element new_ColumnNames_Element = new_ItemList_Element.addElement("ColumnNames");
			for(int iColumnCount=0;iColumnCount<itemListLen;iColumnCount++){
				Element new_ColumnName_Element = new_ColumnNames_Element.addElement("ColumnName");
				new_ColumnName_Element.addText(itemList.getKey(iColumnCount).toString());
			}
			//'ColumnValue
			for(int iRowCount=1;iRowCount<=itemListLen;iRowCount++){
				Element new_ColumnValues_Element = new_ItemList_Element.addElement("ColumnValues");
				for(int iiColumnCount=0;iiColumnCount<itemListLen;iiColumnCount++){
					Element new_ColumnValue_Element = new_ColumnValues_Element.addElement("ColumnValue");
					@SuppressWarnings("unchecked")
					LinkedHashMap<Integer, String> CRitem =  (LinkedHashMap<Integer, String>) itemList.getValue(iiColumnCount);
					new_ColumnValue_Element.addText(CRitem.get(iRowCount).toString());
				}
			}
		}*/
		
//		'===checkpoints===
//		'print dic_CaseInfo.item("CaseResult").count
		NewDictionary caseResult = (NewDictionary) dicCaseInfo.getValue("CaseResult");
		if (null != caseResult){
			int caseResultLen = caseResult.getSize();
			if (caseResultLen != 0){
				for(int i=0;i<caseResultLen;i++){
					Element new_CheckPoint_Element = new_CheckPoints_Element.addElement("CheckPoint");
					
					Element new_CPSN_Element = new_CheckPoint_Element.addElement("CPSN");
					Element new_CPName_Element = new_CheckPoint_Element.addElement("CPName");
					Element new_CPTime_Element = new_CheckPoint_Element.addElement("CPTime");
					Element new_CPExpected_Element = new_CheckPoint_Element.addElement("CPExpected");
					Element new_CPActual_Element = new_CheckPoint_Element.addElement("CPActual");
					Element new_CPStatus_Element = new_CheckPoint_Element.addElement("CPStatus");
					Element new_CPSnapshot_Element = new_CheckPoint_Element.addElement("CPSnapshot");
					// 'value
					new_CPSN_Element.addText(String.valueOf(i));
					NewDictionary caseResultItem = (NewDictionary) caseResult.getValue(i);
					new_CPName_Element.addText(caseResultItem.getValue("CheckPoint Name").toString());
					new_CPTime_Element.addText(caseResultItem.getValue("Executed Time").toString());
					new_CPExpected_Element.addText(caseResultItem.getValue("Expected Result").toString());
					new_CPActual_Element.addText(caseResultItem.getValue("Actual Result").toString());
					new_CPStatus_Element.addText(caseResultItem.getValue("Status").toString());
					new_CPSnapshot_Element.addText(caseResultItem.getValue("Snapshot").toString());
//					String snapshot = Parameter.VERSNAPSHOT;
//					try{
//						snapshot = caseResultItem.getValue("Snapshot").toString().replaceAll("\\", "/");
//					}catch(Exception e){
//						
//					}
//					new_CPSnapshot_Element.addText(snapshot);
				}
			}
		}
		
		//'=======Test Log======='
		Element new_TestLog_Element = newTestCaseElement.addElement("TestLog");
		NewDictionary testLogContainer = logEngine.getDicTestLogContainer();//DIC_TestLogContainer;
		int testLogContainerLen = testLogContainer.getSize();
		if (testLogContainerLen != 0){
			for(int i=0;i<testLogContainerLen;i++){
				Element new_Log = new_TestLog_Element.addElement("Log");
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> lHashMap = (LinkedHashMap<String, String>) testLogContainer.getValue(i);
				new_Log.addAttribute("当前时间", lHashMap.get("CurrentTime"));
				new_Log.addAttribute("对象名", lHashMap.get("EventName"));
				new_Log.addAttribute("状态", lHashMap.get("Status"));
				new_Log.addText(lHashMap.get("EventContent"));
			}
		}
		FileUtil.setEmpty(RunParameter.getResultPaht().getResultFolderReport() +"/ReportSource.xml");
		XmlEngine.saveDocument(document, RunParameter.getResultPaht().getResultFolderReport() + "/ReportSource.xml");
	}

    
	public static void main(String[] args) {
		LogEngine.test();
		NewDictionary dicCaseInfo = new NewDictionary();
		dicCaseInfo.addItem("Case Name", "模块名");
		dicCaseInfo.addItem("No", "用例编号");
		dicCaseInfo.addItem("Error Log", "报错信息");
		dicCaseInfo.addItem("ErrorCapture", "报错截图");
		dicCaseInfo.addItem("Case Status", "Pass");
		// "测试用例结果集合"
		NewDictionary DIC_CaseResult = new NewDictionary();
		NewDictionary dic_CheckPoint = new NewDictionary();
		dic_CheckPoint.addItem("SN", "1");
		dic_CheckPoint.addItem("CheckPoint Name", "检查点");
		dic_CheckPoint.addItem("Executed Time", "10:10:10");
		dic_CheckPoint.addItem("Status", "Pass");
		dic_CheckPoint.addItem("Expected Result", "预期结果");
		dic_CheckPoint.addItem("Actual Result", "实际结果");
		dic_CheckPoint.addItem("Snapshot", "截图");
		
		DIC_CaseResult.addItem("检查点", dic_CheckPoint);
		dicCaseInfo.addItem("CaseResult", DIC_CaseResult);
		// InputData
		NewDictionary Dic_Input = new NewDictionary();
		Dic_Input.addItem("输入数据名1", "数据1");
		Dic_Input.addItem("输入数据名2", "数据2");
		dicCaseInfo.addItem("InputData", Dic_Input);
		
		// OutputData
		NewDictionary Dic_Output = new NewDictionary();
		Dic_Output.addItem("输出数据名1", "数据1");
		Dic_Output.addItem("输出数据名2", "数据2");
		dicCaseInfo.addItem("OutputData", Dic_Output);
		
		// ItemList
		NewDictionary Dic_Item_List = new NewDictionary();
		dicCaseInfo.addItem("ItemList", Dic_Item_List);
//		setXML("c:/testLog/ReportSource.xml",dicCaseInfo);

	}

}
