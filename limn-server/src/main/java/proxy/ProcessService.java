package proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;





import com.limn.tool.common.Common;
import com.limn.tool.common.DateFormat;

import status.ClientStatus;
import status.ModuleStatus;
import monitor.Middleware;

/**
 * 连接到客户端后 发送执行的XML文件内容
 * 
 * @author limn
 * 
 */
public class ProcessService implements Runnable {

	private Socket socket = null;
	HashMap<String,String> map = new HashMap<String,String>();
	ProcessService(Socket sc) {
		socket = sc;
	}

	/**
	 * 测试模块
	 */
	public void runModule() {
		// 获取模块信息
		String[] moduleKey = TestModule.getTestModule();

		// 未获取到 不执行
		if (moduleKey[0] != null) {
			
			DataOutputStream dos = null;
			DataInputStream dis = null;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				
				
				Middleware.appServerLog("发送执行版本号" + moduleKey[0]);
				dos.writeUTF(moduleKey[0]);
				
				
				Middleware.appServerLog("发送执行XML");
				dos.writeUTF(TestModule.getModuleXMLForKey(moduleKey[1]));

				// 发送请求 等待客户端 执行结果 判断客户端是否执行成功

				dis = new DataInputStream(socket.getInputStream());
				String status = ModuleStatus.ABNORMAL;
				if (dis.readUTF().equalsIgnoreCase("Success")) {
					// 提示成功
					Middleware.appServerLog("IP: " + socket.getInetAddress().getHostAddress() + "运行成功");
					status = ModuleStatus.RUN;
					keepConnection();
				} else if (dis.readUTF().equalsIgnoreCase("Fail")) {
					// 提示失败
					Middleware.appServerLog("IP: " + socket.getInetAddress().getHostAddress() + "运行失败");
					status = ModuleStatus.FIALE;
				} else{
					Middleware.appServerLog("IP: " + socket.getInetAddress().getHostAddress() + "异常");
				}
				
				setResults(moduleKey,status);

			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
			
		}
	}
	
	/**
	 * 客户端保持连接,传送日志
	 * @throws IOException
	 */
	private void keepConnection() throws IOException{
		DataInputStream dis = null;
		map.put("status", ClientStatus.RUN);
		Middleware.updateTestClient(map);
		while(!socket.isClosed()){
			//写入客户端日志
			dis = new DataInputStream(socket.getInputStream());
			Middleware.appClientLog(socket.getInetAddress().getHostAddress(),dis.readUTF());
		}
	}
	
	
	
	/**
	 *  写入测试结果
	 * @param versionAndKey
	 * @param status
	 */
	private void setResults(String versionAndKey[],String status){
		
		//更新界面上的显示
		TestModule.updateModule(versionAndKey, status);

		FileWriter fw = null;
		try {
			fw = new FileWriter("version/" + versionAndKey[0] + ".txt",true);
			fw.write(DateFormat.getDate() + ": " +versionAndKey[1] + " - IP :" + socket.getInetAddress().getHostAddress()+ " - 结果:" + status);
			fw.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			
			map.put("IP", socket.getInetAddress().getHostAddress());
			map.put("Port", String.valueOf(socket.getPort()));
			map.put("status", ClientStatus.CONNECT);
			Middleware.updateTestClient(map);
			//不停的获取需要测试的内容
			while (!socket.isClosed()) {
				runModule();

				Common.wait(5000);;
			}
			map.put("status", ClientStatus.OFFLINE);
			Middleware.updateTestClient(map);
		}
	}

}
