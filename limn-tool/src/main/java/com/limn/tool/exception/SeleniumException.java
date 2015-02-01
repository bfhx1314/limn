package com.limn.tool.exception;

import com.limn.tool.parameter.Parameter;


/**
 * selenium��ܵ��쳣���࣬��ҪĿ����Ϊ����չException���Ӵ������code
 * �������������£�
 * 1111 - ϵͳ����
 * 2222 - ���в�������
 * 3333 - ������д����
 * 4444 - ƽ̨���ִ���
 * �д�����
 * @author tangxy
 *
 */
public class SeleniumException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * �����ţ���2222 0001�����ĸ�ʽ��ʾ
	 */
	private int code = -1;
	
	public SeleniumException(int code, String message) {
		super(message);
		this.code = code;
		if (Parameter.ERRORLOG.equals("")){
			Parameter.ERRORLOG = message;
		}else{
			Parameter.ERRORLOG = Parameter.ERRORLOG + " " + message;
		}
	}
	
	public int getCode() {
		return code;
	}

}
