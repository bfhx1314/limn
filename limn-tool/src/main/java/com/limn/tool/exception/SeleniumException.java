package com.limn.tool.exception;


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
	}
	
	public int getCode() {
		return code;
	}

}
