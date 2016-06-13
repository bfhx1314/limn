package com.limn.frame.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.limn.android.tool.DebugBridge;
import com.limn.app.driver.AppDriver;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.common.Common;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLXPath;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class UIViewPanel extends CustomPanel {

	private static final long serialVersionUID = 1L;

	// 图片Panel
	private ImagePanel imagePanel = null;

	// 元素集合
	private ArrayList<ElementSet> elementSet = new ArrayList<ElementSet>();

	private ElementSet hightElement = null;

	// 总计元素个数
	private int elementCount = 0;

	// 高亮的元素标示
	private int elementHighIndex = -1;

	// 定位元素的属性Table
	private JScrollPane attributeJScroll = new JScrollPane();
	private JTable attributeTable = new JTable();
	private DefaultTableModel attributeModel = new DefaultTableModel();

	// 设置别名
	private static JButton setXPathName = new JButton("设置别名");
	private static JButton setSDK = new JButton("设置SDK");

//	private String elementId = null;

	// 页面是否加载
	public boolean isLoad = false;
	
	// 界面截图显示大小
	private BigDecimal scaling = null;
	private int imageWidth = 0;
	private int imageHeight = 0;
	
	//历史文件记录
	// 屏幕截图存放位置
	private String SCREENSHOTPATH = Parameter.DEFAULT_TEMP_PATH + "/AppScreenshot.png";
	private String androidXML = Parameter.DEFAULT_TEMP_PATH + "/AppUI.xml";
	
	
	private Document document = null;
	private XPath xpath = null;
	public UIViewPanel() {
		
		loadUI(false);
		setLayout(null);
		JButton button = new JButton("加载");
		setBoundsAt(button, 320, 5, 60, 20);
		setBoundsAt(setXPathName, 390, 5, 100, 20);
		setBoundsAt(setSDK, 500, 5, 100, 20);
		
		attributeModel.addColumn("属性");
		attributeModel.addColumn("值");
		attributeModel.addRow(new Object[] { "index", "" });
		attributeModel.addRow(new Object[] { "层级", "" });
		attributeModel.addRow(new Object[] { "xpath", "" });
		attributeModel.addRow(new Object[] { "", "" });
		attributeModel.addRow(new Object[] { "", "" });
		attributeModel.addRow(new Object[] { "", "" });
		attributeModel.addRow(new Object[] { "", "" });
		attributeModel.addRow(new Object[] { "", "" });

		attributeTable.setModel(attributeModel);

		attributeTable.setColumnModel(getColumn(attributeTable, new int[] { 60, 180 }));
		attributeTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		attributeJScroll.setViewportView(attributeTable);

		setBoundsAt(attributeJScroll, 320, 30, 300, 390);

		XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
		
		DebugBridge.init();
		// 初始化需要时间
		Common.wait(1000);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadUI(true);
			}
		});

		// 设置XPATH别名按钮
		// JButton setXPathName = new JButton("设置XPATH别名");
		setXPathName.setMargin(new Insets(0, 0, 0, 0));

		setXPathName.setEnabled(true);
		setXPathName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// xpath别名
				String name = null;
				// 是否完成
				boolean flag = true;
				do {
					name = JOptionPane.showInputDialog(UIViewPanel.this, "设置别名", "");
					if (name != null && !name.equals("")) {
						HashMap<String, String> hm = DebugEditFrame.getXpathName();
						String[] keys = RegExp.splitKeyWord(DebugEditFrame.getStepTextArea());
						String elementId = null;
						if(keys.length>1){
							elementId = keys[1];
						}else{
							return;
						}
						if (hm.containsKey(name)) {
							int status = JOptionPane.showConfirmDialog(UIViewPanel.this, "存在重复的关联属性:" + name + ",是否覆盖", "警告", JOptionPane.OK_CANCEL_OPTION);
							if (status == 0) {
								
								DebugEditFrame.setXpathName(name, elementId);
								flag = false;
							}
						} else {
							DebugEditFrame.setXpathName(name, elementId);
							flag = false;
						}
					} else {
						return;
					}
				} while (flag);
				String value = "";
				String currentStep = DebugEditFrame.getStepTextArea();
				String keyword = "M录入:";
				if (!currentStep.isEmpty()) {
					String[] step = RegExp.splitKeyWord(currentStep);
					keyword = step[0] + ":";
					if (step.length >= 3) {
						value = step[2];
					}
				}
				// 传入用例输入框
				String step = keyword + name + ":" + value;
				DebugEditFrame.setStepTextArea(step);

			}
		});
		
		
		//设置SDK目录
		setSDK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String defaultSDK = "";
				if(checkSDKHome()){
					defaultSDK = System.getProperty("com.android.uiautomator.bindir");
				}
				String sdk = JOptionPane.showInputDialog(UIViewPanel.this, "设置SDKHOME", defaultSDK);
				
				if(null == sdk || sdk.isEmpty()){
					
				}else{
					if(new File(sdk).exists()){
						System.setProperty("com.android.uiautomator.bindir",sdk);
						Print.log("设置SDK_HOME成功." + sdk,1);
					}else{
						Print.log("设置SDK_HOME失败,目录不存在." + sdk,2);
					}
				}
			}
		});

	}

	public static void main(String[] args) {
		double x = 1650*1.00/300.00*1.00;
		double y = 1890/400;
		System.out.println(x + "   " + y);
		
		
	}

	/**
	 * 遍历android界面元素
	 * 
	 * @param element
	 * @param reindex
	 */
	private void recursiveElement(Element element, int reindex) {
		try {
			if(AppDriver.AppType.equalsIgnoreCase("Android")){
			setListElement(element, reindex);
			}else if(AppDriver.AppType.equalsIgnoreCase("IOS")){
				setListIOSElement(element, reindex);
			}

		} catch (ParameterException e1) {
			e1.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Iterator<Element> it = element.elementIterator();
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 对子节点进行遍历
			reindex++;
			recursiveElement(e, reindex);
		}
	}

	/**
	 * 检查Android的SDK目录
	 * @return
	 */
	private boolean checkSDKHome() {
		//如果是IOS测试 无需检查
		if(AppDriver.AppType.equalsIgnoreCase("IOS")){
			return true;
		}
		boolean isLoad = false;
		if (System.getProperty("com.android.uiautomator.bindir") == null) {
			if (Parameter.getOS().equalsIgnoreCase("Windows")) {
				String sdk = System.getenv("ANDROID_HOME");
				if (sdk != null && !sdk.isEmpty()) {
					Print.log("获取SDK目录:" + sdk, 1);
					System.setProperty("com.android.uiautomator.bindir", sdk);
					isLoad = true;
				} else {
					Print.log("无法获取SDK目录", 2);
				}
			} else {
				Print.log("无法获取SDK目录", 2);
			}
		}else{
			if(new File(System.getProperty("com.android.uiautomator.bindir")).exists()){
				isLoad = true;
			}
		}
		return isLoad;
	}

	/**
	 * 加载APP界面
	 */
	public void loadUI(boolean loadApp) {

		// 无法加载到SDKhome 直接退出
		if (AppDriver.AppType.equalsIgnoreCase("Android") && !checkSDKHome()) {
			return;
		}

		// 清楚界面上的所有控件
		if (imagePanel != null) {
			imagePanel = null;
		}
		
		//截图路径
		if(loadApp){
			AppDriver.screenshot(SCREENSHOTPATH);
		}else if(!new File(SCREENSHOTPATH).exists()){
			return;
		}
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(SCREENSHOTPATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//获取最优的比例
		try{
			getZoomForImage(image.getWidth(),image.getHeight());
		}catch(NullPointerException e){
			if(null == scaling){
				Common.wait(500);
				getZoomForImage(image.getWidth(),image.getHeight());
			}
		}
		imagePanel = new ImagePanel(SCREENSHOTPATH);
		imagePanel.setLayout(null);

		// 加载截图，并设置到界面上
		setBoundsAt(imagePanel, 20, 5, imageWidth, imageHeight);

		imagePanel.addMouseListener(new AppMouseListener());
		
		imagePanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				highlightElement(e.getX(), e.getY());
			}
			@Override
			public void mouseDragged(MouseEvent e) {}
		});

		repaint();

		
		String xml = null;
		if(loadApp){
			// 加载界面元素的xml
			xml = AppDriver.driver.getPageSource();
		}else if(new File(androidXML).exists()){
			//加载本地文件
			try {
				xml = FileUtil.getFileText(androidXML);
			} catch (IOException e1) {
				Print.log("androidxml本地文件读取失败", 3);
				return;
			}
		}else{
			return;
		}
		
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		Element root = document.getRootElement();
		elementSet.clear();
		elementSet = null;
		elementSet = new ArrayList<ElementSet>();
		recursiveElement(root, 0);
		isLoad = true;
		
		//保存界面xml文件至temp目录
		XMLWriter output = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();   
			format.setEncoding("utf-8");  
			output = new XMLWriter(new FileOutputStream(androidXML), format);	
			output.write(document);
		} catch (IOException e1) {
			Print.log("UIXML文件保存失败", 3);
		} finally{
			try {
				output.close();
			} catch (IOException e1) {
				Print.log("UIXML流对象关闭失败", 3);
			}
		}

	}

	
	
	
	/**
	 * 添加控件
	 * 
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void setBoundsAt(Component comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		this.add(comp);
	}

	/**
	 * 增加界面元素
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param reindex
	 * @return
	 */
	private JLabel setBoundsAtImage(int x, int y, int width, int height, int reindex) {
		JLabel jl = new JLabel();
		jl.setLayout(null);
		jl.setOpaque(false);
		jl.setBorder(BorderFactory.createLineBorder(Color.red));
		jl.setBounds(x, y, width, height);
		// jl.addMouseListener(new AppMouseListener());
		return jl;
	}

