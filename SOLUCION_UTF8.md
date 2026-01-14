# Solución al Problema de Codificación UTF-8 en ProWork

## Cambios Realizados

Se han implementado las siguientes mejoras para soportar correctamente caracteres con tildes y otros caracteres especiales:

### 1. Filtro de Codificación UTF-8 (CharacterEncodingFilter)
- **Archivo creado**: `src/main/java/com/prowork/config/CharacterEncodingFilter.java`
- **Propósito**: Establece automáticamente la codificación UTF-8 para todas las peticiones y respuestas HTTP
- **Configuración**: Se registra en `web.xml` para interceptar todas las URLs (`/*`)

### 2. Configuración en web.xml
- **Archivo modificado**: `src/main/webapp/WEB-INF/web.xml`
- **Cambio**: Se agregó el filtro de codificación UTF-8 que procesa todas las peticiones

### 3. Configuración de Base de Datos
- **Archivo modificado**: `src/main/resources/application.properties`
- **Cambio**: Se agregaron parámetros `characterEncoding=UTF-8&useUnicode=true` a la URL de conexión
- **URL actualizada**: 
  ```
  jdbc:mysql://localhost:3306/ginnet_prowork?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true
  ```

### 4. Configuración de Tomcat
- **Archivo modificado**: `pom.xml`
- **Cambio**: Se agregó `<uriEncoding>UTF-8</uriEncoding>` al plugin de Tomcat Maven

### 5. Script SQL para Base de Datos
- **Archivo creado**: `src/main/resources/sql/configure_utf8.sql`
- **Propósito**: Convierte todas las tablas de la base de datos a utf8mb4

## Pasos para Aplicar la Solución

### 1. Actualizar la Base de Datos
Ejecuta el siguiente script SQL en tu base de datos MySQL:
```bash
mysql -u root -p ginnet_prowork < src/main/resources/sql/configure_utf8.sql
```

O ejecuta manualmente en MySQL:
```sql
ALTER DATABASE ginnet_prowork CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE Empresa CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE usuario CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE Evento CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE Perfil CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE ConfiguracionEmpresa CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Limpiar y Reconstruir el Proyecto
```bash
mvn clean compile
```

### 3. Reiniciar el Servidor Tomcat
```bash
mvn tomcat7:run
```

## Verificación

Después de aplicar estos cambios:

1. **Textos en HTML**: Los caracteres con tildes (á, é, í, ó, ú, ñ) deben mostrarse correctamente
2. **Formularios**: Los datos enviados con tildes deben guardarse y recuperarse correctamente
3. **Base de Datos**: Los registros con caracteres especiales deben almacenarse sin problemas

## Ejemplo de Prueba

Puedes probar creando un usuario o empresa con nombre que contenga tildes:
- Nombre: "José García"
- Descripción: "Administración y gestión"
- Email: "jose@correo.com"

Estos valores deben mostrarse correctamente en toda la aplicación.

## Notas Importantes

- **utf8mb4**: Se usa utf8mb4 en lugar de utf8 porque soporta el conjunto completo de caracteres Unicode, incluyendo emojis
- **Filtro Global**: El filtro se aplica a todas las URLs, asegurando consistencia en toda la aplicación
- **Ya implementado en servlets**: Los servlets ya tenían `charset=UTF-8` en `setContentType()`, pero ahora está garantizado por el filtro

## Solución de Problemas

Si aún ves caracteres extraños:

1. Verifica que tu editor de código esté guardando archivos en UTF-8
2. Asegúrate de que el navegador esté usando UTF-8 (debería ser automático)
3. Revisa los logs de Tomcat para errores de codificación
4. Verifica la configuración de tu MySQL ejecutando:
   ```sql
   SHOW VARIABLES LIKE 'character_set%';
   ```
