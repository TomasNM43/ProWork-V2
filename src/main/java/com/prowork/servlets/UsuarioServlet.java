package com.prowork.servlets;

import com.prowork.dao.UsuarioDAO;
import com.prowork.model.Usuario;
import com.prowork.model.Rol;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para gestión de usuarios
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios", "/usuarios/*"})
public class UsuarioServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = UsuarioDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listarUsuarios(request, response);
        } else {
            // Obtener usuario por ID
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                obtenerUsuario(id, request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        crearUsuario(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        actualizarUsuario(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                eliminarUsuario(id, request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID requerido");
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        List<Usuario> usuarios = usuarioDAO.findAll();
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Usuarios - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Gestión de Usuarios</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Username</th><th>Email</th><th>Nombre</th><th>Apellido</th><th>Activo</th><th>Rol</th></tr>");
            
            for (Usuario usuario : usuarios) {
                out.println("<tr>");
                out.println("<td>" + usuario.getId() + "</td>");
                out.println("<td>" + usuario.getUsername() + "</td>");
                out.println("<td>" + usuario.getEmail() + "</td>");
                out.println("<td>" + usuario.getNombre() + "</td>");
                out.println("<td>" + usuario.getApellido() + "</td>");
                out.println("<td>" + (usuario.isActivo() ? "Sí" : "No") + "</td>");
                out.println("<td>" + (usuario.getRol() != null ? usuario.getRol().getNombre() : "N/A") + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("<br><a href='" + request.getContextPath() + "'>Volver al inicio</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void obtenerUsuario(Long id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        Usuario usuario = usuarioDAO.findById(id);
        
        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Usuario - ProWork</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Detalle del Usuario</h1>");
            out.println("<p><strong>ID:</strong> " + usuario.getId() + "</p>");
            out.println("<p><strong>Username:</strong> " + usuario.getUsername() + "</p>");
            out.println("<p><strong>Email:</strong> " + usuario.getEmail() + "</p>");
            out.println("<p><strong>Nombre:</strong> " + usuario.getNombre() + "</p>");
            out.println("<p><strong>Apellido:</strong> " + usuario.getApellido() + "</p>");
            out.println("<p><strong>Activo:</strong> " + (usuario.isActivo() ? "Sí" : "No") + "</p>");
            out.println("<br><a href='" + request.getContextPath() + "/usuarios'>Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String rol = request.getParameter("rol");
        
        Rol rolEnum = Rol.SUPERVISOR; // Por defecto
        if (rol != null && !rol.isEmpty()) {
            try {
                rolEnum = Rol.valueOf(rol.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Mantener el valor por defecto
            }
        }
        
        Usuario usuario = new Usuario(username, password, email, nombre, apellido, rolEnum);
        
        usuarioDAO.save(usuario);
        
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Usuario creado exitosamente con ID: " + usuario.getId());
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Funcionalidad de actualización de usuario");
    }

    private void eliminarUsuario(Long id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        boolean eliminado = usuarioDAO.delete(id);
        
        response.setContentType("text/plain;charset=UTF-8");
        if (eliminado) {
            response.getWriter().write("Usuario eliminado exitosamente");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Usuario no encontrado");
        }
    }
}
