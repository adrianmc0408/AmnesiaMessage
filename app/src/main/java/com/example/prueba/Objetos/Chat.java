package com.example.prueba.Objetos;

import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private Date fecha;

    public Chat(String sender, String receiver, String message, Date fecha) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.fecha = fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
