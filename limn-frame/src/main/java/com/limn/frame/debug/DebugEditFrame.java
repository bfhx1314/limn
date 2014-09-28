package com.limn.frame.debug;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;






import com.limn.frame.edit.EditTestCasePanel;
import com.limn.frame.keyword.KeyWordDriver;
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
	
	private JFrame jframe = new JFrame();
	
	private JTextArea testCase = new JTextArea();
	private JScrollPane testCaseJSP = new JScrollPane(testCase,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private JButton moveTestCase = new JButton(">");
	private JButton moveEditTestCase = new JButton("<");

	private EditTestCasePanel testCasePanel = new EditTestCasePanel();
	
	//自定义面板
	private CustomPanel customPanel = null;
	
	private JButton change =new JButton("切换");
	
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
	
	private KeyWordDriver keyWordDriver = null;
	/**
	 * 添加自定义面板到界面
	 * @param cp 此面板需要继承CutomPanel的类
	 */
	public DebugEditFrame(CustomPanel cp, KeyWordDriver kwd){
		customPanel = cp;
		keyWordDriver = kwd;
		init();
	}
	
//	private DebugEditFrame(){
//		super("调试编辑器");
//	}
		
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
		

		setBoundsAt(testCasePanel,350, 0, 635, 395);
		setBoundsAt(customPanel,350, 0, 635, 395);
//		testCasePanel.setVisible(false);
		customPanel.setVisible(false);
		
		setBoundsAt(change,302, 100, 46, 20);
		change.setMargin(new Insets(0,0,0,0));
		
		//切换自定义的面板
		change.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(customPanel.isVisible()){
					testCasePanel.setVisible(true);
					customPanel.setVisible(false);
				}else{
					testCasePanel.setVisible(false);
					customPanel.setVisible(true);
				}
				
			}
		});
		
		setBoundsAt(moveTestCase,302, 200, 46, 20);
		setBoundsAt(moveEditTestCase,302, 250, 46, 20);

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
				
				executeStep(testCase.getText());
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

					executeStep(testCase.getText());
					testCase.setText("");
				}
				
			}
		});
		
		
		executeAgain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				if(editTestCase.getRowCount()!=0){
					for(int i = 0 ;i<model.getRowCount();i++){
						executeStep((String) model.getValueAt(i, 0));
						editTestCase.setRowSelectionInterval(0,i);
					}
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
				
			}
		});
		
		//基本设置
		jframe.setAlwaysOnTop(true);
		jframe.setResizable(false);
		jframe.validate();
		jframe.setVisible(true);
	}
	

	private void executeStep(String step){
		String[] steps = step.split("\n");
		for(String keys : steps){
			String[] key = RegExp.splitKeyWord(keys);
			keyWordDriver.start(key);
		}
	}

	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		jframe.add(comp);
	}
	
	public static void main(String[] args){
		new DebugEditFrame(new CustomPanel(),new KeyWordDriver() {
			
			@Override
			public int start(String[] step) {
				return 0;
				// TODO Auto-generated method stub
				
			}
		});
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

}
