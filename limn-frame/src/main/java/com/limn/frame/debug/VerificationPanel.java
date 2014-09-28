package com.limn.frame.debug;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class VerificationPanel extends CustomPanel{

	/**
	 * 实际结果输出界面
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea verification = new JTextArea();
	private JScrollPane verificationJSP = new JScrollPane(verification,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	private JLabel title = new JLabel("实际结果");
	public VerificationPanel(){
		setLayout(null);
		verification.setEditable(false);
		setBoundsAt(verificationJSP,0, 20, 400, 370);
		setBoundsAt(title,0, 0, 100, 20);
	}
	
	public void setResults(String results){
		verification.append(results);
		verification.setCaretPosition(verification.getText().length());
	}
	
	public void clearResults(){
		verification.setText("");
	}
	
	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}
}
