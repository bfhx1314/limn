package com.limn.driver.exception;

import com.limn.tool.parameter.Parameter;



public class SeleniumFindException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SeleniumFindException(int code, String message){
		super(message);
//		Print.log(message, 2);
	}
	
	public SeleniumFindException(String message){
		super(message);
//		Print.log(message, 2);
	}
}
