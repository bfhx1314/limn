package com.automation.frame.panel;

import com.automation.driver.Driver;
import com.automation.exception.RunException;
import com.automation.frame.renderer.WebElementCellRenderer;
import com.automation.tool.dictory.WebElementLocatorDescribe;
import com.automation.tool.util.Print;
import com.automation.tool.util.RegExp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class LoadBrowserPanel extends CustomPanel{


    private JLabel titleLabel = new JLabel("当前URL:",JLabel.CENTER);
    /**
     * URL值
     */
    private static JTextField title = new JTextField();
//    private static JLabel title = new JLabel();


    private static JButton getBaseWebElement = new JButton("基本元素");
    private static JButton getSpecialWebElement = new JButton("插件元素");
    private static JComboBox<String> selectBaseWebElementTagName = new JComboBox<String>();
//    private static JComboBox<String> selectSpecialWebElementTagName = new JComboBox<String>();
    private JButton setXPathName = new JButton("设置别名");

    JLabel locatorLabel = new JLabel("Locator:",JLabel.CENTER);
    /**
     * 搜索结果
     */
//    private JLabel locatorResult = new JLabel("//*[@id=\"wv_pager_center\"]/table/tbody/tr/td[4]");
    private JTextField locatorResult = new JTextField();
//    private JLabel startSearch = new JLabel("加载中......");
    //    result.setText("加载中......");
//    result.setForeground(Color.RED);
//    result.setVisible(false);


    JLabel searchLocatorLabel = new JLabel("定位:",JLabel.CENTER);
    /**
     * 搜索的Locator
     */
    private static JTextField searchLocatorValue = new JTextField();
    /**
     * 搜索按钮
     */
    private JButton searchLocatorButton = new JButton("搜索");
    private JButton cancelSearchLocatorButton = new JButton("取消搜索");
    /**
     * 右击菜单，列表中的WebElement
     */
    private JPopupMenu rightMenu = new JPopupMenu();
    /**
     * WebElement列表
     */
    private static JList<WebElementLocatorDescribe> webElements = new JList<WebElementLocatorDescribe>();
    private JScrollPane webElementsJSP = new JScrollPane(webElements,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    /**
     * 缓存
     */
    private static List<WebElementLocatorDescribe> webElementsListTemp = new ArrayList<>();

    /**
     * JList中的所有元素
     */
    public static DefaultListModel<WebElementLocatorDescribe> webElementsList = new DefaultListModel<WebElementLocatorDescribe>();

    /**
     * 选中的WebElementId
     */
    private String selectedHighLightWebElementId = null;

    // 查询的元素列表
//    private HashMap<Integer, WebElement> findWebElements = new HashMap<Integer, WebElement>();
    private static HashMap<Integer, String> findWebElements = new HashMap<Integer, String>();
    /**
     * 查询的元素id
     */
    private static HashMap<Integer, String> findWebElementsId = new HashMap<Integer, String>();
    /**
     * 存放元素的 序列和Locator描述，与JList中webElements顺序对应
     */
    private static HashMap<Integer, String> webElementLocatorDescribeList = new HashMap<Integer, String>();
    /**
     * 存放一个tagName所有元素序列的范围，展现在JList中并且顺序对应
     */
    private static HashMap<String, String> webElementLocatorRangeList = new HashMap<String, String>();

    // 基本元素
    public final static String[] FIND_BASIC_TAGNAME = { "input", "a", "button", "select", "table" ,"textarea","iframe"};
    // SVG插件元素
    private final String[] FIND_SVG_TAGNAME = {"text"};
    /**
     * 是否搜索基本元素
     */
    private boolean isBasicTagName = true;
    /**
     * 定位器搜索线程
     */
    private Thread searchLocatorThread;
    /**
     * 基本元素 线程
     */
    private Thread searchBaseWebElementThread;
    /**
     * 插件元素 线程
     */
    private Thread searchSpecialWebElementThread;

    /**
     * 元素计数，根据tagName
     */
    private int index = 0;


    public LoadBrowserPanel(){
//        getBaseWebElement.setMargin(new Insets(10,10,10,10));

        // 刷新基本元素按钮
        addGetBaseWebElementListener();
        this.addComponent(getBaseWebElement, 1, 1, 1, 1, 0.01, 0.01);
//        title.setMaximumSize(new Dimension(220,25));
//        title.setPreferredSize(new Dimension(10,25));
        this.addComponent(titleLabel,1,2,1,1,0.01,0.01);
        addTitleListener();
        this.addComponent(title     ,1,3,1,2,0.97,0.01);
        //        this.addComponent(new JLabel("TagName:")     ,1,4,1,1,0.01,0.01);
        // 下拉列表 元素tagName 按钮
        addBaseWebElementTagNameListener();
        this.addComponent(selectBaseWebElementTagName,1,5,1,1,0.01,0.01);

        // 刷新插件元素按钮
        addGetSpecialWebElementListener();
        this.addComponent(getSpecialWebElement, 2, 1, 1, 1, 0.01, 0.01);
        this.addComponent(locatorLabel     ,2,2,1,1,0.01,0.01);
        // Locator值
        addLocatorResultListener();
        this.addComponent(locatorResult     ,2,3,1,3,0.97,0.01);
//        addSpecialWebElementTagNameListener();
//        this.addComponent(selectSpecialWebElementTagName,2,5,1,1,0.01,0.01);

        // 设置别名按钮
        addSetXPathNameListener();
        this.addComponent(setXPathName,3,1,1,1,0.01,0.01);
        // 定位文字
        this.addComponent(searchLocatorLabel,3,2,1,1,0.01,0.01);
        // Locator输入框
        addSearchLocatorValue();
        this.addComponent(searchLocatorValue,3,3,1,1,0.96,0.01);
        // 搜索按钮
        addSearchLocatorButtonListener();
        this.addComponent(searchLocatorButton,3,4,1,1,0.01,0.01);
        addCancelSearchLocatorButtonListener();
        this.addComponent(cancelSearchLocatorButton,3,5,1,1,0.01,0.01);
        // WebElemet列表
        addWebElementsListener();
        this.addComponent(webElementsJSP     ,4,1,1,5,1d,0.96);
    }

    /**
     * 定位输入框监听
     */
    private void addSearchLocatorValue() {
        setSearchLocatorValueUnEditable();
        searchLocatorValue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
/*                // 获取输入的内容
                String locatorValue = searchLocatorValue.getText();
                if (!locatorValue.isEmpty()) {
                    // 判断是否按回车
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (!locatorValue.isEmpty()) {
                            Print.log("开始搜索XPath：" + locatorValue + "  等待时间20s，可以取消搜索。");
                            searchLocatorButton.setEnabled(false);
                            cancelSearchLocatorButton.setEnabled(true);
                            searchLocatorThread = new Thread(new FindWebElement(locatorValue));
                            searchLocatorThread.start();
                        } else {
                            Print.logRed("定位输入框XPath为空，无法定位。");
                            searchLocatorValue.setBorder(new LineBorder(Color.RED.darker()));
                        }
                    }
                }*/
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // 释放键盘时判断输入框值，控制按钮
                String locator = searchLocatorValue.getText();
                if (!locator.isEmpty()){
                    searchLocatorButton.setEnabled(true);
                    searchLocatorValue.setForeground(Color.BLACK);
                    searchLocatorValue.setBorder(new JTextField().getBorder());
                }else {
                    searchLocatorButton.setEnabled(false);
                }
            }
        });

    }

    public static void setSearchLocatorValueEditable() {
        searchLocatorValue.setEditable(true);
    }

    public static void setSearchLocatorValueUnEditable() {
        searchLocatorValue.setEditable(false);
    }

    /**
     * 取消搜索按钮监听
     */
    private void addCancelSearchLocatorButtonListener() {
        cancelSearchLocatorButton.setEnabled(false);
        cancelSearchLocatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != searchLocatorThread){
                    if (searchLocatorThread.isAlive()){
                        searchLocatorThread.stop();
                    }
                }
                if (null != searchBaseWebElementThread){
                    if (searchBaseWebElementThread.isAlive()){
                        searchBaseWebElementThread.stop();
                    }
                }
                if (null != searchSpecialWebElementThread){
                    if (searchSpecialWebElementThread.isAlive()){
                        searchSpecialWebElementThread.stop();
                    }
                }
                Print.log("已停止所有搜索。");
                setBaseWebElementButtonEnabled();
                setSpecialWebElementButtonEnabled();
                searchLocatorButton.setEnabled(true);
                cancelSearchLocatorButton.setEnabled(false);
                locatorResult.setText("");
                searchLocatorValue.setText("");
            }
        });
    }

    /**
     * 增加搜索按钮监听
     */
    private void addSearchLocatorButtonListener() {
        searchLocatorButton.setEnabled(false);
        searchLocatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String locator = searchLocatorValue.getText();
                if (!locator.isEmpty()) {
                    Print.log("开始搜索XPath：" + locator + "  等待时间20s，可以取消搜索。");
//                    locatorResult.setText("开始搜索...");
//                    locatorResult.setBorder(new JTextField().getBorder());

                    searchLocatorButton.setEnabled(false);
                    cancelSearchLocatorButton.setEnabled(true);
                    searchLocatorThread = new Thread(new FindWebElement(locator));
                    searchLocatorThread.start();
                    // TODO 高亮元素
                } else {
                    Print.logRed("定位输入框XPath为空，无法定位。");
                    searchLocatorValue.setBorder(new LineBorder(Color.RED.darker()));
                }
            }
        });

    }

    /**
     * WebElement列表 监听
     */
    private void addWebElementsListener() {
        // 重写控件内元素样式
        webElements.setCellRenderer(new WebElementCellRenderer());
        // 鼠标监听
        webElements.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3){
                    if (findWebElements.size() > 0){
                        if (webElements.getSelectedIndex() == -1) {
                            Print.logRed("请先选择元素。");
                            setXPathName.setEnabled(false);
                            return;
                        }
                    }
                    // 右击事件
                    if (webElements.getSelectedIndex() != -1){
//                        WebElement webElement = findWebElements.get(webElements.getSelectedValue().key());
//                        rightMenu = getRightMenu(webElement);
                        String webElementXPath = findWebElements.get(webElements.getSelectedValue().key());
                        rightMenu = getRightMenu(webElementXPath);
                        // 展示右击菜单，提供复制XPath、复制文本
                        rightMenu.show(webElements, e.getX(), e.getY());
                    }
                }
            }
        });
        // 选中监听
        webElements.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    if (null != getSelectedHighLightWebElementId()) {
                        try {
                            Driver.cancelHighLightWebElement(getSelectedHighLightWebElementId());
                        } catch (RunException e2) {
                            Print.logRed("取消高亮WebElement失败，可能页面已切换。");
                        }
                    }
                    if (webElements.getSelectedIndex() == -1) {
                        return;
                    }

                    // 设置高亮元素
