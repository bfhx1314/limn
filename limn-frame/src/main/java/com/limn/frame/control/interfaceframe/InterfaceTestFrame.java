package com.limn.frame.control.interfaceframe;

import java.awt.Component;

import javax.swing.JFrame;

public class InterfaceTestFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterfaceTestFrame(){
		super("接口测试");
		

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width);
		setBounds((int) ((screenWidth - 1000) *0.5), (int) ((screenHeight - 600) *0.5), 1000, 600);
		
		
		setLayout(null);
		addComponent(new InterfaceTestPanel(),0,0,1000,600);
		
//		setAlwaysOnTop(true);
		setResizable(false);
		validate();
		setVisible(true);

	}
	
	private void addComponent(Component comp, int x, int y, int width, int height){
		this.add(comp);
		comp.setBounds(x, y, width, height);
	}
	
	public static void main(String[] args){
		new InterfaceTestFrame();
	}	
}
