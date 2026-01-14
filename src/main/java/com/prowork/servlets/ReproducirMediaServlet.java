package com.prowork.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

/**
 * Servlet para reproducir archivos de audio y video en ventana emergente
 */
@WebServlet(name = "ReproducirMediaServlet", urlPatterns = {"/reproducir-media"})
public class ReproducirMediaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String rutaArchivo = request.getParameter("ruta");
        String tipo = request.getParameter("tipo");
        
        if (rutaArchivo == null || tipo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros inválidos");
            return;
        }
        
        // Decodificar la ruta URL
        rutaArchivo = URLDecoder.decode(rutaArchivo, "UTF-8");
        
        System.out.println("[ReproducirMediaServlet] Ruta recibida (decodificada): " + rutaArchivo);
        System.out.println("[ReproducirMediaServlet] Tipo: " + tipo);
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>" + (tipo.equals("audio") ? "Reproducir Audio" : "Reproducir Video") + " - ProWork</title>");
            out.println("<style>");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { background: #1a1a1a; display: flex; justify-content: center; align-items: center; min-height: 100vh; font-family: Arial, sans-serif; }");
            out.println(".container { text-align: center; padding: 20px; }");
            out.println("h2 { color: white; margin-bottom: 20px; font-size: 18px; }");
            out.println("audio, video { max-width: 100%; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.5); }");
            out.println("audio { width: 100%; max-width: 450px; }");
            out.println("video { max-width: 100%; max-height: 80vh; }");
            out.println(".info { color: #999; margin-top: 15px; font-size: 12px; }");
            out.println(".debug { color: #ff6b6b; background: #2a2a2a; padding: 10px; margin-top: 10px; border-radius: 4px; font-family: monospace; font-size: 11px; text-align: left; }");
            out.println(".warning-box { background: #ff9800; color: white; padding: 20px; border-radius: 8px; margin: 20px auto; max-width: 600px; text-align: left; }");
            out.println(".btn-download { display: inline-block; padding: 12px 24px; background: #4CAF50; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; margin: 10px 5px; }");
            out.println(".btn-download:hover { background: #45a049; }");
            out.println(".btn-convert { background: #2196F3; }");
            out.println(".btn-convert:hover { background: #0b7dda; }");
            out.println("</style>");
            out.println("<script>");
            out.println("function logEvent(msg) {");
            out.println("  var debug = document.getElementById('debug');");
            out.println("  if (debug) debug.innerHTML += msg + '<br>';");
            out.println("  console.log(msg);");
            out.println("}");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            
            if ("audio".equals(tipo)) {
                out.println("<h2>🔊 Reproducir Audio</h2>");
                out.println("<audio controls autoplay>");
                out.println("<source src='" + request.getContextPath() + "/servir-archivo?ruta=" + java.net.URLEncoder.encode(rutaArchivo, "UTF-8") + "' type='audio/mpeg'>");
                out.println("Tu navegador no soporta la reproducción de audio.");
                out.println("</audio>");
            } else if ("video".equals(tipo)) {
                out.println("<h2>🎥 Reproducir Video</h2>");
                String videoUrl = request.getContextPath() + "/servir-archivo?ruta=" + java.net.URLEncoder.encode(rutaArchivo, "UTF-8");
                String nombreArchivo = rutaArchivo.substring(rutaArchivo.lastIndexOf('\\') + 1);
                String downloadUrl = request.getContextPath() + "/descargar-archivo?ruta=" + java.net.URLEncoder.encode(rutaArchivo, "UTF-8");
                
                out.println("<video id='videoPlayer' controls preload='auto' style='background: #000; max-width: 100%;'>");
                out.println("<source src='" + videoUrl + "' type='video/mp4'>");
                out.println("Tu navegador no soporta la reproducción de video.");
                out.println("</video>");
                
                out.println("<div id='warningBox' class='warning-box' style='display:none;'>");
                out.println("<h3 style='margin-top:0;'>⚠️ El navegador no puede reproducir este video</h3>");
                out.println("<p><strong>Motivo más probable:</strong> El video está codificado con un codec no compatible con navegadores web (como H.265/HEVC).</p>");
                out.println("<p><strong>Soluciones:</strong></p>");
                out.println("<div style='text-align: center;'>");
                out.println("<a href='" + downloadUrl + "' class='btn-download' download='" + nombreArchivo + "'>📥 Descargar y Reproducir con VLC/Windows Media Player</a>");
                out.println("</div>");
                out.println("<p style='font-size: 12px; margin-top: 15px;'><strong>Nota:</strong> Para que los videos se reproduzcan en el navegador, deben estar codificados con H.264 (video) y AAC (audio). Puedes usar herramientas como HandBrake o FFmpeg para convertir los videos.</p>");
                out.println("</div>");
                
                out.println("<div id='debug' class='debug' style='max-height: 300px; overflow-y: auto;'>Iniciando...</div>");
                out.println("<script>");
                out.println("var video = document.getElementById('videoPlayer');");
                out.println("var warningBox = document.getElementById('warningBox');");
                out.println("logEvent('Video URL: " + videoUrl.replace("'", "\\'") + "');");
                out.println("logEvent('Archivo: " + nombreArchivo.replace("'", "\\'") + "');");
                out.println("fetch('" + videoUrl.replace("'", "\\'") + "', {method: 'HEAD'}).then(r => {");
                out.println("  logEvent('Fetch HEAD Response:');");
                out.println("  logEvent('  Status: ' + r.status);");
                out.println("  logEvent('  Content-Type: ' + r.headers.get('Content-Type'));");
                out.println("  logEvent('  Content-Length: ' + r.headers.get('Content-Length'));");
                out.println("  logEvent('  Accept-Ranges: ' + r.headers.get('Accept-Ranges'));");
                out.println("}).catch(e => logEvent('Fetch error: ' + e));");
                out.println("video.addEventListener('loadstart', function() { logEvent('✓ loadstart: Iniciando carga'); });");
                out.println("video.addEventListener('loadedmetadata', function() { ");
                out.println("  logEvent('✓✓✓ loadedmetadata: ¡ÉXITO! Video compatible'); ");
                out.println("  logEvent('  - Duración: ' + video.duration + ' segundos');");
                out.println("  logEvent('  - Dimensiones: ' + video.videoWidth + 'x' + video.videoHeight);");
                out.println("});");
                out.println("video.addEventListener('canplay', function() { logEvent('✓ canplay: Listo para reproducir'); });");
                out.println("video.addEventListener('playing', function() { logEvent('▶ playing: Reproduciendo'); });");
                out.println("video.addEventListener('error', function(e) { ");
                out.println("  var errorMsg = '❌ ERROR: ';");
                out.println("  switch(video.error.code) {");
                out.println("    case 3: errorMsg += 'MEDIA_ERR_DECODE - Codec no compatible'; break;");
                out.println("    case 4: errorMsg += 'MEDIA_ERR_SRC_NOT_SUPPORTED - Formato no soportado'; break;");
                out.println("    default: errorMsg += 'Code ' + video.error.code;");
                out.println("  }");
                out.println("  logEvent(errorMsg);");
                out.println("  warningBox.style.display = 'block';");
                out.println("});");
                out.println("setTimeout(function() {");
                out.println("  if (video.readyState === 0) {");
                out.println("    logEvent('⚠ TIMEOUT: El video no cargó en 5 segundos');");
                out.println("    var ns = video.networkState;");
                out.println("    if (ns === 3) {");
                out.println("      logEvent('❌ NETWORK_NO_SOURCE: Codec incompatible o archivo corrupto');");
                out.println("      warningBox.style.display = 'block';");
                out.println("    }");
                out.println("  }");
                out.println("}, 5000);");
                out.println("</script>");
            }
            
            out.println("<div class='info'>Cierra esta ventana cuando termines de reproducir</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