//                    String webElementXPath = findWebElements.get(webElements.getSelectedValue().key());
                    String webElementId = findWebElementsId.get(webElements.getSelectedValue().key());
                    String webElementXPath = findWebElements.get(webElements.getSelectedValue().key());

                    //                        WebElement webElement = Driver.getElement(webElementXPath);
                    setSelectedHighLightWebElement(webElementId);
                    // 设置高亮locatorResult值
                    setLocatorByWebElment(webElementXPath);
                    // 设置该元素高亮
                    Driver.highLightWebElement(getSelectedHighLightWebElementId());
                }
            }
        });

        webElements.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // TODO
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    /**
     * 根据WebElement获取XPath
     * @param WebElementId 元素
     */
    private void setLocatorByWebElment(String WebElementId) {
//        if (null == WebElementId) {
//            return;
//        }
//        try {
            String locator = WebElementId;
//            if (isBasicTagName){
//                if (WebElementId.contains("EVS-Test.W3T0X2E6Z9Y")){
//                    locator = Driver.getXpathByWebElement(WebElementId);
//                }else {
//                    locator = WebElementId;
//                }
//            }else {
//                locator = Driver.getXpathBySpecialWebElement(WebElementId);
//            }

            if (null != locator && !locator.isEmpty()) {
                // locator是通过WebElement元素获得。
                // 不需要再次搜索，直接高亮页面元素。
                setXPathName.setEnabled(true);
                locatorResult.setText(locator);
                locatorResult.setForeground(Color.GREEN.darker());
                ExecuteSingleStepEditPanel.setWebElementXPahtValue(locator);
//                new Thread(new FindWebElement(locator)).start();
            } else {
                setXPathName.setEnabled(false);
                searchLocatorValue.setForeground(Color.RED.darker());
                searchLocatorValue.setText("未能定位！");
//                locatorResult.setText("未能定位");
//                locatorResult.setForeground(Color.RED.darker());
            }
//        } catch (RunException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 右键菜单
     * @param webElementXPath 元素xpath
     * @return JPopupMenu
     */
    private JPopupMenu getRightMenu(final String webElementXPath) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        JPopupMenu menu = new JPopupMenu();
        JMenuItem copyXPath = new JMenuItem("拷贝XPath");
        JMenuItem copyText = new JMenuItem("拷贝文本");
        // 拷贝文本按钮监听
        copyText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = toolkit.getSystemClipboard();
                String text = "";
                WebElement webElement;
                try {
                    webElement = Driver.getElement(webElementXPath);
                    if (null != webElementXPath) {
                        text = webElement.getText();
                    }
                    if (null == text || text.isEmpty()) {
                        text = webElement.getAttribute("textContent");
                    }
                    if (null == text || text.isEmpty()) {
                        text = webElement.getAttribute("value");
                    }
                    Print.log("控件文本信息：" + text, 0);
                    StringSelection stringSel = new StringSelection(text);
                    clipboard.setContents(stringSel, null);
                } catch (RunException e1) {
                    e1.printStackTrace();
                }
            }
        });
        // 拷贝XPath按钮监听
        copyXPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = toolkit.getSystemClipboard();
                // 先获取locatorResult控件值，判断是否为空
                String xpath = locatorResult.getText();
                if (xpath.isEmpty()){
                    xpath = webElementXPath;
/*
                    try {
                        WebElement webElement = Driver.getElement(webElementXPath);
                        if (isBasicTagName) {
                            xpath = Driver.getXpathByWebElement(webElement);
                        } else {
                            xpath = Driver.getXpathBySpecialWebElement(webElement);
                        }
                    } catch (RunException e1) {
                        e1.printStackTrace();
                    }
*/
                }
                Print.log("控件XPath：" + xpath);
                StringSelection stringSel = new StringSelection(xpath);
                clipboard.setContents(stringSel, null);
            }
        });
        menu.add(copyText);
        menu.add(copyXPath);
        return menu;
    }

    /**
     * locatorResult JTextField监听
     */
    private void addLocatorResultListener() {
        locatorResult.setEditable(false);
        locatorResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // 点击时选中全部
                locatorResult.selectAll();
            }
        });
    }

    /**
     * 插件元素 按钮监听
     */
    private void addGetSpecialWebElementListener() {
        setSpecialWebElementButtonUnEnabled();
        getSpecialWebElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != getSelectedHighLightWebElementId()) {
                    try {
                        Driver.cancelHighLightWebElement(getSelectedHighLightWebElementId());
                    } catch (RunException e2) {
                        Print.logRed("取消高亮WebElement失败，可能页面已切换。");
                    }
                }
                isBasicTagName = false;
                setXPathName.setEnabled(false);
                try {
                    loadWebElement();
                } catch (RunException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * 选择基础tagName 下拉框监听
     */
    private void addBaseWebElementTagNameListener() {
        // 设置可编辑
        setBaseSelectWebElementTagNameUnEditable();
        setBaseSelectWebElementTagNameUnEnabled();
        // 初始化下拉列表选项
        initBaseWebElementTagNameValue();

        Component component = selectBaseWebElementTagName.getEditor().getEditorComponent();
        // 增加编辑时的回车监听
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // 获取输入的内容
                String tagName = selectBaseWebElementTagName.getEditor().getItem().toString();
                if (!tagName.isEmpty()) {
                    // 判断是否按回车
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        try {
                            WebElement webElement = Driver.getWebElement(By.xpath("/html"));
                            new Thread(new SearchWebElement(webElement, tagName)).start();
                        } catch (RunException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // 增加选择选项时的监听
        selectBaseWebElementTagName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 判断选中改变，JList列表中的元素跟着改变
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    removeWebElementsListAllElements();
                    String selectItem = selectBaseWebElementTagName.getSelectedItem().toString();
                    if (selectItem.equalsIgnoreCase("ALL")) {
                        // 显示全部元素
                        for (Map.Entry<Integer, String> entrySet : webElementLocatorDescribeList.entrySet()) {
                            Integer key = entrySet.getKey();
                            String value = entrySet.getValue();
                            WebElementLocatorDescribe dictoryKeyValue = new WebElementLocatorDescribe(key, value);
                            webElementsList.addElement(dictoryKeyValue);
                        }
                    } else if (webElementLocatorRangeList.containsKey(selectItem)) {
                        // 显示选中的tagName对应的元素，JList列表中的元素跟着改变
                        String[] range = webElementLocatorRangeList.get(selectItem).split(":");
                        int start = Integer.parseInt(range[0]);
                        int end = Integer.parseInt(range[1]);
                        for (; start <= end; start++) {
                            String value = webElementLocatorDescribeList.get(start);
                            WebElementLocatorDescribe dictoryKeyValue = new WebElementLocatorDescribe(start, value);
                            webElementsList.addElement(dictoryKeyValue);
                        }
                    } else {
//                        Print.logRed("列表中没有找到tagName：" + selectItem);
                    }
                    // 把筛选过的对象加入JList
                    webElements.setModel(webElementsList);
                }
            }
        });
    }

    public static void setBaseSelectWebElementTagNameEditable() {
        selectBaseWebElementTagName.setEnabled(true);
        selectBaseWebElementTagName.setEditable(true);
    }

    public static void setBaseSelectWebElementTagNameUnEditable() {
        selectBaseWebElementTagName.setEditable(false);
    }

    public static void setBaseSelectWebElementTagNameUnEnabled() {
        selectBaseWebElementTagName.setEnabled(false);
    }

    /**
     * 初始化下拉列表的tagName选项
     */
    public static void initBaseWebElementTagNameValue(){
        selectBaseWebElementTagName.removeAllItems();
        selectBaseWebElementTagName.addItem("ALL");
        for(String tagName : FIND_BASIC_TAGNAME){
            selectBaseWebElementTagName.addItem(tagName);
        }
    }
