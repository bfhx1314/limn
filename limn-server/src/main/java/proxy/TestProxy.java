package proxy;

/**
 * 代理 
 * 接受所有的客户端连接 
 * 接受测试版本的测试
 * @author limn
 *
 */
public class TestProxy {
	
	//测试模块
	private TestModule testModule = new TestModule();
	
	/**
	 * 接受所有的客户端连接(接受测试的客户机)
	 */
	public TestProxy(int port){

		Thread listener = new Thread(new Listener(port));
		listener.start();
		
	}
	/**
	 * 测试版本 信息(需要测试的版本)
	 * @param version
	 */
	public void testVersion(String version){
		testModule.setModule(version);
	}

	
}
