package com.limn.frame.keyword;

public class BaseAppKeyWordType {
	
	/**
	 * 说明: 界面控件录入<br>
	 * 参数1: 定位    元素ID 或者 xpath<br>
	 * 参数2: 录入数据或者事件  id 或者 [Click] 或者 [Touth]<br>
	 * 用法: M录入:[定位]:[数据]<br>
	 * 实例: M录入:username:limn-manager<br>
	 * 实例: M录入:button:[Touth]<br>
	 */
	public static final String M_INPUT = "M录入";
	
	
	/**
	 * 说明: 界面滑动<br>
	 * 参数1:滑动方向  LeftSlide or LS(向左) RightSlide or RS(向右) UpSlide or US(向上) DownSlideor DS(向下)<br>
	 * 参数2:[可选] Center or C 屏幕中间滑动
	 * 用法: M滑动:[LeftSlide|RightSlide|UpSlide|DownSlideor]:[Center|C]<br>
	 * 实例: M滑动:LeftSlide:Center<br>
	 */
	public static final String M_SLIDE = "M滑动";
	
	/**
	 * 说明: 装载app<br>
	 * 参数1:app路径<br>
	 * 用法: M启动:[App路径]<br>
	 * 实例:M启动:E:/HaowuAgent.apk<br>
	 */
	public static final String M_START = "M启动";


	/**
	 * 说明: 界面控件如果存在即录入<br>
	 * 参数1: 定位    元素ID 或者 xpath<br>
	 * 参数2: 录入数据或者事件  id 或者 [Click] 或者 [Touth]<br>
	 * 用法: M录入:[定位]:[数据]<br>
	 * 实例: M录入:username:limn-manager<br>
	 * 实例: M录入:button:[Touth]<br>
	 */
	public static final String M_IFEXISTINPUT = "M存在录入";


	/**
	 * 说明: 界面滑动<br>
	 * 参数1:滑动方向  LeftSlide or LS(向左) RightSlide or RS(向右) UpSlide or US(向上) DownSlideor DS(向下)<br>
	 * 参数2:[可选] Center or C 屏幕中间滑动
	 * 用法: M滑动:[LeftSlide|RightSlide|UpSlide|DownSlideor]:[Center|C]<br>
	 * 实例: M滑动:LeftSlide:Center<br>
	 */
	public static final String M_IRREGULARSLIDE = "M无规律滑动";

	public static final String M_CLOSEAPP = "M关闭APP";
	
	
}
