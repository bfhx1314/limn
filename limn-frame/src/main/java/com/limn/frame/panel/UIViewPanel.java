package com.limn.frame.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.limn.android.tool.DebugBridge;
import com.limn.app.driver.AppDriver;
import com.limn.frame.debug.DebugEditFrame;
import com.limn.tool.common.Common;
import com.limn.tool.common.Print;
import com.limn.tool.exception.ParameterException;
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

	// 屏幕截图存放位置
	private String SCREENSHOTPATH = Parameter.DEFAULT_TEMP_PATH + "/Android_Screenshot.png";

	// 定位元素的属性Table
	private JScrollPane attributeJScroll = new JScrollPane();
	private JTable attributeTable = new JTable();
	private DefaultTableModel attributeModel = new DefaultTableModel();

	// 设置别名
	private static JButton setXPathName = new JButton("设置别名");
	private static JButton setSDK = new JButton("设置SDK");

	private String elementId = null;

	// 页面是否加载
	public boolean isLoad = false;

	public UIViewPanel() {
		System.setProperty("com.android.uiautomator.bindir","/Users/limengnan/Documents/tool/sdk");
		
		
		setLayout(null);
		JButton button = new JButton("加载");
		setBoundsAt(button, 320, 5, 60, 20);
		setBoundsAt(setXPathName, 390, 5, 100, 20);
		setBoundsAt(setSDK, 500, 5, 100, 20);
		
		attributeModel.addColumn("属性");
		attributeModel.addColumn("值");
		attributeModel.addRow(new Object[] { "resource_id", "" });
		attributeModel.addRow(new Object[] { "class", "" });
		attributeModel.addRow(new Object[] { "package", "" });
		attributeModel.addRow(new Object[] { "index", "" });
		attributeModel.addRow(new Object[] { "bounds", "" });
		attributeModel.addRow(new Object[] { "层级", "" });

		attributeTable.setModel(attributeModel);

		attributeTable.setColumnModel(getColumn(attributeTable, new int[] { 90, 150 }));
		attributeTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		attributeJScroll.setViewportView(attributeTable);

		setBoundsAt(attributeJScroll, 320, 30, 300, 390);

		// System.setProperty("com.android.uiautomator.bindir",
		// "/Users/limengnan/Documents/tool/sdk/tools");

		DebugBridge.init();
		// 初始化需要时间
		Common.wait(1000);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadUI();
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
		System.out.println(System.getenv("ANDROID_HOME"));
	}

	/**
	 * 遍历android界面元素
	 * 
	 * @param element
	 * @param reindex
	 */
	private void recursiveElement(Element element, int reindex) {
		try {
			setListElement(element, reindex);

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


	private boolean checkSDKHome() {
		boolean isLoad = false;
		if (System.getProperty("com.android.uiautomator.bindir") == null) {
			if (Parameter.OS.equalsIgnoreCase("Windows")) {
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
	public void loadUI() {

		// 无法加载到SDKhome 直接退出
		if (!checkSDKHome()) {
			return;
		}

		// 清楚界面上的所有控件
		if (imagePanel != null) {
			imagePanel.removeAll();
		}

		imagePanel = new ImagePanel(AppDriver.screenshot(SCREENSHOTPATH));
		imagePanel.setLayout(null);
		// imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

		try {
			// 加载截图，并设置到界面上
			BufferedImage image = ImageIO.read(new File(SCREENSHOTPATH));
			setBoundsAt(imagePanel, 20, 5, image.getWidth() / 5, image.getHeight() / 5);
		} catch (IOException e2) {
			setBoundsAt(imagePanel, 20, 5, 300, 400);
		}

		imagePanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (hightElement != null) {
					Print.debugLog("CLick", 1);
					if (hightElement != null && !hightElement.resource_id.isEmpty()) {

						String[] id = RegExp.splitWord(hightElement.resource_id, ":id/");
						elementId = id[1];
						String step = "M录入:" + elementId + ":";

						if (hightElement._class.equalsIgnoreCase("android.widget.Button")) {
							step = step + "[Click]";
						}
						DebugEditFrame.setStepTextArea(step);
					}
				}
			}
		});

		imagePanel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				highlightElement(e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});

		repaint();

		// 加载界面元素的xml
		String xml = AppDriver.driver.getPageSource();

		Document document = null;
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

	private JTextField setFieldBoundsAtImage(int x, int y, int width, int height) {
		JTextField jf = new JTextField();
		jf.setOpaque(false);
		jf.setBorder(BorderFactory.createLineBorder(Color.blue));
		jf.setBounds(x, y, width, height);
		jf.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				String step = DebugEditFrame.getStepTextArea();
				String[] steps = RegExp.splitKeyWord(step);
				String newStep = steps[0] + ":" + steps[1] + ":" + ((JTextField) e.getSource()).getText();
				DebugEditFrame.setStepTextArea(newStep);
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (hightElement != null) {
					Print.log("CLick", 1);
					if (hightElement != null && !hightElement.resource_id.isEmpty()) {

						String[] id = RegExp.splitWord(hightElement.resource_id, ":id/");
						elementId = id[1];
						String step = "M录入:" + elementId + ":";
						DebugEditFrame.setStepTextArea(step);
					}
				}

			}
		});

		return jf;
	}

	/**
	 * 设置元素
	 * 
	 * @param e
	 * @return
	 * @throws ParameterException
	 */
	private void setListElement(Element e, int reindex) throws ParameterException {
		ElementSet es = new ElementSet();
		es.resource_id = e.attributeValue("resource-id");
		es._class = e.attributeValue("class");
		es._package = e.attributeValue("package");
		es.index = e.attributeValue("index");
		String bounds = e.attributeValue("bounds");
		es.bounds = bounds;

		if (bounds == null) {
			return;
		}
		ArrayList<String> Coordinate = RegExp.matcherCharacters(bounds, "[0-9]{1,}");
		if (Coordinate.size() < 4) {
			throw new ParameterException("bounds元素错误：" + bounds);
		}
		es.x_start = Integer.valueOf(Coordinate.get(0)) / 5;
		es.y_start = Integer.valueOf(Coordinate.get(1)) / 5;
		es.x_end = Integer.valueOf(Coordinate.get(2)) / 5;
		es.y_end = Integer.valueOf(Coordinate.get(3)) / 5;

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
				}
			}
		}
		if (tempES != null) {

			Print.debugLog("find it " + tempES.resource_id, 1);
			if (elementHighIndex != tempES.element_index) {

				for (int i = 0; i < imagePanel.getComponentCount(); i++) {
					imagePanel.remove(i);
				}

				Print.debugLog("start:" + tempES.x_start + " end:" + tempES.y_start, 5);
				// if
				// (tempES._class.equalsIgnoreCase("android.widget.EditText")) {
				// JTextField jl = setFieldBoundsAtImage(tempES.x_start,
				// tempES.y_start, tempES.x_end - tempES.x_start,
				// tempES.y_end - tempES.y_start);
				// imagePanel.add(jl);
				// } else{
				// JLabel jl = setBoundsAtImage(tempES.x_start, tempES.y_start,
				// tempES.x_end - tempES.x_start,
				// tempES.y_end - tempES.y_start, tempES.element_index);
				// imagePanel.add(jl);
				// }
				JLabel jl = setBoundsAtImage(tempES.x_start, tempES.y_start, tempES.x_end - tempES.x_start, tempES.y_end - tempES.y_start, tempES.element_index);
				imagePanel.add(jl);
				hightElement = tempES;
				imagePanel.repaint();

				attributeModel.setValueAt(tempES.resource_id, 0, 1);
				attributeModel.setValueAt(tempES._class, 1, 1);
				attributeModel.setValueAt(tempES._package, 2, 1);
				attributeModel.setValueAt(tempES.index, 3, 1);
				attributeModel.setValueAt(tempES.bounds, 4, 1);
				attributeModel.setValueAt(tempES.z_order, 5, 1);

				attributeTable.setModel(attributeModel);

				elementHighIndex = tempES.element_index;
			} else {
				Print.debugLog("ready", 1);
			}

		} else {
			Print.debugLog("not find it", 1);
		}
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
				if (hightElement != null && !hightElement.resource_id.isEmpty()) {

					String[] id = RegExp.splitWord(hightElement.resource_id, ":id/");
					elementId = id[1];
					String step = "M录入:" + elementId + ":";

					if (hightElement._class.equalsIgnoreCase("android.widget.Button")) {
						step = step + "[Click]";
					}
					DebugEditFrame.setStepTextArea(step);
				}
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

}

/**
 * 元素集合
 * 
 * @author limengnan
 * 
 */
class ElementSet {

	public int x_start = 0;
	public int y_start = 0;
	public int x_end = 0;
	public int y_end = 0;
	public int z_order = -1;
	public String resource_id = "";
	public String index = "";
	public String _class = "";
	public String _package = "";
	public String xpath = "";
	public int element_index = -1;

	public String bounds = "";

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
			image1 = image.getScaledInstance(image.getWidth() / 5, image.getHeight() / 5, Image.SCALE_SMOOTH);
		} catch (IOException e) {

			e.printStackTrace();
		}
		g.drawImage(image1, 0, 0, null);

	}

}
