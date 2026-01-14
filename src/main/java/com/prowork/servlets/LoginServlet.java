package com.prowork.servlets;

import com.prowork.dao.EventoDAO;
import com.prowork.dao.UsuarioDAO;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para autenticación de usuarios
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    private EventoDAO eventoDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = UsuarioDAO.getInstance();
        eventoDAO = EventoDAO.getInstance();
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
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Login - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("<style>");
            out.println(".login-container { max-width: 400px; margin: 100px auto; padding: 30px; background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println(".form-group { margin-bottom: 20px; }");
            out.println(".form-group label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println(".form-group input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
            out.println(".btn-login { width: 100%; padding: 12px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }");
            out.println(".btn-login:hover { background: #0056b3; }");
            out.println(".error { color: red; margin-bottom: 15px; }");
            out.println(".info { background: #e3f2fd; padding: 15px; border-radius: 4px; margin-bottom: 20px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='login-container'>");
            out.println("<h1 style='text-align: center;'>ProWork</h1>");
            out.println("<h2 style='text-align: center; color: #666;'>Iniciar Sesión</h2>");
            
            String error = request.getParameter("error");
            if (error != null) {
                if (error.equals("2")) {
                    out.println("<div class='error'>Acceso denegado. Los empleados no tienen acceso a esta plataforma.</div>");
                } else {
                    out.println("<div class='error'>Usuario o contraseña incorrectos</div>");
                }
            }
            
            String logout = request.getParameter("logout");
            if (logout != null) {
                out.println("<div class='info'>Has cerrado sesión correctamente</div>");
            }
            
            out.println("<div class='info'>");
            out.println("<strong>Usuario por defecto:</strong><br>");
            out.println("Usuario: admin<br>");
            out.println("Contraseña: admin123");
            out.println("</div>");
            
            out.println("<form method='post' action='" + request.getContextPath() + "/login'>");
            out.println("<div class='form-group'>");
            out.println("<label>Usuario:</label>");
            out.println("<input type='text' name='username' required autofocus>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Contraseña:</label>");
            out.println("<input type='password' name='password' required>");
            out.println("</div>");
            out.println("<button type='submit' class='btn-login'>Ingresar</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (usuarioDAO.authenticate(username, password)) {
            Usuario usuario = usuarioDAO.findByUsername(username);
            
            // Bloquear acceso a empleados - solo pueden usar app externa, no la web
            if (usuario.getRol() == com.prowork.model.Rol.EMPLEADO) {
                response.sendRedirect(request.getContextPath() + "/login?error=2");
                return;
            }
            
            // Crear sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("username", usuario.getUsername());
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("empresaId", usuario.getEmpresaId());
            
            // Registrar evento
            eventoDAO.registrarEvento("LOGIN", "Usuario " + username + " inició sesión", username, usuario.getEmpresaId());
            
            // Redirigir según el rol
            if (usuario.isAdministrador()) {
                response.sendRedirect(request.getContextPath() + "/dashboard-admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard-supervisor");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error=1");
        }
    }
}
