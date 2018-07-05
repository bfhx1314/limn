package com.limn.frame.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.limn.tool.common.BaseToolParameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.renderer.WebElementCellRenderer;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.variable.Variable;

public class LoadBroswerPanel extends CustomPanel {

	/**
	 *
	 */
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

	private static JComboBox<String> filterWebElement = new JComboBox<>();

	private VerificationPanel verification = new VerificationPanel();

	// 查询的元素列表
	private HashMap<Integer, WebElement> findWebElements = new HashMap<>();
	private HashMap<Integer, String> showList = new HashMap<>();
	private HashMap<String, String> rangeList = new HashMap<>();

	//	private static JButton verificationButton = new JButton("验证");
	private static JButton setXPathName = new JButton("设置XPATH别名");

	private static JButton clearFilterElement = new JButton("清空元素");



	private JButton returnButton = new JButton("返回");
	private DebugEditFrame def;

	public LoadBroswerPanel(DebugEditFrame def) {

		setBounds(0, 0, 635, 395);
		setLayout(null);
		this.def = def;
		// 页面URL
		setBoundsAt(titleLabel, 5, 5, 80, 20);
		setBoundsAt(title, 65, 5, 500, 20);
		result.setText("努力搜索中....");
		result.setForeground(Color.RED);
		result.setVisible(false);
		JLabel recommendLocatorLabel = new JLabel("Locator:");
		setBoundsAt(recommendLocatorLabel, 5, 30, 50, 20);
		setBoundsAt(recommendLocator, 65, 30, 300, 20);
		recommendLocator.setEditable(false);
		recommendLocator.setBorder(null);
		setBoundsAt(verification, 5, 125, 610, 280);
		verification.setVisible(false);
		setBoundsAt(returnButton, 515, 105, 100, 20);
		returnButton.setVisible(false);
		// 设置XPATH别名按钮
		setXPathName.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(setXPathName, 515, 30, 100, 20);
		setXPathName.setEnabled(false);
		// 搜索框
		JLabel locatorLabel = new JLabel("定位:");
		JButton search = new JButton("搜索");
		setBoundsAt(locatorLabel, 5, 55, 50, 20);
		setBoundsAt(locator, 35, 55, 300, 20);
		locator.setHorizontalAlignment(JTextField.RIGHT);
		setBoundsAt(search, 340, 55, 60, 20);
		setBoundsAt(result, 300, 80, 100, 20);
		result.setVisible(false);
		setBoundsAt(clearFilterElement,410,80,200,20);
		setBoundsAt(filterWebElement, 410, 55, 200, 20);
		// WebElement 的list
		setBoundsAt(webElementsJSP, 5, 105, 610, 280);



//		verificationButton.setMargin(new Insets(0, 0, 0, 0));
//		setBoundsAt(verificationButton, 410, 50, 100, 20);
//		verificationButton.setEnabled(false);
//
//		verificationButton.addActionListener(new ActionListener() {
//
//
//			@Override
//			//验证界面
//			public void actionPerformed(ActionEvent e) {
//				//TODO 验证界面
//
//				WebElement web = def.getDriver().getWebElementBylocator(recommendLocator.getText());
//				verification.setVerWebElement(web);
//				verification.setVisible(true);
//
//				webElementsJSP.setVisible(false);
//				returnButton.setVisible(true);
//				// 重写用例步骤
//				String step = DebugEditFrame.getStepTextArea();
//				if (RegExp.findCharacters(step, "^录入:")){
//					step = step.replace("录入:", "验证:");
//				}
//				def.setStepTextArea(step);
//				def.setAddExpectButton(true);
//			}
//		});


		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				webElementsJSP.setVisible(true);
				returnButton.setVisible(false);
				verification.setVisible(false);
			}
		});


		setXPathName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String locator = recommendLocator.getText();
				String[] caseStep = RegExp.splitKeyWord(DebugEditFrame.getStepTextArea());
				if(caseStep== null || caseStep.length<1){
					JOptionPane.showMessageDialog(LoadBroswerPanel.title,"无内容设置");
					return;
				}
