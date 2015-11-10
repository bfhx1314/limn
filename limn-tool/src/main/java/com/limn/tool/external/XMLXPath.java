package com.limn.tool.external;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.limn.tool.common.Print;


public class XMLXPath {
	
	private LinkedList<String> rule = new LinkedList<String>(); 
	private Document document = null;
	public XMLXPath(){
		rule.add("id");
	}
	
	public XMLXPath(LinkedList<String> list){
		rule = list;
	}
	
	public void setRule(LinkedList<String> list){
		rule = list;
	}
	
	public String getXPath(Element element){
		String xpath = getElementPosition(element);
		if(null == xpath){
			xpath = element.getUniquePath();
		}
//		Print.log("Xpath:" + xpath, 0);
		return xpath;
	}
	
	private String getElementPosition(Element element){
		
		document = element.getDocument();
		String elementTag = element.getName();
		
		AttributeJoin aj = new AttributeJoin();
		Iterator<String> ruleKey = rule.iterator();
		while(ruleKey.hasNext()){
			String key = ruleKey.next();
			String value = element.attributeValue(key);
			if(null != value && !value.isEmpty()){
				String xpath = "//" + elementTag + "[" + aj.add(key, value) + "]";
				if(search(xpath)){
					return xpath;
				}
			}
		}		
		return null;
	}
	
	
	/**
	 * 根据xpath是否可以找到节点
	 * @param xpath
	 * @return
	 */
	private boolean search(String xpath){
		List<Node> nodes = document.selectNodes(xpath);
		if(null != nodes && nodes.size() == 1){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 根据xpath是否可以找到节点
	 * @param xpath
	 * @param element
	 * @return
	 */
	public static boolean search(String xpath,Element element){
		List<Node> nodes = element.getDocument().selectNodes(xpath);
		if(null != nodes && nodes.size() == 1){
			return true;
		}else{
			return false;
		}
	}

	class AttributeJoin{
		
		private String history = null;
		
		public String add(String key ,String value){
			
			if(null == history){
				history = "@" + key + "='" + value + "'";
			}else{
				history = history + " and @" + key + "='" + value + "'";
			}
			return history;
		}
	}
	
	
	
	
	

}
