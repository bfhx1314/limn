package com.limn.frame.control.interfaceframe;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.external.JSONViewer;
import com.limn.tool.httpclient.HttpClientCommon;


public class InterfaceTestEditPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel title = new JPanel();
	private JLabel nameLabel = new JLabel("名称:");
	private JTextField name = new JTextField();
	private JLabel annotationLabel = new JLabel("注释:");
	private JTextField annotation = new JTextField();
	
	private JPanel webServies = new JPanel();
	private JLabel iplabel = new JLabel("服务器名称或IP:");
	private JTextField ip = new JTextField();
	private JLabel portLabel = new JLabel("端口:");
	private JTextField port = new JTextField();
	
	private JPanel httpRequest = new JPanel();
	private JLabel urlLabel = new JLabel("请求地址:");
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JTextField url = new JTextField();
	private JTable parameters = new JTable();
	private DefaultTableModel testCaseModel = new DefaultTableModel();
	private JScrollPane testCaseJScroll = new JScrollPane();
	
	private JScrollPane responseJsonJScroll = new JScrollPane();
	
//	private JButton request = new JButton("请求");
	
//	private JButton save = new JButton("保存");
	
	private String parametersPath = System.getProperty("user.dir") + "/InterfaceData";
	
	private boolean isCreateNew = false;
	
	public InterfaceTestEditPanel(){
		this.setLayout(null);
		JButton request = new JButton("请求");
		//请求
		addComponent(this,request,620,20,60,20);
		request.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				request();
				
			}
		});
		
//		JButton save = new JButton("保存");
//		addComponent(this,save,620,50,60,20);
//		save.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				save("");
//				//TODO
//				
//			}
//		});
		
		title.setBorder(javax.swing.BorderFactory.createTitledBorder("接口名称"));
		title.setLayout(null);
		
		addComponent(this,title,10,10,590,90);
		
		addComponent(title,nameLabel,20,25,50,20);
		addComponent(title,name,70,25,500,20);
		
		addComponent(title,annotationLabel,20,55,50,20);
		addComponent(title,annotation,70,55,500,20);
		
		
		webServies.setBorder(javax.swing.BorderFactory.createTitledBorder("WEB服务器"));
		webServies.setLayout(null);
		
		addComponent(this,webServies,10,110,590,60);
		
		addComponent(webServies,iplabel,20,25,100,20);
		addComponent(webServies,ip,130,25,300,20);
		
		addComponent(webServies,portLabel,450,25,40,20);
		addComponent(webServies,port,500,25,60,20);
		
		port.addKeyListener(new KeyAdapter(){  
            public void keyTyped(KeyEvent e) {  
                int keyChar = e.getKeyChar();                 
                if(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9){  
                      
                }else{  
                    e.consume(); //关键，屏蔽掉非法输入  
                }  
            }  
        });  
		
		
		
		
		httpRequest.setBorder(javax.swing.BorderFactory.createTitledBorder("HTTP请求"));
		httpRequest.setLayout(null);
		
		addComponent(this,httpRequest,10,180,590,350);
		addComponent(httpRequest,urlLabel,20,25,100,20);
		addComponent(httpRequest,url,80,25,500,20);
		
		addComponent(httpRequest,tabbedPane,10,55,500,20);
		
		testCaseJScroll.setViewportView(parameters);
		
		testCaseModel.addColumn("参数名称");
		testCaseModel.addColumn("值");
		testCaseModel.addColumn("备注");
		parameters.setModel(testCaseModel);
		parameters.setRowHeight(20);
		addComponent(httpRequest,tabbedPane,10,55,570,260);
		
		tabbedPane.add("参数", testCaseJScroll);
		
		
		tabbedPane.add("Response",responseJsonJScroll);
		
		
		
		JButton addStep = new JButton("添加");
		JButton deleteStep = new JButton("删除");
		
		addComponent(httpRequest,addStep,100,320,60,20);
		addComponent(httpRequest,deleteStep,170,320,60,20);
		
		
		parameters.putClientProperty("terminateEditOnFocusLost",	Boolean.TRUE);
		
