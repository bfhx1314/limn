package monitor;

import information.Console;

import java.util.Timer;
import java.util.TimerTask;

import proxy.TestProxy;
/**
 * 服务端的处理
 * 获取最新的补丁
 * 分发需要执行的xml
 * @author limn
 *
 */

public class Middleware extends Console{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Timer timer = new Timer(true);

	public TestProxy proxy = null;
	//补丁的版本号
	public String VERSION = null; 
	
	//补丁的存放位置
	public String updatePath = "//1.1.2.32/Tomcat5.0/webapps/update/Pure1.6";
	//端口号
	public int port = 25041;
	
	Middleware() {
		super();
		
		Console.updatePathInfor.setText("监控补丁地址:" + updatePath);
		proxy = new TestProxy(port);
		timer.schedule(new TimerTest(), 0, 5000);
	}
	
	public static void setServerInfo(String infor){
		Console.serverInfor.setText(infor);
	}
	
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void setUpdatePath(String updatePath){
		this.updatePath = updatePath;
	}
	
	public static void main(String[] args) {
		new Middleware();
	}

	/**
	 * 获取到最新的补丁，且未测试。  执行分发xml
	 * @author limn
	 *
	 */
	private class TimerTest extends TimerTask {

		
		TimerTest(){
			appServerLog("系统启动");
		}
		
		@Override
		public void run() {
//			CheckPatch cp = new CheckPatch(updatePath);
//			cp.getLastVersionFile();
//			VERSION = cp.getLastVersion();
//
//			if (!new File("version/" + VERSION + ".txt").exists()) {
//				appServerLog("最新版本:" + VERSION + " 启动测试");
//
//				try {
//					new File("version/" + VERSION + ".txt").createNewFile();
//				} catch (IOException e) {
//
//					e.printStackTrace();
//				}
//				// Run Test 启动代理分发
//				proxy.testVersion(VERSION);
////			}else{
////				setLog("最新版本:" + VERSION + " 已存在测试记录");
//			}

		}

	}
}
