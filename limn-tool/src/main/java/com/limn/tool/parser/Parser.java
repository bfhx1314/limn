package com.limn.tool.parser;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Stack;

/**
 * Parser作为平台定义的脚本解释器
 * 预测分析,实际上这里是一个预测加向前看的复合体
 * 目的是用来产生一个语法树，再根据这个语法树执行
 * @author 王元和
 * @since YES1.0
 */
public class Parser implements INameResolver {
	/**
	 * 是否有消费的标志
	 */
	private static final int CONSUMED = 0x01;
	
	/**
	 * 是否完全匹配的标志
	 */
	private static final int FULL_MATCHED = 0x02;
	
	/**
	 * 函数实现映射 
	 */
	private IFunctionImplMap functionImplMap = null;

	/**
	 * 构造函数,初始化函数实现映射为默认映射
	 */
	public Parser() {
		functionImplMap = DefaultFunctionImplMap.getInstance();
	}
	
	/**
	 * 计算一个表达式的结果
	 * @param env 解析器的环境
	 * @param script 输入字符串
	 * @param syntaxTree 目标语法树
	 * @param context 函数实现需要的上下文
	 * @return 如果计算成功,返回表达式的值,否则抛出异常
	 * @throws Exception
	 */
	public Object eval(IEvalEnv env, String script, SyntaxTree syntaxTree,
			IEvalContext context) throws Exception {
		Object result = null;
		if ( parse(script, syntaxTree) ) {
			syntaxTree.optimize();
			Evaluator evaluator = new Evaluator(functionImplMap, env, syntaxTree);
			evaluator.exec(context);
			result = syntaxTree.getRoot().getValue();
		}
		return result;
	}
	
