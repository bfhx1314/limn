package information;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;




















import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



















import com.limn.tool.common.DateFormat;

import proxy.TestModule;
import renderer.ClientCellRenderer;
import renderer.ModuleCellRenderer;
import renderer.PatchCellRenderer;


public class Console extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//服务端的一个log界面
	public static JTextArea serverLog = new JTextArea();
	//客户端日志
	public static JTextArea clientLog = new JTextArea();
	//连接的客户端
	public static JList<HashMap<String, String>> testClientList = new JList<HashMap<String, String>>();
	//客户端列表
	public static DefaultListModel<HashMap<String, String>> clientList = new DefaultListModel<HashMap<String, String>>();
	//测试版本
	public static JList<String> testPatchList = new JList<String>();
	public static DefaultListModel<String> patchList = new DefaultListModel<String>();
	//测试模块
	public static JList<Object> testModuleList = new JList<Object>();
	public static DefaultListModel<Object> moduleList = new DefaultListModel<Object>();
	
	public static JLabel serverInfor = new JLabel();
	public static JLabel updatePathInfor = new JLabel();
	

	public Console(){
		super("Middleware");
		//服务器log界面样式
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		setBounds((int)((screenWidth-1000)*0.5), (int) ((screenHeight - 530) * 0.5), 1000, 530);
		
		JScrollPane clientListSP = new JScrollPane(testClientList);
		JScrollPane serverLogSP = new JScrollPane(serverLog);
		JScrollPane clientLogSP = new JScrollPane(clientLog);
		JScrollPane testPatchListSP = new JScrollPane(testPatchList);
		JScrollPane testModuleListSP = new JScrollPane(testModuleList);
		
		add(clientListSP);
		add(serverLogSP);
		add(clientLogSP);
		add(testPatchListSP);
		add(testModuleListSP);
		add(serverInfor);
		add(updatePathInfor);
		serverLog.setEditable(false);
		clientLog.setEditable(false);
		

		testPatchList.setCellRenderer(new PatchCellRenderer());
		testModuleList.setCellRenderer(new ModuleCellRenderer());
		testClientList.setCellRenderer(new ClientCellRenderer());
		
		testClientList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			    if(e.getButton() == 3){
			    	JPopupMenu rightMenu = null;
			    	if(testClientList.getSelectedIndex()==-1){
			    		rightMenu = getClinetMenu(true);
			    	}else{
			    		rightMenu = getClinetMenu(false);
			    	}
			    	rightMenu.show(testClientList, e.getX(), e.getY());
			    }
				
			}
		});
		
		testPatchList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent ee) {
				if (testPatchList.getValueIsAdjusting()){
					String selectedValue = testPatchList.getSelectedValue();
					if(selectedValue!=null){
						setTestModule(TestModule.getTestModules().get(selectedValue));
					}
				}
			}
		});
		
		//Document的设置   最大行数200
		serverLog.setDocument(new LogDocument(serverLog, 200));
		
		clientLog.setDocument(new LogDocument(clientLog, 200));
		
		setVisible(true);
		setAlwaysOnTop(true);
		setLayout(null);
		setResizable(false);
	
	
		clientListSP.setBounds(new Rectangle(5,5,150,460));
		
		serverLogSP.setBounds(new Rectangle(160,5,555,225));
		clientLogSP.setBounds(new Rectangle(160,235,555,230));
		
		testPatchListSP.setBounds(new Rectangle(720,5,270,100));
		testModuleListSP.setBounds(new Rectangle(720,110,270,355));
		serverInfor.setText("aaa");
		serverInfor.setBounds(new Rectangle(5,470,200,20));
		updatePathInfor.setBounds(new Rectangle(210,470,500,20));
		
		setDefaultCloseOperation(Console.EXIT_ON_CLOSE);
		
	}
	
	
	/**
	 * 设置测试补丁的列表
	 */
	public static void setTestPath(){
		patchList.removeAllElements();
		for(String version:TestModule.getTestModules().keySet()){
			patchList.addElement(version);
		}
		testPatchList.setModel(patchList);
	}
	
	/**
	 * 只更新当前选中的补丁号的测试模块状态
	 */
	public static void updateTestModule(){
		String selectedValue = testPatchList.getSelectedValue();
		if(selectedValue!=null){
			setTestModule(TestModule.getTestModules().get(selectedValue));
		}else{
			removeTestModule();
		}
	}
	
	
	/**
	 * 设置测试模块的状态
	 * @param module
	 */
	private static void setTestModule(HashMap<String,String> module){
		moduleList.removeAllElements();
		for(String moduleKey:module.keySet()){
			moduleList.addElement(moduleKey + ":" + module.get(moduleKey));
		}
		testModuleList.setModel(moduleList);
	}
	
	/**
	 * 增加客户端的连接
	 * @param client
	 */
	private static void addTestClient(HashMap<String,String> client){
		clientList.addElement(client);
		testClientList.setModel(clientList);
	}
	
	
	private JPopupMenu getClinetMenu(boolean isNew){
		JPopupMenu menu = new JPopupMenu();
		JMenuItem add = new JMenuItem("新增测试");
		JMenuItem delete = new JMenuItem("删除模块");
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		if(isNew){
			menu.add(add);
		}else{
			menu.add(add);
			menu.add(delete);
		}
		return menu;
	}
	
	/**
	 * 更新客户端连接信息
	 * @param client
	 */

	public static void updateTestClient(HashMap<String,String> client){
		boolean isNew = true;
		for(int i=0;i<clientList.getSize();i++){
			if(clientList.get(i).get("IP").equals(client.get("IP")) && 	clientList.get(i).get("Port").equals(client.get("Port"))){
				clientList.set(i, client);
				isNew = false;
			}
		}
		if(isNew){
			addTestClient(client);
		}
	}
	
	/**
	 * 客户端日志  根据选中的客户端 过滤日志
	 * @param ip
	 * @param log
	 */

	public static void appClientLog(String ip,String log){
		if(testClientList.getSelectedValue()!=null && testClientList.getSelectedValue().get("IP").equals(ip)){
			clientLog.append(DateFormat.getDate("yyyy-MM-dd HH:mm:ss") + ": " + log + "\n");
		}
	}
	
	/**
	 * 服务日志
	 * @param str
	 */
	public static void appServerLog(String str){
		serverLog.append(DateFormat.getDate("yyyy-MM-dd HH:mm:ss") + ": " + str + "\n");
	}
	
	//remove
	public static void removeTestPatch(String patch){
		patchList.removeElement(patch);
		testPatchList.setModel(patchList);
	}
	
	public static void removeTestModule(){
		moduleList.removeAllElements();
		testModuleList.setModel(moduleList);
	}
	
	public static void removeClient(String client){
		clientList.removeElement(client);
		testClientList.setModel(clientList);
	}
	
	public static void main(String[] args){
		Console c = new Console();
	}

	

}
