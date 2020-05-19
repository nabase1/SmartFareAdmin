package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class driverBooking implements Serializable {
    private String id;
    private String driverName;
    private String clientName;
    private String clientNumber;
    private String from;
    private String to;
    private String price;
    private String status;


    public driverBooking() {
    }

    public driverBooking(String id, String driverName, String clientName, String clientNumber, String from, String to, String price, String status) {
        this.id = id;
        this.driverName = driverName;
        this.clientName = clientName;
        this.clientNumber = clientNumber;
        this.from = from;
        this.to = to;
        this.price = price;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


