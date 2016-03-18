package com.automation.frame.panel;

import com.automation.exception.MException;
import com.automation.keyword.ExcelType;
import com.automation.keyword.KeyWordImpl;
import com.automation.tool.util.Print;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by chris.li on 2015/10/30.
 */
public class MultiStepExecutionPanel extends CustomPanel {

    //步骤编辑区
    private static JTable executionTable = new JTable();
    private static DefaultTableModel model;
    private JTabbedPane multiStepExcelPanel = new JTabbedPane();
    private JScrollPane stepJScrollStep = new JScrollPane(executionTable,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    //按钮
    JButton executeAllRow = new JButton("执行");
    JButton deleteRow = new JButton("删除");
    JButton deleteAllRow = new JButton("删除全部");
    //table数据交互
    private JButton moveTestCase = new JButton(">");
    private JButton moveEditTestCase = new JButton("<");

    //"操作步骤", "操作对象", "对象参数", "预期结果", "关联属性"
    public static String[] steps = new String[]{ExcelType.STEP, ExcelType.OBJECT, ExcelType.PARAM, ExcelType.EXPECT, ExcelType.XPATH};

    public MultiStepExecutionPanel() {
        multiStepExcelPanel.addTab("用例步骤", null, stepJScrollStep, null);
        this.addComponent(multiStepExcelPanel, 1, 1, 1, 1, 1, 1);

        CustomPanel movePanel = new CustomPanel(GridBagConstraints.NONE, new Insets(20,1,1,1), GridBagConstraints.NORTH);
        movePanel.addComponent(moveTestCase, 1, 1, 1, 1 , 1, 0);
        movePanel.addComponent(moveEditTestCase, 2, 1, 1, 1 , 1, 1);
        this.addComponent(movePanel, 1, 2, 1, 1, 0, 1);

        CustomPanel buttonPanel = new CustomPanel(GridBagConstraints.NONE, new Insets(1,30,1,1), GridBagConstraints.WEST);
        buttonPanel.addComponent(executeAllRow, 1, 1, 1, 1, 0, 1d);
        buttonPanel.addComponent(deleteRow, 1, 2, 1, 1, 0, 1d);
        buttonPanel.addComponent(deleteAllRow, 1, 3, 1, 1, 1, 1d);
        this.addComponent(buttonPanel, 2, 1, 1, 2, 1, 0);

        model = new DefaultTableModel(steps, 0);
        executionTable.setRowHeight(20);
        executionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        executionTable.setModel(model);
        executionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);


//        //只是个暂时用的例子，记得删除哦
//        addRowsExample();

        //添加[执行]的监听
        addExcuteActionListener();
        //添加[删除]的监听
        addDeleteActionListener();
        //添加[删除全部]的监听
        addDeleteAllActionListener();
        //添加向编辑用例table发送数据的监听
        addMoveTestCaseListener();
        //添加向用例步骤table发送数据的监听
        addMoveEditTestCaseListener();
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
                rowData.add(null);
            }
        }
        model.addRow(rowData);
        executionTable.setModel(model);
    }

    //插入一行到table中
    public static void insertRowToTable(Map<String, String> oneRowData) {
        if(hasSelectedRow()) {
            int rowNum = executionTable.getSelectedRow();
            Vector rowData = new Vector();
            for(String step:steps) {
                if(oneRowData.containsKey(step)) {
                    rowData.add(oneRowData.get(step));
                }else {
                    rowData.add(null);
                }
            }
            model.insertRow(rowNum, rowData);
            executionTable.setModel(model);
            executionTable.setRowSelectionInterval(0,rowNum);
        }
    }

    //判断table中是否有选中的行
    public static boolean hasSelectedRow(){
        if(executionTable.getSelectedRow()!=-1) {
            return true;
        }else {
            return false;
        }
    }

    //执行的listener
    private void addExcuteActionListener() {
        executeAllRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //执行
                if (model.getRowCount() > 0 && model.getColumnCount() > 0) {
                    List<Map<String, String>> mulityStep = new ArrayList<Map<String, String>>();
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Map<String, String> singleStep = new HashMap<String, String>();
                        //获取table中的值
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            singleStep.put(model.getColumnName(j), (String) model.getValueAt(i, j));
                        }
                        mulityStep.add(singleStep);
                    }
                    executeSteps(mulityStep);

                }
            }
        });
    }

    //多步执行
    private void executeSteps(List<Map<String, String>> mulityStep){
        executeAllRow.setEnabled(false);
        deleteRow.setEnabled(false);
        deleteAllRow.setEnabled(false);
        new Thread(new ExecuteStep(mulityStep)).start();
    }

    //多步执行的线程
    private class ExecuteStep implements Runnable {
        List<Map<String, String>> mulityStep;
        public ExecuteStep(List<Map<String, String>> mulityStep) {
            this.mulityStep = mulityStep;
        }

        @Override
        public void run() {
            // 根据关键字初始化panel
            setPanelSetting();
            int i = 0;
            KeyWordImpl keyWordImpl = new KeyWordImpl();
            keyWordImpl.setKWD(KeyWordInfoPanel.getKWD());
            for(Map<String, String> singleStep:mulityStep) {
                executionTable.setRowSelectionInterval(0,i);
                try {
                    keyWordImpl.start(singleStep);
                } catch (MException e) {

                }finally {
                    i++;
                }
            }
            setPanelSettingAfter();
            executeAllRow.setEnabled(true);
            deleteRow.setEnabled(true);
            deleteAllRow.setEnabled(true);
            Print.log("用例步骤:已执行所有步骤");
        }

        private void setPanelSettingAfter() {
            LoadBrowserPanel.setBaseWebElementButtonEnabled();
            LoadBrowserPanel.setBaseSelectWebElementTagNameEditable();
            LoadBrowserPanel.setSpecialWebElementButtonEnabled();
            LoadBrowserPanel.setSearchLocatorValueEditable();
        }

        private void setPanelSetting() {
            LoadBrowserPanel.setBaseWebElementButtonUnEnabled();
            LoadBrowserPanel.setBaseSelectWebElementTagNameUnEditable();
            LoadBrowserPanel.setBaseSelectWebElementTagNameUnEnabled();
            LoadBrowserPanel.setSpecialWebElementButtonUnEnabled();
        }


    }

    //删除的listener
    private void addDeleteActionListener() {
        deleteRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hasSelectedRow()) {
                    int rowNum = executionTable.getSelectedRow();
                    model.removeRow(rowNum);
                    if(model.getRowCount()>0 && model.getRowCount()>rowNum) {
                        executionTable.setRowSelectionInterval(0, rowNum);
                    }else if(model.getRowCount()>0) {
                        executionTable.setRowSelectionInterval(0, model.getRowCount()-1);
                    }
                    Print.log("用例步骤:删除了所选数据");
                }
            }
        });
    }

    //删除全部的listener
    private void addDeleteAllActionListener() {
        deleteAllRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAllRow();
                Print.log("用例步骤:删除了所有数据");
            }
        });
    }

    //迭代删除所有行
    private void deleteAllRow() {
        if(executionTable.getRowCount()>0) {
            model.removeRow(executionTable.getRowCount()-1);
            deleteAllRow();
        }else {
            return;
        }
    }

    //用例列表添加至编辑用例
    private void addMoveTestCaseListener() {
        moveTestCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int columnCount = model.getColumnCount();
                int rowCount = model.getRowCount();
                Map<String, String> oneRowData = null;
                for(int i=0; i<rowCount; i++) {
                    oneRowData = new HashMap<String, String>();
                    for(int j=0; j<columnCount; j++) {
                        oneRowData.put(model.getColumnName(j), (String)model.getValueAt(i, j));
                    }
                    MultiStepExcelPanel.addRowToTable(oneRowData);
                }
            }
        });
    }

    //编辑用例添加至用例列表
    private void addMoveEditTestCaseListener() {
        moveEditTestCase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Map<String, String>> rowsDatas = MultiStepExcelPanel.getRowsDatas();
                for(Map<String, String> oneRowData:rowsDatas) {
                    if(oneRowData!=null && oneRowData.size()>0) {
                        addRowToTable(oneRowData);
                    }
                }
            }
        });
    }

}
