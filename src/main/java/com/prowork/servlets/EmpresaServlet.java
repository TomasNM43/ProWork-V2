package com.prowork.servlets;

import com.prowork.dao.EmpresaDAO;
import com.prowork.dao.EventoDAO;
import com.prowork.model.Empresa;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para gestión de empresas
 */
@WebServlet(name = "EmpresaServlet", urlPatterns = {"/empresa"})
public class EmpresaServlet extends HttpServlet {
    
    private EmpresaDAO empresaDAO;
    private EventoDAO eventoDAO;

    @Override
    public void init() throws ServletException {
        empresaDAO = EmpresaDAO.getInstance();
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
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (!usuario.isAdministrador()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String nombre = request.getParameter("nombre");
        String rut = request.getParameter("rut");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        
        Empresa empresa = new Empresa(nombre, rut, direccion, telefono, email);
        empresaDAO.save(empresa);
        
        // Registrar evento
        eventoDAO.registrarEvento("EMPRESA_CREADA", "Nueva empresa registrada: " + nombre, 
            usuario.getUsername(), null);
        
        response.sendRedirect(request.getContextPath() + "/dashboard-admin");
    }
}
