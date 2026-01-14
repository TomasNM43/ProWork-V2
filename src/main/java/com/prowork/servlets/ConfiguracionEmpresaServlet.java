package com.prowork.servlets;

import com.prowork.config.AppConfig;
import com.prowork.dao.ConfiguracionEmpresaDAO;
import com.prowork.dao.EmpresaDAO;
import com.prowork.dao.EventoDAO;
import com.prowork.model.ConfiguracionEmpresa;
import com.prowork.model.Empresa;
import com.prowork.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para configuración de empresa (ruta de archivos y permisos)
 */
@WebServlet(name = "ConfiguracionEmpresaServlet", urlPatterns = {"/configuracion-empresa"})
public class ConfiguracionEmpresaServlet extends HttpServlet {
    
    private ConfiguracionEmpresaDAO configuracionDAO;
    private EventoDAO eventoDAO;
    private EmpresaDAO empresaDAO;
    private AppConfig appConfig;

    @Override
    public void init() throws ServletException {
        configuracionDAO = ConfiguracionEmpresaDAO.getInstance();
        eventoDAO = EventoDAO.getInstance();
        empresaDAO = EmpresaDAO.getInstance();
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
        
        String mensaje = request.getParameter("success") != null ? "Configuración guardada exitosamente" : null;
        String errorParam = request.getParameter("error");
        String error = null;
        if (errorParam != null) {
            // Si el error tiene valor personalizado, usarlo; si no, usar mensaje genérico
            error = errorParam.equals("true") ? "Error al guardar configuración" : errorParam;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Configuración de Empresa - ProWork</title>");
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
            out.println(".form-group label { display: block; margin-bottom: 8px; font-weight: bold; color: #333; }");
            out.println(".form-group input[type='text'] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; font-size: 14px; }");
            out.println(".form-group input[type='checkbox'] { margin-right: 8px; }");
            out.println(".form-group small { color: #666; font-size: 12px; display: block; margin-top: 5px; }");
            out.println(".checkbox-label { font-weight: normal; cursor: pointer; }");
            out.println(".success { background: #d4edda; color: #155724; padding: 12px; border-radius: 4px; margin-bottom: 20px; }");
            out.println(".error { background: #f8d7da; color: #721c24; padding: 12px; border-radius: 4px; margin-bottom: 20px; }");
            out.println(".info-box { background: #e7f3ff; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #17a2b8; }");
            out.println(".input-group { display: flex; gap: 10px; }");
            out.println(".input-group input { flex: 1; }");
            out.println(".btn-browse { background: #28a745; padding: 10px 20px; }");
            out.println(".btn-browse:hover { background: #218838; }");
            out.println("</style>");
            out.println("<script>");
            out.println("function seleccionarCarpeta() {");
            out.println("  // Crear input temporal para selección");
            out.println("  const input = document.createElement('input');");
            out.println("  input.type = 'text';");
            out.println("  input.id = 'tempPathInput';");
            out.println("  input.placeholder = 'Pegue aquí la ruta completa (Ej: C:/Users/user/Documents/ProWork_Grabaciones)';");
            out.println("  input.style.width = '100%';");
            out.println("  input.style.padding = '10px';");
            out.println("  input.style.marginTop = '10px';");
            out.println("  input.style.border = '2px solid #28a745';");
            out.println("  input.style.borderRadius = '4px';");
            out.println("  ");
            out.println("  // Mostrar instrucciones");
            out.println("  const msg = 'INSTRUCCIONES:\\\\n\\\\n' +");
            out.println("              '1. Abra el Explorador de Windows (Windows + E)\\\\n' +");
            out.println("              '2. Navegue hasta la carpeta deseada\\\\n' +");
            out.println("              '3. Haga clic en la barra de direcciones\\\\n' +");
            out.println("              '4. Copie la ruta (Ctrl + C)\\\\n' +");
            out.println("              '5. Pegue la ruta en el campo de texto\\\\n\\\\n' +");
            out.println("              'Ejemplo: C:\\\\\\\\Users\\\\\\\\user\\\\\\\\Documents\\\\\\\\ProWork_Grabaciones\\\\n' +");
            out.println("              'Se convertirá a: C:/Users/user/Documents/ProWork_Grabaciones';");
            out.println("  ");
            out.println("  if (confirm(msg + '\\\\n\\\\n¿Desea continuar?')) {");
            out.println("    const rutaInput = document.getElementById('rutaArchivos');");
            out.println("    const originalValue = rutaInput.value;");
            out.println("    ");
            out.println("    // Mostrar campo temporal");
            out.println("    const tempContainer = document.createElement('div');");
            out.println("    tempContainer.style.marginTop = '15px';");
            out.println("    tempContainer.style.padding = '15px';");
            out.println("    tempContainer.style.backgroundColor = '#e7f3ff';");
            out.println("    tempContainer.style.borderRadius = '4px';");
            out.println("    tempContainer.innerHTML = '<strong>Pegue la ruta de la carpeta aquí:</strong>';");
            out.println("    tempContainer.appendChild(input);");
            out.println("    ");
            out.println("    const btnContainer = document.createElement('div');");
            out.println("    btnContainer.style.marginTop = '10px';");
            out.println("    ");
            out.println("    const btnAceptar = document.createElement('button');");
            out.println("    btnAceptar.type = 'button';");
            out.println("    btnAceptar.textContent = '✓ Usar esta ruta';");
            out.println("    btnAceptar.className = 'btn';");
            out.println("    btnAceptar.style.background = '#28a745';");
            out.println("    btnAceptar.onclick = function() {");
            out.println("      let newPath = input.value.trim();");
            out.println("      if (newPath) {");
            out.println("        // Convertir barras invertidas a diagonales");
            out.println("        newPath = newPath.replace(/\\\\\\\\/g, '/');");
            out.println("        rutaInput.value = newPath;");
            out.println("        tempContainer.remove();");
            out.println("      } else {");
            out.println("        alert('Por favor ingrese una ruta válida');");
            out.println("      }");
            out.println("    };");
            out.println("    ");
            out.println("    const btnCancelar = document.createElement('button');");
            out.println("    btnCancelar.type = 'button';");
            out.println("    btnCancelar.textContent = '✗ Cancelar';");
            out.println("    btnCancelar.className = 'btn btn-secondary';");
            out.println("    btnCancelar.onclick = function() {");
            out.println("      tempContainer.remove();");
            out.println("    };");
            out.println("    ");
            out.println("    btnContainer.appendChild(btnAceptar);");
            out.println("    btnContainer.appendChild(btnCancelar);");
            out.println("    tempContainer.appendChild(btnContainer);");
            out.println("    ");
            out.println("    document.querySelector('.form-group').appendChild(tempContainer);");
            out.println("    input.focus();");
            out.println("  }");
            out.println("}");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            
            // Header
            out.println("<div class='header'>");
            out.println("<div>");
            out.println("<h2 style='margin:0'>ProWork - Configuración de Empresa</h2>");
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
            out.println("<h2>Configuración de Empresa</h2>");
            
            // Mensajes
            if (mensaje != null) {
                out.println("<div class='success'>" + mensaje + "</div>");
            }
            if (error != null) {
                out.println("<div class='error'>" + error + "</div>");
            }
            
            out.println("<div class='info-box'>");
            out.println("<strong>📁 Configuración de Archivos y Permisos</strong><br>");
            out.println("Define dónde se guardarán los archivos de los empleados y qué permisos tendrán ");
            out.println("para grabación de pantalla y audio.");
            out.println("</div>");
            
            out.println("<form method='post' action='" + request.getContextPath() + "/configuracion-empresa'>");
            
            out.println("<div class='form-group'>");
            out.println("<label>Ruta de Archivos: *</label>");
            String rutaActual = configuracion != null ? configuracion.getRutaArchivos() : appConfig.getDocumentosPath();
            // Normalizar la ruta para usar barras diagonales
            if (rutaActual != null) {
                rutaActual = rutaActual.replace("\\\\", "/");
            }
            out.println("<div class='input-group'>");
            out.println("<input type='text' id='rutaArchivos' name='rutaArchivos' value='" + rutaActual + "' required>");
            out.println("<button type='button' class='btn btn-browse' onclick='seleccionarCarpeta()'>📁 Explorar</button>");
            out.println("</div>");
            out.println("<small>Ruta donde los empleados guardarán sus documentos. Ejemplo: C:/ProWork/Archivos o /home/prowork/archivos</small>");
            out.println("</div>");
            
            out.println("<div class='form-group'>");
            out.println("<label style='font-weight: bold; margin-bottom: 10px;'>Permisos de Grabación:</label>");
            boolean grabacionPantalla = configuracion != null && configuracion.isPermiteGrabacionPantalla();
            out.println("<label class='checkbox-label'>");
            out.println("<input type='checkbox' name='permiteGrabacionPantalla' " + (grabacionPantalla ? "checked" : "") + ">");
            out.println("Permitir grabación de pantalla a empleados");
            out.println("</label>");
            out.println("</div>");
            
            out.println("<div class='form-group'>");
            boolean grabacionAudio = configuracion != null && configuracion.isPermiteGrabacionAudio();
            out.println("<label class='checkbox-label'>");
            out.println("<input type='checkbox' name='permiteGrabacionAudio' " + (grabacionAudio ? "checked" : "") + ">");
            out.println("Permitir grabación de audio a empleados");
            out.println("</label>");
            out.println("</div>");
            
            out.println("<div style='margin-top: 30px;'>");
            out.println("<button type='submit' class='btn'>💾 Guardar Configuración</button>");
            out.println("<a href='" + request.getContextPath() + "/dashboard-supervisor' class='btn btn-secondary'>Cancelar</a>");
            out.println("</div>");
            
            out.println("</form>");
            out.println("</div>");
            
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
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
        if (!usuario.isSupervisor() || usuario.getEmpresaId() == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String rutaArchivos = request.getParameter("rutaArchivos");
        // Normalizar la ruta para usar barras diagonales
        if (rutaArchivos != null) {
            rutaArchivos = rutaArchivos.replace("\\\\", "/");
        }
        
        // Crear las carpetas VIDEOS y AUDIOS dentro de la ruta especificada
        boolean carpetasCreadas = true;
        String mensajeError = null;
        
        if (rutaArchivos != null && !rutaArchivos.isEmpty()) {
            try {
                // Crear el directorio base si no existe
                File directorioBase = new File(rutaArchivos);
                if (!directorioBase.exists()) {
                    directorioBase.mkdirs();
                }
                
                // Crear carpeta VIDEOS
                File carpetaVideos = new File(directorioBase, "VIDEOS");
                if (!carpetaVideos.exists()) {
                    carpetasCreadas = carpetaVideos.mkdir();
                }
                
                // Crear carpeta AUDIOS
                File carpetaAudios = new File(directorioBase, "AUDIOS");
                if (!carpetaAudios.exists()) {
                    carpetasCreadas = carpetasCreadas && carpetaAudios.mkdir();
                }
                
                if (!carpetasCreadas) {
                    mensajeError = "No se pudieron crear las carpetas VIDEOS y AUDIOS";
                }
            } catch (Exception e) {
                mensajeError = "Error al crear las carpetas: " + e.getMessage();
                carpetasCreadas = false;
            }
        }
        
        String grabacionPantalla = request.getParameter("permiteGrabacionPantalla");
        String grabacionAudio = request.getParameter("permiteGrabacionAudio");
        
        // Buscar configuración existente o crear nueva
        ConfiguracionEmpresa config = configuracionDAO.findByEmpresaId(usuario.getEmpresaId());
        if (config == null) {
            config = new ConfiguracionEmpresa(usuario.getEmpresaId(), rutaArchivos);
        } else {
            config.setRutaArchivos(rutaArchivos);
        }
        
        config.setPermiteGrabacionPantalla("on".equals(grabacionPantalla));
        config.setPermiteGrabacionAudio("on".equals(grabacionAudio));
        
        // Solo guardar si las carpetas se crearon exitosamente
        if (carpetasCreadas) {
            configuracionDAO.save(config);
            
            // Registrar evento
            String permisos = "";
            if (config.isPermiteGrabacionPantalla()) permisos += "pantalla ";
            if (config.isPermiteGrabacionAudio()) permisos += "audio ";
            
            eventoDAO.registrarEvento("CONFIG_ACTUALIZADA", 
                "Configuración actualizada - Ruta: " + rutaArchivos + 
                " (carpetas VIDEOS y AUDIOS creadas)" +
                (permisos.isEmpty() ? "" : ", Permisos: " + permisos.trim()), 
                usuario.getUsername(), 
                usuario.getEmpresaId());
            
            response.sendRedirect(request.getContextPath() + "/dashboard-supervisor");
        } else {
            // Si hubo error, regresar con mensaje de error
            response.sendRedirect(request.getContextPath() + "/configuracion-empresa?error=" + 
                (mensajeError != null ? java.net.URLEncoder.encode(mensajeError, "UTF-8") : "true"));
        }
    }
}
