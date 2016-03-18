package com.limn.tool.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Properties;

import javax.swing.JComboBox;

import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.variable.Variable;

public class JComboBoxHis extends JComboBox<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String values = null;
	private Properties variableProps = new Properties();
	private String key = null;
	private HashSet<String> hashValue = new HashSet<String>();
	private String filePath = FileUtil.getFileAbsolutelyPath(Parameter.DEFAULT_TEMP_PATH,"swingCom.properties");
	/**
	 * key唯一标示
	 * @param key
	 */
	public JComboBoxHis(String key){
		this.key = key;
		this.setEditable(true);
		File newFile = new File(filePath);
		
		if(!newFile.exists() || newFile.isDirectory()){
			try {
				if(newFile.getParentFile().exists()){
					newFile.getParentFile().mkdir();
				}
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(newFile));
			variableProps.load(isr);
			if(variableProps.containsKey("JComboBoxHis_" + key)){
				values = (String) variableProps.get("JComboBoxHis_" + key);
				for(String value : values.split(",")){
					if(!hashValue.contains(value)){
						this.addItem(value);
						hashValue.add(value);
					}
				}
			}
		} catch (FileNotFoundException e) {
			Print.log("控件加载文件不存在:" + filePath, 2);
		} catch (IOException e) {
			Print.log("控件加载文件不存在:" + filePath, 2);
		}
	}
	
	/**
	 * 保存参数
	 */
	public void save(){

		if(values != null){
			if(!hashValue.contains(this.getSelectedItem().toString())){
				values = values + "," + this.getSelectedItem().toString();
			}
		}else{
			if(null == this.getSelectedItem()){
				values = "";
			}else{
				values = this.getSelectedItem().toString();
			}
		}

		variableProps.put("JComboBoxHis_" + key, values);
	
		
		FileOutputStream fOut = null;
		Writer out = null;
		try {
			fOut = new FileOutputStream(filePath);
			out = new OutputStreamWriter(fOut, "UTF-8");
			
			variableProps.store(out, "variable local cache");
			fOut.close();
			out.close();
		} catch (Exception e) {
			Print.log("存储本地化文件变量出错:" + e.getMessage(),2);
			e.printStackTrace();
		} finally{
			try {
				fOut.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	

}
