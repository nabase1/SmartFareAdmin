package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class GetUserData implements Serializable {
    private String id;
    private String Username;
    private String UserPhone;

    public GetUserData(){}
    public GetUserData(String username, String userPhone) {
        this.setId(id);
        this.setId(username);
        this.setUserPhone(userPhone);
}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
