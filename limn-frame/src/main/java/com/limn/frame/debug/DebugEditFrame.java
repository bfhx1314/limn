package com.limn.frame.debug;



import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.limn.frame.edit.EditTestCasePanel;
import com.limn.frame.keyword.BaseAppKeyWordDriverImpl;
import com.limn.frame.keyword.BaseAppKeyWordType;
import com.limn.frame.keyword.BaseKeyWordDriverImpl;
import com.limn.frame.keyword.BaseKeyWordType;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.panel.CustomPanel;
import com.limn.frame.panel.KeyWordPanel;
import com.limn.frame.panel.LoadBroswerPanel;
import com.limn.frame.panel.UIViewPanel;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.log.LogControlInterface;
import com.limn.tool.log.LogDocument;
import com.limn.tool.log.PrintLogDriver;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;



/**
 * 
 * 主界面
 * 用例维护,用例执行
 * @author limn
 *
 */
public class DebugEditFrame extends PrintLogDriver implements LogControlInterface{
	
	private JFrame jframe = new JFrame("用例调试器 V1.0");
	
	//用例编辑执行框
	private static JTextPane testCase = new JTextPane();
	//增加滚动条
	private JScrollPane testCaseJSP = new JScrollPane(testCase,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private JButton moveTestCase = new JButton(">");
	private JButton moveEditTestCase = new JButton("<");
	
	//自定义面板
	private KeyWordPanel keyWordPanel = null;
	
//	private JTabbedPane Jtab = new JTabbedPane();
	
//	private JButton eidt = new JButton("编辑");
//	private JButton change = new JButton("关键字");
//	private JButton loadBrowser =new JButton("定位");
	
	//步骤编辑区
	private JTable editTestCase = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	
	private JScrollPane stepJScrollStep = new JScrollPane(editTestCase,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	//预期结果编辑区
	private JTable editExpect = new JTable();
	private DefaultTableModel expectModel = new DefaultTableModel();
	private JScrollPane expectJScrollStep = new JScrollPane(editExpect,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private static JButton addExpectResult = new JButton("添加预期");
	private JButton execute = new JButton("执行");
	private JButton insertExecute = new JButton("插入执行");
	private JButton executeAgain = new JButton("执行");
	private JButton clearLog = new JButton("清空日志");
	private JButton deleteRow = new JButton("删除");
	private JButton deleteAllRow = new JButton("删除全部");
	private JButton deleteXPath = new JButton("删除别名");
	
	// TAB页
	private static JTabbedPane tabbedPane = new JTabbedPane();
//
//	public static JTabbedPane getTabbedPane() {
//		return tabbedPane;
//	}

	JPanel pabelLog = new JPanel();
	JPanel pabelTestCase = new JPanel();
	
	private static BaseKeyWordDriverImpl keyWordDriver = new BaseKeyWordDriverImpl();
	
	private EditTestCasePanel testCasePanel = new EditTestCasePanel();
	
	private UIViewPanel uiViewPanel = new UIViewPanel();
	
//	public VerificationPanel verificationPanel = new VerificationPanel();
	//载入面板
	private LoadBroswerPanel loadPanel = new LoadBroswerPanel();
	
	private static boolean isVerKeyWord = false;
	
	private CustomPanel isShowPanel = null;
	
	private LinkedHashMap<String,CustomPanel> panelSet = new LinkedHashMap<String,CustomPanel>();
	/**
	 * 关联TAB中的JScrollPane面板与TAB中的DefaultTableModel累中记录的用例步骤或者预期结果
	 */
	private LinkedHashMap<JTable, DefaultTableModel> paneMode = new LinkedHashMap<JTable,DefaultTableModel>();
	// 存放XPATH与别名
	private static LinkedHashMap<String, String> xpathName = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> getXpathName() {
		return xpathName;
	}

	public static void setXpathName(String key, String value) {
		if(xpathName.containsKey(key)){
			xpathName.replace(key, value);
		}else{
			xpathName.put(key, value);
		}
	}
	
	
	public static void removeXpathAll() {
		Print.log("删除全部关联属性:" + xpathName.size(), 0);
		xpathName.clear();
	}
	
	public static void removeXpathName(String key) {
		if(xpathName.containsKey(key)){
			Print.log("删除关联属性:" + key + "=" + xpathName.get(key), 0);
			xpathName.remove(key);
		}
	}
	
	/**
	 * 设置“添加预期”按钮可交互
	 * @param bool
	 */
	public static void setAddExpectButton(boolean bool){
		addExpectResult.setEnabled(bool);
	}

	public DebugEditFrame(){
		keyWordPanel = new KeyWordPanel();
		addKeyWordDriver("基础关键字", new BaseKeyWordDriverImpl(), BaseKeyWordType.class);
		addKeyWordDriver("App基础关键字", new BaseAppKeyWordDriverImpl(), BaseAppKeyWordType.class);
		//TODO
		init();
	}

	/**
	 * 增加关键字
	 * @param key 关键字分类名称
	 * @param keyWord 关键字驱动
	 * @param keyWordType 关键字定义
	 */
	public void addKeyWordDriver(String key, KeyWordDriver keyWord,Class<?> keyWordType){
		keyWordDriver.addKeyWordDriver(key, keyWord, keyWordType);
		KeyWordPanel.addKeyWord(key,keyWordType);
	}
	
	
	private void init(){
		//基本设置
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width);
		jframe.setBounds((int) ((screenWidth - 1000) *0.5), (int) ((screenHeight - 600) *0.5), 1000, 600);
		
		jframe.setLayout(null);
		
		setBoundsAt(testCaseJSP,2, 5, 300, 70);
		setBoundsAt(addExpectResult, 10, 80, 86, 20);
		addExpectResult.setEnabled(false);
		setBoundsAt(insertExecute,120, 80, 86, 20);
		setBoundsAt(execute,230, 80, 70, 20);
		
//		setBoundsAt(stepJScrollStep,2, 125, 300, 250);
		executeAgain.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(executeAgain,210, 380, 50, 20);
		clearLog.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(clearLog,265, 380, 70, 20);
		
		deleteRow.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(deleteRow,155, 380, 50, 20);
		deleteAllRow.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(deleteAllRow,80, 380, 70, 20);
		deleteXPath.setMargin(new Insets(0, 0, 0, 0));
		setBoundsAt(deleteXPath,5, 380, 70, 20);
		
		
		jframe.getContentPane().add(tabbedPane);
		tabbedPane.setBounds(2, 105, 300, 270);
		tabbedPane.addTab("用例步骤", null, stepJScrollStep, null);
		tabbedPane.addTab("预期结果", null, expectJScrollStep, null);
		// 关联TAB面板中组件与DefaultTableModel类
		paneMode.put(editTestCase, model);
		paneMode.put(editExpect, expectModel);
		
		deleteXPath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DebugEditFrame.removeXpathAll();
			}
		});
		
