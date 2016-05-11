package com.limn.frame.control;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.dom4j.DocumentException;

import com.limn.tool.common.Common;
import com.limn.tool.common.DateFormat;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.keyword.BaseAppKeyWordDriverImpl;
import com.limn.frame.keyword.BaseAppKeyWordType;
import com.limn.frame.keyword.BaseKeyWordDriverImpl;
import com.limn.frame.keyword.BaseKeyWordType;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.panel.KeyWordPanel;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;
import com.sinaapp.msdxblog.apkUtil.entity.ApkInfo;
import com.sinaapp.msdxblog.apkUtil.utils.ApkUtil;

/**
 * 控制台的页面布局
 * 
 * @author tangxy
 * 
 */
public class ConsoleFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final JFrame ConsoleFrame = null;
	// 定义界面参数存放路径
	private String templatePath = null;
	// 定义core参数存放路径
	// private String corePath = null;
	// 定义界面
	private JPanel panel = new JPanel();

	// 定义运行电脑
	private JLabel computerLabel = new JLabel("指定运行电脑:");
	private JComboBox<String> computerContent = new JComboBox<String>(new String[] { "本机", "远程" });
	private JLabel ipLablel = new JLabel("IP及服务端口号:");
	private JTextField ipContent = new JTextField();

	// 定义运行测试类型
	private JLabel runTestModelLabel = new JLabel("测试类型:");
	private JComboBox<String> runTestModelContent = new JComboBox<>(new String[] { "浏览器", "Android", "IOS" });

	// 定义运行方式
	private JLabel runModeLabel = new JLabel("脚本运行方式:");
	private JComboBox<String> runModeContent = new JComboBox<String>(new String[] { "由当前界面配置参数运行", "指定配置文件运行" });
	// 定义指定配置文件路径
	private JLabel filePathLabel = new JLabel("*配置文件存放路径:");
	private FileComboBox filePathContent = new FileComboBox(Parameter.DEFAULT_CONF_MODULE_PATH, "xml");
	private JButton filePathButton = new JButton("选择");

	// Android IOS
	private JLabel appFilePathLabel = new JLabel("APP存放路径:");
	private FileComboBox appFilePathContent = new FileComboBox(Parameter.DEFAULT_CONF_MODULE_PATH, "app");
	private JButton appFilePathButton = new JButton("选择");

	private JLabel appInfoLabel = new JLabel();

	// 定义浏览器类型
	private JLabel browserTypeLabel = new JLabel("浏览器类型:");
	private JComboBox<String> browserTypeContent = new JComboBox<String>(new String[] { "Firefox", "Chrome", "IE" });
	// 定义URL地址
	private JLabel URLLabel = new JLabel("* URL:");
	private JTextField URLContent = new JTextField();

	// 定义中间件启动路径
	// private JLabel MiddlewareLabel = new JLabel("中间件启动路径:");
	// private JTextField MiddlewareContent = new JTextField();
	// private JButton MiddlewareButton = new JButton("选择");
	// 定义Yigo存放路径
	// private JLabel YigoLabel = new JLabel("* Yigo存放路径:");
	// private JTextField YigoContent = new JTextField();
	// private JButton YigoButton = new JButton("选择");
	// 定义平台版本
	// private JLabel versionLabel = new JLabel("平台版本:");
	// private JComboBox<String> versionContent = new JComboBox<String>(new
	// String[] { "1.4", "1.6" });
	// 定义用例路径
	private JLabel testCaseLabel = new JLabel("* 用例路径:");
	private FileComboBox testCaseContent = new FileComboBox(Parameter.DEFAULT_TESTCASE_PATH, true);
	private JButton testCaseButton = new JButton("选择");
	// 定义用例执行模式
	private JLabel executeModeLabel1 = new JLabel("用例执行模式:");
	private JComboBox<String> executeModeContent = new JComboBox<String>(new String[] { "固定模式执行", "指定页面执行" });
	private JLabel sheetNumLabel1 = new JLabel("指定第");
	private JTextField sheetNumContent = new JTextField();
	private JLabel sheetNumLabel2 = new JLabel("页");
	private JLabel executeFrontStepsLabel = new JLabel("是否需要执行前置用例:");
	private JComboBox<String> executeFrontStepsContent = new JComboBox<String>(new String[] { "需要", "不需要" });
	// 定义是否需要还原数据库
	private JLabel initDBLabel = new JLabel("是否需要还原数据库:");
	private JComboBox<String> initDBContent = new JComboBox<String>(new String[] { "需要", "不需要" });
	private JLabel initDBPathLabel = new JLabel("备份文件路径:");
	private JTextField initDBPathContent = new JTextField();
	private JButton initDBPathButton = new JButton("选择");
	// 定义开始按钮
	private JButton OKButton = new JButton("开始运行");
	private JButton runLoop = new JButton("循环运行");
	private JButton CancelButton = new JButton("取消");
	// 定义文件选择框
	private JFileChooser fileChooser = new JFileChooser();

	// 调试复选框
	// private JCheckBox debugCheckBox = new JCheckBox("调试");

	// 无服务端环境
	// private JCheckBox notServer = new JCheckBox("无服务端环境");

	// 上传结果到服务器
	private JCheckBox upload = new JCheckBox("结果上传服务器");

	private JButton uploadSetting = new JButton("设置服务器");

	// private JLabel configPathLabel = new JLabel("* Config路径:");
	// private JTextField configPathContent = new JTextField();
	// private JButton configPathButton = new JButton("选择");

	private JLabel specifyCood = new JLabel("指定步骤执行:");

	private JComboBox<String> specifySwitch = new JComboBox<String>(new String[] { "不指定", "指定" });

	private JLabel specifySheetLabel = new JLabel("Sheet");
	private JTextField specifySheet = new JTextField();

	private JLabel specifyRowLabel = new JLabel("Row");
	private JTextField specifyRow = new JTextField();

	private JLabel specifyStepLabel = new JLabel("Step");
	private JTextField specifyStep = new JTextField();

	// private JCheckBox serverLabel = new JCheckBox("上传服务器日志");
	//
	// private JTextField serverIP = new JTextField();

	// private JButton ver

	// private JLabel setCoreLable = new JLabel("Core文件设置:");
	// private JComboBox<String> setCoreSwitch = new JComboBox<String>(new
	// String[] { "需要", "不需要" });
	// private JButton setCoreSetting = new JButton("设置");

	private JCheckBox reStart = new JCheckBox("重启服务器");

	// private CoreConfig cc = null;
	// private KeyWordDriver keyWordDriver = null;
	private static BaseKeyWordDriverImpl keyWordDriver = new BaseKeyWordDriverImpl();

	
	
	
	
	
	
	public ConsoleFrame() throws Exception {
		super("脚本运行参数配置界面");
		addKeyWordDriver("基础关键字", new BaseKeyWordDriverImpl(), BaseKeyWordType.class);
		addKeyWordDriver("App基础关键字", new BaseAppKeyWordDriverImpl(), BaseAppKeyWordType.class);
	}
	
	
	/**
	 * 显示运行配置界面
	 * @throws Exception
	 */
	public void showUIFrame() throws Exception {
		panel.setLayout(null);
		// 定义界面数据存放路径
		templatePath = getTemplatePath();
		// corePath = getCoreConfigPath();

		// 初始化参数的值
		try {
			loadParameters();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		// 布局运行电脑
		int y = 30;
		// computerLabel.setEnabled(false);
		// computerContent.setEnabled(false);
		// ipLablel.setEnabled(false);
		// ipContent.setEnabled(false);
		setBoundsAtPanel(computerLabel, 50, y, 130, 30);
		setBoundsAtPanel(computerContent, 180, y, 110, 30);
		setBoundsAtPanel(ipLablel, 310, y, 100, 30);
		setBoundsAtPanel(ipContent, 410, y, 220, 30);
		ipLablel.setVisible(computerContent.getSelectedIndex() == 1);
		ipContent.setVisible(computerContent.getSelectedIndex() == 1);
		computerContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (computerContent.getSelectedIndex() == 0) {
					ipLablel.setVisible(false);
					ipContent.setVisible(false);
				} else {
					ipLablel.setVisible(true);
					ipContent.setVisible(true);
				}
			}
		});
		// 布局运行方式
		y = y + 40;
		runModeLabel.setEnabled(false);
		runModeContent.setEnabled(false);
		setBoundsAtPanel(runModeLabel, 50, y, 130, 30);
		setBoundsAtPanel(runModeContent, 180, y, 450, 30);
		runModeContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runModeContent.getSelectedIndex() == 0) {
					setControlsVisible(true);
				} else {
					setControlsVisible(false);
				}
			}
		});

		// 布局测试类型
		y = y + 40;
		setBoundsAtPanel(runTestModelLabel, 50, y, 130, 30);
		setBoundsAtPanel(runTestModelContent, 180, y, 450, 30);
		runTestModelContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runTestModelContent.getSelectedIndex() == 0) {
					setTestTypeVisible(false);
				} else {
					setTestTypeVisible(true);
				}
			}
		});
		setTestTypeVisible(runTestModelContent.getSelectedIndex() != 0);

		// 布局配置文件路径
		y = y + 40;
		setBoundsAtPanel(filePathLabel, 50, y, 130, 30);
		setBoundsAtPanel(filePathContent, 180, y, 400, 30);
		setBoundsAtPanel(filePathButton, 580, y, 50, 30);
		filePathLabel.setVisible(runModeContent.getSelectedIndex() == 1);
		filePathContent.setVisible(runModeContent.getSelectedIndex() == 1);
		filePathButton.setVisible(runModeContent.getSelectedIndex() == 1);
		filePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("请选择文件路径");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				String filePath = filePathContent.getSelectedItem().toString();

				if (filePath != "") {
					if (!RegExp.findCharacters(filePath, ":")) {
						filePath = Parameter.DFAULT_TEST_PATH + "/conf_module/" + filePath;
					}
					fileChooser.setSelectedFile(new File(filePath));
				}

				int a = fileChooser.showOpenDialog(null);
				if (a == JFileChooser.APPROVE_OPTION) {
					filePathContent.setSelectedItem(fileChooser.getSelectedFile().toString());
				}
			}
		});

		// 布局appfilepath
		setBoundsAtPanel(appFilePathLabel, 50, y, 130, 30);
		setBoundsAtPanel(appFilePathContent, 180, y, 400, 30);
		setBoundsAtPanel(appFilePathButton, 580, y, 50, 30);
		appFilePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("请选择文件路径");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				String filePath = appFilePathContent.getItemCount() > 0 ? appFilePathContent.getSelectedItem().toString() : "";

				if (filePath != "") {
					fileChooser.setSelectedFile(new File(filePath));
				}
				int a = fileChooser.showOpenDialog(null);
				if (a == JFileChooser.APPROVE_OPTION) {
					// testCaseContent.setText(fileChooser.getSelectedFile().toString());
					appFilePathContent.addItem(fileChooser.getSelectedFile().toString());
					appFilePathContent.setSelectedItem(fileChooser.getSelectedFile().toString());

					try {
						ApkInfo APKInfo = new ApkUtil().getApkInfo(fileChooser.getSelectedFile().toString());
						appInfoLabel.setText(APKInfo.getApplicationLable() + ": {PackageName:" + APKInfo.getPackageName() + ",Version:" + APKInfo.getVersionName() + ",VersionCode:"
								+ APKInfo.getVersionCode() + "}");
					} catch (Exception e1) {
						// TODO
						e1.printStackTrace();
					}

				}
			}
		});

		// 布局浏览器类型
		setBoundsAtPanel(browserTypeLabel, 50, y, 130, 30);
		setBoundsAtPanel(browserTypeContent, 180, y, 450, 30);
		// 布局URL
		y = y + 40;
		setBoundsAtPanel(URLLabel, 50, y, 130, 30);
		setBoundsAtPanel(URLContent, 180, y, 450, 30);

		setBoundsAtPanel(appInfoLabel, 130, y, 600, 30);

		// 布局中间件启动路径
		// y=y+40;
		// setBoundsAtPanel(MiddlewareLabel,50,y,130,30);
		// setBoundsAtPanel(MiddlewareContent,180,y,400,30);
		// setBoundsAtPanel(MiddlewareButton,580,y,50,30);
		// MiddlewareButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// fileChooser.setDialogTitle("请选择文件路径");
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//
		// String filePath = MiddlewareContent.getText();
		//
		// if(filePath!=""){
		// fileChooser.setSelectedFile(new File(filePath));
		// }
		//
		//
		// int a = fileChooser.showOpenDialog(null);
		// if (a == JFileChooser.APPROVE_OPTION) {
		// MiddlewareContent.setText(fileChooser.getSelectedFile().toString());
		// }
		// }
		// });
		// 布局Yigo存放路径
		// y=y+40;
		// setBoundsAtPanel(YigoLabel,50,y,130,30);
		// setBoundsAtPanel(YigoContent,180,y,400,30);
		// setBoundsAtPanel(YigoButton,580,y,50,30);
		// YigoButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// fileChooser.setDialogTitle("请选择文件路径");
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//
		//
		// String filePath = YigoContent.getText();
		//
		// if(filePath!=""){
		// fileChooser.setSelectedFile(new File(filePath));
		// }
		//
		// int a = fileChooser.showOpenDialog(null);
		// if (a == JFileChooser.APPROVE_OPTION) {
		// YigoContent.setText(fileChooser.getSelectedFile().toString());
		// }
		// }
		// });
		//
		// configPathLabel.setVisible(false);
		// configPathContent.setVisible(false);
		// configPathButton.setVisible(false);
		// setBoundsAtPanel(configPathLabel,50,y,130,30);
		// setBoundsAtPanel(configPathContent,180,y,400,30);
		// setBoundsAtPanel(configPathButton,580,y,50,30);
		//
		// configPathButton.addActionListener(new ActionListener(){
		// public void actionPerformed(ActionEvent e){
		// fileChooser.setDialogTitle("请选择文件路径");
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//
		// String filePath = configPathContent.getText();
		//
		// if(filePath!=""){
		// fileChooser.setSelectedFile(new File(filePath));
		// }
		// int a = fileChooser.showOpenDialog(null);
		// if (a == JFileChooser.APPROVE_OPTION) {
		// configPathContent.setText(fileChooser.getSelectedFile().toString());
		// }
		// }
		// });
		//
		//
		//
		//
		// // 布局平台版本
		// y=y+40;
		//
		// setBoundsAtPanel(versionLabel,50,y,130,30);
		// setBoundsAtPanel(versionContent,180,y,450,30);
		// 布局用例路径
		y = y + 40;

		setBoundsAtPanel(testCaseLabel, 50, y, 130, 30);
		setBoundsAtPanel(testCaseContent, 180, y, 400, 30);
		setBoundsAtPanel(testCaseButton, 580, y, 50, 30);
		testCaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("请选择文件路径");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				String filePath = testCaseContent.getItemCount() > 0 ? testCaseContent.getSelectedItem().toString() : "";

				if (filePath != "") {
					fileChooser.setSelectedFile(new File(filePath));
				}
				int a = fileChooser.showOpenDialog(null);
				if (a == JFileChooser.APPROVE_OPTION) {
					// testCaseContent.setText(fileChooser.getSelectedFile().toString());
					testCaseContent.addItem(fileChooser.getSelectedFile().toString());
					testCaseContent.setSelectedItem(fileChooser.getSelectedFile().toString());
				}
			}
		});
		// 布局用例执行模式
		y = y + 40;

		setBoundsAtPanel(executeModeLabel1, 50, y, 130, 30);
		setBoundsAtPanel(executeModeContent, 180, y, 110, 30);
		setBoundsAtPanel(sheetNumLabel1, 310, y, 40, 30);
		setBoundsAtPanel(sheetNumContent, 350, y, 20, 30);
		setBoundsAtPanel(sheetNumLabel2, 370, y, 20, 30);
		setBoundsAtPanel(executeFrontStepsLabel, 410, y, 140, 30);
		setBoundsAtPanel(executeFrontStepsContent, 550, y, 80, 30);
		sheetNumLabel1.setVisible(executeModeContent.getSelectedIndex() == 1);
		sheetNumContent.setVisible(executeModeContent.getSelectedIndex() == 1);
		sheetNumLabel2.setVisible(executeModeContent.getSelectedIndex() == 1);
		executeFrontStepsLabel.setVisible(executeModeContent.getSelectedIndex() == 1);
		executeFrontStepsContent.setVisible(executeModeContent.getSelectedIndex() == 1);
		executeModeContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (executeModeContent.getSelectedIndex() == 0) {
					sheetNumLabel1.setVisible(false);
					sheetNumContent.setVisible(false);
					sheetNumLabel2.setVisible(false);
					executeFrontStepsLabel.setVisible(false);
					executeFrontStepsContent.setVisible(false);
				} else {
					sheetNumLabel1.setVisible(true);
					sheetNumContent.setVisible(true);
					sheetNumLabel2.setVisible(true);
					executeFrontStepsLabel.setVisible(true);
					executeFrontStepsContent.setVisible(true);
				}
			}
		});
		// 布局是否需要还原数据库
		y = y + 40;
		initDBLabel.setEnabled(false);
		initDBContent.setEnabled(false);
		initDBPathLabel.setEnabled(false);
		initDBPathContent.setEnabled(false);
		initDBPathButton.setEnabled(false);

		setBoundsAtPanel(initDBLabel, 50, y, 130, 30);
		setBoundsAtPanel(initDBContent, 180, y, 110, 30);
		setBoundsAtPanel(initDBPathLabel, 310, y, 90, 30);
		setBoundsAtPanel(initDBPathContent, 400, y, 180, 30);
		setBoundsAtPanel(initDBPathButton, 580, y, 50, 30);

		initDBContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				setinitDBContent();
			}
		});

		setinitDBContent();

		initDBPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("请选择文件路径");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int a = fileChooser.showOpenDialog(null);
				if (a == JFileChooser.APPROVE_OPTION) {
					testCaseContent.setSelectedItem(fileChooser.getSelectedFile().toString());
				}
			}
		});

		y = y + 40;
		setBoundsAtPanel(specifyCood, 50, y, 130, 30);
		setBoundsAtPanel(specifySwitch, 180, y, 110, 30);
		setBoundsAtPanel(specifySheetLabel, 320, y, 40, 30);
		setBoundsAtPanel(specifySheet, 360, y, 40, 30);
		setBoundsAtPanel(specifyRowLabel, 410, y, 40, 30);
		setBoundsAtPanel(specifyRow, 450, y, 40, 30);
		setBoundsAtPanel(specifyStepLabel, 500, y, 40, 30);
		setBoundsAtPanel(specifyStep, 540, y, 40, 30);

		specifySwitch.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				setSpecifyVisible();
			}
		});

		setSpecifyVisible();

		// try{
		// Class<?> clazz = Class.forName("com.limn.client.Client");
		// y=y+40;
		// }catch(ClassNotFoundException e){
		//
		//
		//
		// }

		// y=y+40;
		// setBoundsAtPanel(setCoreLable,50,y,130,30);
		// setBoundsAtPanel(setCoreSwitch,180,y,110,30);
		// setBoundsAtPanel(setCoreSetting,320,y,80,30);
		// setBoundsAtPanel(reStart,450,y,180,30);
		//
		// setCoreSetting.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // TODO 打开设置core文件
		// cc = new CoreConfig(corePath, templatePath);
		// cc.coreOpen();
		//
		// }
		// });
		//
		// setCoreSwitch.addItemListener(new ItemListener() {
		//
		// @Override
		// public void itemStateChanged(ItemEvent e) {
		// setCoreVisible();
		// }
		// });
		//
		// setCoreVisible();

		// 布局调试复选框
		y = y + 50;

		// notServer.setEnabled(false);
		upload.setEnabled(false);
		uploadSetting.setEnabled(false);

		// setBoundsAtPanel(debugCheckBox,175, y, 100, 30);

		// setBoundsAtPanel(notServer,175, y, 150, 30);

		setBoundsAtPanel(upload, 175, y, 120, 30);

		setBoundsAtPanel(uploadSetting, 295, y, 80, 30);
		uploadSetting.setMargin(new Insets(0, 0, 0, 0));

		uploadSetting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new UploadServerSetting(ConsoleFrame, "上传服务器参数设置", true);
			}
		});

		// 布局确定、取消按钮
		y = y + 60;

		setBoundsAtPanel(OKButton, 125, y, 100, 30);
		setBoundsAtPanel(runLoop, 250, y, 100, 30);

		setBoundsAtPanel(CancelButton, 525, y, 100, 30);

		// 循环执行
		runLoop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				start(true);

			}
		});

		// 开始执行 一遍
		OKButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Parameter.STARTTIME = DateFormat.getDateToString();
				start(false);
			}

		});
		CancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// notServer.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// setNoServerVisible();
		// }
		// });

		setNoServerVisible();

		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(panel);
		setBounds((int) ((getScreenWidth() - 700) * 0.5), (int) ((getScreenHeight() - 550) * 0.5), 700, 550);
		setVisible(true);
	}

	/**
	 * 启动执行测试
	 * 
	 * @param isLoop
	 *            是否循环执行
	 */
	private void start(boolean isLoop) {

		// 判断运行模式是否由当前配置参数
		if (runModeContent.getSelectedIndex() == 0) {
			try {
				// 当选择运行模式为:由当前界面配置参数运行时,对必填项做基本检查
				checkEmpty(URLContent.getText(), URLLabel.getText());

				if (upload.isSelected()) {
					checkUpload();
				}

				// 判断指定电脑是否本机
				if (computerContent.getSelectedIndex() == 1) {
					// 当远程运行时,检查界面输入
					checkIP(ipContent.getText());
					checkURL(URLContent.getText());
				} else {
					ipLablel.setText("");
				}
				checkKeyWordDriver();
				saveParameters();
				dispose();
				XMLReader xml = new XMLReader(templatePath, true);
				try {
					xml.clearCoreInfo(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}

				new Thread(new BeforeTest(xml.getNodeValueByTemplateIndex(0), keyWordDriver, isLoop)).start();

			} catch (ParameterException e1) {

			}
		} else {
			try {
				// 当选择运行模式为:指定配置文件运行时,对必填项做基本检查
				String xmlPath = filePathContent.getSelectedItem().toString();
				if (!RegExp.findCharacters(xmlPath, ":")) {
					xmlPath = Parameter.DFAULT_TEST_PATH + "/conf_module/" + xmlPath;
				}
				System.out.println(xmlPath);
				checkEmpty(xmlPath, filePathLabel.getText());
				checkExist(xmlPath, filePathLabel.getText());
				// 判断指定电脑是否本机
				if (computerContent.getSelectedIndex() == 1) {
					// 当远程运行时,检查界面输入,启动远程控制
					checkIP(ipContent.getText());
					checkURL(URLContent.getText());
				}
				saveParameters();
				dispose();
				// TODO 运行配置的文件
			} catch (ParameterException e1) {

			}
		}
	}
	
	/**
	 * 配置xml的地址
	 * @param xmlPath
	 */
	public void startByConfigXML(String xmlPath){
		XMLReader xml = new XMLReader(xmlPath, true);
		//TODO  
	}
	
	/**
	 * 配置的属性文件
	 * @param config
	 * @param isLoop  是否循环执行
	 */
	public void startByConfigXML(StartConfigBean scb, boolean isLoop){
		new Thread(new BeforeTest(scb, keyWordDriver, isLoop)).start();
	}
	

	public void addKeyWordDriver(String key, KeyWordDriver keyWord, Class<?> keyWordType) {
		keyWordDriver.addKeyWordDriver(key, keyWord, keyWordType);
		KeyWordPanel.addKeyWord(key, keyWordType);
	}

	private void setSpecifyVisible() {
		if (specifySwitch.getSelectedIndex() == 0) {
			specifySheetLabel.setVisible(false);
			specifySheet.setVisible(false);
			specifyRowLabel.setVisible(false);
			specifyRow.setVisible(false);
			specifyStepLabel.setVisible(false);
			specifyStep.setVisible(false);
		} else {
			specifySheetLabel.setVisible(true);
			specifySheet.setVisible(true);
			specifyRowLabel.setVisible(true);
			specifyRow.setVisible(true);
			specifyStepLabel.setVisible(true);
			specifyStep.setVisible(true);
		}
	}

	// private void setCoreVisible(){
	// if (setCoreSwitch.getSelectedIndex() == 0){
	// setCoreSetting.setVisible(true);
	//
	// }else{
	// setCoreSetting.setVisible(false);
	//
	// }
	// setReStartSelect();
	// }

	private void setinitDBContent() {
		if (initDBContent.getSelectedIndex() == 1) {
			initDBPathLabel.setVisible(false);
			initDBPathContent.setVisible(false);
			initDBPathButton.setVisible(false);

		} else {
			initDBPathLabel.setVisible(true);
			initDBPathContent.setVisible(true);
			initDBPathButton.setVisible(true);
		}
		setReStartSelect();
	}

	private void setReStartSelect() {
		if (initDBContent.getSelectedIndex() == 0) {
			// || setCoreSwitch.getSelectedIndex() == 0){
			reStart.setEnabled(false);
			reStart.setSelected(true);
		} else {
			reStart.setEnabled(true);
			reStart.setSelected(false);
		}
	}

	private void setNoServerVisible() {

		// if(notServer.isSelected()){
		// // MiddlewareLabel.setVisible(false);
		// // MiddlewareContent.setVisible(false);
		// // MiddlewareButton.setVisible(false);
		// // YigoLabel.setVisible(false);
		// // YigoContent.setVisible(false);
		// // YigoButton.setVisible(false);
		// // configPathLabel.setVisible(true);
		// // configPathContent.setVisible(true);
		// // configPathButton.setVisible(true);
		//
		//
		// initDBLabel.setVisible(false);
		// initDBContent.setVisible(false);
		// initDBPathLabel.setVisible(false);
		// initDBPathContent.setVisible(false);
		// initDBPathButton.setVisible(false);
		//
		// // setCoreLable.setVisible(false);
		// // setCoreSetting.setVisible(false);
		// // setCoreSwitch.setVisible(false);
		// reStart.setVisible(false);
		//
		// }else{
		// // MiddlewareLabel.setVisible(true);
		// // MiddlewareContent.setVisible(true);
		// // MiddlewareButton.setVisible(true);
		// // YigoLabel.setVisible(true);
		// // YigoContent.setVisible(true);
		// // YigoButton.setVisible(true);
		// // configPathLabel.setVisible(false);
		// // configPathContent.setVisible(false);
		// // configPathButton.setVisible(false);
		//
		//
		// initDBLabel.setVisible(true);
		// initDBContent.setVisible(true);
		//
		// // setCoreLable.setVisible(true);
		// // setCoreSwitch.setVisible(true);
		// reStart.setVisible(true);
		// // setCoreVisible();
		//
		// setinitDBContent();
		//
		// }

	}

	/**
	 * 添加控件 并且设置大小
	 * 
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void setBoundsAtPanel(JComponent comp, int x, int y, int width, int height) {
		panel.add(comp);
		comp.setBounds(x, y, width, height);
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

	private void setTestTypeVisible(Boolean isVisible) {
		appFilePathButton.setVisible(isVisible);
		appFilePathContent.setVisible(isVisible);
		appFilePathLabel.setVisible(isVisible);
		appInfoLabel.setVisible(isVisible);

		URLLabel.setVisible(!isVisible);
		URLContent.setVisible(!isVisible);
		browserTypeLabel.setVisible(!isVisible);
		browserTypeContent.setVisible(!isVisible);
	}

	/**
	 * 批量设置控制参数控件是否可见
	 * 
	 * @param isVisible
	 *            参数控件是否可见
	 */
	private void setControlsVisible(Boolean isVisible) {
		filePathLabel.setVisible(!isVisible);
		filePathContent.setVisible(!isVisible);
		filePathButton.setVisible(!isVisible);
		browserTypeLabel.setVisible(isVisible);
		browserTypeContent.setVisible(isVisible);
		URLContent.setVisible(isVisible);
		URLLabel.setVisible(isVisible);
		// MiddlewareLabel.setVisible(isVisible);
		// MiddlewareContent.setVisible(isVisible);
		// MiddlewareButton.setVisible(isVisible);
		// YigoLabel.setVisible(isVisible);
		// YigoContent.setVisible(isVisible);
		// YigoButton.setVisible(isVisible);
		// versionLabel.setVisible(isVisible);
		// versionContent.setVisible(isVisible);
		testCaseLabel.setVisible(isVisible);
		testCaseContent.setVisible(isVisible);
		testCaseButton.setVisible(isVisible);
		executeModeLabel1.setVisible(isVisible);
		executeModeContent.setVisible(isVisible);
		sheetNumLabel1.setVisible(isVisible);
		sheetNumContent.setVisible(isVisible);
		sheetNumLabel2.setVisible(isVisible);
		executeFrontStepsLabel.setVisible(isVisible);
		executeFrontStepsContent.setVisible(isVisible);
		initDBLabel.setVisible(isVisible);
		initDBContent.setVisible(isVisible);
		initDBPathLabel.setVisible(isVisible);
		initDBPathContent.setVisible(isVisible);
		initDBPathButton.setVisible(isVisible);
		// debugCheckBox.setVisible(isVisible);
		// notServer.setVisible(isVisible);
		// configPathLabel.setVisible(false);
		// configPathContent.setVisible(false);
		// configPathButton.setVisible(false);
		// notServer.setSelected(false);

		specifyCood.setVisible(isVisible);
		specifySwitch.setVisible(isVisible);

		upload.setVisible(isVisible);
		uploadSetting.setVisible(isVisible);

		// setCoreSwitch.setVisible(isVisible);
		// setCoreLable.setVisible(isVisible);
		// setCoreSetting.setVisible(isVisible);
		reStart.setVisible(isVisible);
		if (isVisible && specifySwitch.getSelectedIndex() == 1) {
			specifySheetLabel.setVisible(isVisible);
			specifySheet.setVisible(isVisible);
			specifyRowLabel.setVisible(isVisible);
			specifyRow.setVisible(isVisible);
			specifyStepLabel.setVisible(isVisible);
			specifyStep.setVisible(isVisible);
		} else {
			specifySheetLabel.setVisible(false);
			specifySheet.setVisible(false);
			specifyRowLabel.setVisible(false);
			specifyRow.setVisible(false);
			specifyStepLabel.setVisible(false);
			specifyStep.setVisible(false);
		}

		if (isVisible && initDBContent.getSelectedIndex() == 0) {
			initDBPathLabel.setVisible(isVisible);
			initDBPathContent.setVisible(isVisible);
			initDBPathButton.setVisible(isVisible);
		} else {
			initDBPathLabel.setVisible(false);
			initDBPathContent.setVisible(false);
			initDBPathButton.setVisible(false);
		}

		if (isVisible && executeModeContent.getSelectedIndex() == 1) {
			sheetNumLabel1.setVisible(isVisible);
			sheetNumContent.setVisible(isVisible);
			sheetNumLabel2.setVisible(isVisible);
			executeFrontStepsLabel.setVisible(isVisible);
			executeFrontStepsContent.setVisible(isVisible);
		} else {
			sheetNumLabel1.setVisible(false);
			sheetNumContent.setVisible(false);
			sheetNumLabel2.setVisible(false);
			executeFrontStepsLabel.setVisible(false);
			executeFrontStepsContent.setVisible(false);
		}

		// if (setCoreSwitch.getSelectedIndex() == 0){
		// setCoreSetting.setVisible(isVisible);
		// }
		// else{
		// setCoreSetting.setVisible(false);
		// }

	}

	/**
	 * 初始化页面上参数的值
	 * 
	 * @throws DocumentException
	 */
	private void loadParameters() throws DocumentException {
		XMLReader xml = new XMLReader(templatePath, true);
		HashMap<String, String> hm = xml.getNodeValueByTemplateIndex(0);
		if (!hm.get("Computer").isEmpty()) {
			computerContent.setSelectedItem(hm.get("Computer"));
		}
		ipContent.setText(hm.get("IP"));
		if (!hm.get("BrowserType").isEmpty()) {
			browserTypeContent.setSelectedItem(hm.get("BrowserType"));
		}

		if (hm.containsKey("RunTestModel") && !hm.get("RunTestModel").isEmpty()) {
			runTestModelContent.setSelectedItem(hm.get("RunTestModel"));
		}

		if (hm.containsKey("AppFilePath") && !hm.get("AppFilePath").isEmpty()) {
			appFilePathContent.addItem(hm.get("AppFilePath"));
		}

		URLContent.setText(hm.get("URL"));
		// MiddlewareContent.setText(hm.get("Middleware"));
		// YigoContent.setText(hm.get("Yigo"));
		// if (!hm.get("Version").isEmpty()) {
		// versionContent.setSelectedItem(hm.get("Version"));
		// }
		String excelPath = hm.get("ExcelPath");

		if (!excelPath.isEmpty()) {
			if (Common.isAbsolutePath(excelPath)) {
				testCaseContent.addItem(excelPath);
			}
			testCaseContent.setSelectedItem(excelPath);
		}

		executeModeContent.setSelectedItem(hm.get("ExecuteMode"));
		sheetNumContent.setText(hm.get("SheetsNum"));
		if (!hm.get("FrontSteps").isEmpty()) {
			executeFrontStepsContent.setSelectedItem(hm.get("FrontSteps"));
		}
		if (hm.containsKey("InitDB") && !hm.get("InitDB").isEmpty()) {
			initDBContent.setSelectedItem(hm.get("InitDB"));
		}
		initDBPathContent.setText(hm.get("SqlData"));
		filePathContent.setSelectedItem(hm.get("FilePath"));

		if (hm.containsKey("Specify") && !hm.get("Specify").isEmpty()) {
			specifySwitch.setSelectedItem(hm.get("Specify"));
		}

		specifySheet.setText(hm.get("SpecifySheet"));
		specifyRow.setText(hm.get("SpecifyRow"));
		specifyStep.setText(hm.get("SpecifyStep"));

		// if(hm.containsKey("ConfigPath") && !hm.get("ConfigPath").isEmpty()){
		// configPathContent.setText(hm.get("ConfigPath"));
		// }

		// if(hm.containsKey("NotServer") && !hm.get("NotServer").isEmpty()){
		// notServer.setSelected(Boolean.valueOf(hm.get("NotServer")));
		// }

		// if(hm.containsKey("Debug") && !hm.get("Debug").isEmpty()){
		// debugCheckBox.setSelected(Boolean.valueOf(hm.get("Debug")));
		// }

		if (hm.containsKey("UploadResults") && !hm.get("UploadResults").isEmpty()) {
			upload.setSelected(Boolean.valueOf(hm.get("UploadResults")));
		}

		if (hm.containsKey("StartPlatform") && !hm.get("StartPlatform").isEmpty()) {
			reStart.setSelected(Boolean.valueOf(hm.get("StartPlatform")));
		}

		// if(hm.containsKey("SetCore") && !hm.get("SetCore").isEmpty()){
		// setCoreSwitch.setSelectedItem(hm.get("SetCore"));
		// }
	}

	/**
	 * 保存页面上参数的值
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void saveParameters() {
		XMLReader xml = new XMLReader(templatePath, true);
		try {
			xml.setNodeValueByTemplateIndex(0, "RunTestModel", runTestModelContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "BrowserType", browserTypeContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "AppFilePath", appFilePathContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "URL", URLContent.getText());
			// xml.setNodeValueByTemplateIndex(0, "Middleware",
			// MiddlewareContent.getText());
			// xml.setNodeValueByTemplateIndex(0, "Yigo",
			// YigoContent.getText());
			// xml.setNodeValueByTemplateIndex(0, "Version",
			// versionContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "ExcelPath", testCaseContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "ExecuteMode", executeModeContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "SheetsNum", sheetNumContent.getText());
			xml.setNodeValueByTemplateIndex(0, "FrontSteps", executeFrontStepsContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "InitDB", initDBContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "SqlData", initDBPathContent.getText());
			// xml.setNodeValueByTemplateIndex(0, "FilePath",
			// filePathContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "Computer", computerContent.getSelectedItem().toString());
			xml.setNodeValueByTemplateIndex(0, "IP", ipContent.getText());

			xml.setNodeValueByTemplateIndex(0, "Specify", String.valueOf(specifySwitch.getSelectedItem()));
			xml.setNodeValueByTemplateIndex(0, "SpecifySheet", String.valueOf(specifySheet.getText()));
			xml.setNodeValueByTemplateIndex(0, "SpecifyRow", String.valueOf(specifyRow.getText()));
			xml.setNodeValueByTemplateIndex(0, "SpecifyStep", String.valueOf(specifyStep.getText()));
			// xml.setNodeValueByTemplateIndex(0, "Debug",
			// String.valueOf(debugCheckBox.isSelected()));
			//
			// xml.setNodeValueByTemplateIndex(0, "NotServer",
			// String.valueOf(notServer.isSelected()));
			xml.setNodeValueByTemplateIndex(0, "UploadResults", String.valueOf(upload.isSelected()));

			// xml.setNodeValueByTemplateIndex(0, "ConfigPath",
			// String.valueOf(configPathContent.getText()));

			xml.setNodeValueByTemplateIndex(0, "StartPlatform", String.valueOf(reStart.isSelected()));
			// xml.setNodeValueByTemplateIndex(0, "SetCore",
			// String.valueOf(setCoreSwitch.getSelectedItem()));

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 检查界面上的必填参数是否为空
	 * 
	 * @param 取值界面上的控件
	 * @param 定义界面上控件的报错标签
	 * @throws ParameterException
	 */
	private void checkEmpty(String content, String note) throws ParameterException {
		if (content.equals("")) {
			throw new ParameterException(ConsoleFrame.this, ParameterException.PARAMETER_NOT_ASSIGNED, "必填项" + note + "未定义");
		}
	}

	/**
	 * 检查传入的路径是否存在
	 * 
	 * @param 路径字符串
	 * @param 路径字符串在界面上对应控件的识别标签
	 * @throws ParameterException
	 */
	private void checkExist(String path, String note) throws ParameterException {
		File file = new File(path);
		if (!file.exists()) {
			throw new ParameterException(ConsoleFrame.this, ParameterException.PARAMETER_NOT_ASSIGNED, note + "对应的路径不存在");
		}
	}

	private void checkIP(String IP) throws ParameterException {
		// if (IP.split(":").length != 2) {
		// throw new ParameterException(ConsoleFrame.this,
		// ParameterException.INPUT_ERROR,
		// "IP及服务端口号输入格式为IP:PORT,例:1.1.1.10:5555,请检查.");
		// }
	}

	private void checkURL(String URL) throws ParameterException {
		if (URL.contains("localhost")) {
			throw new ParameterException(ConsoleFrame.this, ParameterException.INPUT_ERROR, "由于您指定其它电脑运行,浏览器地址请以具体IP替代localhost.");
		}
	}

	private void checkKeyWordDriver() throws ParameterException {
		if (keyWordDriver == null) {
			throw new ParameterException(ConsoleFrame.this, ParameterException.INPUT_ERROR, "必须指定keyWordDriver!,启动代码有误");
		}
	}

	private void checkUpload() throws ParameterException {
		// boolean isConnect =
		// DataBaseVerification.isConnect(Variable.resolve("[UploadServer_driver]"),
		// Variable.resolve("[UploadServer_url]"),
		// Variable.resolve("[UploadServer_user]"),
		// Variable.resolve("[UploadServer_passwd]"));
		// if(!isConnect){
		// throw new ParameterException(ConsoleFrame.this,
		// ParameterException.INPUT_ERROR,
		// "上传服务器的连接信息错误,请核实");
		// }
	}

	private String getTemplatePath() throws SeleniumFindException {
		String templatePath = null;
		File file = new File(Parameter.DEFAULT_CONF_PATH + "/Template.xml");
		// 判断系统目录下是否存在模板文件
		if (!file.exists()) {
			throw new SeleniumFindException("缺少界面加载配置文件");
			// 不存在就将jar包里的ParameterValues.xml复制到指定路径下
			// File f = new File(Parameter.DEFAULT_TEMP_PATH);
			// f.mkdirs();
			// URL parameterPath =
			// this.getClass().getResource("ParameterValues.xml");
			// try {
			// Document document = new
			// SAXReader().read(parameterPath.toString());
			// try {
			// FileOutputStream out = new FileOutputStream(file);
			// OutputFormat format = OutputFormat.createPrettyPrint();
			// format.setEncoding("utf-8");
			// XMLWriter output = new XMLWriter(out, format);
			// try {
			// output.write(document);
			// output.close();
			// out.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (UnsupportedEncodingException e1) {
			// e1.printStackTrace();
			// }
			// } catch (DocumentException e) {
			// e.printStackTrace();
			// }
		}

		templatePath = file.toString();
		return templatePath;
	}

	// private String getCoreConfigPath(){
	// return "";
	// File file = new File(Parameter.DEFAULT_TEMP_PATH + "\\core.properties");
	// // 判断系统目录下是否存在coreConfig文件
	// if (!file.exists()) {
	// // 不存在就将jar包里的core.txt复制到指定路径下
	// File f = new File(Parameter.DEFAULT_TEMP_PATH);
	// f.mkdirs();
	// URL parameterPath = this.getClass().getResource("core.properties");
	// FileInputStream inStream = null;
	// FileOutputStream outStream = null;
	// try {
	// // 读入原文件
	// String inPath = parameterPath.toString();
	// int start =inPath.indexOf("file:");
	// if(start!=-1){
	// inPath = inPath.substring(start+6);
	// }
	// inStream = new FileInputStream(inPath);
	// outStream = new FileOutputStream(file);
	// byte[] buffer = new byte[4096 * 4];
	// int n = 0;
	// while (-1 != (n = inStream.read(buffer))) {
	// outStream.write(buffer, 0, n);
	// }
	// outStream.flush();
	// }
	// catch (Exception e) {
	// System.out.println("复制单个文件操作出错");
	// e.printStackTrace();
	// }
	// finally{
	// try {
	// inStream.close();
	// outStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return file.toString();
	// }

	public static void main(String args[]) throws Exception {
		Parameter.init();
		new ConsoleFrame().showUIFrame();
	}
}
