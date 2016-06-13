package com.limn.driver.common;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.log.RunLog;
import com.limn.tool.common.CallBat;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.GetSystemInfo;
import com.limn.tool.common.Print;



/**
 * 对话框
 * @author zhangli
 *
 */
public class OperateDialog {
	/**
	 * 操作dialog导入文件
	 * @param filePath
	 */
	public static String importFile(String filePath){
		// 浏览器不同，弹出的对话框标题也不同。需要区分
		String browserName = DriverParameter.getDriverPaht().getBrowserName();
		String cmdBufferedReader = "";
		if (browserName.equalsIgnoreCase("firefox")){
			cmdBufferedReader = CallBat.returnExec(System.getProperty("user.dir")+"/bin/DownLoad_Upload_Dialog.exe " + "文件上载 " + "\""+filePath+"\"" + " 打开");
		}else if(browserName.equalsIgnoreCase("chrome")){
			cmdBufferedReader = CallBat.returnExec(System.getProperty("user.dir")+"/bin/DownLoad_Upload_Dialog.exe " + "打开 " + "\""+filePath+"\"" + " 打开");
		}else if(browserName.equalsIgnoreCase("iexplore")){
			cmdBufferedReader = CallBat.returnExec(System.getProperty("user.dir")+"/bin/DownLoad_Upload_Dialog.exe " + "选择要加载的文件 " + "\""+filePath+"\"" + " 打开");
		}else{
			// 没有对应浏览器
		}
		return cmdBufferedReader;
	}
	/**
	 * 保存（另存为）
	 * @param path 文件路径
	 * @param key 关键字
	 * @throws SeleniumFindException 
	 */
	public static void saveFile(String path, String key) throws SeleniumFindException{
		if (!FileUtil.exists(path)){
			Print.log("文件不存在："+path, 2);
			return;
		}
		String cmdBufferedReader = "";
		String browserName = DriverParameter.getDriverPaht().getBrowserName();
		if (browserName.equalsIgnoreCase("iexplore")){
			// 获取IE版本
			int ieVersion = (Integer) DriverParameter.getDriverPaht().runScript("return getIeVersion()");
			if (ieVersion == 9 || ieVersion == 10 || ieVersion == 11){
				// 屏幕坐标点击
				// X坐标：(document.body.clientWidth - 960)/2+960-126  Y坐标：27
				float bodyWidth = (Float) DriverParameter.getDriverPaht().runScript("return document.body.clientWidth;"); //网页可见区域宽
				float bodyHeight = (Float) DriverParameter.getDriverPaht().runScript("return window.screen.availHeight;"); //屏幕可用工作区高度
				float clickX = (Float) (bodyWidth-960)/2 + 960 -126; // 960 下载条宽度，126 下载条右边界与点击按钮的x坐标差
				float clickY = (Float) bodyHeight - 50/2; 			// 50 下载条高度。
				cmdBufferedReader = clickByXY("Yigo",clickX,clickY);
				clickX = (Float) (bodyWidth-960)/2 + 960;
				clickY = (Float) bodyHeight - 50;
				cmdBufferedReader = clickByXY("Yigo",clickX,clickY);
				cmdBufferedReader = downLoad("另存为",path,"保存");
			}else if(ieVersion != -1){
				if (key.equalsIgnoreCase("Excel导出")){
					cmdBufferedReader = clickButton("文件下载 ","保存");
					if (cmdBufferedReader.indexOf("true") == -1){
						Print.log("没有找到“文件下载”对话框！", 2);
						return;
					}
				}
				cmdBufferedReader = downLoad("另存为","\""+path+"\"","保存");
				if (cmdBufferedReader.indexOf("false") != -1){
					cmdBufferedReader = close("下载完毕");
					if (cmdBufferedReader.indexOf("false") != -1){
						Print.log("没有找到“下载完毕”对话框！", 2);
					}
				}else{
					Print.log("没有保存成功！", 2);
				}
			}else{
				Print.log("功能没有实现，IE版本："+ieVersion, 2);
			}
		}else if(browserName.equalsIgnoreCase("firefox")){
			if (key.equalsIgnoreCase("Excel导出") || key.equalsIgnoreCase("保存文件")){
				cmdBufferedReader = getDialogXY("正在打开");
				if (!cmdBufferedReader.isEmpty()){
					// 选择保存文件选项
					String[] arr = cmdBufferedReader.split(":");
					float dialogX = Float.parseFloat(arr[0]); // dialog的X坐标
					float dialogY = Float.parseFloat(arr[1]);// dialog的Y坐标
					float clickX = dialogX + 46; // 点击的X坐标
					float clickY = dialogY + 203; // 点击的Y坐标
					cmdBufferedReader = clickByXY("正在打开",clickX,clickY);
					// 点击“确定”按钮
					clickX = dialogX + 294;
					clickY = dialogY + 299;
					cmdBufferedReader = clickByXY("正在打开",clickX,clickY);
				}else{
					Print.log("没有获取到“正在打开”坐标", 2);
					return;
				}
			}
			if (key.equalsIgnoreCase("添加附件")){
				cmdBufferedReader = downLoad("文件上传","\""+path+"\"","打开");
			}else{
				cmdBufferedReader = downLoad("请输入要保存的文件名","\""+path+"\"","保存");
			}
			if (cmdBufferedReader.equalsIgnoreCase("true")){
				Print.log("dialog还存在，保存失败！", 2);
			}
		}else if(browserName.equalsIgnoreCase("chrome")){
			cmdBufferedReader = downLoad("另存为","\""+path+"\"","保存");
		}
	}
	
