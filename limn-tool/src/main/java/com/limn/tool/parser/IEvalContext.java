package com.limn.tool.parser;

/**
 * IEvalContext是所有解析环境上下文的基类,其仅仅是个声明接口,用作类型检查用,
 * 在外部实现中,所有的上下文必须从该接口实现,用于确保使用时不至于将一些无用的上下文传入解析器,
 * 该接口的目的是为了在嵌套的表达式调用或者多线程的表达式调用中隔离执行时的环境参数,以免造成不必要的环境混乱.
 * <p>
 * 比如我们在表格中执行一个事件的时候,当前行的信息需要在执行时记录在这个上下文中,并且嵌套的调用不会改变这个上下文的值(除非确实需要),
 * 因为嵌套的一些函数可以自己改变环境中的当前行,以使得后续的处理作用在错误的行上.
 * </p>
 * <p><blockquote><pre>
 * class UIContext implements IEvalContext {
 *     private int rowIndex = -1;
 *     public int getRowIndex() {
 *         return rowIndex;
 *     }
 *     
 *     public void setRowIndex(int rowIndex) {
 *         this.rowIndex = rowIndex;
 *     }
 * }
 * </pre></blockquote></p>
 * @author 王元和
 * @since YES1.0
 */
public interface IEvalContext {

}
