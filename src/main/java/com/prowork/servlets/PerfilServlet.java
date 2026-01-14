package com.prowork.servlets;

import com.prowork.dao.PerfilDAO;
import com.prowork.model.Perfil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para gestión de perfiles
 */
@WebServlet(name = "PerfilServlet", urlPatterns = {"/perfiles", "/perfiles/*"})
public class PerfilServlet extends HttpServlet {
    
    private PerfilDAO perfilDAO;

    @Override
    public void init() throws ServletException {
        perfilDAO = PerfilDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listarPerfiles(request, response);
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                obtenerPerfil(id, request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        crearPerfil(request, response);
    }

    private void listarPerfiles(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        List<Perfil> perfiles = perfilDAO.findAll();
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Perfiles - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Gestión de Perfiles</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>ID</th><th>Nombre</th><th>Descripción</th><th>Activo</th><th>Permisos</th></tr>");
            
            for (Perfil perfil : perfiles) {
                out.println("<tr>");
                out.println("<td>" + perfil.getId() + "</td>");
                out.println("<td>" + perfil.getNombre() + "</td>");
                out.println("<td>" + perfil.getDescripcion() + "</td>");
                out.println("<td>" + (perfil.isActivo() ? "Sí" : "No") + "</td>");
                out.println("<td>" + perfil.getPermisos().size() + "</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("<br><a href='" + request.getContextPath() + "'>Volver al inicio</a>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void obtenerPerfil(Long id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        Perfil perfil = perfilDAO.findById(id);
        
        if (perfil == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Perfil no encontrado");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Perfil - ProWork</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Detalle del Perfil</h1>");
            out.println("<p><strong>ID:</strong> " + perfil.getId() + "</p>");
            out.println("<p><strong>Nombre:</strong> " + perfil.getNombre() + "</p>");
            out.println("<p><strong>Descripción:</strong> " + perfil.getDescripcion() + "</p>");
            out.println("<p><strong>Activo:</strong> " + (perfil.isActivo() ? "Sí" : "No") + "</p>");
            out.println("<p><strong>Permisos:</strong></p>");
            out.println("<ul>");
            for (String permiso : perfil.getPermisos()) {
                out.println("<li>" + permiso + "</li>");
            }
            out.println("</ul>");
            out.println("<br><a href='" + request.getContextPath() + "/perfiles'>Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void crearPerfil(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        Perfil perfil = new Perfil(nombre, descripcion);
        perfilDAO.save(perfil);
        
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("Perfil creado exitosamente con ID: " + perfil.getId());
    }
}
