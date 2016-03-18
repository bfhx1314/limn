package com.limn.tool.parser;

import java.math.BigDecimal;
import java.util.Date;

import com.limn.tool.def.DataType;
import com.limn.tool.struct.HashMapIgnoreCase;
import com.limn.tool.util.TypeConvertor;

/**
 * Evaluator为语法树的解析执行类，其唯一的作用就是递归一个语法树，解释其中每条规则的执行，
 * 这里不一一说明，每条规则的执行方法可以参见具体的实现函数。
 * 其执行完成后返回根的结果
 * @author 王元和
 *
 */
public class Evaluator {
	/** 需要执行的语法树 */
	private SyntaxTree syntanTree = null;
	/** 函数实现映射 */
	private IFunctionImplMap functionImplMap = null;
	/** 环境 */
	private IEvalEnv evalEnv = null;
	/**
	 * 内部变量的存储堆，所有通过var声明的变量都存储在这个堆中，其主要提供以下功能：
	 * <p>1.向堆中增加一个变量</p>
	 * <p>2.取得一个变量的值</p>
	 * <p>3.判断一个变量是否存在</p>
	 * 内部类，具体函数不再另外说明
	 * @author 王元和
	 *
	 */
	class Heap {
		private HashMapIgnoreCase<Object> variableMap = null;
		public Heap() {
			variableMap = new HashMapIgnoreCase<Object>();
		}
		
		public void addVariable(String name, Object value) {
			variableMap.put(name, value);
		}
		
		public Object getVariable(String name) {
			return variableMap.get(name);
		}
		
		public boolean containVariable(String name) {
			return variableMap.containsKey(name);
		}
	}
	
	/**
	 * 内部堆
	 */
	private Heap heap = new Heap();
	
	/**
	 * 构造函数
	 * @param functionImplMap 函数实现映射
	 * @param evalEnv 环境
	 * @param syntanTree 语法树
	 */
	public Evaluator(IFunctionImplMap functionImplMap, IEvalEnv evalEnv,
			SyntaxTree syntanTree) {
		this.functionImplMap = functionImplMap;
		this.evalEnv = evalEnv;
		this.syntanTree = syntanTree;
	}
	
	/**
	 * 执行并返回最终的计算结果
	 * @param context 上下文
	 * @return 如果执行成功返回根的结果，否则返回null
	 * @throws Exception 计算过程中的任何异常，通过是函数实现的异常
	 */
	public Object exec(IEvalContext context) throws Exception {
		Object value = null;
		exec(context, syntanTree.getRoot());
		return value;
	}
	
	private void exec(IEvalContext context, Factor parent) throws Exception {
		ParserRule rule = parent.getRule();
		int ruleIndex = -1;		
		if ( rule != null ) {
			ruleIndex = rule.getIndex();
			switch ( ruleIndex ) {
			case 0:
				execRule0(context, parent);
				break;
			case 1:
				execRule1(context, parent);
				break;
			case 2:
				execRule2(context, parent);
				break;
			case 3:
				execRule3(context, parent);
				break;
			case 4:
				execRule4(context, parent);
				break;
			case 5:
				execRule5(context, parent);
				break;
			case 6:
				execRule6(context, parent);
				break;
			case 7:
				execRule7(context, parent);
				break;
			case 8:
				execRule8(context, parent);
				break;
			case 9:
				execRule9(context, parent);
				break;
			case 10:
				execRule10(context, parent);
				break;
			case 11:
				execRule11(context, parent);
				break;
			case 12:
				execRule12(context, parent);
				break;
			case 13:
				execRule13(context, parent);
				break;
			case 14:
				execRule14(context, parent);
				break;
			case 15:
				execRule15(context, parent);
				break;
			case 16:
				execRule16(context, parent);
				break;
			case 17:
				execRule17(context, parent);
				break;
			case 18:
				execRule18(context, parent);
				break;
			case 19:
				execRule19(context, parent);
				break;
			case 20:
				execRule20(context, parent);
				break;
			case 21:
				execRule21(context, parent);
				break;
			case 22:
				execRule22(context, parent);
				break;
			case 23:
				execRule23(context, parent);
				break;
			case 24:
				execRule24(context, parent);
				break;
			case 25:
				execRule25(context, parent);
				break;
			case 26:
				execRule26(context, parent);
				break;
			case 27:
				execRule27(context, parent);
				break;
			case 28:
				execRule28(context, parent);
				break;
			case 29:
				execRule29(context, parent);
				break;
			case 30:
				execRule30(context, parent);
				break;
			}
		}
	}
	
	// SL -> E
	private void execRule0(IEvalContext context, Factor factor) throws Exception {
		Factor E = factor.getFactor(0);
		this.exec(context, E);
		
		factor.setValue(E.getValue());
	}
	
	// SL -> E ; SL
	private void execRule1(IEvalContext context, Factor factor) throws Exception {
		int expLength = (factor.getChildCount() + 1) / 2;
		Object value = null;
		for ( int i = 0; i<expLength; ++i ) {
			Factor E = factor.getFactor(i * 2);
			this.exec(context, E);
			
			value = E.getValue();
		}
		factor.setValue(value);
	}
			
