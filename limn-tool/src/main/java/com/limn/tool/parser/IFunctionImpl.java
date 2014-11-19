package com.limn.tool.parser;

/**
 * IFunctionImpl定义定制函数的实现接口
 * 定义定制函数实现所需要的所有方法定义，例如:
 * <p><blockquote><pre>
 * class SetValueImpl implements IFunctionImpl {
 *     public Object calc(String name, IEvalContext context, Object[] arguments) {
 *         // Set value
 *     }
 * }
 * </pre></blockquote>
 * <p>
 * @author 王元和
 * @since YES1.0
 */
public interface IFunctionImpl {
	/**
	 * 计算方法,具体功能完全由具体的实现类来决定
	 * @param name 定制函数的名称
	 * @param context 执行上下文
	 * @param arguments 定制函数的传递的参数列表
	 * @return 定制函数的返回值,由具体的实现类来决定,但必须有返回值
	 */
	public Object calc(String name, IEvalContext context, Object[] arguments);
}
