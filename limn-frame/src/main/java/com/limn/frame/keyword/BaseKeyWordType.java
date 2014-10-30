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
	public final static String INPUT = "录入";
	
	/**
	 * 说明: 启动浏览器<br>
	 * 可选参数1: 浏览器类型 : firefox,ie,chrome<br>
	 * 用法: 启动浏览器:[浏览器类型]<br>
	 * 实例: 启动浏览器:firefox<br>
	 */
	public final static String START_BROWSER = "启动浏览器";
	
	/**
	 * 说明: 关闭打开的浏览器<br>
	 * 实例: 关闭浏览器
	 */
	public final static String CLOSE_BROSWER = "关闭浏览器";

}
