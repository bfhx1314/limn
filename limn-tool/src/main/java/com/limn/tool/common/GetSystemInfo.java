package com.limn.tool.common;

/**
 * ��ȡϵͳ��Ϣ
 * @author zhangli	
 *
 */
public class GetSystemInfo {

	/**
	 * ��ȡ����ϵͳλ��
	 * @return
	 */
	public static String getBit(){
    	String bit = "32";
    	if (System.getProperty("os.name").contains("Windows")) {
    	    if(System.getenv("ProgramFiles(x86)") != null){
    	    	bit = "64";
    	    };
    	} else {
    	    if (System.getProperty("os.arch").indexOf("64") != -1){
    	    	bit = "64";
    	    };
    	}
    	return bit;
	}
	
}