package com.limn.tool.common;

import java.io.File;

public class IOSPackageInfo {
	private String CFBundleVersion = null;
	private String CFBundleIdentifier = null;
	private String CFBundleName = null;
	private String CFBundleDisplayName = null;
	private File icon = null;
	private File appPackage = null;
	
	public String getCFBundleVersion() {
		return CFBundleVersion;
	}
	public void setCFBundleVersion(String cFBundleVersion) {
		CFBundleVersion = cFBundleVersion;
	}
	public String getCFBundleIdentifier() {
		return CFBundleIdentifier;
	}
	public void setCFBundleIdentifier(String cFBundleIdentifier) {
		CFBundleIdentifier = cFBundleIdentifier;
	}
	public String getCFBundleName() {
		return CFBundleName;
	}
	public void setCFBundleName(String cFBundleName) {
		CFBundleName = cFBundleName;
	}
	public String getCFBundleDisplayName() {
		return CFBundleDisplayName;
	}
	public void setCFBundleDisplayName(String cFBundleDisplayName) {
		CFBundleDisplayName = cFBundleDisplayName;
	}
	public File getIcon() {
		return icon;
	}
	public void setIcon(File icon) {
		this.icon = icon;
	}
	public File getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(File appPackage) {
		this.appPackage = appPackage;
	}
	


}
