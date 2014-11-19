package com.limn.tool.parser;

import com.limn.tool.exception.SeleniumException;



/**
 * 解析器异常类
 * 其中code的编号如下
 * <p><pre>
 * 8001 0001 - 不完全的输入
 * 8001 0002 - 无效输入
 * 8001 0003 - 不兼容的类型
 * </pre></p>
 * 创建时间2013-11-13
 * @author 王元和
 * @since YES1.0
 */
public class ParserException extends SeleniumException {
	
	public ParserException(int code, String message) {
		super(code, message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int INCOMPLETE_INPUT = 80010001;
	public static final int INVALID_INPUT = 80010002;
	public static final int INCOMPATIBLE_TYPE = 80010003;
}

