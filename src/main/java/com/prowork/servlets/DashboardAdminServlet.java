package com.prowork.servlets;

import com.prowork.dao.EmpresaDAO;
import com.prowork.dao.EventoDAO;
import com.prowork.dao.UsuarioDAO;
import com.prowork.model.Empresa;
import com.prowork.model.Rol;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet para el dashboard del Administrador
 */
@WebServlet(name = "DashboardAdminServlet", urlPatterns = {"/dashboard-admin"})
public class DashboardAdminServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO;
    private EmpresaDAO empresaDAO;
    private EventoDAO eventoDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = UsuarioDAO.getInstance();
        empresaDAO = EmpresaDAO.getInstance();
        eventoDAO = EventoDAO.getInstance();
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
        if (!usuario.isAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/dashboard-supervisor");
            return;
        }
        
        List<Empresa> empresas = empresaDAO.findAll();
        List<Usuario> supervisores = usuarioDAO.findByRol(Rol.SUPERVISOR);
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Dashboard Administrador - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("<style>");
            out.println(".header { background: #343a40; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }");
            out.println(".container { padding: 30px; }");
            out.println(".cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-bottom: 30px; }");
            out.println(".card { padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
            out.println(".card h3 { margin-top: 0; color: #007bff; }");
            out.println(".btn { padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px; }");
            out.println(".btn:hover { background: #0056b3; }");
            out.println(".btn-logout { background: #dc3545; }");
            out.println(".btn-logout:hover { background: #c82333; }");
            out.println("table { width: 100%; border-collapse: collapse; background: white; color: #333; }");
            out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
            out.println("th { background: #f8f9fa; font-weight: bold; }");
            out.println(".form-group { margin-bottom: 15px; }");
            out.println(".form-group label { display: block; margin-bottom: 5px; font-weight: bold; }");
            out.println(".form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }");
            out.println(".modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); }");
            out.println(".modal-content { background: white; margin: 50px auto; padding: 30px; width: 90%; max-width: 600px; border-radius: 8px; }");
            out.println("</style>");
            out.println("<script>");
            out.println("function showModal(modalId) { document.getElementById(modalId).style.display = 'block'; }");
            out.println("function hideModal(modalId) { document.getElementById(modalId).style.display = 'none'; }");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<div class='header'>");
            out.println("<div>");
            out.println("<h2 style='margin:0'>ProWork - Panel Administrador</h2>");
            out.println("<p style='margin:5px 0 0 0'>Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido() + "</p>");
            out.println("</div>");
            out.println("<div>");
            out.println("<a href='" + request.getContextPath() + "/logout' class='btn btn-logout'>Cerrar Sesión</a>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<div class='container'>");
            
            // Tarjetas de resumen
            out.println("<div class='cards'>");
            out.println("<div class='card'>");
            out.println("<h3>Total Empresas</h3>");
            out.println("<h1 style='margin: 10px 0; color: #28a745;'>" + empresas.size() + "</h1>");
            out.println("</div>");
            out.println("<div class='card'>");
            out.println("<h3>Total Supervisores</h3>");
            out.println("<h1 style='margin: 10px 0; color: #17a2b8;'>" + supervisores.size() + "</h1>");
            out.println("</div>");
            out.println("</div>");
            
            // Sección Empresas
            out.println("<div class='card' style='margin-bottom: 20px;'>");
            out.println("<h2>Gestión de Empresas</h2>");
            out.println("<button class='btn' onclick='showModal(\"modalEmpresa\")'>+ Nueva Empresa</button>");
            out.println("<br><br>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Nombre</th><th>RUT</th><th>Dirección</th><th>Teléfono</th><th>Email</th><th>Estado</th></tr>");
            for (Empresa emp : empresas) {
                out.println("<tr>");
                out.println("<td>" + emp.getId() + "</td>");
                out.println("<td>" + emp.getNombre() + "</td>");
                out.println("<td>" + emp.getRut() + "</td>");
                out.println("<td>" + emp.getDireccion() + "</td>");
                out.println("<td>" + emp.getTelefono() + "</td>");
                out.println("<td>" + emp.getEmail() + "</td>");
                out.println("<td>" + (emp.isActiva() ? "Activa" : "Inactiva") + "</td>");
                out.println("</tr>");
            }
            if (empresas.isEmpty()) {
                out.println("<tr><td colspan='7' style='text-align:center;'>No hay empresas registradas</td></tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            // Sección Supervisores
            out.println("<div class='card'>");
            out.println("<h2>Gestión de Supervisores</h2>");
            out.println("<button class='btn' onclick='showModal(\"modalSupervisor\")'>+ Nuevo Supervisor</button>");
            out.println("<br><br>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Usuario</th><th>Nombre</th><th>Apellido</th><th>Email</th><th>Empresa</th><th>Estado</th></tr>");
            for (Usuario sup : supervisores) {
                out.println("<tr>");
                out.println("<td>" + sup.getId() + "</td>");
                out.println("<td>" + sup.getUsername() + "</td>");
                out.println("<td>" + sup.getNombre() + "</td>");
                out.println("<td>" + sup.getApellido() + "</td>");
                out.println("<td>" + sup.getEmail() + "</td>");
                String empresaNombre = sup.getEmpresaId() != null ? 
                    (empresaDAO.findById(sup.getEmpresaId()) != null ? empresaDAO.findById(sup.getEmpresaId()).getNombre() : "N/A") : "N/A";
                out.println("<td>" + empresaNombre + "</td>");
                out.println("<td>" + (sup.isActivo() ? "Activo" : "Inactivo") + "</td>");
                out.println("</tr>");
            }
            if (supervisores.isEmpty()) {
                out.println("<tr><td colspan='7' style='text-align:center;'>No hay supervisores registrados</td></tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            out.println("</div>");
            
            // Modal Empresa
            out.println("<div id='modalEmpresa' class='modal'>");
            out.println("<div class='modal-content'>");
            out.println("<h2>Nueva Empresa</h2>");
            out.println("<form method='post' action='" + request.getContextPath() + "/empresa'>");
            out.println("<div class='form-group'>");
            out.println("<label>Nombre:</label>");
            out.println("<input type='text' name='nombre' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>RUT:</label>");
            out.println("<input type='text' name='rut' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Dirección:</label>");
            out.println("<input type='text' name='direccion' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Teléfono:</label>");
            out.println("<input type='text' name='telefono' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Email:</label>");
            out.println("<input type='email' name='email' required>");
            out.println("</div>");
            out.println("<button type='submit' class='btn'>Guardar</button>");
            out.println("<button type='button' class='btn btn-logout' onclick='hideModal(\"modalEmpresa\")'>Cancelar</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            
            // Modal Supervisor
            out.println("<div id='modalSupervisor' class='modal'>");
            out.println("<div class='modal-content'>");
            out.println("<h2>Nuevo Supervisor</h2>");
            out.println("<form method='post' action='" + request.getContextPath() + "/supervisor'>");
            out.println("<div class='form-group'>");
            out.println("<label>Usuario:</label>");
            out.println("<input type='text' name='username' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Contraseña:</label>");
            out.println("<input type='password' name='password' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Nombre:</label>");
            out.println("<input type='text' name='nombre' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Apellido:</label>");
            out.println("<input type='text' name='apellido' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Email:</label>");
            out.println("<input type='email' name='email' required>");
            out.println("</div>");
            out.println("<div class='form-group'>");
            out.println("<label>Empresa:</label>");
            out.println("<select name='empresaId' required>");
            out.println("<option value=''>Seleccione una empresa</option>");
            for (Empresa emp : empresas) {
                out.println("<option value='" + emp.getId() + "'>" + emp.getNombre() + "</option>");
            }
            out.println("</select>");
            out.println("</div>");
            out.println("<button type='submit' class='btn'>Guardar</button>");
            out.println("<button type='button' class='btn btn-logout' onclick='hideModal(\"modalSupervisor\")'>Cancelar</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}
