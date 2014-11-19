package com.limn.tool.parser;

import java.util.HashSet;
import java.util.List;

/**
 * 产生式的规则因子，一批规则因子的数组描述一条产生式规则
 * 创建时间 2013-09-24
 * @author 王元和
 */
public class ParserRuleFactor {
	/**
	 * 词法符号
	 */
	private int id = -1;
	
	/**
	 * 是否为终结符
	 */
	private boolean isTerminal = false;
	
	/**
	 * 符号名称
	 */
	private String name = null;
	
	/**
	 * 当前符号出发的直接推导规则，不包括传递的推导
	 */
	private List<ParserRule> reduceRuleList = null;
	
	/**
	 * 当前符号出发的规则中，第一右部符号集合
	 */
	private List<ParserRuleFactor> firstRightFactorList = null;
	
	/**
	 * 可推导出的规则的前导终结符号
	 */
	private HashSet<Integer> preTokenSet = null;
	private String strPreToken = null;
	
	/**
	 * 可推导出的规则的直接前导终结符号
	 */
	private HashSet<Integer> directPreTokenSet = null;
	private String strDirectPreToken = null;
	
	public ParserRuleFactor(int id, String name, boolean isTerminal) {
		this.id = id;
		this.name = name;
		this.isTerminal = isTerminal;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isTerminal() {
		return isTerminal;
	}
		
	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
	
	public void setReduceRule(List<ParserRule> reduceRuleList) {
		this.reduceRuleList = reduceRuleList;
	}
	
	public List<ParserRule> getReduceRule() {
		return reduceRuleList;
	}
	
	public void setFirstRightFactorList(List<ParserRuleFactor> firstRightFactorList) {
		this.firstRightFactorList = firstRightFactorList;
	}
	
	public List<ParserRuleFactor> getFirstRightFactorList() {
		return firstRightFactorList;
	}
	
	public boolean isMatch(int lexID) {
		return id == lexID;
	}
	
	public void addPreToken(Integer token) {
		if ( preTokenSet == null ) {
			preTokenSet = new HashSet<Integer>();
		}
		preTokenSet.add(token);
	}
	
	public void addPreToken(HashSet<Integer> preTokenSet) {
		if ( preTokenSet != null ) {
			if ( this.preTokenSet == null ) {
				this.preTokenSet = new HashSet<Integer>();
			}
			this.preTokenSet.addAll(preTokenSet);
		}
	}
	
	public HashSet<Integer> getPreToken() {
		return preTokenSet;
	}
	
	public void addDirectPreToken(Integer token) {
		if ( directPreTokenSet == null ) {
			directPreTokenSet = new HashSet<Integer>();
		}
		directPreTokenSet.add(token);
	}
	
	public void addDirectPreToken(HashSet<Integer> directPreTokenSet) {
		if ( directPreTokenSet != null ) {
			if ( this.directPreTokenSet == null ) {
				this.directPreTokenSet = new HashSet<Integer>();
			}
			this.directPreTokenSet.addAll(directPreTokenSet);
		}
	}
	
	public HashSet<Integer> getDirectPreToken() {
		return directPreTokenSet;
	}
	
	public void setStringPreToken(String strPreToken) {
		this.strPreToken = strPreToken;
	}
	
	public String getStringPreToken() {
		return strPreToken;
	}
	
	public void setStringDirectPreToken(String strDirectPreToken) {
		this.strDirectPreToken = strDirectPreToken;
	}
	
	public String getStringDirectPreToken() {
		return strDirectPreToken;
	}
}
