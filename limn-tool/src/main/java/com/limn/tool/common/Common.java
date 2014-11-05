package com.limn.tool.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.tool.parameter.Parameter;

public class Common {

	public static void wait(int sec){
		Long time = ((Integer) sec).longValue();
		try {
			Print.debugLog("Wait : " + sec, 0);
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getCodeByPicturePath(String filePath) throws ParameterException{
		return new GetCodeByPicture().identificationByPath(filePath);
	}
	
	public String getCodeByPictureIS(InputStream instr) throws ParameterException{
		return new GetCodeByPicture().identification(instr);
	}
	
	public String getCodeByPictureURL(String URL) throws ParameterException{
		return new GetCodeByPicture().identification(URL);
	}
	
	
	
	public static HashMap<String,String> getTemplateData(){
		XMLReader xmlr = new XMLReader(getTemplatePath(),true);
		return xmlr.getNodeValueByTemplateIndex(0);
	}
	
	public static void saveTemplateData(String key,String value){
		XMLReader xml = new XMLReader(Parameter.DEFAULT_TEMP_PATH + "\\Template.xml",true);
		try {
			xml.setNodeValueByTemplateIndex(0,key ,value );
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static String getTemplatePath() {
		String templatePath = null;
		File file = new File(Parameter.DEFAULT_TEMP_PATH + "\\Template.xml");
		// 判断系统目录下是否存在模板文件
		if (!file.exists()) {
			// 不存在就将jar包里的ParameterValues.xml复制到指定路径下
			File f = new File(Parameter.DEFAULT_TEMP_PATH);
			f.mkdirs();
			InputStream parameterPath = Common.class.getClassLoader().getResourceAsStream("data/ParameterValues.xml");
			try {
				Document document = new SAXReader().read(parameterPath);
				try {
					FileOutputStream out = new FileOutputStream(file);
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("utf-8");
					XMLWriter output = new XMLWriter(out, format);
					try {
						output.write(document);
						output.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} 
		
		templatePath = file.toString();
		return templatePath;
	}
}
