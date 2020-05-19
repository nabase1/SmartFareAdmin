package com.example.smartfareadmin.dataObjects;

import java.io.Serializable;

public class CodeObjects implements Serializable {

    private String id;
    private String code;
    private String status;

    public CodeObjects(){}

    public CodeObjects(String id, String code, String status) {
        this.setId(id);
        this.setCode(code);
        this.setStatus(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
