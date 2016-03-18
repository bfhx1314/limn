package com.limn.tool.parser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.poi.hssf.record.ObjectProtectRecord;

import com.limn.tool.common.DateFormat;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.exception.SeleniumException;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.util.TypeConvertor;


/**
 * InternalFunctionImplCluster内部函数的实现类
 * 包括以下函数实现
 * <p><pre>
 * In
 * Left
 * Right
 * Mid
 * Length
 * IndexOf
 * ToInt
 * ToDecimal
 * ToString
 * IIF
 * IIFS
 * GetAutoIncrement
 * GetHostName
 * </pre><p>
 * @author 王元和
 * @since YES1.0
 *
 */
public class InternalFunctionImplCluster extends BaseFunctionImplCluster {
	public InternalFunctionImplCluster() {
		super();
	}
	
	class InImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			boolean result = false;
			Object value = arguments[0];
			boolean isString = value instanceof String;
			int length = arguments.length;
			for ( int i = 1; i<length; ++i ) {
				Object tmpValue = arguments[i];
				if ( isString ) {
					if ( ((String)value).equalsIgnoreCase((String)tmpValue) ) {
						result = true;
						break;
					}
				} else {
					if ( value.equals(tmpValue) ) {
						result = true;
						break;
					}
				}
				
			}
			return result;
		}
		
	}
	
	class LeftImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String string = TypeConvertor.toString(arguments[0]);
			Integer length = TypeConvertor.toInteger(arguments[1]);
			String result = string.substring(0, length);
			return result;
		}
		
	}
	
	class RightImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String string = TypeConvertor.toString(arguments[0]);
			Integer length = TypeConvertor.toInteger(arguments[1]);
			String result = string.substring(string.length() - length);
			return result;
		}
		
	}
	
	class MidImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String string = TypeConvertor.toString(arguments[0]);
			Integer pos = TypeConvertor.toInteger(arguments[1]);
			Integer length = TypeConvertor.toInteger(arguments[2]);
			String result = string.substring(pos, pos + length);
			return result;
		}
		
	}
	
	class LengthImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String string = TypeConvertor.toString(arguments[0]);
			Long length = (long)string.length();
			return length;
		}
		
	}
	
	class IndexOfImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String fullString = TypeConvertor.toString(arguments[0]);
			String subString = TypeConvertor.toString(arguments[1]);
			Long pos = (long)fullString.indexOf(subString);
			return pos;
		}
		
	}
	
	class ToIntImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			Object value = arguments[0];
			Object result = TypeConvertor.toInteger(value);
			return result;
		}
		
	}
	
	class ToDecimalImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			Object value = arguments[0];
			Object result = TypeConvertor.toBigDecimal(value);
			return result;
		}
		
	}
	
	class ToStringImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			Object value = arguments[0];
			Object result = TypeConvertor.toString(value);
			return result;
		}
		
	}
	
	class IIFImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			boolean controlValue = TypeConvertor.toBoolean(arguments[0]);
			Object value = null;
			if ( controlValue ) {
				value = arguments[1];
			} else if ( arguments.length > 2 ) {
				value = arguments[2];
			}
			return value;
		}
		
	}
	
	class IIFSImpl implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			Object value = null;
			int length = arguments.length;
			boolean controlValue = TypeConvertor.toBoolean(arguments[0]);
			for ( int i = 0; i<length; ++i ) {
				if ( i % 2 == 0 ) {
					controlValue = TypeConvertor.toBoolean(arguments[i]);
				} else if ( controlValue ) {
					value = arguments[i];
					break;
				}
			}
			return value;
		}
	}

	
	class GetAutoincrement implements IFunctionImpl {

		
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			
			int digit = TypeConvertor.toInteger(arguments[0]);
			
			return getAutoIncrement(digit);

		}
		/**
		 * 根据本地的环境自增
		 * @param digit
		 * @return
		 */
		private String getAutoIncrement(int digit){
			String number = null;
			String path = Parameter.DEFAULT_TEMP_PATH + "/AutoIncrement"+digit+".txt";
			try {
				
				number = FileUtil.getFileText(path);
	
				number = String.valueOf(Integer.parseInt(number) + 1);
				
				FileUtil.setFileText(path, number);
			} catch (IOException e) {
				Print.log(e.getMessage(), 2);
			}
			
			int diff = digit - number.length();
			if(diff>0){
				String diffStr = "";
				for(int i = 0 ; i < diff ; i++){
					diffStr += "0";
				}
				number = diffStr + number;
			}else{
				number = number.substring(diff);
			}
			return number;
		}
	}
	
	class GetHostName implements IFunctionImpl {

		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				return "unknow";
			}
			
			return addr.getHostName().toString();
		}
	}
	
	/**
	 * 获取今天日期 yyyy-MM-dd
	 *
	 */
	class GetDate implements IFunctionImpl {
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			return DateFormat.getDateString();
		}
	}
	
	/**
	 * 获取今天以后的日期 yyyy-MM-dd
	 *
	 */
	class GetAddDay implements IFunctionImpl {
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			int num =  Integer.parseInt(String.valueOf(arguments[0])); 
			try {
				return DateFormat.getAddDay(num);
			} catch (SeleniumException e) {
				return "";
			}
			
		}
	}
	
	/**
	 * 获取当前时间 yyyy-MM-dd 24h:mm:ss
	 *
	 */
	class GetTime implements IFunctionImpl {
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			return DateFormat.getDateToString();
		}
	}
	
	/**
	 * 获取当前时间 yyyy-MM-dd 24h:mm:ss
	 *
	 */
	class GetCurrentTimeMillis implements IFunctionImpl {
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			return DateFormat.getCurrentTimeMillis();
		}
	}
	
	/**
	 * 加密手机号：131****0000
	 *
	 */
	class EncryptPhone implements IFunctionImpl {
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments) {
			String phone =  String.valueOf(arguments[0]);
			String leftStr = phone.substring(0, 3);
			String rightStr = phone.substring(phone.length()-4, phone.length());
//			RegExp.findCharacters(phone, "^\\d{11}$");
			return leftStr + "****" + rightStr;
		}
	}
	
	/**
	 * 转化金额为 千分位+2位数字
	 *
	 */
	class NumberFormat implements IFunctionImpl{
		@Override
		public Object calc(String name, IEvalContext context, Object[] arguments){
			double str = Double.valueOf(String.valueOf(arguments[0]));
			DecimalFormat nf = new DecimalFormat(",###.00");
			return nf.format(str);
		}
	}
	
	@Override
	protected Object[][] getImplTable() {
		return new Object[][] {
				{ "in", new InImpl() },
				{ "left", new LeftImpl() },
				{ "right", new RightImpl() },
				{ "midImpl", new MidImpl() },
				{ "length", new LengthImpl() },
				{ "indexof", new IndexOfImpl() },
				{ "toint", new ToIntImpl() },
				{ "todecimal", new ToDecimalImpl() },
				{ "tostring", new ToStringImpl() },
				{ "iif", new IIFImpl() },
				{ "iifs", new IIFSImpl() },
				{ "getAutoIncrement", new GetAutoincrement() },
				{ "getHostName", new GetHostName() },
				{ "getDate", new GetDate() },
				{ "getAddDay", new GetAddDay() },
				{ "getTime", new GetTime() },
				{ "getCurrentTimeMillis", new GetCurrentTimeMillis() },
				{ "encryptPhone", new EncryptPhone() },
				{ "numberFormat", new NumberFormat() }
		};
	}

}

