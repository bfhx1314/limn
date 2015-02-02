package com.limn.tool.log;



import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;




public abstract class PrintLogDriver implements LogControlInterface{
		
	//日志面板
	public JTextPane writeLogPane = new JTextPane();
	public JScrollPane logJScrollLog = new JScrollPane(writeLogPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	//通信
	public Socket socketServer = null;

	//进度条
	public JProgressBar progressBar;
	
	// 修改文字的起始位子
	public int LastScroll = -1;
	public int currentLength;
	
	//当前执行的用例
	public int currentStepIndex = 0;
	//总计的步骤数
	public int stepCount = 0;
	
	public int[] everySteplen = null;
	public int[] everyDescription = null;
	
	//进程中是否已经存在
	private boolean isStart = false;
	
	//通信状态
	public  int socketStatus = 0;
	
	
	public PrintLogDriver(){
		init();
	}
	
	public PrintLogDriver(Socket sc){
		socketServer = sc;
		init();
	}
	
	private void init(){
		isStart = true;
	}
	
	/**
	 * 执行进度长度
	 * @param count 
	 */
	public void progressLength(int count){
		
	}
	
	
	/**
	 * 写入日志
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

	
	
	
	/**
	 * 返回是否已经启动
	 * @return
	 */
	public boolean isStart(){
		return isStart;
	}
	
	/**
	 * 样式的设置
	 * @param col
	 * @param bold
	 * @return
	 */
	public SimpleAttributeSet setDocs(Color col, boolean bold) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// 颜色
		StyleConstants.setForeground(attrSet, col);
		// 字体是否加粗
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}
		return attrSet;
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

	/**
	 * 设置服务端和客户端之间的通信
	 * @param socket
	 */
	public void setSocket(Socket socket){
		socketServer = socket;
		new Timer().scheduleAtFixedRate(new TimerSocket(), 0, 5000);
	}
	
	/**
	 * 更新服务器与客户端的通信
	 * @param log
	 */
	public void uploadLog(String log){
		if(socketServer!=null && !socketServer.isClosed()){
			try {
				new DataOutputStream(socketServer.getOutputStream()).writeUTF(log+"\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回通信状态
	 * @return
	 */
	public int getSocketStatus(){
		return socketStatus;
	}
	
	/**
	 * 通信的维护
	 * @author limn
	 */
	private class TimerSocket extends TimerTask {

		@Override
		public void run() {
			
			if(socketServer != null && !socketServer.isConnected()){
				try {
					socketServer = new Socket(socketServer.getInetAddress(), socketServer.getPort());
					socketStatus = SocketStatusType.CONNECT;
//					socketConnect.setBackground(Color.GREEN);
//					socketConnect.setForeground(Color.GREEN);
				} catch (IOException e) {
					socketStatus = SocketStatusType.BREAK;
//					socketConnect.setBackground(Color.LIGHT_GRAY);
//					socketConnect.setForeground(Color.LIGHT_GRAY);
					e.printStackTrace();
				}
			}else{
				socketStatus = SocketStatusType.BREAK;
//				socketConnect.setBackground(Color.GREEN);
//				socketConnect.setForeground(Color.GREEN);
			}
				
			
		}
	
	}
	
}
