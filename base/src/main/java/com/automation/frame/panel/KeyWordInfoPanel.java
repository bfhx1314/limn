package com.automation.frame.panel;

import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by snow.zhang on 2015-10-30.
 */
public class KeyWordInfoPanel extends CustomPanel {

    /**
     * 关键字树
     */
    private static JTree keyWordTree;
    /**
     * 关键字列表
     */
    private static DefaultMutableTreeNode keyWordNode = new DefaultMutableTreeNode("关键字列表");
    /**
     * 关键字树的滚动条
     */
    private JScrollPane keyWordTreeJSP;

    /**
     * 关键字
     */
    private static LinkedHashMap<String,String> keyWord = new LinkedHashMap<>();
    /**
     * 关键字注释
     */
    private static HashMap keyWordAnnotate = new HashMap<>();
    /**
     * 关键字帮助面板
     */
    private JTextPane helpPanel = new JTextPane();
    /**
     * 关键字实现信息
     */
    private JScrollPane helpPanelJSP = new JScrollPane(helpPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


    public KeyWordInfoPanel(){
        // 增加关键字树
        keyWordTree = new JTree(keyWordNode);
        keyWordTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) keyWordTree.getLastSelectedPathComponent();
                if (null == selectedNode){
                    return;
                }
                String nodeName = selectedNode.toString();
                if (keyWord.containsKey(nodeName)){
                    String key = keyWord.get(nodeName);
                    // 自动选择关键字
                    ExecuteSingleStepEditPanel.setKeyWordSelectedValue(nodeName);
//                    DebugEditFrame.setStepSelect(nodeName);
                    if(keyWordAnnotate.containsKey(key)){
                        helpPanel.setText(keyWordAnnotate.get(key).toString());
                    }
                }

            }
        });
        keyWordTreeJSP = new JScrollPane(keyWordTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.addComponent(keyWordTreeJSP,0,0,1,1,0.3d,1d);
        // 增加关键字帮助面板
        this.addComponent(helpPanelJSP,0,1,1,1,0.7d,1d);
        // 设置panel属性。
        helpPanel.setContentType("text/html");
        helpPanel.setEditable(false);
    }

    /**
     * 把关键字放入keyWordTree
     * @param moduleTree 树节点
     * @param keyType 关键字信息
     */
    private static void setKeyWord(DefaultMutableTreeNode moduleTree, Class<?> keyType) {
        LinkedHashMap<String,String> keyWordTmpe = new LinkedHashMap<String,String>();
        Field[] fields = keyType.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    String fieldKey = field.get(keyType).toString();
                    String fieldName = field.getName();
                    keyWordTmpe.put(fieldKey, fieldName);
                    keyWord.put(fieldKey, fieldName);
                    // 增加关键字节点
                    moduleTree.add(new DefaultMutableTreeNode(fieldKey));
                    // 添加关键字到list
                    setKeyWords(fieldKey);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把关键字描述放入helpPane
     * @param keyType 关键字信息
     */
    private static void setKeyWordAnnotate(Class<?> keyType) {
        InputStream is = keyType.getClassLoader().getResourceAsStream(
                "javadoc/" + keyType.getSimpleName() + ".html");
        if(null == is){
            return;
        }
        Document doc;
        try {
            doc = Jsoup.parse(is, "UTF-8", "");
            if (null == doc){
                // TODO 提示
                return;
            }
            Elements codes = doc.getElementsByTag("H4");
            for(Element code:codes){
                if(!code.equals(keyType.getSimpleName())){
                    Element keyWordNode = code.nextElementSibling().nextElementSibling();
                    if (null != keyWordNode){
                        String value = keyWordNode.outerHtml();
                        keyWordAnnotate.put(code.text(),value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加关键字信息
     * @param keyWord 关键字归属名称
     * @param keyWordType 关键字描述
     */
    public static void addKeyWord(String keyWord, Class<?> keyWordType) {
        DefaultMutableTreeNode moduleTree = new DefaultMutableTreeNode(keyWord);
        setKeyWord(moduleTree,keyWordType);
        setKeyWordAnnotate(keyWordType);
        keyWordNode.add(moduleTree);
    }
}