/*

    public static void setSpecialSelectWebElementTagNameEditable() {
        selectSpecialWebElementTagName.setEnabled(true);
        selectSpecialWebElementTagName.setEditable(true);
    }

    public static void setSpecialSelectWebElementTagNameUnEditable() {
        selectSpecialWebElementTagName.setEditable(false);
    }

    public static void setSpecialSelectWebElementTagNameUnEnabled() {
        selectSpecialWebElementTagName.setEnabled(false);
    }
*/

    /**
     * 设置别名 按钮监听
     */
    private void addSetXPathNameListener() {
        setXPathName.setEnabled(false);
        setXPathName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String locator = locatorResult.getText();
                if (locator.isEmpty() || locator.equals("未能定位！")) {
                    Print.logRed("locator为：" + locator + "，没有定位到该元素。");
                    return;
                }
                String xpathName;
                xpathName = JOptionPane.showInputDialog(LoadBrowserPanel.this, "设置XPATH别名", "");
/*
                boolean flag = true;
                do {
                    // 弹输入框，设置别名
                    xpathName = JOptionPane.showInputDialog(LoadBrowserPanel.this, "设置XPATH别名", "");
                    if (null != xpathName && !xpathName.isEmpty()) {
                        LinkedHashMap<String, String> mapXPathName = WebElementXPathName.getXpathName();
                        if (mapXPathName.containsKey(xpathName)) {
                            // 已存在的别名 弹出警告，选择是否覆盖
                            int status = JOptionPane.showConfirmDialog(LoadBrowserPanel.this
                                    , "存在重复的关联属性:" + xpathName + ",是否覆盖"
                                    , "警告"
                                    , JOptionPane.OK_CANCEL_OPTION);
                            if (0 == status) {
                                WebElementXPathName.setXpathName(xpathName, locator);
                                flag = false;
                            }
                        } else {
                            WebElementXPathName.setXpathName(xpathName, locator);
                            flag = false;
                        }
                    }else {
                        return;
                    }
                } while (flag);
                */

                // 设置关键字为“录入”
//                ExecuteSingleStepEditPanel.setKeyWordSelectedValue(KeyWordType.INPUT);
                // 设置元素名为 别名
                ExecuteSingleStepEditPanel.setWebElementName(xpathName);
                // 赋值到EditPanel的xpath输入框
                ExecuteSingleStepEditPanel.setWebElementXPahtValue(locator);
            }
        });
    }

    /**
     * 基本元素 按钮监听
     */
    private void addGetBaseWebElementListener() {
        setBaseWebElementButtonUnEnabled();

        getBaseWebElement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (null != getSelectedHighLightWebElementId()) {
                    try {
                        Driver.cancelHighLightWebElement(getSelectedHighLightWebElementId());
                    } catch (RunException e2) {
                        Print.logRed("取消高亮WebElement失败，可能页面已切换。");
                    }
                }
                setXPathName.setEnabled(false);
                isBasicTagName = true;
                try {
                    loadWebElement();
                } catch (RunException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void addTitleListener(){
        title.setEditable(false);
        title.setBorder(new EmptyBorder(0,0,0,0));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                title.selectAll();
            }
        });
    }

    /**
     * 加载页面
     */
    public void loadWebElement() throws RunException {
        String url = Driver.getCurrentUrl();
        Print.log("URL:" + url, 0);
        setUrlToTile(url);
        traversal();
    }

    /**
     * 展示URL
     * @param url
     */
    public static void setUrlToTile(String url){
/*        if (url.length()>45){
            url = url.substring(0,45)+"...";
        }*/
        title.setText(url);
    }

    /**
     * 遍历页面中所有元素,取出tagname
     */
    private void traversal() throws RunException {
        setSelectedHighLightWebElement(null);
        // 清空所有元素
//        removeWebElementsListAllElements();
//        findWebElements.clear();
        selectBaseWebElementTagName.removeAllItems();
        webElements.removeAll();
        // 显示所有的
        selectBaseWebElementTagName.addItem("ALL");
        selectBaseWebElementTagName.setSelectedItem("ALL");
        WebElement web = Driver.getWebElement(By.xpath("/html"));
        if (isBasicTagName){
            searchBaseWebElementThread = new Thread(new SearchWebElement(web));
            searchBaseWebElementThread.start();
        }else {
            searchSpecialWebElementThread = new Thread(new SearchWebElement(web));
            searchSpecialWebElementThread.start();
        }
    }

    /**
     * 清空webElementsList所有元素
     */
    public static void removeWebElementsListAllElements(){
        webElementsList.removeAllElements();
    }

    /**
     * 清空webElements所有元素
     */
    public static void removeAllWebElements(){
        webElements.removeAll();
    }

    /**
     * 设置加载时的信息
     */
    private void setStartLoadingInfo(){
        setBaseWebElementButtonUnEnabled();
        setSpecialWebElementButtonUnEnabled();
        cancelSearchLocatorButton.setEnabled(true);
        locatorResult.setForeground(Color.red);
//        searchLocatorValue.setFont(new Font("宋体",Font.BOLD,13));
        locatorResult.setText("加载中。。。");
//        searchLocatorValue.setText("加载中。。。");
//        locatorResult.setText("");
    }

    private void setCompleteLoadingInfo(){
        for(WebElementLocatorDescribe obj:webElementsListTemp){
            webElementsList.addElement(obj);
        }
        setBaseWebElementButtonEnabled();
        setSpecialWebElementButtonEnabled();
        cancelSearchLocatorButton.setEnabled(false);
        locatorResult.setForeground(Color.black);
        locatorResult.setText("");
//        searchLocatorValue.setText("");
    }

    /**
     * 获取当前高亮的webElementId
     * @return
     */
    private String getSelectedHighLightWebElementId() {
        return selectedHighLightWebElementId;
    }

    private void setSelectedHighLightWebElement(String selectedHighLightWebElementId) {
        this.selectedHighLightWebElementId = selectedHighLightWebElementId;
    }

    /**
     * 设置“基本元素”按钮可交互
     */
    public static void setBaseWebElementButtonEnabled(){
        getBaseWebElement.setEnabled(true);
    }

    /**
     * 设置“基本元素”按钮不可交互
     */
    public static void setBaseWebElementButtonUnEnabled(){
        getBaseWebElement.setEnabled(false);
    }
    /**
     * 设置“插件元素”按钮可交互
     */
    public static void setSpecialWebElementButtonEnabled(){
        getSpecialWebElement.setEnabled(true);
    }
    /**
     * 设置“插件元素”按钮不可交互
     */
    public static void setSpecialWebElementButtonUnEnabled(){
        getSpecialWebElement.setEnabled(false);
    }

    /**
     * 搜索WebElement
     */
    private class SearchWebElement implements Runnable {

        private WebElement webElement = null;
        private String tagName = null;
//        private int index = 0;

        public SearchWebElement(WebElement webE) {
            this.webElement = webE;
        }
        public SearchWebElement(WebElement webE, String tagN){
            this.webElement = webE;
            this.tagName = tagN;
        }

        @Override
        public void run() {
            boolean searchSingle = false;
            setStartLoadingInfo();
            if (null == this.tagName){
                // 搜索所有默认的元素
                clearFindWebElementsInfo();
                index = 0;
                String[] tempTagName = {};
                if (isBasicTagName){
                    tempTagName = FIND_BASIC_TAGNAME;
                }else {
                    tempTagName = FIND_SVG_TAGNAME;
                }
                for (String tagName : tempTagName) {
                    Print.log("开始加载“"+tagName+"”元素。");
                    serach(tagName);
                }
            }else {
                searchSingle = true;
                Print.log("开始加载“"+this.tagName+"”元素。");
                // 搜索指定单个元素
                serach(this.tagName);
            }
            Print.log("全部加载完成。");
            setCompleteLoadingInfo();
            if (searchSingle){
                selectBaseWebElementTagName.setSelectedItem("ALL");
                selectBaseWebElementTagName.setSelectedItem(this.tagName);
            }
            // 点击后，倒计时3秒后才能再次点击。
            new Thread(new CountBackwards()).start();
        }

        private void serach(String tagName){
            // 如果tagName已经存在于下拉列表中就不增加。
            boolean doesAddTagName = true;
            int tagNameCount = selectBaseWebElementTagName.getItemCount();
            for(int i=0;i<tagNameCount;i++){
                if (selectBaseWebElementTagName.getItemAt(i).equals(tagName)){
                    doesAddTagName = false;
                    break;
                }
            }
            if (doesAddTagName){
                selectBaseWebElementTagName.addItem(tagName);
            }

            int start = index;
            if (null == webElement){
                Print.log("搜索的父元素为空。", 2);
                return;
            }
            try {
                List<String> list = (ArrayList<String>) Driver.runScript("return getWebElementsFullXPath('" + tagName + "')");
//                int listSize = list.size();
                // 重新计数所有信息
//                reSetWebElementInfo(listSize);
                for(String str : list){
                    setWebElementInfo(str);
                }
            } catch (RunException e) {
                e.printStackTrace();
            }
            webElementLocatorRangeList.put(tagName, start + ":" + (index - 1));
        }


/*        private void reSetWebElementInfo(int size){
            if (null != this.tagName){
                webElementLocatorDescribeList.containsValue();
            }
        }*/

        /**
         * 记录WebElement信息
         * @param info
         */
        private void setWebElementInfo(String info){
            String[] arr = info.split("@#%");
            webElementLocatorDescribeList.put(index, arr[0]);
            webElementsListTemp.add(new WebElementLocatorDescribe(index, arr[0]));
//            webElementsList.addElement(new WebElementLocatorDescribe(index, arr[0]));
            findWebElements.put(index, arr[1]);
            findWebElementsId.put(index, arr[2]);
            index++;
        }
    }

    /**
     * 清空所有记录WebElement信息的对象。
     */
    public static void clearFindWebElementsInfo() {
        webElementsListTemp.clear();
        findWebElements.clear();
        findWebElementsId.clear();
        webElementLocatorDescribeList.clear();
        webElementLocatorRangeList.clear();
        removeWebElementsListAllElements();
    }

    /**
     * 搜索单个WebElement
     */
    private class FindWebElement implements Runnable {

        private String locator = null;

        public FindWebElement(String locator) {
            this.locator = locator;
        }

        @Override
        public void run() {
            setStartLoadingInfo();
            if(null != getWebElementBylocator(locator)){
                Print.logGreen("定位成功。xpath:"+locator);
                setXPathName.setEnabled(true);
                locatorResult.setText(locator);
                locatorResult.setForeground(Color.GREEN.darker());
                searchLocatorValue.setText("");
                ExecuteSingleStepEditPanel.setWebElementXPahtValue(locator);
            }else{
                locatorResult.setText("未能定位！");
                locatorResult.setForeground(Color.RED.darker());
            }
            cancelSearchLocatorButton.setEnabled(false);
            setCompleteLoadingInfo();
        }

        /**
         * 根据locator 获取WebElement
         * @param locator 定位器
         * @return
         */
        private WebElement getWebElementBylocator(String locator) {
            WebElement webElement = null;
            try {
                if (RegExp.findCharacters(locator, "^/")) {
                    String xpath = locator;
                    webElement = Driver.getWebElement(By.xpath(xpath));
                } else {
                    webElement = Driver.getWebElement(By.id(locator));
                }
            } catch (RunException e) {
                e.printStackTrace();
            }
            return webElement;
        }
    }


    private class CountBackwards implements Runnable{
        int wait = 2;
        @Override
        public void run() {
            setBaseWebElementButtonUnEnabled();
            setSpecialWebElementButtonUnEnabled();
            setBaseSelectWebElementTagNameUnEditable();
//            setSpecialSelectWebElementTagNameUnEditable();
            for(int i=wait; i>0; i--){
                LoadBrowserPanel.getBaseWebElement.setText(i+"");
                LoadBrowserPanel.getSpecialWebElement.setText(i+"");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setBaseWebElementButtonEnabled();
            setSpecialWebElementButtonEnabled();
            setBaseSelectWebElementTagNameEditable();
//            setSpecialSelectWebElementTagNameEditable();
            LoadBrowserPanel.getBaseWebElement.setText("基本元素");
            LoadBrowserPanel.getSpecialWebElement.setText("插件元素");
        }
    }


}