	// E -> E + E
	private void execRule2(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 + l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.add(d2));
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(s1 + s2);
		}
			break;
		}
	}
	
	// E -> E - E
	private void execRule3(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 - l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.subtract(d2));
		}
			break;
		}
	}
				
	// E -> E * E
	private void execRule4(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 * l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.multiply(d2));
		}
			break;
		}
	}
				
	// E -> E / E
	private void execRule5(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 / l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.divide(d2, 10, BigDecimal.ROUND_HALF_DOWN));
		}
			break;
		}
	}
		
	// E -> E & E
	private void execRule6(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType(E1.getValue(), E2.getValue());
		if ( dataType == DataType.STRING ) {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(s1 + s2);
		}
	}
		
	// E -> ( E )
	private void execRule7(IEvalContext context, Factor factor) throws Exception {
		Factor E = factor.getFactor(1);
		this.exec(context, E);
		
		factor.setValue(E.getValue());
	}
		
	// E -> ! E
	private void execRule8(IEvalContext context, Factor factor) throws Exception {
		Factor E = factor.getFactor(1);
		this.exec(context, E);
		
		Boolean value = TypeConvertor.toBoolean(E.getValue());
		factor.setValue(!value);
	}
		
	// E -> E || E
	private void execRule9(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
		Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
		factor.setValue(b1 || b2);
	}
		
	// E -> E && E
	private void execRule10(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
		Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
		factor.setValue(b1 && b2);
	}
	
	// E -> E == E
	private void execRule11(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1.equals(l2));
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.equals(d2));
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(s1.equalsIgnoreCase(s2));
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			factor.setValue(b1.equals(b2));
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			factor.setValue(d1.equals(d2));
		}
			break;
		}
	}
		
	// E -> E <> E
	private void execRule12(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(!l1.equals(l2));
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(!d1.equals(d2));
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(!s1.equalsIgnoreCase(s2));
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			factor.setValue(!b1.equals(b2));
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			factor.setValue(!d1.equals(d2));
		}
			break;
		}
	}
		
	// E -> E > E
	private void execRule13(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 > l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.compareTo(d2) == 1);
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(s1.compareToIgnoreCase(s2) == 1);
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			factor.setValue(b1.compareTo(b2) == 1);
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			factor.setValue(d1.compareTo(d2) == 1);
		}
			break;
		}
	}
		
	// E -> E >= E
	private void execRule14(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 >= l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			int compareResult = d1.compareTo(d2);
			factor.setValue(compareResult == 1 || compareResult == 0);
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			int compareResult = s1.compareToIgnoreCase(s2);
			factor.setValue(compareResult == 1 || compareResult == 0);
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			int compareResult = b1.compareTo(b2);
			factor.setValue(compareResult == 1 || compareResult == 0);
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			int compareResult = d1.compareTo(d2);
			factor.setValue(compareResult == 1 || compareResult == 0);
		}
			break;
		}
	}
	
	// E -> E < E
	private void execRule15(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 < l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			factor.setValue(d1.compareTo(d2) == -1);
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			factor.setValue(s1.compareToIgnoreCase(s2) == -1);
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			factor.setValue(b1.compareTo(b2) == -1);
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			factor.setValue(d1.compareTo(d2) == -1);
		}
			break;
		}
	}
		
	// E -> E <= E
	private void execRule16(IEvalContext context, Factor factor) throws Exception {
		Factor E1 = factor.getFactor(0);
		Factor E2 = factor.getFactor(2);
		this.exec(context, E1);
		this.exec(context, E2);
		
		int dataType = TypeCheck.getCompatibleType4Logic(E1.getValue(), E2.getValue());
		switch ( dataType ) {
		case DataType.INT: {
			Long l1 = TypeConvertor.toLong(E1.getValue());
			Long l2 = TypeConvertor.toLong(E2.getValue());
			factor.setValue(l1 <= l2);
		}
			break;
		case DataType.NUMERIC: {
			BigDecimal d1 = TypeConvertor.toBigDecimal(E1.getValue());
			BigDecimal d2 = TypeConvertor.toBigDecimal(E2.getValue());
			int compareResult = d1.compareTo(d2);
			factor.setValue(compareResult == -1 || compareResult == 0);
		}
			break;
		case DataType.STRING: {
			String s1 = TypeConvertor.toString(E1.getValue());
			String s2 = TypeConvertor.toString(E2.getValue());
			int compareResult = s1.compareToIgnoreCase(s2);
			factor.setValue(compareResult == -1 || compareResult == 0);
		}
			break;
		case DataType.BOOLEAN: {
			Boolean b1 = TypeConvertor.toBoolean(E1.getValue());
			Boolean b2 = TypeConvertor.toBoolean(E2.getValue());
			int compareResult = b1.compareTo(b2);
			factor.setValue(compareResult == -1 || compareResult == 0);
		}
			break;
		case DataType.DATETIME: {
			Date d1 = TypeConvertor.toDate(E1.getValue());
			Date d2 = TypeConvertor.toDate(E2.getValue());
			int compareResult = d1.compareTo(d2);
			factor.setValue(compareResult == -1 || compareResult == 0);
		}
			break;
		}
	}
	
	// E -> const
	private void execRule17(IEvalContext context, Factor factor) throws Exception {
		Factor constFactor = factor.getFactor(0);
		factor.setValue(constFactor.getValue());
	}
	
	// E -> id
	private void execRule18(IEvalContext context, Factor factor) throws Exception {
		Factor idFactor = factor.getFactor(0);
		String id = (String)idFactor.getValue();
		factor.setValue(evalEnv.getValue(id));
	}
			
	// E -> FUNC_HEAD FUNC_TAIL
	private void execRule19(IEvalContext context, Factor factor) throws Exception {
		Factor fun = factor.getFactor(0);
		String name = fun.getLexValue();
		
		boolean controlValue = false;
		
		// if ( 1, 2, 3, 4 )
		int length = (factor.getChildCount() - 2) / 2;
		Object[] arguments = new Object[length];
		for ( int i = 0; i<length; ++i ) {
			Factor P = factor.getFactor(2 + i * 2);
			if ( fun.isControlFunction() ) {
				if ( LexDef.CTRL_FUNCTION_IIF.equalsIgnoreCase(name) ) {
					if ( i == 0 ) {
						this.exec(context, P);
						controlValue = TypeConvertor.toBoolean(P.getValue());
					} else if ( controlValue ) {
						if ( i == 1 ) {
							this.exec(context, P);
							arguments[i] = P.getValue();
						}
					} else {
						if ( i == 2 ) {
							this.exec(context, P);
							arguments[i] = P.getValue();
						}
					}
				} else {
					if ( i % 2 == 0 ) {
						controlValue = TypeConvertor.toBoolean(P.getValue());
					} else {
						if ( controlValue ) {
							this.exec(context, P);
							arguments[i] = P.getValue();
							break;
						}
					}
				}
			} else {
				this.exec(context, P);
				arguments[i] = P.getValue();
			}
		}
		
		
		Object result = null;
		IFunctionImpl impl = functionImplMap.getFunctionImpl(name);
		if ( impl != null ) {
			result = impl.calc(name, context, arguments);
		}
		factor.setValue(result);
	}
			
	// FUNC_HEAD -> function (
	private void execRule20(IEvalContext context, Factor factor) throws Exception {
		
	}
	
	// FUNC_TAIL -> )
	private void execRule21(IEvalContext context, Factor factor) throws Exception {
		
	}
		
	// FUNC_TAIL -> PL )
	private void execRule22(IEvalContext context, Factor factor) throws Exception {
		
	}
		
	// PL -> E
	private void execRule23(IEvalContext context, Factor factor) throws Exception {
		
	}
	
	// PL -> E , PL
	private void execRule24(IEvalContext context, Factor factor) throws Exception {
		
	}
			
	// E -> var id = E
	private void execRule25(IEvalContext context, Factor factor) throws Exception {
		Factor id = factor.getFactor(1);
		Factor E = factor.getFactor(3);
		this.exec(context, E);
		
		heap.addVariable(id.getLexValue(), E.getValue());
	}
		
	// E -> IF_HEAD
	private void execRule26(IEvalContext context, Factor factor) throws Exception {
		// if ( E ) { SL }
		Factor E = factor.getFactor(2);
		this.exec(context, E);
		if ( TypeConvertor.toBoolean(E.getValue()) ) {
			Factor SL = factor.getFactor(5);
			this.exec(context, SL);
		}
	}
	
	// E -> IF_HEAD IF_TAIL
	private void execRule27(IEvalContext context, Factor factor) throws Exception {
		// if ( E } { SL1 } else { SL2 }
		Factor E = factor.getFactor(2);
		this.exec(context, E);
		if ( TypeConvertor.toBoolean(E.getValue()) ) {
			Factor SL1 = factor.getFactor(5);
			this.exec(context, SL1);
		} else {
			Factor SL2 = factor.getFactor(9);
			this.exec(context, SL2);
		}
	}
			
	// IF_HEAD -> if ( E ) { SL }
	private void execRule28(IEvalContext context, Factor factor) throws Exception {
		Factor E = factor.getFactor(2);
		this.exec(context, E);
		if ( TypeConvertor.toBoolean(E.getValue()) ) {
			Factor SL = factor.getFactor(5);
			this.exec(context, SL);
		}
	}
	
	// IF_TAIL -> else { SL }
	private void execRule29(IEvalContext context, Factor factor) throws Exception {
		
	}
		
	// E -> while ( E ) { SL }
	private void execRule30(IEvalContext context, Factor factor) throws Exception {
		Factor E = factor.getFactor(2);
		Factor SL = factor.getFactor(5);
		this.exec(context, E);
		while ( TypeConvertor.toBoolean(E.getValue()) ) {
			this.exec(context, SL);
			this.exec(context, E);
		}
	}
}