//				if(locator.equals("未能定位")){
//					return ;
//				}
				//xpath别名
				String name = null;
				//是否完成
				boolean flag = true;
				do{
					name = JOptionPane.showInputDialog(LoadBroswerPanel.this, "设置XPATH别名", "");
					if (name != null && !name.equals("")) {
						HashMap<String, String> hm = DebugEditFrame.getXpathName();
						if (hm.containsKey(name)){
							int status = JOptionPane.showConfirmDialog(LoadBroswerPanel.this, "存在重复的关联属性:" + name + ",是否覆盖", "警告",JOptionPane.OK_CANCEL_OPTION);
							if(status == 0){
								DebugEditFrame.setXpathName(name, locator);
								flag = false;
							}
						}else{
							def.setXpathName(name, locator);
							flag = false;
						}
					}else{
						return ;
					}
				}while(flag);
				String value = "";
				String currentStep = DebugEditFrame.getStepTextArea();
				String keyword = "录入:";
				if(!currentStep.isEmpty()){
					String[] step = RegExp.splitKeyWord(currentStep);
					keyword = step[0] + ":";
					if(step.length >= 3){
						value = step[2];
					}
				}
				// 传入用例输入框
				String step = keyword + name + ":" + value;
				def.setStepTextArea(step);

			}
		});


		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!locator.getText().isEmpty()) {
					title.setText(def.getDriver().driver.getCurrentUrl());
					new Thread(new SearchWebElement(locator.getText())).start();
				}
			}
		});
		filterWebElement.setEditable(true);
		Component comp = filterWebElement.getEditor().getEditorComponent();
		comp.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (!filterWebElement.getEditor().getItem().toString().isEmpty()) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) // 判断按下的键是否是回车键
					{
						WebElement web = null;
//
						try {
							web = def.getDriver().getWebElement(By.xpath("/html"));
						} catch (SeleniumFindException e1) {

						}

//						new Thread(new SearchWebElement(web,filterWebElement.getEditor().getItem().toString())).start();
					}
				}

			}
		});
		// 选中过滤
		filterWebElement.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// 清空所有元素
					webElementsList.removeAllElements();
					String selectItem = filterWebElement.getSelectedItem().toString();
					if (selectItem.equalsIgnoreCase("ALL")) {
						for (Integer key : showList.keySet()) {
							webElementsList.addElement(new DictoryKeyValue(key, showList.get(key)));
						}
					} else if(rangeList.size() != 0 && rangeList.get(selectItem) != null){
						String[] range = rangeList.get(selectItem).split(":");
						int start = Integer.valueOf(range[0]);
						int end = Integer.valueOf(range[1]);
						for (; start <= end; start++) {
							webElementsList.addElement(new DictoryKeyValue(start, showList.get(start)));
						}
					}
					webElements.setModel(webElementsList);
				}
			}

		});

		webElements.setCellRenderer(new WebElementCellRenderer());
		//右键菜单
		webElements.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

