# ProWork

Aplicación web desarrollada con Java, Servlets, JSP y Apache Tomcat.

## Requisitos

- **JDK 11** o superior
- **Apache Maven** 3.6+
- **Apache Tomcat** 9.0+ o 10.0+
- **VS Code** con extensiones de Java

## Estructura del Proyecto

```
ProWork-V2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/prowork/
│   │   │       └── servlets/
│   │   │           └── HelloServlet.java
│   │   ├── webapp/
│   │   │   ├── WEB-INF/
│   │   │   │   └── web.xml
│   │   │   ├── css/
│   │   │   │   └── style.css
│   │   │   ├── js/
│   │   │   │   └── main.js
│   │   │   └── index.jsp
│   │   └── resources/
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

## Instalación

1. **Clonar o navegar al proyecto**
   ```bash
   cd ProWork-V2
   ```

2. **Compilar el proyecto con Maven**
   ```bash
   mvn clean package
   ```

3. **El archivo WAR se generará en**
   ```
   target/ProWork.war
   ```

## Despliegue

### Opción 1: Tomcat Local
1. Copiar `target/ProWork.war` a la carpeta `webapps` de Tomcat
2. Iniciar Tomcat
3. Acceder a: `http://localhost:8080/ProWork/`

### Opción 2: Desde VS Code
1. Instalar la extensión "Community Server Connectors"
2. Configurar servidor Tomcat
3. Desplegar el proyecto directamente

## Uso

- **Página principal**: `http://localhost:8080/ProWork/`
- **Servlet de ejemplo**: `http://localhost:8080/ProWork/hello`

## Tecnologías

- Java 11
- Servlet API 4.0
- JSP 2.3
- JSTL 1.2
- Apache Tomcat
- Maven

## Desarrollo

Para desarrollo activo:
```bash
mvn clean compile
mvn package
```

## Licencia

© 2025 ProWork
