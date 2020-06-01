package com.example.prueba.Objetos;

public class Solicitud {
    //Declaramos los atributos de la clase
    private String id_destino;
    private String id;
    private String nombre_usuario;
    private String email;
    private String telefono;
    private String url_imagen;
    //Metodo constructor vacio
    public Solicitud() {

    }
    //Metodo constructor
    public Solicitud(String id_destino, String id, String nombre_usuario, String email, String telefono, String url_imagen) {
        this.id_destino = id_destino;
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

    public String getId_destino() {
        return id_destino;
    }

    public void setId_destino(String id_destino) {
        this.id_destino = id_destino;
    }
}
