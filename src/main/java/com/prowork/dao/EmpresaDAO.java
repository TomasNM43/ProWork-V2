package com.prowork.dao;

import com.prowork.config.AppConfig;
import com.prowork.model.Empresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DAO para manejo de empresas con soporte para base de datos MySQL
 */
public class EmpresaDAO {
    private static EmpresaDAO instance;
    private Map<Long, Empresa> empresas;
    private AtomicLong idGenerator;
    
    // Optional DB connection
    private Connection dbConnection;
    private boolean useDatabase = false;

    private EmpresaDAO() {
        empresas = new HashMap<>();
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

    public static EmpresaDAO getInstance() {
        if (instance == null) {
            synchronized (EmpresaDAO.class) {
                if (instance == null) {
                    instance = new EmpresaDAO();
                }
            }
        }
        return instance;
    }

    public Empresa save(Empresa empresa) {
        if (useDatabase && dbConnection != null) {
            if (empresa.getId() == null) {
                String sql = "INSERT INTO Empresa (nombre, rut, direccion, telefono, email, activa) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, empresa.getNombre());
                    ps.setString(2, empresa.getRut());
                    ps.setString(3, empresa.getDireccion());
                    ps.setString(4, empresa.getTelefono());
                    ps.setString(5, empresa.getEmail());
                    ps.setInt(6, empresa.isActiva() ? 1 : 0);
                    ps.executeUpdate();
                    
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            long id = keys.getLong(1);
                            empresa.setId(id);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String sql = "UPDATE Empresa SET nombre=?, rut=?, direccion=?, telefono=?, email=?, activa=? WHERE id=?";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                    ps.setString(1, empresa.getNombre());
                    ps.setString(2, empresa.getRut());
                    ps.setString(3, empresa.getDireccion());
                    ps.setString(4, empresa.getTelefono());
                    ps.setString(5, empresa.getEmail());
                    ps.setInt(6, empresa.isActiva() ? 1 : 0);
                    ps.setLong(7, empresa.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if (empresa.getId() == null) {
                empresa.setId(idGenerator.getAndIncrement());
            }
            empresas.put(empresa.getId(), empresa);
            return empresa;
        } else {
            if (empresa.getId() == null) {
                empresa.setId(idGenerator.getAndIncrement());
            }
            empresas.put(empresa.getId(), empresa);
            return empresa;
        }
    }

    public Empresa findById(Long id) {
        if (useDatabase && dbConnection != null) {
            if (empresas.containsKey(id)) return empresas.get(id);
            
            String sql = "SELECT id, nombre, rut, direccion, telefono, email, activa FROM Empresa WHERE id = ? LIMIT 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Empresa emp = new Empresa();
                        emp.setId(rs.getLong("id"));
                        emp.setNombre(rs.getString("nombre"));
                        emp.setRut(rs.getString("rut"));
                        emp.setDireccion(rs.getString("direccion"));
                        emp.setTelefono(rs.getString("telefono"));
                        emp.setEmail(rs.getString("email"));
                        emp.setActiva(rs.getInt("activa") == 1);
                        empresas.put(emp.getId(), emp);
                        return emp;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return empresas.get(id);
        }
    }

    public Empresa findByRut(String rut) {
        if (useDatabase && dbConnection != null) {
            String sql = "SELECT id, nombre, rut, direccion, telefono, email, activa FROM Empresa WHERE rut = ? LIMIT 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setString(1, rut);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Empresa emp = new Empresa();
                        emp.setId(rs.getLong("id"));
                        emp.setNombre(rs.getString("nombre"));
                        emp.setRut(rs.getString("rut"));
                        emp.setDireccion(rs.getString("direccion"));
                        emp.setTelefono(rs.getString("telefono"));
                        emp.setEmail(rs.getString("email"));
                        emp.setActiva(rs.getInt("activa") == 1);
                        empresas.put(emp.getId(), emp);
                        return emp;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return empresas.values().stream()
                    .filter(e -> e.getRut().equals(rut))
                    .findFirst()
                    .orElse(null);
        }
    }

    public List<Empresa> findAll() {
        if (useDatabase && dbConnection != null) {
            List<Empresa> result = new ArrayList<>();
            String sql = "SELECT id FROM Empresa";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empresa emp = findById(rs.getLong("id"));
                    if (emp != null) result.add(emp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return new ArrayList<>(empresas.values());
        }
    }

    public List<Empresa> findActivas() {
        if (useDatabase && dbConnection != null) {
            List<Empresa> result = new ArrayList<>();
            String sql = "SELECT id FROM Empresa WHERE activa = 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empresa emp = findById(rs.getLong("id"));
                    if (emp != null) result.add(emp);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return empresas.values().stream()
                    .filter(Empresa::isActiva)
                    .toList();
        }
    }

    public boolean update(Empresa empresa) {
        if (empresa.getId() != null && empresas.containsKey(empresa.getId())) {
            empresas.put(empresa.getId(), empresa);
            save(empresa);
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        if (useDatabase && dbConnection != null) {
            String sql = "DELETE FROM Empresa WHERE id = ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return empresas.remove(id) != null;
    }
}
