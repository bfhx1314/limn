package com.limn.driver.common;

import com.limn.driver.Driver;

public class DriverParameter {
	
	private static ThreadLocal<Driver> driverConfigBean = new ThreadLocal<>();



	public static void setDriverPaht(Driver driverPaht) {
		driverConfigBean.set(driverPaht);
	}

	public static Driver getDriverPaht() {

		if(driverConfigBean.get() == null){
			driverConfigBean.set(new Driver());
		}

		return driverConfigBean.get();
	}
	
	
}
