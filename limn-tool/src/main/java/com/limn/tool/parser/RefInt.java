package com.limn.tool.parser;

/**
 * RefInt为Integer类型引用类,主要用于模拟引用参数,
 * 语法实现中使用,不在外部使用,不在API文档中体现
 * @author 王元和
 * @since YES1.0
 */
public class RefInt {
	private int value = -1;
	public RefInt(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void inc() {
		++value;
	}
	
	public void dec() {
		--value;
	}
}
