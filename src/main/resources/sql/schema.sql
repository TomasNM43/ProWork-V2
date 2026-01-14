-- Script para crear las tablas de ProWork
-- Base de datos: ginnet_prowork

-- Tabla de Empresas
CREATE TABLE IF NOT EXISTS Empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rut VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(500),
    telefono VARCHAR(50),
    email VARCHAR(255),
    activa TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    rol VARCHAR(50) NOT NULL,
    empresa_id BIGINT NULL,
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuarios_empresa FOREIGN KEY (empresa_id) REFERENCES Empresa(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla de Eventos
CREATE TABLE IF NOT EXISTS Evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    usuario VARCHAR(100),
    empresa_id BIGINT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eventos_empresa FOREIGN KEY (empresa_id) REFERENCES Empresa(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertar usuario administrador por defecto (password: admin123)
-- El hash BCrypt es: $2a$10$YliDUrJZLkT/eSCZXazOaOeLp7z8QEVpOvSwm0MEO0tQ4rbXlb6B2
INSERT INTO usuario (username, password, email, nombre, apellido, rol, activo) 
VALUES ('admin', '$2a$10$YliDUrJZLkT/eSCZXazOaOeLp7z8QEVpOvSwm0MEO0tQ4rbXlb6B2', 
        'admin@prowork.com', 'Administrador', 'Sistema', 'ADMINISTRADOR', 1)
ON DUPLICATE KEY UPDATE username=username;
