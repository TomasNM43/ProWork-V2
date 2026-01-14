package com.prowork.servlets;

import com.prowork.dao.EventoDAO;
import com.prowork.dao.UsuarioDAO;
import com.prowork.model.Rol;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para creación de empleados (usuarios sin acceso web)
 */
@WebServlet(name = "EmpleadoServlet", urlPatterns = {"/empleado"})
public class EmpleadoServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    private EventoDAO eventoDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = UsuarioDAO.getInstance();
        eventoDAO = EventoDAO.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        if (!usuarioActual.isSupervisor()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        
        // Crear empleado (sin acceso web) asignado a la empresa del supervisor
        Usuario empleado = new Usuario();
        empleado.setUsername(username);
        empleado.setPassword(password);
        empleado.setNombre(nombre);
        empleado.setApellido(""); // Nombre completo en un solo campo
        empleado.setEmail(email != null ? email : "");
        empleado.setRol(Rol.EMPLEADO);
        empleado.setEmpresaId(usuarioActual.getEmpresaId());
        empleado.setActivo(true);
        
        try {
            usuarioDAO.save(empleado);
            
            // Registrar evento
            eventoDAO.registrarEvento("EMPLEADO_CREADO", 
                "Nuevo empleado registrado: " + username, 
                usuarioActual.getUsername(), 
                usuarioActual.getEmpresaId());
            
            response.sendRedirect(request.getContextPath() + "/crear-empleado?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/crear-empleado?error=1");
        }
    }
}
