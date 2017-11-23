package com.limn.tool.struct;

import java.util.LinkedHashMap;

public class LinkedHashMapIgnoreCase<V> extends LinkedHashMap<KeyIgnoreCase, V> {
	private static final long serialVersionUID = 1L;
	public boolean containsKey(String key) {
		return super.containsKey(new KeyIgnoreCase(key));
	}

	public V get(String key) {
		return super.get(new KeyIgnoreCase(key));
	}

	public V put(String key, V value) {
		return super.put(new KeyIgnoreCase(key), value);
	}

	public V remove(String key) {
		return super.remove(new KeyIgnoreCase(key));
	}

	public boolean exists(String key) {
		return containsKey(key);
	}
}
