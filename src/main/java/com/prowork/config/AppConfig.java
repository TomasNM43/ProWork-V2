package com.prowork.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Clase de configuración de la aplicación
 */
public class AppConfig {
    private static AppConfig instance;
    private Properties properties;
    private String documentosPath;

    private AppConfig() {
        properties = new Properties();
        loadProperties();
        initializeDocumentosPath();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                // Valores por defecto si no existe el archivo
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error al cargar application.properties: " + e.getMessage());
            setDefaultProperties();
        }
    }

    private void setDefaultProperties() {
        // Ruta por defecto para documentos
        String userHome = System.getProperty("user.home");
        properties.setProperty("documentos.path", userHome + "/ProWork/documentos");
        properties.setProperty("documentos.maxSize", "10485760"); // 10 MB en bytes
        properties.setProperty("documentos.allowedExtensions", ".pdf,.doc,.docx,.xls,.xlsx,.txt,.jpg,.jpeg,.png");

        // Default DB properties (can be overridden in application.properties)
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/ginnet_prowork?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        properties.setProperty("db.user", "root");
        properties.setProperty("db.password", "");
    }

    private void initializeDocumentosPath() {
        documentosPath = properties.getProperty("documentos.path");
        
        // Crear directorio si no existe
        try {
            Path path = Paths.get(documentosPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Directorio de documentos creado: " + documentosPath);
            }
        } catch (IOException e) {
            System.err.println("Error al crear directorio de documentos: " + e.getMessage());
        }
    }

    public String getDocumentosPath() {
        return documentosPath;
    }

    public void setDocumentosPath(String newPath) {
        this.documentosPath = newPath;
        properties.setProperty("documentos.path", newPath);
        
        // Crear el nuevo directorio
        try {
            Path path = Paths.get(newPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Error al crear nuevo directorio de documentos: " + e.getMessage());
        }
    }

    public long getMaxFileSize() {
        return Long.parseLong(properties.getProperty("documentos.maxSize", "10485760"));
    }

    public String[] getAllowedExtensions() {
        String extensions = properties.getProperty("documentos.allowedExtensions", ".pdf,.doc,.docx");
        return extensions.split(",");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUser() {
        return properties.getProperty("db.user");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
