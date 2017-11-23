package com.limn.tool.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

/**
 * 执行bat命令
 * @author Administrator
 *
 */
public class CallBat {
	
	
	
	public static void exec(String cmd) {
//		cmd = cmd.replace("/", "\\");
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行命令
	 * @param cmd cmd命令
	 * @return 返回输出流,以冒号拼接
	 */
    public static String returnExec(String cmd) {
        Process p;
        String str = "";
        try {
            //执行命令
        	cmd = cmd.replace("/", "\\");
            p = Runtime.getRuntime().exec(cmd);
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis,"GB2312");
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null) {
            	str = str + line + ":";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    
	/**
	 * 结束进程，如果存在多个一并结束。
	 * @param processName 进程名 、PID
	 */
	public static void closeProcess(String processName){
		
		if(Parameter.getOS() != null && Parameter.getOS().equalsIgnoreCase("Windows")){
			
			Runtime rt = Runtime.getRuntime();
	//		String[] command1=new String[]{"cmd","cd","C://Program Files//Thunder"};
		    String command = "taskkill /F /IM " + processName;    
		    try
		    {
	//	      rt.exec(command1);//返回一个进程
		      rt.exec(command);
	//	      System.out.println("success closed："+processName);
		    }
		    catch (IOException e)
		    {
		      e.printStackTrace();
		    }
		}
	}
	
	
	public static String returnExecByPipeline(String cmd) {
		return returnExec("cmd /c " + cmd);
    }
	
	public static void returnExecByNewWindows(String name,String cmd) {
		cmd = cmd.replace("/", "\\");
		exec("cmd /c start \"" + name + "\"" + cmd);
    }
	
	public static void closeProcessByTitle(String titleName){
		Runtime rt = Runtime.getRuntime();
	    String command = "taskkill /F /FI " + "\"WINDOWTITLE eq " +  titleName + "\"";    
	    try
	    {
	      rt.exec(command);
	      System.out.println("success closed："+titleName);
	      Thread.sleep(1000);
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    } catch (InterruptedException e) {
			e.printStackTrace();
		}
	   
	}
	
	
	public static void closeMiddleware(){
		String str = CallBat.returnExec("jps");
		String PID = null;
		String[] arr = str.split(":");
		for(int i=0;i<arr.length;i++){
			if (arr[i].indexOf("Bootstrap") != -1){
				ArrayList<String> arrayList = RegExp.matcherCharacters(arr[i], "\\d+");
				PID = arrayList.get(0);
				break;
			}
		}
		if (PID!=null){
			CallBat.closeProcess(PID); // 结束TOMCAT
		}
	}
	
}

