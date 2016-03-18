package com.limn.client;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.limn.client.remote.RemoteClient;
import com.limn.tool.common.Common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.log.PrintLogDriver;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
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
	
	private PrintLogDriver printLog = null;
	
	/**
	 * 客户端
	 */
	public Client() {
		init();
	}

    public Client(String ip){
        serverIP = ip;
        init();
        connectServer();
    }

    public Client(String ip,PrintLogDriver printLog) throws ConnectException {
        serverIP = ip;
        init();
        if(!connectServer()){
            throw new ConnectException("链接服务器失败");
        }
        this.printLog = printLog;
    }
	
	private void init(){
		//去上次用户成功执行的配置信息
		templatePath = getTemplatePath();
		
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().height);

		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width);

		seleniumClient.setBounds((int) ((screenWidth - 500) * 0.5),
				(int) ((screenHeight - 300) * 0.5), 500, 300);

		seleniumClient.setLayout(null);
		
		
		JSeparator hs = new JSeparator(SwingConstants.VERTICAL);
		setBoundsAt(hs, 250, 20, 10, 230);
		
		JLabel titleConnect = new JLabel("受控客户端连接");
		setBoundsAt(titleConnect, 80, 5, 100, 30);
		try{
			Class.forName("com.limn.frame.control.BeforeTest");

			setBoundsAt(connectIPLabel, 20, 50, 60, 30);
			setBoundsAt(connectIP, 80, 56, 150, 22);
			setBoundsAt(connectPortLabel, 20, 96, 60, 30);
			setBoundsAt(connectPort, 80, 100, 60, 22);
			connectPort.setEditable(false);
			setBoundsAt(submit, 100, 180, 60, 30);
			setBoundsAt(infor, 110, 215, 180, 30);
			infor.setForeground(Color.RED);
			connectPort.setText(String.valueOf(serverPort));
		}catch(ClassNotFoundException e){
			JLabel message = new JLabel("缺失功能模块,不能加载该功能!");
			setBoundsAt(message, 40, 80, 200, 30);
			message.setForeground(Color.YELLOW.darker());
		}	


		JLabel titleRemote = new JLabel("受控远程客户端");
		JButton submitRemote  = new JButton("启动");
		setBoundsAt(titleRemote, 330, 5, 100, 30);
		setBoundsAt(submitRemote, 350, 180, 60, 30);
		
		submitRemote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seleniumClient.dispose();
				new Thread(new RemoteClient()).start();
			}
		});
		

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
                connectServer();
			}
		});

		try {
			loadParameters();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

	}
	
	
	private void setBoundsAt(Component comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		seleniumClient.add(comp);
	}

    /**
     * 链接服务器验证
     * @return
     */
    private boolean connectServer(){
        if (connectIP.getText().isEmpty()) {
            infor.setText("连接地址不能为空");
        } else if (connectPort.getText().isEmpty()) {
            infor.setText("端口不能为空");
        } else {
            infor.setText("");
            serverIP = connectIP.getText();
            serverPort = Integer.parseInt(connectPort.getText());
            if (verConnectServer()) {
                saveParameter();
                seleniumClient.dispose();
                getInformation();
                return true;
            }
        }
        return false;
    }


    public void sendLog(String log) throws IOException {
        
        dos.writeUTF(log);
    }


	/**
	 * 连接的判断
	 * @return
	 */
	private boolean verConnectServer() {
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
//			String version = dis.readUTF();
//			String tmpXML = dis.readUTF();
            dos.writeUTF("start");
			while(true) {
                dos.writeUTF("Success");
                Common.wait(1000);
            }
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
