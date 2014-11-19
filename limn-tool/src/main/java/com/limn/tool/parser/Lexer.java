package com.limn.tool.parser;

import java.util.TreeMap;

/**
 * Lexer类为词法解析器,用于为语法解析提供词文服务,将输入字符串拆分成词法解析可以处理的词法表示.
 * 其主要通过next函数提供服务,通过其它函数获得解析过程中的额外信息,比如词文类型等
 * 例如
 * <p><pre>
 * 1+1
 * 输出序列为
 * LexDef.T_CONST LexDef.T_ADD LexDef.T_CONST
 * </pre></p>
 * @author 王元和
 * @since YES1.0
 */
public class Lexer {
	/**
	 * 输入字符串,是需要被解析的内容
	 */
	private String content = null;
	/**
	 * 名解析器
	 */
	private INameResolver nameResolver = null;
	/**
	 * 对象解析器
	 */
	private IObjectResolver objectResolver = null;
	/**
	 * 缓冲的词文ID
	 */
	private String cacheLexID = null;
	/**
	 * 缓冲的词文
	 */
	private String cacheLexValue = null;
	/**
	 * 缓冲的词文类型,只在是常量和数值时有效
	 */
	private int cacheType = -1;
	/**
	 * 词法解析过程中的当前位置
	 */
	private int pos = 0;
	/**
	 * 缓冲的对象ID
	 */
	private int cacheObject = -1;
	/**
	 * 词法解析过程中错误原因,当前作为保留,未实现,用于兼容之后可能的实现
	 */
	private int errorID = -1;
	/**
	 * 输入字符串的总长度
	 */
	private int length = 0;
	/**
	 * 关键字映射
	 */
	private TreeMap<String, Integer> keyWordMap = null;
	/**
	 * 对象映射
	 */
	private TreeMap<String, Integer> objectMap = null;
	/**
	 * 上一次解析的词文数值表示
	 */
	private int cacheLastID = -1;

	/**
	 * 无输入的构造函数,之后必须通过setContent设置需要解析的字符串
	 */
	public Lexer() {
		init();
	}

	/**
	 * 通过输入字符串初始化词法解析器
	 * @param content 输入字符串
	 */
	public Lexer(String content) {
		this.content = content;
		length = content.length();
		init();
	}

	/**
	 * 设置输入字符串
	 * @param content 输入字符串
	 */
	public void setContent(String content) {
		this.content = content;
		length = content.length();
	}

	/**
	 * 设置名解析器
	 * @param resolver 名解析器,实现INameResolver接口
	 */
	public void setNameResolver(INameResolver resolver) {
		nameResolver = resolver;
	}
	
	/**
	 * 返回名解析器
	 * @return 名解析器
	 */
	public INameResolver getNameResolver() {
		return nameResolver;
	}
	
	/**
	 * 设置对象解析器
	 * @param resolver 对象解析器
	 */
	public void setObjectResolver(IObjectResolver resolver) {
		objectResolver = resolver;
	}
	
	/**
	 * 返回对象解析器
	 * @return 对象解析器
	 */
	public IObjectResolver getObjectResolver() {
		return objectResolver;
	}

	@SuppressWarnings("unused")
	private int lookforward(String lexID, String lexValue, int returnID,
			char forward, String forwardLexID, String forwardLexValue,
			int forwardID) {
		int id = -1;
		int oldPos = pos;
		char c = ' ';
		if (pos < length) {
			c = content.charAt(pos);
			++pos;
			
			int start = pos;
			int error = 0;
			RefInt refStart = new RefInt(pos);
			RefInt refError = new RefInt(0);
			RefBool blankFlag = new RefBool(false);
			RefBool commentsFlag = new RefBool(false);
	        
			do {
				c = skipBlank(c, refStart, refError, blankFlag);
				start = refStart.getValue();
				error = refError.getValue();
				if (error == -1) {
					return -1;
				}
				refStart.setValue(start);
				c = skipComments(c, refStart, commentsFlag);
				start = refStart.getValue();
				error = 0;
				refStart.setValue(start);
				c = skipBlank(c, refStart, refError, blankFlag);
				start = refStart.getValue();
				error = refError.getValue();
				if (error == -1)
		        {
		            return -1;
		        }
			} while (commentsFlag.getValue() || blankFlag.getValue());

			if (c == forward) {
				id = forwardID;
				cacheLexID = forwardLexID;
				cacheLexValue = forwardLexValue;
				return id;
			} else {
				pos = oldPos;
			}
		}
		id = returnID;
		cacheLexID = lexID;
		cacheLexValue = lexValue;
		return id;
	}

