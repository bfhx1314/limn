package com.limn.tool.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * 语法树，目前并不提供任何功能，只是作为语法树的因子的容器
 * @author 王元和
 */
public class SyntaxTree {
	private Stack<Factor> factorStack = null;
	private Factor root = null;
	public SyntaxTree() {
		factorStack = new Stack<Factor>();
	}
	
	/**
	 * 设置语法树的根
	 * @param root
	 */
	public void setRoot(Factor root) {
		this.root = root;
	}
	
	/**
	 * 取得语法树的根
	 * @return
	 */
	public Factor getRoot() {
		return root;
	}
	
	/**
	 * 判断语法树的栈是否为空，此函数在外部不能使用
	 * @return
	 */
	protected boolean isEmpty() {
		return factorStack.isEmpty();
	}
	
	/**
	 * 在语法树栈中压入一个元素
	 * @param factor
	 */
	public void push(Factor factor) {
		factorStack.push(factor);
	}
	
	/**
	 * 取得栈顶的语法树元素
	 * @return
	 */
	public Factor peek() {
		return factorStack.peek();
	}
	
	/**
	 * 弹出栈顶的语法树元素并返回
	 * @return
	 */
	public Factor pop() {
		return factorStack.pop();
	}
	
	/**
	 * 从语法树栈中产生语法树的根
	 */
	public void extract() {
		if ( !factorStack.isEmpty() ) {
			root = factorStack.pop();
		}
	}
	
	
	/**
	 * 优化语法树，主要是用来合并多次递归替换产生的无用节点
	 */
	public void optimize() {
		optimizeFactor(root);
	}
	
	/**
	 * 根据规则对节点进行优化
	 * @param parent 需要优化的节点
	 */
	private void optimizeFactor(Factor parent) {
		ParserRule rule = parent.getRule();
		if ( rule == null ) {
			return;
		}
		
		ArrayList<Factor> childList = parent.getAllChild();
		if ( childList != null ) {
			Factor childFactor = null;
			Iterator<Factor> itChildFactor = childList.iterator();
			while ( itChildFactor.hasNext() ) {
				childFactor = itChildFactor.next();
				optimizeFactor(childFactor);
			}
		}
		
		// 优化主要针对参数列表
		int ruleIndex = rule.getIndex();

		switch ( ruleIndex ) {
		case 1:
			// SL -> E ; SL
			optimizeRule1(parent);
			break;
		case 19:
			// E -> FUNC_HEAD FUNC_TAIL
			optimizeRule19(parent);
			break;
		case 22:
			// FUNC_TAIL -> PL )
			optimizeRule22(parent);
			break;
		case 24:
			// PL -> E , PL 24
			optimizeRule23(parent);
			break;
		case 26:
			// E -> IF_HEAD
			optimizeRule26(parent);
			break;
		case 27:
			// E -> IF_HEAD IF_TAIL
			optimizeRule27(parent);
			break;
		}
	}
	
	/**
	 * 优化规则1 SL -> E ; SL
	 * @param parent
	 */
	private void optimizeRule1(Factor parent) {
		Factor SL = parent.getFactor(2);
		parent.replaceChild(2, SL.getAllChild());
	}
	
	/**
	 * 优化规则 E -> FUNC_HEAD FUNC_TAIL
	 * @param parent
	 */
	private void optimizeRule19(Factor parent) {
		Factor FUNC_TAIL = parent.getFactor(1);
		parent.replaceChild(1, FUNC_TAIL.getAllChild());
		
		Factor FUNC_HEAD = parent.getFactor(0);
		parent.replaceChild(0, FUNC_HEAD.getAllChild());
	}
	
	/**
	 * 优化规则 FUNC_TAIL -> PL )
	 * @param parent
	 */
	private void optimizeRule22(Factor parent) {
		Factor PL = parent.getFactor(0);
		parent.replaceChild(0, PL.getAllChild());
	}
	
	/**
	 * 优化规则  PL -> E , PL 24
	 * @param parent
	 */
	private void optimizeRule23(Factor parent) {
		Factor PL = parent.getFactor(2);
		parent.replaceChild(2, PL.getAllChild());
	}
	
	/**
	 * 优化规则 E -> IF_HEAD
	 * @param parent
	 */
	private void optimizeRule26(Factor parent) {
		Factor IF_HEAD = parent.getFactor(0);
		parent.replaceChild(0, IF_HEAD.getAllChild());
	}
	
	/**
	 * 优化规则 E -> IF_HEAD IF_TAIL
	 * @param parent
	 */
	private void optimizeRule27(Factor parent) {
		Factor IF_TAIL = parent.getFactor(1);
		parent.replaceChild(1, IF_TAIL.getAllChild());
		
		Factor IF_HEAD = parent.getFactor(0);
		parent.replaceChild(0, IF_HEAD.getAllChild());
	}
}
