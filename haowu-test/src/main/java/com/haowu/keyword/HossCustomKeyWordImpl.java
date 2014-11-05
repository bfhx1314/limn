package com.haowu.keyword;

import com.haowu.exception.HaowuException;
import com.haowu.test.apply_workflow.ApplyWorkflow;
import com.haowu.test.apply_workflow.ApplyWorkflowTest;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.tool.common.Print;


/**
 * 自定义关键字
 * @author limn
 *
 */
public class HossCustomKeyWordImpl implements KeyWordDriver {

	@Override
	public int start(String[] step) {
		
		int status = 1;
		
		try {
			switch(step[0]){
			case HossKeyWordType.WORKFLOW_TEST:
				if(step.length<2){
					throw new HaowuException(10020000, "工作流测试关键字:参数个数有误");
				}
				ApplyWorkflowTest awt = new ApplyWorkflowTest(step);
				awt.run();
				break;
			case HossKeyWordType.WORKFLOW:
				if(step.length<2){
					throw new HaowuException(10020000, "工作流关键字:参数个数有误");
				}
				ApplyWorkflow awt1 = new ApplyWorkflow(step);
				awt1.run();
				break;
			default :
				Print.log("不存在此关键字:" + step[0], 2);
			}
			
		} catch (HaowuException e) {
			status = e.getCode();
			Print.log("异常信息: Message:" + e.getMessage(), 2);
		}
		
		return status;
	}

//	@Override
//	public boolean isKeyWord(String key) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
