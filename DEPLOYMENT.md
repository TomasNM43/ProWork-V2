# Guía de Despliegue - ProWork V2

## 📋 Requisitos Previos

### En la PC de Destino
1. **Java JDK 11 o superior**
   - Descargar de: https://adoptium.net/
   - Verificar instalación: `java -version`

2. **Apache Maven**
   - Descargar de: https://maven.apache.org/download.cgi
   - Verificar instalación: `mvn -version`

3. **MySQL Server**
   - Descargar de: https://dev.mysql.com/downloads/mysql/
   - Versión recomendada: 8.0+

4. **Git**
   - Descargar de: https://git-scm.com/downloads
   - Verificar instalación: `git --version`

5. **Apache Tomcat 9 o 10** (opcional si usas el plugin de Maven)
   - Descargar de: https://tomcat.apache.org/download-90.cgi

---

## 🚀 Opción 1: Despliegue con Tomcat Standalone

### Paso 1: Clonar el Repositorio
```bash
cd C:\inetpub\
git clone https://github.com/TU_USUARIO/ProWork-V2.git
cd ProWork-V2
```

### Paso 2: Configurar la Base de Datos
```bash
# Conectarse a MySQL
mysql -u root -p

# Crear la base de datos
CREATE DATABASE prowork CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Crear usuario (opcional)
CREATE USER 'prowork_user'@'localhost' IDENTIFIED BY 'tu_password_seguro';
GRANT ALL PRIVILEGES ON prowork.* TO 'prowork_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;

# Ejecutar los scripts SQL
mysql -u root -p prowork < src/main/resources/sql/schema.sql
mysql -u root -p prowork < src/main/resources/sql/create_configuracion_empresa.sql
mysql -u root -p prowork < src/main/resources/sql/configure_utf8.sql
```

### Paso 3: Configurar application.properties
Editar `src/main/resources/application.properties`:
```properties
# Configuración de Base de Datos
db.url=jdbc:mysql://localhost:3306/prowork?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
db.username=prowork_user
db.password=tu_password_seguro
db.driver=com.mysql.cj.jdbc.Driver

# Pool de Conexiones
db.pool.initialSize=5
db.pool.maxActive=20
db.pool.maxIdle=10
db.pool.minIdle=5
```

### Paso 4: Compilar el Proyecto
```bash
mvn clean package
```

### Paso 5: Desplegar en Tomcat

#### Opción A: Usar el plugin de Maven (Desarrollo)
```bash
mvn tomcat7:run
```
La aplicación estará disponible en: `http://localhost:8083/ProWork`

#### Opción B: Desplegar en Tomcat Instalado (Producción)
```bash
# Copiar el WAR generado a Tomcat
copy target\ProWork.war C:\apache-tomcat-9.0\webapps\

# Iniciar Tomcat
cd C:\apache-tomcat-9.0\bin
startup.bat
```
La aplicación estará disponible en: `http://localhost:8083/ProWork`

---

## 🌐 Opción 2: Usar IIS como Proxy Inverso (Producción)

Si necesitas que la aplicación esté disponible en el puerto 80 (HTTP) o 443 (HTTPS) usando IIS:

### Paso 1: Instalar URL Rewrite en IIS
1. Descargar: https://www.iis.net/downloads/microsoft/url-rewrite
2. Instalar el módulo

### Paso 2: Instalar Application Request Routing (ARR)
1. Descargar: https://www.iis.net/downloads/microsoft/application-request-routing
2. Instalar el módulo
3. Abrir **Administrador de IIS** (IIS Manager)
4. Hacer clic en el **nombre del servidor** (nodo raíz) → **Enrutamiento de solicitud de aplicación** (Application Request Routing Cache)
5. **Configuración del servidor proxy** (Server Proxy Settings) → Marcar **"Habilitar proxy"** → Aplicar

### Paso 3: Crear Sitio en IIS
1. Abrir **Administrador de IIS** (IIS Manager)
2. **Sitios** (Sites) → Clic derecho → **Agregar sitio web** (Add Website)
   - **Nombre del sitio:** ProWork
   - **Ruta de acceso física:** C:\Users\user\Documents\Proyectos\Ginnet\ProWork-V2
   - **Puerto:** 10003 (o el que desees)

### Paso 4: Configurar URL Rewrite
Crear archivo `web.config` en la raíz del proyecto:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <system.webServer>
        <rewrite>
            <rules>
                <rule name="ReverseProxyInboundRule" stopProcessing="true">
                    <match url="(.*)" />
                    <action type="Rewrite" url="http://localhost:8083/ProWork/{R:1}" />
                </rule>
            </rules>
        </rewrite>
    </system.webServer>
