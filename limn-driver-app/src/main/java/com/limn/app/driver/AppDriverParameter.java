package com.limn.app.driver;

/**
 * Created by limengnan on 2017/11/22.
 */
public class AppDriverParameter {

    private static ThreadLocal<AppDriver> driverConfigBean =  new ThreadLocal<>();


    public static AppDriver getDriverConfigBean() {
        if(driverConfigBean.get() == null){
            setDriverConfigBean();
        }
        return driverConfigBean.get();
    }

    public static void setAppDriver(AppDriver appDriver){
        driverConfigBean.set(appDriver);

    }

    private static void setDriverConfigBean() {
        driverConfigBean.set(new AppDriver());
    }
}
