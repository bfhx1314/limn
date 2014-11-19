package com.limn.tool.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * 语法树因子，用来表示语法树的一个组成节点
 * @author 王元和
 * @since YES1.0
 */
public class Factor {
	/** 左部规则因子 */
	private ParserRuleFactor ruleFactor = null;
	/** 使用的规则 */
	private ParserRule rule = null;
	/** 子语法树因子集合 */
	private ArrayList<Factor> childList = null;
	/** 词文 */
	private String lexValue = null;
	/** 语法树因子上附着的计算值 */
	private Object value = null;
	/** 是否控制函数标志 */
	private boolean controlFunction = false;
	
	/**
	 * 使用语法因子及推导规则初始化一个词法树元素
	 * @param ruleFactor 语法因子
	 * @param rule 推导规则
	 */
	public Factor(ParserRuleFactor ruleFactor, ParserRule rule) {
		this.ruleFactor = ruleFactor;
		this.rule = rule;
	}
	
	/**
	 * 仅使用语法因子初始化一个词法树元素,后续的规则通过setRule设置或者没有推导规则,比如如果语法树元素是一个左括号(,则没有推导规则.
	 * @param ruleFactor
	 */
	public Factor(ParserRuleFactor ruleFactor ) {
		this.ruleFactor = ruleFactor;
	}
	
	/**
	 * 设置推导规则
	 * @param rule 语法规则中有效的推导规则,不作有效性检查
	 */
	public void setRule(ParserRule rule) {
		this.rule = rule;
	}
	
	/**
	 * 取得推导规则
	 * @return
	 */
	public ParserRule getRule() {
		return rule;
	}
	
	/**
	 * 取得语法因子
	 * @return
	 */
	public ParserRuleFactor getRuleFactor() {
		return ruleFactor;
	}
	
	/**
	 * 增加子元素
	 * @param factor
	 */
	public void addChild(Factor factor) {
		if ( childList == null ) {
			childList = new ArrayList<Factor>();
		}
		childList.add(factor);
	}
	
	/**
	 * 设置子元素集合
	 * @param childList
	 */
	public void setAllChild(ArrayList<Factor> childList) {
		this.childList = childList;
	}
	
	/**
	 * 取得子元素集合
	 * @return
	 */
	public ArrayList<Factor> getAllChild() {
		return childList;
	}
	
	/**
	 * 取得子节点的数量
	 * @return
	 */
	public int getChildCount() {
		if ( childList == null ) {
			return 0;
		}
		return childList.size();
	}
	
	/**
	 * 取得对应位置的子元素
	 * @param index 子元素序号
	 * @return 相应位置的元素
	 */
	public Factor getFactor(int index) {
		return childList.get(index);
	}
	
	/**
	 * 设置对应位置的子元素
	 * @param index 子元素位置
	 * @param factor 语法树因子
	 */
	public void setFactor(int index, Factor factor) {
		childList.set(index, factor);
	}
	
	/**
	 * 设置节点的值,该值用于在计算过程中存储当前节点的数据
	 * @param value 值
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * 取得节点的值
	 * @return
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * 设置节点对应的词文
	 * @param lexValue 词文,通过词法分析取出来的词文字面值,比如if,或者"124"
	 */
	public void setLexValue(String lexValue) {
		this.lexValue = lexValue;
	}
	
	/**
	 * 取得节点对应的词文
	 * @return
	 */
	public String getLexValue() {
		return lexValue;
	}
	
	/**
	 * 设置是否控制函数标志
	 * @param controlFunction 标志
	 */
	public void setControlFunction(boolean controlFunction) {
		this.controlFunction = controlFunction;
	}
	
	/**
	 * 取得是否控制函数标志
	 * @return 控制函数标志 
	 */
	public boolean isControlFunction() {
		return controlFunction;
	}
	
	/**
	 * 设置子元素的值
	 * @param index 子元素的位置
	 * @param value 值
	 */
	public void setChildValue(int index, Object value) {
		childList.get(index).setValue(value);
	}
	
	/**
	 * 设置子元素词文
	 * @param index 子元素位置
	 * @param lexValue 词文
	 */
	public void setChildLexValue(int index, String lexValue) {
		childList.get(index).setLexValue(lexValue);
	}
	
	/**
	 * 设置子元素的控制函数标志
	 * @param index 子元素位置
	 * @param controlFunction 标志
	 */
	public void setChildControlFunction(int index, boolean controlFunction) {
		childList.get(index).setControlFunction(controlFunction);
	}
	
	/**
	 * 取得子元素的值
	 * @param index 子元素的位置
	 * @return 对应子元素的值
	 */
	public Object getChildValue(int index) {
		return childList.get(index).getValue();
	}
	
	/**
	 * 将两个节点合并,用于词法树优化,优化的方法就是用子节点的规则替换当前节点的规则,用子节点的子元素集合替换当前节点的子元素集合
	 * @param childFactor
	 */
	public void mergeFactor(Factor childFactor) {
		this.rule = childFactor.getRule();
		this.childList = childFactor.getAllChild();
	}
	
	/**
	 * 用一个集合去替换某个位置的子元素,比如
	 * <p><pre>
	 *      E
	 *  E   +   E
	 *  调用replaceChild(2, [E - E])之后,结果为
	 *          E
	 *  E   +   E   -   E
	 * </pre></p>
	 * @param index 子节点序号
	 * @param factorList 元素集合
	 */
	public void replaceChild(int index, List<Factor> factorList) {
		childList.remove(index);
		childList.addAll(index, factorList);
	}
}
