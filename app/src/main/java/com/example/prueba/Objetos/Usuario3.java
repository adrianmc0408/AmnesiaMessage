package com.example.prueba.Objetos;

import android.location.Location;

import java.io.Serializable;
//La clase hereda de Serializable
public class Usuario3 implements Serializable {
    //Declaramos los atributos de la clase
    private double latitud;
    private double longitud;
    private boolean localizable;
    private String id;
    private String nombre_usuario;
    private String email;
    private String telefono;
    private String url_imagen;
    //Metodo constructor vacio
    public Usuario3(){

    }
    //Metodo constructor
    public Usuario3(double latitud, double longitud, boolean localizable, String id, String nombre_usuario, String email, String telefono, String url_imagen) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.localizable = localizable;
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.email = email;
        this.telefono = telefono;
        this.url_imagen = url_imagen;
    }
    //Getters y setters de los atributos
    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }


    public boolean isLocalizable() {
        return localizable;
    }

    public void setLocalizable(boolean localizable) {
        this.localizable = localizable;
    }
}