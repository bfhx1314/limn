package com.limn.control;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.limn.parameter.Parameter;



public class UploadServerSetting{
	
	private JDialog uploadInfor;
	
	private JLabel labelDriver = new JLabel("数据库驱动");
	private JTextField textDriver = new JTextField();

	private JLabel labelURL = new JLabel("地址");
	private JTextField textURL = new JTextField();

	private JLabel labelUser = new JLabel("用户名");
	private JTextField textUser = new JTextField();

	private JLabel labelPass = new JLabel("密码");
	private JTextField textPass = new JTextField();
	
	private JButton ok = new JButton("确定");
	
	public UploadServerSetting(JFrame owner, String title, boolean modal) {
		uploadInfor = new JDialog(owner, title, modal);
		
		uploadInfor.setLayout(null);
		
		int labelX = 20;
		int textX = 100;
		
		int IntervalY = 40;
		
		int labelWidth = 70;
		int textWidth = 300;
		
		int labelHeight = 30;
		int textHeight = 30;
		
		setBoundsAtPanel(labelDriver, labelX, IntervalY, labelWidth, labelHeight);
		setBoundsAtPanel(textDriver, textX, IntervalY, textWidth, textHeight);
		IntervalY+=40;
		setBoundsAtPanel(labelURL, labelX, IntervalY, labelWidth, labelHeight);
		setBoundsAtPanel(textURL, textX, IntervalY, textWidth, textHeight);
		IntervalY+=40;
		setBoundsAtPanel(labelUser, labelX, IntervalY, labelWidth, labelHeight);
		setBoundsAtPanel(textUser, textX, IntervalY, textWidth, textHeight);
		IntervalY+=40;
		setBoundsAtPanel(labelPass, labelX, IntervalY, labelWidth, labelHeight);
		setBoundsAtPanel(textPass, textX, IntervalY, textWidth, textHeight);
		
		IntervalY+=40;
		setBoundsAtPanel(ok,labelX + 200,IntervalY + 20,labelWidth,labelHeight);
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(testConnect()){
					saveParameter();
					uploadInfor.dispose();
				}else{
					JOptionPane.showMessageDialog(uploadInfor,"数据库连接失败");
				}
				
			}
		});
		
		loadParameter();
		
		uploadInfor.setBounds((int) ((getScreenWidth() - 450) *0.5), (int) ((getScreenHeight() - 300) *0.5), 450,
				300);
		
		uploadInfor.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		uploadInfor.setVisible(true);
		
		
		
		
	}


	/**
	 * 取得当前显示器屏幕宽度
	 */
	private int getScreenWidth(){
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		return screenWidth;
	}
	
	/**
	 * 取得当前显示器屏幕高度
	 */
	private int getScreenHeight(){
		int screenHeight =java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;	
		return screenHeight;
		
	}
	
	/**
	 * 添加控件 并且设置大小
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void setBoundsAtPanel(JComponent comp,int x,int y,int width,int height){
		uploadInfor.add(comp);
		comp.setBounds(x, y, width, height);
	}
	
	private void loadParameter(){
//		textDriver.setText(Variable.resolve("[UploadServer_driver]"));
//		textURL.setText(Variable.resolve("[UploadServer_url]"));
//		textUser.setText(Variable.resolve("[UploadServer_user]"));
//		textPass.setText(Variable.resolve("[UploadServer_passwd]"));
	}
	
	private void saveParameter(){
		CoreReader cr = new CoreReader();
		cr.setCorePath(Parameter.DEFAULT_CONF_PATH + "/variable.properties");
		cr.getAllValue();
		cr.setValueByKey("UploadServer_driver", textDriver.getText());
		cr.setValueByKey("UploadServer_url", textURL.getText());
		cr.setValueByKey("UploadServer_user", textUser.getText());
		cr.setValueByKey("UploadServer_passwd", textPass.getText());
	}
	
	private boolean testConnect(){
		return true;
//		return DataBaseVerification.isConnect(textDriver.getText(), textURL.getText(), textUser.getText(), textPass.getText());
	}
	
	
}