		clearLog.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearLog();
			}
		});
		
		
//		setBoundsAt(Jtab,350, 0, 635, 395);
		
//		setBoundsAt(testCasePanel,350, 0, 635, 395);
//		setBoundsAt(keyWordPanel,350, 0, 635, 395);
//		setBoundsAt(loadPanel,350, 0, 635, 395);
		
//		Jtab.add("用例编辑器", testCasePanel);
//		Jtab.add("页面定位器", loadPanel);
//		Jtab.add("自定义面板", customPanel);
//		testCasePanel.setVisible(false);
//		customPanel.setVisible(false);
		

		
		addPanel("编辑", testCasePanel);
		addPanel("关键字", keyWordPanel);
		addPanel("Web", loadPanel);
		addPanel("Mobile",uiViewPanel);
//		addPanel("验证", verificationPanel);
		
		
		testCasePanel.setVisible(true);
		isShowPanel = testCasePanel;
//		setBoundsAt(eidt, 303, 5, 46, 20);
//		eidt.setMargin(new Insets(0,0,0,0));
//		eidt.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e){
////				if (loadPanel.isVisible()) {
//					testCasePanel.setVisible(true);
//					loadPanel.setVisible(false);
//					keyWordPanel.setVisible(false);
////				} else {
////					testCasePanel.setVisible(false);
////					loadPanel.setVisible(true);
////					customPanel.setVisible(false);
////				}
//			}
//		});
//		
//		setBoundsAt(loadBrowser,303, 30, 46, 20);
//		loadBrowser.setMargin(new Insets(0,0,0,0));
//		loadBrowser.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e){
////				if (loadPanel.isVisible()) {
////					testCasePanel.setVisible(true);
////					loadPanel.setVisible(false);
////					customPanel.setVisible(false);
////				} else {
//					testCasePanel.setVisible(false);
//					loadPanel.setVisible(true);
//					keyWordPanel.setVisible(false);
////				}
//			}
//		});
//		
//		loadPanel.setVisible(false);
//		setBoundsAt(change, 303, 55, 46, 20);
//		change.setMargin(new Insets(0,0,0,0));
//		
//		//切换自定义的面板
//		change.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e){
////				if(customPanel.isVisible()){
////					testCasePanel.setVisible(true);
////					customPanel.setVisible(false);
////					loadPanel.setVisible(false);
////				}else{
//					testCasePanel.setVisible(false);
//					keyWordPanel.setVisible(true);
//					loadPanel.setVisible(false);
////				}
//				
//			}
//		});
		
		setBoundsAt(moveTestCase,303, 200, 46, 20);
		setBoundsAt(moveEditTestCase,303, 250, 46, 20);
		setBoundsAt(logJScrollLog,2, 405, 990, 165);

