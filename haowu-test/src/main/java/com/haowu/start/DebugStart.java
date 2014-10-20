package com.haowu.start;

import com.haowu.keyword.CustomKeyWordImpl;
import com.haowu.keyword.HaowuKeyWordImpl;
import com.haowu.panel.HaowuPanel;
import com.haowu.uitest.hossweb.Initialization;
import com.limn.frame.debug.CustomPanel;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.log.RunLog;

public class DebugStart {
	/**
	 * 调试运行界面
	 */
	public static void debugStart(){
		//面板
		HaowuPanel cp = new HaowuPanel();
		
		//好屋关键字
		HaowuKeyWordImpl haowuKWI = new HaowuKeyWordImpl();

		//自定义关键字
		haowuKWI.setKeyWordDriver(new CustomKeyWordImpl());
		
		Initialization.start();
		DebugEditFrame a = new DebugEditFrame(cp, haowuKWI);
		new RunLog(a);
	}
	
	public static void main(String[] args){
		debugStart();
	}
}
