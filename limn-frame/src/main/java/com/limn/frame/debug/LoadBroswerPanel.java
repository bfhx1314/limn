package com.limn.frame.debug;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.renderer.WebElementCellRenderer;



public class LoadBroswerPanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static List<WebElement> inputWeb = null;
	private static List<WebElement> buttonWeb = null;
	private static List<WebElement> aWeb = null;
	
	private JLabel titleLabel = new JLabel("当前URL:");
	private static JLabel title = new JLabel();
	
	private static JList<WebElement> webElements = new JList<WebElement>();
	public static DefaultListModel<WebElement> webElementsList = new DefaultListModel<WebElement>();
	private JScrollPane webElementsJSP = new JScrollPane(webElements,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private WebElement currentHighWebElement = null;
	
	private JTextField locator = new JTextField();
	
	
	public LoadBroswerPanel(){
		
		setBounds(0, 0, 635, 395);
		setLayout(null);
		setBoundsAt(titleLabel,5,5,80,20);
		setBoundsAt(title,60,5,400,20);
		
		JLabel locatorLabel = new JLabel("定位:");
		JButton search = new JButton("搜索");
		setBoundsAt(locatorLabel,5,78,50,20);
		setBoundsAt(locator,35,78,200,20);
		setBoundsAt(search,240,78,60,20);
		
		setBoundsAt(webElementsJSP,5,100,500,280);
		
		search.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!locator.getText().isEmpty()) {
					try {
						if (null != currentHighWebElement) {
							Driver.cancelHighLightWebElement(currentHighWebElement);
						}
						currentHighWebElement = Driver.getWebElementBylocator(locator.getText());
						Driver.highLightWebElement(currentHighWebElement);
					} catch (SeleniumFindException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		webElements.setCellRenderer(new WebElementCellRenderer());
		
		webElements.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					if (null != currentHighWebElement) {
						Driver.cancelHighLightWebElement(currentHighWebElement);
					}

					currentHighWebElement = webElements.getSelectedValue();

					Driver.highLightWebElement(currentHighWebElement);
				} catch (SeleniumFindException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public static void loadWebElement() throws SeleniumFindException{
		WebElement web = Driver.getWebElement(By.xpath("/html"));
		title.setText(Driver.driver.getCurrentUrl());
		traversal(web);
		
	}
	
	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}
	
	/**
	 * 遍历页面中所有元素,取出tagname = input,a,button
	 * @param web
	 */
	private static void traversal(WebElement web){
		
		inputWeb = web.findElements(By.tagName("input"));
		aWeb = web.findElements(By.tagName("a"));
		buttonWeb = web.findElements(By.tagName("button"));
		
		for(WebElement webs:inputWeb){
			webElementsList.addElement(webs);
		}
		
		for(WebElement webs:aWeb){
			webElementsList.addElement(webs);
		}
		
		for(WebElement webs:buttonWeb){
			webElementsList.addElement(webs);
		}
		webElements.setModel(webElementsList);
	}

}
