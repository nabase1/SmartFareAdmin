package com.example.smartfareadmin.dataObjects;

public class TripDetailsData {
    private String id;
    private String userId;
    private String driverId;
    private String status;
    private String totalFare;
    private String bookingId;

    public TripDetailsData() {}

    public TripDetailsData(String driverId, String userId, String status, String totalCharges, String bookingId) {
        this.id = id;
        this.driverId = driverId;
        this.userId = userId;
        this.status = status;
        this.totalFare = totalCharges;
        this.bookingId = bookingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
