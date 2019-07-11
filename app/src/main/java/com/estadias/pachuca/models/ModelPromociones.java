package com.estadias.pachuca.models;

public class ModelPromociones {

    private Integer id_promo;
    private String titulo;
    private String descripcion;
    private Integer id;

    public Integer getId_promo() {
        return id_promo;
    }

    public void setId_promo(Integer id_promo) {
        this.id_promo = id_promo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
