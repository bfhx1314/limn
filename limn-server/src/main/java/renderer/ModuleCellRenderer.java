package renderer;

import information.Console;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import status.ModuleStatus;


public class ModuleCellRenderer extends JPanel implements ListCellRenderer<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private Color background;
	private Color foreground;
	private String status = null;
	
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		String[] info = ((String) value).split(":");
		
		text = info[0];
	
		status = info[1];
		Color statusBackground  = null;
		Color statusForeground  = Color.GRAY;
		text = text + " -- " + status;
		if(status.equals(ModuleStatus.READY)){
			statusBackground = Color.WHITE;
		}else if(status.equals(ModuleStatus.RUN)){
			statusBackground = Color.GREEN;
		}else if(status.equals(ModuleStatus.START)){
			statusBackground = Color.YELLOW;
		}else if(status.equals(ModuleStatus.START)){
			statusBackground = Color.RED;
		}
		Console.updateTestModule();
		background = statusBackground;
		
		foreground = isSelected ? Color.BLACK : statusForeground;
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
