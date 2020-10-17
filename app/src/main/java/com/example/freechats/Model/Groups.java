package com.example.freechats.Model;

public class Groups {

    private String id;
    private String username;
    private String imageURL;
    private String hou_create;
    private String data_time;
    private String daTe;

    public Groups(String hou_create, String id, String username, String imageURL, String data_time, String daTe) {
        this.hou_create = hou_create;
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.data_time = data_time;
        this.daTe = daTe;

    }

    public Groups() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHou_create() {
        return hou_create;
    }

    public void setHou_create(String hou_create) {
        this.hou_create = hou_create;
    }

    public String getData_time() {
        return data_time;
    }

    public void setData_time(String data_time) {
        this.data_time = data_time;
    }

    public String getDaTe() {
        return daTe;
    }

    public void setDaTe(String daTe) {
        this.daTe = daTe;
    }
}
