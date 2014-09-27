package proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import monitor.Middleware;
/**
 * 所有的客户端的连接
 * @author limn
 *
 */
public class Listener implements Runnable {
	
	private ServerSocket server = null;
	
	//开启监听
	Listener(int port) {
		try {
			server = new ServerSocket(port);
			Middleware.appServerLog("开启服务器进程: " + InetAddress.getLocalHost());
			server.getInetAddress();
			Middleware.setServerInfo("IP地址: " + InetAddress.getLocalHost().getHostAddress() + 
					":" + server.getLocalPort());
			Middleware.appServerLog("端口:" + server.getLocalPort());
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		
		while(true){
			Middleware.appServerLog("监听...");
			Socket sc = null;
			try {
				sc = server.accept();
				Middleware.appServerLog("连接客户端: " + sc.getInetAddress().toString());
				//启动这个客户端的连接内容
				new Thread(new ProcessService(sc)).start();
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			
		}
	}
	

}
