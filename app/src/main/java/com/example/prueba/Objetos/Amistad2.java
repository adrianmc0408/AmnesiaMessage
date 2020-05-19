package com.example.prueba.Objetos;

public class Amistad2 {
    private String id;
    private String referencia;

    public Amistad2(){

    }

    public Amistad2(String id,String referencia) {
        this.referencia=referencia;
        this.id = id;
    }

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
