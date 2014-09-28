package com.limn.frame.control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.DocumentException;


public class CoreConfig {
	private String corePath;
	private String xmlPath;
	public CoreConfig(String corePath,String xmlPath){
		this.corePath = corePath;
		this.xmlPath = xmlPath;
	}
//	corePath = "H:/core.txt";
//	String xmlPath = "H:/Template.xml";
	public void coreOpen(){
		try {    
			Runtime.getRuntime().exec("cmd.exe  /c notepad "+corePath);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void coreSave(){
		//清空xml中的core节点下原有信息
		XMLReader xmlRead = new XMLReader(xmlPath,true);
//		xmlRead.clearCoreInfo(0);
		//根据core文件重新添加
		CoreReader coreRead = new CoreReader();
		coreRead.setCorePath(corePath);
		HashMap<String,String> coreValue = coreRead.getAllValue();
		Iterator iter = coreValue.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			System.out.print(key+" , "+val+"\n");
			try {
				xmlRead.setNodeValueByCoreIndex(0, key.toString().trim(), val.toString().trim());
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}		
//	 public static void main(String args[]){
//		 CoreConfig cc = new CoreConfig("H:/core.txt","H:/Template.xml");
//		 cc.coreOpen();		 
//		 cc.coreSave();
//		 }
}
