package com.limn.frame.keyword;

public interface KeyWordDriver {
	
	/**
	 * 关键字运行
	 * @param step 关键字 [0]关键字   [1] ++ 内容
	 * @return 
	 */
	public int start(String[] step);
	
	/**
	 * 判断是否是关键字
	 * @param key
	 * @return
	 */
	public boolean isKeyWord(String key);

}
