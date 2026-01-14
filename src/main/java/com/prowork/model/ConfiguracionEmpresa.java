package com.prowork.model;

import java.time.LocalDateTime;

/**
 * Modelo para la configuración específica de cada empresa
 */
public class ConfiguracionEmpresa {
    private Long id;
    private Long empresaId;
    private String rutaArchivos;
    private boolean permiteGrabacionPantalla;
    private boolean permiteGrabacionAudio;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public ConfiguracionEmpresa() {
    }

    public ConfiguracionEmpresa(Long empresaId, String rutaArchivos) {
        this.empresaId = empresaId;
        this.rutaArchivos = rutaArchivos;
        this.permiteGrabacionPantalla = false;
        this.permiteGrabacionAudio = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public boolean isPermiteGrabacionPantalla() {
        return permiteGrabacionPantalla;
    }

    public void setPermiteGrabacionPantalla(boolean permiteGrabacionPantalla) {
        this.permiteGrabacionPantalla = permiteGrabacionPantalla;
    }

    public boolean isPermiteGrabacionAudio() {
        return permiteGrabacionAudio;
    }

    public void setPermiteGrabacionAudio(boolean permiteGrabacionAudio) {
        this.permiteGrabacionAudio = permiteGrabacionAudio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
