package com.haowu.start;

import com.haowu.keyword.CustomKeyWordImpl;
import com.haowu.keyword.HaowuKeyWordImpl;
import com.haowu.uitest.hossweb.Initialization;
import com.limn.frame.control.ConsoleFrame;


public class Start {
	

	
	public static void runTest(){
	
		//好屋关键字
		HaowuKeyWordImpl haowuKWI = new HaowuKeyWordImpl();
		//自定义关键字
		haowuKWI.setKeyWordDriver(new CustomKeyWordImpl());
		
		Initialization.start();
		
		try {
			new ConsoleFrame(haowuKWI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
//		debugStart();
		runTest();
	}

}
