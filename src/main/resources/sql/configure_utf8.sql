-- Script para configurar la base de datos con soporte completo UTF-8
-- Este script asegura que toda la base de datos y tablas soporten caracteres UTF-8

-- Alterar la base de datos para usar UTF-8
ALTER DATABASE ginnet_prowork CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar y actualizar el charset de las tablas existentes
ALTER TABLE Empresa CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE usuario CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE Evento CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Si existen las tablas de Perfil y ConfiguracionEmpresa, también convertirlas
ALTER TABLE Perfil CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE ConfiguracionEmpresa CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
