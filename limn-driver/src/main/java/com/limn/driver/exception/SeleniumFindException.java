package com.limn.driver.exception;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.limn.tool.common.Print;

public class SeleniumFindException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SeleniumFindException(int code, String message){
		super(message);
		Print.log(message, 2);
	}
	
	public SeleniumFindException(String message){
		super(message);
		Print.log(message, 2);
	}
}