	/**
	 * 语法解析,将输入的字符串解析成语法树表示
	 * @param script 输入字符串
	 * @param syntaxTree 目标语法树对象
	 * @return 如果解析成功,返回true,如果解析失败,返回false,参见ParserException
	 * @throws ParserException
	 */
	public boolean parse(String script, SyntaxTree syntaxTree) throws ParserException {
		// 初始化词法解析器
		Lexer lex = new Lexer(script);
		// 设置名解析器
		lex.setNameResolver(this);
		// 取得语法规则集
		PredefinedParserRules rules = PredefinedParserRules.getInstance();
		String lexValue = null;
		
		// 取得第一个词法符号
		int lexID = lex.nextID();
		lexValue = lex.getLexValue();
		int dataType = lex.getType();
		
		ParserRuleTrace topRuleTrace = null;
		ParserRuleTrace delayRuleTrace = null;
		ParserRuleTrace curRuleTrace = null;
		
		boolean successed = false;
		RefInt refLexID = new RefInt(-1);
		int delayForwardFactorID = -1;
		int consumPos = -1;
		
		boolean finish = lexID == -1;
		Stack<ParserRuleTrace> ruleTraceStack = new Stack<ParserRuleTrace>();
		while ( !finish ) {
			// 预先解决延迟的规则
			if ( delayRuleTrace != null ) {
				delayPredict(rules, ruleTraceStack, syntaxTree, 
						delayRuleTrace, delayForwardFactorID);
				delayRuleTrace = null;
			}
			
			// 取得栈顶元素
			if ( ruleTraceStack.isEmpty() ) {
				topRuleTrace = null;
			} else {
				topRuleTrace = ruleTraceStack.peek();
				printStack(ruleTraceStack);
				topRuleTrace.printTrace();
			}
			
			// 取得是否需要预测下一个规则
			refLexID.setValue(lexID);
			boolean needPredict = checkNeedPredict(ruleTraceStack, topRuleTrace, refLexID);
			lexID = refLexID.getValue();
			
			// 对于需要预测的
			if ( needPredict ) {
				ParserRule predictRule = rules.getFirstRule(lexID);
				// 如果有先导符号为当前输入的规则
				if ( predictRule != null ) {
					ParserRuleTrace predictRuleTrace = new ParserRuleTrace(predictRule);
					ruleTraceStack.add(predictRuleTrace);
					
					consumPos = predictRuleTrace.consum(lexID);
					// 处理语法树
					Factor terminalFactor = putToSyntaxTree(syntaxTree, predictRuleTrace);
					// 在语法树当前消费的位置,设置词文
					processLexValue(lexID, terminalFactor, consumPos, dataType, lexValue);
					
					// 取下一个词法符号
					lexID = lex.nextID();
					lexValue = lex.getLexValue();
					dataType = lex.getType();
					finish = lexID == -1;
					printStack(ruleTraceStack);
					predictRuleTrace.printTrace();
					topRuleTrace = predictRuleTrace;
					
					
				} else {
					// 没有可预测的规则，那么试图将栈顶加入延迟预测规则
					if ( topRuleTrace != null && topRuleTrace.isMatched() ) {
						delayRuleTrace = topRuleTrace;
						printStack(ruleTraceStack);
						delayRuleTrace.printTrace();
						delayForwardFactorID = lexID;
					} else {
						// 无效的输入
						throw new ParserException(ParserException.INVALID_INPUT, "Invalid input " + lexValue);
					}
				}
			}
			
			// 如果栈顶成功匹配，那么将其出栈，并且让上一级规则来消费匹配的语法因子
			if ( topRuleTrace.isMatched() ) {
				curRuleTrace = topRuleTrace;
				ruleTraceStack.pop();
				// 根据当前词法符号及输入来匹配最大规则
				// 如果检测到可以匹配的规则，那么将其加入延迟判断的处理中
				if ( checkNeedMoreMatch(rules, ruleTraceStack, topRuleTrace, lexID) ) {
					ruleTraceStack.add(topRuleTrace);
					delayRuleTrace = topRuleTrace;
					printStack(ruleTraceStack);
					delayRuleTrace.printTrace();
					delayForwardFactorID = lexID;
				} else {
					int consumResult = consumUp(ruleTraceStack, syntaxTree, 
							curRuleTrace, lexID);
				
					if ( (consumResult & CONSUMED) == 0 ) {
						// 如果没有消费，则判断栈顶元素是否是上次被延迟未处理的
						topRuleTrace = curRuleTrace;
						ruleTraceStack.push(topRuleTrace);
					
						// 如果没有匹配，那么需要延迟预测的规则是当前元素
						delayRuleTrace = topRuleTrace;
						printStack(ruleTraceStack);
						delayRuleTrace.printTrace();
						delayForwardFactorID = lexID;
					} else {
						// 如果没有完全匹配
						if ( (consumResult & FULL_MATCHED) == 0 ) {
							// 如果有消费，但没有完全匹配，那么需要延迟预测的规则为当前栈顶元素
							topRuleTrace = ruleTraceStack.peek();
							if ( topRuleTrace.isMatched() ) {
								delayRuleTrace = topRuleTrace;
								printStack(ruleTraceStack);
								delayRuleTrace.printTrace();
								delayForwardFactorID = lexID;
							} else {
								printStack(ruleTraceStack);
							}
						} else {
							// 如果完全匹配，应该解析完成
							successed = true;
							lexID = lex.nextID();
							lexValue = lex.getLexValue();
							dataType = lex.getType();
							if ( lexID != -1 ) {
								topRuleTrace = ruleTraceStack.pop();
								if ( checkNeedMoreMatch(rules, ruleTraceStack, topRuleTrace, lexID)) {
									ruleTraceStack.push(topRuleTrace);

									delayRuleTrace = topRuleTrace;
									printStack(ruleTraceStack);
									delayRuleTrace.printTrace();
									delayForwardFactorID = lexID;
								} else {
									successed = false;
									resolveProblem4MoreInput(rules, ruleTraceStack, lexID, lexValue);
									break;
								}
							} else {
								break;
							}
						}
					}
				}
			}
			
			// 如果词法符号被消费，那么继续取下一个
			// 取得下一个词法符号
			if ( lexID == -1 ) {
				lexID = lex.nextID();
				lexValue = lex.getLexValue();
				dataType = lex.getType();
				delayForwardFactorID = lexID;
				finish = lexID == -1;
				
				if ( lexID == -1 ) {
					// 如果输入已经是-1，但栈不空，并且栈顶未匹配，说明输入不完整
					if ( !ruleTraceStack.isEmpty() ) {
						topRuleTrace = ruleTraceStack.peek();
						if ( !topRuleTrace.isMatched() ) {
							resolveProblem4NotMatch(rules, ruleTraceStack);
							break;
						} else {
							// 如果栈顶只有一个对象,说明匹配成功
							if ( ruleTraceStack.size() == 1 ) {
								// 否则解析成功
								successed = true;
								break;
							} else if ( delayRuleTrace != null ) {
								// 如果有需要延迟预测的规则
								finish = false;
							} else {
								// 出错处理
							}
						}
					}
				}
			}
		}
		
		if ( successed ) {
			syntaxTree.extract();
		}
		return successed;
	}
	
