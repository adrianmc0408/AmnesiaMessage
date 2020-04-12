package com.example.prueba.Objetos;

public class Usuario {
private String nombre_usuario;
private String email;
private String telefono;

    public Usuario(String nombre_usuario, String email, String telefono) {
        this.nombre_usuario = nombre_usuario;
        this.email = email;
        this.telefono = telefono;
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
}
