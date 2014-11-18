package com.limn.frame.panel;

import java.awt.Component;
import java.awt.Dialog;







import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openqa.selenium.WebElement;

import com.limn.tool.common.Print;

public class VerificationPanel extends JPanel{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebElement verWeb = null;
	
	private JLabel verValueLabel = new JLabel();
	
	private JLabel verValue = new JLabel();
	
	public VerificationPanel(){
		
		setLayout(null);
		
		verValueLabel.setText("验证数据:");
		setBoundsAt(verValueLabel,10,10,200,20);
		
		setBoundsAt(verValue,220,10,200,20);
		
		
		
	}
	
	
	public void setVerWebElement(WebElement web){
		verWeb = web;
		String value = "";
		if(verWeb.getTagName().equalsIgnoreCase("input") 
				&& verWeb.getTagName().equalsIgnoreCase("textarea")){
			value = verWeb.getAttribute("value");
		}else if(verWeb.getTagName().equalsIgnoreCase("table")){
			
		}else{
			value = verWeb.getText();
		}
		
		verValue.setText(value);
		
//		List<WebElement> webs = verWeb.findElements(By.xpath("descendant::book"));
//		for(WebElement we : webs){
//			System.out.println(we.getText());
//		}
	}
	
	private void setBoundsAt(Component comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}
	

}
