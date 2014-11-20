package com.limn.client.remote;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.limn.tool.log.LogDocument;


/**
 * 远程运行的客户端  Grid
 * 
 * @author limn
 */
public class RemoteClient extends JFrame implements Runnable{
	
	

	private static final long serialVersionUID = 1L;

	private static JTextArea textArea = new JTextArea();
	private static JScrollPane logJScrollLog = new JScrollPane(textArea,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private RemoteListener rl = null;
	
	public RemoteClient(){
		super("Remote Clinet");
		
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width);
		setBounds((int) ((screenWidth - 400) * 0.99), (int) ((screenHeight - 300) * 0.95), 400, 300);
		
		textArea.setDocument(new LogDocument(textArea));
		
		
		textArea.setEditable(false);

		add(logJScrollLog);

		setResizable(false);

		validate();
		setVisible(true);
		
		setDefaultCloseOperation(RemoteClient.EXIT_ON_CLOSE);
		

		
	}
	
	public static void setText(String log){
		textArea.append(log + "\n");
	}
	
	
	public static void main(String[] args){
		new RemoteClient();
	}

	@Override
	public void run() {
		//监听请求
		rl = new RemoteListener();
		try {
			rl.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
