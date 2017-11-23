package com.limn.tool.parser;

/**
 * 语法分析规则跟踪
 * 其记录了推导规则,以rule表示
 * 以及推导规则当前匹配到的位置pos表示,例如
 * <p><pre>
 * E -> E + ^ E
 * ^所在位置表示当前匹配到的位置,及下一个需要匹配的符号是第二个E
 * </pre><p>
 * 创建时间 2013-11-08
 * @author 王元和
 * @see YES1.0
 */
public class ParserRuleTrace {
	/**
	 * 当前的规则
	 */
	private ParserRule rule = null;
	/**
	 * 已经匹配的语法因子位置
	 */
	private int pos = -1;
	
	/**
	 * 是否是被延迟未决的，即上次被延迟但未处理成功
	 */
	private boolean delayPending = false;
	
	/**
	 * 因子的值,只在常量或ID时有效,如果是常量,表示的是字符表示的值,如果是ID,表示ID的符号
	 */
	private Object value = null;
	
	public ParserRuleTrace(ParserRule rule) {
		this.rule = rule;
	}
	
	/**
	 * 取得规则定义
	 * @return 规则定义
	 */
	public ParserRule getRule() {
		return rule;
	}
	
	/**
	 * 取得当前匹配到的位置
	 * @return 位置
	 */
	public int getPos() {
		return pos;
	}
	
	/**
	 * 判断是否已经完全匹配
	 * @return 如果pos位于规则的最后一个因子位置返回true，否则返回false.
	 */
	public boolean isMatched() {
		return pos == rule.getRightLength() - 1;
	}
	
	/**
	 * 消费一个语法因子
	 * @param lexID
	 * @return 返回已经匹配的最后一个位置
	 */
	public int consum(int lexID) {
		++pos;
		return pos;
	}
	
	/**
	 * 取得下一个规则因子
	 * @return 如果存在下一个，则返回该规则因子，否则返回null
	 */
	public ParserRuleFactor getNextRuleFactor() {
		ParserRuleFactor factor = null;
		if ( pos < rule.getRightLength() - 1 ) {
			factor = rule.getRightFactorAt(pos + 1);
		}
		return factor;
	}
	
	/**
	 * 判断指定的语法因子是否匹配语法规则中的下一个符号
	 * @param lexID
	 * @return 如果下一个符号跟指定的语法因子相同，返回true，否则返回false
	 */
	public boolean isMatchNext(int lexID) {
		ParserRuleFactor ruleFactor = rule.getRightFactorAt(pos + 1);
		return ruleFactor.getID() == lexID;
	}
	
	/**
	 * 取得左部语法符号
	 * @return 左部语法符号
	 */
	public int getLeftID() {
		return rule.getLeft().getID();
	}
	
	/**
	 * 是否是递归关闭规则
	 * @return 如果是返回true，否则返回false
	 */
	public boolean isCloseRule() {
		return rule.isCloseRule();
	}
	
	/**
	 * 设置是否是延迟未决的
	 * @param delayPending
	 */
	public void setDelayPending(boolean delayPending) {
		this.delayPending = delayPending;
	}
	
	/**
	 * 取得是否是延迟未决的
	 * @return 如果是返回true，否则返回false
	 */
	public boolean isDelayPending() {
		return this.delayPending;
	}
	
	/**
	 * 设置因子的值
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * 取得因子的值
	 * @return
	 */
	public Object getValue() {
		return value;
	}
	
	public void printTrace() {
		printTrace("trace --> ");
	}
	
	public void printStack() {
		printTrace("								stack -->");
	}
	
	public void printTrace(String mark) {
		ParserRuleFactor left = rule.getLeft();
		String leftString = left.getName();
		String rightString = null;
		ParserRuleFactor[] vRight = rule.getRight();
		int length = vRight.length;
		
		for ( int i = 0; i<length; ++i ) {
			if ( rightString == null ) {
				rightString = vRight[i].getName();
			} else {
				rightString += " " + vRight[i].getName();
			}
			if ( pos == i ) {
				rightString += " ^ ";
			}
		}
		String result = null;
		if ( pos == -1 ) {
			result = leftString + " -> ^ " + rightString;
		} else {
			result = leftString + " -> " + rightString;
		}
		//System.out.println(mark + result);
	}
}
