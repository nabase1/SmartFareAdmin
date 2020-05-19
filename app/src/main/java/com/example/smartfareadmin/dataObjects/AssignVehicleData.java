package com.example.smartfareadmin.dataObjects;

public class AssignVehicleData {
    private String id;
    private String driverId;
    private String vehicleId;
    private String status;
    private String cDate;

    public AssignVehicleData() {}

    public AssignVehicleData(String id, String driverId, String vehicleId, String status, String cDate) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = status;
        this.cDate = cDate;
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

