package com.prowork.servlets;

import com.prowork.model.Empresa;
import com.prowork.model.Usuario;
import com.prowork.dao.EmpresaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para la pantalla de creación de empleados
 */
@WebServlet(name = "CrearEmpleadoServlet", urlPatterns = {"/crear-empleado"})
public class CrearEmpleadoServlet extends HttpServlet {
    
    private EmpresaDAO empresaDAO;

    @Override
    public void init() throws ServletException {
        empresaDAO = EmpresaDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (!usuario.isSupervisor()) {
            response.sendRedirect(request.getContextPath() + "/dashboard-admin");
            return;
        }
        
        Empresa empresa = usuario.getEmpresaId() != null ? empresaDAO.findById(usuario.getEmpresaId()) : null;
        String mensaje = request.getParameter("success") != null ? "Empleado creado exitosamente" : null;
        String error = request.getParameter("error") != null ? "Error al crear empleado" : null;
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Crear Empleado - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("<style>");
            out.println(".header { background: #17a2b8; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }");
            out.println(".container { max-width: 800px; margin: 30px auto; padding: 0 20px; }");
            out.println(".card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
            out.println(".btn { padding: 10px 20px; background: #17a2b8; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px; }");
            out.println(".btn:hover { background: #138496; }");
            out.println(".btn-secondary { background: #6c757d; }");
            out.println(".btn-secondary:hover { background: #5a6268; }");
            out.println(".btn-logout { background: #dc3545; }");
            out.println(".btn-logout:hover { background: #c82333; }");
            out.println(".form-group { margin-bottom: 20px; }");
            out.println(".form-group label { display: block; margin-bottom: 5px; font-weight: bold; color: #333; }");
            out.println(".form-group input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; font-size: 14px; }");
            out.println(".form-group small { color: #666; font-size: 12px; }");
            out.println(".success { background: #d4edda; color: #155724; padding: 12px; border-radius: 4px; margin-bottom: 20px; }");
            out.println(".error { background: #f8d7da; color: #721c24; padding: 12px; border-radius: 4px; margin-bottom: 20px; }");
            out.println(".info-box { background: #fff3cd; padding: 15px; border-radius: 4px; margin-bottom: 20px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<div class='header'>");
            out.println("<div>");
            out.println("<h2 style='margin:0'>ProWork - Crear Empleado</h2>");
            out.println("<p style='margin:5px 0 0 0'>Supervisor: " + usuario.getNombre() + " " + usuario.getApellido());
            if (empresa != null) {
                out.println(" - " + empresa.getNombre());
            }
            out.println("</p>");
            out.println("</div>");
            out.println("<div>");
            out.println("<a href='" + request.getContextPath() + "/logout' class='btn btn-logout'>Cerrar Sesión</a>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<div class='container'>");
            
            // Botón volver
            out.println("<div style='margin-bottom: 20px;'>");
            out.println("<a href='" + request.getContextPath() + "/dashboard-supervisor' class='btn btn-secondary'>← Volver al Dashboard</a>");
            out.println("</div>");
            
            out.println("<div class='card'>");
            out.println("<h2>Crear Nuevo Empleado</h2>");
            
            // Mensajes
            if (mensaje != null) {
                out.println("<div class='success'>" + mensaje + "</div>");
            }
            if (error != null) {
                out.println("<div class='error'>" + error + "</div>");
            }
            
            out.println("<div class='info-box'>");
            out.println("<strong>ℹ️ Importante:</strong> Los empleados pueden generar eventos y usar la aplicación móvil/desktop, ");
            out.println("pero <strong>NO tienen acceso a esta plataforma web</strong>.");
            out.println("</div>");
            
            out.println("<form method='post' action='" + request.getContextPath() + "/empleado'>");
            
            out.println("<div class='form-group'>");
            out.println("<label>Usuario: *</label>");
            out.println("<input type='text' name='username' required placeholder='Ejemplo: jperez'>");
            out.println("<small>Nombre de usuario único para el empleado</small>");
            out.println("</div>");
            
            out.println("<div class='form-group'>");
            out.println("<label>Contraseña: *</label>");
            out.println("<input type='password' name='password' required placeholder='Mínimo 6 caracteres'>");
            out.println("<small>La contraseña será encriptada automáticamente</small>");
            out.println("</div>");
            
            out.println("<div class='form-group'>");
            out.println("<label>Nombre Completo: *</label>");
            out.println("<input type='text' name='nombre' required placeholder='Ejemplo: Juan Pérez'>");
            out.println("</div>");
            
            out.println("<div class='form-group'>");
            out.println("<label>Email:</label>");
            out.println("<input type='email' name='email' placeholder='ejemplo@empresa.com'>");
            out.println("<small>Opcional - Para notificaciones futuras</small>");
            out.println("</div>");
            
            out.println("<div style='margin-top: 30px;'>");
            out.println("<button type='submit' class='btn'>Crear Empleado</button>");
            out.println("<a href='" + request.getContextPath() + "/dashboard-supervisor' class='btn btn-secondary'>Cancelar</a>");
            out.println("</div>");
            
            out.println("</form>");
            out.println("</div>");
            
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}
