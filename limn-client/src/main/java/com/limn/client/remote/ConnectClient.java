package com.limn.client.remote;


//import java.io.DataInputStream; 
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.CallBat;
import com.limn.tool.common.Common;




public class ConnectClient implements Runnable {


	private DataOutputStream dos = null;
//	private DataInputStream dis = null;
	private Socket sc = null;
	
	private int port = 0;
	
	private String code = null;
	
	private boolean flag = true;
	
	public ConnectClient(Socket sc,int port){
		this.sc = sc;
		this.port = port;
	}
	/**
	 * 连接的维护.如果断了就关闭
	 */
	@Override
	public void run() {
		startNode(sc.getInetAddress().getHostAddress());
		while(flag){
			
			try {
				sc.sendUrgentData(0xFF);
			} catch (IOException e1) {
				flag = false;
			}

			Common.wait(10000);
		}
		
		RemoteClient.setText("关闭对应Node:" + code);
		CallBat.closeProcessByTitle(code);
	}
	
	/**
	 * 开启Node的进程  根据IP
	 * @param ip
	 */
	private void startNode(String ip){
		code = "RemoteClinet:" + port;
		CallBat.returnExecByNewWindows(code ," java -jar " + Parameter.DEFAULT_BIN_PATH +"/selenium-server-standalone.jar -role node http://"
				+ ip + ":4444/grid/register -hubHost "+ ip + " -port " + port);
		try {
			Thread.sleep(1000);
			dos = new DataOutputStream(sc.getOutputStream());
			//发送开启的端口给服务器
			dos.writeUTF(String.valueOf(port));
			
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
		RemoteClient.setText("开启Node端口: " + port);
		
	}

}
