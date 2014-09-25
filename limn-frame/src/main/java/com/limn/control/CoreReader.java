package com.limn.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import com.limn.exception.ParameterException;



/**
 * 根据Yigo路径,读写core文件中的所有属性和值
 * 
 * @author tangxy
 * 
 */
public class CoreReader {
	private String corePath = null;
	


//	private FileReader fileReader = null;
	private InputStreamReader isr = null;
	public HashMap<String, String> coreMap = new HashMap<String, String>();
	private String UTF8_BOM = "\uFEFF";

	public CoreReader(String Yigo) {
		String yigoPath = changePath(Yigo);
		corePath = yigoPath + "/WEB-INF/classes/core.properties";
		coreMap = getAllValue();
		
	}
	
	public CoreReader() {
		
	}
	
	/**
	 * 根据HashMap中的Key取其对应的Value
	 * 
	 * @param key
	 *            HashMap中的key
	 * @return key对应的Value
	 * @throws ParameterException 
	 */
	public String getValueByKey(String key) throws ParameterException{
		String value = null;
		// 判断HashMap中是否存在该key
		if (coreMap.containsKey(key)) {
			value = coreMap.get(key);
			// 判断是否存在多个dsn,这种情况下返回default为Y的dsn信息
			if (key.equals("server.dsn.dbtype")
					|| key.equals("server.dsn.default")
					|| key.equals("server.dsn.description")
					|| key.equals("server.dsn.name")) {
				String[] hasDefault = coreMap.get("server.dsn.default").split(
						",");
				if (hasDefault.length > 1) {
					value = getDefaultValue().get(key);
				}
			}

		} else {
				throw new ParameterException(ParameterException.FILE_ERROR, "core文件中不存在"+key+"对应的信息");
		}
		return value;
	}

	
	public void setCorePath(String corePath) {
		this.corePath = corePath;
	}
	
	
	/**
	 * 取key的默认值,对于有多个dsn的配置dbtype/description/name都返回default为"Y"对应的值
	 * 
	 * @return key对应的value值
	 * @throws ParameterException 
	 */
	private HashMap<String, String> getDefaultValue(){
		HashMap<String, String> defaultValue = new HashMap<String, String>();
		String[] hasDefault = coreMap.get("server.dsn.default").split(",");
		String[] defaultdbtype = coreMap.get("server.dsn.dbtype").split(",");
		String[] defaultdescription = coreMap.get("server.dsn.description")
				.split(",");
		String[] defaultname = coreMap.get("server.dsn.name").split(",");

		for (int i = 0; i < hasDefault.length; i++) {
			if (hasDefault[i].equals("Y")) {
				defaultValue.put("server.dsn.default", hasDefault[i]);
				defaultValue.put("server.dsn.dbtype", defaultdbtype[i]);
				defaultValue.put("server.dsn.description",
						defaultdescription[i]);
				defaultValue.put("server.dsn.name", defaultname[i]);
			} 
		}
		return defaultValue;
	}

	/**
	 * 将"\"表示的文件路径转换成"/"表示
	 * 
	 * @param 需要转换的文件路劲
	 * @return 转换后的文件路径
	 */
	private String changePath(String yigo) {
		String result = yigo.replace("\\", "/");
		return result;
	}

	/**
	 * 取得core文件中的所有信息
	 * 
	 * @return
	 */
	public HashMap<String, String> getAllValue() {
		HashMap<String, String> coreValue = new HashMap<String, String>();
		Properties props = new Properties();
		try {
			isr = new InputStreamReader(new FileInputStream(corePath));
			props.load(isr);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Object key : props.keySet()) {
			coreValue.put("" + key, props.getProperty("" + key));
//			System.out.println(key + " : " + props.getProperty("" + key));
		}

		
		try {
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return coreValue;
	}

	/**
	 * 根据key修改值
	 * 
	 * @param key
	 *            core文件中等号前面的字符串
	 * @param value
	 *            core文件中等号后面的字符串
	 * @throws ParameterException 
	 */
	
	public void setValueByKey(String key, String value){
		
		if(key.equalsIgnoreCase("server.config")){
			value = value.replace("\\\\", "/");
			value = value.replace("\\", "/");
		}
		
		
		String line = "";
		boolean falg = true;
		try {
			File file = new File(corePath);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);

			BufferedReader br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			// 保存该行前面的内容
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#") && !line.isEmpty()) {
					int equalSign = line.indexOf("=");

					String key_get = line.substring(0, equalSign);
					if (key_get.trim().equals(key) && equalSign!=-1 ){
						buf = buf.append(key_get + "=");
						buf = buf.append(value);
						buf = buf.append(System.getProperty("line.separator"));
						falg = false;
					} else {
						buf = buf.append(line);
						buf = buf.append(System.getProperty("line.separator"));
					}

				} else {
					buf = buf.append(line);
					buf = buf.append(System.getProperty("line.separator"));
				}
			}
			if(falg){
				buf = buf.append(key+"="+value);
			}
			br.close();			
			FileOutputStream fos = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		coreMap = getAllValue();
	}

//	 public static void main(String[] args) throws FileNotFoundException{
//	 CoreReader cr = new CoreReader("G:\\Tomcat\\webapps\\Yigo");
//	 cr.setValueByKey("aaa", "bbb");
//	 }
	

}
