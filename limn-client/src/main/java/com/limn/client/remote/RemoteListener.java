package com.limn.client.remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.limn.tool.parameter.Parameter;



public class RemoteListener{

	private ServerSocket socketServer = null;
	
	private Socket sc = null;
	
	private int port = 5556;
	
	public RemoteListener(){
		try {
			socketServer = new ServerSocket(Parameter.REMOTEPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void accept() throws IOException{
		while(true){
			RemoteClient.setText("监听已开启");
			sc = socketServer.accept();
			RemoteClient.setText("连接客户端: " + sc.getInetAddress().getHostAddress());
			//客户端的连接维护.
			new Thread(new ConnectClient(sc,port)).start();
			port ++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	
}
