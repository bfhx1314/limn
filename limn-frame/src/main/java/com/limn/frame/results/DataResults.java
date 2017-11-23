package com.limn.frame.results;

import com.limn.frame.testcase.TestCase;
import com.limn.tool.exception.ParameterException;

public interface DataResults {

	
	/**
	 * 初始化xml
	 */
	public void init(final TestCase tc);
	
	/**
	 * 添加Sheet页
	 * @param index 第几页
	 */
	public void addSheet(int index);
	
	
	/**
	 * 添加模块
	 */
	public void addModule(String moduleName);
	
	/**
	 * 添加用例
	 */
	public void addCase(String caseNo);
	
	/**
	 * 添加步骤
	 */
	public void addStep(String stepName, String result);
	
	
	/**
	 * 添加实际结果
	 */
	public void addActualResults(String[] results);
	
	
	/**
	 * 添加预期结果
	 */
	public void addExpectedResults(String[] results);
	
	/**
	 * 添加测试结果
	 */
	public void addResult(boolean isPass);
	
	/**
	 * 添加截图
	 * @param bitMapPath 路径
	 */
	public void addBitMap(String bitMapPath);
	
	/**
	 * 添加相关用例编号
	 * @param CaseNo
	 */
	public void addRelatedCase(String CaseNo);
	
	/**
	 * 自定义添加
	 * @param node
	 * @param value
	 */
	public void addCustom(String node, String value);
	
	public void addTestCaseCount();
	
	
	public void addCaseReport();

	/**
	 * 每一步的日志
	 * @param step
	 * @param result
	 * @throws ParameterException 
	 */
	public void addCaseLog(String step, int result);

	public int getCaseCount();
	
	public int getExecuteCaseCount();
	
	public int getSucessCaseCount();
	
	
}
