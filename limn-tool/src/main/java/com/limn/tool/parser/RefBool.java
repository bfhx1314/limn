package com.limn.tool.parser;

/**
 * RefBool为Boolean类型引用类,主要用于模拟引用参数,
 * 语法实现中使用,不在外部使用,不在API文档中体现
 * @author 王元和
 * @since YES1.0
 */
public class RefBool {
	private boolean value = false;
	public RefBool(boolean value) {
		this.value = value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
}
