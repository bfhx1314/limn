package com.haowu.keyword;

import java.lang.reflect.Field;
import java.util.HashSet;

import com.haowu.exception.HaowuException;
import com.haowu.parameter.ParameterHaowu;
import com.haowu.uitest.common.WebControl;
import com.haowu.uitest.hossweb.HossWeb;
import com.limn.driver.Driver;
import com.limn.driver.common.OperateWindows;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.debug.LoadBroswerPanel;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.Print;
/**
 * 好屋默认的关键字实现
 * @author limn
 *
 */
public class HaowuKeyWordImpl implements KeyWordDriver {

	private KeyWordDriver customKeyWordDriver = null;

	private HashSet<String> data = new HashSet<String>();
	
	private boolean flag = false;
	
	/**
	 * 增加自定义关键字入口
	 * @param kwd
	 */
	public void setKeyWordDriver(KeyWordDriver kwd){
		customKeyWordDriver = kwd;
	}

	
	@Override
	public int start(String[] step) {

		int status = 1;
		
		try {
			
			switch (step[0]) {

			case HaowuKeyWordType.START_BROWSER:
				RunKeyWord.startBrowser(step);
				break;
			case HaowuKeyWordType.CLOSE_BROWSER:
				RunKeyWord.stopBroswer();
				break;
			case HaowuKeyWordType.LOGIN:
				cheakKeyWordCount(step.length, 3);
				Print.log("账号:" + step[1] + "  密码:" + step[2], 0);
				HossWeb.login(step[1], step[2]);
				break;
			case HaowuKeyWordType.LOGOUT:
				Print.log("注销登陆", 0);
				HossWeb.logout();
				break;
			case HaowuKeyWordType.MENU:
				cheakKeyWordCount(step.length,2);
				Print.log("菜单:" + step[1], 0);
				HossWeb.menu(step[1]);
				break;
			case HaowuKeyWordType.LIST:
				cheakKeyWordCount(step.length, 2);
				Print.log("列表:" + step[1], 0);
				HossWeb.list(step[1]);
				break;
			case HaowuKeyWordType.Check:
				
				break;
			case HaowuKeyWordType.INPUT:
				cheakKeyWordCount(step.length, 2);
				WebControl.setValue(step[1], step[2]);
				break;
			case HaowuKeyWordType.DIALOG:
				cheakKeyWordCount(step.length, 1);
				if(step[1].equals("确定") || step[1].equals("是")){
					OperateWindows.dealPotentialAlert(true);
				}else{
					OperateWindows.dealPotentialAlert(false);
				}
				break;
			case HaowuKeyWordType.CHANGE_URL:
				cheakKeyWordCount(step.length, 1);
				RunKeyWord.toURL(step);
				break;
			default:
				if (customKeyWordDriver != null) {
					customKeyWordDriver.start(step);
				} else {
					Print.log("不存在此关键字:" + step[0], 2);
				}
			}
			
			if(HaowuKeyWordClassification.isWebElementKeyword(step[0])){
				HossWeb.waitLoadForPage();
				try {
					LoadBroswerPanel.loadWebElement();
				} catch (SeleniumFindException e) {
					throw new HaowuException(10010000, e.getMessage());
				}
			}
		} catch (HaowuException e) {
			status = e.getCode();
			Print.log("异常信息: Message:" + e.getMessage(), 2);
		}

		return status;

	}


	/**
	 * 返回是否是关键字
	 */
	@Override
	public boolean isKeyWord(String key) {
		if(!flag){
			init();
		}
		return data.contains(key);
	}
	
	private void init(){
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.haowu.keyword.HaowuKeyWordType");
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				if (f.getGenericType().toString().equals("class java.lang.String")) {
					data.add((String) f.get(clazz));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void cheakKeyWordCount(int keyWordCount, int needCount) throws HaowuException{
		if(keyWordCount<needCount){
			throw new HaowuException(10020000, "列表关键字:参数个数有误");
		}
	}
}