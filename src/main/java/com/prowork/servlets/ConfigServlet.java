package com.prowork.servlets;

import com.prowork.config.AppConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para gestión de configuración
 */
@WebServlet(name = "ConfigServlet", urlPatterns = {"/config"})
public class ConfigServlet extends HttpServlet {
    
    private AppConfig appConfig;

    @Override
    public void init() throws ServletException {
        appConfig = AppConfig.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Configuración - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Configuración del Sistema</h1>");
            out.println("<h2>Ruta de Documentos</h2>");
            out.println("<p><strong>Ruta actual:</strong> " + appConfig.getDocumentosPath() + "</p>");
            out.println("<p><strong>Tamaño máximo:</strong> " + (appConfig.getMaxFileSize() / 1024 / 1024) + " MB</p>");
            out.println("<p><strong>Extensiones permitidas:</strong></p>");
            out.println("<ul>");
            for (String ext : appConfig.getAllowedExtensions()) {
                out.println("<li>" + ext + "</li>");
            }
            out.println("</ul>");
            
            out.println("<h2>Cambiar Ruta de Documentos</h2>");
            out.println("<form method='post' action='" + request.getContextPath() + "/config'>");
            out.println("<label>Nueva ruta: <input type='text' name='newPath' value='" + appConfig.getDocumentosPath() + "' size='50'/></label>");
            out.println("<button type='submit'>Actualizar</button>");
            out.println("</form>");
            
            out.println("<br><a href='" + request.getContextPath() + "'>Volver al inicio</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String newPath = request.getParameter("newPath");
        
        if (newPath != null && !newPath.trim().isEmpty()) {
            appConfig.setDocumentosPath(newPath);
            response.sendRedirect(request.getContextPath() + "/config?success=true");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ruta inválida");
        }
    }
}
