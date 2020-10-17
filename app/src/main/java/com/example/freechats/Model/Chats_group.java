package com.example.freechats.Model;

public class Chats_group {

    private String sender;
    private String receiver;
    private String message;
    private String daTe;
    private boolean isseen;
    private String username;
    private String daTe3;
    private String date16;

    public Chats_group(String sender, String receiver, String message, boolean isseen,  String username, String daTe, String daTe3, String date16) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.username = username;
        this.daTe = daTe;
        this.daTe3 = daTe3;
        this.date16 = date16;
    }

    public Chats_group() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDaTe() {
        return daTe;
    }

    public void setDaTe(String daTe) {
        this.daTe = daTe;
    }

    public String getDaTe3() {
        return daTe3;
    }

    public void setDaTe3(String daTe3) {
        this.daTe3 = daTe3;
    }

    public String getDate16() {
        return date16;
    }

    public void setDate16(String date16) {
        this.date16 = date16;
    }
}
