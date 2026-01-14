package com.prowork.dao;

import com.prowork.model.Perfil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DAO para manejo de perfiles
 * Implementación en memoria (para producción usar base de datos)
 */
public class PerfilDAO {
    private static PerfilDAO instance;
    private Map<Long, Perfil> perfiles;
    private AtomicLong idGenerator;

    private PerfilDAO() {
        perfiles = new HashMap<>();
        idGenerator = new AtomicLong(1);
        initializeDefaultProfiles();
    }

    public static PerfilDAO getInstance() {
        if (instance == null) {
            synchronized (PerfilDAO.class) {
                if (instance == null) {
                    instance = new PerfilDAO();
                }
            }
        }
        return instance;
    }

    private void initializeDefaultProfiles() {
        // Perfil Administrador
        Perfil admin = new Perfil("Administrador", "Acceso total al sistema");
        admin.addPermiso("usuarios.crear");
        admin.addPermiso("usuarios.editar");
        admin.addPermiso("usuarios.eliminar");
        admin.addPermiso("usuarios.ver");
        admin.addPermiso("perfiles.crear");
        admin.addPermiso("perfiles.editar");
        admin.addPermiso("perfiles.eliminar");
        admin.addPermiso("perfiles.ver");
        admin.addPermiso("documentos.subir");
        admin.addPermiso("documentos.descargar");
        admin.addPermiso("documentos.eliminar");
        save(admin);

        // Perfil Usuario
        Perfil usuario = new Perfil("Usuario", "Acceso básico al sistema");
        usuario.addPermiso("usuarios.ver");
        usuario.addPermiso("perfiles.ver");
        usuario.addPermiso("documentos.subir");
        usuario.addPermiso("documentos.descargar");
        save(usuario);

        // Perfil Invitado
        Perfil invitado = new Perfil("Invitado", "Acceso de solo lectura");
        invitado.addPermiso("usuarios.ver");
        invitado.addPermiso("documentos.descargar");
        save(invitado);
    }

    public Perfil save(Perfil perfil) {
        if (perfil.getId() == null) {
            perfil.setId(idGenerator.getAndIncrement());
        }
        perfiles.put(perfil.getId(), perfil);
        return perfil;
    }

    public Perfil findById(Long id) {
        return perfiles.get(id);
    }

    public Perfil findByNombre(String nombre) {
        return perfiles.values().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public List<Perfil> findAll() {
        return new ArrayList<>(perfiles.values());
    }

    public List<Perfil> findActivos() {
        return perfiles.values().stream()
                .filter(Perfil::isActivo)
                .toList();
    }

    public boolean update(Perfil perfil) {
        if (perfil.getId() != null && perfiles.containsKey(perfil.getId())) {
            perfiles.put(perfil.getId(), perfil);
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        return perfiles.remove(id) != null;
    }
}
