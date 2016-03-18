package com.automation.frame.panel;

import com.automation.exception.RunException;
import com.automation.keyword.ExcelType;
import com.automation.tool.excelEdit.ExcelEditor;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.StringUtil;
import com.automation.tool.util.Common;
import com.automation.tool.util.Print;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by chris.li on 2015/10/30.
 */
public class MultiStepExcelPanel extends CustomPanel {
    // 测试用例表格
    private static JTable testCaseTable = new JTable();
    private static DefaultTableModel testCaseModel;
    private JScrollPane testCaseJScroll = new JScrollPane();
    // 菜单栏
    private JMenuBar menuBar = new JMenuBar();
    // 用例列表
    private JComboBox<String> caseList = new JComboBox<String>();
    private JMenuBar caseBar = new JMenuBar();
    private JLabel currentCase = new JLabel("");
    //sheet页
    private JComboBox<String> sheetList = new JComboBox<String>();
    private JMenuBar sheetBar = new JMenuBar();
    private JLabel currentSheet = new JLabel("");
    //增加行
    private JButton addStep = new JButton("增加行");
    //删除行
    private JButton deleteStep = new JButton("删除行");
    //删除全部
    private JButton deleteAllStep = new JButton("删除全部");
    //保存用例
    private JButton saveModule = new JButton("保存用例");
    private File fileName = null;
    //excel处理
    private ExcelEditor excelEditor;
    //是否执行 操作步骤	操作对象	对象参数	预期结果	关联属性
    public static String[] steps = new String[]{ExcelType.DOES_RUN, ExcelType.STEP, ExcelType.OBJECT,
                                            ExcelType.PARAM, ExcelType.EXPECT, ExcelType.XPATH};
    private boolean tableModelChange = false;
    //0--addCreateSheetListener;1--addModifySheetListener;2--addCreateCaseListener;3--addModifyCaseListener;
    //4--addOpenListener;5--addCreateListener;6--addSheetListListener;7--addCaseListListener;
    //-1--初始状态；-2--取消状态；
    private int executing = -1;

    private enum executeMode  {
        saveCaseAndExecute, executeWhitOutSave, cancel
    }

    public MultiStepExcelPanel() {
        //上部分的功能panel
        CustomPanel functionPanel = new CustomPanel(GridBagConstraints.NONE, new Insets(0,0,0,0), GridBagConstraints.NORTHWEST);
        //button 大小
        Dimension menuSize = new Dimension(60, 25);
        Dimension size = new Dimension(120, 25);
        //文件
        CustomPanel menuPanel = new CustomPanel();
        setSize(menuSize, menuBar);
        addComponent(menuBar, newGridBagConstraints(GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), null), getMenuFile(), 1, 1, 1, 1, 1d, 1d);
        menuPanel.addComponent(menuBar,  1, 1, 1, 1, 1, 1);
        functionPanel.addComponent(menuPanel, 1, 1, 1, 1, 0.1, 0.5);
        //sheet页
        CustomPanel sheetPanel = new CustomPanel();
        setSize(menuSize, sheetBar);
        addComponent(sheetBar, newGridBagConstraints(GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), null), getSheetMenu(), 1, 1, 1, 1, 1d, 1d);
//        JLabel jSheet = new JLabel("Sheet页");
//        jSheet.setHorizontalTextPosition(SwingConstants.RIGHT);
        sheetPanel.addComponent(sheetBar, 1, 1, 1, 1, 0.03, 1d);
        setSize(size, sheetList);
        sheetPanel.addComponent(sheetList, 1, 2, 1, 1, 0.97, 1d);
        functionPanel.addComponent(sheetPanel, 1, 2, 1, 2, 0.4, 0.5);
        //用例列表
        CustomPanel modulePanel = new CustomPanel();
