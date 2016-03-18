package com.automation.frame.panel;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class LogPanel extends CustomPanel {
    //日志面板
    public static JTextPane writeLogPane = new JTextPane();
    public JScrollPane logJScrollLog = new JScrollPane(writeLogPane,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    public static boolean logPanelStart = false;
    /**
     * 右击菜单
     */
    private JPopupMenu rightMenu = new JPopupMenu();

    public static Logger logger = Logger.getLogger(LogPanel.class.getName());

//    public LogPanel(){
//        init();
//    }

    public LogPanel(){
        // 增加监听
        addLogPaneListener();
        this.addComponent(logJScrollLog,1,1,1,1,1d,1d);
        logPanelStart = true;
    }

    /**
     * logJScrollLog监听
     */
    private void addLogPaneListener() {
        writeLogPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
                if (e.getButton() == 3) {
                    rightMenu = getRightMenu();
                    // 展示右击菜单，提供复制XPath、复制文本
                    rightMenu.show(writeLogPane, e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * 设置右键菜单监听
     * @return JPopupMenu
     */
    private JPopupMenu getRightMenu() {
        JPopupMenu menu = new JPopupMenu();
        final JMenuItem clearLog = new JMenuItem("ClearLog");
        clearLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLog();
            }
        });
        menu.add(clearLog);
        return menu;
    }

    /**
     * 输出
     * @param log 日志
     * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
     */
    public static void log(String log,int style){
        if(logPanelStart){
            printLog(log, style);
        }else{
            System.out.println(log);
        }
    }

    /**
     * 打印日志
     * @param log 日志
     * @param style 1 green , 2 red , 3 yellow` ,4 sold black,else black
     */
    public static void printLog(String log, int style){
        logger.info(log);
        runLogWrite(new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()) + "-->", log + "\n\r", style);

//        uploadLog(log);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getLogTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 写入日志
     * @param strng 日志
     * @param strngDescrption 描述
     * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
     */
    public static void runLogWrite(String strng, String strngDescrption, int style) {

        SimpleAttributeSet attrSetRunLog;
        switch (style) {
            case 1:
                attrSetRunLog = setDocs(Color.green.darker(), false);
                break;
            case 2:
                attrSetRunLog = setDocs(Color.red, false);
                break;
            case 3:
                attrSetRunLog = setDocs(Color.yellow.darker(), false);
                break;
            case 4:
                attrSetRunLog = setDocs(Color.black, true);
                break;
            case 5:
                attrSetRunLog = setDocs(Color.gray, false);
                break;
            default:
                attrSetRunLog = setDocs(Color.black, false);
                break;
        }
        try {
            writeLogPane.getDocument().insertString(
                    writeLogPane.getDocument().getLength(), strng + strngDescrption,
                    attrSetRunLog);
            writeLogPane.setCaretPosition(writeLogPane.getDocument().getLength());
//            writeLogPane.get
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空日志面板内容
     */
    public static void clearLog(){
        writeLogPane.setText("");
    }

    /**
     * 样式的设置
     * @param col 颜色
     * @param bold 是否加粗
     * @return SimpleAttributeSet
     */
    public static SimpleAttributeSet setDocs(Color col, boolean bold) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        // 颜色
        StyleConstants.setForeground(attrSet, col);
        // 字体是否加粗
        if (bold == true) {
            StyleConstants.setBold(attrSet, true);
        }
        return attrSet;
    }


}