	/**
	 * 词文处理,把常量的值设置到语法树元素中,把函数的名称设置到语法树元素中
	 * @param lexID 词法元素ID
	 * @param factor 语法树元素
	 * @param pos 规则中子对象的位置,也就是需要设置值的元素
	 * @param dataType 常量的数据类型,对函数名称无用
	 * @param lexValue 词文
	 */
	private void processLexValue(int lexID, Factor factor, int pos, int dataType, String lexValue) {
		factor.setChildLexValue(pos, lexValue);
		if ( lexID == LexDef.T_FUN ) {
			if ( LexDef.CTRL_FUNCTION_IIFS.equalsIgnoreCase(lexValue)
					|| LexDef.CTRL_FUNCTION_IIFS.equalsIgnoreCase(lexValue) ) {
				factor.setChildControlFunction(pos, true);
			}
		}
		switch ( lexID ) {
		case LexDef.T_CONST : {
			Object value = null;
			switch ( dataType ) {
			case LexDef.L_TYPE_BOOL: {
				value = Boolean.parseBoolean(lexValue);
			}
				break;
			case LexDef.L_TYPE_INT: {
				value = Long.parseLong(lexValue);
			}
				break;
			case LexDef.L_TYPE_NUMBER: {
				value = BigDecimal.valueOf(Double.parseDouble(lexValue));
			}
				break;
			case LexDef.L_TYPE_STRING: {
				value = lexValue;
			}
				break;
			}
			factor.setChildValue(pos, value);
		}
			break;
		}
	}
	
	/**
	 * 将一个解析跟踪步骤表示的规则加入到语法树
	 * @param syntaxTree 语法树
	 * @param ruleTrace 规则运行跟踪对象
	 * @return 返回新增的语法树规则
	 */
	private Factor putToSyntaxTree(SyntaxTree syntaxTree, ParserRuleTrace ruleTrace) {
		ParserRule rule = ruleTrace.getRule();
		Factor factor = new Factor(rule.getLeft(), rule);
		ParserRuleFactor[] rightParts = rule.getRight();
		int length = rightParts.length;
		for ( int i = 0; i<length; ++i ) {
			Factor childFactor = new Factor(rightParts[i]);
			factor.addChild(childFactor);
		}
		syntaxTree.push(factor);
		return factor;
	}
	
	/**
	 * 延迟预测,其方法是根据当前规则跟踪对象及输入符号以及follow集合来预测当前规则的下一个推导规则
	 * @param rules 语法规则集合
	 * @param ruleTraceStack 分析栈
	 * @param syntaxTree 语法树
	 * @param delayRuleTrace 被延迟的规则跟踪对象
	 * @param factorID 输入的词法符号,这里不仅仅有终结符号,还包括一些非终结符
	 */
	private void delayPredict(PredefinedParserRules rules, Stack<ParserRuleTrace> ruleTraceStack, 
			SyntaxTree syntaxTree, ParserRuleTrace delayRuleTrace, int factorID) {
		// 延迟规则出栈
		ruleTraceStack.pop();
		Factor topFactor = syntaxTree.pop();
		ParserRule delayPredictRule = resolveDelayRule(rules, ruleTraceStack, delayRuleTrace, factorID);
		if ( delayPredictRule != null ) {
			// 产生预测规则的跟踪，加入到分析栈
			ParserRuleTrace delayPredictRuleTrace = new ParserRuleTrace(delayPredictRule);
			ruleTraceStack.add(delayPredictRuleTrace);
			
			// 向语法树中增加新的规则
			putToSyntaxTree(syntaxTree, delayPredictRuleTrace);
			syntaxTree.push(topFactor);
			// 调用消费函数消费被延迟的规则
			consum(syntaxTree, delayPredictRuleTrace, delayRuleTrace);
			// 调试输出
			// 1.打印分析栈
			printStack(ruleTraceStack);
			// 2.打印延迟预测的结果规则
			delayPredictRuleTrace.printTrace();
		} else {
			// 如果找不到，那么延迟规则再次入栈
			// 这里应该有一个出错检查点，如果已经不存在输入的情况下，应该报错
			ruleTraceStack.add(delayRuleTrace);
			syntaxTree.push(topFactor);
		}
	}
	
