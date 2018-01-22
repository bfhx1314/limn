package com.limn.frame.panel;

import com.limn.app.driver.bean.AppiumStartParameterBean;
import com.limn.tool.common.BaseUntil;
import com.limn.tool.common.CallBat;
import com.limn.tool.common.Common;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.variable.Variable;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;


/**
 *
 * Created by limengnan on 2017/11/23.
 */
public class AppiumConsole extends JFrame{

    public JTextPane writeLogPane = new JTextPane();
    public JScrollPane logJScrollLog = new JScrollPane(writeLogPane,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    private  Thread  runLogAppium = null;

    public AppiumConsole(AppiumStartParameterBean aspb, String cmd){
        super("Appium" + aspb.toString());
        setResizable(false);
        writeLogPane.setEditable(false);
        add(logJScrollLog);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        addWindowListener(new WindowAdapter(){
//
//            @Override
//            public void windowClosed(WindowEvent e) {
//                super.windowClosed(e);
//                try {
//                    AppiumConsole.this.br.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
        setBounds((int) ((getScreenWidth() - 500) * 0.5), (int) ((getScreenHeight() - 600) * 0.5), 500, 600);
        setVisible(true);

//        String a =CallBat.returnExec(cmd + " > ");
//        String a =CallBat.returnExec(cmd + " > "  + Parameter.DEFAULT_TEMP_PATH +  "/appium.log");
        CallBat.exec("echo '' > " + Parameter.DEFAULT_TEMP_PATH + "/appium.log");
        CallBat.exec(cmd + " -g " + Parameter.DEFAULT_TEMP_PATH + "/appium.log");
        new Thread(new AppiumReadLog(new File(Parameter.DEFAULT_TEMP_PATH +  "/appium.log"))).start();


//        runLogAppium = new Thread(new AppiumLog(cmd));
//        runLogAppium.start();
    }


    public static void main(String[] args){
        AppiumStartParameterBean aspb = new AppiumStartParameterBean();
        aspb.setAddress("127.0.0.1");
        aspb.setPort("4723");
        String appiumPath = Variable.getExpressionValue("appium.path");
        if(BaseUntil.isNotEmpty(appiumPath)){
            new AppiumConsole(aspb,"node " + appiumPath + " " + aspb.toString());
            Common.wait(3000);
        }
    }

    /**
     * 写入日志
     * @param strng
     * @param style
     * @param style 1 green , 2 red , 3 yellow ,4 sold black,else black
     */
    private void runLogWrite(String strng, int style) {

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
            default:
                attrSetRunLog = setDocs(Color.black, false);
                break;
        }
        try {
            writeLogPane.getDocument().insertString(
                    writeLogPane.getDocument().getLength(), strng,
                    attrSetRunLog);
        } catch (BadLocationException e) {

            e.printStackTrace();
        }
//        LogInformation.appLog(strng + strngDescrption);
    }

    /**
     * 样式的设置
     * @param col
     * @param bold
     * @return
     */
    public SimpleAttributeSet setDocs(Color col, boolean bold) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        // 颜色
        StyleConstants.setForeground(attrSet, col);
        // 字体是否加粗
        if (bold == true) {
            StyleConstants.setBold(attrSet, true);
        }
        return attrSet;
    }

    /**
     * 取得当前显示器屏幕宽度
     */
    private int getScreenWidth() {
        int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        return screenWidth;
    }

    /**
     * 取得当前显示器屏幕高度
     */
    private int getScreenHeight() {
        int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        return screenHeight;


    }

    private boolean isCmdStop = false;
    BufferedReader br = null;

    class AppiumLog implements Runnable{

        private String cmd;
        public AppiumLog(String cmd){
            this.cmd = cmd;
        }

        @Override
        public void run() {
            Process p;
            try {
                //执行命令
                p = Runtime.getRuntime().exec(cmd);
                //取得命令结果的输出流
                InputStream fis=p.getInputStream();
                //用一个读输出流类去读
                InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
                //用缓冲器读行
                br=new BufferedReader(isr);
                String line=null;
                //直到读完为止
                while(!isCmdStop && (line=br.readLine())!=null) {
                    runLogWrite(line + "\n\r",0);
                }
                fis.close();
                isr.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    class AppiumReadLog implements Runnable {

        private File logFile = null;
        private long lastTimeFileSize = 0; // 上次文件大小

        public AppiumReadLog(File logFile) {
            this.logFile = logFile;
//            lastTimeFileSize = logFile.length();
        }

        private boolean flag = true;

        @Override
        public void run() {


            while (flag) {

                try {
                    RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
                    randomFile.seek(lastTimeFileSize);
                    String tmp = null;
                    while ((tmp = randomFile.readLine()) != null) {
                        runLogWrite(tmp + "\n\r", 0);
                        tmp = randomFile.readLine();
                    }
                    lastTimeFileSize = randomFile.length();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
