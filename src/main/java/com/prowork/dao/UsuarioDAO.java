package com.prowork.dao;

import com.prowork.config.AppConfig;
import com.prowork.model.Rol;
import com.prowork.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
 * DAO para manejo de usuarios
 * Implementación en memoria con soporte opcional para base de datos MySQL
 */
public class UsuarioDAO {
    private static UsuarioDAO instance;
    private Map<Long, Usuario> usuarios;
    private AtomicLong idGenerator;

    // Optional DB connection
    private Connection dbConnection;
    private boolean useDatabase = false;
    private BCryptPasswordEncoder passwordEncoder;

    private UsuarioDAO() {
        usuarios = new HashMap<>();
        idGenerator = new AtomicLong(1);
        passwordEncoder = new BCryptPasswordEncoder();

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

        if (!useDatabase) {
            initializeDefaultUsers();
        } else {
            // Optionally ensure an admin user exists in-memory cache by loading from DB
            try {
                Usuario admin = findByUsername("admin");
                if (admin == null) {
                    // do not auto-insert into DB to avoid duplicates; keep in-memory admin
                    initializeDefaultUsers();
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            synchronized (UsuarioDAO.class) {
                if (instance == null) {
                    instance = new UsuarioDAO();
                }
            }
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        // Usuario administrador por defecto
        Usuario admin = new Usuario("admin", "admin123", "admin@prowork.com", "Administrador", "Sistema", Rol.ADMINISTRADOR);
        save(admin);
    }

    /**
     * Guarda o actualiza un usuario. Si la configuración de DB está activa, persiste en la tabla Usuario
     */
    public Usuario save(Usuario usuario) {
        if (useDatabase && dbConnection != null) {
            String pwd = usuario.getPassword();
            String toStore = null;
            if (pwd != null) {
                if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$")) {
                    toStore = pwd;
                } else {
                    toStore = passwordEncoder.encode(pwd);
                }
            }

            if (usuario.getId() == null) {
                String sql = "INSERT INTO Usuario (username, password, email, nombre, apellido, rol, empresa_id, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, usuario.getUsername());
                    ps.setString(2, toStore);
                    ps.setString(3, usuario.getEmail());
                    ps.setString(4, usuario.getNombre());
                    ps.setString(5, usuario.getApellido());
                    ps.setString(6, usuario.getRol() != null ? usuario.getRol().name() : null);
                    if (usuario.getEmpresaId() != null) ps.setLong(7, usuario.getEmpresaId()); else ps.setNull(7, java.sql.Types.BIGINT);
                    ps.setInt(8, usuario.isActivo() ? 1 : 0);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            long id = keys.getLong(1);
                            usuario.setId(id);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String sql = "UPDATE Usuario SET username=?, password=?, email=?, nombre=?, apellido=?, rol=?, empresa_id=?, activo=? WHERE id=?";
                try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                    ps.setString(1, usuario.getUsername());
                    ps.setString(2, toStore);
                    ps.setString(3, usuario.getEmail());
                    ps.setString(4, usuario.getNombre());
                    ps.setString(5, usuario.getApellido());
                    ps.setString(6, usuario.getRol() != null ? usuario.getRol().name() : null);
                    if (usuario.getEmpresaId() != null) ps.setLong(7, usuario.getEmpresaId()); else ps.setNull(7, java.sql.Types.BIGINT);
                    ps.setInt(8, usuario.isActivo() ? 1 : 0);
                    ps.setLong(9, usuario.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (usuario.getId() == null) {
                usuario.setId(idGenerator.getAndIncrement());
            }
            usuarios.put(usuario.getId(), usuario);
            return usuario;
        } else {
            if (usuario.getId() == null) {
                usuario.setId(idGenerator.getAndIncrement());
            }
            usuarios.put(usuario.getId(), usuario);
            return usuario;
        }
    }

    public Usuario findById(Long id) {
        if (useDatabase && dbConnection != null) {
            if (usuarios.containsKey(id)) return usuarios.get(id);
            String sql = "SELECT id, username, password, email, nombre, apellido, rol, empresa_id, activo FROM Usuario WHERE id = ? LIMIT 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = new Usuario();
                        u.setId(rs.getLong("id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password"));
                        u.setEmail(rs.getString("email"));
                        u.setNombre(rs.getString("nombre"));
                        u.setApellido(rs.getString("apellido"));
                        String rolStr = rs.getString("rol");
                        if (rolStr != null && !rolStr.isEmpty()) {
                            try { u.setRol(Rol.valueOf(rolStr)); } catch (IllegalArgumentException ignored) {}
                        }
                        long empId = rs.getLong("empresa_id");
                        if (!rs.wasNull()) u.setEmpresaId(empId);
                        u.setActivo(rs.getInt("activo") == 1);
                        usuarios.put(u.getId(), u);
                        return u;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return usuarios.get(id);
        }
    }

    public Usuario findByUsername(String username) {
        if (useDatabase && dbConnection != null) {
            String sql = "SELECT id, username, password, email, nombre, apellido, rol, empresa_id, activo FROM Usuario WHERE username = ? LIMIT 1";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = new Usuario();
                        u.setId(rs.getLong("id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password"));
                        u.setEmail(rs.getString("email"));
                        u.setNombre(rs.getString("nombre"));
                        u.setApellido(rs.getString("apellido"));
                        String rolStr = rs.getString("rol");
                        if (rolStr != null && !rolStr.isEmpty()) {
                            try { u.setRol(Rol.valueOf(rolStr)); } catch (IllegalArgumentException ignored) {}
                        }
                        long empId = rs.getLong("empresa_id");
                        if (!rs.wasNull()) u.setEmpresaId(empId);
                        u.setActivo(rs.getInt("activo") == 1);
                        usuarios.put(u.getId(), u);
                        return u;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return usuarios.values().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        }
    }

    public Usuario findByEmail(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> findAll() {
        if (useDatabase && dbConnection != null) {
            List<Usuario> res = new ArrayList<>();
            String sql = "SELECT id FROM Usuario";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = findById(rs.getLong("id"));
                    if (u != null) res.add(u);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        } else {
            return new ArrayList<>(usuarios.values());
        }
    }

    public List<Usuario> findByRol(Rol rol) {
        if (useDatabase && dbConnection != null) {
            List<Usuario> res = new ArrayList<>();
            String sql = "SELECT id FROM Usuario WHERE rol = ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setString(1, rol.name());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) res.add(findById(rs.getLong("id")));
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return res;
        } else {
            return usuarios.values().stream().filter(u -> u.getRol() == rol).toList();
        }
    }

    public List<Usuario> findByEmpresa(Long empresaId) {
        if (useDatabase && dbConnection != null) {
            List<Usuario> res = new ArrayList<>();
            String sql = "SELECT id FROM Usuario WHERE empresa_id = ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, empresaId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) res.add(findById(rs.getLong("id")));
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return res;
        } else {
            return usuarios.values().stream().filter(u -> u.getEmpresaId() != null && u.getEmpresaId().equals(empresaId)).toList();
        }
    }

    public boolean update(Usuario usuario) {
        if (usuario.getId() != null && usuarios.containsKey(usuario.getId())) {
            usuarios.put(usuario.getId(), usuario);
            // also persist to DB if available
            save(usuario);
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        if (useDatabase && dbConnection != null) {
            String sql = "DELETE FROM Usuario WHERE id = ?";
            try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.executeUpdate();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return usuarios.remove(id) != null;
    }

    /**
     * Autenticación: valida contra la tabla `Usuario` usando BCrypt si está configurada.
     * En caso de error o si no hay DB, usa la implementación en memoria (password en texto plano).
     */
    public boolean authenticate(String username, String password) {
        Usuario u = findByUsername(username);
        if (u == null) return false;
        if (!u.isActivo()) return false;
        String hash = u.getPassword();
        if (hash == null) return false;
        // If DB-backed, passwords should be bcrypt hashes; otherwise may be plain text
        if (hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$")) {
            return passwordEncoder.matches(password, hash);
        } else {
            return hash.equals(password);
        }
    }
}
