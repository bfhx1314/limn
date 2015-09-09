package com.limn.frame.keyword;

public class BaseAppKeyWordType {
	
	/**
	 * 说明: 界面控件录入<br>
	 * 参数1: 定位    元素ID 或者 xpath<br>
	 * 参数2: 录入数据或者事件  id 或者 [Click]<br>
	 * 用法: M录入:[定位]:[数据]<br>
	 * 实例: M录入:username:limn-manager<br>
	 */
	public static final String M_INPUT = "M录入";
	
	
	/**
	 * 说明: 界面滑动<br>
	 * 参数1:滑动方向  Left(向左) Right(向右) Top(向上) Bottom(向下)<br>
	 * 用法: M滑动:[Left|Right|Top|Bottom]<br>
	 * 实例: M滑动:Left<br>
	 */
	public static final String M_SLIDE = "M滑动";
	
	/**
	 * 说明: 装载app<br>
	 * 参数1:app路径<br>
	 * 用法: M启动:[App路径]<br>
	 * 实例:M启动:E:/HaowuAgent.apk<br>
	 */
	public static final String M_START = "M启动";
	
	
}
