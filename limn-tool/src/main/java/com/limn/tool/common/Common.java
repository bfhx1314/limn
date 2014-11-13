package com.limn.tool.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.tool.httpclient.StructureMethod;
import com.limn.tool.parameter.Parameter;

public class Common {

	/**
	 * 等待
	 * @param sec 毫秒
	 */
	public static void wait(int sec){
		Long time = ((Integer) sec).longValue();
		try {
			Print.debugLog("Wait : " + sec, 0);
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据请求返回InputStream
	 * @param cookies 当前浏览器的cookies
	 * @param ip ip地址
	 * @param port 端口号
	 * @param requestURL 请求页面
	 * @return
	 */
	public static InputStream getISByRequest(String cookies, String ip, int port ,String requestURL){
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost(ip,port,"http");
		
		NameValuePair[] param = {};
		PostMethod recommennd = StructureMethod.getPostMethod(param,requestURL);
		
		recommennd.setRequestHeader("Cookie", cookies);

		try {
			client.executeMethod(recommennd);
			return recommennd.getResponseBodyAsStream();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * 返回OCR识别的验证码,注意有失败的几率
	 * @param filePath 图片路径
	 * @return
	 * @throws ParameterException
	 */
	public static String getCodeByPicturePath(String filePath) throws ParameterException{
		return new GetCodeByPicture().identificationByPath(filePath);
	}
	
	/**
	 * 返回OCR识别的验证码,注意有失败的几率
	 * @param instr 
	 * @return
	 * @throws ParameterException
	 */
	public static String getCodeByPictureIS(InputStream instr) throws ParameterException{
		return new GetCodeByPicture().identification(instr);
	}
	
	/**
	 * 
	 * @param URL
	 * @return
	 * @throws ParameterException
	 */
	public static String getCodeByPictureURL(String URL) throws ParameterException{
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
