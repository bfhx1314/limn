package com.limn.driver.common;

import com.limn.driver.Driver;

public class DriverParameter {
	
	private static ThreadLocal<Driver> driverConfigBean = new ThreadLocal<>();
	
	public static void setDriverPaht() {
		driverConfigBean.set(new Driver());
	}

	public static Driver getDriverPaht() {
		return driverConfigBean.get();
	}
	
	
}
