package com.limn.frame.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.frame.edit.EditTestCasePanel;
import com.limn.renderer.WebElementCellRenderer;
import com.limn.tool.common.Print;

public class LoadBroswerPanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel titleLabel = new JLabel("当前URL:");
	private static JLabel title = new JLabel();

	private static JList<DictoryKeyValue> webElements = new JList<DictoryKeyValue>();
	public static DefaultListModel<DictoryKeyValue> webElementsList = new DefaultListModel<DictoryKeyValue>();
	private JScrollPane webElementsJSP = new JScrollPane(webElements,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	private WebElement currentHighWebElement = null;

	private JTextField locator = new JTextField();

	private JLabel result = new JLabel();

	private static JTextField recommendLocator = new JTextField();

	private static JComboBox<String> filterWebElement = new JComboBox<String>();

	// 查询的元素列表
	private HashMap<Integer, WebElement> findWebElements = new HashMap<Integer, WebElement>();
	private HashMap<Integer, String> showList = new HashMap<Integer, String>();
	private HashMap<String, String> rangeList = new HashMap<String, String>();



	private JButton refresh = new JButton("刷新");
	private static JButton setXPathName = new JButton("设置XPATH别名");
	private static String locatorXPath = "";
	// private JButton setXPathName = new JButton("设置XPATH别名");
	public LoadBroswerPanel() {

		setBounds(0, 0, 635, 395);
		setLayout(null);
		// 页面URL
		setBoundsAt(titleLabel, 5, 5, 80, 20);
		setBoundsAt(title, 65, 5, 500, 20);
		setBoundsAt(refresh, 565, 5, 50, 20);
		refresh.setMargin(new Insets(0, 0, 0, 0));
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setXPathName.setEnabled(false);
					loadWebElement();
				} catch (SeleniumFindException e1) {
					e1.printStackTrace();
				}

			}
		});
		JLabel recommendLocatorLabel = new JLabel("Locator:");

		setBoundsAt(recommendLocatorLabel, 5, 50, 50, 20);
		setBoundsAt(recommendLocator, 65, 50, 300, 20);

		recommendLocator.setEditable(false);
		recommendLocator.setBorder(null);

		// 设置XPATH别名按钮
		// JButton setXPathName = new JButton("设置XPATH别名");
		setXPathName.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(setXPathName, 515, 50, 100, 20);
		setXPathName.setEnabled(false);
		setXPathName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(
						LoadBroswerPanel.this, "设置XPATH别名", "");
				if (name != null && !name.equals("")) {
					DebugEditFrame.xpathName.put(name, locatorXPath);
					// 传入用例输入框
					String step = "录入:" + name + ":" ;
					DebugEditFrame.setStepTextArea(step);
					// System.out.println(name);
				}

			}
		});
		// 搜索框
		JLabel locatorLabel = new JLabel("定位:");
		JButton search = new JButton("搜索");
		setBoundsAt(locatorLabel, 5, 78, 50, 20);
		setBoundsAt(locator, 35, 78, 300, 20);
		locator.setHorizontalAlignment(JTextField.RIGHT);
		setBoundsAt(search, 340, 78, 60, 20);
		setBoundsAt(result, 410, 78, 60, 20);
		result.setVisible(false);

		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!locator.getText().isEmpty()) {
					// 开始搜索
					result.setText("努力搜索中....");
					result.setForeground(Color.BLACK);
					try {
						if (null != currentHighWebElement) {
							Driver.cancelHighLightWebElement(currentHighWebElement);
						}
						currentHighWebElement = Driver
								.getWebElementBylocator(locator.getText());
						result.setVisible(true);
						if (null != currentHighWebElement) {
							// 找到元素
							result.setForeground(Color.GREEN.darker());
							result.setText("Find");
							Driver.highLightWebElement(currentHighWebElement);
						} else {
							// 未找到元素
							result.setForeground(Color.RED);
							result.setText("Not Find");
						}
					} catch (SeleniumFindException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		setBoundsAt(filterWebElement, 515, 78, 100, 20);
		// 选中过滤
		filterWebElement.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 清空所有元素
					webElementsList.removeAllElements();
					String selectItem = filterWebElement.getSelectedItem()
							.toString();
					if (selectItem.equalsIgnoreCase("ALL")) {
						for (Integer key : showList.keySet()) {
							webElementsList.addElement(new DictoryKeyValue(key,
									showList.get(key)));
						}
					} else {
						String[] range = rangeList.get(selectItem).split(":");
						int start = Integer.valueOf(range[0]);
						int end = Integer.valueOf(range[1]);
						for (; start <= end; start++) {
							webElementsList.addElement(new DictoryKeyValue(
									start, showList.get(start)));
						}
					}
					webElements.setModel(webElementsList);
				}
			}

		});

		// WebElement 的list
		setBoundsAt(webElementsJSP, 5, 105, 610, 280);
		webElements.setCellRenderer(new WebElementCellRenderer());
		webElements.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					try {
						if (null != currentHighWebElement) {
							Driver.cancelHighLightWebElement(currentHighWebElement);
						}

						if (webElements.getSelectedIndex() == -1) {
							return;
						}

						currentHighWebElement = findWebElements
								.get(((DictoryKeyValue) webElements
										.getSelectedValue()).key());

						setWebElmentByLocator(currentHighWebElement);

						Driver.highLightWebElement(currentHighWebElement);
					} catch (SeleniumFindException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

	}

	public void loadWebElement() throws SeleniumFindException {

		Print.log("URL:" + Driver.getCurrentURL(), 0);
		title.setText(Driver.driver.getCurrentUrl());
		traversal();
	}

	private void setBoundsAt(Component comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}

	/**
	 * 遍历页面中所有元素,取出tagname = input,a,button
	 * 
	 * @param web
	 */
	private void traversal() {
		currentHighWebElement = null;
		WebElement web = null;
		try {
			web = Driver.getWebElement(By.xpath("/html"));
			title.setText(Driver.driver.getCurrentUrl());

			// 清空所有元素

			webElementsList.removeAllElements();
			findWebElements.clear();
			filterWebElement.removeAllItems();
			webElements.removeAll();

			// 要搜索哪些元素
			String[] findTagName = { "input", "a", "button", "select" };
			// 显示所有的
			filterWebElement.addItem("ALL");

			int range = 0;
			for (String tagName : findTagName) {
				filterWebElement.addItem(tagName);
				int start = range;
				for (WebElement webs : web.findElements(By.tagName(tagName))) {
					findWebElements.put(range, webs);
					showList.put(range, getIdentifiedByWebElement(webs));
					range++;
				}
				rangeList.put(tagName, start + ":" + (range - 1));
			}

			filterWebElement.setSelectedItem("ALL");
			for (Integer key : showList.keySet()) {
				webElementsList.addElement(new DictoryKeyValue(key, showList
						.get(key)));
			}

		} catch (SeleniumFindException e) {

		}

	}

	private static void setWebElmentByLocator(WebElement web) {
		if (null == web) {
			return;
		}
		// first 查找唯一属性
		recommendLocator.setText("努力搜索中....");
		recommendLocator.setForeground(Color.BLACK);
		try {

			String locator = String.valueOf(Driver.runScript(
					"return getLocatorByNode(arguments[0])", web));

			if (null != locator && !locator.isEmpty()) {
				setXPathName.setEnabled(true);
				recommendLocator.setText(locator);
				recommendLocator.setForeground(Color.GREEN.darker());
				locatorXPath = locator;
				// 点击xpath时传入用例输入框
				String step = "录入:" + locator + ":";
				DebugEditFrame.setStepTextArea(step);
			} else {
				recommendLocator.setText("未能定位");
				recommendLocator.setForeground(Color.RED.darker());
			}

		} catch (SeleniumFindException e) {

		}

	}

	private String getIdentifiedByWebElement(WebElement web) {

		String text = web.getTagName() + "{";
		String att_id = web.getAttribute("id");
		String att_name = web.getAttribute("name");
		String att_class = web.getAttribute("class");

		if (web.getTagName().equalsIgnoreCase("input")) {
			text = text + " type=" + web.getAttribute("type");
		}

		if (null != att_id && !att_id.isEmpty()) {
			text = text + " id=" + att_id;
		}

		if (null != att_name && !att_name.isEmpty()) {
			text = text + " name=" + att_name;
		}

		if (null != att_class && !att_class.isEmpty()) {
			text = text + " class=" + att_class;
		}

		text = text + "}";

		return text;
	}

}
