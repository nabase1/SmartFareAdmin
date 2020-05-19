package com.example.smartfareadmin.dataObjects;

public class DriverLocation {
    private String id;
    private String locationName;
    private String longintude;
    private String latitude;
    private String vehicle;
    private String name;

    public DriverLocation() {}

    public DriverLocation(String id, String locationName, String longintude, String latitude, String vehicle, String name) {
        this.id = id;
        this.locationName = locationName;
        this.longintude = longintude;
        this.latitude = latitude;
        this.vehicle = vehicle;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLongintude() {
        return longintude;
    }

    public void setLongintude(String longintude) {
        this.longintude = longintude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
