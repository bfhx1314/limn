package com.limn.frame.panel;


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
import javax.swing.tree.TreeModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	//关键字树的滚动条
	private JScrollPane keyWordTreeJSP = null;
	//关键字注释
	private static HashMap<String,String> keyWordAnnotate = new HashMap<String,String>();
	//关键字
	private static LinkedHashMap<String,String> keyWord = new LinkedHashMap<String,String>();
	
	
	private JTextPane helpPane = new JTextPane();
	private JScrollPane helpPaneJSP = new JScrollPane(helpPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
	private static DefaultMutableTreeNode keyWordNode = new DefaultMutableTreeNode("关键字列表"); 

	private DebugEditFrame def = null;
	public KeyWordPanel(DebugEditFrame def){


		setBounds(0, 0, 635, 395);
		setLayout(null);
		this.def = def;
		keyWordTree = new JTree(keyWordNode);
		
		keyWordTreeJSP = new JScrollPane(keyWordTree, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		keyWordTree.addMouseListener(new MouseAdapter() {
	
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) keyWordTree.getLastSelectedPathComponent();//返回最后选定的节点
				if(null == selectedNode){
					return ;
				}
				String name = selectedNode.toString();
				if(keyWordAnnotate.containsKey(keyWord.get(name))){
					helpPane.setText(keyWordAnnotate.get(keyWord.get(name)));
					def.setStepTextArea(name + ":");
				}
			}
		});
		
		helpPane.setContentType("text/html");
		helpPane.setEditable(false);
		
		setBoundsAtPanel(keyWordTreeJSP,0,5,200,390);
		setBoundsAtPanel(helpPaneJSP,205,5,430,390);
		
	}
	
	public static void addKeyWord(String key, Class<?> keyType){
		DefaultMutableTreeNode moduleTree = new DefaultMutableTreeNode(key);
		setKeyWord(moduleTree,keyType);
		setKeyWordAnnotate(keyType);
		keyWordNode.add(moduleTree);
	}
	
	
	
	/**
	 * 获取关键字列表
	 * @return
	 */
	private static void setKeyWord(DefaultMutableTreeNode moduleTree, Class<?> keyType){

		LinkedHashMap<String,String> keyWordTmpe = new LinkedHashMap<String,String>();
		try {

			Field[] fields = keyType.getDeclaredFields();
			for (Field f : fields) {
				if (f.getGenericType().toString().equals("class java.lang.String")) {
					keyWordTmpe.put((String)f.get(keyType),f.getName());
					keyWord.put((String)f.get(keyType),f.getName());
					moduleTree.add(new DefaultMutableTreeNode((String)f.get(keyType)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
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



	private static void setKeyWordAnnotate(Class<?> keyType){
	
		InputStream is = keyType.getClassLoader().getResourceAsStream(  
                "javadoc/" + keyType.getSimpleName() + ".html");
		if(null == is){
			return;
		}
		Document doc = null;
		try {
			doc = Jsoup.parse(is, "UTF-8", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements codes = doc.getElementsByTag("H4");
		for(Element code:codes){
			if(!code.equals(keyType.getSimpleName())){
				String value = "";
				try{
					value = code.nextElementSibling().nextElementSibling().outerHtml();
				}catch(Exception e){
					
				}
				keyWordAnnotate.put(code.text(),value);
			}
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
}

