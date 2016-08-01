package com.limn.tool.bean;

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
