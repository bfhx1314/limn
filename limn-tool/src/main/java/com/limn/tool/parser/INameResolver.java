package com.limn.tool.parser;

/**
 * INameResolver定义词法解析中需要的名解析器,只在词法分析中使用,不在外部使用
 * @author 王元和
 * @since YES1.0
 */
public interface INameResolver {
	/**
	 * 判断一个符号是否是一个函数
	 * @param token 符号
	 * @return 如果一个符号是一个函数,返回true,否则返回false
	 */
	public boolean resolveFunction(String token);
}
