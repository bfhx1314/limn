package com.limn.frame.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.dom4j.DocumentException;

import com.limn.frame.panel.CustomPanel;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

/**
 * 测试用例编辑的界面 主要用于 测试用例的编写.维护. 以及界面中执行
 * 
 * @author limn
 * 
 */

public class EditTestCasePanel extends CustomPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 测试用例表格
	private JTable testCaseTable = new JTable();
	private DefaultTableModel testCaseModel = new DefaultTableModel();
	private JScrollPane testCaseJScroll = new JScrollPane();

	// 菜单栏
	private JMenuBar menubar = new JMenuBar();
	private JMenu menuFile = new JMenu("文件");

	private JLabel moduleKey = new JLabel();

	// 模块列表
	private JList<String> moduleJList = new JList<String>();
	private JScrollPane moduleJListJSP = new JScrollPane(moduleJList,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	private DefaultListModel<String> moudleModel = new DefaultListModel<String>();

	private JComboBox<String> sheetIndex = new JComboBox<String>();

	// private JButton executeSingleTestCase = new JButton("执行单步");
	//
	// private JButton executeTestCase = new JButton("执行模块");
	//
	//
	// private String tmpOpenFile = Parameter.DEFAULT_TEMP_PATH +
	// "/openfile.xml";

	// private JButton addSheet = new JButton("增加Sheet页");
	// private JButton deleteSheet = new JButton("删除Sheet页");

	private JButton addStep = new JButton("增加行");
	private JButton deleteStep = new JButton("删除行");
	private JButton saveModule = new JButton("保存用例");

	private EditTestCase eTestCase = new EditTestCase();

	private boolean isRefreshTable = true;

	private JPopupMenu rightMenu = new JPopupMenu();

	private boolean isEdit = false;

	private File fileName = null;

	private boolean isOpenExcel = false;

	public EditTestCasePanel() {

		setLayout(null);

		JLabel jSheet = new JLabel("Sheet页");
		JLabel jModuleList = new JLabel("模块列表:");

		menubar.add(getMenuFile());

		// 菜单栏
		setBoundsAt(menubar, 0, 3, 40, 25);

		// sheet
		setBoundsAt(jSheet, 280, 3, 60, 25);
		setBoundsAt(sheetIndex, 345, 5, 60, 20);

		// 模块列表

		setBoundsAt(moduleKey, 0, 35, 100, 20);
		setBoundsAt(jModuleList, 50, 3, 60, 25);
		setBoundsAt(moduleJListJSP, 110, 3, 160, 60);
		//
		// setBoundsAt(addSheet,420,5,80,20);
		// addSheet.setMargin(new Insets(0,0,0,0));

		// setBoundsAt(deleteSheet,510,5,80,20);
		// deleteSheet.setMargin(new Insets(0,0,0,0));

		// setBoundsAt(executeSingleTestCase,440, 5, 80, 20);
		// executeSingleTestCase.setMargin(new Insets(0,0,0,0));

		// setBoundsAt(executeTestCase,530, 5, 80, 20);
		// executeTestCase.setMargin(new Insets(0,0,0,0));

		// 增加行
		setBoundsAt(addStep, 290, 35, 80, 20);
		addStep.setMargin(new Insets(0, 0, 0, 0));

		// 删除行
		setBoundsAt(deleteStep, 375, 35, 80, 20);
		deleteStep.setMargin(new Insets(0, 0, 0, 0));

		// 保存
		setBoundsAt(saveModule, 460, 35, 80, 20);
		saveModule.setMargin(new Insets(0, 0, 0, 0));

		testCaseJScroll.setViewportView(testCaseTable);
		
		// TestCaseTable
		setBoundsAt(testCaseJScroll, 0, 65, 635, 330);

		testCaseModel.addColumn("是否执行");
		testCaseModel.addColumn("用例编号");
		testCaseModel.addColumn("相关用例");
		testCaseModel.addColumn("用例步骤");
		testCaseModel.addColumn("预期结果");
		testCaseModel.addColumn("关联属性");
		testCaseTable.setModel(testCaseModel);

		testCaseTable.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar == KeyEvent.VK_DELETE) {
					// TODO delete键的删除事件
					// System.out.println("ccc:"+testCaseModel.getValueAt(testCaseTable.getSelectedRow(),
					// testCaseTable.getSelectedColumn()));
					// testCaseModel.setValueAt("",
					// testCaseTable.getSelectedRow(),
					// testCaseTable.getSelectedColumn());
					// System.out.println("zzz:"+testCaseModel.getValueAt(testCaseTable.getSelectedRow(),
					// testCaseTable.getSelectedColumn()));
					// // testCaseModel.fireTableDataChanged();
					// // testCaseTable.repaint();
					// // testCaseJScroll.repaint();
					// // testCaseTable.validate();
					// // if(testCaseTable.isEditing()){
					// // int row = testCaseTable.getEditingRow();
					// // int col = testCaseTable.getEditingColumn();
					// //
					// testCaseTable.getCellEditor(row,col).stopCellEditing();
					// // }
				}
			}
		});

		testCaseTable.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
		testCaseTable.setDefaultEditor(Object.class, new TableCellTextAreaEdit());

		testCaseTable.getColumnModel().getColumn(0).setCellEditor(new TableCellCheckBoxEdit());
		testCaseTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellCheckBoxRenderer());

		testCaseTable.setColumnModel(getColumn(testCaseTable, new int[] { 75, 75, 75, 200, 200, 200 }));
		
		testCaseTable.putClientProperty("terminateEditOnFocusLost",	Boolean.TRUE);
		
		testCaseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// 右键菜单 模块列表中
		moduleJList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == 3 && isOpenExcel) {
					if (moduleJList.getSelectedIndex() == -1) {
						rightMenu = getRightMenu(true);
					} else {
						rightMenu = getRightMenu(false);
					}
					rightMenu.show(moduleJList, e.getX(), e.getY());
				}

			}
		});

		// 模块选中事件
		moduleJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (moduleJList.getSelectedIndex() == -1) {
					clearTable();
				} else {

				}
				if (e.getValueIsAdjusting()) {
					setModuleList(moduleJList.getSelectedIndex());
				}

			}
		});

		// 保存
		saveModule.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveModuleCase();
			}
		});

		sheetIndex.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Print.debugLog("sheet:" + sheetIndex.getSelectedIndex(), 2);
					eTestCase.setTableSheet(sheetIndex.getSelectedIndex());
					setTestCaseModule();
				}
			}
		});

		addStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = -1;
				if (testCaseTable.getSelectedRow() != -1) {
					row = testCaseTable.getSelectedRow();
				} else {
					row = testCaseTable.getRowCount();
				}
				testCaseModel.insertRow(row, new Object[] {});

			}
		});

		deleteStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (testCaseTable.getSelectedRow() != -1) {
					int row = testCaseTable.getSelectedRow();
					deleteRow(row);

				}
			}
		});

		setUIControlEnable(false);
		// loadParameters();

	}

	/**
	 * 选择ModuleList
	 * 
	 * @param index
	 */
	private void setModuleList(int index) {
		setTestCaseByModule(index);
		moduleKey.setText((String) moudleModel.get(index));
	}

	/**
	 * 模块列表的右键菜单
	 * 
	 * @param isNew
	 *            是否有选中项目
	 * @return
	 */
	private JPopupMenu getRightMenu(Boolean isNew) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem modfily = new JMenuItem("修改名称");
		JMenuItem create = new JMenuItem("新增模块");
		JMenuItem insert = new JMenuItem("插入新增");
		JMenuItem up = new JMenuItem("模块上移");
		JMenuItem down = new JMenuItem("模块下移");
		JMenuItem delete = new JMenuItem("删除模块");
		up.setEnabled(false);
		down.setEnabled(false);
		modfily.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(
						EditTestCasePanel.this, "修改模块名称",
						moduleJList.getSelectedValue());
				if (name != null && !name.equals("")) {
					isRefreshTable = false;
					int index = moduleJList.getSelectedIndex();
					eTestCase.setModuleNameByIndex(index, name);
					setTestCaseModule();
					moduleJList.setSelectedIndex(index);
					isRefreshTable = true;
					setModuleList(index);
				}

			}
		});

		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(
						EditTestCasePanel.this, "新增模块名称");
				if (name != null) {
					addTestCaseModule(moudleModel.getSize(), name);
				}
			}
		});

		insert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(
						EditTestCasePanel.this, "新增模块名称");
				if (name != null) {
					addTestCaseModule(moduleJList.getSelectedIndex(), name);
					setTestCaseByModule(moduleJList.getSelectedIndex() + 1);
					moduleKey
							.setText(moduleJList.getSelectedValue().toString());
				}
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteTestCaseModule();
			}
		});

		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				upTestCasemodule();

			}
		});

		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				downTestCasemodule();

			}
		});
		if (isNew) {
			menu.add(create);
		} else {
			menu.add(modfily);
			menu.add(create);
			menu.add(insert);
			menu.add(up);
			menu.add(down);
			menu.add(delete);
		}
		return menu;
	}

	/**
	 * 保存页面上参数的值
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void saveParameters(String path) {
		Common.saveTemplateData("EditExcelPath", path);
	}

	public void loadParameters() {
		HashMap<String, String> hm = Common.getTemplateData();

		if (hm.containsKey("EditExcelPath")) {
			if (new File(hm.get("EditExcelPath")).exists()) {
				eTestCase.openTestCase(hm.get("EditExcelPath"));
				setUIControlEnable(true);
				setSheetList(eTestCase.getSheetRowCount());
				if (moudleModel.getSize() > 0) {
					moduleJList.setSelectedIndex(0);
					setModuleList(moduleJList.getSelectedIndex());
				}
			}
		}

	}

	private JMenu getMenuFile() {
		JMenuItem open = new JMenuItem("打开");
		JMenuItem create = new JMenuItem("新建");
		JMenuItem save = new JMenuItem("保存");
		JMenuItem saveAs = new JMenuItem("另存为");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = openFile("请选择测试用例路径", true);
				if (path != null) {
					eTestCase.openTestCase(path);
					setUIControlEnable(true);
					saveParameters(path);
					setSheetList(eTestCase.getSheetRowCount());
					moduleJList.setSelectedIndex(0);
					setModuleList(moduleJList.getSelectedIndex());
				}
			}
		});

		create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = openFile("请指定保存路径", false);

				if (path != null) {
					if (!RegExp.findCharacters(path, "\\.xls$")) {
						path = path + ".xls";
					}
					eTestCase.createBook(path);
					setUIControlEnable(true);
					setSheetList(eTestCase.getSheetRowCount());
				}

			}
		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				eTestCase.save();
			}

		});

		saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = openFile("请指定保存路径", false);

				if (path != null) {
					if (!RegExp.findCharacters(path, "\\.xls$")) {
						path = path + ".xls";
					}
					eTestCase.setSavePath(path);
					eTestCase.save();
				}

			}
		});

		menuFile.add(open);
		menuFile.add(create);
		menuFile.add(save);
		menuFile.add(saveAs);
		return menuFile;
	}

	private TableColumnModel getColumn(JTable table, int[] width) {
		TableColumnModel columns = table.getColumnModel();
		for (int i = 0; i < width.length; i++) {
			TableColumn column = columns.getColumn(i);
			column.setPreferredWidth(width[i]);
		}
		return columns;
	}

	private void setBoundsAt(Component comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}

	/**
	 * 设置模块 根据 index 从1开始
	 * 
	 * @param moduleIndex
	 */
	private void setTestCaseByModule(int moduleIndex) {
		if (isRefreshTable) {
			clearTable();
			String[][] testCaseStep = eTestCase
					.getModuleTestCaseByIndex(moduleIndex);
			for (int i = 0; i < testCaseStep.length; i++) {
				testCaseModel.insertRow(i, new Object[] { testCaseStep[i][0],
						testCaseStep[i][1], testCaseStep[i][2],
						testCaseStep[i][3], testCaseStep[i][4], 
						testCaseStep[i][5] });
			}
			testCaseTable.setModel(testCaseModel);
		}

	}

	/**
	 * 打开测试用例的xls
	 * 
	 * @return
	 */
	private String openFile(String title, boolean isOpen) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (fileName != null) {
			fileChooser.setSelectedFile(fileName);
		} else if (new File(Parameter.TMP + "/userlike/openFilePath.txt")
				.exists()) {
			fileName = new File(readFile(Parameter.TMP
					+ "/userlike/openFilePath.txt"));
			fileChooser.setSelectedFile(fileName);
		}

		fileChooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "*.xls";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return f.getName().endsWith(".xls");
				}
			}
		});
		int path;
		if (isOpen) {
			path = fileChooser.showOpenDialog(EditTestCasePanel.this);
		} else {
			path = fileChooser.showSaveDialog(EditTestCasePanel.this);
		}
		if (path == JFileChooser.APPROVE_OPTION) {
			String paths = fileChooser.getSelectedFile().getAbsolutePath();
			fileName = new File(paths);
			writeFile(paths);
			return paths;
		} else {
			return null;
		}
	}

	private void setTestCaseModule() {
		moudleModel.removeAllElements();
		for (int i = 0; i < eTestCase.getModuleCount(); i++) {
			moudleModel
					.addElement(eTestCase.getModuleNameByIndex(i) == "" ? "未命名"
							: eTestCase.getModuleNameByIndex(i));
		}
		moduleJList.setModel(moudleModel);
	}

	private void addTestCaseModule(int index, String element) {

		eTestCase.addModuleCase(index, element);
		saveModuleCase();
		moudleModel.add(index, element);
		moduleJList.setModel(moudleModel);
		moduleJList.ensureIndexIsVisible(index);
		moduleJList.setSelectedIndex(index);

	}

	private void deleteTestCaseModule() {

		boolean isDeleted = eTestCase.deleteModuleCase(moduleJList
				.getSelectedIndex());
		if (isDeleted) {
			moudleModel.removeElementAt(moduleJList.getSelectedIndex());
			moduleJList.setModel(moudleModel);
			moduleJList.setSelectedIndex(moduleJList.getSelectedIndex());
		}

	}

	private void upTestCasemodule() {
		try {
			eTestCase.upModuleCase(moduleJList.getSelectedIndex());
		} catch (FileNotFoundException e) {
			errorDialog(e.getMessage());
			e.printStackTrace();
		}
		String up = moduleJList.getSelectedValue().toString();
		int upIndex = moduleJList.getSelectedIndex();
		moudleModel.removeElementAt(upIndex);
		moudleModel.add(upIndex - 1, up);
		moduleJList.setModel(moudleModel);
		moduleJList.setSelectedIndex(upIndex - 1);
		setModuleList(upIndex - 1);
	}

	private void downTestCasemodule() {
		try {
			eTestCase.upModuleCase(moduleJList.getSelectedIndex() + 1);
		} catch (FileNotFoundException e) {
			errorDialog(e.getMessage());
			e.printStackTrace();
		}
		String up = moduleJList.getSelectedValue().toString();
		int upIndex = moduleJList.getSelectedIndex();
		moudleModel.removeElementAt(upIndex);
		moudleModel.add(upIndex + 1, up);
		moduleJList.setModel(moudleModel);
		moduleJList.setSelectedIndex(upIndex + 1);
		setModuleList(upIndex + 2);
	}

	private void setSheetList(int itemCount) {
		sheetIndex.removeAllItems();
		for (int i = 1; i <= itemCount; i++) {
			sheetIndex.addItem(String.valueOf(i));
		}
	}

	private void clearTable() {
		int rowCount = testCaseModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			testCaseModel.removeRow(0);
		}
		testCaseTable.setModel(testCaseModel);
	}

	private void deleteRow(int index) {
		testCaseModel.removeRow(index);
		if (testCaseModel.getRowCount() != 0
				&& testCaseModel.getRowCount() > index) {
			testCaseTable.setRowSelectionInterval(index, index);
		} else if (testCaseModel.getRowCount() != 0
				&& testCaseModel.getRowCount() <= index) {
			testCaseTable.setRowSelectionInterval(index - 1, index - 1);
		}
		testCaseTable.setModel(testCaseModel);
	}

	private void saveModuleCase() {
		if (moduleJList.getSelectedIndex() != -1) {
			int rowCount = testCaseTable.getRowCount();
			String[][] testCase = new String[rowCount][6];
			for (int i = 0; i < rowCount; i++) {
				testCase[i][0] = testCaseTable.getValueAt(i, 0) == null ? "" : testCaseTable.getValueAt(i, 0).toString();
				testCase[i][1] = testCaseTable.getValueAt(i, 1) == null ? "" : testCaseTable.getValueAt(i, 1).toString();
				testCase[i][2] = testCaseTable.getValueAt(i, 2) == null ? "" : testCaseTable.getValueAt(i, 2).toString();
				testCase[i][3] = testCaseTable.getValueAt(i, 3) == null ? "" : testCaseTable.getValueAt(i, 3).toString();
				testCase[i][4] = testCaseTable.getValueAt(i, 4) == null ? "" : testCaseTable.getValueAt(i, 4).toString();
				testCase[i][5] = testCaseTable.getValueAt(i, 5) == null ? "" : testCaseTable.getValueAt(i, 5).toString();
			}
			eTestCase.saveModuleCase(moduleJList.getSelectedIndex(), testCase);
		}

	}

	private String readFile(String filePath) {
		StringBuilder sbr = new StringBuilder();
		String str = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((str = br.readLine()) != null) {
				sbr.append(str + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbr.toString();
	}

	private void writeFile(String content) {
		String path = Parameter.TMP + "/userlike";
		if (!new File(path).exists())
			new File(path).mkdirs();
		try {
			FileWriter fw = new FileWriter(path + "/openFilePath.txt");
			fw.write(content);
			fw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void errorDialog(String message) {
		JOptionPane.showMessageDialog(EditTestCasePanel.this, message);
	}

	/**
	 * 返回测试用例
	 * @return
	 */
	public String[] getSelectStep() {
		String steps = null;
		int row = testCaseTable.getSelectedRow();
		if (row != -1) {
			steps = (String) testCaseTable.getValueAt(row, 3);
		} else {
			return null;
		}
		return steps.split("\n");
	}
	
	/**
	 * 返回关联属性
	 * @return
	 */
	public String getSelectAssProperties() {
		String steps = null;
		int row = testCaseTable.getSelectedRow();
		if (row != -1) {
			steps = (String) testCaseTable.getValueAt(row, 3);
		} else {
			return null;
		}
		return steps;
	}
	

	/**
	 * 设置用例步骤
	 * @param steps
	 */
	public void setTestCaseStep(String steps) {

		int row = testCaseTable.getSelectedRow();
		if (row != -1) {
			testCaseTable.setValueAt(steps, row, 3);
		}

	}
	
	/**
	 * 设置关联属性
	 * @param steps
	 */
	public void setAssProperties(String steps) {

		int row = testCaseTable.getSelectedRow();
		if (row != -1) {
			testCaseTable.setValueAt(steps, row, 5);
		}

	}

	/**
	 * 设置界面按钮是否可以点击
	 * 
	 * @param isOpen
	 */
	private void setUIControlEnable(boolean isOpen) {
		isOpenExcel = isOpen;
		addStep.setEnabled(isOpen);
		deleteStep.setEnabled(isOpen);
		saveModule.setEnabled(isOpen);
		sheetIndex.setEnabled(isOpen);
	}

	class TableCellTextAreaEdit extends JTextArea implements TableCellEditor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		transient protected ChangeEvent changeEvent = null;

		private String keyEvent = null;

		private EventObject event = null;

		private JTable table = null;

		private int row = 0;

		private int sourceHeight = 0;

		// public TableCellTextAreaEdit(){
		// addKeyListener(new KeyAdapter() {
		// public void keyTyped(KeyEvent e) {
		// int keyChar = e.getKeyChar();
		// Print.log("xxxx", 2);
		// if (keyChar ==KeyEvent.VK_DELETE) {
		// setText("");
		// }
		// }
		// });
		// }

		@Override
		public Object getCellEditorValue() {
			return this.getText();
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {

			if (anEvent instanceof KeyEvent) {

				// if (((KeyEvent) anEvent).getKeyChar() ==KeyEvent.VK_DELETE) {
				// System.out.println(getCellEditorValue());
				// this.setText("");
				// }
				//

				// keyEvent = String.valueOf(((KeyEvent) anEvent).getKeyChar());
				return true;
			} else {
				// anEvent instanceof MouseEvent
				if (((MouseEvent) anEvent).getClickCount() == 2) {
					return true;
				} else if (event == null) {
					event = anEvent;
					return false;
				} else if (event == anEvent) {
					return true;
				} else {
					return false;
				}
			}
		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		@Override
		public boolean stopCellEditing() {

			fireEditingStopped();
			isEdit = false;
			return true;
		}

		// 编辑框结束时的操作
		private void fireEditingStopped() {
			CellEditorListener listener;
			Object[] listeners = listenerList.getListenerList();
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i] == CellEditorListener.class) {
					// 之所以是i+1，是因为一个为CellEditorListener.class（Class对象），
					// 接着的是一个CellEditorListener的实例
					listener = (CellEditorListener) listeners[i + 1];
					// 让changeEvent去通知编辑器已经结束编辑
					// 在editingStopped方法中，JTable调用getCellEditorValue()取回单元格的值，
					// 并且把这个值传递给TableValues(TableModel)的setValueAt()
					listener.editingStopped(changeEvent);
				}
			}
		}

		@Override
		public void cancelCellEditing() {

		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {
			listenerList.add(CellEditorListener.class, l);

		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
			listenerList.remove(CellEditorListener.class, l);
		}

		private void setRowHeight() {
			int height = this.getRowHeight() * this.getLineCount();
			if (height > sourceHeight) {
				table.setRowHeight(row, height);
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.table = table;
			this.row = row;
			isEdit = true;
			setLineWrap(true);
			sourceHeight = table.getRowHeight(row);
			JScrollPane stepJScrollStep = new JScrollPane(this,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			this.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					setRowHeight();

				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					setRowHeight();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {

				}
			});
			;

			if (keyEvent != null) {
				// this.setRequestFocusEnabled(true);
				// this.setText(keyEvent);
			} else {
				if (value == null) {
					value = "";
				}
				this.setText(value + "");
			}
			if (column > 2) {
				return stepJScrollStep;
			} else {
				return this;
			}

		}

	}

	class TableCellCheckBoxEdit extends JCheckBox implements TableCellEditor {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Object getCellEditorValue() {
			return this.isSelected() ? "1" : "0";
		}

		@Override
		public boolean isCellEditable(EventObject anEvent) {
			return true;

		}

		@Override
		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		@Override
		public boolean stopCellEditing() {

			fireEditingStopped();
			return true;
		}

		// 编辑框结束时的操作
		private void fireEditingStopped() {
			CellEditorListener listener;
			Object[] listeners = listenerList.getListenerList();
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i] == CellEditorListener.class) {
					// 之所以是i+1，是因为一个为CellEditorListener.class（Class对象），
					// 接着的是一个CellEditorListener的实例
					listener = (CellEditorListener) listeners[i + 1];
					// 让changeEvent去通知编辑器已经结束编辑
					// 在editingStopped方法中，JTable调用getCellEditorValue()取回单元格的值，
					// 并且把这个值传递给TableValues(TableModel)的setValueAt()
					listener.editingStopped(changeEvent);
				}
			}
		}

		@Override
		public void cancelCellEditing() {

		}

		@Override
		public void addCellEditorListener(CellEditorListener l) {
			listenerList.add(CellEditorListener.class, l);

		}

		@Override
		public void removeCellEditorListener(CellEditorListener l) {
			listenerList.remove(CellEditorListener.class, l);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (value == null) {
				setSelected(false);
			} else if (value.equals("1")) {
				setSelected(true);
			} else {
				setSelected(false);
			}
			return this;
		}

	}

	class TableCellCheckBoxRenderer extends JCheckBox implements
			TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			if (value == null) {
				setSelected(false);
			} else if (value.equals("1")) {
				setSelected(true);
			} else {
				setSelected(false);
			}
			return this;
		}
	}

	class TableCellTextAreaRenderer extends JTextArea implements
			TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Border focusBorder = new LineBorder(Color.BLACK, 1);

		public TableCellTextAreaRenderer() {
			// setLineWrap(true);
			// setWrapStyleWord(true);

		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			// 计算当下行的最佳高度
			int maxPreferredHeight = 20;

			// int diffLineCount = 0;
			//
			// String[] steps = value.toString().split("\n");
			//
			// for (String text:steps) {
			// if(text.length()>20){
			// diffLineCount++;
			// }
			// }

			// String text = ((String) value).replace("\n", "");
			// int line = text.length() / 15;
			// StringBuffer buffer = new StringBuffer();
			// int x;
			// for (x = 0; x < line; x++) {
			// buffer.append(text.substring(x * 15, (x + 1) * 15));
			// buffer.append("\n");
			// }
			// buffer.append(text.substring(x * 15));
			// value = buffer.toString();
			//

			// System.out.println("大于20个字符 " + diffLineCount);

			for (int i = 0; i < table.getColumnCount(); i++) {
				setText(table.getValueAt(row, i) + "");
				// setText(value.toString());
				setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
				// 取行高度 * 行数
				// System.out.println(this.getRows() +" : "+
				// this.getLineCount());

				maxPreferredHeight = Math.max(maxPreferredHeight,
						this.getRowHeight() * this.getLineCount());
			}

			// 给焦点的行设置背景
			if (row == table.getSelectedRow()) {
				setBackground(Color.lightGray);
			} else {
				setBackground(Color.white);
			}

			// 给焦点的单元格 设置边框
			if (hasFocus) {
				setBorder(focusBorder);
			} else {
				setBorder(null);
			}

			if (table.getRowHeight(row) != maxPreferredHeight && !isEdit) { // 少了这行则处理器瞎忙
				table.setRowHeight(row, maxPreferredHeight);
			}

			setText(value == null ? "" : value.toString());

			return this;
		}
	}

}

