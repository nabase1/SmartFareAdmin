package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;
import java.util.Map;

public class Bookings implements Serializable {

   private String id;
   private String name;
   private String phoneNumber;
   private String from;
   private String to;
   private String pick_up_date;
   private String pick_up_time;
   private String amount;
   private String status;
   private String distance;
   private String duration;
   private String fromLatitude;
   private String fromLongitude;
   private String toLatitude;
   private String toLongitude;
   private String msg;
   private String totalFare;
   private String serviceType;
    private Long dateTime;

    public Bookings(){}

    public Bookings(String name, String phoneNumber, String from, String to, String pick_up_date, String pick_up_time, String amount, String status, String distance, String duration, String fromLatitude, String fromLongitude, String toLatitude, String toLongitude, String msg, String totalFare, String serviceType, Long dateTime) {
        this.setId(id);
        this.setName(name);
        this.setPhoneNumber(phoneNumber);
        this.setFrom(from);
        this.setTo(to);
        this.setPick_up_date(pick_up_date);
        this.setPick_up_time(pick_up_time);
        this.setAmount(amount);
        this.setStatus(status);
        this.setDistance(distance);
        this.setDuration(duration);
        this.setFromLatitude(fromLatitude);
        this.setFromLongitude(fromLongitude);
        this.setToLatitude(toLatitude);
        this.setToLongitude(toLongitude);
        this.setMsg(msg);
        this.setTotalFare(totalFare);
        this.setServiceType(serviceType);
        this.setDateTime(dateTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPick_up_date() {
        return pick_up_date;
    }

    public void setPick_up_date(String pick_up_date) {
        this.pick_up_date = pick_up_date;
    }

    public String getPick_up_time() {
        return pick_up_time;
    }

    public void setPick_up_time(String pick_up_time) {
        this.pick_up_time = pick_up_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(String fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public String getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(String fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public String getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(String toLatitude) {
        this.toLatitude = toLatitude;
    }

    public String getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(String toLongitude) {
        this.toLongitude = toLongitude;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
