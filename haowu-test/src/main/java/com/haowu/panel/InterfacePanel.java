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
		
		int x = 25;
		int y = 15;
		int height = 20;
		int widthLable = 100;
		int width = 200;
		
		this.setLayout(null);
		setBoundsAt(assistantUsernameLable,x,y,widthLable,height);
		setBoundsAt(assistantUsername,x + widthLable, y ,width,20);
		
		y=y+25;
		
		setBoundsAt(assistantPasswordLable,x,y,widthLable,height);
		setBoundsAt(assistantPassword,x + widthLable, y ,width,20);
		
		y=y+25;
		
		setBoundsAt(clientUserNameLable,x,y,widthLable,height);
		setBoundsAt(clientUserName,x + widthLable, y ,width,20);
		
		y=y+25;
		
		setBoundsAt(clientPasswordLable,x,y,widthLable,height);
		setBoundsAt(clientPassword,x + widthLable, y ,width,20);	
		
		y=y+25;
		
		setBoundsAt(houseIDLable,x,y,widthLable,height);
		setBoundsAt(houseID,x + widthLable, y ,width,20);			
		
		y=y+25;
		
		setBoundsAt(houseTypeLable,x,y,widthLable,height);
		setBoundsAt(houseType,x + widthLable, y ,width,20);			
		
		y=y+25;
		
		setBoundsAt(determinedLable,x,y,widthLable,height);
		setBoundsAt(determined,x + widthLable, y ,width,20);	
	}
	
	
	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}
	
	

}
