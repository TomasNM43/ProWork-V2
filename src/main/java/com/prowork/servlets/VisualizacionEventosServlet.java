package com.prowork.servlets;

import com.prowork.dao.EmpresaDAO;
import com.prowork.dao.EventoDAO;
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
 * Servlet para visualización de eventos en tiempo real con auto-refresh
 */
@WebServlet(name = "VisualizacionEventosServlet", urlPatterns = {"/visualizar-eventos"})
public class VisualizacionEventosServlet extends HttpServlet {
    
    private EventoDAO eventoDAO;
    private EmpresaDAO empresaDAO;

    @Override
    public void init() throws ServletException {
        eventoDAO = EventoDAO.getInstance();
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
        
        // Filtrar eventos solo de la empresa del supervisor
        List<Evento> eventos = usuario.getEmpresaId() != null ? 
            eventoDAO.findByEmpresa(usuario.getEmpresaId()) : 
            new java.util.ArrayList<>();
        
        Empresa empresa = usuario.getEmpresaId() != null ? empresaDAO.findById(usuario.getEmpresaId()) : null;
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Eventos en Tiempo Real - ProWork</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/css/style.css'>");
            out.println("<style>");
            out.println(".header { background: #17a2b8; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }");
            out.println(".container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }");
            out.println(".card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
            out.println(".btn { padding: 10px 20px; background: #17a2b8; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; margin-right: 10px; }");
            out.println(".btn:hover { background: #138496; }");
            out.println(".btn-secondary { background: #6c757d; }");
            out.println(".btn-secondary:hover { background: #5a6268; }");
            out.println(".btn-logout { background: #dc3545; }");
            out.println(".btn-logout:hover { background: #c82333; }");
            out.println(".auto-refresh { background: #fff3cd; padding: 15px; border-radius: 4px; margin-bottom: 20px; text-align: center; font-weight: bold; }");
            out.println(".eventos { max-height: 600px; overflow-y: auto; }");
            out.println(".evento { padding: 15px; border-left: 4px solid #17a2b8; margin-bottom: 15px; background: #f8f9fa; border-radius: 4px; }");
            out.println(".evento:hover { background: #e9ecef; }");
            out.println(".evento-tipo { font-weight: bold; color: #17a2b8; font-size: 16px; margin-bottom: 5px; }");
            out.println(".evento-descripcion { color: #333; margin-bottom: 8px; }");
            out.println(".evento-time { font-size: 13px; color: #666; }");
            out.println(".no-eventos { text-align: center; padding: 40px; color: #666; }");
            out.println(".stats { display: flex; gap: 20px; margin-bottom: 20px; }");
            out.println(".stat-card { flex: 1; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px; text-align: center; }");
            out.println(".stat-number { font-size: 32px; font-weight: bold; }");
            out.println(".stat-label { font-size: 14px; opacity: 0.9; }");
            out.println(".media-buttons { margin-top: 10px; }");
            out.println(".btn-media { padding: 8px 16px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; font-size: 14px; }");
            out.println(".btn-media:hover { background: #218838; }");
            out.println(".btn-media.audio { background: #17a2b8; }");
            out.println(".btn-media.audio:hover { background: #138496; }");
            out.println(".ruta-archivo { font-size: 11px; color: #666; margin-top: 5px; word-break: break-all; background: #f0f0f0; padding: 5px; border-radius: 3px; }");
            out.println(".filter-container { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 20px; }");
            out.println(".filter-row { display: flex; align-items: center; gap: 15px; margin-bottom: 15px; flex-wrap: wrap; }");
            out.println(".filter-group { display: flex; align-items: center; gap: 10px; }");
            out.println(".filter-label { font-weight: bold; color: #333; }");
            out.println(".filter-select { padding: 8px 15px; border: 2px solid #17a2b8; border-radius: 4px; font-size: 14px; min-width: 200px; }");
            out.println(".filter-select:focus { outline: none; border-color: #138496; }");
            out.println(".filter-date { padding: 8px 15px; border: 2px solid #17a2b8; border-radius: 4px; font-size: 14px; }");
            out.println(".filter-date:focus { outline: none; border-color: #138496; }");
            out.println(".btn-clear-filter { padding: 8px 15px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer; }");
            out.println(".btn-clear-filter:hover { background: #5a6268; }");
            out.println(".btn-apply-filter { padding: 8px 15px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; }");
            out.println(".btn-apply-filter:hover { background: #218838; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<div class='header'>");
            out.println("<div>");
            out.println("<h2 style='margin:0'>ProWork - Eventos en Tiempo Real</h2>");
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
            
            // Auto-refresh notice
            out.println("<div class='auto-refresh'>");
            out.println("⚡ Actualización automática de eventos cada 5 segundos <span id='lastUpdate'></span>");
            out.println("</div>");
            
            // Filtro por usuario y fechas
            out.println("<div class='filter-container'>");
            out.println("<h3 style='margin-top: 0; margin-bottom: 15px; color: #333;'>🔍 Filtros de Búsqueda</h3>");
            
            // Primera fila: Filtro de usuario
            out.println("<div class='filter-row'>");
            out.println("<div class='filter-group'>");
            out.println("<span class='filter-label'>👤 Usuario:</span>");
            out.println("<select id='userFilter' class='filter-select'>");
            out.println("<option value=''>Todos los usuarios</option>");
            
            // Obtener lista única de usuarios
            java.util.Set<String> usuarios = new java.util.LinkedHashSet<>();
            for (Evento e : eventos) {
                if (e.getUsuario() != null && !e.getUsuario().isEmpty()) {
                    usuarios.add(e.getUsuario());
                }
            }
            for (String user : usuarios) {
                out.println("<option value='" + user + "'>" + user + "</option>");
            }
            
            out.println("</select>");
            out.println("</div>");
            out.println("</div>");
            
            // Segunda fila: Filtros de fecha
            out.println("<div class='filter-row'>");
            out.println("<div class='filter-group'>");
            out.println("<span class='filter-label'>📅 Fecha Inicio:</span>");
            out.println("<input type='date' id='fechaInicio' class='filter-date'>");
            out.println("</div>");
            out.println("<div class='filter-group'>");
            out.println("<span class='filter-label'>📅 Fecha Fin:</span>");
            out.println("<input type='date' id='fechaFin' class='filter-date'>");
            out.println("</div>");
            out.println("</div>");
            
            // Tercera fila: Botones
            out.println("<div class='filter-row'>");
            out.println("<button class='btn-apply-filter' onclick='applyAllFilters()'>Aplicar Filtros</button>");
            out.println("<button class='btn-clear-filter' onclick='clearAllFilters()'>Limpiar Todos</button>");
            out.println("</div>");
            
            out.println("</div>");
            
            // Estadísticas
            out.println("<div class='stats'>");
            out.println("<div class='stat-card'>");
            out.println("<div class='stat-number'>" + eventos.size() + "</div>");
            out.println("<div class='stat-label'>Total de Eventos</div>");
            out.println("</div>");
            out.println("<div class='stat-card' style='background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);'>");
            out.println("<div class='stat-number'>" + (empresa != null ? empresa.getNombre() : "N/A") + "</div>");
            out.println("<div class='stat-label'>Empresa</div>");
            out.println("</div>");
            out.println("</div>");
            
            // Lista de eventos
            out.println("<div class='card'>");
            out.println("<h2>📊 Eventos de Empleados</h2>");
            out.println("<p style='color: #666; margin-bottom: 20px;'>Monitoreo en tiempo real de las actividades de los empleados</p>");
            
            out.println("<div class='eventos'>");
            if (eventos.isEmpty()) {
                out.println("<div class='no-eventos'>");
                out.println("<h3>📭 No hay eventos registrados</h3>");
                out.println("<p>Los eventos generados por los empleados aparecerán aquí automáticamente</p>");
                out.println("</div>");
            } else {
                for (Evento evento : eventos) {
                    String fechaEvento = evento.getFechaCreacion() != null ? evento.getFechaCreacion().toString().substring(0, 10) : "";
                    out.println("<div class='evento' data-usuario='" + evento.getUsuario() + "' data-fecha='" + fechaEvento + "'>");
                    out.println("<div class='evento-tipo'>📌 " + evento.getTipo() + "</div>");
                    out.println("<div class='evento-descripcion'>" + evento.getDescripcion() + "</div>");
                    out.println("<div class='evento-time'>");
                    out.println("👤 Usuario: <strong>" + evento.getUsuario() + "</strong> | ");
                    out.println("🕒 Fecha: " + evento.getFechaCreacion());
                    out.println("</div>");
                    
                    // Mostrar botón para reproducir audio o video si corresponde
                    if (evento.getRutaVisualizacion() != null && !evento.getRutaVisualizacion().isEmpty()) {
                        out.println("<div class='media-buttons'>");
                        
                        if ("GUARDADO_AUDIO".equals(evento.getTipo())) {
                            out.println("<button class='btn-media audio' onclick=\"window.open('" + request.getContextPath() + "/reproducir-media?ruta=" + 
                                java.net.URLEncoder.encode(evento.getRutaVisualizacion(), "UTF-8") + "&tipo=audio', 'audio_" + evento.getId() + 
                                "', 'width=500,height=200,resizable=yes,scrollbars=no')\">🔊 Escuchar Audio</button>");
                            out.println("<div class='ruta-archivo'>📁 Ruta: " + evento.getRutaVisualizacion() + "</div>");
                        } else if ("GUARDADO_VIDEO".equals(evento.getTipo())) {
                            out.println("<button class='btn-media' onclick=\"window.open('" + request.getContextPath() + "/reproducir-media?ruta=" + 
                                java.net.URLEncoder.encode(evento.getRutaVisualizacion(), "UTF-8") + "&tipo=video', 'video_" + evento.getId() + 
                                "', 'width=800,height=600,resizable=yes,scrollbars=no')\">🎥 Ver Video</button>");
                            out.println("<div class='ruta-archivo'>📁 Ruta: " + evento.getRutaVisualizacion() + "</div>");
                        }
                        
                        out.println("</div>");
                    }
                    
                    out.println("</div>");
                }
            }
            out.println("</div>");
            
            out.println("</div>");
            
            out.println("</div>");
            
            // JavaScript para los filtros y actualización AJAX
            out.println("<script>");
            out.println("var autoRefreshInterval;");
            out.println("");
            out.println("// Recuperar los filtros guardados al cargar la página");
            out.println("window.addEventListener('load', function() {");
            out.println("  var savedUserFilter = localStorage.getItem('eventoUsuarioFilter');");
            out.println("  var savedFechaInicio = localStorage.getItem('eventoFechaInicio');");
            out.println("  var savedFechaFin = localStorage.getItem('eventoFechaFin');");
            out.println("  ");
            out.println("  if (savedUserFilter) {");
            out.println("    document.getElementById('userFilter').value = savedUserFilter;");
            out.println("  }");
            out.println("  if (savedFechaInicio) {");
            out.println("    document.getElementById('fechaInicio').value = savedFechaInicio;");
            out.println("  }");
            out.println("  if (savedFechaFin) {");
            out.println("    document.getElementById('fechaFin').value = savedFechaFin;");
            out.println("  }");
            out.println("  ");
            out.println("  // Aplicar filtros si existen");
            out.println("  if (savedUserFilter || savedFechaInicio || savedFechaFin) {");
            out.println("    applyAllFilters();");
            out.println("  }");
            out.println("  ");
            out.println("  // Iniciar actualización automática");
            out.println("  startAutoRefresh();");
            out.println("  updateLastUpdateTime();");
            out.println("});");
            out.println("");
            out.println("// Aplicar filtro cuando cambia la selección de usuario");
            out.println("document.getElementById('userFilter').addEventListener('change', function() {");
            out.println("  applyAllFilters();");
            out.println("});");
            out.println("");
            out.println("// Aplicar filtro cuando cambian las fechas");
            out.println("document.getElementById('fechaInicio').addEventListener('change', function() {");
            out.println("  applyAllFilters();");
            out.println("});");
            out.println("");
            out.println("document.getElementById('fechaFin').addEventListener('change', function() {");
            out.println("  applyAllFilters();");
            out.println("});");
            out.println("");
            out.println("// Función para iniciar la actualización automática");
            out.println("function startAutoRefresh() {");
            out.println("  // Actualizar cada 5 segundos");
            out.println("  autoRefreshInterval = setInterval(function() {");
            out.println("    refreshEventos();");
            out.println("  }, 5000);");
            out.println("}");
            out.println("");
            out.println("// Función para actualizar los eventos mediante AJAX");
            out.println("function refreshEventos() {");
            out.println("  fetch(window.location.href + '?ajax=true')");
            out.println("    .then(response => response.text())");
            out.println("    .then(html => {");
            out.println("      // Crear un elemento temporal para parsear el HTML");
            out.println("      var tempDiv = document.createElement('div');");
            out.println("      tempDiv.innerHTML = html;");
            out.println("      ");
            out.println("      // Extraer el nuevo contenido de eventos");
            out.println("      var newEventos = tempDiv.querySelector('.eventos');");
            out.println("      var newUserOptions = tempDiv.querySelectorAll('#userFilter option');");
            out.println("      var newTotalEventos = tempDiv.querySelector('.stat-number');");
            out.println("      ");
            out.println("      if (newEventos) {");
            out.println("        // Actualizar el contenedor de eventos");
            out.println("        document.querySelector('.eventos').innerHTML = newEventos.innerHTML;");
            out.println("        ");
            out.println("        // Actualizar opciones de usuario si hay nuevas");
            out.println("        var currentUserFilter = document.getElementById('userFilter');");
            out.println("        var savedUserValue = currentUserFilter.value;");
            out.println("        currentUserFilter.innerHTML = '';");
            out.println("        newUserOptions.forEach(function(option) {");
            out.println("          currentUserFilter.appendChild(option.cloneNode(true));");
            out.println("        });");
            out.println("        currentUserFilter.value = savedUserValue;");
            out.println("        ");
            out.println("        // Aplicar filtros actuales");
            out.println("        applyAllFilters();");
            out.println("        ");
            out.println("        // Actualizar hora de última actualización");
            out.println("        updateLastUpdateTime();");
            out.println("      }");
            out.println("    })");
            out.println("    .catch(error => {");
            out.println("      console.error('Error al actualizar eventos:', error);");
            out.println("    });");
            out.println("}");
            out.println("");
            out.println("// Función para actualizar la hora de última actualización");
            out.println("function updateLastUpdateTime() {");
            out.println("  var now = new Date();");
            out.println("  var timeString = now.toLocaleTimeString('es-ES');");
            out.println("  var lastUpdateSpan = document.getElementById('lastUpdate');");
            out.println("  if (lastUpdateSpan) {");
            out.println("    lastUpdateSpan.textContent = '(Última actualización: ' + timeString + ')';");
            out.println("  }");
            out.println("}");
            out.println("");
            out.println("// Función para aplicar todos los filtros");
            out.println("function applyAllFilters() {");
            out.println("  var selectedUser = document.getElementById('userFilter').value;");
            out.println("  var fechaInicio = document.getElementById('fechaInicio').value;");
            out.println("  var fechaFin = document.getElementById('fechaFin').value;");
            out.println("  ");
            out.println("  // Guardar en localStorage");
            out.println("  localStorage.setItem('eventoUsuarioFilter', selectedUser);");
            out.println("  localStorage.setItem('eventoFechaInicio', fechaInicio);");
            out.println("  localStorage.setItem('eventoFechaFin', fechaFin);");
            out.println("  ");
            out.println("  var eventos = document.querySelectorAll('.evento');");
            out.println("  var count = 0;");
            out.println("  ");
            out.println("  eventos.forEach(function(evento) {");
            out.println("    var eventoUsuario = evento.getAttribute('data-usuario');");
            out.println("    var eventoFecha = evento.getAttribute('data-fecha');");
            out.println("    ");
            out.println("    var matchUsuario = selectedUser === '' || eventoUsuario === selectedUser;");
            out.println("    var matchFechaInicio = fechaInicio === '' || eventoFecha >= fechaInicio;");
            out.println("    var matchFechaFin = fechaFin === '' || eventoFecha <= fechaFin;");
            out.println("    ");
            out.println("    if (matchUsuario && matchFechaInicio && matchFechaFin) {");
            out.println("      evento.style.display = 'block';");
            out.println("      count++;");
            out.println("    } else {");
            out.println("      evento.style.display = 'none';");
            out.println("    }");
            out.println("  });");
            out.println("  ");
            out.println("  // Actualizar contador");
            out.println("  updateEventCount(count);");
            out.println("}");
            out.println("");
            out.println("// Función para limpiar todos los filtros");
            out.println("function clearAllFilters() {");
            out.println("  document.getElementById('userFilter').value = '';");
            out.println("  document.getElementById('fechaInicio').value = '';");
            out.println("  document.getElementById('fechaFin').value = '';");
            out.println("  ");
            out.println("  localStorage.removeItem('eventoUsuarioFilter');");
            out.println("  localStorage.removeItem('eventoFechaInicio');");
            out.println("  localStorage.removeItem('eventoFechaFin');");
            out.println("  ");
            out.println("  applyAllFilters();");
            out.println("}");
            out.println("");
            out.println("// Función para actualizar el contador de eventos");
            out.println("function updateEventCount(count) {");
            out.println("  var statNumber = document.querySelector('.stat-number');");
            out.println("  if (statNumber) {");
            out.println("    statNumber.textContent = count;");
            out.println("  }");
            out.println("}");
            out.println("</script>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
}
