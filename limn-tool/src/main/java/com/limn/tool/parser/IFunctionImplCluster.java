package com.limn.tool.parser;

import java.util.Collection;

/**
 * IFunctionImplCluster接口定义一个函数簇实现,其用来实现一系列函数
 * @author 王元和
 * @since YES1.0
 */
public interface IFunctionImplCluster extends IFunctionImpl {
	/**
	 * 返回一个函数簇中实现的函数名称的列表
	 * @return 函数名称集合
	 */
	public Collection<String> getFunctionNames();
}
