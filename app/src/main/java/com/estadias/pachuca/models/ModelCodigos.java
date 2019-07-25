package com.estadias.pachuca.models;

public class ModelCodigos {

    private Integer id_codigo;
    private String codigo;
    private Integer id_promo;
    private String estado;

    //Para los datos del cliente
    private String nombre;
    private String correo;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(Integer id_codigo) {
        this.id_codigo = id_codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getId_promo() {
        return id_promo;
    }

    public void setId_promo(Integer id_promo) {
        this.id_promo = id_promo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
