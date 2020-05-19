package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class VehicleData implements Serializable {
private String id;
private String tag;
private String registrationNumber;
private String makeModel;
private String chassisNumber;
private String engineType;
private String engineNumber;
private String fuelType;
private String fuelLevel;
private String steering;
private String transmission;
private String tyresType;
private String exteriorColor;
private String interiorColor;
private String centralLock;
private String trackingDevice;
private String insurancePolicy;
private String insuranceDate;
private String roadWorthyCertificateDate;
private String manufacturingDate;
private String currentOwnership;
private String sent;
private String received;
private String status;
private String registrationDate;
private String imageUrl;


    public VehicleData(){}

    public VehicleData(String id, String tag, String registrationNumber, String makeModel, String chassisNumber, String engineType, String engineNumber, String fuelType, String fuelLevel, String steering, String transmission, String tyresType, String exteriorColor, String interiorColor, String centralLock, String trackingDevice, String insurancePolicy, String insuranceDate, String roadWorthyCertificateDate, String manufacturingDate, String currentOwnership, String sent, String received, String status, String registrationDate, String imageUrl) {
        this.setId(id);
        this.setTag(tag);
        this.setRegistrationNumber(registrationNumber);
        this.setMakeModel(makeModel);
        this.setChassisNumber(chassisNumber);
        this.setEngineType(engineType);
        this.setEngineNumber(engineNumber);
        this.setFuelType(fuelType);
        this.setFuelLevel(fuelLevel);
        this.setSteering(steering);
        this.setTransmission(transmission);
        this.setTyresType(tyresType);
        this.setExteriorColor(exteriorColor);
        this.setInteriorColor(interiorColor);
        this.setCentralLock(centralLock);
        this.setTrackingDevice(trackingDevice);
        this.setInsurancePolicy(insurancePolicy);
        this.setInsuranceDate(insuranceDate);
        this.setRoadWorthyCertificateDate(roadWorthyCertificateDate);
        this.setManufacturingDate(manufacturingDate);
        this.setCurrentOwnership(currentOwnership);
        this.setSent(sent);
        this.setReceived(received);
        this.setRegistrationDate(registrationDate);
        this.setStatus(status);

        this.setImageUrl(imageUrl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(String fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public String getSteering() {
        return steering;
    }

    public void setSteering(String steering) {
        this.steering = steering;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getTyresType() {
        return tyresType;
    }

    public void setTyresType(String tyresType) {
        this.tyresType = tyresType;
    }

    public String getExteriorColor() {
        return exteriorColor;
    }

    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }

    public String getInteriorColor() {
        return interiorColor;
    }

    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }

    public String getCentralLock() {
        return centralLock;
    }

    public void setCentralLock(String centralLock) {
        this.centralLock = centralLock;
    }

    public String getTrackingDevice() {
        return trackingDevice;
    }

    public void setTrackingDevice(String trackingDevice) {
        this.trackingDevice = trackingDevice;
    }

    public String getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(String insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getRoadWorthyCertificateDate() {
        return roadWorthyCertificateDate;
    }

    public void setRoadWorthyCertificateDate(String roadWorthyCertificateDate) {
        this.roadWorthyCertificateDate = roadWorthyCertificateDate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getCurrentOwnership() {
        return currentOwnership;
    }

    public void setCurrentOwnership(String currentOwnership) {
        this.currentOwnership = currentOwnership;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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