//        JLabel jModuleList = new JLabel("用例列表");
        setSize(menuSize, caseBar);
        addComponent(caseBar, newGridBagConstraints(GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), null), getCaseMenu(), 1, 1, 1, 1, 1d, 1d);
        modulePanel.addComponent(caseBar, 1, 1, 1, 1, 0.03, 1d);
        caseList.setMinimumSize(size);
        setSize(size, caseList);
        modulePanel.addComponent(caseList, 1, 2, 1, 1, 0.97, 1d);
        functionPanel.addComponent(modulePanel, 1, 4, 1, 2, 0.5, 0.5);

        Dimension buttonSize = new Dimension(90, 25);
        //当前用例currentCase
        functionPanel.addComponent(currentCase, 2, 1, 1, 1, 0.1, 0.5);
        // 增加行
        setSize(buttonSize, addStep);
        functionPanel.addComponent(addStep, 2, 2, 1, 1, 0.1, 0.5);
        // 删除行
        setSize(buttonSize, deleteStep);
        functionPanel.addComponent(deleteStep, 2, 3, 1, 1, 0.1, 0.5);
        // 删除全部
        setSize(buttonSize, deleteAllStep);
        functionPanel.addComponent(deleteAllStep, 2, 4, 1, 1, 0.1, 0.5);
        // 保存
        setSize(buttonSize, saveModule);
        functionPanel.addComponent(saveModule, 2, 5, 1, 1, 0.1, 0.5);
        //将上部分放入panel中
        this.addComponent(functionPanel, 1, 1, 1, 1, 1d, 0d);

        //table
        testCaseJScroll.setViewportView(testCaseTable);
        testCaseJScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        testCaseJScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        testCaseJScroll.setMaximumSize(new Dimension(testCaseJScroll.getWidth(), 200));

        //将TestCaseTable放入panel中
        this.addComponent(testCaseJScroll, 2, 1, 1, 1, 1d, 1d);

        //实现table
        testCaseModel = new DefaultTableModel(steps, 0);
        testCaseTable.setRowHeight(20);
        testCaseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        testCaseTable.setModel(testCaseModel);
        //将第一列的值设为下拉框
        String[] doesRun = { "Y", "N"};
        JComboBox doesRunBox = new JComboBox(doesRun);
        doesRunBox.setSelectedIndex(0);
        testCaseTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(doesRunBox));
        testCaseTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

