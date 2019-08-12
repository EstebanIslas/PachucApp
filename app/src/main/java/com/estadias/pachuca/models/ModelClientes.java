package com.estadias.pachuca.models;

/**
 * Mapea la informacion para mostrarla en pantalla o enviarle algun valor
 */
public class ModelClientes {

    private Integer id_cliente;
    private String nombre;
    private  String correo;
    private String password;
    private String clave_ine;

    public Integer getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Integer id_cliente) {
        this.id_cliente = id_cliente;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClave_ine() {
        return clave_ine;
    }

    public void setClave_ine(String ruta_imagen) {
        this.clave_ine= ruta_imagen;
    }
}
