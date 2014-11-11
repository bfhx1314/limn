package com.haowu.start;

import com.haowu.keyword.HossCustomKeyWordImpl;
import com.haowu.keyword.HossKeyWordImpl;
import com.haowu.keyword.HossKeyWordType;
import com.haowu.panel.InterfacePanel;
import com.haowu.uitest.hossweb.Initialization;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.log.RunLog;

public class DebugStart {
	/**
	 * 调试运行界面
	 */
	public static void debugStart(){
		
		//好屋关键字  ***************************************************
		HossKeyWordImpl haowuKWI = new HossKeyWordImpl();
		//自定义关键字
		haowuKWI.setKeyWordDriver(new HossCustomKeyWordImpl());
		//**********************************************************
		
		//合伙人关键字 *************************************************
		
		
		//**********************************************************
		
		Initialization.start();
		DebugEditFrame a = new DebugEditFrame();
		a.addKeyWordDriver("Hoss关键字", haowuKWI, HossKeyWordType.class);
		a.addPanel("接口", new InterfacePanel());
		new RunLog(a);
	}
	
	
	public static void main(String[] args){
		debugStart();
	}
}
