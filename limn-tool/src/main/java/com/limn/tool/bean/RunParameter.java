package com.limn.tool.bean;

import com.limn.tool.common.BaseToolParameter;
import com.limn.tool.common.Print;

public class RunParameter {

	private static ThreadLocal<StartConfigBean> startConfigBean = new ThreadLocal<>();
	
	private static ThreadLocal<ResultConfigBean> resultConfigBean = new ThreadLocal<>();
	
	private static ThreadLocal<DataBaseConfigBean> dataBaseConfigBean = new ThreadLocal<>();

	public static void setStartPaht(StartConfigBean scb) {
		startConfigBean.set(scb);
	}

	public static StartConfigBean getStartPaht() {
		return startConfigBean.get();
	}
	
	public static void setResultPaht(ResultConfigBean rcb){
		resultConfigBean.set(rcb);
	}
	
	public static ResultConfigBean getResultPaht(){
		if(resultConfigBean.get() == null){

			BaseToolParameter.getPrintThreadLocal().log("resultConfigBean为null,调试模式请忽视改警告", 3);
			resultConfigBean.set(new ResultConfigBean()); 
		}
		return resultConfigBean.get();
	}
	
	public static void setDataBasePaht(){
		dataBaseConfigBean.set(new DataBaseConfigBean());
	}
	
	public static DataBaseConfigBean getDataBasePaht(){
		if(dataBaseConfigBean.get() == null){
			setDataBasePaht();
		}
		return dataBaseConfigBean.get();
	}

}
