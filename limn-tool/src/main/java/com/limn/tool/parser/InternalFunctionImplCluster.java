package com.limn.tool.parser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.limn.tool.common.FileUtil;
import com.limn.tool.parameter.Parameter;
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
			String path = Parameter.DEFAULT_TEMP_PATH + "/AutoIncrement.txt";
			try {
				number = FileUtil.getFileText(path);
	
				number = String.valueOf(Integer.parseInt(number) + 1);
				
				FileUtil.setFileText(path, number);
			} catch (IOException e) {

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
				{ "getHostName", new GetHostName() }
		};
	}

}