	@SuppressWarnings("unused")
	private int lookforwardSpec(String lexID, String lexValue, int returnID,
			String specForward) {
		int id = -1;
		int oldPos = pos;
		char c = ' ';
		int markIndex = -1;
		while (pos < length) {
			c = content.charAt(pos);
			++pos;
			if (!isIgnorable(c)) {
				if (c == specForward.charAt(markIndex + 1)) {
					++markIndex;

					// Match
					if (markIndex == specForward.length() - 1) {
						id = returnID;
						cacheLexID = lexID;
						cacheLexValue = lexValue;
						return returnID;
					}
				} else {
					pos = oldPos;
					return -1;
				}
			}
		}
		return -1;
	}

	/**
	 * 判断一个字符是否可略过
	 * @param c
	 * @return
	 */
	private boolean isIgnorable(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	/**
	 * 略过注释
	 * @param c 起始字符
	 * @param start 起始字符位置
	 * @param flag 是否存在注释的标志
	 * @return 返回注释之后的第一个字符
	 */
    private char skipComments(char c, RefInt start, RefBool flag) {
        char result = c;
        flag.setValue(false);
        if (pos < length)
        {
            int oldPos = pos;
            int oldStart = start.getValue();
            boolean found = false;
            if (c == '/')
            {
                if (pos < length)
                {
                    c = content.charAt(pos);
                    ++pos;
                    start.inc();
                    if (c == '/')
                    {
                        // Until next \n
                        if (pos < length)
                        {
                            c = content.charAt(pos);
                            ++pos;
                            start.inc();
                            while (c != '\n' && pos < length)
                            {
                                if (pos < length)
                                {
                                    c = content.charAt(pos);
                                    ++pos;
                                    start.inc();
                                }
                            }
                            // Forward to next
                            if (pos < length)
                            {
                                c = content.charAt(pos);
                                ++pos;
                                start.inc();
                            }
                            result = c;
                            found = true;
                        }
                    }
                    else if (c == '*')
                    {
                        // Until next * and /
                        if (pos < length)
                        {
                            c = content.charAt(pos);
                            ++pos;
                            start.inc();
                            while (c != '*' && pos < length)
                            {
                                if (pos < length)
                                {
                                    c = content.charAt(pos);
                                    ++pos;
                                    start.inc();
                                }
                            }
                            // Skip /
                            ++pos;
                            start.inc();
                            // Forward to next
                            if (pos < length)
                            {
                                c = content.charAt(pos);
                                ++pos;
                                start.inc();
                            }
                            result = c;
                            found = true;
                        }
                    }
                }
            }
            if (!found)
            {
                pos = oldPos;
                start.setValue(oldStart);
            } else {
            	flag.setValue(true);
            }
        }
        return result;
    }

    /**
     * 略过空白
     * @param c 起始字符串
     * @param start 开始位置
     * @param error 错误标记,如果超出输入字符串长度,则设置-1,表示从当前位置到结束都是空白
     * @param flag 如果存在空白,则设置为true,否则为false
     * @return 空白之后的第一个字符
     */
    private char skipBlank(char c, RefInt start, RefInt error, RefBool flag) {
        char result = c;
        flag.setValue(false);
        while (isIgnorable(c) && pos < length)
        {
        	flag.setValue(true);
            if (pos < length)
            {
                c = content.charAt(pos);
            }
            else
            {
                error.setValue(-1);
                return result;
            }
            result = c;
            ++pos;
            start.inc();
        }
        return result;
    }

    /**
     * 判断在什么符号后面-表示负数
     * @return 如果后续将-解释成负号,那么返回true,否则返回false
     */
	private boolean checkLastID4Negtive() {
		if (cacheLastID == -1 || (cacheLastID >= LexDef.T_ADD && cacheLastID <= LexDef.T_LESS_EQUAL)
				|| cacheLastID == LexDef.T_LB || cacheLastID == LexDef.T_COMMA) {
			return true;
		}
		return false;
    }
    
	/**
	 * 返回下一个词文
	 * @return 返回词法的数值表示,参见LexDef中以T_开头的定义,如果已经解析完成,返回-1
	 */
	public int nextID() {
		errorID = 0;
		cacheObject = LexDef.SCOPE_SELF;
		int id = -1;
		// Temp variable,refer to current char
		char c;
		// Current valid position
		int start = pos;

		// If over the end of string,return -1
		if (pos >= length)
			return -1;

		int error = 0;
		// Skip all backspace and tab
		RefInt refStart = new RefInt(start);
		RefInt refError = new RefInt(error);
		c = content.charAt(pos);
        ++pos;
        
        RefBool blankFlag = new RefBool(false);
        RefBool commentsFlag = new RefBool(false);
        
		do {
			c = skipBlank(c, refStart, refError, blankFlag);
			start = refStart.getValue();
			error = refError.getValue();
			if (error == -1) {
				return -1;
			}
			refStart.setValue(start);
			c = skipComments(c, refStart, commentsFlag);
			start = refStart.getValue();
			error = 0;
			refStart.setValue(start);
			c = skipBlank(c, refStart, refError, blankFlag);
			start = refStart.getValue();
			error = refError.getValue();
			if (error == -1)
	        {
	            return -1;
	        }
		} while (commentsFlag.getValue() || blankFlag.getValue());
        

		// Valid lex length
		int cnt = 1;
		switch (c) {
		case '+':
			id = LexDef.T_ADD;
			cacheLexID = "+";
			cacheLexValue = "+";
			break;
		case '-':
			if ( checkLastID4Negtive() ) {
				char tmpC = ' ';
	            if (pos < length)
	            {
	                tmpC = content.charAt(pos);
	            }
	            if (Character.isDigit(tmpC))
	            {
	                boolean isInt = true;
	                if (pos < length)
	                {
	                    c = content.charAt(pos);
	                }
	                while (Character.isDigit(c) || c == '.')
	                {
	                    if (c == '.')
	                        isInt = false;
	                    ++cnt;
	                    ++pos;
	                    if (pos >= length)
	                    {
	                        break;
	                    }
	                    c = content.charAt(pos);
	                }

	                id = LexDef.T_CONST;
	                if (isInt)
	                    cacheType = LexDef.L_TYPE_INT;
	                else
	                    cacheType = LexDef.L_TYPE_NUMBER;
	                cacheLexID = "const";
	                cacheLexValue = content.substring(start, start + cnt);
	            }
	            else
	            {
	                id = LexDef.T_SUB;
	                cacheLexID = "-";
	                cacheLexValue = "-";
	            }
			}
			else
            {
                id = LexDef.T_SUB;
                cacheLexID = "-";
                cacheLexValue = "-";
            }
			
			break;
		case '*':
			id = LexDef.T_MUL;
			cacheLexID = "*";
			cacheLexValue = "*";
			break;
		case '/':
			id = LexDef.T_DIV;
			cacheLexID = "/";
			cacheLexValue = "/";
			break;
		case '(':
			id = LexDef.T_LB;
			cacheLexID = "(";
			cacheLexValue = "(";
			break;
		case '{':
			id = LexDef.T_L_BRACE;
			cacheLexID = "{";
			cacheLexValue = "{";
			break;
		case ')':
			id = LexDef.T_RB;
			cacheLexID = ")";
			cacheLexValue = ")";
			break;
		case '}':
			id = LexDef.T_R_BRACE;
			cacheLexID = "}";
			cacheLexValue = "}";
			break;
		case ',':
			id = LexDef.T_COMMA;
			cacheLexID = ",";
			cacheLexValue = ",";
			break;
		case ';':
			id = LexDef.T_SEMICOLON;
			cacheLexID = ";";
			cacheLexValue = ";";
			break;
		case '!':
			id = LexDef.T_NOT;
			cacheLexID = "!";
			cacheLexValue = "!";
		case '=':
			if ( pos < length ) {
				c = content.charAt(pos);
				if ( c == '=' ) {
					id = LexDef.T_EQUAL;
					cacheLexID = "==";
					cacheLexValue = "==";
					++pos;
				} else {
					id = LexDef.T_ASSIGN;
					cacheLexID = "=";
					cacheLexValue = "=";
				}
			} else {
				id = LexDef.T_ASSIGN;
				cacheLexID = "=";
				cacheLexValue = "=";
			}
			break;
		case '&':
			if ( pos < length ) {
				c = content.charAt(pos);
				if ( c == '&' ) {
					id = LexDef.T_AND;
					cacheLexID = "&&";
					cacheLexValue = "&&";
				} else {
					id = LexDef.T_STRCAT;
					cacheLexID = "&";
					cacheLexValue = "&";
				}
			} else {
				id = LexDef.T_STRCAT;
				cacheLexID = "&";
				cacheLexValue = "&";
			}
			break;
		case '|':
			id = -1;
			if ( pos < length ) {
				c = content.charAt(pos);
				if ( c == '|' ) {
					id = LexDef.T_OR;
					cacheLexID = "||";
					cacheLexValue = "||";
				}
			}
			break;
		case '>':
			if (pos < length) {
				c = content.charAt(pos);
				if (c == '=') {
					id = LexDef.T_LARGE_EQUAL;
					cacheLexID = ">=";
					cacheLexValue = ">=";
					++pos;
				} else {
					id = LexDef.T_LARGE;
					cacheLexID = ">";
					cacheLexValue = ">";
				}
			} else {
				id = LexDef.T_LARGE;
				cacheLexID = ">";
				cacheLexValue = ">";
			}
			break;
		case '<':
			if (pos < length) {
				c = content.charAt(pos);
				if (c == '=') {
					id = LexDef.T_LESS_EQUAL;
					cacheLexID = "<=";
					cacheLexValue = "<=";
					++pos;
				} else if (c == '>') {
					id = LexDef.T_NEQUAL;
					cacheLexID = "<>";
					cacheLexValue = "<>";
					++pos;
				} else {
					id = LexDef.T_LESS;
					cacheLexID = "<";
					cacheLexValue = "<";
				}
			} else {
				id = LexDef.T_LESS;
				cacheLexID = "<";
				cacheLexValue = "<";
			}
			break;
		case '$':
			id = LexDef.T_DOLLAR;
			cacheLexID = "$";
			cacheLexValue = "$";
			break;
		case '\'':
			do {
				if (pos < length) {
					c = content.charAt(pos);
					++pos;
					++cnt;
				} else {
					errorID = 1;
					return -1;
				}
			} while (c != '\'');

			id = LexDef.T_CONST;
			cacheLexID = "const";
			cacheType = LexDef.L_TYPE_STRING;
			cacheLexValue = content.substring(start + 1, start + cnt - 1);
			break;
		case '"':
			do {
				if (pos < length) {
					c = content.charAt(pos);
					++pos;
					++cnt;
				} else {
					errorID = 1;
					return -1;
				}
			} while (c != '"');

			id = LexDef.T_CONST;
			cacheType = LexDef.L_TYPE_STRING;
			cacheLexID = "const";
			cacheLexValue = content.substring(start + 1, start + cnt - 1);
			break;
		case '[': {
			int matchcnt = 0;
			while (matchcnt != 2) {
				if (pos < length) {
					c = content.charAt(pos);
				} else {
					errorID = 1;
					return -1;
				}
				++pos;
				++cnt;
				if (c == ']')
					++matchcnt;
			}
			id = LexDef.T_ID;
			cacheLexID = "id";
			cacheLexValue = content.substring(start, start + cnt);
		}
			break;
		default:
			if (c == '@') {
				if (pos < length) {
					c = content.charAt(pos);
				}

				while (Character.isLetter(c)) {
					++cnt;
					++pos;
					if (pos >= length) {
						break;
					}
					c = content.charAt(pos);
				}

				id = LexDef.T_ID;
				cacheLexID = "id";
				cacheLexValue = content.substring(start, start + cnt);
			} else if (Character.isLetter(c)) {
				if (pos < length) {
					c = content.charAt(pos);
					while (Character.isLetterOrDigit(c) || c == '.' || c == '['
							|| c == ']' || c == '_') {
						++cnt;
						++pos;
						if (pos >= length) {
							break;
						}
						c = content.charAt(pos);
					}
				}

				id = LexDef.T_FUN;
				cacheLexID = "fun";
				cacheLexValue = content.substring(start, start + cnt);
				int nID = -1;
				// find const value
				nID = resolveConst(cacheLexValue);
				if (nID != -1) {
					id = nID;
					cacheLexID = "const";
				} else {
					// find key word or function
					nID = resolveID(cacheLexValue);
					if (nID != -1) {
						id = nID;
						if (nID != LexDef.T_FUN) {
							cacheLexID = cacheLexValue;
						}
					} else {
						id = LexDef.T_ID;
						cacheLexID = "id";
						cacheLexValue = content.substring(start, start + cnt);
					}
				}
			} else if (Character.isDigit(c)) {
				boolean isInt = true;
				if (pos < length) {
					c = content.charAt(pos);
					while (Character.isDigit(c) || c == '.') {
						if (c == '.')
							isInt = false;
						++cnt;
						++pos;
						if (pos >= length) {
							break;
						}
						c = content.charAt(pos);
					}
				}

				id = LexDef.T_CONST;
				if (isInt) {
					cacheType = LexDef.L_TYPE_INT;
				} else {
					cacheType = LexDef.L_TYPE_NUMBER;
				}
				cacheLexID = "const";
				cacheLexValue = content.substring(start, start + cnt);
			}
			break;
		}

		cacheLastID = id;
		return id;
	}

	/**
	 * 返回词法符号,比如解析出逗号,则返回","
	 * @return 词法符号
	 */
	public String getLexID() {
		return cacheLexID;
	}

	/**
	 * 返回词文,比如解析出函数,则返回函数的名称,例如"setValue".
	 * @return 词文
	 */
	public String getLexValue() {
		return cacheLexValue;
	}

	/**
	 * 返回词文表示的数值类型,比如10返回整型,10.0返回数值类型.
	 * @return 数值类型
	 */
	public int getType() {
		return cacheType;
	}

	/**
	 * 返回出错原因,当前未定义,仅用于处理后续兼容
	 * @return
	 */
	public int getErrorID() {
		return errorID;
	}

	/**
	 * 返回对象标识
	 * @return 对象标识
	 */
	public int getObject() {
		return cacheObject;
	}

	/**
	 * 初始系统的保留字列表及保留对象列表
	 */
	protected void init() {
		keyWordMap = new TreeMap<String, Integer>();
		keyWordMap.put("if", LexDef.T_IF);
		keyWordMap.put("else", LexDef.T_ELSE);
		keyWordMap.put("while", LexDef.T_WHILE);
		keyWordMap.put("var", LexDef.T_VAR);
		
		objectMap = new TreeMap<String, Integer>();
		objectMap.put("self", LexDef.SCOPE_SELF);
		objectMap.put("parent", LexDef.SCOPE_PARENT);
	}

	/**
	 * 用于解析一个词文符号的类型
	 * @param token 词文符号
	 * @return 如果是一个标识符,那么返回T_ID,如果是一个函数返回T_FUN,如果是保留字,返回保留字列表中的相应值,
	 * 如果是对象标识,返回对象标识列表中的相应值
	 */
	protected int resolveID(String token) {
		int index = token.indexOf('.');

		if (index > 0) {
			String firstPart = token.substring(0, index);
			String secondPart = token.substring(index + 1);
			if (objectMap.containsKey(firstPart)) {
				cacheObject = objectMap.get(firstPart);
				token = secondPart;
				cacheLexValue = secondPart;
			} else {
				int object = objectResolver.resolveObject(firstPart);
				if ( object != -1 ) {
					token = secondPart;
					cacheObject = object;
					cacheLexValue = secondPart;
				}
			}
		}else{
			cacheObject = LexDef.SCOPE_SELF;
			cacheLexValue = token;
		}

		if (nameResolver != null) {
			if (nameResolver.resolveFunction(token)) {
				return LexDef.T_FUN;
			}
		}

		Object id = keyWordMap.get(token);
		if (id != null) {
			return (Integer) id;
		}
		return -1;
	}

	/**
	 * 解析一个词文是否是一个常量
	 * @param token 词文符号
	 * @return 如果词文是一个常量,那么返回true,否则返回false,当前词文常量只有true和false.
	 */
	protected int resolveConst(String token) {
		if (token.equalsIgnoreCase("true")) {
			cacheLexValue = "true";
			cacheType = LexDef.L_TYPE_BOOL;
			return LexDef.T_CONST;
		} else if (token.equalsIgnoreCase("false")) {
			cacheLexValue = "false";
			cacheType = LexDef.L_TYPE_BOOL;
			return LexDef.T_CONST;
		}
		return -1;
	}
}
