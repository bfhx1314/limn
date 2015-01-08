package com.limn.frame.keyword;

public class BaseKeyWordType {
	
	/**
	 * 说明: 界面控件录入<br>
	 * 参数1: 定位    元素ID 或者 xpath<br>
	 * 参数2: 录入数据或者事件  id 或者 [Click]<br>
	 * 用法: 录入:[定位]:[数据]<br>
	 * 实例: 录入:username:limn-manager<br>
	 * 实例: 录入://input[@id='username']:limn-manager<br>
	 */
	public static final String INPUT = "录入";
	
	/**
	 * 说明: 启动浏览器<br>
	 * 可选参数1: 浏览器类型 : firefox,ie,chrome<br>
	 * 用法: 启动浏览器:[浏览器类型]<br>
	 * 实例: 启动浏览器:firefox<br>
	 */
	public static final String START_BROWSER = "启动浏览器";
	
	/**
	 * 说明: 关闭打开的浏览器<br>
	 * 实例: 关闭浏览器
	 */
	public static final String CLOSE_BROSWER = "关闭浏览器";

	/**
	 * 说明: 界面弹出的提示框<br>
	 * 参数1: 确定 or 取消
	 * 实例: 提示框:确定
	 */
	public static final String DIALOG = "提示框";

	/**
	 * 说明: 页面跳转<br>
	 * 参数1: 转向的URL<br>
	 * 实例: 跳转:http:www.baidu.com
	 */
	public static final String CHANGE_URL = "跳转";
	
	/**
	 * 说明: 键盘事件<br>
	 * 参数1: 按键名称<br>
	 * 实例: 键盘事件:Enter
	 */
	public static final String KEYBOARD_EVENT = "键盘事件";
	
	/**
	 * 
	 */
	public static final String MOUSE_EVENT = "鼠标事件";
	
	/**
	 * 说明: 等待<br>
	 * 参数1: 等待的秒数<br>
	 * 实例: 等待:5<br>
	 * 
	 */
	public static final String WAIT = "等待";
	
	/**
	 * 说明: EXPRESSION<br>
	 * 参数1: 表达式<br>
	 * 实例: 表达式:{set}={get}&'a' 、 内置函数自增{houseName}=getAutoIncrement(5) <br>
	 * 
	 */
	public static final String EXPRESSION = "表达式";
	
	/**
	 * 说明: ADDATTACHMENT<br>
	 * 参数1: 附件的相对路径<br>
	 * 实例: 添加附件:attachment\\upload.txt <br>
	 * 
	 */
	public final static String ADDATTACHMENT = "添加附件";

	/**
	 * 说明: 获取控件的值，存入变量中<br>
	 * 参数1: xpath别名<br>
	 * 参数2: 变量名<br>
	 * 实例: 获取:xpath别名:{变量} <br>
	 * 
	 */
	public final static String GETWEBELEMENTVALUETOVAR = "获取";
	
	/**
	 * 说明: 切换浏览器页面（一步操作可能弹出新的浏览器页面）<br>
	 * 参数1: 第i个页面<br>
	 * 实例: 切换页面:2 （从1开始，1代表第一个浏览器） <br>
	 * 
	 */
	public final static String CHANGEBROTAB = "切换页面";
	
	/**
	 * 说明: 关闭浏览器页面<br>
	 * 参数1: 第i个页面<br>
	 * 实例: 关闭页面:2 （从1开始，1代表第一个浏览器） <br>
	 * 
	 */
	public final static String CLOSEBROTAB = "关闭页面";
	
	/**
	 * 说明: 验证界面上的数据，读取预期结果列<br>
	 * 预期结果参数1: xpath别名<br>
	 * 预期结果参数2: 控件值<br>
	 * 实例: 验证， 预期结果：input[@id='test']:1<br>
	 * 
	 */
	public final static String VERIFICATION = "验证";
}
