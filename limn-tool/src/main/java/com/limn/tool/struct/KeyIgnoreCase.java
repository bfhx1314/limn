package com.limn.tool.struct;

public class KeyIgnoreCase {
	private String key = null;
	private String lowerCaseKey = null;
	public KeyIgnoreCase(String key) {
		this.key = key;
		lowerCaseKey = key.toLowerCase();
	}
	
	@Override
	public int hashCode() {
		return lowerCaseKey.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (key == null) {
			return ((KeyIgnoreCase) obj).getKey() == null;
		}
		if (obj != null && obj instanceof KeyIgnoreCase) {
			return key.equalsIgnoreCase(((KeyIgnoreCase) obj).getKey());
		}
		return false;
	}
	
	public String getKey() {
		return key;
	}
}
