package com.limn.tool.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.tool.httpclient.StructureMethod;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.parser.Parser;
import com.limn.tool.parser.SyntaxTree;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

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
	
	/**
	 * 获取用例变量值，支持变量再次运算
	 * @param str 变量表达式
	 * @return 解析后的结果
	 * @throws ParameterException
	 */
	public static String getExpressionValue(String str) throws ParameterException {
//		if (!RegExp.findCharacters(str, "\\{.*?\\}")){
//			return str;
//		}
		String variableValue = null;
		String exp = Variable.resolve(str);
		SyntaxTree tree = new SyntaxTree();
		Parser parser = new Parser();
		String[] array = {};
		if (RegExp.findCharacters(exp, "&")){
			array = exp.split("&");
		}
		if (array.length == 0){
			try{
				variableValue = parser.eval(null, exp, tree, null).toString();
				if(null == variableValue){
					
				}
			}catch(Exception e){
				variableValue = exp;
			}
		}else{
			for(int i=0;i<array.length;i++){
				try{
					array[i] = parser.eval(null, array[i], tree, null).toString();
				}catch(Exception e){
					
				}finally{
					array[i] = "'" + array[i] + "'";
				}
			}
			String strArr = StringUtils.join(array,"&");
			try {
				variableValue = parser.eval(null, strArr, tree, null).toString();
			} catch (Exception e) {
				Print.log("语法解析失败，表达式："+str, 2);
				return exp;
//				throw new ParameterException("语法解析失败，表达式："+str);
			}
		}
		return variableValue;
	}

	/**
	 * 返回md5码
	 * @param value
	 * @return
	 */
	public static String getMD5(String value){
		if(null == value || value.isEmpty()){
			return value;
		}
		return EncryptUtil.md5(value);
	}
	
	/**
	 * 根据本地的环境自增
	 * @param digit
	 * @param key 标示
	 * @return
	 */
	public static String getAutoIncrement(int digit, String key){
		String number = null;
		String path = Parameter.DEFAULT_TEMP_PATH + "/" + key + ".txt";
		try {
			number = FileUtil.getFileText(path);

			number = String.valueOf(Integer.parseInt(number) + 1);
			
			FileUtil.setFileText(path, number);
		} catch (IOException e) {

		}
		
		int diff = digit - number.length();
		if(diff>0){
			String diffStr = "";
			for(int i = 0 ; i < diff ; i++){
				diffStr += "0";
			}
			number = diffStr + number;
		}else{
			number = number.substring(diff);
		}
		return number;
	}
	
	
	public static Long getParseTimeStamp(String datatime){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		
		Date date = null;
		try {
			date = simpleDateFormat.parse(datatime);
		} catch (ParseException e) {
			Print.log("时间格式有误" + datatime , 2);
		}
	    return date.getTime();
	} 
	
	public static String getIP(String name){
		InetAddress address = null;
		try {
			address = InetAddress.getByName(name);
		} catch (UnknownHostException e) {
			Print.log(e.getMessage(), 2);
			Print.log("获取失败",2);
		}
			return address.getHostAddress().toString();
		}

	
	public static void main(String[] args){
		HashMap<String,String> a = new HashMap<String, String>();
		a.put("aa", "2014-12-12 22:22:22");
		
		NameValuePair[] b= StructureMethod.getNameValuePair(a);
		System.out.println(getParseTimeStamp("2010-02-02 22:22:22"));
	}
	
	
}
