package com.ugb.controlesbasicos;

public class amigos {
    String idAmigo;
    String nombre;
    String direccion;
    String email;
    String urlFotoAmigo;
    String urlFotoAmigoFirestore;
    String token;

    public amigos(String idAmigo, String nombre, String direccion, String email, String urlFoto, String urlFotoAmigoFirestore, String token) {
        this.idAmigo = idAmigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
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

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}