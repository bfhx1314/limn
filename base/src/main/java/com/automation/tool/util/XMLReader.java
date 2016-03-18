package com.automation.tool.util;


import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * 读写XML文件
 * 
 * @author tangxy
 *
 */
public class XMLReader {
	private SAXReader saxReader = null;
	private Document document = null;
	public String inputPath = null;
	
	
	/**
	 * 
	 * @param inputPath  
	 * @param isPath true路径  false内容
	 */
	public XMLReader(String inputPath, boolean isPath){
		if(isPath==true){
			this.inputPath = inputPath;
			try{
				saxReader = new SAXReader();
				document = saxReader.read(inputPath);		
			}catch(DocumentException e){
				System.out.println(e.getMessage());
			}
		}else{
			try {
				document = DocumentHelper.parseText(inputPath);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 取得XML中定义的Template个数
	 * @return
	 */
	public int getTemplateCount(){
		int count = 0;
		@SuppressWarnings("unchecked")
		List<Node> list = document.selectNodes("//Template");
		count = list.size();
		return count;
	}
	
	/**
	 * 根据Template在XML中的序号取得其下对应节点的值
	 * @param index Template在XML中的序号(从0开始)
	 * @return HashMap的key代表其下的节点名称,value表示节点内容
	 * @throws org.dom4j.DocumentException
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getNodeValueByTemplateIndex(int index){
		HashMap<String, String> templateMap = new HashMap<String, String>();
		List<Element> templateList = document.selectNodes("//Template");
		Element templateElement = templateList.get(index);
		Iterator<Element> templateIterator = templateElement.elementIterator();

		// 取得template(index)下所有子节点的节点名和值,添加到hashMap中
		while (templateIterator.hasNext()) {
			Element node = templateIterator.next();
//			templateMap.put(node.getName(), Variable.resolve(node.getText()));
			templateMap.put(node.getName(), node.getText());
		}
		return templateMap;
	}

	/**
	 * 根据TemplateIndex在XML中的序号取得其指向core节点其下的所有节点信息
	 * @param index Template在XML中的序号(从0开始)
	 * @return HashMap的key代表其对应core节点下的节点名称,value表示节点内容
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String,String> getCoreInfoByTemplateIndex(int index){
		HashMap<String, String> coreMap = new HashMap<String, String>();
		List<Element> templateList = document.selectNodes("//Template");
		Element templateElement = templateList.get(index);
		String core = null;

		// 取得template(index)的指定coreId
		Iterator<Attribute> templateAttribute = templateElement.attributeIterator();
		int flag = 0;
		while (templateAttribute.hasNext() && flag == 0) {
			Attribute attribute = templateAttribute.next();
			if (attribute.getName().equalsIgnoreCase("Execution")) {
				// 根据Execution="true"的Template取出对应的Core
				if (templateElement.attributeValue("Execution")
						.equalsIgnoreCase("true")) {
					core = templateElement.attributeValue("Core");
					flag = 1;
				}
			}
		}
		// 根据coreID信息,得到core节点下所有子节点的节点名和值,添加到hashMap中
		if (core != null && core != "") {
			Node coreNode = document.selectSingleNode("//Core[@id='" + core
					+ "']");

			Element coreElement = (Element) coreNode;
			Iterator<Element> coreIterator = coreElement.elementIterator();

			while (coreIterator.hasNext()) {
				Element node = coreIterator.next();
//				coreMap.put(node.getName(), Variable.resolve(node.getText()));
				coreMap.put(node.getName(), node.getText());
			}
		}

		return coreMap;
	}


	/**
	 * 根据Template在XML中的序号设置其下对应节点的值
	 * @param index Template在XML中的序号(从0开始)
	 * @param nodeName 节点的名称
	 * @param nodeValue 节点的值
	 * @throws org.dom4j.DocumentException
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("unchecked")
	public void setNodeValueByTemplateIndex(int index, String nodeName,
			String nodeValue) throws DocumentException, IOException {
		List<Element> list = document.selectNodes("//Template");
		Element element = list.get(index);
		Iterator<Element> iterator = element.elementIterator();


		boolean isElementExist = false;
		while (iterator.hasNext()) {
			Element node = iterator.next();
			if (node.getName().equals(nodeName)) {
				node.setText(nodeValue);
				isElementExist = true;
			}
		}

		//如果不存在加入此节点
		if(!isElementExist){
			element.addElement(nodeName).setText(nodeValue);
		}


		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		XMLWriter output = new XMLWriter(new FileOutputStream(inputPath), format);
		output.write(document);
		output.close();

	}

	/**
	 * 情况core下的属性
	 * @param index
	 * @throws org.dom4j.DocumentException
	 * @throws java.io.IOException
	 */
	public void clearCoreInfo(int index) throws IOException, DocumentException {
		List<Element> list = document.selectNodes("//Core");
		if(list.isEmpty()){
			List<Element> cores = document.selectNodes("//Cores");
			if(cores.isEmpty()){
				List<Element> roots = document.selectNodes("//QTP");
				Element root = roots.get(0);
				root.addElement("Cores");
				cores = document.selectNodes("//Cores");
			}
			Element coresNode = cores.get(0);
			coresNode.addElement("Core").addAttribute("id", "s1");
			List<Element> template = document.selectNodes("//Template");
			template.get(0).addAttribute("id", "s1");
			template.get(0).addAttribute("core", "s1");
			list = document.selectNodes("//Core");
		}
		Element element = list.get(index);
		Iterator<Element> iterator = element.elementIterator();
		while (iterator.hasNext()) {
			Element node = iterator.next();
			element.remove(node);
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		XMLWriter output = new XMLWriter(new FileOutputStream(inputPath), format);
		output.write(document);
		output.close();
	}


	/**
	 *根据Core中的设置逐一添加
	 * @param index Core在XML中的序号(从0开始)
	 * @param nodeName 节点的名称
	 * @param nodeValue 节点的值
	 * @throws org.dom4j.DocumentException
	 * @throws java.io.IOException
	 */
	public void setNodeValueByCoreIndex(int index, String nodeName,
			String nodeValue) throws  DocumentException, IOException{
		List<Element> list = document.selectNodes("//Core");
		Element element = list.get(index);		
		element.addElement(nodeName).setText(nodeValue);		
		OutputFormat format = OutputFormat.createPrettyPrint();   
		format.setEncoding("utf-8");  
		XMLWriter output = new XMLWriter(new FileOutputStream(inputPath), format);	
		output.write(document);
//		output.close();
	}
	
	
	public String getNodeValueByTagName(String element){
		return document.selectSingleNode("//" + element).getText();

	}
	
}
