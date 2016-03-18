package com.automation.frame.panel;

import com.automation.keyword.BaseKeyWordType;
import com.automation.tool.util.Print;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class ExecuteSingleStepEditPanel extends CustomPanel {

    // 关键字
    JLabel key = new JLabel("关键字：", JLabel.RIGHT);
    static JComboBox<String> keyWord = new JComboBox<>();

    // 对象
    JLabel object = new JLabel("元素名：", JLabel.RIGHT);
    static JTextField webElement = new JTextField();

    // xpath
    JLabel xpath = new JLabel("XPath：", JLabel.RIGHT);
    public static JTextField xpathValue = new JTextField();

    // 参数
    JLabel para = new JLabel("参数：", JLabel.RIGHT);
    static JTextField parameter = new JTextField();

    // expect
    JLabel expect = new JLabel("预期值：", JLabel.RIGHT);
    static JTextField expectValue = new JTextField();

    // 实际值
    JLabel actual = new JLabel("实际值：", JLabel.RIGHT);
    static JTextField actualValue = new JTextField("");

    // 对比结果
    JLabel result = new JLabel("对比结果：", JLabel.CENTER);
    static JTextField resultValue = new JTextField();

    // 执行 插入执行
    static ExecuteSingleStepRunPanel ESSRP;

    // 默认border
    final static Border defaultBorder = new JTextField().getBorder();
    /**
     * 右击菜单，xpathValue
     */
    private JPopupMenu rightMenu = new JPopupMenu();
    /**
     * 定义关键字与输入框 联动关系
     * 1：只开启“参数”输入框，2：开启“元素名”、“XPath”、“参数”输入框，3：开启“元素名”、“XPath”、“预期值”输入框
     */
    final static int keyWordType = 1;

    public ExecuteSingleStepEditPanel(){
        this.getConstraints().insets = new Insets(1,3,1,3);

        //步骤
        this.addComponent(key,        1, 1, 1, 1, 0.1, 0.1);
        // 设置关键字到 JComboBox
        setKeyWordListener();
        this.addComponent(keyWord,    1, 2, 1, 3, 0.8, 0.6);
        //对象
        this.addComponent(object,     2, 1, 1, 1, 0.1, 0.1);
        addWebElementListener();
        this.addComponent(webElement, 2, 2, 1, 3, 0.8, 0.6);
        //xpath
        this.addComponent(xpath,       3, 1, 1, 1, 0.1, 0.1);
        addXPathValueListener();
        this.addComponent(xpathValue,  3, 2, 1, 3, 0.8, 0.6);
        //参数
        this.addComponent(para,       4, 1, 1, 1, 0.1, 0.1);
        this.addComponent(parameter,  4, 2, 1, 3, 0.8, 0.6);
        //expect
        this.addComponent(expect,       5, 1, 1, 1, 0.1, 0.1);
        addExpectValueListener();
        this.addComponent(expectValue,  5, 2, 1, 3, 0.8, 0.6);
        // actual
        this.addComponent(actual,       6, 1, 1, 1, 0.1, 0.1);
        addActualValueListener();
        this.addComponent(actualValue,  6, 2, 1, 1, 0.3, 0.6);
        // 对比结果
        this.addComponent(result,       6, 3, 1, 1, 0.1, 0.1);
        addResultValueListener();
        this.addComponent(resultValue, 6, 4, 1, 1, 0.2, 0.1);
        // 执行、插入执行、验证按钮
        ESSRP = new ExecuteSingleStepRunPanel();
        this.addComponent(ESSRP,      7, 2, 1, 3, 0.1, 0.1);
    }

    /**
     * 对比结果 监听
     */
    private void addResultValueListener() {
        resultValue.setHorizontalAlignment(JTextField.CENTER);
        resultValue.setEditable(false);
        resultValue.setFont(new Font(null,Font.BOLD,13));
//        setResultValueFail();
//        setResultValuePass();
    }

    /**
     * 实际值 监听
     */
    private void addActualValueListener() {
        actualValue.setEditable(false);

        actualValue.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击后全选内容
                actualValue.selectAll();
                if (e.getButton() == 3){
                    rightMenu = getRightMenu(actualValue);
                    // 展示右击菜单，提供复制XPath、复制文本
                    rightMenu.show(actualValue, e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * actualValue 右击菜单
     * @return JPopupMenu
     */
    private JPopupMenu getRightMenu(final JTextField textField) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copyXPath = new JMenuItem("拷贝");
        // 拷贝文本按钮监听
        copyXPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = toolkit.getSystemClipboard();
                String text = textField.getText();
                Print.log("拷贝内容：" + text, 0);
                StringSelection stringSel = new StringSelection(text);
                clipboard.setContents(stringSel, null);
            }
        });
        menu.add(copyXPath);
        return menu;
    }

    /**
     * 设置预期值 监听
     */
    private void addExpectValueListener() {
        expectValue.setEditable(false);
    }

    /**
     * 设置XPath 监听
     */
    private void addXPathValueListener() {
        xpathValue.setEditable(false);
        xpathValue.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击后全选内容
                xpathValue.selectAll();
                if (e.getButton() == 3){
                    rightMenu = getRightMenu(xpathValue);
                    // 展示右击菜单，提供复制XPath、复制文本
                    rightMenu.show(xpathValue, e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * 设置元素名 监听
     */
    private void addWebElementListener() {
        webElement.setEditable(false);
    }

    /**
     * 设置关键字 监听
     */
    private void setKeyWordListener() {
        // 设置可编辑
//        keyWord.setEditable(true);
        // 添加所有关键字到下拉列表
        for(String key:getKeyWords()){
            keyWord.addItem(key);
        }
        keyWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 初始化JTextField样式
                setDefaultBorder(xpathValue);
                setDefaultBorder(parameter);
                setDefaultBorder(expectValue);
                // 设置可编辑性
                String selectedItem = keyWord.getSelectedItem().toString();
                setEditadbleByKeyWord(selectedItem);
            }
        });
    }

    /**
     * 根据关键字 设置输入框是否可编辑
     * @param keyWord 关键字
     */
    public static void setEditadbleByKeyWord(String keyWord){
        int keyWordType = getKeyWordType(keyWord);
        setEditadbleByKeyWordType(keyWordType);
    }

    /**
     * 根据关键字 设置输入框是否可编辑
     * @param keyWordType 关键字类型
     */
    public static void setEditadbleByKeyWordType(int keyWordType){
        switch (keyWordType){
            case 1:
                webElement.setText("");
                webElement.setEditable(false);
                xpathValue.setText("");
                xpathValue.setEditable(false);
                parameter.setEditable(true);
                expectValue.setText("");
                expectValue.setEditable(false);
                ESSRP.setVerifyButtonEnabled(false);
                actualValue.setText("");
                setResultValueDefault();
                break;
            case 2:
                webElement.setEditable(true);
                xpathValue.setEditable(true);
                parameter.setEditable(true);
                expectValue.setText("");
                expectValue.setEditable(false);
                ESSRP.setVerifyButtonEnabled(false);
                actualValue.setText("");
                setResultValueDefault();
                break;
            case 3:
                webElement.setEditable(true);
                xpathValue.setEditable(true);
                parameter.setText("");
                parameter.setEditable(false);
                expectValue.setEditable(true);
                ESSRP.setVerifyButtonEnabled(true);
                actualValue.setText("");
                break;
            case 4:
                webElement.setEditable(true);
                xpathValue.setEditable(true);
                parameter.setEditable(true);
                expectValue.setEditable(true);
                ESSRP.setVerifyButtonEnabled(true);
                break;
            case 5:
                webElement.setText("");
                webElement.setEditable(false);
                xpathValue.setText("");
                xpathValue.setEditable(false);
                parameter.setText("");
                parameter.setEditable(false);
                expectValue.setEditable(true);
                ESSRP.setVerifyButtonEnabled(true);
                break;
            case 6:
                webElement.setText("");
                webElement.setEditable(false);
                xpathValue.setText("");
                xpathValue.setEditable(false);
                parameter.setText("");
                parameter.setEditable(false);
                expectValue.setText("");
                expectValue.setEditable(false);
                ESSRP.setVerifyButtonEnabled(false);
                break;
        }
        ExecuteSingleStepRunPanel.popDialog = false;
    }

    /**
     * 定义关键字与输入框 联动关系
     * 1：只开启“参数”输入框，
     * 2：开启“元素名”、“XPath”、“参数”输入框，
     * 3：开启“元素名”、“XPath”、“预期值”输入框，
     * 4：开启“元素名”、“XPath”、“参数”，“预期值”输入框
     * 5：开启“元素名”、“预期值” 输入框
     * 6: 关闭所有输入框
     * @param keyWord 关键字
     * @return int
     */
    public static int getKeyWordType(String keyWord) {
        int type = 4;
        if (keyWord.equals(BaseKeyWordType.START_BROWSER)
                ||keyWord.equals(BaseKeyWordType.ENTER_IFRAME)
                ||keyWord.equals(BaseKeyWordType.CHANGE_BRO_TAB)
                ||keyWord.equals(BaseKeyWordType.CLOSE_BRO_TAB)
                ||keyWord.equals(BaseKeyWordType.ACCESS_URL)
                ||keyWord.equals(BaseKeyWordType.ENCRYPT_BASE64)
                ||keyWord.equals(BaseKeyWordType.DECRYPT_BASE64)
                ){
            type = 1;
        }else if (keyWord.equals(BaseKeyWordType.INPUT)
                ||keyWord.equals(BaseKeyWordType.INPUT_PASSWORD)
                ||keyWord.equals(BaseKeyWordType.CLICK)
                ||keyWord.equals(BaseKeyWordType.SELECT_OPTION)
                ||keyWord.equals(BaseKeyWordType.MOUSE_OVER)
                ||keyWord.equals(BaseKeyWordType.PAUSE_TIME)
                || keyWord.equals(BaseKeyWordType.CLOSE_ALERT)){
            type = 2;
        }else if (keyWord.equals(BaseKeyWordType.SYNC)
                || keyWord.equals(BaseKeyWordType.ENTER)){
            // keyWord.equals(KeyWordType.VERIFY)|| keyWord.equals(KeyWordType.FUZZY_MATCHING_TABLE)
            type = 3;
        }else if (keyWord.equals(BaseKeyWordType.FUZZY_MATCHING)
                || keyWord.equals(BaseKeyWordType.VERIFY)
                || keyWord.equals(BaseKeyWordType.FUZZY_MATCHING_TABLE)){
            type = 4;
        }else if (keyWord.equals(BaseKeyWordType.VERIFY_ALERT)){
            type = 5;
        }else if (keyWord.equals(BaseKeyWordType.CLOSE_BROWSER)

                ){
            type = 6;
        }
        return type;
    }

    /**
     * 获取当前选择的关键字
     * @return 关键字
     */
    public static String getKeyWordSelectedValue(){
        return keyWord.getSelectedItem().toString();
    }
    /**
     * 选择关键字
     */
    public static void setKeyWordSelectedValue(String keyword){
        keyWord.setSelectedItem(keyword);
    }

    /**
     * 获取 “xpath”JTextField 内容
     * @return String
     */
    public static String getWebElementXPathValue() {
        return xpathValue.getText();
    }

    /**
     * 设置 “xpath”JTextField 内容
     * @param value 值
     */
    public static void setWebElementXPahtValue(String value) {
        xpathValue.setText(value);
    }

    /**
     * 获取参数内容
     * @return String
     */
    public static String getParameterValue(){
        return parameter.getText();
    }

    /**
     * 设置参数内容
     * @param value 值
     */
    public static void setParameterValue(String value){
        parameter.setText(value);
    }

    /**
     * 获取预期结果控件内容
     * @return String
     */
    public static String getExpectValue() {
        return expectValue.getText();
    }

    /**
     * 设置预期结果控件内容
     * @param value 值
     */
    public static void setExpectValue(String value) {
        expectValue.setText(value);
    }

    /**
     * 获取实际值
     * @return
     */
    public static String getActualValue(){
        return actualValue.getText();
    }

    /**
     * 设置实际值
     * @param value
     */
    public static void setActualValue(String value){
        actualValue.setText(value);
    }

    /**
     * 获取设置元素名
     * @return String
     */
    public static String getWebElementName() {
        return webElement.getText();
    }

    /**
     * 设置元素名
     * @param value 值
     */
    public static void setWebElementName(String value) {
        webElement.setText(value);
    }

    /**
     * 设置JTextField为默认仰视
     * @param jTextField
     */
    public static void setDefaultBorder(JTextField jTextField) {
        jTextField.setBorder(defaultBorder);
    }

    /**
     * 设置对比结果 初始化
     */
    public static void setResultValueDefault() {
        resultValue.setText("");
        resultValue.setBackground(actualValue.getBackground());
    }

    /**
     * 设置对比结果失败
     */
    public static void setResultValueFail() {
        resultValue.setText("F A I L");
        resultValue.setForeground(Color.RED);
        resultValue.setBackground(new Color(255,128,130));
    }

    /**
     * 设置对比结果通过
     */
    public static void setResultValuePass(){
        resultValue.setText("P A S S");
        resultValue.setForeground(new Color(40, 190, 35));
        resultValue.setBackground(new Color(40, 253, 33));
    }

    /**
     * 把所有输入框清空
     */
    public static void setJTextFieldEmpty() {
        webElement.setText("");
        xpathValue.setText("");
        parameter.setText("");
        expectValue.setText("");
        actualValue.setText("");
        resultValue.setText("");
    }
}

