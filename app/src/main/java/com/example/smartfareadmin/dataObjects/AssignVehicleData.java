package com.example.smartfareadmin.dataObjects;

public class AssignVehicleData {
    private String id;
    private String driverId;
    private String vehicleId;
    private String status;
    private String cDate;
    private String service_type;
    private String pick_up_date;
    private String pick_up_time;

    public AssignVehicleData() {}

    public AssignVehicleData(String id, String driverId, String vehicleId, String status, String cDate,String service_type, String pick_up_date, String pick_up_time) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = status;
        this.cDate = cDate;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

