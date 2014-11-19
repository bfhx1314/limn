package com.limn.tool.parser;

/**
 * IFunctionImplMap接口定义函数实现映射,用来描述函数的具体实现,该接口的目的是允许外部环境替换解析时的函数实现
 * @author 王元和
 * @since YES1.0
 */
public interface IFunctionImplMap {
	/**
	 * 判断是否包含某个函数的实现
	 * @param name 函数名称
	 * @return 如果函数在这个映射中实现,那么返回true,否则返回false
	 */
	public boolean containFunction(String name);
	
	/**
	 * 返回某个函数的具体实现对象
	 * @param name 函数名称
	 * @return 如果函数在这个映射中实现,那么返回其实现对象,否则返回null
	 */
	public IFunctionImpl getFunctionImpl(String name);
}
