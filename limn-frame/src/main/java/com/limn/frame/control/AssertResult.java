package com.limn.frame.control;

public class AssertResult {
	
	/**
	 * 结果输入
	 * @param isPass
	 */
	public static void assertResult(boolean isPass){
		if(isPass){
			Test.setAcutalResult(true);
		}else{
			Test.setAcutalResult(false);
		}
	}
	
	/**
	 * 对比结果
	 * @param expect 预期结果
	 * @param actual 实际结果
	 */
	public static void assertResult(String expect,String actual){
		assertResult(expect.equalsIgnoreCase(actual));
	}
	
	

}
