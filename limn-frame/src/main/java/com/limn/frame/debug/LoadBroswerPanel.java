package com.limn.frame.debug;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.renderer.WebElementCellRenderer;



public class LoadBroswerPanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	
	private JLabel titleLabel = new JLabel("当前URL:");
	private static JLabel title = new JLabel();
	
	private static JList<WebElement> webElements = new JList<WebElement>();
	public static DefaultListModel<WebElement> webElementsList = new DefaultListModel<WebElement>();
	private JScrollPane webElementsJSP = new JScrollPane(webElements,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private WebElement currentHighWebElement = null;
	
	private JTextField locator = new JTextField();
	
	private JLabel result = new JLabel();
	
	private static JTextField recommendLocator = new JTextField();
	
	private static JComboBox<String> filterWebElement  = new JComboBox<String>();
	
	//查询的元素列表
	private static HashMap<String,List<WebElement>> findWebElements = new HashMap<String, List<WebElement>>();
	
	public LoadBroswerPanel(){
		
		setBounds(0, 0, 635, 395);
		setLayout(null);
		//页面URL
		setBoundsAt(titleLabel,5,5,80,20);
		setBoundsAt(title,65,5,400,20);
		
		JLabel recommendLocatorLabel = new JLabel("Locator:");
		
		setBoundsAt(recommendLocatorLabel,5,50,50,20);
		setBoundsAt(recommendLocator,65,50,200,20);

		recommendLocator.setEditable(false);
		recommendLocator.setBorder(null);
		
		//搜索框
		JLabel locatorLabel = new JLabel("定位:");
		JButton search = new JButton("搜索");
		setBoundsAt(locatorLabel,5,78,50,20);
		setBoundsAt(locator,35,78,200,20);
		locator.setHorizontalAlignment(JTextField.RIGHT);
		setBoundsAt(search,240,78,60,20);
		setBoundsAt(result,320,78,60,20);
		result.setVisible(false);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!locator.getText().isEmpty()) {
					//开始搜索
					result.setText("努力搜索中....");
					result.setForeground(Color.BLACK);
					try {
						if (null != currentHighWebElement) {
							Driver.cancelHighLightWebElement(currentHighWebElement);
						}
						currentHighWebElement = Driver.getWebElementBylocator(locator.getText());
						result.setVisible(true);
						if( null != currentHighWebElement){
							//找到元素
							result.setForeground(Color.GREEN.darker());
							result.setText("Find");
							Driver.highLightWebElement(currentHighWebElement);
						}else{
							//未找到元素
							result.setForeground(Color.RED);
							result.setText("Not Find");
						}
					} catch (SeleniumFindException e1) {
						e1.printStackTrace();
					}
				}
			}
		});		
		
		setBoundsAt(filterWebElement,404,78,100,20);
		//选中过滤
		filterWebElement.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					//清空所有元素
					webElementsList.removeAllElements();
					
					String selectItem = filterWebElement.getSelectedItem().toString();
					if(selectItem.equalsIgnoreCase("ALL")){
						for (String tagname : findWebElements.keySet()) {
							for (WebElement webs : findWebElements.get(tagname)) {
								webElementsList.addElement(webs);
							}
						}
					}else{
						for (WebElement webs : findWebElements.get(selectItem)) {
							webElementsList.addElement(webs);
						}
					}
					webElements.setModel(webElementsList);
				}
			}
		});
		
		
		//WebElement 的list
		setBoundsAt(webElementsJSP, 5, 100, 500, 280);

		webElements.setCellRenderer(new WebElementCellRenderer());

		webElements.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					if (null != currentHighWebElement) {
						Driver.cancelHighLightWebElement(currentHighWebElement);
					}

					currentHighWebElement = webElements.getSelectedValue();
					
					setWebElmentByLocator(currentHighWebElement);

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
		//清空所有元素
		webElementsList.removeAllElements();
		
		
		//要搜索哪些元素
		String[] findTagName = {"input","a","button"};
		//显示所有的
		filterWebElement.addItem("ALL");
		
		for(String tagName:findTagName){
			filterWebElement.addItem(tagName);
			findWebElements.put(tagName,web.findElements(By.tagName(tagName)));
		}
		
		filterWebElement.setSelectedItem("ALL");
		for (String tagname : findWebElements.keySet()) {
			for (WebElement webs : findWebElements.get(tagname)) {
				webElementsList.addElement(webs);
			}
		}
	}

	
	private static void setWebElmentByLocator(WebElement web){
		if(null == web){
			return;
		}
		//first 查找唯一属性
		recommendLocator.setText("努力搜索中....");
		recommendLocator.setForeground(Color.BLACK);
		try {
			HashMap<String,String> data = new HashMap<String,String>();
			int attCount = ((Long) Driver.runScript("return arguments[0].attributes.length", web)).intValue();
			for (int i = 0; i < attCount; i++) {
				String name = String.valueOf(Driver.runScript("return arguments[0].attributes[" + i + "].name", web));
				String value = String.valueOf(Driver.runScript("return arguments[0].attributes[" + i + "].value", web));
				data.put(name, value);
			}
			
			if(data.containsKey("id")){
				recommendLocator.setText(data.get("id"));
				recommendLocator.setForeground(Color.GREEN.darker());
			}else{
				recommendLocator.setText("未能定位");
				recommendLocator.setForeground(Color.RED.darker());
			}
			
		} catch (SeleniumFindException e) {
			
		}

	}
	
}
