package com.ugb.controlesbasicos;

public class amigos {
    String _id;
    String _rev;
    String idProducto;
    String codigo;
    String descripcion;
    String marca;
    String presentacion;
    String precio;
    String costo;
    String stok;
    String urlFotoProdu;

    public amigos(String _id, String _rev, String idProducto, String descripcion, String marca, String presentacion, String precio, String codigo, String costo, String stok, String urlFoto) {
        this._id = _id;
        this._rev = _rev;
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.costo = costo;
        this.stok = stok;
        this.urlFotoProdu = urlFoto;
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
    public String getUrlFotoProdu() {
        return urlFotoProdu;
    }

    public void setUrlFotoProdu(String urlFotoProdu) {
        this.urlFotoProdu = urlFotoProdu;
    }

    public String getIdproductos() {
        return idProducto;
    }

    public void setIdproductos(String idproductos) {
        this.idProducto = idproductos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }
}