//				if (e.getButton() == 3) {
//
//					if (webElements.getSelectedIndex() == -1) {
//
//					} else {
//						WebElement web = findWebElements.get(webElements.getSelectedValue().key());
//						rightMenu = getRightMenu(web);
//					}
//					rightMenu.show(webElements, e.getX(), e.getY());
//				}

			}
		});
		webElements.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					try {
						if (null != currentHighWebElement) {
							def.getDriver().cancelHighLightWebElement(currentHighWebElement);
						}
						if (webElements.getSelectedIndex() == -1) {
							return;
						}
						currentHighWebElement = findWebElements.get((webElements.getSelectedValue()).key());
						setWebElmentByLocator(currentHighWebElement);
						def.getDriver().highLightWebElement(currentHighWebElement);
						def.setAddExpectButton(false);
					} catch (SeleniumFindException e1) {
						e1.printStackTrace();
					}
				}
			}
		});


		clearFilterElement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				webElementsList.removeAllElements();
				findWebElements.clear();
				filterWebElement.removeAllItems();
				webElements.removeAll();
				showList.clear();
				rangeList.clear();
				searchRange = 0;
			}
		});
	}

	/**
	 * 模块列表的右键菜单
	 *
	 * @param web
	 *            是否有选中项目
	 * @return
	 */
	private JPopupMenu getRightMenu(final WebElement web) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem verification = new JMenuItem("验证");

		verification.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO 
			}
		});
		menu.add(verification);
		return menu;

	}

	private void setBoundsAt(Component comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}


	/**
	 * loading
	 */
	private void loading(){
		recommendLocator.setText("");
		result.setVisible(true);
	}

	/**
	 * 加载完成
	 */
	private void complete(){
		result.setVisible(false);
	}


	/**
	 * 遍历页面中所有元素,取出tagname
	 * 废弃
	 */
	private void traversal() {
		currentHighWebElement = null;
		WebElement web = null;
		try {
			web = def.getDriver().getWebElement(By.xpath("/html"));
			title.setText(def.getDriver().getCurrentURL());
			// 清空所有元素
			webElementsList.removeAllElements();
			findWebElements.clear();
			filterWebElement.removeAllItems();
			webElements.removeAll();
			// 显示所有的
			filterWebElement.addItem("ALL");
			filterWebElement.setSelectedItem("ALL");
//			new Thread(new SearchWebElement(web)).start();

		} catch (SeleniumFindException e) {

		}

	}

	private void setWebElmentByLocator(WebElement web) {

		if (null == web) {
			return;
		}

		try {
			String locator = def.getDriver().getXpathByWebElement(web);
			if (null != locator && !locator.isEmpty()) {
				new Thread(new FindWebElement(locator)).start();
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

		if(null != web.getText() && !web.getText().isEmpty()){
			text = text + " text=" + web.getText();
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

	//搜索标记
	private int searchRange = 0;
	/**
	 * 搜索页面元素
	 * @author 001392
	 *
	 */
	class SearchWebElement implements Runnable{


		private String xpath;

		public SearchWebElement(String xpath){
			this.xpath = xpath;
		}

		@Override
		public void run() {
			loading();
			serachByXpath(xpath);
			complete();
		}

		private void serachByXpath(String value) {
			filterWebElement.addItem(value);
			filterWebElement.setSelectedItem(value);
			try {
				List<WebElement> webTagElementsList = def.getDriver().getWebElementsByXPath(xpath);
				addUIList(webTagElementsList, value);
			}catch (SeleniumFindException e){
				BaseToolParameter.getPrintThreadLocal().log("未查询到元素", 2);
			}
		}


		private void addUIList(List<WebElement> webTagElementsList,String tagName){
			int start = searchRange;
			webElements.setModel(webElementsList);
			for (WebElement webs : webTagElementsList) {
				findWebElements.put(searchRange, webs);
				String ident = getIdentifiedByWebElement(webs);
				showList.put(searchRange, ident);
				webElementsList.addElement(new DictoryKeyValue(searchRange, ident));
				searchRange++;
			}
			rangeList.put(tagName, start + ":" + (searchRange - 1));
		}


	}

	/**
	 * 根据页面元素生成XPATH进入用例步骤
	 * @author limengnan
	 *
	 */
	class FindWebElement implements Runnable{

		private String locator;

		public FindWebElement(String locator){
			locator = Variable.resolve(locator);
			this.locator = locator;
		}


		@Override
		public void run() {
			loading();

			if(null != def.getDriver().getWebElementBylocator(locator) || null != def.getDriver().getWebElementBylocator("/"+locator)){
				setXPathName.setEnabled(true);
//				verificationButton.setEnabled(true);
				recommendLocator.setText(locator);
				recommendLocator.setForeground(Color.GREEN.darker());
				String keyword = "录入:";
//				locatorXPath = locator;
				// 点击xpath时传入用例输入框
				String step = keyword + locator + ":";
				def.setStepTextArea(step);
			}else{
				recommendLocator.setText("未能定位");
				recommendLocator.setForeground(Color.RED.darker());
			}
			complete();
		}

	}



}
