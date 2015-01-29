package com.limn.tool.report;

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



public class LogEngine {

	public static NewDictionary DIC_TestLogContainer = null;
	private String strLogFilePath = null;
	private static int iEventIndex = -1;
	private String strActionName = null;
	
	public LogEngine(){
		iEventIndex = 0;
		DIC_TestLogContainer = new NewDictionary();
		strLogFilePath = "C:\\File Template" + "\\TestLog.xml";
		strActionName = "TESTXML"; // Testcase Name
		
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
			
			saveDocument(document,"c:/testLog/TestLog.xml");
			
		}
		
	}
    /** 
     * 将文档输出到文件保存，可指定格式化输出,可指定字符编码。 
     *  
     * @param document 
     * @param outputFile 
     */  
    public void saveDocument(Document doc, String outputPath) {  
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



