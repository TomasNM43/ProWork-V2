package com.prowork.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

/**
 * Servlet para servir archivos de audio y video desde el sistema de archivos
 */
@WebServlet(name = "ServicioArchivosServlet", urlPatterns = {"/servir-archivo"})
public class ServicioArchivosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
            return;
        }
        
        String rutaArchivo = request.getParameter("ruta");
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ruta de archivo no especificada");
            return;
        }
        
        // Decodificar la ruta URL
        rutaArchivo = URLDecoder.decode(rutaArchivo, "UTF-8");
        
        System.out.println("[ServicioArchivosServlet] Ruta recibida (decodificada): " + rutaArchivo);
        
        File archivo = new File(rutaArchivo);
        
        System.out.println("[ServicioArchivosServlet] ¿Archivo existe?: " + archivo.exists());
        System.out.println("[ServicioArchivosServlet] ¿Es archivo?: " + archivo.isFile());
        System.out.println("[ServicioArchivosServlet] Tamaño del archivo: " + archivo.length() + " bytes");
        
        if (!archivo.exists() || !archivo.isFile()) {
            System.err.println("[ServicioArchivosServlet] ERROR: Archivo no encontrado: " + rutaArchivo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado: " + rutaArchivo);
            return;
        }
        
        // Determinar el tipo de contenido basado en la extensión del archivo
        String contentType = getServletContext().getMimeType(archivo.getName());
        if (contentType == null) {
            String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.') + 1).toLowerCase();
            switch (extension) {
                case "mp4":
                    contentType = "video/mp4";
                    break;
                case "avi":
                    contentType = "video/x-msvideo";
                    break;
                case "webm":
                    contentType = "video/webm";
                    break;
                case "mp3":
                    contentType = "audio/mpeg";
                    break;
                case "wav":
                    contentType = "audio/wav";
                    break;
                case "ogg":
                    contentType = "audio/ogg";
                    break;
                default:
                    contentType = "application/octet-stream";
            }
        }
        
        response.setContentType(contentType);
        long fileLength = archivo.length();
        
        // Headers adicionales importantes para streaming de video
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        
        // Manejar range requests para video
        String range = request.getHeader("Range");
        
        System.out.println("[ServicioArchivosServlet] Content-Type: " + contentType);
        System.out.println("[ServicioArchivosServlet] Range header: " + (range != null ? range : "null (transmisión completa)"));
        
        if (range != null && range.startsWith("bytes=")) {
            // El navegador está solicitando un rango específico (común para videos)
            String[] ranges = range.substring("bytes=".length()).split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileLength - 1;
            long contentLength = end - start + 1;
            
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            if (contentLength <= Integer.MAX_VALUE) {
                response.setContentLength((int) contentLength);
            }
            
            System.out.println("[ServicioArchivosServlet] Respondiendo con rango: bytes " + start + "-" + end + "/" + fileLength);
            System.out.println("[ServicioArchivosServlet] Content-Length: " + contentLength);
            
            try (FileInputStream fis = new FileInputStream(archivo);
                 OutputStream out = response.getOutputStream()) {
                
                fis.skip(start);
                byte[] buffer = new byte[8192];
                long bytesToRead = contentLength;
                int bytesRead;
                
                while (bytesToRead > 0 && (bytesRead = fis.read(buffer, 0, (int) Math.min(buffer.length, bytesToRead))) != -1) {
                    out.write(buffer, 0, bytesRead);
                    bytesToRead -= bytesRead;
                }
                out.flush();
                System.out.println("[ServicioArchivosServlet] Range transmitido exitosamente");
            } catch (IOException e) {
                // ClientAbortException es normal cuando el navegador cancela la solicitud
                if (e.getClass().getSimpleName().equals("ClientAbortException") || 
                    e instanceof java.net.SocketException) {
                    System.out.println("[ServicioArchivosServlet] Cliente canceló la transmisión (normal para videos)");
                } else {
                    System.err.println("[ServicioArchivosServlet] ERROR al leer/transmitir el rango: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            // Transmisión completa del archivo
            response.setStatus(HttpServletResponse.SC_OK);
            if (fileLength <= Integer.MAX_VALUE) {
                response.setContentLength((int) fileLength);
            }
            
            System.out.println("[ServicioArchivosServlet] Transmisión completa. Tamaño: " + fileLength + " bytes");
            
            try (FileInputStream fis = new FileInputStream(archivo);
                 OutputStream out = response.getOutputStream()) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                System.out.println("[ServicioArchivosServlet] Archivo transmitido exitosamente");
            } catch (IOException e) {
                // ClientAbortException es normal cuando el navegador cancela la solicitud
                if (e.getClass().getSimpleName().equals("ClientAbortException") || 
                    e instanceof java.net.SocketException) {
                    System.out.println("[ServicioArchivosServlet] Cliente canceló la transmisión (normal)");
                } else {
                    System.err.println("[ServicioArchivosServlet] ERROR al leer/transmitir el archivo: " + e.getMessage());
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al leer el archivo");
                }
            }
        }
    }
}
