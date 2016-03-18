package com.automation.frame.panel;

import com.automation.keyword.KeyWordDriver;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by chris.li on 2015/10/30.
 */
public class CustomPanel extends JPanel {

    /*当前面板的容器*/
    private Container container;
    /*网格布局器*/
    private GridBagLayout layout;

    /*网格布局器的约束器*/
    private GridBagConstraints constraints ;


    /**
     * 关键字MAP
     */
    protected static LinkedHashMap<String,KeyWordDriver> KWD = new LinkedHashMap<>();
    /**
     * 关键字信息
     */
    protected static LinkedHashMap<String,Class<?>> KWDT = new LinkedHashMap<>();

    /**
     * 所有关键字
     */
    protected static LinkedList<String> keyWords = new LinkedList<>();

    public CustomPanel() {
        container = this;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        container.setLayout(layout);
        constraints.fill = constraints.BOTH; //在水平方向和垂直方向上同时调整组件大小
        constraints.insets = new Insets(3,3,3,3);
    }

    public CustomPanel(Integer fill, Insets insets, Integer anchor) {
        container = this;
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        container.setLayout(layout);
        if(fill!=null)constraints.fill = fill; //在水平方向和垂直方向上同时调整组件大小
        if(insets!=null)constraints.insets = insets;
        if(anchor!=null) constraints.anchor = anchor;
    }

    /**
     * 供panel使用
     * @param componentToAdd
     * @param row
     * @param column
     * @param rowNum
     * @param columnNum
     * @param x
     * @param y
     */
    public void addComponent(Component componentToAdd, int row, int column, int rowNum, int columnNum, double x, double y) {
        addComponent(componentToAdd, new Integer(row), new Integer(column), new Integer(rowNum), new Integer(columnNum), new Double(x), new Double(y));
    }

    /**
     * 供panel使用
     * @param componentToAdd
     * @param row
     * @param column
     * @param rowNum
     * @param columnNum
     * @param x
     * @param y
     */
    public void addComponent(Component componentToAdd, Integer row, Integer column, Integer rowNum, Integer columnNum, Double x, Double y) {

        if(row!=null)constraints.gridy = row; //组件所在行
        if(column!=null)constraints.gridx = column; //组件所在列

        if(rowNum!=null)constraints.gridheight = rowNum; //组件高度,占几行
        if(columnNum!=null)constraints.gridwidth = columnNum; //组件宽度，占几列

        if(x!=null)constraints.weightx = x; //组件在水平方向的拉伸能力
        if(y!=null)constraints.weighty = y; //组件在垂直方向的拉伸能力

        layout.setConstraints(componentToAdd, constraints); //设置目标组件的约束
        container.add(componentToAdd); //在容器中添加目标组件
    }

    /**
     * 供panel使用
     * @param componentToAdd
     * @param row
     * @param column
     * @param rowNum
     * @param columnNum
     * @param x
     * @param y
     * @param insets
     * @param anchor
     */
    public void addComponent(Component componentToAdd, Integer row, Integer column, Integer rowNum, Integer columnNum,
                             Double x, Double y, Insets insets, Integer anchor) {
        if(insets!=null) constraints.insets = insets;//设定间距
        if(anchor!=null) constraints.anchor = anchor;//设定位置
        addComponent(componentToAdd, row, column, rowNum, columnNum, x, y);
    }

    /**
     * 供控件使用
     * @param container
     * @param constraints
     * @param componentToAdd
     * @param row
     * @param column
     * @param rowNum
     * @param columnNum
     * @param x
     * @param y
     */
    public void addComponent(Container container, GridBagConstraints constraints, Component componentToAdd,
                             Integer row, Integer column, Integer rowNum, Integer columnNum, Double x, Double y) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        if(container.getLayout()!=null && (container.getLayout() instanceof GridBagLayout)) gridBagLayout = (GridBagLayout)container.getLayout();

        if(row!=null)constraints.gridy = row; //组件所在行
        if(column!=null)constraints.gridx = column; //组件所在列

        if(rowNum!=null)constraints.gridheight = rowNum; //组件高度,占几行
        if(columnNum!=null)constraints.gridwidth = columnNum; //组件宽度，占几列

        if(x!=null)constraints.weightx = x; //组件在水平方向的拉伸能力
        if(y!=null)constraints.weighty = y; //组件在垂直方向的拉伸能力

        gridBagLayout.setConstraints(componentToAdd, constraints); //设置目标组件的约束
        container.setLayout(gridBagLayout);
        container.add(componentToAdd); //在容器中添加目标组件
    }

    public GridBagConstraints newGridBagConstraints(Integer fill, Insets insets, Integer anchor) {
        GridBagConstraints tempConstraints = new GridBagConstraints();
        if(fill!=null) tempConstraints.fill = fill; //在水平方向和垂直方向上同时调整组件大小
        if(insets!=null) tempConstraints.insets = insets;//设定间距
        if(anchor!=null) tempConstraints.anchor = anchor;
        return tempConstraints;
    }

    public void setGridBagConstraints(GridBagConstraints tempConstraints, Integer fill, Insets insets, Integer anchor) {
        if(fill!=null) tempConstraints.fill = fill; //在水平方向和垂直方向上同时调整组件大小
        if(insets!=null) tempConstraints.insets = insets;//设定间距
        if(anchor!=null) tempConstraints.anchor = anchor;
    }

    public static void addKeyWordDriver(String keyWord, KeyWordDriver keyWordDriver, Class<?> keyWordType) {
        KWD.put(keyWord, keyWordDriver);
        KWDT.put(keyWord, keyWordType);
    }

    public static LinkedList<String> getKeyWords() {
        return keyWords;
    }

    public static void setKeyWords(String keyWord) {
        keyWords.add(keyWord);
    }

    public GridBagConstraints getConstraints() {
        return constraints;
    }

    public void setConstraints(GridBagConstraints constraints) {
        this.constraints = constraints;
    }

    public static LinkedHashMap<String, KeyWordDriver> getKWD() {
        return KWD;
    }
}