//		parameters.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		
		addStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = -1;
				if (parameters.getSelectedRow() != -1) {
					row = parameters.getSelectedRow() + 1;
				} else {
					row = parameters.getRowCount();
				}
				testCaseModel.insertRow(row, new Object[] {"","",""});
//				parameters.setModel(testCaseModel);
				if(row==0){
//					parameters.setRowSelectionInterval(1, 1);
				}else{
					parameters.setRowSelectionInterval(row, row);
				}
				
			}
		});

		deleteStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (parameters.getSelectedRow() != -1) {
					int row = parameters.getSelectedRow();
					deleteRow(row);
				}
			}
		});
	
		
		setEditable(false);
		
	}

	
	/**
	 * 添加控件
	 * @param jComp
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void addComponent(JComponent jComp, Component comp, int x, int y, int width, int height){
		jComp.add(comp);
		comp.setBounds(x, y, width, height);
	}
	
	
	private void deleteRow(int index) {
		testCaseModel.removeRow(index);
		if (testCaseModel.getRowCount() != 0
				&& testCaseModel.getRowCount() > index) {
			parameters.setRowSelectionInterval(index, index);
		} else if (testCaseModel.getRowCount() != 0
				&& testCaseModel.getRowCount() <= index) {
			parameters.setRowSelectionInterval(index - 1, index - 1);
		}
		parameters.setModel(testCaseModel);
	}
	
	/**
	 * 请求
	 */
	public void request(){
		String ipValue = ip.getText();
		String portValue = port.getText();
		String urlValue = url.getText();
		if(ipValue.isEmpty()){
			JOptionPane.showMessageDialog(this,"IP不能为空");
		}else if(portValue.isEmpty()){
			JOptionPane.showMessageDialog(this,"端口不能为空");
		}else if(urlValue.isEmpty()){
			JOptionPane.showMessageDialog(this,"请求地址不能为空");
		}else{
		
			HashMap<String,String> data = new HashMap<String,String>();
			for (int rowCount = 0; rowCount < parameters.getRowCount(); rowCount++) {
				String parameter = parameters.getValueAt(rowCount, 0).toString();
				String value = parameters.getValueAt(rowCount, 1).toString();
				
				parameter = parameter == null ? "" : parameter;
				value = value == null ? "" : value;
				data.put(parameter,value);
			}
			
			
			String result = HttpClientCommon.getResult(ipValue, Integer.valueOf(portValue), urlValue, data);
			System.out.println(result);
			
			responseJsonJScroll.setViewportView(new JSONViewer(result));
		}
	}
	
	/**
	 * 保存界面数据到本地xml
	 * @param path
	 */
	public String save(String path){
		String ipValue = ip.getText();
		String portValue = port.getText();
		String urlValue = url.getText();
		String nameValue = name.getText();
		String annotationValue = annotation.getText();
		if(nameValue.isEmpty()){
			JOptionPane.showMessageDialog(this,"名称不能为空");
			return null;
		}
		String filePath = parametersPath + path;
		File paraFile = new File(filePath);
		if(paraFile.exists()){
			
			filePath = parametersPath + path + "/" + nameValue + ".xml";
			if(new File(filePath).exists() && isCreateNew){
				JOptionPane.showMessageDialog(this,"名称不能重复");
				return null;
			}
	
		}else{
			paraFile.mkdirs();
		}
		Document document = DocumentHelper.createDocument();  
		Element root = document.addElement("root");
		
		Element IPElement = root.addElement("ip");  
		IPElement.setText(ipValue);
		
		Element portElement = root.addElement("port");
		portElement.setText(portValue);
		
		Element urlElement = root.addElement("url");
		urlElement.setText(urlValue);
		
		Element nameElement = root.addElement("name");
		nameElement.setText(nameValue);
		
		Element annotationElement = root.addElement("annotation");
		annotationElement.setText(annotationValue);

		//参数列表
		Element parametersElement = root.addElement("parameters");
		for (int rowCount = 0; rowCount < parameters.getRowCount(); rowCount++) {
			String parameter = parameters.getValueAt(rowCount, 0).toString();
			String value = parameters.getValueAt(rowCount, 1).toString();
			String annotation = parameters.getValueAt(rowCount, 2).toString();
			
			parameter = parameter == null ? "" : parameter;
			value = value == null ? "" : value;
			annotation = annotation == null ? "" : annotation;
			
			
			Element parameterElement = parametersElement.addElement("parameter");
			parameterElement.addAttribute("name", parameter);
			parameterElement.addAttribute("value", value);
			parameterElement.addAttribute("annotation", annotation);

		}
		
		//保存
		saveDocument(document,filePath);
		setEditable(false);
		
		if(isCreateNew){
			isCreateNew = false;
			return nameValue;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 加载xml数据至界面
	 */
	public void load(String filePath){
		isCreateNew = false;
		filePath = parametersPath + "/" + filePath + ".xml";
		if(!new File(filePath).exists()){
			JOptionPane.showMessageDialog(this,"本地数据丢失");
			return;
		}
		SAXReader saxReader = null;
		Document document = null;
		try{
			saxReader = new SAXReader();
			document = saxReader.read(filePath);		
		}catch(DocumentException e){
			System.out.println(e.getMessage());
		}
		Element IPElement = (Element) document.selectSingleNode("/root/ip");
		Element portElement = (Element) document.selectSingleNode("/root/port");
		Element urlElement = (Element) document.selectSingleNode("/root/url");
		Element nameElement = (Element) document.selectSingleNode("/root/name");
		Element annotationElement = (Element) document.selectSingleNode("/root/annotation");

		ip.setText(IPElement.getText());
		port.setText(portElement.getText());
		url.setText(urlElement.getText());
		name.setText(nameElement.getText());
		annotation.setText(annotationElement.getText());
		
		
		@SuppressWarnings("unchecked")
		List<Element> parametersElement = document.selectNodes("//parameter");
		Iterator<Element> parametersIterator = parametersElement.iterator();
		int rowIndex = 0;
		clearParameters();
		while (parametersIterator.hasNext()) {
			Element node = parametersIterator.next();
			String name = node.attribute("name").getText();
			String value = node.attribute("value").getText();
			String annotation = node.attribute("annotation").getText();
			testCaseModel.insertRow(rowIndex, new Object[]{name,value,annotation});
			rowIndex ++ ;
		}
	}
	
	
	/**
	 * 保存document
	 * @param doc
	 * @param outputPath
	 */
    private void saveDocument(Document doc, String outputPath) {  
    	
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			XMLWriter output = new XMLWriter(new FileOutputStream(outputPath), format);
			output.write(doc);
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    } 
    
    /**
     * 创建新的接口
     */
    public void createInterface(){
    	
    	isCreateNew = true;
    	name.setText("");
    	annotation.setText("");
    	ip.setText("");
    	port.setText("");
    	url.setText("");
    	clearParameters();
    	setEditable(true);
    	
    }
    
    private void clearParameters(){
		int rowCount = testCaseModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			testCaseModel.removeRow(0);
		}
		parameters.setModel(testCaseModel);
    }
    
    public void delete(String path){
    	new File(path + ".xml").delete();
    	clearParameters();
    }
    
    
    
    public void setEditable(boolean b){
    	ip.setEditable(b);
    	port.setEditable(b);
    	name.setEditable(b);
    	annotation.setEditable(b);
    	url.setEditable(b);
    	parameters.setEnabled(b);
    }
    
    
    
    
    
    
}
