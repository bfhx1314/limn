package com.limn.tool.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * PredefinedParserRules类定义了规则集合,用来表示在语法规则说明中定义的规则,描述规则的first集合和非终结符的follow集合.
 * 此类为内部实现类,不再另作说明,也不在API文档中体现
 * @author 王元和
 * @since YES1.0
 */
public class PredefinedParserRules {
	/**
	 * 以因子描述的规则集合
	 */
	private ParserRule[] rules = null;
	/**
	 * 以产生式左部表示的规则集合
	 */
	private HashMap<Integer, List<ParserRule>> ruleMap = null;
	/**
	 * 语法因子跟名称的对应关系，用于测试时输出语法产生式的字符串表示
	 */
	private HashMap<Integer, String> factorNameMap = null;
	/**
	 * 以语法符号为第一个右部因子的规则映射
	 */
	private HashMap<Integer, ParserRule> firstRuleMap = null;
	
	/**
	 * 所有语法符号的集合
	 */
	private HashMap<Integer, ParserRuleFactor> factorMap = null;
	
	/**
	 * 语法因子和后序终结符的预测规则集
	 */
	private HashMap<Integer, RuleFactorFollowLookup> followRuleMap = null;
	
	private static Object[][] factorNameTable = new Object[][] {
			{ LexDef.N_SL, "SL" },
			{ LexDef.N_IF_S, "IF_S" },
			{ LexDef.N_WHILE_S, "WHILE_S" },
			{ LexDef.N_VAR_S, "VAR_S" },
			{ LexDef.N_E, "E" },
			{ LexDef.N_IF_HEAD, "IF_HEAD" },
			{ LexDef.N_IF_TAIL, "IF_TAIL" },
			{ LexDef.N_FUNC_HEAD, "FUNC_HEAD" },
			{ LexDef.N_FUNC_TAIL, "FUNC_TAIL" },
			{ LexDef.N_PL, "PL" },
			{ LexDef.T_SEMICOLON, ";" },
			{ LexDef.T_COMMA, "," },
			{ LexDef.T_IF, "if" },
			{ LexDef.T_ELSE, "else" },
			{ LexDef.T_LB, "(" },
			{ LexDef.T_RB, ")" },
			{ LexDef.T_L_BRACE, "{" },
			{ LexDef.T_R_BRACE, "}" },
			{ LexDef.T_ADD, "+" },
			{ LexDef.T_SUB, "-" },
			{ LexDef.T_MUL, "*" },
			{ LexDef.T_DIV, "/" },
			{ LexDef.T_STRCAT, "&" },
			{ LexDef.T_NOT, "!" },
			{ LexDef.T_OR, "||" },
			{ LexDef.T_AND, "&&" },
			{ LexDef.T_EQUAL, "==" },
			{ LexDef.T_ASSIGN, "=" },
			{ LexDef.T_NEQUAL, "<>" },
			{ LexDef.T_LARGE, ">" },
			{ LexDef.T_LARGE_EQUAL, ">=" },
			{ LexDef.T_LESS, "<" },
			{ LexDef.T_LESS_EQUAL, "<=" },
			{ LexDef.T_ID, "id" },
			{ LexDef.T_CONST, "const" },
			{ LexDef.T_FUN, "fun" },
			{ LexDef.T_VAR, "var" },
			{ LexDef.T_WHILE, "while" }
	};
	
