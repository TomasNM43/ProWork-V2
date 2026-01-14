package com.prowork.servlets;

import com.prowork.config.AppConfig;
import com.prowork.dao.ConfiguracionEmpresaDAO;
import com.prowork.dao.EmpresaDAO;
import com.prowork.dao.EventoDAO;
import com.prowork.model.ConfiguracionEmpresa;
import com.prowork.model.Empresa;
import com.prowork.model.Evento;
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
 * Servlet para el dashboard del Supervisor
 */
@WebServlet(name = "DashboardSupervisorServlet", urlPatterns = {"/dashboard-supervisor"})
public class DashboardSupervisorServlet extends HttpServlet {
    
    private EmpresaDAO empresaDAO;
    private EventoDAO eventoDAO;
    private ConfiguracionEmpresaDAO configuracionDAO;
    private AppConfig appConfig;

    @Override
    public void init() throws ServletException {
        empresaDAO = EmpresaDAO.getInstance();
        eventoDAO = EventoDAO.getInstance();
        configuracionDAO = ConfiguracionEmpresaDAO.getInstance();
        appConfig = AppConfig.getInstance();
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
        ConfiguracionEmpresa configuracion = usuario.getEmpresaId() != null ? 
            configuracionDAO.findByEmpresaId(usuario.getEmpresaId()) : null;
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Dashboard Supervisor - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("<style>");
            out.println(".header { background: #17a2b8; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }");
            out.println(".container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }");
            out.println(".welcome-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; border-radius: 8px; margin-bottom: 30px; }");
            out.println(".welcome-card h1 { margin: 0 0 10px 0; font-size: 28px; }");
            out.println(".welcome-card p { margin: 5px 0; opacity: 0.9; }");
            out.println(".menu-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }");
            out.println(".menu-card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); text-align: center; transition: transform 0.3s; }");
            out.println(".menu-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.2); }");
            out.println(".menu-icon { font-size: 48px; margin-bottom: 15px; }");
            out.println(".menu-card h3 { color: #17a2b8; margin: 0 0 10px 0; }");
            out.println(".menu-card p { color: #666; margin-bottom: 20px; }");
            out.println(".btn { padding: 12px 24px; background: #17a2b8; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; font-weight: bold; }");
            out.println(".btn:hover { background: #138496; }");
            out.println(".btn-logout { background: #dc3545; }");
            out.println(".btn-logout:hover { background: #c82333; }");
            out.println(".info-box { background: #e7f3ff; padding: 15px; border-radius: 8px; margin-top: 30px; border-left: 4px solid #17a2b8; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<div class='header'>");
            out.println("<div>");
            out.println("<h2 style='margin:0'>ProWork - Panel Supervisor</h2>");
            out.println("<p style='margin:5px 0 0 0'>Sistema de Gestión de Empleados</p>");
            out.println("</div>");
            out.println("<div>");
            out.println("<a href='" + request.getContextPath() + "/logout' class='btn btn-logout'>Cerrar Sesión</a>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<div class='container'>");
            
            // Welcome card
            out.println("<div class='welcome-card'>");
            out.println("<h1>Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido() + "</h1>");
            if (empresa != null) {
                out.println("<p><strong>Empresa:</strong> " + empresa.getNombre() + "</p>");
            }
            if (configuracion != null) {
                out.println("<p><strong>Ruta de archivos:</strong> " + configuracion.getRutaArchivos() + "</p>");
                out.println("<p><strong>Permisos:</strong> ");
                if (configuracion.isPermiteGrabacionPantalla()) out.print("📹 Grabación de pantalla ");
                if (configuracion.isPermiteGrabacionAudio()) out.print("🎤 Grabación de audio");
                if (!configuracion.isPermiteGrabacionPantalla() && !configuracion.isPermiteGrabacionAudio()) {
                    out.print("Sin permisos de grabación");
                }
                out.println("</p>");
            }
            out.println("</div>");
            
            // Menu grid
            out.println("<div class='menu-grid'>");
            
            // Crear Empleado
            out.println("<div class='menu-card'>");
            out.println("<div class='menu-icon'>👥</div>");
            out.println("<h3>Crear Empleado</h3>");
            out.println("<p>Registra nuevos empleados que utilizarán la aplicación móvil/desktop</p>");
            out.println("<a href='" + request.getContextPath() + "/crear-empleado' class='btn'>Ir a Crear Empleado</a>");
            out.println("</div>");
            
            // Configuración
            out.println("<div class='menu-card'>");
            out.println("<div class='menu-icon'>⚙️</div>");
            out.println("<h3>Configuración</h3>");
            out.println("<p>Define la ruta de archivos y permisos de grabación para empleados</p>");
            out.println("<a href='" + request.getContextPath() + "/configuracion-empresa' class='btn'>Ir a Configuración</a>");
            out.println("</div>");
            
            // Visualizar Eventos
            out.println("<div class='menu-card'>");
            out.println("<div class='menu-icon'>📊</div>");
            out.println("<h3>Visualizar Eventos</h3>");
            out.println("<p>Monitorea en tiempo real las actividades de los empleados</p>");
            out.println("<a href='" + request.getContextPath() + "/visualizar-eventos' class='btn'>Ver Eventos en Vivo</a>");
            out.println("</div>");
            
            out.println("</div>");
            
            // Info box
            out.println("<div class='info-box'>");
            out.println("<strong>ℹ️ Información:</strong> ");
            out.println("Los empleados que crees <strong>NO tendrán acceso a esta plataforma web</strong>. ");
            out.println("Solo podrán usar la aplicación móvil o desktop para generar eventos. ");
            out.println("Tú podrás monitorear sus actividades en tiempo real desde la sección de Visualizar Eventos.");
            out.println("</div>");
            
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}