	/**
	 * 处理不完全匹配错误
	 * @param rules 规则集合
	 * @param ruleTraceStack 分析栈
	 * @throws ParserException 抛出不完全的输入异常
	 */
	private void resolveProblem4NotMatch(PredefinedParserRules rules,
			Stack<ParserRuleTrace> ruleTraceStack) throws ParserException {
		// 在没有匹配的时候，查找期望的输入是什么
		ParserRuleTrace topRuleTrace = ruleTraceStack.peek();
		ParserRuleFactor nextFactor = topRuleTrace.getNextRuleFactor();
		if ( nextFactor.isTerminal() ) {
			throw new ParserException(ParserException.INCOMPLETE_INPUT, "Incomplete input,except " + nextFactor.getName());
		} else {
			throw new ParserException(ParserException.INCOMPLETE_INPUT, "Incomplete input,except " + nextFactor.getStringPreToken());
		}
	}
	
	/**
	 * 处理过多的输入异常
	 * @param rules 规则集合
	 * @param ruleTraceStack 分析栈
	 * @param lexID 词文分析器返回的词法符号
	 * @param lexValue 词文
	 * @throws ParserException 如果输入有效,那么抛出不完全的输入错误,否则输出无效输入错误
	 */
	private void resolveProblem4MoreInput(PredefinedParserRules rules,
			Stack<ParserRuleTrace> ruleTraceStack, int lexID, String lexValue) throws ParserException {
		// 还有更多的输入
		// 取出栈顶以判断缺少什么
		ParserRuleTrace delayRuleTrace = ruleTraceStack.pop();
		ParserRule delayPredictRule = resolveDelayRule(rules, ruleTraceStack, delayRuleTrace, lexID);
		if ( delayPredictRule != null ) {
			ParserRuleTrace delayPredictRuleTrace = new ParserRuleTrace(delayPredictRule);
			// 让栈顶元素消费当前输入，以确定错误的原因
			delayPredictRuleTrace.consum(delayRuleTrace.getLeftID());
			delayPredictRuleTrace.consum(lexID);
			ParserRuleFactor nextFactor = delayPredictRuleTrace.getNextRuleFactor();
			if ( nextFactor.isTerminal() ) {
				throw new ParserException(ParserException.INCOMPLETE_INPUT, "Incomplete input,except " + nextFactor.getName());
			} else {
				throw new ParserException(ParserException.INCOMPLETE_INPUT, "Incomplete input,except " + nextFactor.getStringPreToken());
			}
		} else {
			// 无效的输入
			throw new ParserException(ParserException.INVALID_INPUT, "Invalid input " + lexValue);
		}
	}
	
	/**
	 * 取得是否需要预测规则
	 * @param ruleTraceStack - 分析栈
	 * @param topRuleTrace - 顶部规则
	 * @param lexID - 当前输入
	 * @return
	 */
	private boolean checkNeedPredict(Stack<ParserRuleTrace> ruleTraceStack, 
			ParserRuleTrace topRuleTrace, RefInt lexID) {
		boolean needPredict = true;
		if ( topRuleTrace != null ) {
			if ( !topRuleTrace.isMatched() ) {
				// 如果下一个语法因子可以匹配，则消费一个当前输入的语法因子
				if ( topRuleTrace.isMatchNext(lexID.getValue()) ) {
					topRuleTrace.consum(lexID.getValue());
					lexID.setValue(-1);
					printStack(ruleTraceStack);
					topRuleTrace.printTrace();
					needPredict = false;
				}
			} else {
				// 如果是关闭规则，则不需要预测，需要回溯
				if ( topRuleTrace.isCloseRule() ) {
					needPredict = false;
				}
			}
		}
		return needPredict;
	}
	
	/**
	 * 打印分析栈
	 * @param ruleTraceStack
	 */
	private void printStack(Stack<ParserRuleTrace> ruleTraceStack) {
		Iterator<ParserRuleTrace> it = ruleTraceStack.iterator();
		ParserRuleTrace trace = null;
		while ( it.hasNext() ) {
			trace = it.next();
			trace.printStack();
		}
	}
	
	/**
	 * 查找延迟规则
	 * @param rules - 规则集
	 * @param ruleTraceStack - 规则运行时跟踪栈
	 * @param delayRule - 被延迟的规则
	 * @param lexID - 当前输入的语法因子符号
	 * @return 如果成功取得预测规则，返回此规则，否则返回null
	 */
	private ParserRule resolveDelayRule(PredefinedParserRules rules, 
			Stack<ParserRuleTrace> ruleTraceStack, ParserRuleTrace delayRule, int lexID) {
		int factorID = delayRule.getLeftID();
		ParserRuleTrace topRuleTrace = null;
		ParserRule topRule = null;
		ParserRule rule = null;
		if ( !ruleTraceStack.isEmpty() ) {
			topRuleTrace = ruleTraceStack.peek();
			topRule = topRuleTrace.getRule();
		}
		if ( topRule != null ) {
			rule = topRule.getRuleByFollowIncludeFactor(factorID, lexID);
			if ( rule == null ) {
				rule = topRule.getRuleByFollowIncludeFactor(factorID, -1);
			}
		}
		
		if ( rule == null ) {
			rule = rules.getRuleByFollowIncludeFactor(factorID, lexID);
		}
		if ( rule == null ) {
			rule = rules.getRuleByFollowIncludeFactor(factorID, -1);
		}
		
		if ( rule == null ) {
			rule = rules.getRuleByFollowExcludeFactor(factorID, lexID);
		}
		
		return rule;
	}
	
