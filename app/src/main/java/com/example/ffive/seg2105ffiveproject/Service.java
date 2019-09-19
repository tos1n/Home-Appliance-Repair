package com.example.ffive.seg2105ffiveproject;

public class Service {
    private String serviceName;
    private double ratePerHour;

    public Service(String serviceName, double ratePerHour){
        this.serviceName = serviceName;
        this.ratePerHour = ratePerHour;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return this.serviceName;
    }
    public void setRate(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }
    public double getRate() {
        return ratePerHour;
    }
}
