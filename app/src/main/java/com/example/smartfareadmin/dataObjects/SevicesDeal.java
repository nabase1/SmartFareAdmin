package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class SevicesDeal implements Serializable {

    private String id;
    private String name;
    private String price_per_km;
    private String price_per_min;
    private String base_price;
    private String min_price;
    private String description;
    private String status;
    private String imageUrl;

    public SevicesDeal(){}

    public SevicesDeal(String name, String price_per_km, String description, String status, String min_price, String price_per_min, String base_price, String imageUrl) {
        this.setId(id);
        this.setName(name);
        this.setPrice_per_km(price_per_km);
        this.setPrice_per_min(price_per_min);
        this.setBase_price(base_price);
        this.setMin_price(min_price);
        this.setDescription(description);
        this.setStatus(status);
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

    public String getPrice_per_km() {
        return price_per_km;
    }

    public void setPrice_per_km(String price_per_km) {
        this.price_per_km = price_per_km;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setPrice_per_min(String price_per_min) {
        this.price_per_min = price_per_min;
    }

    public String getPrice_per_min() {
        return price_per_min;
    }
}
