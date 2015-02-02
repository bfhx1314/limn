package com.limn.frame.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.limn.tool.common.FileUtil;
import com.limn.tool.parameter.Parameter;



public class LogEngine {

	public static NewDictionary DIC_TestLogContainer = null;
	private String strLogFilePath = null;
	private static int iEventIndex = -1;
	private String strActionName = null;
	
	public LogEngine(){
		iEventIndex = 0;
		DIC_TestLogContainer = new NewDictionary();
		strLogFilePath = Parameter.RESULT_FOLDER_REPORT + "\\TestLog.xml";
		strActionName = Parameter.TESTNAME; // Testcase Name
		
	}
	
	public void logEvent(String strStatus, String strEventName, String strEvenContent){
		LinkedHashMap<String, String> lHashMap = new LinkedHashMap<String, String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String time = df.format(new Date());// new Date()为获取当前系统时间
		lHashMap.put("CurrentTime", time);
		lHashMap.put("EventName", strEventName); // 控件名
		lHashMap.put("EventContent", strEvenContent); // 针对控件的信息
		switch (strStatus.toUpperCase()){
		case "PASS":
			lHashMap.put("Status", "Pass");
			break;
		case "1":
			lHashMap.put("Status", "Pass");
			break;
		case "FAIL":
			lHashMap.put("Status", "Fail");
			break;
		case "0":
			lHashMap.put("Status", "Fail");
			break;
		case "WARNING":
			lHashMap.put("Status", "Warning");
			break;
		case "3":
			lHashMap.put("Status", "Warning");
			break;
		default:
			lHashMap.put("Status", "Done");
			break;
		}
 		DIC_TestLogContainer.addItem(iEventIndex, lHashMap);
		iEventIndex++;
	}
	
	public void generateLogSegment(){
		int iSize = DIC_TestLogContainer.getSize();
		if (DIC_TestLogContainer.getSize() == 0){
			return;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try { 
			document = saxReader.read(strLogFilePath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element testLog = (Element) document.selectSingleNode("//TestLog");
		Element new_Action = testLog.addElement("Action");
		new_Action.addAttribute("Name", strActionName);
//		Element new_Att_ActionName = document.add;
		for(int i=0;i<iSize;i++){
			Element new_Log = new_Action.addElement("Log");
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> lHashMap = (LinkedHashMap<String, String>) DIC_TestLogContainer.getValue(i);
			new_Log.addAttribute("CurrentTime", lHashMap.get("CurrentTime"));
			new_Log.addAttribute("EventName", lHashMap.get("EventName"));
			new_Log.addAttribute("Status", lHashMap.get("Status"));
			new_Log.addText(lHashMap.get("EventContent"));
			
			FileUtil.setEmpty(Parameter.RESULT_FOLDER_REPORT+"/TestLog.xml");
			XmlEngine.saveDocument(document,Parameter.RESULT_FOLDER_REPORT+"/TestLog.xml");
			
		}
		
	}

	public static void test() {
		LogEngine logEngine = new LogEngine();
		logEngine.logEvent("1","case1","tetsset111");
		logEngine.logEvent("0","case2","tetsset111");
		logEngine.logEvent("3","case3","tetsset111");
		logEngine.logEvent("1","case4","tetsset111");
		logEngine.generateLogSegment();
	}
	public static void main(String[] args) {
		LogEngine logEngine = new LogEngine();
		logEngine.logEvent("1","case1","tetsset111");
		logEngine.logEvent("0","case2","tetsset111");
		logEngine.logEvent("3","case3","tetsset111");
		logEngine.logEvent("1","case4","tetsset111");
		logEngine.generateLogSegment();
	}

}



