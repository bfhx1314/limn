package com.automation.tool.util;

import com.automation.exception.ExceptionInfo;
import com.automation.exception.RunException;
import com.automation.tool.parameter.Parameters;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.HashMap;

public class Common {
	
	public static HashMap<String,String> getTemplateData(){
		XMLReader xmlr = new XMLReader(getTemplatePath(),true);
		return xmlr.getNodeValueByTemplateIndex(0);
	}
	
	public static void saveTemplateData(String key,String value){
		XMLReader xml = new XMLReader(Parameters.DEFAULT_TEMP_PATH + "\\Template.xml",true);
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
		File file = new File(Parameters.DEFAULT_TEMP_PATH + "\\Template.xml");
		// 判断系统目录下是否存在模板文件
		if (!file.exists()) {
			// 不存在就将jar包里的ParameterValues.xml复制到指定路径下
			File f = new File(Parameters.DEFAULT_TEMP_PATH);
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
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
/*    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
    */


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptBASE64(String key) throws RunException {
        String value = "";
        try {
            value = new String((new BASE64Decoder()).decodeBuffer(key));
        } catch (IOException e) {
            throw new RunException(ExceptionInfo.get(RunException.DECRYPT_BASE64_FAIL, key));
        }
        return value;
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws RunException {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
//
//    public static  void  main(String[] args) {
//
//        byte[] srtbyte = "1234567".getBytes();
//        try {
//            String a = encryptBASE64(srtbyte);
//            System.out.println(a);
//            System.out.println(new String(decryptBASE64(a)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
