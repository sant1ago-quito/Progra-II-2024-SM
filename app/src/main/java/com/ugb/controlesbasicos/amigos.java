package com.ugb.controlesbasicos;

public class amigos {
    String idNota;
    String nombre;
    String titulo;
    String emocion;
    String contenido;
    String urlFotoAmigo;
    String urlFotoAmigoFirestore;
    String token;

    public amigos(){}
    public amigos(String idNota, String nombre, String titulo, String emocion, String contenido, String urlFoto, String urlFotoAmigoFirestore, String token) {
        this.idNota = idNota;
        this.nombre = nombre;
        this.titulo = titulo;
        this.emocion = emocion;
        this.contenido = contenido;
        this.urlFotoAmigo = urlFoto;
        this.urlFotoAmigoFirestore = urlFotoAmigoFirestore;
        this.token = token;
    }

    public String getUrlFotoAmigoFirestore() {
        return urlFotoAmigoFirestore;
    }

    public void setUrlFotoAmigoFirestore(String urlFotoAmigoFirestore) {
        this.urlFotoAmigoFirestore = urlFotoAmigoFirestore;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrlFotoAmigo() {
        return urlFotoAmigo;
    }

    public void setUrlFotoAmigo(String urlFotoAmigo) {
        this.urlFotoAmigo = urlFotoAmigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdNota() {
        return idNota;
    }

    public void setIdNota(String idNota) {
        this.idNota = idNota;
    }

    public String getEmocion() {
        return emocion;
    }

    public void setEmocion(String emocion) {
        this.emocion = emocion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}