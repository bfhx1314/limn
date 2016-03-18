package com.limn.tool.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.limn.tool.struct.HashMapIgnoreCase;
import com.limn.tool.struct.KeyIgnoreCase;

/**
 * BaseFunctionImplCluster是所有函数簇的基类，避免外部重写编写同样实现代码。其主要作用是用来外部扩展函数名称和函数实现的配对。
 * 派生类只需要实现getImplTable函数返回一个Object[][]的数组，数组中的每一行中最后一个为函数的实现，之前的为函数名称，例如。
 * <p><pre>
 * class MyFunction extends BaseFunctionImplCluster {
 *     public MyFunction() {
 *         super();
 *     }
 *     
 *     public Object[][] getImplTable() {
 *         return new Object[][] {
 *             { "set1", "set2", new SetImpl() },
 *             { "get", new GetImpl() }
 *         }
 *     }
 * }
 * </p></pre>
 * @author 王元和
 * @since YES1.0
 */
public abstract class BaseFunctionImplCluster implements IFunctionImplCluster {
	private HashMapIgnoreCase<IFunctionImpl> implMap = null;
	public BaseFunctionImplCluster() {
		implMap = new HashMapIgnoreCase<IFunctionImpl>();
		init();
	}
	
	private void init() {
		Object[][] implTable = getImplTable();
		int length = implTable.length;
		for ( int i = 0; i<length; ++i ) {
			Object[] line = implTable[i];
			int nameCount = line.length - 1;
			IFunctionImpl impl = (IFunctionImpl)line[nameCount];
			for ( int j = 0; j<nameCount; ++j ) {
				String name = (String)line[j];
				implMap.put(name, impl);
			}
		}
	}
	
	/**
	 * 返回函数名称和实现的查找表，返回值的格式如下
	 * <p><pre>
	 * [
	 *     [ "name" , "name" ... impl ]
	 *     [ "name", "name" ... impl ]
	 * ]
	 * </pre></p>
	 * @return
	 */
	protected abstract Object[][] getImplTable();
	
	@Override
	public Object calc(String name, IEvalContext context, Object[] arguments) {
		Object result = null;
		IFunctionImpl impl = implMap.get(name);
		if ( impl != null ) {
			result = impl.calc(name, context, arguments);
		}
		return result;
	}

	@Override
	public Collection<String> getFunctionNames() {
		List<String> nameList = new ArrayList<String>();
		Entry<KeyIgnoreCase, IFunctionImpl> entry = null;
		Iterator<Entry<KeyIgnoreCase, IFunctionImpl>> it = implMap.entrySet().iterator();
		while ( it.hasNext() ) {
			entry = it.next();
			nameList.add(entry.getKey().getKey());
		}
		return nameList;
	}

}
