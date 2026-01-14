package com.prowork.dao;

import com.prowork.config.AppConfig;
import com.prowork.model.Evento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DAO para manejo de eventos del sistema con soporte para base de datos MySQL
 */
public class EventoDAO {
    private static EventoDAO instance;
    private List<Evento> eventos;
    private AtomicLong idGenerator;
    
    // Optional DB connection
    private Connection dbConnection;
    private boolean useDatabase = false;

    private EventoDAO() {
        eventos = new CopyOnWriteArrayList<>();
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

    public static EventoDAO getInstance() {
        if (instance == null) {
            synchronized (EventoDAO.class) {
                if (instance == null) {
                    instance = new EventoDAO();
                }
            }
        }
        return instance;
    }

    public Evento save(Evento evento) {
        if (useDatabase && dbConnection != null) {
            String sql = "INSERT INTO Evento (tipo, descripcion, usuario, empresa_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, evento.getTipo());
                ps.setString(2, evento.getDescripcion());
                ps.setString(3, evento.getUsuario());
                if (evento.getEmpresaId() != null) {
                    ps.setLong(4, evento.getEmpresaId());
                } else {
                    ps.setNull(4, java.sql.Types.BIGINT);
                }
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        long id = keys.getLong(1);
                        evento.setId(id);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            if (evento.getId() == null) {
                evento.setId(idGenerator.getAndIncrement());
            }
            eventos.add(evento);
            return evento;
        } else {
            if (evento.getId() == null) {
                evento.setId(idGenerator.getAndIncrement());
            }
            eventos.add(evento);
            return evento;
        }
    }

    public List<Evento> findAll() {
        if (useDatabase && dbConnection != null) {
            List<Evento> result = new ArrayList<>();
            String sql = "SELECT id, tipo, descripcion, usuario, empresa_id, fecha_creacion, ruta_visualizacion FROM Evento ORDER BY fecha_creacion DESC";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evento evento = new Evento();
                    evento.setId(rs.getLong("id"));
                    evento.setTipo(rs.getString("tipo"));
                    evento.setDescripcion(rs.getString("descripcion"));
                    evento.setUsuario(rs.getString("usuario"));
                    long empId = rs.getLong("empresa_id");
                    if (!rs.wasNull()) evento.setEmpresaId(empId);
                    Timestamp ts = rs.getTimestamp("fecha_creacion");
                    if (ts != null) evento.setFechaCreacion(ts.toLocalDateTime());
                    evento.setRutaVisualizacion(rs.getString("ruta_visualizacion"));
                    result.add(evento);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            List<Evento> lista = new ArrayList<>(eventos);
            Collections.reverse(lista);
            return lista;
        }
    }

    public List<Evento> findByEmpresa(Long empresaId) {
        if (useDatabase && dbConnection != null) {
            List<Evento> result = new ArrayList<>();
            String sql = "SELECT id, tipo, descripcion, usuario, empresa_id, fecha_creacion, ruta_visualizacion FROM Evento WHERE empresa_id = ? ORDER BY fecha_creacion DESC";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, empresaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Evento evento = new Evento();
                        evento.setId(rs.getLong("id"));
                        evento.setTipo(rs.getString("tipo"));
                        evento.setDescripcion(rs.getString("descripcion"));
                        evento.setUsuario(rs.getString("usuario"));
                        evento.setEmpresaId(rs.getLong("empresa_id"));
                        Timestamp ts = rs.getTimestamp("fecha_creacion");
                        if (ts != null) evento.setFechaCreacion(ts.toLocalDateTime());
                        evento.setRutaVisualizacion(rs.getString("ruta_visualizacion"));
                        result.add(evento);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            List<Evento> lista = eventos.stream()
                    .filter(e -> e.getEmpresaId() != null && e.getEmpresaId().equals(empresaId))
                    .toList();
            List<Evento> resultado = new ArrayList<>(lista);
            Collections.reverse(resultado);
            return resultado;
        }
    }

    public List<Evento> findRecientes(int limit) {
        if (useDatabase && dbConnection != null) {
            List<Evento> result = new ArrayList<>();
            String sql = "SELECT id, tipo, descripcion, usuario, empresa_id, fecha_creacion, ruta_visualizacion FROM Evento ORDER BY fecha_creacion DESC LIMIT ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setInt(1, limit);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Evento evento = new Evento();
                        evento.setId(rs.getLong("id"));
                        evento.setTipo(rs.getString("tipo"));
                        evento.setDescripcion(rs.getString("descripcion"));
                        evento.setUsuario(rs.getString("usuario"));
                        long empId = rs.getLong("empresa_id");
                        if (!rs.wasNull()) evento.setEmpresaId(empId);
                        Timestamp ts = rs.getTimestamp("fecha_creacion");
                        if (ts != null) evento.setFechaCreacion(ts.toLocalDateTime());
                        evento.setRutaVisualizacion(rs.getString("ruta_visualizacion"));
                        result.add(evento);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            List<Evento> lista = new ArrayList<>(eventos);
            Collections.reverse(lista);
            return lista.stream().limit(limit).toList();
        }
    }

    public void registrarEvento(String tipo, String descripcion, String usuario, Long empresaId) {
        Evento evento = new Evento(tipo, descripcion, usuario, empresaId);
        save(evento);
    }
}
