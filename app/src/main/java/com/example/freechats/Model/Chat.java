package com.example.freechats.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String daTe, daTe2, daTe3;
    private String fotoURL;
    private boolean isseen;
    private String date16;

    public Chat(String sender, String receiver, String message, boolean isseen, String daTe, String daTe2,String daTe3, String fotoURL, String date16) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.daTe = daTe;
        this.daTe2 = daTe2;
        this.daTe3 = daTe3;
        this.fotoURL = fotoURL;
        this.date16 = date16;
    }

    public Chat() {
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

    public String getDaTe() {
        return daTe;
    }

    public void setDaTe(String daTe) {
        this.daTe = daTe;
    }

    public String getDaTe2() {
        return daTe2;
    }

    public void setDaTe2(String daTe2) {
        this.daTe2 = daTe2;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
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