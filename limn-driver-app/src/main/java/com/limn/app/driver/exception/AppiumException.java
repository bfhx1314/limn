package com.limn.app.driver.exception;

import com.limn.tool.parameter.Parameter;



public class AppiumException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppiumException(int code, String message){
		super(message);
//		Print.log(message, 2);
	}
	
	public AppiumException(String message){
		super(message);
//		Print.log(message, 2);
	}
}
