package com.limn.tool.parser;

/**
 * 对象解析器
 * @author 王元和
 * @since YES1.0
 */
public interface IObjectResolver {
	/**
	 * 解析一个词法是否是一个对象
	 * @param token 词文
	 * @return 如果一个词文是一个对象，返回这个对象的标识，否则返回-1
	 */
	public int resolveObject(String token);
}
