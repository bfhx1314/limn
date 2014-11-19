package com.limn.tool.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 语法规则的定义类，定义一个语法产生式的左部和右部，
 * <p><pre>
 * 类似E -> E + E
 * 那么表示方法为
 * left:LexDef.N_E
 * vRight:LexDef.N_E LexDef.T_ADD LexDef.N_E
 * </pre><p>
 * 创建时间 2013-09-24
 * @author 王元和
 * @since YES1.0
 */
public class ParserRule {
	/**
	 * 规则序号
	 */
	private int index = -1;
	/**
	 * 规则左部
	 */
	private ParserRuleFactor leftPart = null;
	
	/**
	 * 规则右部
	 */
	private ParserRuleFactor[] rightParts = null;
	
	/**
	 * 前导终结符号
	 */
	private HashSet<Integer> preTokenSet = null;
	
	/**
	 * 直接前导终结符号
	 */
	private HashSet<Integer> directPreTokenSet = null;
	
	/**
	 * 算法标识，对产生式没有意义，只是外部算法的标记
	 */
	private int flag = 0;
	
	/**
	 * 这符串表示的句子，调试用
	 */
	private String statement = null;
	
	/**
	 * 是否递归的关闭规则
	 */
	private boolean closeRule = false;
	
	/**
	 * 语法因子和后序终结符的预测规则集(在些规则中限定)
	 */
	private HashMap<Integer, RuleFactorFollowLookup> followRuleMap = null;
	
	public ParserRule(int index, ParserRuleFactor leftPart, ParserRuleFactor[] rightParts, boolean closeRule) {
		this.index = index;
		this.leftPart = leftPart;
		this.rightParts = rightParts;
		this.closeRule = closeRule;
		preTokenSet = new HashSet<Integer>();
		directPreTokenSet = new HashSet<Integer>();
		this.statement = this.getStatement();
	}
	
	/**
	 * 取得规则序号
	 * @return 序号
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * 取得产生式左部
	 * @return ParserRuleFactor
	 */
	public ParserRuleFactor getLeft() {
		return leftPart;
	}
	
	/**
	 * 取得产生式右部
	 * @return ParserRuleFactor[]
	 */
	public ParserRuleFactor[] getRight() {
		return rightParts;
	}
	
	/**
	 * 根据序号取得产生式右部的因子
	 * @param index
	 * @return 产生式因子
	 */
	public ParserRuleFactor getRightFactorAt(int index) {
		return rightParts[index];
	}
	
	/**
	 * 取得产生式右部长度
	 * @return 长度
	 */
	public int getRightLength() {
		return rightParts.length;
	}
	
	/**
	 * 取得是否递归的关闭规则
	 * @return 如果是关闭规则返回true，否则返回false
	 */
	public boolean isCloseRule() {
		return closeRule;
	}
	
	/**
	 * 增加前导终结符号
	 * @param preToken 前导终结符号
	 * @return 返回增加的数量
	 */
	public int addPreToken(int preToken) {
		int count = 0;
		if ( !preTokenSet.contains(preToken) ) {
			preTokenSet.add(preToken);
			++count;
		}
		return count;
	}
	
	/**
	 * 判断一个符号是否是当前产生式的前导终结符号
	 * @param token 词法符号
	 * @return 如果setPreToken中存在，则返回true,否则返回false
	 */
	public boolean isPreToken(int token) {
		return preTokenSet.contains(token);
	}
	
	/**
	 * 取得前导终点符号集合
	 * @return HashSet<Integer>
	 */
	public HashSet<Integer> getPreToken() {
		return preTokenSet;
	}
	
	/**
	 * 合并前导终结符
	 * @param srcRule
	 * @return 返回实际增加的数量
	 */
	public int mergePreToken(ParserRule srcRule) {
		int count = 0;
		HashSet<Integer> srcSetPreToken = srcRule.getPreToken();
		Iterator<Integer> it = srcSetPreToken.iterator();
		Integer preToken = null;
		while ( it.hasNext() ) {
			preToken = it.next();
			count += addPreToken(preToken);
		}
		return count;
	}
	
	/**
	 * 增加直接前导终结符
	 * @param token - 文法符号
	 */
	public void addDirectPreToken(int token) {
		directPreTokenSet.add(token);
	}
	
	/**
	 * 判断一个文法符号是否是其直接前导终结符
	 * @param token
	 * @return
	 */
	public boolean isDirectPreToken(int token) {
		return directPreTokenSet.contains(token);
	}
	
	/**
	 * 取得直接前导终点符号集合
	 * @return HashSet<Integer>
	 */
	public HashSet<Integer> getDirectPreToken() {
		return directPreTokenSet;
	}
	
	/**
	 * 设置算法标志
	 * @param flag 整型标记
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	/**
	 * 取得算法标志
	 * @return 整型标记
	 */
	public int getFlag() {
		return flag;
	}
	
	/**
	 * 返回以字符串表示的推导规则
	 * @return 推导规则的字符串表示,比如E -> E + E
	 */
	public String getStatement() {
		String leftString = leftPart.getName();
		String rightString = null;
		int length = rightParts.length;
		for ( int i = 0; i<length; ++i ) {
			if ( rightString == null ) {
				rightString = rightParts[i].getName();
			} else {
				rightString += " " + rightParts[i].getName();
			}
		}
		return leftString + " -> " + rightString;
	}
	
	/**
	 * 给规则本身增加以follow集合表示的预测规则
	 * 该函数不对外使用,仅在算法实现中使用.
	 * @param factorID 语法的非终结符
	 * @param followFactorID follow集中词法符号,这里做过变形,不仅仅包括非终结符
	 * @param rule 推导规则
	 */
	public void addFollowPredictRuleIncludeFactor(int factorID, int followFactorID, ParserRule rule) {
		if ( factorID == -1 ) {
			return;
		}
		if ( followRuleMap == null ) {
			followRuleMap = new HashMap<Integer, RuleFactorFollowLookup>();
		}
		RuleFactorFollowLookup lookup = followRuleMap.get(factorID);
		if ( lookup == null ) {
			lookup = new RuleFactorFollowLookup(factorID);
			followRuleMap.put(factorID, lookup);
		}
		lookup.addIncludeRule(followFactorID, rule);
	}
	
	/**
	 * 根据后续因子取得预测规则
	 * @param factorID - 当前语法因子
	 * @param followFactorID - 后续语法因子
	 * @return 匹配的规则集，此规则集在使用时还需要通过延迟预测集合的过滤
	 */
	public ParserRule getRuleByFollowIncludeFactor(int factorID, int followFactorID) {
		ParserRule rule = null;
		if ( followRuleMap != null ) {
			RuleFactorFollowLookup followRuleLookup = followRuleMap.get(factorID);
			if ( followRuleLookup != null ) {
				rule = followRuleLookup.getIncludeRule(followFactorID);
			}
		}
		return rule;
	}
	
	/**
	 * 返回规则的字符串表示,为了调试目的,输出这个字符串是为了方便查看正在使用的规则
	 * @return 规则的字符串表示
	 */
	public String getStringStatement() {
		return this.statement;
	}
}
