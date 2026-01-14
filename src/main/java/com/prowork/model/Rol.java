package com.prowork.model;

/**
 * Enum para los roles del sistema
 */
public enum Rol {
    ADMINISTRADOR("Administrador"),
    SUPERVISOR("Supervisor"),
    EMPLEADO("Empleado");

    private final String nombre;

    Rol(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
