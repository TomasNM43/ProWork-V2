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
 * Servlet para gestión de supervisores
 */
@WebServlet(name = "SupervisorServlet", urlPatterns = {"/supervisor"})
public class SupervisorServlet extends HttpServlet {
    
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
        if (!usuarioActual.isAdministrador()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String empresaIdStr = request.getParameter("empresaId");
        
        Usuario supervisor = new Usuario(username, password, email, nombre, apellido, Rol.SUPERVISOR);
        if (empresaIdStr != null && !empresaIdStr.isEmpty()) {
            supervisor.setEmpresaId(Long.parseLong(empresaIdStr));
        }
        
        usuarioDAO.save(supervisor);
        
        // Registrar evento
        eventoDAO.registrarEvento("SUPERVISOR_CREADO", "Nuevo supervisor registrado: " + username, 
            usuarioActual.getUsername(), supervisor.getEmpresaId());
        
        response.sendRedirect(request.getContextPath() + "/dashboard-admin");
    }
}
