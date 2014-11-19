package com.limn.tool.util;

import java.math.BigDecimal;
import java.util.Date;

public class TypeConvertor {
	public static final String toString(Object value) {
		String stringValue = "";
		if ( value != null ) {
			stringValue = value.toString();
		}
		return stringValue;
	}
	
	public static final Integer toInteger(Object value) {
		Integer integerValue = null;
		if ( value == null ) {
			integerValue = 0;
		} else {
			if ( value instanceof Integer ) {
				integerValue = (Integer)value;
			} else if ( value instanceof Long ) {
				integerValue = ((Long)value).intValue();
			} else if ( value instanceof BigDecimal ) {
				integerValue = ((BigDecimal)value).intValue();
			} else if ( value instanceof String ) {
				integerValue = Integer.parseInt((String)value);
			} else if ( value instanceof Boolean ) {
				integerValue = (Boolean)value ? 1 : 0;
			}
		}
		return integerValue;
	}
	
	public static final Long toLong(Object value) {
		Long longValue = null;
		if ( value == null ) {
			longValue = (long) 0;
		} else {
			if ( value instanceof Integer ) {
				longValue = ((Integer)value).longValue();
			} else if ( value instanceof Long ) {
				longValue = (Long)value;
			} else if ( value instanceof BigDecimal ) {
				longValue = ((BigDecimal)value).longValue();
			} else if ( value instanceof String ) {
				longValue = Long.parseLong((String)value);
			} else if ( value instanceof Boolean ) {
				longValue = (Boolean)value ? (long)1 : (long)0;
			}
		}
		return longValue;
	}
	
	public static final BigDecimal toBigDecimal(Object value) {
		BigDecimal decimalValue = null;
		if ( value == null ) {
			decimalValue = new BigDecimal(0.0);
		} else {
			if ( value instanceof Integer ) {
				decimalValue = new BigDecimal((Integer)value);
			} else if ( value instanceof Long ) {
				decimalValue = new BigDecimal((Long)value);
			} else if ( value instanceof BigDecimal ) {
				decimalValue = (BigDecimal)value;
			} else if ( value instanceof String ) {
				decimalValue = BigDecimal.valueOf(Double.parseDouble((String)value));
			} else if ( value instanceof Boolean ) {
				decimalValue = (Boolean)value ? new BigDecimal(1) : new BigDecimal(0);
			}
		}
		return decimalValue;
	}
	
	public static final Boolean toBoolean(Object value) {
		Boolean booleanValue = false;
		if ( value != null ) {
			if ( value instanceof Integer) {
				booleanValue = !((Integer)value).equals(0);
			} else if ( value instanceof Long ) {
				booleanValue = !((Long)value).equals(0);
			} else if ( value instanceof BigDecimal ) {
				booleanValue = !((BigDecimal)value).equals(0);
			} else if ( value instanceof String ) {
				booleanValue = !((String)value).isEmpty();
			} else if ( value instanceof Boolean ) {
				booleanValue = (Boolean)value;
			}
		}
		return booleanValue;
	}
	
	public static final Date toDate(Object value) {
		Date dateValue = null;
		if ( value != null ) {
			if ( value instanceof Date ) {
				dateValue = (Date)value;
			}
		}
		return dateValue;
	}
}