//        //只是个暂时用的例子，记得删除哦
//        addRowsExample();

        //增加下拉框的监听
        addSheetListListener();
        addCaseListListener();

        //table变动的监听
        addTableModelChangeListener();

        //添加按钮的监听
        addAddStepListener(); //增加行
        addDeleteStepListener(); //删除
        addDeleteAllStepListener(); //删除全部
        addSaveModuleListener(); //保存用例
    }


    private void setSize(Dimension size, Container container) {
        container.setPreferredSize(size);
        container.setMaximumSize(size);
        container.setMinimumSize(size);
        container.setSize(size);
    }

    public void addRowsExample() {
        Map<String, String> rows = new HashMap<String, String>();
        rows.put(ExcelType.STEP, "启动浏览器");
        rows.put(ExcelType.PARAM, "http://apollo.envisioncn.com/");
        rows.put(ExcelType.XPATH, "1");
        rows.put(ExcelType.EXPECT, "");
        addRowToTable(rows);
        rows.put(ExcelType.XPATH, "2");
        addRowToTable(rows);
        rows.put(ExcelType.XPATH, "3");
        addRowToTable(rows);
    }

    //添加一行到table中
    public static void addRowToTable(Map<String, String> oneRowData) {
        Vector rowData = new Vector();
        for(String step:steps) {
            if(oneRowData.containsKey(step)) {
                rowData.add(oneRowData.get(step));
            }else {
                if(step.equals(ExcelType.DOES_RUN)) {
                    rowData.add("Y");
                }else {
                    rowData.add(null);
                }
            }
        }
        testCaseModel.addRow(rowData);
        testCaseTable.setModel(testCaseModel);
    }

    //插入一行到table中
    public static void insertRowToTable(Map<String, String> oneRowData) {
        if(hasSelectedRow()) {
            int rowNum = testCaseTable.getSelectedRow();
            Vector rowData = new Vector();
            for(String step:steps) {
                if(oneRowData.containsKey(step)) {
                    rowData.add(oneRowData.get(step));
                }else {
                    rowData.add(null);
                }
            }
            testCaseModel.insertRow(rowNum, rowData);
            testCaseTable.setModel(testCaseModel);
            testCaseTable.setRowSelectionInterval(0,rowNum);
        }
    }

    //获取table中所有的数据
    public static List<Map<String, String>> getRowsDatas() {
        List<Map<String, String>> rowsDatas = new ArrayList<Map<String, String>>();
        int columnCount = testCaseModel.getColumnCount();
        int rowCount = testCaseModel.getRowCount();
        Map<String, String> oneRowData = null;
        for(int i=0; i<rowCount; i++) {
            oneRowData = new HashMap<String, String>();
            for(int j=0; j<columnCount; j++) {
                oneRowData.put(testCaseModel.getColumnName(j), (String)testCaseModel.getValueAt(i, j));
            }
            rowsDatas.add(oneRowData);
        }
        return rowsDatas;
    }

    //获取文件的按钮下拉框
    private JMenu getMenuFile() {
        JMenu menuFile = new JMenu("文件");
        JMenuItem open = new JMenuItem("打开");
        JMenuItem create = new JMenuItem("新建");
        JMenuItem save = new JMenuItem("保存");
        JMenuItem saveAs = new JMenuItem("另存为");

        //添加打开的监听
        addOpenListener(open);
        //添加新建的监听
        addCreateListener(create);
//        //添加保存的监听
//        addSaveListener();
//        //添加另存为的监听
//        addSaveAsListener();

        menuFile.add(open);
        menuFile.add(create);
//        menuFile.add(save);
//        menuFile.add(saveAs);
        menuFile.setHorizontalTextPosition(SwingConstants.CENTER);
        return menuFile;
    }

    //获取sheet的按钮下拉框
    private JMenu getSheetMenu() {
        JMenu sheetMenu = new JMenu("Sheet页");
        JMenuItem create = new JMenuItem("新建");
        JMenuItem modify = new JMenuItem("修改");

        //添加新建的监听
        addCreateSheetListener(create);
        //添加修改的监听
        addModifySheetListener(modify);

        sheetMenu.add(create);
        sheetMenu.add(modify);
        sheetMenu.setHorizontalTextPosition(SwingConstants.CENTER);
        return sheetMenu;
    }

    private executeMode optionConfirm() {
        if(tableModelChange) {
            //表格数据已修改，选择性执行动作
            Object[] options = {"保存用例后执行", "直接执行", "取消"};
            int result = JOptionPane.showOptionDialog(MultiStepExcelPanel.this,
                    "用例修改后尚未保存，确认保存用例吗？","提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if(0==result) {
                return executeMode.saveCaseAndExecute;
            }else if(1==result) {
                return executeMode.executeWhitOutSave;
            }else if(2==result) {
                return executeMode.cancel;
            }else {
                Print.logRed("确认框出现异常");
                return executeMode.cancel;
            }
        }else {
            //表格数据未修改，那么直接执行动作
            return executeMode.executeWhitOutSave;
        }
    }

    //监听  新建sheet
    private void addCreateSheetListener(JMenuItem create) {
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(executing==-1) executing = 0;
            if(executing==0) {
                switch (optionConfirm()) {
                    case saveCaseAndExecute:
                        boolean flag = saveModule();
                        if(flag) {
                            tableModelChange = false;
                            createSheet();
                        }else {
                            Print.logRed("保存用例失败，未新建sheet");
                        }
                        break;
                    case executeWhitOutSave:
                        boolean created = createSheet();
                        if(created) tableModelChange = false;
                        break;
                    case cancel:
                        break;
                }
            }else {
                createSheet();
            }
            if(executing==0) executing = -1;
            }
        });
    }

    private boolean createSheet() {
        if (excelEditor != null) {
            //弹出框，提示新建
            String name = JOptionPane.showInputDialog(MultiStepExcelPanel.this, "新增sheet");
            if (!StringUtil.isEmpty(name)) {
                boolean isCreate = excelEditor.createSheet(name);
                if (isCreate) {
                    setSheetList();
                    sheetList.setSelectedItem(name);
                    currentSheet.setText(name);
                    return true;
                } else {
                    Print.logRed("新建sheet失败");
                    return false;
                }
            }else {
                Print.log("sheet名为空，取消新建");
                return false;
            }
        }else {
            Print.logRed("未打开任何文件，不能新建sheet");
            return false;
        }
    }

    //监听 修改sheet
    private void addModifySheetListener(JMenuItem modify) {
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(executing==-1) executing = 1;
            if(executing==1) {
                switch (optionConfirm()) {
                    case saveCaseAndExecute:
                        boolean flag = saveModule();
                        if(flag) {
                            tableModelChange = false;
                            modifySheet();
                        }else {
                            Print.logRed("保存用例失败，未修改sheet");
                        }
                        break;
                    case executeWhitOutSave:
                        boolean modified = modifySheet();
                        if(modified) tableModelChange = false;
                        break;
                    case cancel:
                        break;
                }
            }else {
                modifySheet();
            }
            if(executing==1) executing = -1;
            }
        });
    }

    private boolean modifySheet() {
        if (excelEditor != null) {
            //弹出框，提示新建
            String oldName = sheetList.getSelectedItem().toString();
            String name = JOptionPane.showInputDialog(MultiStepExcelPanel.this, "修改sheet名称", oldName);
            if (!StringUtil.isEmpty(name) && !name.equals(oldName)) {
                boolean isModify = excelEditor.modifySheetName(oldName, name);
                if (isModify) {
                    setSheetList();
                    sheetList.setSelectedItem(name);
                    currentSheet.setText(name);
                    return true;
                }else {
                    Print.logRed("sheet名称未改动，请检查");
                    return false;
                }
            }else {
                Print.log("sheet名称为空或未改动，取消修改");
                return false;
            }

        }else {
            Print.logRed("未打开任何文件，不能修改sheet");
            return false;
        }
    }

    //获取用例列表的按钮下拉框
    private JMenu getCaseMenu() {
        JMenu caseMenu = new JMenu("用例列表");
        JMenuItem create = new JMenuItem("新建");
        JMenuItem modify = new JMenuItem("修改");

        //添加新建的监听
        addCreateCaseListener(create);
        //添加修改的监听
        addModifyCaseListener(modify);

        caseMenu.add(create);
        caseMenu.add(modify);
        caseMenu.setHorizontalTextPosition(SwingConstants.CENTER);
        return caseMenu;
    }

    //监听 新建用例
    private void addCreateCaseListener(JMenuItem create) {
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(executing==-1) executing = 2;
            if(executing==2) {
                switch (optionConfirm()) {
                    case saveCaseAndExecute:
                        boolean flag = saveModule();
                        if(flag) {
                            tableModelChange = false;
                            createCase();
                        }else {
                            Print.logRed("保存用例失败，未新建用例");
                        }
                        break;
                    case executeWhitOutSave:
                        boolean created = createCase();
                        if(created) tableModelChange = false;
                        break;
                    case cancel:
                        break;
                }
            }else {
                createCase();
            }
            if(executing==2) executing = -1;
            }
        });
    }

    private boolean createCase() {
        if (excelEditor != null) {
            //弹出框，提示新建
            String name = JOptionPane.showInputDialog(MultiStepExcelPanel.this, "新增用例");
            if (!StringUtil.isEmpty(name)) {
                boolean isCreate = excelEditor.createCase(currentSheet.getText(), name);
                if (isCreate) {
                    setCaseList();
                    caseList.setSelectedItem(name);
                    currentCase.setText(name);
                    return true;
                }else {
                    Print.logRed("用例创建失败，请检查");
                    return false;
                }
            }else {
                Print.log("用例名称为空，取消新建");
                return false;
            }
        }else {
            Print.logRed("未打开任何文件，不能新建用例");
            return false;
        }
    }

    //监听 修改用例
    private void addModifyCaseListener(JMenuItem modify) {
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(executing==-1) executing = 3;
            if(executing==3) {
                switch (optionConfirm()) {
                    case saveCaseAndExecute:
                        boolean flag = saveModule();
                        if(flag) {
                            tableModelChange = false;
                            modifyCase();
                        }else {
                            Print.logRed("保存用例失败，未修改用例");
                        }
                        break;
                    case executeWhitOutSave:
                        boolean modified = modifyCase();
                        if(modified) tableModelChange = false;
                        break;
                    case cancel:
                        break;
                }
            }else {
                modifyCase();
            }
            if(executing==3) executing = -1;
            }
        });
    }

    private boolean modifyCase() {
        if (excelEditor != null) {
            //弹出框，提示新建
            String oldName = caseList.getSelectedItem().toString();
            String name = JOptionPane.showInputDialog(MultiStepExcelPanel.this, "修改用例", oldName);
            if (!StringUtil.isEmpty(name) && !name.equals(oldName)) {
                boolean isModify = excelEditor.modifyCase(currentSheet.getText(), oldName, name);
                if (isModify) {
                    setCaseList();
                    caseList.setSelectedItem(name);
                    currentCase.setText(name);
                    return true;
                }else {
                    Print.logRed("用例修改失败，请检查");
                    return false;
                }
            }else {
                Print.log("用例名称为空或未改动，取消修改");
                return false;
            }
        }else {
            Print.logRed("未打开任何文件，不能修改用例");
            return false;
        }
    }

    //监听 打开文件
    private void addOpenListener(JMenuItem open) {
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(executing==-1) executing = 4;
                if(executing==4) {
                    switch (optionConfirm()) {
                        case saveCaseAndExecute:
                            boolean flag = saveModule();
                            if(flag) {
                                tableModelChange = false;
                                openFile();
                            }else {
                                Print.logRed("保存用例失败，未打开文件");
                            }
                            break;
                        case executeWhitOutSave:
                            boolean opened = openFile();
                            if(opened) tableModelChange = false;
                            break;
                        case cancel:
                            break;
                    }
                }else {
                    openFile();
                }
                if(executing==4) executing = -1;
            }
        });
    }

    private boolean openFile() {
        String path = openFile("请选择测试用例路径", true);
        if (!StringUtil.isEmpty(path)) {
            excelEditor = new ExcelEditor();
            excelEditor.excelMode = 0; //标记打开模式
            //加载当前table的数据
            excelEditor.openExcelTestCase(path);
            //设置sheetList
            setSheetList();
            //设置其他功能可用
            setUIControlEnable(true);
            //将这次打开的路径保存至Template.xml
            saveParameters(path);
            excelEditor.excelMode = -1; //标记结束打开模式
            return true;
        }else {
            return false;
        }
    }

    //监听  新建文件
    private void addCreateListener(JMenuItem open) {
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if(executing==-1) executing = 5;
            if(executing==5) {
                switch (optionConfirm()) {
                    case saveCaseAndExecute:
                        boolean flag = saveModule();
                        if(flag) {
                            tableModelChange = false;
                            createFile();
                        }else {
                            Print.logRed("保存用例失败，未新建文件");
                        }
                        break;
                    case executeWhitOutSave:
                        boolean created = createFile();
                        if(created) tableModelChange = false;
                        break;
                    case cancel:
                        break;
                }
            }else {
                createFile();
            }
            if(executing==5) executing = -1;
            }
        });
    }

    private boolean createFile() {
        String path = openFile("请选择测试用例路径", false);
        if (!StringUtil.isEmpty(path)) {
            try {
                excelEditor = new ExcelEditor();
                excelEditor.excelMode = 0; //标记打开模式
                //保存文件
                excelEditor.saveFile(path);
                //加载当前table的数据
                excelEditor.openExcelTestCase(path);
                //设置sheetList
                setSheetList();
                //设置其他功能可用
                setUIControlEnable(true);
                //将这次打开的路径保存至Template.xml
                saveParameters(path);
                excelEditor.excelMode = -1; //标记结束打开模式
                return true;
            } catch (RunException ex) {
                ex.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 打开测试用例的excel文件
     *
     * @return
     */
    private String openFile(String title, boolean isOpen) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        //如果上次有打开的目录，那么默认选择该目录
        String userLikePath = Parameters.TMP + "/userlike/openFilePath.txt";
        if (fileName != null) {
            fileChooser.setSelectedFile(fileName);
        } else if (new File(userLikePath).exists()) {
            fileName = new File(readFile(userLikePath));
            fileChooser.setSelectedFile(fileName);
        }

        fileChooser.addChoosableFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*.xls and *.xlsx";
            }

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx");
                }
            }
        });
        int path;
        if (isOpen) {
            path = fileChooser.showOpenDialog(MultiStepExcelPanel.this);
        } else {
            path = fileChooser.showSaveDialog(MultiStepExcelPanel.this);
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

    //读取文件--用户偏好
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

    //写入用户偏好
    private void writeFile(String content) {
        String path = Parameters.TMP + "/userlike";
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

    //设置sheet页的值
    private void setSheetList() {
        sheetList.removeAllItems();
        for (String sheetName:excelEditor.getSheetList()) {
            sheetList.addItem(sheetName);
        }
        if(sheetList==null || sheetList.getItemCount()<=0) {
            sheetList.addItem("");
        }
        //设第一个有效sheet为默认选中
        if(!StringUtil.isEmpty(excelEditor.getCurrentSheetName())) {
            sheetList.setSelectedItem(excelEditor.getCurrentSheetName());
        }
    }

    //添加sheet页下拉框的监听
    private void addSheetListListener() {
        sheetList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //如果选择，则更新用例列表
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if(executing==-1) executing = 6;
                    if(executing==6) {
                        switch (optionConfirm()) {
                            case saveCaseAndExecute:
                                boolean flag = saveModule();
                                if(flag) {
                                    tableModelChange = false;
                                    changeSheet(e);
                                }else {
                                    Print.logRed("保存用例失败，未选择sheet");
                                }
                                break;
                            case executeWhitOutSave:
                                changeSheet(e);
                                tableModelChange = false;
                                break;
                            case cancel:
                                executing = -2;
                                sheetList.setSelectedItem(currentSheet.getText());
                                break;
                        }
                    }else if(executing!=-2) {
                        changeSheet(e);
                    }
                    if(executing==6 || executing==-2) executing = -1;
                }
            }
        });
    }

    private void changeSheet(ItemEvent e) {
        String selectedItem = e.getItem().toString();
        currentSheet.setText(selectedItem);
        setCaseList();
    }

    //设置用例列表的值
    private void setCaseList() {
        //清空用例列表
        caseList.removeAllItems();
        //如果对应的sheet有数据，那么更新用例列表
        if(excelEditor.getExcelCaseNameList()!=null
                && excelEditor.getExcelCaseNameList().get(currentSheet.getText())!=null) {
            for(String caseName:excelEditor.getExcelCaseNameList().get(currentSheet.getText())) {
                caseList.addItem(caseName);
            }
        }
        if(caseList==null || caseList.getItemCount()<=0) {
            caseList.addItem("");
        }
    }

    //添加用例列表下拉框的监听
    private void addCaseListListener() {
        caseList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            //如果选择，则更新table
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if(executing==-1) executing = 7;
                if(executing==7) {
                    switch (optionConfirm()) {
                        case saveCaseAndExecute:
                            boolean flag = saveModule();
                            if(flag) {
                                tableModelChange = false;
                                changeCase(e);
                            }else {
                                Print.logRed("保存用例失败，未选择sheet");
                            }
                            break;
                        case executeWhitOutSave:
                            changeCase(e);
                            tableModelChange = false;
                            break;
                        case cancel:
                            executing = -2;
                            caseList.setSelectedItem(currentCase.getText());
                            break;
                    }
                }else if(executing!=-2) {
                    changeCase(e);
                }
                if(executing==7 || executing==-2) executing = -1;
            }
            }
        });
    }

    private void changeCase(ItemEvent e) {
        String selectedItem = e.getItem().toString();
        currentCase.setText(selectedItem);
        addRowListToTable();
        tableModelChange = false;
    }

    //添加table的内容
    private void addRowListToTable() {
        //清空table
        deleteAllRow();
        //如果对应的用例有数据，则更新table里的数据
        if(excelEditor.getExcelCaseResult()!=null
                && excelEditor.getExcelCaseResult().get(currentSheet.getText())!=null
                && excelEditor.getExcelCaseResult().get(currentSheet.getText()).get(currentCase.getText())!=null) {
            for(Map<String, String> rowData:excelEditor.getExcelCaseResult().get(currentSheet.getText()).get(currentCase.getText())) {
                addRowToTable(rowData);
            }
        }
    }

    //迭代删除所有行
    private void deleteAllRow() {
        if(testCaseTable.getRowCount()>0) {
            testCaseModel.removeRow(testCaseTable.getRowCount()-1);
            deleteAllRow();
        }else {
            return;
        }
    }

    /**
     * 设置界面按钮是否可以点击
     *
     * @param isOpen
     */
    private void setUIControlEnable(boolean isOpen) {
        addStep.setEnabled(isOpen);
        deleteStep.setEnabled(isOpen);
        saveModule.setEnabled(isOpen);
    }

    private void addTableModelChangeListener() {
        testCaseModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                tableModelChange = true;
            }
        });
    }

    //增加行的监听
    private void addAddStepListener() {
        addStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hasSelectedRow()) {
                    insertRowToTable(new HashMap<String, String>());
                    Print.log("编辑用例:插入了一行");
                }else {
                    addRowToTable(new HashMap<String, String>());
                    Print.log("编辑用例:增加了一行");
                }
            }
        });
    }

    //监听 删除行
    private void addDeleteStepListener() {
        deleteStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hasSelectedRow()) {
                    int rowNum = testCaseTable.getSelectedRow();
                    testCaseModel.removeRow(rowNum);
                    if(testCaseModel.getRowCount()>0 && testCaseModel.getRowCount()>rowNum) {
                        testCaseTable.setRowSelectionInterval(0, rowNum);
                    }else if(testCaseModel.getRowCount()>0) {
                        testCaseTable.setRowSelectionInterval(0, testCaseModel.getRowCount()-1);
                    }
                    Print.log("编辑用例:删除了所选数据");
                }
            }
        });
    }

    //监听 删除全部
    private void addDeleteAllStepListener() {
        deleteAllStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAllRow();
                Print.log("编辑用例:删除了所有数据");
            }
        });
    }

    //判断table中是否有选中的行
    public static boolean hasSelectedRow(){
        if(testCaseTable.getSelectedRow()!=-1) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 保存用例
     * @throws IOException
     * @throws org.dom4j.DocumentException
     */
    private void saveParameters(String path) {
        Common.saveTemplateData("EditExcelPath", path);
    }

    private void addSaveModuleListener() {
        saveModule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = saveModule();
                if(flag) tableModelChange = false;
            }
        });
    }

    private boolean saveModule() {
        if(excelEditor!=null) {
            excelEditor.excelMode = 1; //标记保存模式
            boolean flag = saveModuleCase();
            excelEditor.excelMode = -1; //标记结束保存模式
            return flag;
        }else {
            Print.logRed("未打开任何的文件");
            return false;
        }
    }

    //保存用例
    private boolean saveModuleCase() {
        //异常状况不能保存
        if(StringUtil.isEmpty(currentSheet.getText())) {
            Print.logRed("sheet页未选择，请选择");
            return false;
        }
        if(StringUtil.isEmpty(currentCase.getText())) {
            Print.logRed("用例列表未选择，请选择");
            return false;
        }
        int rowCount = testCaseTable.getRowCount();
        int columnCount = testCaseModel.getColumnCount();
        if(rowCount<=0 || columnCount<=0) {
            Print.logRed("表格数据有误，请检查");
            return false;
        }

        //解析table
        List<Map<String, String>> tableCaseData = new ArrayList<Map<String, String>>();
        Map<String, String> singleRowData = null;
        for(int i=0; i<rowCount; i++) {
            singleRowData = new HashMap<String, String>();
            for(int j=0; j<Parameters.titles.length; j++) {
                singleRowData.put(Parameters.titles[j], null);
            }
            for(int k=0; k<columnCount; k++) {
                if(singleRowData.containsKey(testCaseModel.getColumnName(k))) {
                    singleRowData.put(testCaseModel.getColumnName(k), StringUtil.objectToString(testCaseModel.getValueAt(i, k)));
                }
            }
            if(singleRowData.containsKey(ExcelType.CASE_NAME)) {
                singleRowData.put(ExcelType.CASE_NAME, currentCase.getText());
            }
            tableCaseData.add(singleRowData);
        }
        //保存数据
        return excelEditor.saveModuleCase(currentSheet.getText(), currentCase.getText(), tableCaseData);
    }
}
