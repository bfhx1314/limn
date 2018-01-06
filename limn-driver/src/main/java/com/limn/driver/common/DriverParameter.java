package com.limn.driver.common;

import com.limn.driver.Driver;

public class DriverParameter {
	
	private static ThreadLocal<Driver> driverConfigBean = new ThreadLocal<>();


	private static void setDriverPaht() {
		driverConfigBean.set(new Driver());
	}

	public static Driver getDriverPaht() {

		if(driverConfigBean.get() == null){
			driverConfigBean.set(new Driver());
		}

		return driverConfigBean.get();
	}
	
	
}
