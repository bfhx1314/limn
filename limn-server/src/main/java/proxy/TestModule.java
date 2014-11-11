package proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import monitor.Middleware;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import status.ModuleStatus;

/**
 * 所有测试的信息  存储于此
 * @author limn
 *
 */
public class TestModule {
	//信息全部存在折这个LinkedHashMap 用linked 的目的就是版本之间先后测试的关系
	private static LinkedHashMap<String, HashMap<String,String>> testModules = new LinkedHashMap<String, HashMap<String,String>>();

	/**
	 * 增加测试补丁
	 * @param version 补丁号
	 */
	public void setModule(String version){
		testModules.put(version, getModules());
		Middleware.setTestPath();
	}
	/**
	 * 
	 * @return 返回所有的测试信息
	 */
	public static LinkedHashMap<String, HashMap<String,String>> getTestModules(){
		return testModules;
	}
	
	/**
	 * 
	 * @return 返回需要测试模块信息
	 */
	@SuppressWarnings("resource")
	private HashMap<String,String> getModules() {
		HashMap<String,String> module = new HashMap<String,String>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
		
			fileReader = new FileReader("TestModule.txt");
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.startsWith("#") && !line.isEmpty()) {
					//兼容以前的文件config.txt 里面有 :0 的这种
					String content = line.split(":")[0];

					if (new File("Conf/" + content).exists()){
						
						module.put(content, ModuleStatus.READY);
	
					}
				}
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {

		}

		return module;
	}
	
	/**
	 * 更新测试状态
	 * @param versionAndKey  数组  [0]version [1]moduleKey
	 * @param value 状态的值   TestStatus
	 */
	public static void updateModule(String[] versionAndKey,String value){
		testModules.get(versionAndKey[0]).put(versionAndKey[1],value);
		Middleware.updateTestModule();
	}
	
	/**
	 * 
	 * @return 返回当前需要测试的信息    [0]version [1]moduleKey
	 */
	public static String[] getTestModule(){
		String moduleString = null;
		String[] versionAndKey = new String[2];
 		if(!testModules.isEmpty()){
			for(String version:testModules.keySet()){
				moduleString = getModuleByVersion(version);
				if(moduleString!=null){
					versionAndKey[0] = version;
					versionAndKey[1] = moduleString;
					
				}else{
					testModules.remove(version);
				}
			}
		}
			
		return versionAndKey;
	}
	
	/**
	 * 根据version返回没有测试过的模块
	 * @param version 测试版本号
	 * @return 测试模块
	 */
	private static String getModuleByVersion(String version){
		String moduleString = null;
		if(!testModules.get(version).isEmpty()){
			for(String moduleKey:testModules.get(version).keySet()){
				if (testModules.get(version).get(moduleKey)==ModuleStatus.READY){
						
					updateModule(new String[]{version,moduleKey},ModuleStatus.START);
					
					moduleString = moduleKey;
					return moduleString;
				}
			}
		}
	
		return moduleString;
		
	}
	
	/**
	 * 根据模块key 返回 xml文件内容
	 * @param moduleKey 模块标示
	 * @return xml内容
	 */
	public static String getModuleXMLForKey(String moduleKey){
		String asXMLString = "NoMore";
		if(new File("conf/" + moduleKey).exists()){
			Document document = null;
			try{
				SAXReader saxReader = new SAXReader();
				document = saxReader.read("conf/" + moduleKey);
				asXMLString = document.asXML();
			}catch(DocumentException e){
				System.out.println(e.getMessage());
			}
		}
		return asXMLString;
	}
	

}
