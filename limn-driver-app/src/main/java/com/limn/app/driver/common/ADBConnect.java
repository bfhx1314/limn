package com.limn.app.driver.common;

import com.limn.tool.common.BaseToolParameter;
import com.limn.tool.common.BaseUntil;
import com.limn.tool.common.CallBat;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

/**
 * Created by limengnan on 2018/1/22.
 */
public class ADBConnect {

    private String adbPath;

    private boolean flag = true;


    private final String ADB_SHELL_PROCESS = "/adb shell ps | grep ";

    private final String ADB_SHELL_PACKAGE = "/adb shell pm list packages | grep ";

    private final String ADB_PULL = "/adb pull ";

    private final String ADB_PUSH = "/adb push ";

    private final String ADB_SHELL_CLEAR = "/adb shell pm clear ";

    private final String ADB_SHELL_CHMOD = "/adb shell chmod ";

    private final String ADB_SHELL_CHOWN = "/adb shell chown ";

    public ADBConnect(){

        adbPath = Variable.getExpressionValue("android.sdk.adb.path");
        if(BaseUntil.isEmpty(adbPath)){
            BaseToolParameter.getPrintThreadLocal().log("adbPath为空",2);
            flag = false;
        }

    }

    /**
     * 下载文件至本地
     * @param sourcePath android目录
     * @param targetPath 本机目录
     */
    public void pull(String sourcePath, String targetPath){
        String result = exeShell(ADB_PULL + sourcePath + " " + targetPath);
        System.out.println(result);
    }

    /**
     * 上传文件至Android
     * @param sourcePath
     * @param targetPath
     */
    public void push(String sourcePath, String targetPath){
        String result = exeShell(ADB_PUSH + sourcePath + " " + targetPath);
        System.out.println(result);
    }


    public void clear(String packageName){
        String result = exeShell(ADB_SHELL_CLEAR + packageName);
        System.out.println(result);

    }


    public void chmod(String cmd){
        String result = exeShell(ADB_SHELL_CHMOD + cmd);
        System.out.println(result);
    }


    public void chown(String cmd){
        String result = exeShell(ADB_SHELL_CHOWN + cmd);
        System.out.println(result);
    }



    /**
     * 判断App是否运行
     * @param packageName
     * @return
     */
    public boolean isRunningByPackageName(String packageName){
        return find(ADB_SHELL_PROCESS + packageName , packageName);
    }


    /**
     * 判断是App是否安装
     * @param packageName
     * @return
     */
    public boolean isIntalledByPackageName(String packageName){
        return find(ADB_SHELL_PACKAGE + packageName , packageName);
    }

    private boolean find(String shell, String pattern){
        String context = exeShell(shell);
        if(BaseUntil.isNotEmpty(context)){
            return RegExp.findCharacters(context,pattern);
        }
        return false;
    }


    private String exeShell(String shell){
        if(flag) {
            return CallBat.returnExec(adbPath + shell);
        }else{
            BaseToolParameter.getPrintThreadLocal().log("adbPath为空",2);
        }
        return null;
    }





    public static void main(String[] args){

        ADBConnect adb = new ADBConnect();
//        System.out.println(adb.isRunningByPackageName("com.jifen.qukan"));
//        System.out.println(adb.isIntalledByPackageName("com.jifen.qukan"));
//        adb.pull("/storage/sdcard0/Android/data/com.jifen.qukan","/Users/limengnan/Documents/temp/sd/com.jifen.qukan");
//        adb.pull("/data/data/com.jifen.qukan","/Users/limengnan/Documents/temp/phone/com.jifen.qukan");
        adb.clear("com.jifen.qukan");
//        adb.push("/Users/limengnan/Documents/temp/sd/com.jifen.qukan","/storage/sdcard0/Android/data/");
        adb.push("/Users/limengnan/Documents/temp/phone/com.jifen.qukan","/data/data/");

//        adb.push("/Users/limengnan/Documents/temp/sd/com.jifen.qukan/","/storage/sdcard0/Android/data/com.jifen.qukan/");
    }



}
