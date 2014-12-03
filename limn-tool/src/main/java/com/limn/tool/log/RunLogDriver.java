package com.limn.tool.log;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.limn.tool.common.Print;

import java.awt.Color;
import java.awt.Dimension;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 默认的日志界面
 * @author limn
 *
 */
public class RunLogDriver extends PrintLogDriver implements LogControlInterface {

	//主panel
	private JPanel panel = new JPanel();

	//底部panel
	private JPanel southPanel = new JPanel();

	//通信状态显示的Label
	private JLabel socketConnect = new JLabel();
	
	//测试用例步骤界面
	public JTextPane writeStepPane = new JTextPane();
	public JScrollPane logJScrollStep = new JScrollPane(writeStepPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	public JFrame jFrame = new JFrame("RunLog");
	
	private boolean isStart = false;
	/**
	 * 默认的日志界面
	 * 连接服务器RunLog
	 * @param socket
	 */
	public RunLogDriver(Socket socket){
		init();
		setSocket(socket);
	}
	
	/**
	 * 默认的日志界面
	 */
	public RunLogDriver() {

		init();
	}
	
	public RunLogDriver(String title){

		init();		
	}
	
	private void init(){
		isStart = true;
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);

		jFrame.setBounds(0, (int) ((screenHeight - 400) * 0.95), 700, 400);
		// 初始化两个Log的大小
		writeLogPane.setPreferredSize(new Dimension(290, 234));
		writeLogPane.setEditable(false);
		writeStepPane.setPreferredSize(new Dimension(244, 234));
		writeStepPane.setEditable(false);
		writeLogPane.setDocument(new LogDocument(writeLogPane, 400));


		// 增加进度条
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);

		southPanel.setLayout(null);
		
		socketConnect.setBounds(10, 5, 200, 30);
		
		progressBar.setBounds(300, 5, 200, 30);

		southPanel.add(socketConnect);	
		southPanel.add(progressBar);


		// 整体的布局
		panel.setLayout(null);
		logJScrollLog.setBounds(0, 0, 380, 330);
		logJScrollStep.setBounds(380,0, 315, 330);
		southPanel.setBounds(0, 332, 700, 40);
		

		panel.add(logJScrollLog);
		panel.add(logJScrollStep);
		panel.add(southPanel);
		
		jFrame.add(panel);

		jFrame.setAlwaysOnTop(true);
		jFrame.setResizable(false);


		jFrame.validate();
		jFrame.setVisible(true);
	}
	

	/**
	 * 初始化
	 * @param sumStep 总计的步骤数
	 */
	public void progressLength(int sumStep) {
		progressBar.setMaximum(sumStep);
		currentStepIndex = 1;
		stepCount = sumStep;
	}

	
	/**
	 * 测试用例的Step
	 * @param currentStepNum 第几个Step
	 * @param currentStep 步骤的描述
	 * 
	 */
	public void setStepsForTextAreaByIndex(int currentStepNum, String[] currentStep ,String testCaseNo) {
		int eStepNum;
		if (currentStepIndex <= currentStepNum) {
			eStepNum = currentStepNum + currentStepIndex;
		} else {
			eStepNum = currentStepNum;
		}

		everySteplen = new int[currentStep.length + 1];
		everyDescription = new int[currentStep.length + 1];
		String firstDescription = "当前行: " + currentStepNum + " 总计: "
				+ stepCount + " 编号:" + testCaseNo + "\n";
		
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
		
		setStepForTextArea(allStep,eStepNum);


	}
	
	public void appStepForTextArea(String strngStep){
		Document doc = writeStepPane.getDocument();
		try {
			doc.insertString(doc.getLength(), strngStep + "\n", setDocs(Color.BLACK, false));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * runStep的界面输入
	 * @param strngStep
	 * @param currentStep
	 */
	private void setStepForTextArea(String strngStep, int currentStep) {
		writeStepPane.setText(strngStep);
		progressBar.setValue(currentStep);
	}

	
	/**
	 * runStep的界面修改
	 * @param pos 字符数的起始位置
	 * @param currentL 修改的字符串的长度
	 * @param style 样式: 1 绿色 2红色 3黄色 4加粗 其他 还原
	 */
	private void modifyStyle(int pos, int currentL, int style) {
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
			modifyFontStyle(setDocs(Color.green.darker(), false));
			break;
		// 有问题的步骤样式
		case 2:
			modifyFontStyle(setDocs(Color.red, false));
			break;
		// 可能有问题的步骤样式
		case 3:
			modifyFontStyle(setDocs(Color.yellow.darker(), false));
			break;
		// 当前正在执行的步骤样式
		case 4:
			modifyFontStyle(setDocs(Color.black, true));
			break;
		// 其他 无样式变化
		default:
			modifyFontStyle(setDocs(Color.black, false));
			break;
		}
	}

	/**
	 * 修改原来的字符的样式
	 * @param attrSet
	 */
	private void modifyFontStyle(AttributeSet attrSet) {
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
	
	
	/**
	 * 界面上高亮当前的步骤
	 * @param currentNum  index 从1开始
	 */
	public void highLightCurrentStep(int currentNum){
		if(currentNum>0){
			modifyStyle(everySteplen[currentNum-1],everyDescription[currentNum],1);
		}
		modifyStyle(everySteplen[currentNum],everyDescription[currentNum+1],4);
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
		runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date())+"-->",log + "\n\r",style);
		uploadLog(log);
	}
	
	
	public void printLocalLog(String log,int style){
		runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()) + "-->",log + "\n\r",style);
	}

	public boolean isStart() {
		return isStart;
	}
	
}
