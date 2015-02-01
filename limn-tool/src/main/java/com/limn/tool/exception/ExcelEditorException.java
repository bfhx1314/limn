package com.limn.tool.exception;

import com.limn.tool.parameter.Parameter;

public class ExcelEditorException extends Exception{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExcelEditorException(String message) {
		super(message);
		Parameter.ERRORLOG = message;
	}
	

}
