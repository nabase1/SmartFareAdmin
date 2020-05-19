package com.example.smartfareadmin;

public class KeyValues {

    private static String services;
    private static String BOOKING;
    private static String CHOOSE;
    private static String PRICE;

    public static String getPRICE() {
        return PRICE;
    }

    public static void setPRICE(String PRICE) {
        KeyValues.PRICE = PRICE;
    }

    public void StringValues(){
    }

    public static String getSERVICES() {
        return services;
    }

    public static void setSERVICES(String SERVICES) {
        KeyValues.services = SERVICES;
    }

    public static String getBOOKING() {
        return BOOKING;
    }

    public static void setBOOKING(String BOOKING) {
        KeyValues.BOOKING = BOOKING;
    }

    public static String getCHOOSE() {
        return CHOOSE;
    }

    public static void setCHOOSE(String CHOOSE) {
        KeyValues.CHOOSE = CHOOSE;
    }
}
