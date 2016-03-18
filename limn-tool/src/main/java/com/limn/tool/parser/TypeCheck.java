package com.limn.tool.parser;

import java.math.BigDecimal;
import java.util.Date;

import com.limn.tool.def.DataType;

/**
 * 兼容性类型检查,内部实现类,不作特别说明
 * @author 王元和
 */
public class TypeCheck {
	/**
	 * 算术运算的类型兼容性检查
	 * @param value1 值1
	 * @param value2 值2
	 * @return 结果类型
	 * @throws ParserException 如果不兼容性,抛出不兼容错误
	 */
	public static final int getCompatibleType(Object value1, Object value2) throws ParserException {
		int dataType = -1;
		if ( value1 instanceof Integer ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Long ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof String ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Date ) {
				dataType = DataType.STRING;
			}
		} else if ( value1 instanceof BigDecimal ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Boolean ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Date ) {
			if ( value2 instanceof Integer ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Long ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Boolean ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		}
		return dataType;
	}
	
	/**
	 * 逻辑运算兼容性检查
	 * @param value1 值1
	 * @param value2 值2
	 * @return 返回结果类型
	 * @throws ParserException 如果不兼容性,抛出不兼容错误
	 */
	public static final int getCompatibleType4Logic(Object value1, Object value2) throws ParserException {
		int dataType = -1;
		if ( value1 instanceof Integer ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Long ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof String ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof Date ) {
				dataType = DataType.STRING;
			}
		} else if ( value1 instanceof BigDecimal ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Boolean ) {
			if ( value2 instanceof Integer ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof Long ) {
				dataType = DataType.INT;
			} else if ( value2 instanceof String ) {
				dataType = DataType.STRING;
			} else if ( value2 instanceof BigDecimal ) {
				dataType = DataType.NUMERIC;
			} else if ( value2 instanceof Boolean ) {
				dataType = DataType.BOOLEAN;
			} else if ( value2 instanceof Date ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			}
		} else if ( value1 instanceof Date ) {
			if ( value2 instanceof Integer ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Long ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof String ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof BigDecimal ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Boolean ) {
				throw new ParserException(ParserException.INCOMPATIBLE_TYPE, "Incompatible type");
			} else if ( value2 instanceof Date ) {
				dataType = DataType.DATETIME;
			}
		}
		return dataType;
	}
}
