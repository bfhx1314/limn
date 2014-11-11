package com.haowu.exception;


public class HaowuException extends Exception {

	private static final long serialVersionUID = 1L;
	/**
	 * 错误等级划分
	 * 1001 0000 界面元素问题
	 * 1001 0001
	 * 1001 0002
	 * 1001 0003
	 * 
	 * 1002 0000测试用例问题
	 * 1002 0001参数个数有误
	 * 
	 * 
	 * 
	 * 
	 */
	private int code = 0;
	
//	private HashMap<Integer,String> exception = new HashMap<Integer,String>();
	
	public HaowuException(int code ,String message){
		super(message);
		this.code = code;
//		exception.put(code, message);
	}
	
	/**
	 * 返回错误code
	 * @return
	 */
	public int getCode(){
		return code;
	}

}
