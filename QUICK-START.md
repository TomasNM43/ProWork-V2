# 🚀 Inicio Rápido - ProWork V2

## Para Desarrolladores (PC Local)

### 1. Clonar el Repositorio
```bash
git clone https://github.com/TU_USUARIO/ProWork-V2.git
cd ProWork-V2
```

### 2. Configurar MySQL
```sql
CREATE DATABASE prowork CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Ejecutar los scripts:
```bash
mysql -u root -p prowork < src/main/resources/sql/schema.sql
mysql -u root -p prowork < src/main/resources/sql/create_configuracion_empresa.sql
mysql -u root -p prowork < src/main/resources/sql/configure_utf8.sql
```

### 3. Configurar Credenciales
Editar `src/main/resources/application.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/prowork?useUnicode=true&characterEncoding=UTF-8
db.username=root
db.password=tu_password
```

### 4. Ejecutar
```bash
mvn clean tomcat7:run
```

### 5. Acceder
- URL: http://localhost:8080/ProWork
- Crear usuario admin en la BD (ver DEPLOYMENT.md)

---

## Para Producción (Servidor Windows)

### Requisitos
- Windows Server 2016+ o Windows 10+
- Java JDK 11+
- MySQL 8.0+
- Apache Tomcat 9+
- IIS (opcional, para proxy)

### Despliegue Rápido
```bash
# 1. Clonar
git clone https://github.com/TU_USUARIO/ProWork-V2.git
cd ProWork-V2

# 2. Configurar DB (ejecutar scripts SQL)

# 3. Editar application.properties

# 4. Compilar
mvn clean package

# 5. Copiar a Tomcat
copy target\ProWork.war C:\apache-tomcat-9.0\webapps\

# 6. Iniciar Tomcat
C:\apache-tomcat-9.0\bin\startup.bat
```

---

## Configuración de IIS como Proxy (Opcional)

**¿Por qué?** Para usar el puerto 80/443 estándar y tener HTTPS.

1. Instalar módulos IIS:
   - URL Rewrite
   - Application Request Routing (ARR)

2. Crear `web.config` en la raíz del proyecto

3. Configurar sitio en IIS apuntando a la carpeta del proyecto

4. Iniciar Tomcat en segundo plano

**Ver detalles completos en [DEPLOYMENT.md](DEPLOYMENT.md)**

---

## URLs Importantes

- **Desarrollo:** http://localhost:8080/ProWork
- **Producción (Tomcat directo):** http://servidor:8080/ProWork  
- **Producción (con IIS):** http://servidor/ProWork

---

## Primeros Pasos

1. **Crear usuario admin:**
```sql
INSERT INTO usuarios (nombre, apellido, email, contrasena, rol, activo, fecha_creacion)
VALUES ('Admin', 'Sistema', 'admin@prowork.com', 'admin123', 'ADMINISTRADOR', true, NOW());
```

2. **Iniciar sesión:** admin@prowork.com / admin123

3. **Crear empresa desde el dashboard**

4. **Crear supervisor para la empresa**

5. **El supervisor puede crear empleados**

---

## Solución Rápida de Problemas

| Problema | Solución |
|----------|----------|
| Error de conexión DB | Verificar MySQL ejecutándose y credenciales |
| Puerto 8080 ocupado | Cambiar puerto en `conf/server.xml` de Tomcat |
| 404 Not Found | Verificar que el WAR se desplegó en `webapps/` |
| IIS no conecta | Habilitar ARR Proxy en IIS |
| Error UTF-8 | Ejecutar `configure_utf8.sql` |

---

**📚 Documentación completa:** Ver [DEPLOYMENT.md](DEPLOYMENT.md)
