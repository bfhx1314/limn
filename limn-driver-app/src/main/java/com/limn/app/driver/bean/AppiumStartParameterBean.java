package com.limn.app.driver.bean;

/**
 * Created by limengnan on 2017/11/22.
 */
public class AppiumStartParameterBean {
    private String address = null;
    private String port = null;
    private String udid = null;
    private String bootstrapPort = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getBootstrapPort() {
        return bootstrapPort;
    }

    public void setBootstrapPort(String bootstrapPort) {
        this.bootstrapPort = bootstrapPort;
    }

    public String toString(){
        String toString = getAddress() == null ? "" : " -a " + getAddress();
        toString = toString + (getPort() == null ? "" : " -p " + getPort());
        toString = toString + (getUdid() == null ? "" :" -U " + getUdid());
        toString = toString + (getBootstrapPort() == null ? "" :" -bp " + getBootstrapPort());
        return toString;
    }


}
