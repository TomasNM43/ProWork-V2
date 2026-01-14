package com.prowork.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet de ejemplo para ProWork
 */
@WebServlet(name = "HelloServlet", urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>ProWork - Servlet</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<header>");
            out.println("<h1>ProWork - Servlet de Ejemplo</h1>");
            out.println("</header>");
            out.println("<main>");
            out.println("<div class='hero'>");
            out.println("<h2>¡Servlet funcionando correctamente!</h2>");
            out.println("<p>Este es un ejemplo de Servlet en tu aplicación ProWork</p>");
            out.println("</div>");
            out.println("<div class='actions'>");
            out.println("<div class='card'>");
            out.println("<h3>Información</h3>");
            out.println("<p>Método: " + request.getMethod() + "</p>");
            out.println("<p>URI: " + request.getRequestURI() + "</p>");
            out.println("<p>Contexto: " + request.getContextPath() + "</p>");
            out.println("<a href='" + request.getContextPath() + "/' class='btn'>Volver al Inicio</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</main>");
            out.println("<footer>");
            out.println("<p>&copy; 2025 ProWork - Servlet Example</p>");
            out.println("</footer>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet de ejemplo para ProWork";
    }
}
