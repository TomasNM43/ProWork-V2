package com.prowork.servlets;

import com.prowork.config.AppConfig;
import com.prowork.dao.EventoDAO;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet para configuración de ruta de documentos por supervisor
 */
@WebServlet(name = "ConfigRutaServlet", urlPatterns = {"/config-ruta"})
public class ConfigRutaServlet extends HttpServlet {
    
    private AppConfig appConfig;
    private EventoDAO eventoDAO;

    @Override
    public void init() throws ServletException {
        appConfig = AppConfig.getInstance();
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
        if (!usuario.isSupervisor()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String rutaDocumentos = request.getParameter("rutaDocumentos");
        
        if (rutaDocumentos != null && !rutaDocumentos.trim().isEmpty()) {
            String rutaAnterior = appConfig.getDocumentosPath();
            appConfig.setDocumentosPath(rutaDocumentos);
            
            // Registrar evento
            eventoDAO.registrarEvento("CONFIG_RUTA", 
                "Ruta de documentos actualizada de '" + rutaAnterior + "' a '" + rutaDocumentos + "'", 
                usuario.getUsername(), usuario.getEmpresaId());
        }
        
        response.sendRedirect(request.getContextPath() + "/dashboard-supervisor");
    }
}