//	private JTextField setFieldBoundsAtImage(int x, int y, int width, int height) {
//		JTextField jf = new JTextField();
//		jf.setOpaque(false);
//		jf.setBorder(BorderFactory.createLineBorder(Color.blue));
//		jf.setBounds(x, y, width, height);
//		jf.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				String step = DebugEditFrame.getStepTextArea();
//				String[] steps = RegExp.splitKeyWord(step);
//				String newStep = steps[0] + ":" + steps[1] + ":" + ((JTextField) e.getSource()).getText();
//				DebugEditFrame.setStepTextArea(newStep);
//			}
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				if (hightElement != null) {
//					Print.log("CLick", 1);
//					if (hightElement != null && !hightElement.resource_id.isEmpty()) {
//
//						String[] id = RegExp.splitWord(hightElement.resource_id, ":id/");
//						elementId = id[1];
//						
//						//TODO
//						String step = "M录入:" + elementId + ":";
//						DebugEditFrame.setStepTextArea(step);
//					}
//				}
//
//			}
//		});
//
//		return jf;
//	}
	
	
	
	
	/**
	 * 根据节点元素返回xpath路径
	 * @param element 界面元素
	 * @return xpath路径
	 */
	private String getXPath(Element element){
		try {
			xpath.evaluate("", document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			Print.log("xpath数据异常:", 2);			
		}
		return null;
	}
	
	
	

	/**
	 * 设置元素
	 * 
	 * @param e
	 * @return
	 * @throws ParameterException
	 */
	private void setListElement(Element e, int reindex) throws ParameterException {
		AndroidElementSet es = new AndroidElementSet();
		es.element = e;
		es.resource_id = e.attributeValue("resource-id");
		es._class = e.attributeValue("class");
		es._package = e.attributeValue("package");
		if(null == e.attributeValue("index")){
			es.index = -1;
		}else{
			es.index = Integer.valueOf(e.attributeValue("index"));
		}
		String bounds = e.attributeValue("bounds");
		es.bounds = bounds;
		es.text = e.attributeValue("text");
		if (bounds == null) {
			return;
		}
		ArrayList<String> Coordinate = RegExp.matcherCharacters(bounds, "[0-9]{1,}");
		if (Coordinate.size() < 4) {
			throw new ParameterException("bounds元素错误：" + bounds);
		}
		
		es.x_start = new Double(new Double(Coordinate.get(0)) / scaling.doubleValue()).intValue();
		es.y_start = new Double(new Double(Coordinate.get(1)) / scaling.doubleValue()).intValue();
	
		es.x_end = new Double(new Double(Coordinate.get(2)) / scaling.doubleValue()).intValue();
		es.y_end = new Double(new Double(Coordinate.get(3)) / scaling.doubleValue()).intValue();

		es.element_index = ++elementCount;
		es.z_order = reindex;
		elementSet.add(es);
	}
	
	
	private void setListIOSElement(Element e, int reindex) throws ParameterException {
		IOSElementSet es = new IOSElementSet();
		es.element = e;
		es.name = e.attributeValue("name");
		es.label = e.attributeValue("label");
		es.dom = e.attributeValue("dom");
		if(null == e.attributeValue("index")){
			es.index = -1;
		}else{
			es.index = Integer.valueOf(e.attributeValue("index"));
		}
		es.text = e.attributeValue("text");
		
		Double x = e.attributeValue("x") == null?0:Double.valueOf(e.attributeValue("x"))*3;
		Double y = e.attributeValue("y") == null?0:Double.valueOf(e.attributeValue("y"))*3;
		Double width1 = e.attributeValue("width") == null?0:Double.valueOf(e.attributeValue("width"))*3;
		Double height1 = e.attributeValue("height") == null?0:Double.valueOf(e.attributeValue("height"))*3;
		
		
		es.x_start = new Double(new Double(x) / scaling.doubleValue()).intValue();
		es.y_start = new Double(new Double(y) / scaling.doubleValue()).intValue();
		
		es.x_end = new Double(new Double(x + width1) / scaling.doubleValue()).intValue();
		es.y_end = new Double(new Double(y + height1) / scaling.doubleValue()).intValue();

		es.element_index = ++elementCount;
		es.z_order = reindex;
		elementSet.add(es);
	}
	
	
	

	private void highlightElement(int x, int y) {
		ElementSet tempES = null;
		int z_order = -1;
		for (ElementSet es : elementSet) {
			if (es.x_start < x && es.x_end > x && es.y_start < y && es.y_end > y) {
				if (z_order < es.z_order) {
					tempES = es;
					z_order = tempES.z_order;
				}else if(z_order == es.z_order && es.index > tempES.index){
					tempES = es;
				}
			}
		}
		if (tempES != null) {

			if(null == imagePanel){
				return;
			}
			
//			Print.debugLog("find it " + tempES.resource_id, 1);
			if (elementHighIndex != tempES.element_index) {

				for (int i = 0; i < imagePanel.getComponentCount(); i++) {
					imagePanel.remove(i);
				}

				Print.debugLog("start:" + tempES.x_start + " end:" + tempES.y_start, 5);
				
				JLabel jl = setBoundsAtImage(tempES.x_start, tempES.y_start, tempES.x_end - tempES.x_start, tempES.y_end - tempES.y_start, tempES.element_index);
				imagePanel.add(jl);
				hightElement = tempES;
				imagePanel.repaint();
				
				attributeModel.setValueAt(tempES.index, 0, 1);
				attributeModel.setValueAt(tempES.z_order, 1, 1);
				attributeModel.setValueAt(xpath, 2, 1);
				if(AppDriver.AppType.equalsIgnoreCase("Android")){
					attributeModel.setValueAt("resource_id", 3, 0);
					attributeModel.setValueAt(((AndroidElementSet)tempES).resource_id, 3, 1);
					attributeModel.setValueAt("class", 4, 0);
					attributeModel.setValueAt(((AndroidElementSet)tempES)._class, 4, 1);
					attributeModel.setValueAt("package", 5, 0);
					attributeModel.setValueAt(((AndroidElementSet)tempES)._package, 5, 1);
					attributeModel.setValueAt("bounds", 6, 0);
					attributeModel.setValueAt(((AndroidElementSet)tempES).bounds, 6, 1);
					attributeModel.setValueAt("text", 7, 0);
					attributeModel.setValueAt(((AndroidElementSet)tempES).text, 7, 1);
					
				}else if(AppDriver.AppType.equalsIgnoreCase("IOS")){
					attributeModel.setValueAt("dom", 3, 0);
					attributeModel.setValueAt(((IOSElementSet)tempES).dom, 3, 1);
					attributeModel.setValueAt("label", 4, 0);
					attributeModel.setValueAt(((IOSElementSet)tempES).label, 4, 1);
					attributeModel.setValueAt("name", 5, 0);
					attributeModel.setValueAt(((IOSElementSet)tempES).name, 5, 1);
					attributeModel.setValueAt("valid", 6, 0);
					attributeModel.setValueAt(((IOSElementSet)tempES).valid, 6, 1);
					attributeModel.setValueAt("value", 7, 0);
					attributeModel.setValueAt(((IOSElementSet)tempES).value, 7, 1);
				}
				
				LinkedList<String> rule = new LinkedList<String>();
				rule.add("resource-id");
				rule.add("text");
				rule.add("index");
				
				XMLXPath xmlXpath = new XMLXPath(rule);
				String xpath = xmlXpath.getXPath(tempES.element);

				String[] list = RegExp.splitWord(xpath, ":");
				xpath = list[0];
				for(int i = 1 ; i < list.length ; i ++){
					xpath = xpath + "\\:" + list[i];		
				}
				if(AppDriver.AppType.equalsIgnoreCase("Android")){
					boolean searchIt = XMLXPath.search(
							"//" + tempES.element.getName() + "[@resource-id='" + ((AndroidElementSet)tempES).resource_id + "']",
							tempES.element);
					if (searchIt) {
						String[] id = RegExp.splitWord(((AndroidElementSet)tempES).resource_id, ":id/");
						if (id.length > 1) {
							xpath = id[1];
						}
					}	
				}
				hightElement.xpath = xpath;
				
				attributeTable.setModel(attributeModel);

				elementHighIndex = tempES.element_index;
			} else {
				Print.debugLog("ready", 1);
			}

		} else {
			Print.debugLog("not find it", 1);
		}
	}
	
	/**
	 * 通过界面大小所处缩小比例
	 * @param width
	 * @param height
	 */
	private void getZoomForImage(int width,int height){
		BigDecimal x = new BigDecimal(width*1.00/300);
		BigDecimal  y = new BigDecimal(height*1.00/390);
		if(x.compareTo(y)>0){
			scaling = x;
		}else{
			scaling = y;
		}
		imageWidth = new Double(width / scaling.doubleValue()).intValue();
		imageHeight = new Double(height / scaling.doubleValue()).intValue();
				
		Print.debugLog("截图缩放比例:" + scaling.doubleValue(), 0);
	}

	private TableColumnModel getColumn(JTable table, int[] width) {
		TableColumnModel columns = table.getColumnModel();
		for (int i = 0; i < width.length; i++) {
			TableColumn column = columns.getColumn(i);
			column.setPreferredWidth(width[i]);
		}
		return columns;
	}

	/**
	 * 界面元素事件
	 * 
	 * @author limengnan
	 * 
	 */
	class AppMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (hightElement != null) {
				Print.debugLog("CLick", 1);
//				if (hightElement != null && !hightElement.resource_id.isEmpty()) {
//
//					String[] id = RegExp.splitWord(hightElement.resource_id, ":id/");
//					elementId = id[1];
					String step = "M录入:" + hightElement.xpath + ":";

					if (AppDriver.AppType.equalsIgnoreCase("Android") && ((AndroidElementSet)hightElement)._class.equalsIgnoreCase("android.widget.Button")) {
						step = step + "[Click]";
					}
					DebugEditFrame.setStepTextArea(step);
//				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	}
	
	class ImagePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private File imageFile = null;

		public ImagePanel(String imagePath) {
			imageFile = new File(imagePath);
			
		}

		
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image image1 = null;
			
			try {
				BufferedImage image = ImageIO.read(imageFile);
				//获取最优的比例
				getZoomForImage(image.getWidth(),image.getHeight());
				int width = new Double(image.getWidth() / scaling.doubleValue()).intValue();
				int height = new Double(image.getHeight() / scaling.doubleValue()).intValue();
				image1 = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
			} catch (IOException e) {

				e.printStackTrace();
			}
			g.drawImage(image1, 0, 0, null);

		}

	}

}

/**
 * 元素集合
 * 
 * @author limengnan
 * 
 */
class AndroidElementSet extends ElementSet {

	public String resource_id = "";
	public int index = -1;
	public String _class = "";
	public String _package = "";
	public String bounds = "";
	public String text = "";

}
class ElementSet {
	public int x_start = 0;
	public int y_start = 0;
	public int x_end = 0;
	public int y_end = 0;
	public int z_order = -1;
	public int index = -1;
	public int element_index = -1;
	public String xpath = "";
	public Element element = null;
}
class IOSElementSet extends ElementSet {

	public String name = "";
	public String label = "";
	public String value = "";
	public String dom = "";
	public String valid = "";
	public String text = "";
}


