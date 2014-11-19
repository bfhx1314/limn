package com.limn.tool.def;

/**
 * DataType定义了YES平台支持的数据类型,所有从数据库载入的数据及生成的数据均需转成相应的兼容类型,
 * 不可以直接使用JDBC中定义的数据类型,这样做的目的是为了统一对数据类型的处理,将上级代码同底层数据库隔离
 * @author 王元和
 *
 */
public class DataType {

	/** 32位整型 */
	public static final int INT = 1;

	/** 字符串类型 */
	public static final int STRING = 2;

	/** 日期类型 */
	public static final int DATETIME = 3;

	/** 数值类型 */
	public static final int NUMERIC = 4;

	/** 二进制类型 */
	public static final int BINARY = 5;

	/** 布尔类型 */
	public static final int BOOLEAN = 6;
	
	/** 64位整型 */
	public static final int LONG = 7;

	/**
	 * 从字符串解析数据表示的类型
	 * @param type 字符串
	 * @return 如果是有效的字符串,返回相应的数据类型,否则返回-1
	 */
	public static int parseDataType(String type) {
		if (type.equalsIgnoreCase("Integer")) {
			return INT;
		} else if (type.equalsIgnoreCase("Varchar")) {
			return STRING;
		} else if (type.equalsIgnoreCase("Numeric")) {
			return NUMERIC;
		} else if (type.equalsIgnoreCase("DateTime")) {
			return DATETIME;
		} else if (type.equalsIgnoreCase("Binary")) {
			return BINARY;
		} else if (type.equalsIgnoreCase("Boolean")) {
			return BOOLEAN;
		} else if (type.equalsIgnoreCase("Long")) {
			return LONG;
		}
		return -1;
	}

	/**
	 * 根据数据类型,转换成字符串
	 * @param type 数据类型
	 * @return 如果是有效的类型,返回相应的字符串表示,否则返回null
	 */
	public static String toString(int type) {
		switch (type) {
		case INT:
			return "Integer";
		case STRING:
			return "Varchar";
		case NUMERIC:
			return "Numeric";
		case DATETIME:
			return "DateTime";
		case BINARY:
			return "Binary";
		case BOOLEAN:
			return "Boolean";
		case LONG:
			return "Long";
		}
		return null;
	}
}