	public PredefinedParserRules() {
		ruleMap = new HashMap<Integer, List<ParserRule>>();
		firstRuleMap = new HashMap<Integer, ParserRule>();
		factorNameMap = new HashMap<Integer, String>();
		factorMap = new HashMap<Integer, ParserRuleFactor>();
		followRuleMap = new HashMap<Integer, RuleFactorFollowLookup>(); 
		rules = new ParserRule[] {
			// SL -> E
			RULE(0, true, SL(), E()),
			// SL -> E ; SL
			RULE(1, false, SL(), E(), SEMICOLON(), SL()),
			// E -> E + E
			RULE(2, false, E(), E(), ADD(), E()),
			// E -> E - E
			RULE(3, false, E(), E(), SUB(), E()),
			// E -> E * E
			RULE(4, false, E(), E(), MUL(), E()),
			// E -> E / E
			RULE(5, false, E(), E(), DIV(), E()),
			// E -> E & E
			RULE(6, false, E(), E(), STRCAT(), E()),
			// E -> ( E )
			RULE(7, false, E(), LB(), E(), RB()),
			// E -> ! E
			RULE(8, false, E(), NOT(), E()),
			// E -> E || E
			RULE(9, false, E(), E(), OR(), E()),
			// E -> E && E
			RULE(10, false, E(), E(), AND(), E()),
			// E -> E == E
			RULE(11, false, E(), E(), EQUAL(), E()),
			// E -> E <> E
			RULE(12, false, E(), E(), NEQUAL(), E()),
			// E -> E > E
			RULE(13, false, E(), E(), LARGE(), E()),
			// E -> E >= E
			RULE(14, false, E(), E(), LARGE_EQUAL(), E()),
			// E -> E < E
			RULE(15, false, E(), E(), LESS(), E()),
			// E -> E <= E
			RULE(16, false, E(), E(), LESS_EQUAL(), E()),
			// E -> const
			RULE(17, false, E(), CONST()),
			// E -> id
			RULE(18, false, E(), ID()),
			// E -> FUNC_HEAD FUNC_TAIL
			RULE(19, false, E(), FUNC_HEAD(), FUNC_TAIL()),
			// FUNC_HEAD -> function (
			RULE(20, false, FUNC_HEAD(), FUN(), LB()),
			// FUNC_TAIL -> )
			RULE(21, false, FUNC_TAIL(), RB()),
			// FUNC_TAIL -> PL )
			RULE(22, false, FUNC_TAIL(), PL(), RB()),
			// PL -> E
			RULE(23, true, PL(), E()),
			// PL -> E , PL
			RULE(24, false, PL(), E(), COMMA(), PL()),
			// E -> var id = E
			RULE(25, false, E(), VAR(), ID(), ASSIGN(), E()),
			// E -> IF_HEAD
			RULE(26, false, E(), IF_HEAD()),
			// E -> IF_HEAD IF_TAIL
			RULE(27, false, E(), IF_HEAD(), IF_TAIL()),
			// IF_HEAD -> if ( E ) { SL }
			RULE(28, false, IF_HEAD(), IF(), LB(), E(), RB(), L_BRACE(), SL(), R_BRACE()),
			// IF_TAIL -> else { SL }
			RULE(29, false, IF_TAIL(), ELSE(), L_BRACE(), SL(), R_BRACE()),
			// E -> while ( E ) { SL }
			RULE(30, false, E(), WHILE(), LB(), E(), RB(), L_BRACE(), SL(), R_BRACE())
		};
		
		initFactorName();
		initPreToken();
		initFirstRightFactor();
		initDelayPredictRule();
		initFollowLookup();
		initFactorPreToken();
	}
	
	private void initFactorPreToken() {
		PRE_TOKEN(LexDef.N_SL);
		PRE_TOKEN(LexDef.N_E);
		PRE_TOKEN(LexDef.N_IF_S);
		PRE_TOKEN(LexDef.N_WHILE_S);
		PRE_TOKEN(LexDef.N_VAR_S);
		PRE_TOKEN(LexDef.N_IF_HEAD);
		PRE_TOKEN(LexDef.N_IF_TAIL);
		PRE_TOKEN(LexDef.N_FUNC_HEAD);
		PRE_TOKEN(LexDef.N_FUNC_TAIL);
		PRE_TOKEN(LexDef.N_PL);
	}
	