	/**
	 * 匹配是否存在更大的匹配,因为预测规则也是取大优先,比如
	 * <p><pre>E -> E + E
	 *E -> const
	 *在匹配E -> const之后,如果有+号跟从,那么应该取大优先,再去匹配E -> E + E规则,而不是直接返回E结束.
	 * </pre></p>
	 * @param rules 规则集合
	 * @param ruleTraceStack 分析栈
	 * @param rule 被检查是否有更多匹配的规则
	 * @param lexID 词法分析器返回的词文符号
	 * @return
	 */
	private boolean checkNeedMoreMatch(PredefinedParserRules rules, 
			Stack<ParserRuleTrace> ruleTraceStack, ParserRuleTrace rule, int lexID) {
		ParserRule matchRule = resolveDelayRule(rules, ruleTraceStack, rule, lexID);
		if ( matchRule != null ) {
			if ( rule.getRule().getLeft() != matchRule.getLeft() ) {
				matchRule = null;
			}
		}
		return matchRule != null;
	}
	
	/**
	 * 向上逐级消费语法因子
	 * @param ruleTraceStack
	 * @param curRule
	 * @return 如果完全匹配，则返回true，否则返回false
	 */
	private int consumUp(Stack<ParserRuleTrace> ruleTraceStack, 
			SyntaxTree syntaxTree, ParserRuleTrace curRule, int lexID) {
		boolean fullMatched = false;
		boolean consumed = false;
		if ( !ruleTraceStack.isEmpty() ) {
			// 取得栈顶
			ParserRuleTrace topRule = ruleTraceStack.peek();
			if ( !topRule.isMatched() && topRule.isMatchNext(curRule.getLeftID()) ) {
				consumed = true;
				// 消费一个语法因子，并弹出新的栈顶，以继续下一轮循环
				consum(syntaxTree, topRule, curRule);
			
				while ( topRule.isMatched() ) {
					if ( !ruleTraceStack.isEmpty() ) {
						curRule = topRule;
						ruleTraceStack.pop();
						if ( !ruleTraceStack.isEmpty() ) {
							topRule = ruleTraceStack.peek();
							if ( topRule.isMatchNext(curRule.getLeftID()) ) {
								consum(syntaxTree, topRule, curRule);
							} else {
								ruleTraceStack.add(curRule);
								break;
							}
						} else {
							fullMatched = true;
							// 栈为空，应该已经匹配成功
							// 将最后成功匹配的规则压入栈顶
							ruleTraceStack.add(topRule);
							break;
						}
					} else {
						break;
					}
				}
			}
		}
		int result = 0;
		if ( fullMatched ) {
			result |= FULL_MATCHED;
		}
		if ( consumed ) {
			result |= CONSUMED;
		}
		return result;
	}
	
	/**
	 * 消费一个规则中的元素,主要是为了步进规则跟踪对象的位置和处理语法树
	 * @param syntaxTree 语法树
	 * @param sourceRule 源泉规则跟踪对象
	 * @param feedRule 被消费的跟踪对象
	 */
	private void consum(SyntaxTree syntaxTree, ParserRuleTrace sourceRule, 
			ParserRuleTrace feedRule) {
		sourceRule.consum(feedRule.getLeftID());
		int pos = sourceRule.getPos();
		Factor feedFactor = syntaxTree.pop();
		Factor sourceFactor = syntaxTree.peek();
		Factor curFactor = sourceFactor.getFactor(pos);
		curFactor.mergeFactor(feedFactor);
	}

	@Override
	public boolean resolveFunction(String token) {
		boolean matched = false;
		
		if ( LexDef.CTRL_FUNCTION_IIF.equalsIgnoreCase(token) ) {
			matched = true;
		} else if ( LexDef.CTRL_FUNCTION_IIFS.equalsIgnoreCase(token) ) {
			matched = true;
		}
		
		if ( !matched ) {
			matched = functionImplMap.containFunction(token);
		}
		
		return matched;
	}
}
