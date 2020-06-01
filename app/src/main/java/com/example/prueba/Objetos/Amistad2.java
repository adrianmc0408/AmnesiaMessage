package com.example.prueba.Objetos;

public class Amistad2 {
    //Declaramos los atributos de la clase
    private String id;
    private String referencia;
    //Metodo onstructor vacio
    public Amistad2(){

    }
    //Metodo constructor
    public Amistad2(String id,String referencia) {
        this.referencia=referencia;
        this.id = id;
    }

    //Getters y setters de los atributos
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
