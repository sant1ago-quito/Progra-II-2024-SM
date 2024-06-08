package com.ugb.controlesbasicos;

import java.util.Base64;


public class utilidades {
    static String urlConsulta = "http://192.168.0.25:5984/carros/_design/carros/_view/carros"; /*cambiar*/
    static String urlMto = "http://192.168.0.25:5984/carros"; /*cambiar*/
    static String user = "admin";
    static String passwd = "12345";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}