package com.limn.tool.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.limn.tool.regexp.RegExp;

/**
 * ִ��bat����
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
	 * ִ������
	 * @param cmd����
	 * @return ���������,��ð��ƴ��
	 */
    public static String returnExec(String cmd) {
        Process p;
        String str = "";
        try {
            //ִ������
        	cmd = cmd.replace("/", "\\");
            p = Runtime.getRuntime().exec(cmd);
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //ȡ���������������
            InputStream fis=p.getInputStream();
            //��һ�����������ȥ��
            InputStreamReader isr=new InputStreamReader(fis);
            //�û���������
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //ֱ������Ϊֹ
            while((line=br.readLine())!=null) {
            	str = str + line + ":";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    
	/**
	 * �������̣�������ڶ��һ��������
	 * @param processName ������ ��PID
	 */
	public static void closeProcess(String processName){
		Runtime rt = Runtime.getRuntime();
//		String[] command1=new String[]{"cmd","cd","C://Program Files//Thunder"};
	    String command = "taskkill /F /IM " + processName;    
	    try
	    {
//	      rt.exec(command1);//����һ������
	      rt.exec(command);
	      System.out.println("success closed");
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
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
	      System.out.println("success closed");
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
			CallBat.closeProcess(PID); // ����TOMCAT
		}
	}
	
}