//		writeLogPane.setSize(new Dimension(290, 234));
		writeLogPane.setEditable(false);
//		writeStepPane.setPreferredSize(new Dimension(244, 234));
//		writeStepPane.setEditable(false);
		writeLogPane.setDocument(new LogDocument(writeLogPane, 400));
		
		expectModel.addColumn("预期结果");
		editExpect.setRowHeight(20);
		editExpect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		editExpect.setModel(expectModel);
		addExpectResult.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isVerKeyWord){
					int status = JOptionPane.showConfirmDialog(jframe, "关键字有误,是否继续执行", "警告" ,JOptionPane.YES_NO_OPTION);
					if(status==1){
						return; 
					}
				}
				
				String[] steps = testCase.getText().split("\n");
				for(String evertStep:steps){
					if (RegExp.findCharacters(evertStep, "^验证:")){
						evertStep = evertStep.replace("验证:", "");
					}
					expectModel.addRow(new Object[]{evertStep});
				}
//				model.addRow(new Object[]{testCase.getText()});
				int row = expectModel.getRowCount();
				editExpect.setModel(expectModel);
				editExpect.setRowSelectionInterval(0,row - 1);
				
				Rectangle rect = editExpect.getCellRect(row -1, 0, true);
				editExpect.scrollRectToVisible(rect);
				
//				executeStep(testCase.getText(),false);
				testCase.setText("验证:");
				// 激活"预期结果"面板
				tabbedPane.setSelectedComponent(expectJScrollStep);
				addExpectResult.setEnabled(false);
			}
		});
		
		model.addColumn("用例步骤");
		editTestCase.setRowHeight(20);
		editTestCase.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		editTestCase.setModel(model);
		
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isVerKeyWord){
					int status = JOptionPane.showConfirmDialog(jframe, "关键字有误,是否继续执行", "警告" ,JOptionPane.YES_NO_OPTION);
					if(status==1){
						return; 
					}
				}
				
				String[] steps = testCase.getText().split("\n");
				for(String evertStep:steps){
					model.addRow(new Object[]{evertStep});
				}
//				model.addRow(new Object[]{testCase.getText()});
				int row = model.getRowCount();
				editTestCase.setModel(model);
				editTestCase.setRowSelectionInterval(0,row - 1);
				
				Rectangle rect = editTestCase.getCellRect(row -1, 0, true);
				editTestCase.scrollRectToVisible(rect);
				
				executeStep(testCase.getText(),false);
				testCase.setText("");
				tabbedPane.setSelectedComponent(stepJScrollStep);
				setAddExpectButton(false);
			}
		});
		
		deleteAllRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取当前激活的TAB面板
