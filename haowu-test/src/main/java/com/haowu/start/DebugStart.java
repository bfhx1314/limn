package com.haowu.start;

import com.haowu.keyword.CustomKeyWordImpl;
import com.haowu.keyword.HaowuKeyWordImpl;
import com.haowu.keyword.HaowuKeyWordType;
import com.haowu.uitest.hossweb.Initialization;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.log.RunLog;

public class DebugStart {
	/**
	 * 调试运行界面
	 */
	public static void debugStart(){
		
		//好屋关键字
		HaowuKeyWordImpl haowuKWI = new HaowuKeyWordImpl();

		//自定义关键字
		haowuKWI.setKeyWordDriver(new CustomKeyWordImpl());
		
		Initialization.start();
		DebugEditFrame a = new DebugEditFrame(HaowuKeyWordType.class, haowuKWI);
		new RunLog(a);
	}
	
	public static void main(String[] args){
		debugStart();
	}
}
