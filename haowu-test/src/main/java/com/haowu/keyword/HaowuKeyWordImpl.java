package com.haowu.keyword;

import java.lang.reflect.Field;
import java.util.HashSet;

import com.haowu.exception.HaowuException;
import com.haowu.parameter.ParameterHaowu;
import com.haowu.test.apply_workflow.ApplyWorkflow;
import com.haowu.test.apply_workflow.ApplyWorkflowTest;
import com.haowu.uitest.hossweb.HossWeb;
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
				if (step.length > 1) {
					if(step[1].equalsIgnoreCase("firefox")){
						Parameter.BROWSERTYPE = 1;
					}else if(step[1].equalsIgnoreCase("ie")){
						Parameter.BROWSERTYPE = 3;
					}else if(step[1].equalsIgnoreCase("chrome")){
						Parameter.BROWSERTYPE = 2;
					}else{
						throw new HaowuException(10020000, "不支持此浏览器类型:" + step[1] + " 支持的类型有:firefox,chrome,ie");
					}
				}
				HossWeb.startBroswer(Parameter.BROWSERTYPE, ParameterHaowu.HAOWU_URL, null);
				break;
			case HaowuKeyWordType.CLOSE_BROWSER:
				HossWeb.stopBroswer();
				break;
			case HaowuKeyWordType.LOGIN:
				if(step.length<3){
					throw new HaowuException(10020000, "登录关键字:参数个数有误");
				}
				HossWeb.login(step[1], step[2]);
				Print.log("账号:" + step[1] + "  密码:" + step[2], 0);
				break;
			case HaowuKeyWordType.LOGOUT:
				HossWeb.logout();
				Print.log("注销登陆", 0);
				break;
			case HaowuKeyWordType.MENU:
				if(step.length<2){
					throw new HaowuException(10020000, "菜单关键字:参数个数有误");
				}
				HossWeb.menu(step[1]);
				Print.log("菜单:" + step[1], 0);
				break;
			case HaowuKeyWordType.LIST:
				if(step.length<2){
					throw new HaowuException(10020000, "列表关键字:参数个数有误");
				}
				HossWeb.list(step[1]);
				Print.log("列表:" + step[1], 0);
				break;
			case HaowuKeyWordType.Check:
				
				break;
			default:
				if (customKeyWordDriver != null) {
					customKeyWordDriver.start(step);
				} else {
					Print.log("不存在此关键字:" + step[0], 2);
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
}
