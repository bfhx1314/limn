package com.limn.tool.parser;

/**
 * IEvalEnv为解析的环境,用来描述解析器所在的执行环境可以提供的能力或者可以提供的服务,
 * <p>比如取得某个标识符(请参见语法定义部分关于标识符的定义)的值.</p>
 * <p>解析object(请参见表达式语法定义部分关于object的描述)所在的对象等.</p>
 * @author 王元和
 * @since YES1.0
 */
public interface IEvalEnv {
	/**
	 * 取得标识符的值
	 * @param id 标识符
	 * @return 返回环境中标识符所表示的值,比如在界面上,取得控件的值;在数据对象中,取得列所在的值.
	 */
	public Object getValue(String id);
}