	/**
	 * 初始化后序终结符查询表
	 */
	private void initFollowLookup() {
		// SL -> E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, -1, 0);
		// SL -> E ; SL
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_SEMICOLON, 1);
		// E -> E + E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_ADD, 2);
		// E -> E - E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_SUB, 3);
		// E -> E * E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_MUL, 4);
		// E -> E / E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_DIV, 5);
		// E -> E & E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_STRCAT, 6);
		// E -> ( E )
		FOLLOW_PREDICT_INCLUDE(-1, -1, 7);
		// E -> ! E
		FOLLOW_PREDICT_INCLUDE(-1, -1, 8);
		// E -> E || E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_OR, 9);
		// E -> E && E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_AND, 10);
		// E -> E == E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_EQUAL, 11);
		// E -> E <> E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_NEQUAL, 12);
		// E -> E > E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_LARGE, 13);
		// E -> E >= E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_LARGE_EQUAL, 14);
		// E -> E < E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_LESS, 15);
		// E -> E <= E
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_LESS_EQUAL, 16);
		// E -> const
		FOLLOW_PREDICT_INCLUDE(-1, -1, 17);
		// E -> id
		FOLLOW_PREDICT_INCLUDE(-1, -1, 18);
		// E -> FUNC_HEAD FUNC_TAIL
		FOLLOW_PREDICT_INCLUDE(LexDef.N_FUNC_HEAD, -1, 19);
		// FUNC_HEAD -> function (
		FOLLOW_PREDICT_INCLUDE(-1, -1, 20);
		// FUNC_TAIL -> )
		FOLLOW_PREDICT_INCLUDE(-1, -1, 21);
		// FUNC_TAIL -> PL )
		FOLLOW_PREDICT_INCLUDE(LexDef.N_PL, LexDef.T_RB, 22);
		// PL -> E
		FOLLOW_PREDICT_INCLUDE(-1, -1, 23);
		// PL -> E , PL
		FOLLOW_PREDICT_INCLUDE(LexDef.N_E, LexDef.T_COMMA, 24);
		// E -> var id = E
		FOLLOW_PREDICT_INCLUDE(-1, -1, 25);
		// E -> IF_HEAD
		FOLLOW_PREDICT_INCLUDE(-1, -1, 26);
		FOLLOW_PREDICT_EXCLUDE(LexDef.N_IF_HEAD, LexDef.T_ELSE, 26);
		// E -> IF_HEAD IF_TAIL
		FOLLOW_PREDICT_INCLUDE(LexDef.N_IF_HEAD, LexDef.T_ELSE, 27);
		// IF_HEAD -> if ( E ) else { SL }
		FOLLOW_PREDICT_INCLUDE(-1, -1, 28);
		// IF_TAIL -> else { SL }
		FOLLOW_PREDICT_INCLUDE(-1, -1, 29);
		// E -> while ( E ) { SL }
		FOLLOW_PREDICT_INCLUDE(-1, -1, 30);
	}
	
	/**
	 * 初始化延迟预测规则
	 */
	private void initDelayPredictRule() {
		FOLLOW_PREDICT_IN_RULE(19, LexDef.N_E, LexDef.T_RB, 23);
		FOLLOW_PREDICT_IN_RULE(19, LexDef.N_E, LexDef.T_COMMA, 24);
		
		FOLLOW_PREDICT_IN_RULE(24, LexDef.N_E, LexDef.T_RB, 23);
	}
	
	/**
	 * 初始化前导终结符
	 */
	private void initPreToken() {
		int passCount = 1;
		int count = calcOnePassPreToken();
		while ( count > 0 ) {
			count = calcOnePassPreToken();
			++passCount;
		}
		//System.out.println("pass count = " + passCount);
	}
	
	/**
	 * 单次遍历计算前导终结符
	 * @return 遍历中实际增加的前导终结符的数量
	 */
	private int calcOnePassPreToken() {
		int count = 0;
		int length = rules.length;
		ParserRule rule = null;
		int firstFactorID = -1;
		for ( int i = 0; i<length; ++i ) {
			rule = rules[i];
			ParserRuleFactor[] rightParts = rule.getRight();
			ParserRuleFactor firstFactor = rightParts[0];
			firstFactorID = firstFactor.getID();
			if ( firstFactor.isTerminal() ) {
				count += rule.addPreToken(firstFactorID);
				rule.addDirectPreToken(firstFactorID);
			} else {
				List<ParserRule> ruleList = ruleMap.get(firstFactorID);
				if ( ruleList != null ) {
					ParserRule childRule = null;
					Iterator<ParserRule> itChildRule = ruleList.iterator();
					while ( itChildRule.hasNext() ) {
						childRule = itChildRule.next();
						count += rule.mergePreToken(childRule);
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * 计算所有非终结符推导规则的第一个右部符号
	 */
	private void initFirstRightFactor() {
		Set<Entry<Integer, ParserRuleFactor>> entrySet = factorMap.entrySet();
		Entry<Integer, ParserRuleFactor> entry = null;
		Iterator<Entry<Integer, ParserRuleFactor>> itEntry = entrySet.iterator();
		while ( itEntry.hasNext() ) {
			entry = itEntry.next();
			ParserRuleFactor factor = entry.getValue();
			if ( factor.isTerminal() ) {
				List<ParserRule> ruleList = factor.getReduceRule();
				if ( ruleList != null ) {
					ParserRule rule = null;
					Iterator<ParserRule> itRule = ruleList.iterator();
					List<ParserRuleFactor> firstRightFactorList = new ArrayList<ParserRuleFactor>();
					factor.setFirstRightFactorList(firstRightFactorList);
					HashSet<Integer> firstRightFactorSet = new HashSet<Integer>();
					while ( itRule.hasNext() ) {
						rule = itRule.next();
						ParserRuleFactor firstRightFactor = rule.getRightFactorAt(0);
						if ( !firstRightFactorSet.contains(firstRightFactor.getID() ) ) {
							firstRightFactorList.add(firstRightFactor);
							firstRightFactorSet.add(firstRightFactor.getID());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 取得语法中的规则数量
	 * @return 规则数量
	 */
	public int getRuleCount() {
		return rules.length;
	}
	
	/**
	 * 根据序号取得规则
	 * @param index
	 * @return 以因子描述的规则定义
	 */
	public ParserRule getRuleAt(int index) {
		return rules[index];
	}
	
	/**
	 * 计算语法因子的名称
	 * @param id
	 * @return 语法因子的名称
	 */
	public String getFactorName(int id) {
		return factorNameMap.get(id);
	}
	
	/**
	 * 根据词法符号取得产生式右部第一个符号为参数指定的终结符的语法规则
	 * @param id
	 * @return
	 */
	public ParserRule getFirstRule(int id) {
		return firstRuleMap.get(id);
	}
	
	/**
	 * 根据后续因子取得预测规则(包含后续因子)
	 * 只在后续因子是指定的因子时取得预测规则
	 * @param factorID - 当前语法因子
	 * @param followFactorID - 后续语法因子
	 * @return 匹配的规则集，此规则集在使用时还需要通过延迟预测集合的过滤
	 */
	public ParserRule getRuleByFollowIncludeFactor(int factorID, int followFactorID) {
		ParserRule rule = null;
		RuleFactorFollowLookup followRuleLookup = followRuleMap.get(factorID);
		if ( followRuleLookup != null ) {
			rule = followRuleLookup.getIncludeRule(followFactorID);
		}
		return rule;
	}
	
	/**
	 * 根据后续因子取得预测规则(不包含后续因子)
	 * 只在后续因子不是指定的因子时取得预测规则
	 * @param factorID - 当前语法因子
	 * @param followFactorID - 后续语法因子
	 * @return 匹配的规则集，此规则集在使用时还需要通过延迟预测集合的过滤
	 */
	public ParserRule getRuleByFollowExcludeFactor(int factorID, int followFactorID) {
		ParserRule rule = null;
		RuleFactorFollowLookup followRuleLookup = followRuleMap.get(factorID);
		if ( followRuleLookup != null ) {
			rule = followRuleLookup.getExcludeRule(followFactorID);
		}
		return rule;
	}
	
	private ParserRule RULE(int index, boolean closeRule, ParserRuleFactor left, ParserRuleFactor... rights) {
		ParserRule rule = new ParserRule(index, left, rights, closeRule);
		factorMap.put(left.getID(), left);
		int length = rights.length;
		for ( int i = 0; i<length; ++i ) {
			ParserRuleFactor rightRuleFactor = rights[i];
			factorMap.put(rightRuleFactor.getID(), rightRuleFactor);
			if ( i == 0 && rightRuleFactor.isTerminal() ) {
				firstRuleMap.put(rightRuleFactor.getID(), rule);
			}
		}
		List<ParserRule> ruleList = ruleMap.get(left.getID());
		if ( ruleList == null ) {
			ruleList = new ArrayList<ParserRule>();
			ruleMap.put(left.getID(), ruleList);
		}
		ruleList.add(rule);
		return rule;
	}
	
	/**
	 * 计算语法符号的名称，用于测试目的
	 */
	private void initFactorName() {
		factorNameMap = new HashMap<Integer, String>();
		int length = factorNameTable.length;
		for ( int i = 0; i<length; ++i ) {
			Object[] vLine = factorNameTable[i];
			Integer id = (Integer)vLine[0];
			String name = (String)vLine[1];
			factorNameMap.put(id, name);
		}
	}
	
	/**
	 * 根据产生左部取得所有其推导的所有产生集合
	 * @param leftFactorID
	 * @return 产生式列表
	 */
	public List<ParserRule> getRuleListByLeft(int leftFactorID) {
		List<ParserRule> ruleList = null;
		ruleList = this.ruleMap.get(leftFactorID);
		return ruleList;
	}
	
	private void PRE_TOKEN(int factorID) {
		ParserRuleFactor factor = this.factorMap.get(factorID);
		List<ParserRule> ruleList = this.ruleMap.get(factorID);
		if ( ruleList != null ) {
			ParserRule rule = null;
			Iterator<ParserRule> it = ruleList.iterator();
			while ( it.hasNext() ) {
				rule = it.next();
				factor.addPreToken(rule.getPreToken());
				factor.addDirectPreToken(rule.getDirectPreToken());
			}
			
			String strPreToken = null;
			String strDirectPreToken = null;
			HashSet<Integer> preTokenSet = factor.getPreToken();
			HashSet<Integer> directPreTokenSet = factor.getDirectPreToken();
			
			Integer token = null;
			if ( preTokenSet != null ) {
				Iterator<Integer> itToken = preTokenSet.iterator();
				while ( itToken.hasNext() ) {
					token = itToken.next();
					if ( strPreToken == null ) {
						strPreToken = getFactorName(token);
					} else {
						strPreToken += " " + getFactorName(token);
					}
				}
			}
			
			if ( directPreTokenSet != null ) {
				Iterator<Integer> itToken = directPreTokenSet.iterator();
				while ( itToken.hasNext() ) {
					token = itToken.next();
					if ( strDirectPreToken == null ) {
						strDirectPreToken = getFactorName(token);
					} else {
						strDirectPreToken += " " + getFactorName(token);
					}
				}
			}
			factor.setStringPreToken(strPreToken);
			factor.setStringDirectPreToken(strDirectPreToken);
		}
	}

	private void FOLLOW_PREDICT_INCLUDE(int factorID, int followFactorID, int ruleIndex) {
		if ( factorID == -1 ) {
			return;
		}
		ParserRule rule = rules[ruleIndex];
		RuleFactorFollowLookup lookup = followRuleMap.get(factorID);
		if ( lookup == null ) {
			lookup = new RuleFactorFollowLookup(factorID);
			followRuleMap.put(factorID, lookup);
		}
		lookup.addIncludeRule(followFactorID, rule);
	}
	
	private void FOLLOW_PREDICT_EXCLUDE(int factorID, int followFactorID, int ruleIndex) {
		if ( factorID == -1 ) {
			return;
		}
		ParserRule rule = rules[ruleIndex];
		RuleFactorFollowLookup lookup = followRuleMap.get(factorID);
		if ( lookup == null ) {
			lookup = new RuleFactorFollowLookup(factorID);
			followRuleMap.put(factorID, lookup);
		}
		lookup.addExcludeRule(followFactorID, rule);
	}
	
	private void FOLLOW_PREDICT_IN_RULE(int ruleIndex, int factorID, 
			int followFactorID, int... delayRuleIndexes) {
		int delayRuleCount = delayRuleIndexes.length;
		ParserRule rule = rules[ruleIndex];
		for ( int i = 0; i<delayRuleCount; ++i ) {
			ParserRule delayRule = rules[delayRuleIndexes[i]];
			rule.addFollowPredictRuleIncludeFactor(factorID, followFactorID, delayRule);
		}
	}
	
	private static ParserRuleFactor SL = null;
	private static ParserRuleFactor SL() {
		if ( SL == null ) {
			SL = new ParserRuleFactor(LexDef.N_SL, LexDef.S_N_SL, false);
		}
		return SL;
	}
	
	private static ParserRuleFactor E = null;
	private static ParserRuleFactor E() {
		if ( E == null ) {
			E = new ParserRuleFactor(LexDef.N_E, LexDef.S_N_E, false);
		}
		return E;
	}
	
	private static ParserRuleFactor IF_S = null;
	public static ParserRuleFactor IF_S() {
		if ( IF_S == null ) {
			IF_S = new ParserRuleFactor(LexDef.N_IF_S, LexDef.S_N_IF_S, false);
		}
		return IF_S;
	}
	
	private static ParserRuleFactor WHILE_S = null;
	public static ParserRuleFactor WHILE_S() {
		if ( WHILE_S == null ) {
			WHILE_S = new ParserRuleFactor(LexDef.N_WHILE_S, LexDef.S_N_WHILE_S, false);
		}
		return WHILE_S;
	}
	
	private static ParserRuleFactor VAR_S = null;
	public static ParserRuleFactor VAR_S() {
		if ( VAR_S == null ) {
			VAR_S = new ParserRuleFactor(LexDef.N_VAR_S, LexDef.S_N_VAR_S, false);
		}
		return VAR_S;
	}
	
	private static ParserRuleFactor IF_HEAD = null;
	public static ParserRuleFactor IF_HEAD() {
		if ( IF_HEAD == null ) {
			IF_HEAD = new ParserRuleFactor(LexDef.N_IF_HEAD, LexDef.S_N_IF_HEAD, false);
		}
		return IF_HEAD;
	}
	
	private static ParserRuleFactor IF_TAIL = null;
	public static ParserRuleFactor IF_TAIL() {
		if ( IF_TAIL == null ) {
			IF_TAIL = new ParserRuleFactor(LexDef.N_IF_TAIL, LexDef.S_N_IF_TAIL, false);
		}
		return IF_TAIL;
	}
	
	private static ParserRuleFactor FUNC_HEAD = null;
	public static ParserRuleFactor FUNC_HEAD() {
		if ( FUNC_HEAD == null ) {
			FUNC_HEAD = new ParserRuleFactor(LexDef.N_FUNC_HEAD, LexDef.S_N_FUNC_HEAD, false);
		}
		return FUNC_HEAD;
	}
	
	private static ParserRuleFactor FUNC_TAIL = null;
	public static ParserRuleFactor FUNC_TAIL() {
		if ( FUNC_TAIL == null ) {
			FUNC_TAIL = new ParserRuleFactor(LexDef.N_FUNC_TAIL, LexDef.S_N_FUNC_TAIL, false);
		}
		return FUNC_TAIL;
	}
	
	private static ParserRuleFactor PL = null;
	public static ParserRuleFactor PL() {
		if ( PL == null ) {
			PL = new ParserRuleFactor(LexDef.N_PL, LexDef.S_N_PL, false);
		}
		return PL;
	}
	
	private static ParserRuleFactor ADD = null;
	public static ParserRuleFactor ADD() {
		if ( ADD == null ) {
			ADD = new ParserRuleFactor(LexDef.T_ADD, LexDef.S_T_ADD, true);
		}
		return ADD;
	}
	
	private static ParserRuleFactor STRCAT = null;
	public static ParserRuleFactor STRCAT() {
		if ( STRCAT == null ) {
			STRCAT = new ParserRuleFactor(LexDef.T_STRCAT, LexDef.S_T_STRCAT, true);
		}
		return STRCAT;
	}
	
	private static ParserRuleFactor SUB = null;
	public static ParserRuleFactor SUB() {
		if ( SUB == null ) {
			SUB = new ParserRuleFactor(LexDef.T_SUB, LexDef.S_T_SUB, true);
		}
		return SUB;
	}
	
	private static ParserRuleFactor MUL = null;
	public static ParserRuleFactor MUL() {
		if ( MUL == null ) {
			MUL = new ParserRuleFactor(LexDef.T_MUL, LexDef.S_T_MUL, true);
		}
		return MUL;
	}
	
	private static ParserRuleFactor DIV = null;
	public static ParserRuleFactor DIV() {
		if ( DIV == null ) {
			DIV = new ParserRuleFactor(LexDef.T_DIV, LexDef.S_T_DIV, true);
		}
		return DIV;
	}
	
	private static ParserRuleFactor OR = null;
	public static ParserRuleFactor OR() {
		if ( OR == null ) {
			OR = new ParserRuleFactor(LexDef.T_OR, LexDef.S_T_OR, true);
		}
		return OR;
	}
	
	private static ParserRuleFactor AND = null;
	public static ParserRuleFactor AND() {
		if ( AND == null ) {
			AND = new ParserRuleFactor(LexDef.T_AND, LexDef.S_T_AND, true);
		}
		return AND;
	}
	
	private static ParserRuleFactor EQUAL = null;
	public static ParserRuleFactor EQUAL() {
		if ( EQUAL == null ) {
			EQUAL = new ParserRuleFactor(LexDef.T_EQUAL, LexDef.S_T_EQUAL, true);
		}
		return EQUAL;
	}
	
	private static ParserRuleFactor ASSIGN = null;
	public static ParserRuleFactor ASSIGN() {
		if ( ASSIGN == null ) {
			ASSIGN = new ParserRuleFactor(LexDef.T_ASSIGN, LexDef.S_T_ASSIGN, true);
		}
		return ASSIGN;
	}
	
	private static ParserRuleFactor NEQUAL = null;
	public static ParserRuleFactor NEQUAL() {
		if ( NEQUAL == null ) {
			NEQUAL = new ParserRuleFactor(LexDef.T_NEQUAL, LexDef.S_T_NEQUAL, true);
		}
		return NEQUAL;
	}
	
	private static ParserRuleFactor LARGE = null;
	public static ParserRuleFactor LARGE() {
		if ( LARGE == null ) {
			LARGE = new ParserRuleFactor(LexDef.T_LARGE, LexDef.S_T_LARGE, true);
		}
		return LARGE;
	}
	
	private static ParserRuleFactor LARGE_EQUAL = null;
	public static ParserRuleFactor LARGE_EQUAL() {
		if ( LARGE_EQUAL == null ) {
			LARGE_EQUAL = new ParserRuleFactor(LexDef.T_LARGE_EQUAL, LexDef.S_T_LARGE_EQUAL, true);
		}
		return LARGE_EQUAL;
	}
	
	private static ParserRuleFactor LESS = null;
	public static ParserRuleFactor LESS() {
		if ( LESS == null ) {
			LESS = new ParserRuleFactor(LexDef.T_LESS, LexDef.S_T_LESS, true);
		}
		return LESS;
	}
	
	private static ParserRuleFactor LESS_EQUAL = null;
	public static ParserRuleFactor LESS_EQUAL() {
		if ( LESS_EQUAL == null ) {
			LESS_EQUAL = new ParserRuleFactor(LexDef.T_LESS_EQUAL, LexDef.S_T_LESS_EQUAL, true);
		}
		return LESS_EQUAL;
	}
	
	private static ParserRuleFactor FUN = null;
	public static ParserRuleFactor FUN() {
		if ( FUN == null ) {
			FUN = new ParserRuleFactor(LexDef.T_FUN, LexDef.S_T_FUN, true);
		}
		return FUN;
	}
	
	private static ParserRuleFactor ID = null;
	public static ParserRuleFactor ID() {
		if ( ID == null ) {
			ID = new ParserRuleFactor(LexDef.T_ID, LexDef.S_T_ID, true);
		}
		return ID;
	}
	
	private static ParserRuleFactor CONST = null;
	public static ParserRuleFactor CONST() {
		if ( CONST == null ) {
			CONST = new ParserRuleFactor(LexDef.T_CONST, LexDef.S_T_CONST, true);
		}
		return CONST;
	}
	
	private static ParserRuleFactor LB = null;
	public static ParserRuleFactor LB() {
		if ( LB == null ) {
			LB = new ParserRuleFactor(LexDef.T_LB, LexDef.S_T_LB, true);
		}
		return LB;
	}
	
	private static ParserRuleFactor RB = null;
	public static ParserRuleFactor RB() {
		if ( RB == null ) {
			RB = new ParserRuleFactor(LexDef.T_RB, LexDef.S_T_RB, true);
		}
		return RB;
	}
	
	private static ParserRuleFactor COMMA = null;
	public static ParserRuleFactor COMMA() {
		if ( COMMA == null ) {
			COMMA = new ParserRuleFactor(LexDef.T_COMMA, LexDef.S_T_COMMA, true);
		}
		return COMMA;
	}
	
	private static ParserRuleFactor DOLLAR = null;
	public static ParserRuleFactor DOLLAR() {
		if ( DOLLAR == null ) {
			DOLLAR = new ParserRuleFactor(LexDef.T_DOLLAR, LexDef.S_T_DOLLAR, true);
		}
		return DOLLAR;
	}
	
	private static ParserRuleFactor IF = null;
	public static ParserRuleFactor IF() {
		if ( IF == null ) {
			IF = new ParserRuleFactor(LexDef.T_IF, LexDef.S_T_IF, true);
		}
		return IF;
	}
	
	private static ParserRuleFactor ELSE = null;
	public static ParserRuleFactor ELSE() {
		if ( ELSE == null ) {
			ELSE = new ParserRuleFactor(LexDef.T_ELSE, LexDef.S_T_ELSE, true);
		}
		return ELSE;
	}
	
	private static ParserRuleFactor WHILE = null;
	public static ParserRuleFactor WHILE() {
		if ( WHILE == null ) {
			WHILE = new ParserRuleFactor(LexDef.T_WHILE, LexDef.S_T_WHILE, true);
		}
		return WHILE;
	}
	
	private static ParserRuleFactor VAR = null;
	public static ParserRuleFactor VAR() {
		if ( VAR == null ) {
			VAR = new ParserRuleFactor(LexDef.T_VAR, LexDef.S_T_VAR, true);
		}
		return VAR;
	}
	
	private static ParserRuleFactor NOT = null;
	public static ParserRuleFactor NOT() {
		if ( NOT == null ) {
			NOT = new ParserRuleFactor(LexDef.T_NOT, LexDef.S_T_NOT, true);
		}
		return NOT;
	}
	
	private static ParserRuleFactor SEMICOLON = null;
	public static ParserRuleFactor SEMICOLON() {
		if ( SEMICOLON == null ) {
			SEMICOLON = new ParserRuleFactor(LexDef.T_SEMICOLON, LexDef.S_T_SEMICOLON, true);
		}
		return SEMICOLON;
	}
	
	private static ParserRuleFactor L_BRACE = null;
	public static ParserRuleFactor L_BRACE() {
		if ( L_BRACE == null ) {
			L_BRACE = new ParserRuleFactor(LexDef.T_L_BRACE, LexDef.S_T_L_BRACE, true);
		}
		return L_BRACE;
	}
	
	private static ParserRuleFactor R_BRACE = null;
	public static ParserRuleFactor R_BRACE() {
		if ( R_BRACE == null ) {
			R_BRACE = new ParserRuleFactor(LexDef.T_R_BRACE, LexDef.S_T_R_BRACE, true);
		}
		return R_BRACE;
	}
	
	private static PredefinedParserRules INSTANCE = null;
	public static PredefinedParserRules getInstance() {
		if ( INSTANCE == null ) {
			PredefinedParserRules instance = createInstance();
			INSTANCE = instance;
		}
		return INSTANCE;
	}
	
	private static synchronized PredefinedParserRules createInstance() {
		return new PredefinedParserRules();
	}
}
