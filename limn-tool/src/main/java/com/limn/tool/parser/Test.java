package com.limn.tool.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

public class Test {
	public static void main(String[] args) {
//		testRule();
//		testParser("a + iif(b>1,2,3)");
//		String[] vScript = new String[] {
//				"a + iif(b>1,2,3)",
//				"a+b",
//				"a+b+",
//				"if(b>1) { 1 } else { 2 }",
//				"while(b>1) { 1 }",
//				"if ( a > 1 ) { if ( c > 2 ) { 1 } } else { 2 }"
//		};
//		testMultiParser(vScript, 5);
		SyntaxTree tree = new SyntaxTree();
		Parser parser = new Parser();
		Object result = null;
		try {
			String[] vScript = new String[] {
					"1+1*(8*3.0)",
					"left('abc',1)",
					"toint(left('123',2))+3",
					"iif(1<2,2+2,3+3)"
			};
			result = parser.eval(null, vScript[3], tree, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("result = " + result);
	}
	
	private static void testMultiParser(String[] vScript, int... indexes) {
		String script = null;
		int length = indexes.length;
		for ( int i = 0; i<length; ++i ) {
			script = vScript[indexes[i]];
			testParser(script);
		}
	}
	
	private static void testRule() {
		PredefinedParserRules rules = PredefinedParserRules.getInstance();
		int ruleCount = rules.getRuleCount();
		ParserRule rule = null;
		for ( int i = 0; i<ruleCount; ++i ) {
			rule = rules.getRuleAt(i);
			printRuleDebug(rules, rule);
		}
	}
	
	private static void printRuleDebug(PredefinedParserRules rules, ParserRule rule) {
		HashSet<Integer> preTokenSet = rule.getDirectPreToken();
		String s = "直接前导终结符";
		Iterator<Integer> it = preTokenSet.iterator();
		Integer preToken = null;
		while ( it.hasNext() ) {
			preToken = it.next();
			s += rules.getFactorName(preToken) + " ";
		}
		// System.out.println(rule.getStatement() + " " + s);
	}
	
	private static void testParser(String script) {
		SyntaxTree syntaxTree = new SyntaxTree();
		Parser parser = new Parser();
		try {
			parser.parse(script, syntaxTree);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	private static void testLexer() {
		File f = new File("D:\\Temp\\Script.txt");
		String exp = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			FileInputStream in = new FileInputStream(f);
			byte[] bs = new byte[1024];
			int len = in.read(bs);
			while ( len > 0 ) {
				out.write(bs, 0, len);
				len = in.read(bs);
			}
			exp = out.toString();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Lexer lex = new Lexer(exp);
		int tokenID = lex.nextID();
		while ( tokenID != -1 ) {
			String lexID = lex.getLexID();
			//System.out.println(tokenID + "," + lexID);
			tokenID = lex.nextID();
		}
	}
}
