package com.limn.frame.debug;

public class DictoryKeyValue {
	
	private Integer key = 0;
	private String value = null;
	
	DictoryKeyValue(Integer key, String value){
		this.key = key;
		this.value = value;
	}

	public String value(){
		return value;
	}
	
	public int key(){
		return key;
	}
}
