package com.limn.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import com.limn.frame.panel.DictoryKeyValue;



public class WebElementCellRenderer extends JPanel implements ListCellRenderer<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private Color background;
	private Color foreground;

	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		

		text = ((DictoryKeyValue) value).value();

		
		Color statusBackground = null;
		Color statusForgeround = null;
		statusForgeround = Color.GRAY;
//		if(status == ClientStatus.CONNECT){
			statusBackground = Color.WHITE;
//		}else if(status == ClientStatus.OFFLINE){
//			statusBackground = Color.LIGHT_GRAY;
//		}else if(status == ClientStatus.RUN){
//			statusBackground = Color.GREEN;
//		}
		background = statusBackground;
		foreground = isSelected ? Color.BLACK : statusForgeround;
		return this;
	}
	
	public void paintComponent(Graphics g)
    {
       g.setColor(background);
       g.fillRect(0, 0, getWidth(), getHeight());  //设置背景色
       g.setColor(foreground);
       g.setFont(new Font(null,Font.BOLD,12));
       g.drawString(text, 5, 15);   //在制定位置绘制文本
    }

    public Dimension getPreferredSize()
    {
       return new Dimension(30, 20);   //Cell的尺寸
    }
	
	

}
