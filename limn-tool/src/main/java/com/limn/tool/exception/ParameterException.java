package com.limn.tool.exception;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.limn.tool.common.Print;



/**
 * 运行参数异常,异常编码如下:
 * 
 * <pre>
 * 2222 0001 - 参数未定义
 * 2222 0002 - 文件不存在
 * 2222 0003 - 文件内容出错
 * 2222 1001 - 运行参数中忽略连接数据库的关键字
 * </pre>
 *   
 * @author tangxy
 *
 */
public class ParameterException extends Exception{
	private static final long serialVersionUID = 1L;

	/**
	 * 参数未定义
	 */
	public static final int PARAMETER_NOT_ASSIGNED = 22220001;

	/**
	 * 文件不存在
	 */
	public static final int FILE_NOT_EXIST = 22220002;

	/**
	 * 文件内容出错
	 */
	public static final int FILE_ERROR = 22220003;
	
	/**
	 * 输入错误
	 */
	
	public static final int INPUT_ERROR = 22220004;
	
	/**
	 * 数据库异常
	 */
	
	public static final int SQL_Exception = 22220005;


	public ParameterException(JFrame consoleFrame,int code, String message) {
		super(message);
		printStackTrace();
		JOptionPane.showMessageDialog(consoleFrame,message);


		
	}
	public ParameterException(int code, String message){
		super(message);


	}
	
	public ParameterException( String message){
		super(message);

		
	}
	
}
