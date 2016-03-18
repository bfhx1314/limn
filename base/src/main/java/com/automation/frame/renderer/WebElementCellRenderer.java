package com.automation.frame.renderer;

import com.automation.tool.dictory.WebElementLocatorDescribe;

import javax.swing.*;
import java.awt.*;

/**
 * Created by snow.zhang on 2015-11-05.
 */
public class WebElementCellRenderer extends JPanel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;
    private String text;
    private Color background;
    private Color foreground;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        text = ((WebElementLocatorDescribe) value).value();
        Color statusBackground;
        Color statusForgeround;
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
