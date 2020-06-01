package com.example.prueba.Objetos;

import java.io.Serializable;

//La clase hereda de Serializable
public class Usuario2 implements Serializable {
    //Declaramos los atributos de la clase
    private String referencia;
    private String id;
    private String nombre_usuario;
    private String email;
    private String telefono;
    private String url_imagen;

    //Metodo constructor vacio
    public Usuario2(){

    }

    //Metodo constructor
    public Usuario2(String referencia, String id, String nombre_usuario, String email, String telefono, String url_imagen) {
        this.referencia = referencia;
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.email = email;
        this.telefono = telefono;
        this.url_imagen = url_imagen;
    }
    //Getters y setters de los atributos
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}