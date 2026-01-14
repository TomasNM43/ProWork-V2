-- Tabla para configuración específica de cada empresa
CREATE TABLE IF NOT EXISTS Configuracion_Empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    ruta_archivos VARCHAR(500) NOT NULL,
    permite_grabacion_pantalla BOOLEAN DEFAULT FALSE,
    permite_grabacion_audio BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_config_empresa FOREIGN KEY (empresa_id) REFERENCES Empresa(id) ON DELETE CASCADE,
    CONSTRAINT unique_empresa_config UNIQUE (empresa_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índice para búsquedas rápidas por empresa
CREATE INDEX idx_config_empresa_id ON Configuracion_Empresa(empresa_id);
