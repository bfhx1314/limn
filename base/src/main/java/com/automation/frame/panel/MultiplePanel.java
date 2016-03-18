package com.automation.frame.panel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class MultiplePanel extends CustomPanel {

    private static JTabbedPane multiStepExcelPanel = new JTabbedPane();

    public MultiplePanel(){
        // 增加JTabbedPane控件
        init();
        this.addComponent(multiStepExcelPanel, 0, 0, 1, 1, 1d, 1d);
//        this.setBackground(Color.green);
    }

    private void init() {
        multiStepExcelPanel.addTab(" 编辑用例 ", new MultiStepExcelPanel());
        multiStepExcelPanel.addTab(" 操作页面 ", new LoadBrowserPanel());
        addKeyWordPanel();
    }

    /**
     * 添加关键字面板
     */
    public static void addKeyWordPanel(){
        int tabLen = multiStepExcelPanel.getTabCount();
        boolean flag = false;
        int index = -1;
        for(int i=0;i<tabLen;i++){
            String name = multiStepExcelPanel.getTitleAt(i);
            if (name.equals(PanelType.KEY_WORD_PANEL)){
                index = i;
                flag = true;
            }
        }
        if (flag){
            if (index != -1){
                multiStepExcelPanel.removeTabAt(index);
            }
        }
        multiStepExcelPanel.addTab(PanelType.KEY_WORD_PANEL , new KeyWordInfoPanel());
    }
}
