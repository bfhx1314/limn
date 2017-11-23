package com.limn.tool.external;


import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.limn.tool.regexp.RegExp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONViewer extends JTree {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public JSONViewer(JSONObject json) {	
		
		super(decodeJSONObject(json));
	}

	
	public JSONViewer(String jsonString) {	
		
		super(decodeJSONObject(jsonString));
	}
	
	private static DefaultMutableTreeNode decodeJSONObject(JSONObject json) {
		return decodeJSONObject("数据",json);
	}
	
	private static DefaultMutableTreeNode decodeJSONObject(String jsonString) {
		if(RegExp.findCharacters(jsonString,"^\\(")){
			jsonString = jsonString.substring(1, jsonString.length()-1);
		}
		JSONObject json = JSONObject.fromObject(jsonString);
		return decodeJSONObject("数据",json);
	}
	
	

	@SuppressWarnings("unchecked")
	private static DefaultMutableTreeNode decodeJSONObject(String key, Object ob) {
		JSONObject jsonObject = null;
		if (ob instanceof JSONObject) {
			jsonObject = (JSONObject) ob;

			Iterator<String> keys = jsonObject.keys();
			DefaultMutableTreeNode moduleTree = new DefaultMutableTreeNode(key);
//			System.out.println(key);
			while (keys.hasNext()) {
				key = keys.next();
				moduleTree.add(decodeJSONObject(key, jsonObject.get(key)));
			}
			return moduleTree;
		} else if (ob instanceof JSONArray) {
			DefaultMutableTreeNode moduleTree = new DefaultMutableTreeNode(key);
//			System.out.println(key);
			int i = 0;
			for (Object jsOb : (JSONArray) ob) {
				moduleTree.add(decodeJSONObject(String.valueOf(i), jsOb));
				i++;
			}
			return moduleTree;
		} else {
//			System.out.println(key + ":" + ob);
			return new DefaultMutableTreeNode(key + ":" + ob);
		}
	}

//	public static void main(String[] args) {
//		
//		JFrame a = new JFrame("xxx");
//		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
//				.getScreenSize().height);
//		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
//				.getScreenSize().width);
//		a.setBounds((int) ((screenWidth - 1000) *0.5), (int) ((screenHeight - 600) *0.5), 1000, 600);
//		JSONObject jsonObject = JSONObject.fromObject("{\"detail\":\"请求数据成功!\",\"status\":\"1\",\"data\":{\"sum\":0,\"functionDtoList\":[{\"a\":\"1\"},{\"a\":\"2\"}]}}");
//		
//		JTree b = new JSONViewer(jsonObject);
//		a.setLayout(null);
//		a.add(b);
//		b.setBounds(0, 0, 500, 500);
//		a.setLayout(null);
//		a.setResizable(false);
//		a.validate();
//		a.setVisible(true);
//
//	}

}