//				Component selectComponent = tabbedPane.getSelectedComponent();
				int tabPaneLan = tabbedPane.getComponentCount();
				for(int j=0;j<tabPaneLan;j++){
					// 转换类型
//					JScrollPane selectComponentJScrollPane = (JScrollPane) selectComponent;
					JScrollPane selectComponentJScrollPane = (JScrollPane) tabbedPane.getComponentAt(j);
					// 获取当前TAB面板中的组件，并且转换类型
					JTable selectJTable = ((JTable) selectComponentJScrollPane.getViewport().getView());
					// 获取组件对应的类
					DefaultTableModel selectJTbaleMod = paneMode.get(selectJTable);
					for(int i = selectJTbaleMod.getRowCount()-1 ;i>-1;i--){
						selectJTbaleMod.removeRow(i);
					}
				}
				removeXpathAll();
			}
		});
		
		deleteRow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取当前激活的TAB面板
				Component selectComponent = tabbedPane.getSelectedComponent();
				// 转换类型
				JScrollPane selectComponentJScrollPane = (JScrollPane) selectComponent;
				// 获取当前TAB面板中的组件，并且转换类型
				JTable selectJTable = ((JTable) selectComponentJScrollPane.getViewport().getView());
				// 获取组件对应的类
				DefaultTableModel selectJTbaleMod = paneMode.get(selectJTable);
				// TODO 执行按钮、删除XPath按钮也要更改
				if(selectJTable.getSelectedRow()!=-1){
					int row = selectJTable.getSelectedRow();
//					int in = model.getColumnCount();
					String excelVaule = selectJTbaleMod.getValueAt(row, 0).toString();
					String splitKey[] = RegExp.splitKeyWord(excelVaule);
					if(splitKey.length>1 && (splitKey[0].equals("录入")
											||splitKey[0].equals("录入日期")
											||splitKey[0].equals("添加附件")
											||splitKey[0].equals("获取"))){
						removeXpathName(splitKey[1]);
					}else {
						removeXpathName(splitKey[0]);
					}
//					String searchKey = RegExp.splitKeyWord(excelVaule)[1];
//					String[] getNameXPath = testCasePanel.getSelectStepXPath();
//					int index = -1;
//					if (getNameXPath.length>1){
//						for(int j=1;j<getNameXPath.length;j++){
//							String[] arrXPath = RegExp.splitWord(getNameXPath[j], "\t");
//							String XPathKey = arrXPath[0];
//							String XPathVaule = arrXPath[1];
//							DebugEditFrame.setXpathName(XPathKey, XPathVaule);
//							if (searchKey.equals(XPathKey)){
//								DebugEditFrame.removeXpathName(XPathKey);
////								index = j;
//							}
//						}
//					}

//					String[] ary = new String[getNameXPath.length-1];
//					if (index != -1){
//						System.arraycopy(getNameXPath, 0, ary, 0, index);
//						System.arraycopy(getNameXPath, index+1, ary, index, ary.length-index);
//						getNameXPath = ary;
////						testCasePanel.setAssProperties
//					}
					selectJTbaleMod.removeRow(row);
					if(selectJTbaleMod.getRowCount()!=0 && selectJTbaleMod.getRowCount()>row){
						selectJTable.setRowSelectionInterval(0,row);
					}else if(selectJTbaleMod.getRowCount()!=0 && selectJTbaleMod.getRowCount()<=row){
						selectJTable.setRowSelectionInterval(0,row - 1);
					}
					selectJTable.setModel(selectJTbaleMod);
				}
			}
		});
		
		insertExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(editTestCase.getSelectedRow()!=-1){
					int row = editTestCase.getSelectedRow();
					String[] steps = testCase.getText().split("\n");
					for(String evertStep:steps){
						model.insertRow(row, new Object[]{evertStep});
					}
					
//					model.insertRow(row, new Object[]{testCase.getText()});
					editTestCase.setModel(model);
					
					editTestCase.setRowSelectionInterval(0,row);

					executeStep(testCase.getText(),false);
					testCase.setText("");
				}
				setAddExpectButton(false);
			}
		});
		
		
		executeAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取当前激活的TAB面板
				Component selectComponent = tabbedPane.getSelectedComponent();
				// 转换类型
				JScrollPane selectComponentJScrollPane = (JScrollPane) selectComponent;
				// 获取当前TAB面板中的组件，并且转换类型
				JTable selectJTable = ((JTable) selectComponentJScrollPane.getViewport().getView());
				// 获取组件对应的类
				DefaultTableModel selectJTbaleMod = paneMode.get(selectJTable);
				
				String step = null;
				if(selectJTable.getRowCount()!=0){
					for(int i = 0 ;i<selectJTbaleMod.getRowCount();i++){	
						if(null==step){
							step = (String) selectJTbaleMod.getValueAt(i, 0);
						}else{
							step = step + "\n" + (String) selectJTbaleMod.getValueAt(i, 0);
						}
					}
					executeStep(step,true);
					
				}
				
			}
		});
		
		moveTestCase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String steps = null;
				if(editTestCase.getRowCount()!=0){
					steps = (String) model.getValueAt(0, 0);
					for(int i = 1 ;i<model.getRowCount();i++){
						steps =  steps + "\n" +(String) model.getValueAt(i, 0);
					}
					testCasePanel.setTestCaseStep(steps);

				}
				steps = null;
				if(editExpect.getRowCount()!=0){
					steps = (String) expectModel.getValueAt(0, 0);
					for(int i = 1 ;i<expectModel.getRowCount();i++){
						steps =  steps + "\n" +(String) expectModel.getValueAt(i, 0);
					}
				}
				testCasePanel.setExpectResult(steps);
				testCasePanel.setAssProperties(TransformationMap.transformationByMap(xpathName));
			}
		});
		
		moveEditTestCase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] steps = testCasePanel.getSelectStep();
				if(steps!=null){
					for(String evertStep:steps){
						model.addRow(new Object[]{evertStep});
					}
					editTestCase.setModel(model);
				}
				steps = testCasePanel.getExceptResultStep();
				if(steps!=null){
					if (steps.length != 0){
						if (!steps[0].equals("")){
							for(String evertStep:steps){
								expectModel.addRow(new Object[]{evertStep});
							}
							editExpect.setModel(expectModel);
						}
					}
				}
				String assPro = testCasePanel.getSelectAssProperties();
				if(null != assPro && !assPro.isEmpty()){
					xpathName = TransformationMap.transformationByString(assPro);
					Print.log("获取关联属性:" + xpathName.size(),0);
				}else{
					Print.log("未获取获取关联属性",0);
				}
			}
		});

		testCase.addKeyListener(new KeyListener() {
			
			private boolean isTyped = false;
			//键入
			@Override
			public void keyTyped(KeyEvent e) {
				isTyped = true;
				if (RegExp.findCharacters(testCase.getText(), "^验证:")){
					addExpectResult.setEnabled(true);
				}else{
					addExpectResult.setEnabled(false);
				}
			}
			//释放
			@Override
			public void keyReleased(KeyEvent e) {
				if(isTyped){
					int care = testCase.getCaretPosition();
					setKeyWordHigh();
					testCase.setCaretPosition(care);
				}
				isTyped = false;
				if (RegExp.findCharacters(testCase.getText(), "^验证:")){
					addExpectResult.setEnabled(true);
				}else{
					addExpectResult.setEnabled(false);
				}
			}
			//按下
			@Override
			public void keyPressed(KeyEvent e) {
				isTyped = false;
				if (RegExp.findCharacters(testCase.getText(), "^验证:")){
					addExpectResult.setEnabled(true);
				}else{
					addExpectResult.setEnabled(false);
				}
			}
		});
