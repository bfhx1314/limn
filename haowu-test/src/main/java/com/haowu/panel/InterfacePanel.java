package com.haowu.panel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.limn.frame.panel.CustomPanel;

public class InterfacePanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InterfacePanel(){
		
		JLabel assistantUsernameLable = new JLabel("助理账号:");
		JTextField assistantUsername = new JTextField();
		
		JLabel assistantPasswordLable = new JLabel("助理密码:");
		JTextField assistantPassword = new JTextField();
		
		JLabel clientUserNameLable = new JLabel("客户账号:");
		JTextField clientUserName = new JTextField();
		
		JLabel clientPasswordLable = new JLabel("客户密码:");
		JTextField clientPassword = new JTextField();
		
		JLabel houseIDLable = new JLabel("楼盘ID:");
		JTextField houseID = new JTextField();
		
		JLabel houseTypeLable = new JLabel("产品类型:");
		JTextField houseType = new JTextField();
		
		JLabel determinedLable = new JLabel("下定金额:");
		JTextField determined = new JTextField();
		
		
	}
	
	
	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}
	
	

}
