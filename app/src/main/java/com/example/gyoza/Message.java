package com.example.gyoza;

public class Message {
    public String atcreate;
    public String message;
    public String user;
    public String categori;
    public String station;


    public Message(){

    };

    public Message(String atcreate , String message, String user,String categori,String station){
        this.atcreate = atcreate;
        this.message = message;
        this.user = user;
        this.categori = categori;
        this.station = station;
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

    public String getStation(){ return station;}

}
