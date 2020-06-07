package com.example.gyoza;

public class Message {
    public String atcreate;
    public String message;
    public String user;
    public String categori;


    public Message(){

    };

    public Message(String atcreate , String message, String user,String categori){
        this.atcreate = atcreate;
        this.message = message;
        this.user = user;
        this.categori = categori;
    }

    public String getAtcreate(){
        return atcreate;
    };

    public String getCategori() {
        return categori;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

}