</configuration>
```

### Paso 5: Iniciar Tomcat y IIS
```bash
# Iniciar Tomcat
cd C:\apache-tomcat-9.0\bin
startup.bat

# IIS ya debe estar ejecutándose
```

Ahora la aplicación estará disponible en:
- **Directo (Tomcat):** http://localhost:8083/ProWork
- **A través de IIS:** http://localhost:10003/ o http://tu-dominio.com:10003

---

## 🔒 Configuración de HTTPS en IIS (Opcional)

1. Obtener un certificado SSL (Let's Encrypt, comprado, o autofirmado)
2. En IIS Manager → Tu sitio → Bindings → Add
   - **Type:** https
   - **Port:** 443
   - **SSL certificate:** Seleccionar tu certificado
3. Actualizar `web.config` para forzar HTTPS:

```xml
<rule name="HTTP to HTTPS redirect" stopProcessing="true">
    <match url="(.*)" />
    <conditions>
        <add input="{HTTPS}" pattern="off" ignoreCase="true" />
    </conditions>
    <action type="Redirect" url="https://{HTTP_HOST}/{R:1}" redirectType="Permanent" />
</rule>
```

---

## 📝 Configuración Inicial de la Aplicación

### Primera Ejecución
1. Acceder a: `http://localhost/ProWork` (o tu URL)
2. Crear el primer usuario administrador directamente en la base de datos:

```sql
INSERT INTO usuarios (nombre, apellido, email, contrasena, rol, activo, fecha_creacion)
VALUES ('Admin', 'Sistema', 'admin@prowork.com', 'admin123', 'ADMINISTRADOR', true, NOW());
```

3. Iniciar sesión con:
   - **Email:** admin@prowork.com
   - **Contraseña:** admin123
   - ⚠️ **Cambiar la contraseña inmediatamente**

---

## 🔧 Solución de Problemas

### Error de Conexión a Base de Datos
- Verificar que MySQL esté ejecutándose: `services.msc`
- Verificar credenciales en `application.properties`
- Verificar que el firewall permita conexiones al puerto 3306

### Error 404 - Aplicación no encontrada
- Verificar que el WAR se desplegó correctamente en `webapps`
- Revisar logs de Tomcat: `C:\apache-tomcat-9.0\logs\catalina.out`

### IIS no puede conectarse a Tomcat
- Verificar que Tomcat esté ejecutándose
- Verificar que ARR Proxy esté habilitado
- Revisar logs de IIS: Event Viewer → Windows Logs → Application

### Puerto 8083 ya está en uso
Editar `C:\apache-tomcat-9.0\conf\server.xml` y cambiar el puerto:
```xml
<Connector port="8084" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

---

## 📊 Monitoreo y Logs

### Logs de Tomcat
- Ubicación: `C:\apache-tomcat-9.0\logs\`
- Archivo principal: `catalina.out`

### Logs de IIS
- Event Viewer → Windows Logs → Application
- Logs de sitio específico en: `C:\inetpub\logs\LogFiles\`

---

## 🔄 Actualización del Proyecto

Para actualizar a una nueva versión:

```bash
cd C:\inetpub\ProWork-V2

# Detener Tomcat
cd C:\apache-tomcat-9.0\bin
shutdown.bat

# Actualizar código
git pull origin main

# Recompilar
mvn clean package

# Redesplegar
copy target\ProWork.war C:\apache-tomcat-9.0\webapps\

# Reiniciar Tomcat
startup.bat
```

---

## 📞 Soporte

Para problemas o dudas:
- Revisar los logs en `target/tomcat/logs/`
- Verificar la configuración de base de datos
- Asegurar que todos los servicios estén ejecutándose

---

## ✅ Checklist de Despliegue

- [ ] Java JDK instalado y configurado
- [ ] Maven instalado
- [ ] MySQL instalado y ejecutándose
- [ ] Base de datos creada y scripts ejecutados
- [ ] `application.properties` configurado
- [ ] Proyecto compilado sin errores
- [ ] Tomcat ejecutándose
- [ ] Aplicación accesible en navegador
- [ ] Usuario administrador creado
- [ ] IIS configurado (si aplica)
- [ ] HTTPS configurado (si aplica)
- [ ] Firewall configurado para permitir tráfico
