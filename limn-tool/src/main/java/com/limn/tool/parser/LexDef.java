package com.limn.tool.parser;

/**
 * 词法定义常量
 * @author 王元和
 * @since YES1.0
 */
public class LexDef {
	/** 加法(+) */
	public static final int T_ADD = 0;
	/**  */
	public static final int T_STRCAT = 1;
	public static final int T_SUB = 2;
	public static final int T_MUL = 3;
	public static final int T_DIV = 4;
	public static final int T_OR = 5;
	public static final int T_AND = 6;
	public static final int T_EQUAL = 7;
	public static final int T_ASSIGN = 8;
	public static final int T_NEQUAL = 9;
	public static final int T_LARGE = 10;
	public static final int T_LARGE_EQUAL = 11;
	public static final int T_LESS = 12;
	public static final int T_LESS_EQUAL = 13;
	public static final int T_FUN = 14;
	public static final int T_ID = 15;
	public static final int T_CONST = 16;
	public static final int T_LB = 17;
	public static final int T_RB = 18;
	public static final int T_COMMA = 19;
	public static final int T_DOLLAR = 20;
	public static final int T_IF = 21;
	public static final int T_ELSE = 22;
	public static final int T_WHILE = 23;
	public static final int T_VAR = 24;
	public static final int T_NOT = 25;
	public static final int T_SEMICOLON = 26;
	public static final int T_L_BRACE = 27;
	public static final int T_R_BRACE = 28;

	public static final int N_SL = 101;
	public static final int N_E = 102;
	public static final int N_IF_S = 103;
	public static final int N_WHILE_S = 104;
	public static final int N_VAR_S = 105;
	public static final int N_IF_HEAD = 106;
	public static final int N_IF_TAIL = 107;
	public static final int N_FUNC_HEAD = 108;
	public static final int N_FUNC_TAIL = 109;
	public static final int N_PL = 110;
	
	public static final int SCOPE_SELF = 0;
	public static final int SCOPE_PARENT = 1;
	public static final int SCOPE_OTHER = 2;
	public static final int SCOPE_OBJECT = 3;
	
	public static final int L_TYPE_INT = 0;
	public static final int L_TYPE_NUMBER = 1;
	public static final int L_TYPE_STRING = 2;
	public static final int L_TYPE_BOOL = 3;
	
	public static final String S_N_SL = "SL";
	public static final String S_N_IF_S = "IF_S";
	public static final String S_N_WHILE_S = "WHILE_S";
	public static final String S_N_VAR_S = "VAR_S";
	public static final String S_N_E = "E";
	public static final String S_N_IF_HEAD = "IF_HEAD";
	public static final String S_N_IF_TAIL = "IF_TAIL";
	public static final String S_N_FUNC_HEAD = "FUNC_HEAD";
	public static final String S_N_FUNC_TAIL = "FUNC_TAIL";
	public static final String S_N_PL = "PL";
	public static final String S_T_SEMICOLON = ";";
	public static final String S_T_COMMA = ",";
	public static final String S_T_DOLLAR = "$";
	public static final String S_T_IF = "if";
	public static final String S_T_ELSE = "else";
	public static final String S_T_LB = "(";
	public static final String S_T_RB = ")";
	public static final String S_T_L_BRACE = "{";
	public static final String S_T_R_BRACE = "}";
	public static final String S_T_ADD = "+";
	public static final String S_T_SUB = "-";
	public static final String S_T_MUL = "*";
	public static final String S_T_DIV = "/";
	public static final String S_T_STRCAT = "&";
	public static final String S_T_NOT = "!";
	public static final String S_T_OR = "||";
	public static final String S_T_AND = "&&";
	public static final String S_T_EQUAL = "==";
	public static final String S_T_ASSIGN = "=";
	public static final String S_T_NEQUAL = "<>";
	public static final String S_T_LARGE = ">";
	public static final String S_T_LARGE_EQUAL = ">=";
	public static final String S_T_LESS = "<";
	public static final String S_T_LESS_EQUAL = "<=";
	public static final String S_T_ID = "id";
	public static final String S_T_CONST = "const";
	public static final String S_T_FUN = "fun";
	public static final String S_T_VAR = "var";
	public static final String S_T_WHILE = "while";
	
	public static final String CTRL_FUNCTION_IIF = "iif";
	public static final String CTRL_FUNCTION_IIFS = "iifs";
}
