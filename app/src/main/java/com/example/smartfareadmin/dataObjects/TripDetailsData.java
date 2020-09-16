package com.example.smartfareadmin.dataObjects;

public class TripDetailsData {
    private String id;
    private String userId;
    private String driverId;
    private String status;
    private String totalFare;
    private String bookingId;
    private String vehicleId;
    private Long time_stamp;
    private String service_type;
    private String pick_up_date;
    private String pick_up_time;

    public TripDetailsData() {}

    public TripDetailsData(String driverId, String userId, String status, String totalCharges,
                           String bookingId, String vehicleId, Long time_stamp, String service_type, String pick_up_date, String pick_up_time) {
        this.id = id;
        this.driverId = driverId;
        this.userId = userId;
        this.status = status;
        this.totalFare = totalCharges;
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.time_stamp = time_stamp;
        this.service_type = service_type;
        this.pick_up_date = pick_up_date;
        this.pick_up_time = pick_up_time;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
    }
}
