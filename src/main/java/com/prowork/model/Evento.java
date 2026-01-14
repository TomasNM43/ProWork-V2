package com.prowork.model;

import java.time.LocalDateTime;

/**
 * Clase modelo para Evento del sistema
 */
public class Evento {
    private Long id;
    private String tipo;
    private String descripcion;
    private String usuario;
    private Long empresaId;
    private LocalDateTime fechaCreacion;
    private String rutaVisualizacion;

    public Evento() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Evento(String tipo, String descripcion, String usuario, Long empresaId) {
        this();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.empresaId = empresaId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getRutaVisualizacion() {
        return rutaVisualizacion;
    }

    public void setRutaVisualizacion(String rutaVisualizacion) {
        this.rutaVisualizacion = rutaVisualizacion;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", usuario='" + usuario + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