	/**
	 * 获取dialog的x、y坐标
	 * @param dialogTitle 标题
	 * @return
	 */
	private static String getDialogXY(String dialogTitle){
		String systemBit = GetSystemInfo.getBit();
		if (systemBit.equals("64")){
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/getDialogXY.exe " + dialogTitle);
		}else{
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/getDialogXY_32.exe " + dialogTitle);
		}
	}
	
	/**
	 * 通过坐标来处理
	 * @param dialogTitle 标题
	 * @param clickX
	 * @param clickY
	 * @return
	 */
	public static String clickByXY(String dialogTitle, float clickX, float clickY){
		String systemBit = GetSystemInfo.getBit();
		if (systemBit.equals("64")){
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/ClickByXY.exe " + dialogTitle + " " + clickX + " " + clickY);
		}else{
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/ClickByXY_32.exe " + dialogTitle + " " + clickX + " " + clickY);
		}
	}
	
	/**
	 * 处理下载的dialog
	 * @param dialogTitle 标题
	 * @param path 
	 * @param buttonName 
	 * @return
	 */
	private static String downLoad(String dialogTitle, String path, String buttonName){
		String systemBit = GetSystemInfo.getBit();
		if (systemBit.equals("64")){
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/DownLoad_Upload_Dialog.exe " + dialogTitle + " " + "\""+path+"\"" + " " + buttonName);
		}else{
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/DownLoad_Upload_Dialog_32.exe " + dialogTitle + " " + "\""+path+"\"" + " " + buttonName);
		}
	}
	
	/**
	 * 点击dialog的按钮
	 * @param dialogTitle 标题
	 * @param buttonName
	 * @return
	 */
	public static String clickButton(String dialogTitle, String buttonName){
		String systemBit = GetSystemInfo.getBit();
		if (systemBit.equals("64")){
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/ClickButton.exe " + dialogTitle + " " + buttonName);
		}else{
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/ClickButton_32.exe " + dialogTitle + " " + buttonName);
		}
	}
	
	/**
	 * 关闭
	 * @param dialogTitle 标题
	 * @return
	 */
	private static String close(String dialogTitle){
		String systemBit = GetSystemInfo.getBit();
		if (systemBit.equals("64")){
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/CloseDialog.exe " + dialogTitle);
		}else{
			return CallBat.returnExec(System.getProperty("user.dir")+"/bin/CloseDialog_32.exe " + dialogTitle);
		}
	}
	
}
