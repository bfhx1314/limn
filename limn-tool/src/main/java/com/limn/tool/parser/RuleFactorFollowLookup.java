package com.limn.tool.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 语法因子，跟从符号预测规则对照表
 * E -> E * E
 * 那么记录E后跟*时，采用此规则
 * 创建时间 2013-11-11
 * @author 王元和
 */
public class RuleFactorFollowLookup {
	/**
	 * 语法符号
	 */
	private int factorID = -1;
	/**
	 * 包含某个符号时的预测规则集合
	 */
	private HashMap<Integer, ParserRule> includeRuleMap = null;
	/**
	 * 排斥某个符号时的预测规则集合
	 */
	private HashMap<Integer, ParserRule> excludeRuleMap = null;
	/**
	 * 构造函数,以语法符号来初始一个对照表
	 * @param factorID
	 */
	public RuleFactorFollowLookup(int factorID) {
		this.factorID = factorID;
	}
	
	/**
	 * 取得语法符号
	 * @return
	 */
	public int getFactorID() {
		return factorID;
	}
	
	/**
	 * 从包含规则中取得可用规则
	 * @param followFactorID 跟在factorID之后的符号
	 * @return 如果存在此规则,则返回该规则,否则返回null
	 */
	public ParserRule getIncludeRule(int followFactorID) {
		if ( includeRuleMap == null ) {
			return null;
		}
		return includeRuleMap.get(followFactorID);
	}
	
	/**
	 * 增加包含规则
	 * @param followFactorID 跟从语法符号
	 * @param rule 规则
	 */
	public void addIncludeRule(int followFactorID, ParserRule rule) {
		if ( includeRuleMap == null ) {
			includeRuleMap = new HashMap<Integer, ParserRule>();
		}
		includeRuleMap.put(followFactorID, rule);
	}
	
	/**
	 * 从排斥规则中取得某个规则
	 * @param followFactorID 跟从语法符号
	 * @return 如果存在此规则,则返回null,否则返回查找表中第一个规则
	 */
	public ParserRule getExcludeRule(int followFactorID ) {
		if ( excludeRuleMap == null ) {
			return null;
		}
		ParserRule resultRule = null;
		if ( !excludeRuleMap.isEmpty() ) {
			Iterator<Entry<Integer, ParserRule>> it = excludeRuleMap.entrySet().iterator();
			Entry<Integer, ParserRule> entry = it.next();
			resultRule = entry.getValue();
		}
		if ( excludeRuleMap.containsKey(followFactorID) ) {
			resultRule = null;
		}
		return resultRule;
	}
	
	/**
	 * 增加排斥规则
	 * @param followFactorID 跟踪语法符号
	 * @param rule 规则
	 */
	public void addExcludeRule(int followFactorID, ParserRule rule) {
		if ( excludeRuleMap == null ) {
			excludeRuleMap = new HashMap<Integer, ParserRule>();
		}
		excludeRuleMap.put(followFactorID, rule);
	}
}
