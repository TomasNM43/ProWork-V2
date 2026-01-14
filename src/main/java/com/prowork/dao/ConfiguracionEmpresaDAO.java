package com.prowork.dao;

import com.prowork.config.AppConfig;
import com.prowork.model.ConfiguracionEmpresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DAO para manejo de configuración de empresas con soporte para MySQL
 */
public class ConfiguracionEmpresaDAO {
    private static ConfiguracionEmpresaDAO instance;
    private Map<Long, ConfiguracionEmpresa> configuraciones;
    private AtomicLong idGenerator;
    
    private Connection dbConnection;
    private boolean useDatabase = false;

    private ConfiguracionEmpresaDAO() {
        configuraciones = new HashMap<>();
        idGenerator = new AtomicLong(1);
        
        // Try to initialize DB connection from global AppConfig
        try {
            AppConfig cfg = AppConfig.getInstance();
            String url = cfg.getDbUrl();
            String user = cfg.getDbUser();
            String pass = cfg.getDbPassword();
            if (url != null && !url.isEmpty()) {
                dbConnection = DriverManager.getConnection(url, user, pass);
                useDatabase = true;
            }
        } catch (SQLException e) {
            useDatabase = false;
        }
    }

    public static ConfiguracionEmpresaDAO getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionEmpresaDAO.class) {
                if (instance == null) {
                    instance = new ConfiguracionEmpresaDAO();
                }
            }
        }
        return instance;
    }

    public ConfiguracionEmpresa save(ConfiguracionEmpresa config) {
        if (useDatabase && dbConnection != null) {
            if (config.getId() == null) {
                // INSERT
                String sql = "INSERT INTO Configuracion_Empresa (empresa_id, ruta_archivos, permite_grabacion_pantalla, permite_grabacion_audio) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, config.getEmpresaId());
                    ps.setString(2, config.getRutaArchivos());
                    ps.setBoolean(3, config.isPermiteGrabacionPantalla());
                    ps.setBoolean(4, config.isPermiteGrabacionAudio());
                    ps.executeUpdate();
                    
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            config.setId(keys.getLong(1));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // UPDATE
                String sql = "UPDATE Configuracion_Empresa SET ruta_archivos=?, permite_grabacion_pantalla=?, permite_grabacion_audio=? WHERE id=?";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                    ps.setString(1, config.getRutaArchivos());
                    ps.setBoolean(2, config.isPermiteGrabacionPantalla());
                    ps.setBoolean(3, config.isPermiteGrabacionAudio());
                    ps.setLong(4, config.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if (config.getId() == null) {
                config.setId(idGenerator.getAndIncrement());
            }
            configuraciones.put(config.getEmpresaId(), config);
            return config;
        } else {
            // In-memory
            if (config.getId() == null) {
                config.setId(idGenerator.getAndIncrement());
            }
            configuraciones.put(config.getEmpresaId(), config);
            return config;
        }
    }

    public ConfiguracionEmpresa findByEmpresaId(Long empresaId) {
        if (useDatabase && dbConnection != null) {
            if (configuraciones.containsKey(empresaId)) {
                return configuraciones.get(empresaId);
            }
            
            String sql = "SELECT id, empresa_id, ruta_archivos, permite_grabacion_pantalla, permite_grabacion_audio, fecha_creacion, fecha_actualizacion FROM Configuracion_Empresa WHERE empresa_id = ? LIMIT 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, empresaId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ConfiguracionEmpresa config = new ConfiguracionEmpresa();
                        config.setId(rs.getLong("id"));
                        config.setEmpresaId(rs.getLong("empresa_id"));
                        config.setRutaArchivos(rs.getString("ruta_archivos"));
                        config.setPermiteGrabacionPantalla(rs.getBoolean("permite_grabacion_pantalla"));
                        config.setPermiteGrabacionAudio(rs.getBoolean("permite_grabacion_audio"));
                        
                        Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
                        if (tsCreacion != null) config.setFechaCreacion(tsCreacion.toLocalDateTime());
                        
                        Timestamp tsActualizacion = rs.getTimestamp("fecha_actualizacion");
                        if (tsActualizacion != null) config.setFechaActualizacion(tsActualizacion.toLocalDateTime());
                        
                        configuraciones.put(empresaId, config);
                        return config;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return configuraciones.get(empresaId);
        }
    }

    public boolean update(ConfiguracionEmpresa config) {
        return save(config) != null;
    }
}
