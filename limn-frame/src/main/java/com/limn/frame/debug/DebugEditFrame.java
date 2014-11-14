package com.limn.frame.debug;



import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.util.HashMap;
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
import com.limn.frame.keyword.BaseKeyWordDriverImpl;
import com.limn.frame.keyword.BaseKeyWordType;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.panel.CustomPanel;
import com.limn.frame.panel.KeyWordPanel;
import com.limn.frame.panel.LoadBroswerPanel;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.log.LogControlInterface;
import com.limn.tool.log.LogDocument;
import com.limn.tool.log.PrintLogDriver;
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
	
	private static JTextPane testCase = new JTextPane();
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
	
	private JButton execute = new JButton("执行");
	private JButton insertExecute = new JButton("插入执行");
	private JButton executeAgain = new JButton("执行");
	private JButton deleteRow = new JButton("删除");
	
	
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel pabelLog = new JPanel();
	JPanel pabelTestCase = new JPanel();
	
	private static BaseKeyWordDriverImpl keyWordDriver = new BaseKeyWordDriverImpl();
	
	private EditTestCasePanel testCasePanel = new EditTestCasePanel();
	
	//载入面板
	private LoadBroswerPanel loadPanel = new LoadBroswerPanel();
	
	private static boolean isVerKeyWord = false;
	
	private boolean isRunning = false;
	
	private CustomPanel isShowPanel = null;
	
	private LinkedHashMap<String,CustomPanel> panelSet = new LinkedHashMap<String,CustomPanel>();
	
	// 存放XPATH与别名
	private static HashMap<String, String> xpathName = new HashMap<String, String>();
	public static HashMap<String, String> getXpathName() {
		return xpathName;
	}

	public static void setXpathName(String key, String value) {
		DebugEditFrame.xpathName.put(key, value);
	}

	/**
	 * 添加自定义面板到界面
	 * @param cp 此面板需要继承CutomPanel的类
	 */
//	public DebugEditFrame(Class<?> cp, KeyWordDriver kwd){
//		customPanel = new KeyWordPanel(cp);
//		keyWordDriver.addKeyWordDriver(key, kwd, cp);
//		init();
//	}
	

	
	public DebugEditFrame(){
		keyWordPanel = new KeyWordPanel();
		addKeyWordDriver("基础关键字", new BaseKeyWordDriverImpl(), BaseKeyWordType.class);
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
		setBoundsAt(execute,202, 80, 100, 20);
		setBoundsAt(insertExecute,82, 80, 100, 20);
		
		setBoundsAt(stepJScrollStep,2, 105, 300, 270);
		setBoundsAt(executeAgain,202, 380, 100, 20);
		setBoundsAt(deleteRow,82, 380, 100, 20);
		
		
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
		addPanel("定位", loadPanel);
		addPanel("关键字", keyWordPanel);
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

		
		

		
		writeLogPane.setPreferredSize(new Dimension(290, 234));
		writeLogPane.setEditable(false);
		
//		writeStepPane.setPreferredSize(new Dimension(244, 234));
//		writeStepPane.setEditable(false);
		
		writeLogPane.setDocument(new LogDocument(writeLogPane, 400));
		
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
				
			}
		});
		
		deleteRow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(editTestCase.getSelectedRow()!=-1){
					int row = editTestCase.getSelectedRow();
					model.removeRow(row);
					if(model.getRowCount()!=0 && model.getRowCount()>row){
						editTestCase.setRowSelectionInterval(0,row);
					}else if(model.getRowCount()!=0 && model.getRowCount()<=row){
						editTestCase.setRowSelectionInterval(0,row - 1);
					}
					editTestCase.setModel(model);
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
				
			}
		});
		
		
		executeAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String step = null;
				if(editTestCase.getRowCount()!=0){
					for(int i = 0 ;i<model.getRowCount();i++){	
						if(null==step){
							step = (String) model.getValueAt(i, 0);
						}else{
							step = step + "\n" + (String) model.getValueAt(i, 0);
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

					testCasePanel.setAssProperties(TransformationMap.transformationByMap(xpathName));
				}
				
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
				String assPro = testCasePanel.getSelectAssProperties();
				if(null != assPro && !assPro.isEmpty()){
					xpathName = TransformationMap.transformationByString(assPro);
				}
				
			}
		});

		testCase.addKeyListener(new KeyListener() {
			
			private boolean isTyped = false;
			//键入
			@Override
			public void keyTyped(KeyEvent e) {
				isTyped = true;
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
			}
			//按下
			@Override
			public void keyPressed(KeyEvent e) {
				isTyped = false;
				
			}
		});
//			
		//基本设置
		jframe.setAlwaysOnTop(true);
		jframe.setResizable(false);
		jframe.validate();
		jframe.setVisible(true);
		
		testCasePanel.loadParameters();
	}
	
	public void addPanel(String name, CustomPanel cp){
		panelSet.put(name, cp);
		cp.setVisible(false);
		refreshPanel();
	}
	
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
		isRunning = true;
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
		new DebugEditFrame();
	}

	public static void setStepTextArea(String step){
		testCase.setText(step);
		setKeyWordHigh();
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
					if (RegExp.findCharacters(steps[j], ("^录入:"))){
						steps[j] = steps[j] + ":" + TransformationMap.transformationByMap(xpathName);
					}
				}
				for(int i = 0;i<steps.length;i++){
					String[] key = RegExp.splitKeyWord(steps[i]);
					if(isExecuteAgain){
						editTestCase.setRowSelectionInterval(0,i);
					}

					keyWordDriver.start(key);
				}
			}finally{
				execute.setEnabled(true);
				insertExecute.setEnabled(true);
				executeAgain.setEnabled(true);
				isRunning = false;
			}
			
		}
		
	}
	
}











