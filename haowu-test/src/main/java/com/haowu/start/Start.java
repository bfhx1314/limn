package com.haowu.start;

import com.haowu.keyword.HossCustomKeyWordImpl;
import com.haowu.keyword.HossKeyWordImpl;
import com.haowu.uitest.hossweb.Initialization;
import com.limn.frame.control.ConsoleFrame;


public class Start {
	

	
	public static void runTest(){
	
		//好屋关键字
		HossKeyWordImpl haowuKWI = new HossKeyWordImpl();
		//自定义关键字
		haowuKWI.setKeyWordDriver(new HossCustomKeyWordImpl());
		
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
