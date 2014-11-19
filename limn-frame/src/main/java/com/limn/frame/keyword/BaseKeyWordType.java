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

}
