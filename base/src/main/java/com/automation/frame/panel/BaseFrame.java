package com.automation.frame.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chris.li on 2015/10/30.
 */
public class BaseFrame extends JFrame {

    private MultiplePanel multiplePanel;

    public BaseFrame(String title) {
        this.setTitle(title);
        //基本设置
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭按钮的动作为退出 窗口
        //窗口大小
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得 显示器大小对象
//        Dimension frameSize = this.getSize();             // 获得窗口大小对象
        int screenHeight = displaySize.height;
        int screenWidth = displaySize.width;
        int frameHeight = 700;
        int frameWidth = 1000;
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2 - 10;
        if (frameHeight > screenHeight)frameHeight = screenHeight;          // 窗口的高度不能大于显示器的高度
        if (frameWidth > screenWidth)frameWidth = screenWidth;           // 窗口的宽度不能大于显示器的宽度
        this.setBounds(x, y, frameWidth, frameHeight); //窗口大小

        //this
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(layout);
        constraints.fill = constraints.BOTH; //在水平方向和垂直方向上同时调整组件大小

        //右边的panel
        CustomPanel rightPanel = new CustomPanel();
        rightPanel.setPreferredSize(new Dimension(650, 700));
        // 多面板
        multiplePanel = new MultiplePanel();
        multiplePanel.setPreferredSize(new Dimension(650, 400));
        rightPanel.addComponent(multiplePanel, 1, 1, 1, 1, 1, 0.6);

        //日志
        LogPanel logPanel = new LogPanel();
        logPanel.setPreferredSize(new Dimension(650, 300));
        rightPanel.addComponent(logPanel, 2, 1, 1, 1, 1, 0.4);
        addComponent(this, layout, constraints, rightPanel, 1, 2, 1, 1, 0.6, 1);

        //左边的panel
        CustomPanel leftPanel = new CustomPanel();
        leftPanel.setPreferredSize(new Dimension(350, 700));
        //单步执行 将panel加入frame
        ExecuteSingleStepEditPanel executeSingleStepEditPanel = new ExecuteSingleStepEditPanel();
        executeSingleStepEditPanel.setPreferredSize(new Dimension(350, 200));
        leftPanel.addComponent(executeSingleStepEditPanel, 1, 1, 1, 1, 1, 0.1);
        //用例步骤
        MultiStepExecutionPanel multiStepExecutionPanel = new MultiStepExecutionPanel();
        multiStepExecutionPanel.setPreferredSize(new Dimension(350, 500));
        leftPanel.addComponent(multiStepExecutionPanel, 2, 1, GridBagConstraints.REMAINDER, 1, 1, 0.9);
        addComponent(this, layout, constraints, leftPanel, 1, 1, 1, 1, 0.4, 1);

        this.setAlwaysOnTop(true);
        this.setVisible(true);// 设置窗口为可见的，默认为不可见

    }

    private void addComponent(Container container, GridBagLayout layout, GridBagConstraints constraints, Component componentToAdd,
                              int row, int column, int rowNum, int columnNum, double x, double y) {
        constraints.gridx = column; //组件所在列
        constraints.gridy = row; //组件所在行

        constraints.gridwidth = columnNum; //组件宽度，占几列
        constraints.gridheight = rowNum; //组件高度,占几行

        constraints.weightx = x; //组件在水平方向的拉伸能力
        constraints.weighty = y; //组件在垂直方向的拉伸能力

        layout.setConstraints(componentToAdd, constraints); //设置目标组件的约束
        container.add(componentToAdd); //在容器中添加目标组件
    }

}
