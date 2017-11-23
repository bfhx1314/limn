package com.limn.tool.parser;

import java.util.Collection;
import java.util.Iterator;

import com.limn.tool.struct.HashMapIgnoreCase;

/**
 * DefaultFunctionImplMap为系统默认提供的函数实现映射，其在内部存储一个函数名称和IFunctionImpl的对照表，
 * 并提供注册服务。
 * <p>
 * DefaultFunctionImplMap类不能通过new DefaultFunctionImplMap()去创建，
 * 只能通过DefaultFunctionImplMap提供的getInstance静态方法去获得。
 * 这样实现的目的是为了注册内部实现函数。
 * </p>
 * @author 王元和
 * @since YES1.0
 */
public class DefaultFunctionImplMap implements IFunctionImplMap {
	private HashMapIgnoreCase<IFunctionImpl> functionImplMap = null;
	private DefaultFunctionImplMap() {
		functionImplMap = new HashMapIgnoreCase<IFunctionImpl>();
	}
	
	/**
	 * 注册单个的函数名称和函数实现
	 * @param name 函数的名称
	 * @param impl 实现类，IFunctionImpl的实现类对象
	 */
	public void regFunctionImpl(String name, IFunctionImpl impl) {
		functionImplMap.put(name, impl);
	}
	
	/**
	 * 注册一个函数簇
	 * @param cluster 函数簇实现类对象
	 */
	public void regFunctionImplCluster(IFunctionImplCluster cluster) {
		Collection<String> functionNames = cluster.getFunctionNames();
		String name = null;
		Iterator<String> it = functionNames.iterator();
		while ( it.hasNext() ) {
			name = it.next();
			functionImplMap.put(name, cluster);
		}
	}
	
	@Override
	public boolean containFunction(String name) {
		return functionImplMap.containsKey(name);
	}
	
	@Override
	public IFunctionImpl getFunctionImpl(String name) {
		return functionImplMap.get(name);
	}
	
	/**
	 * 单体的实例
	 */
	private static DefaultFunctionImplMap INSTANCE = null;
	
	/**
	 * 获得默认函数映射实例，如果INSTANCE不存在，会自己创建一个，并注册内部函数实现。
	 * @return
	 */
	public static final DefaultFunctionImplMap getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new DefaultFunctionImplMap();
			INSTANCE.regFunctionImplCluster(new InternalFunctionImplCluster());
		}
		return INSTANCE;
	}
	
	/**
	 * 初始化，主要是用于创建INSTANCE实例
	 */
	public static void init() {
		DefaultFunctionImplMap.getInstance();
	}
}
