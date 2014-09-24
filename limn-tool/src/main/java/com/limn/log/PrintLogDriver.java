package com.limn.log;

import java.awt.Color;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;




public class PrintLogDriver extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JTextPane writeLogPane = new JTextPane();
	public static JScrollPane logJScrollLog = new JScrollPane(writeLogPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	public static JTextPane writeStepPane = new JTextPane();
	public static JScrollPane logJScrollStep = new JScrollPane(writeStepPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	public static JProgressBar current;
	
	// 修改文字的起始位子
	public static int LastScroll = -1;
	public static int currentLength;
	
	public static int executedStepNum = 0;
	public static int sumStepNum = 0;
	
	public static int[] everySteplen = null;
	public static int[] everyDescription = null;
	
	public static boolean isStart = false;
	public PrintLogDriver(String title){
		super(title);
		isStart = true;
	}
	
	public PrintLogDriver(){
		isStart = true;
	}
	
	public PrintLogDriver(Socket sc){
		isStart = true;
	}
	
	
	public void init(int count){
		
	}
	
	/**
	 * 
	 * @param strng
	 * @param strngDescrption
	 * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black 
	 */
	public void runLogWrite(String strng, String strngDescrption, int style) {

		SimpleAttributeSet attrSetRunLog;
		switch (style) {
		case 1:
			attrSetRunLog = setDocs(Color.green.darker(), false);
			break;
		case 2:
			attrSetRunLog = setDocs(Color.red, false);
			break;
		case 3:
			attrSetRunLog = setDocs(Color.yellow.darker(), false);
			break;
		case 4:
			attrSetRunLog = setDocs(Color.black, true);
			break;
		default:
			attrSetRunLog = setDocs(Color.black, false);
			break;
		}
		try {
			writeLogPane.getDocument().insertString(
					writeLogPane.getDocument().getLength(), strng + strngDescrption,
					attrSetRunLog);
		} catch (BadLocationException e) {
		
			e.printStackTrace();
		}
		LogInformation.appLog(strng + strngDescrption);
	}

	
	// runStep的界面输入
	public static void runStepWrite(String strngStep, int currentStep) {
		writeStepPane.setText(strngStep);
		current.setValue(currentStep);
	}

	// runStep的界面修改 pos字符数的起始位置,currentL修改的字符串的长度
	public void runStepInsert(int pos, int currentL, int style) {
		LastScroll = pos;
		currentLength = currentL;
		if(writeStepPane.getText().length()<pos+currentL){
			
		}else{
			writeStepPane.setCaretPosition(pos + currentL);
		}

		switch (style) {
		// 没有问题的步骤样式
		case 1:
			// setDocs(Color.green.darker(),false);
			insert(setDocs(Color.green.darker(), false));
			break;
		// 有问题的步骤样式
		case 2:
			insert(setDocs(Color.red, false));
			break;
		// 可能有问题的步骤样式
		case 3:
			insert(setDocs(Color.yellow.darker(), false));
			break;
		// 当前正在执行的步骤样式
		case 4:
			insert(setDocs(Color.black, true));
			break;
		// 其他 无样式变化
		default:
			insert(setDocs(Color.black, false));
			break;
		}
	}

	// 修改原来的字符的样式
	public void insert(AttributeSet attrSet) {
		Document doc = writeStepPane.getDocument();
		try {
			String strng = writeStepPane.getDocument().getText(LastScroll,
					currentLength);
			doc.remove(LastScroll, currentLength);
			doc.insertString(LastScroll, strng, attrSet);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException:   " + e);
		}
	}
	
	public static boolean isStart(){
		return isStart;
	}

	// 样式的设置
	public SimpleAttributeSet setDocs(Color col, boolean bold) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// 颜色
		StyleConstants.setForeground(attrSet, col);
		// 字体是否加粗
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}
		// 字体大小
		// StyleConstants.setFontSize(attrSet,fontSize);
		return attrSet;
		// insert(writeStepPane,attrSet);
	}





	
	/**
	 * 测试用例的Step
	 * @param currentStepNum 第几个Step
	 * @param currentStep 步骤的描述
	 */
	public void runStep(int currentStepNum, String[] currentStep) {
		int eStepNum;
		if (executedStepNum <= currentStepNum) {
			eStepNum = currentStepNum + executedStepNum;
		} else {
			eStepNum = currentStepNum;
		}

		everySteplen = new int[currentStep.length + 1];
		everyDescription = new int[currentStep.length + 1];
		String firstDescription = "当前用例行: " + currentStepNum + " 总计用例行: "
				+ sumStepNum + "\n";
		
		everySteplen[0] = firstDescription.length();
		everyDescription[0] = firstDescription.length();
		
		String allStep = firstDescription;
		int everyStep = 0;
		for (String step:currentStep){
			step = everyStep + "." + step + "\n";
			everyStep++;
			
			everyDescription[everyStep] = step.length();
			
			everySteplen[everyStep] = everySteplen[everyStep-1] + everyDescription[everyStep];

			allStep = allStep + step;
			
		}
		
		runStepWrite(allStep,eStepNum);


	}
	
	/**
	 * 界面上高亮当前的步骤
	 * @param currentNum  index 从1开始
	 */
	public void runStepInsert(int currentNum){
		if(currentNum>0){
			runStepInsert(everySteplen[currentNum-1],everyDescription[currentNum],1);
		}
		runStepInsert(everySteplen[currentNum],everyDescription[currentNum+1],4);
	}


	
	
	/**
	 * 调试信息
	 */
	public void debug(String log){
		printLog(log,4);
	}
	
	public void info(String log,int style){
		printLog(log,style);
	}
	
	public void error(String log){
		printLog(log,2);
	}
	
	
	public void printLog(String log,int style){
	}
	
	
	public void printLocalLog(String log,int style){
		
	}

	
	
}
