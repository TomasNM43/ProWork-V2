-- Script para corregir el tipo de la columna 'rol' en la tabla usuario
-- La columna actualmente es ENUM('ADMINISTRADOR', 'SUPERVISOR') pero necesita incluir 'EMPLEADO'
-- Ejecutar este script si obtienes el error: Data truncated for column 'rol'

USE ginnet_prowork;

-- Opción 1: Cambiar de ENUM a VARCHAR para mayor flexibilidad (RECOMENDADO)
ALTER TABLE usuario MODIFY COLUMN rol VARCHAR(50) NOT NULL DEFAULT 'EMPLEADO';

-- Opción 2 (comentada): Si prefieres mantener ENUM, agrega 'EMPLEADO'
-- ALTER TABLE usuario MODIFY COLUMN rol ENUM('ADMINISTRADOR', 'SUPERVISOR', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO';

-- Verificar el cambio
DESCRIBE usuario;
