package com.ugb.controlesbasicos;

public class amigos {
    String _id;
    String _rev;
    String idCarro;
    String marca;
    String motor;
    String chasis;
    String VIN;
    String combustion;
    String urlFotoCarro;

    public amigos(String _id, String _rev, String idCarro, String marca, String motor, String chasis, String VIN, String combustion, String urlFoto) {
        this._id = _id;
        this._rev = _rev;
        this.idCarro= idCarro;
        this.marca = marca;
        this.motor = motor;
        this.chasis = chasis;
        this.VIN = VIN;
        this.combustion = combustion;
        this.urlFotoCarro = urlFoto;
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String get_rev() {
        return _rev;
    }
    public void set_rev(String _rev) {
        this._rev = _rev;
    }
    public String getUrlFotoCarro() {
        return urlFotoCarro;
    }

    public void setUrlFotoCarro(String urlFotoCarro) {
        this.urlFotoCarro = urlFotoCarro;
    }

    public String getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(String idCarro) {
        this.idCarro = idCarro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getCombustion() {
        return combustion;
    }

    public void setCombustion(String combustion) {
        this.combustion = combustion;
    }
}