//			
		//基本设置
//		jframe.setAlwaysOnTop(true);
		jframe.setResizable(false);
		jframe.validate();
		jframe.setVisible(true);
		
		testCasePanel.loadParameters();
	}
	
	/**
	 * 增加面板
	 * @param name
	 * @param cp
	 */
	public void addPanel(String name, CustomPanel cp){
		panelSet.put(name, cp);
		cp.setVisible(false);
		refreshPanel();
	}
	
	
	/**
	 * 刷新面板显示
	 */
	private void refreshPanel(){
		int y = 5;
		for(final String name : panelSet.keySet()){
			JButton button = new JButton(name);
			button.setVisible(true);
			button.setMargin(new Insets(0,0,0,0));
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					
					if(null != isShowPanel){
						isShowPanel.setVisible(false);
					}
					
					panelSet.get(name).setVisible(true);
					isShowPanel = panelSet.get(name);
				}
			});
			setBoundsAt(button, 303, y, 46, 20);
			setBoundsAt(panelSet.get(name),350, 0, 635, 395);
			y+=25;
		}
		jframe.repaint();
	}
	
	
	
	/**
	 * 设置关键字高亮
	 */
	private static void setKeyWordHigh(){
		String content = testCase.getText();
		String value= RegExp.splitKeyWord(content)[0];
		if(keyWordDriver.isKeyWord(value)){
			setStyleByKey(value,Color.GREEN.darker(),0);
			isVerKeyWord = true;
		}else{
			setStyleByKey(value,Color.RED,0);
			isVerKeyWord = false;
		}
		int customLenth = content.length() - value.length();
		if(customLenth>0){
			String customContent = content.substring(value.length());
			setStyleByKey(customContent,Color.BLACK,value.length());
		}
	}

	private static void setStyleByKey(String key,Color color,int startPos){

	
		SimpleAttributeSet attrSet  = setDocsStyle(color,true);
		try {
			testCase.getDocument().remove(startPos, key.length());
			testCase.getDocument().insertString(startPos, key, attrSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		


	}
	
	
	/**
	 * 样式的设置
	 * @param col
	 * @param bold
	 * @return
	 */
	private static SimpleAttributeSet setDocsStyle(Color col, boolean bold) {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// 颜色
		StyleConstants.setForeground(attrSet, col);
		// 字体是否加粗
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}
		return attrSet;
	}
	
	private void executeStep(String step,boolean isExeAg){
		execute.setEnabled(false);
		insertExecute.setEnabled(false);
		executeAgain.setEnabled(false);
		new Thread(new ExecuteStep(step,isExeAg)).start();
	}

	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		jframe.add(comp);
	}
	
	public static void main(String[] args){
		Print.setLevel(Print.INFO);
		new RunLog(new DebugEditFrame());
	}

	/**
	 * 设置运行框的内容
	 * @param step
	 */
	public static void setStepTextArea(String step){
		String stepTemp = step;
		String strTemp = RegExp.splitKeyWord(step)[0];
		String tabPanelName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		if (tabPanelName.equals("预期结果")){
			stepTemp = step.replaceFirst(strTemp+":", "验证:");
		}
		testCase.setText(stepTemp);
		setKeyWordHigh();
	}
	
	/**
	 * 获取运行框的内容
	 * @param step
	 * @return
	 */
	public static String getStepTextArea(){
		return testCase.getText();
	}
	
	
	public void printLog(String log,int style){
		runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date())+"-->",log + "\n\r",style);
	}
	
	
	public void printLocalLog(String log,int style){
	
	}
	
	public void debug(String log){
	
	}
	
	public void info(String log,int style){
	
	}
	
	public void error(String log){
	
	}
	
	/**
	 * 执行
	 * @author limn
	 *
	 */
	class ExecuteStep implements Runnable{
		
		public String step = null;
		private boolean  isExecuteAgain = false;
		public ExecuteStep(String step,boolean isExeAg){
			this.step = step;
			isExecuteAgain = isExeAg;
					
			
		}
		
		@Override
		public void run() {
			try{
				String[] steps = step.split("\n");
				for (int j = 0; j < steps.length; j++){
					if (RegExp.findCharacters(steps[j], ("^录入:")) 

							|| RegExp.findCharacters(steps[j], ("^录入日期:")) 
							|| RegExp.findCharacters(steps[j], ("^获取:"))
							|| RegExp.findCharacters(steps[j], ("^M录入:"))
							|| RegExp.findCharacters(steps[j], ("^添加附件:"))){
						steps[j] = steps[j] + ":" + TransformationMap.transformationByMap(xpathName);
					}
				}
				for(int i = 0;i<steps.length;i++){
					String[] key = RegExp.splitKeyWord(steps[i]);
					if(isExecuteAgain){
						editTestCase.setRowSelectionInterval(0,i);
					}
					// debug时判断关键字，验证时不执行
					//TODO
					if (!key[0].equals("验证")){
						keyWordDriver.start(key);
						if(uiViewPanel.isVisible() && uiViewPanel.isLoad){
							Common.wait(2000);
							uiViewPanel.loadUI(true);
						}
					}
					
				}
			}finally{
				execute.setEnabled(true);
				insertExecute.setEnabled(true);
				executeAgain.setEnabled(true);
			}
			
		}
		
	}
	
}











