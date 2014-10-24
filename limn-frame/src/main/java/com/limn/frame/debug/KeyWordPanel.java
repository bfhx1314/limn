package com.limn.frame.debug;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.limn.frame.debug.CustomPanel;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.random.RandomData;

/**
 * 关键字帮助面板
 * @author limn
 *
 */
public class KeyWordPanel extends CustomPanel {

	private static final long serialVersionUID = 1L;
	
	//关键字树
	private JTree keyWordTree = null;
	private JScrollPane keyWordTreeJSP = null;
	private HashMap<String,String> keyWordAnnotate = new HashMap<String,String>();
	private LinkedHashMap<String,String> keyWord = new LinkedHashMap<String,String>();
	private JTextPane helpPane = new JTextPane();
	private JScrollPane helpPaneJSP = new JScrollPane(helpPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private Class<?> keyType = null;
	
	public KeyWordPanel(Class<?> keyType){
		this.keyType = keyType;
		setBounds(0, 0, 635, 395);
		setLayout(null);
		DefaultMutableTreeNode keyWordNode = new DefaultMutableTreeNode("关键字列表"); 
		keyWord = getKeyWord();
		if(null != keyWord){
			for(String key:keyWord.keySet()){
				keyWordNode.add(new DefaultMutableTreeNode(key));
			}
			keyWordTree = new JTree(keyWordNode);
			keyWordTreeJSP = new JScrollPane(keyWordTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			keyWordAnnotate = getKeyWordAnnotate();
			
			keyWordTree.addMouseListener(new MouseAdapter() {
		
				@Override
				public void mouseClicked(MouseEvent e) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) keyWordTree.getLastSelectedPathComponent();//返回最后选定的节点
					String name = selectedNode.toString();
					if(keyWordAnnotate.containsKey(keyWord.get(name))){
						helpPane.setText(keyWordAnnotate.get(keyWord.get(name)));
						DebugEditFrame.setStepTextArea(name + ":");
					}
				}
			});
			
			helpPane.setContentType("text/html");
			helpPane.setEditable(false);
			
			setBoundsAtPanel(keyWordTreeJSP,0,5,200,390);
			setBoundsAtPanel(helpPaneJSP,205,5,430,390);
		}
	}
	
	/**
	 * 获取关键字列表
	 * @return
	 */
	private LinkedHashMap<String,String> getKeyWord(){
		if(null == keyType){
			return null;
		}
		
		LinkedHashMap<String,String> keyWord = new LinkedHashMap<String,String>();
		try {

			Field[] fields = keyType.getDeclaredFields();
			for (Field f : fields) {
				if (f.getGenericType().toString().equals("class java.lang.String")) {
					keyWord.put((String)f.get(keyType),f.getName());		
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return keyWord;
	}
	
	
	/**
	 * 添加控件 并且设置大小
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void setBoundsAtPanel(JComponent comp,int x,int y,int width,int height){
		this.add(comp);
		comp.setBounds(x, y, width, height);
	}



	private HashMap<String,String> getKeyWordAnnotate(){
		HashMap<String,String> data = new HashMap<String,String>();
		InputStream is = keyType.getClassLoader().getResourceAsStream(  
                "javadoc/KeyWordType.html");
		Document doc = null;
		try {
			doc = Jsoup.parse(is, "UTF-8", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements codes = doc.getElementsByTag("H4");
		for(Element code:codes){
			if(!code.equals("KeyWordType")){
				String value = "";
				try{
					value = code.nextElementSibling().nextElementSibling().outerHtml();
//					value = value.replace("<br>", "\n");
				}catch(Exception e){
					
				}
				data.put(code.text(),value);
			}
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
		
	}
	
	public static void main(String[] args){
		
	}
	
	
	
	
	
	
	
	
	
	
	
}

