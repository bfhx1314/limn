package com.limn.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.log.PrintLogDriver;
import com.limn.log.RunLog;
import com.limn.log.RunLogDriver;
import com.limn.parameter.Parameter;
import com.limn.tool.external.XMLReader;



/**
 * 连接服务器的客户端
 * @author limn
 *
 */
public class Client {

	DataOutputStream dos = null;
	DataInputStream dis = null;

	private JFrame seleniumClient = new JFrame("Selenium Client");

	private JTextField connectIP = new JTextField();

	private JTextField connectPort = new JTextField();

	private JButton submit = new JButton("连接");

	private JLabel connectIPLabel = new JLabel("连接地址:");

	private JLabel connectPortLabel = new JLabel("端口号:");

	private JLabel infor = new JLabel();

	private String serverIP = null;

	private int serverPort = Parameter.SERVERPORT;

	private Socket sc = null;

	public static RunLog rl = null;
	
	private String templatePath = null;
	
	private PrintLogDriver printLog = new RunLogDriver();
	
	/**
	 * 客户端
	 */
	public Client() {
		init();
	}
	
	public Client(PrintLogDriver printLog){
		this.printLog = printLog;
		init();
	}
	
	
	private void init(){
		//去上次用户成功执行的配置信息
		templatePath = getTemplatePath();
		
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);

		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width);

		seleniumClient.setBounds((int) ((screenWidth - 300) * 0.5),
				(int) ((screenHeight - 300) * 0.5), 300, 300);

		seleniumClient.setLayout(null);
		connectIPLabel.setBounds(20, 50, 100, 30);
		connectIP.setBounds(100, 50, 180, 30);
		connectPortLabel.setBounds(20, 100, 100, 30);
		connectPort.setBounds(100, 100, 180, 30);
		submit.setBounds(100, 170, 100, 30);
		infor.setBounds(80, 220, 180, 30);

		
		connectPort.setText(String.valueOf(serverPort));
		
		seleniumClient.add(connectIP);
//		seleniumClient.add(connectPort);
		seleniumClient.add(submit);
		seleniumClient.add(connectIPLabel);
//		seleniumClient.add(connectPortLabel);
		seleniumClient.add(infor);

		infor.setForeground(Color.RED);

		seleniumClient.setVisible(true);
		seleniumClient.setAlwaysOnTop(true);
		seleniumClient.setResizable(false);
		seleniumClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		connectPort.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (connectIP.getText().isEmpty()) {
					infor.setText("连接地址不能为空");
				} else if (connectPort.getText().isEmpty()) {
					infor.setText("端口不能为空");
				} else {
					infor.setText("");
					serverIP = connectIP.getText();
					serverPort = Integer.parseInt(connectPort.getText());
					if (connectServer()) {
						saveParameter();
						seleniumClient.dispose();
						getInformation();
					}
				}
			}
		});
		
		
		
		try {
			loadParameters();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 连接的判断
	 * @return
	 */
	private boolean connectServer() {
		boolean connect = false;
		try {
			sc = new Socket(serverIP, serverPort);
			infor.setText("连接成功");
			connect = true;
			
		} catch (UnknownHostException e) {
			infor.setText("未知的连接地址");
		} catch (IOException e) {
			infor.setText("连接失败");
		}
		return connect;
	}

	/**
	 * 加载上次成功的配置
	 * @throws DocumentException
	 */
	private void loadParameters() throws DocumentException{

		XMLReader xml = new XMLReader(templatePath,true);
		HashMap<String, String> hm = xml.getNodeValueByTemplateIndex(0);
		if(!hm.get("Computer").isEmpty()){
			connectIP.setText(hm.get("Computer"));
		}
		if(!hm.get("Port").isEmpty()){
			connectPort.setText(hm.get("Port"));
		}
		
	}
	
	/**
	 * 保存设置配置
	 */
	private void saveParameter(){

		XMLReader xml = new XMLReader(templatePath,true);
		try {
			xml.setNodeValueByTemplateIndex(0, "Computer",connectIP.getText() );
			xml.setNodeValueByTemplateIndex(0, "Port",connectPort.getText() );
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取配置文件内容
	 * @return 文件内容
	 */
	private String getTemplatePath() {
		String templatePath = null;
		File file = new File(Parameter.DEFAULT_TEMP_PATH + "\\ClinetParameter.xml");
		// 判断系统目录下是否存在模板文件
		if (!file.exists()) {
			// 不存在就将jar包里的ParameterValues.xml复制到指定路径下
			File f = new File(Parameter.DEFAULT_TEMP_PATH);
			f.mkdirs();
			URL parameterPath = this.getClass().getResource("ClinetParameter.xml");
			try {
				Document document = new SAXReader().read(parameterPath.toString());
				try {
					FileOutputStream out = new FileOutputStream(file);
					OutputFormat format = OutputFormat.createPrettyPrint();
					format.setEncoding("utf-8");
					XMLWriter output = new XMLWriter(out, format);
					try {
						output.write(document);
						output.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		} 
		templatePath = file.toString();
		return templatePath;
	}
	
	/**
	 * 运行服务器返回的执行文件
	 */
	private void getInformation(){

		try {
			dis = new DataInputStream(sc.getInputStream());
			dos = new DataOutputStream(sc.getOutputStream());
			String version = dis.readUTF();
			String tmpXML = dis.readUTF();
			
			dos.writeUTF("Success");
			//设置补丁信息
			Parameter.VERSION = version;
			
//			printLog
//			UpdatePlat.updateByVersion(version);
			//运行测试
//			new ModularTest(tmpXML, false, sc);
			//TODO 运行借口
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
