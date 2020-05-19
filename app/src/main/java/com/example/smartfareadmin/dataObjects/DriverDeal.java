package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class DriverDeal implements Serializable {

    private String id;
    private String name;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String address;
    private String driverLicense;
    private String licenseExpireDate;
    private String vehicle;
    private String status;
    private String registrationDate;
    private String imageUrl;

    public DriverDeal(){}

    public DriverDeal( String name, String displayName, String email, String phoneNumber, String address, String driverLicense, String licenseExpireDate, String vehicle, String status, String registrationDate, String imageUrl) {
        this.setId(id);
        this.setName(name);
        this.setDisplayName(displayName);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setAddress(address);
        this.setDriverLicense(driverLicense);
        this.setLicenseExpireDate(licenseExpireDate);
        this.setVehicle(vehicle);
        this.setStatus(status);
        this.setRegistrationDate(registrationDate);
        this.setImageUrl(imageUrl);
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(String licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
}
