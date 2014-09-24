package com.limn.log;

import javax.swing.*;


import javax.swing.JFrame;



import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class RunLogDriver extends PrintLogDriver{
	
//直接调用时 使用的变量
//	private static int executedStepNum = 0;
//	private static int sumStepNum = 0;
//	private static int[] everySteplen = null;
//	private static int[] everyDescription = null;
	
	private static Socket socketServer = null;
	private static JLabel socketConnect = new JLabel();
	
//	private static JTextPane writeLogPane = new JTextPane();
//	private static JTextPane writeStepPane = new JTextPane();
//	private static JScrollPane logJScrollLog = new JScrollPane(writeLogPane,
//			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//	private static JScrollPane logJScrollStep = new JScrollPane(writeStepPane,
//			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	private static JPanel panel = new JPanel();

	private static JPanel southPanel = new JPanel();


	
	
	
//	private static JProgressBar current;



	JLabel sheetLabel = new JLabel("Excel Sheet");

	// 修改文字的起始位子
//	private static int LastScroll = -1;

	// 当前内容的的字符数
//	private static int currentLength;
	private static DataOutputStream dos = null;
	private Timer timer = new Timer(true);
	
	
	
	private static boolean isStart = false;
	
	/**
	 * 连接服务器RunLog
	 * @param socket
	 */
	public RunLogDriver(Socket socket){
		new RunLog();
		setSocket(socket);
	}
	
	/**
	 * 
	 */
	public RunLogDriver() {
		super("RunLog");
		isStart = true;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);

		setBounds(0, (int) ((screenHeight - 400) * 0.95), 700, 400);
		// 初始化两个Log的大小
		writeLogPane.setPreferredSize(new Dimension(290, 234));
		writeLogPane.setEditable(false);
		writeStepPane.setPreferredSize(new Dimension(244, 234));
		writeStepPane.setEditable(false);
		writeLogPane.setDocument(new LogDocument(writeLogPane, 400));


		// 增加进度条
		current = new JProgressBar();
		current.setStringPainted(true);
		// 进度条和按钮放在一块
		// FlowLayout lm = new FlowLayout(FlowLayout.RIGHT);

		southPanel.setLayout(null);
		socketConnect.setBounds(10, 5, 200, 30);
		current.setBounds(300, 5, 200, 30);

		southPanel.add(socketConnect);
		southPanel.add(current);



		// 整体的布局
		panel.setLayout(null);
		logJScrollLog.setBounds(0, 0, 380, 330);
		logJScrollStep.setBounds(380,0, 315, 330);
		southPanel.setBounds(0, 332, 700, 40);
		
		
		
		
		panel.add(logJScrollLog);
		panel.add(logJScrollStep);
		panel.add(southPanel);
		
		add(panel);

		
		setAlwaysOnTop(true);
		setResizable(false);


		validate();
		setVisible(true);
	}
	



	private void setSocket(Socket socket){
		RunLogDriver.socketServer = socket;
		socketConnect.setText(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		timer.schedule(new TimerSocket(),0,500);
	}
	

	
	public static boolean isStart(){
		return isStart;
	}


	// 进度条的最大值
	private static void runCurrentSchedule(int maxStep) {
		current.setMaximum(maxStep);

	}


	/**
	 * 初始化
	 * @param sumStep 总计的步骤数
	 */
	public void init(int sumStep) {
		runCurrentSchedule(sumStep);
		executedStepNum = 1;
		sumStepNum = sumStep;
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
		runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date())+"-->",log + "\n\r",style);
		uploadLog(log);
	}
	
	
	public void printLocalLog(String log,int style){
		runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()) + "-->",log + "\n\r",style);
	}
	
	private static void uploadLog(String log){
		if(socketServer!=null && !socketServer.isClosed()){
			try {
				dos = new DataOutputStream(socketServer.getOutputStream());
				dos.writeUTF(log+"\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class TimerSocket extends TimerTask {

		@Override
		public void run() {
			
			if(socketServer != null && !socketServer.isConnected()){
				try {
					socketServer = new Socket(socketServer.getInetAddress(), socketServer.getPort());
//					socketConnect.setBackground(Color.GREEN);
					socketConnect.setForeground(Color.GREEN);
				} catch (IOException e) {
//					socketConnect.setBackground(Color.LIGHT_GRAY);
					socketConnect.setForeground(Color.LIGHT_GRAY);
					e.printStackTrace();
				}
			}else{
//				socketConnect.setBackground(Color.GREEN);
				socketConnect.setForeground(Color.GREEN);
			}
				
			
		}
	
	}

	
	public static void main(String ags[]) {
		new RunLog();
	}
	


}
