# Piedra Azul (Prototipo: Aun le faltan cosas (ing soft 3 - Segunda entrega!!))

> Sistema de gestión de citas médicas desarrollado con Spring Boot.

---

## 📋 Tabla de contenidos

- [Descripción general](#descripción-general)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Variables de entorno](#variables-de-entorno)
- [Endpoints principales](#endpoints-principales)
- [Autenticación](#autenticación)
- [Equipo](#equipo)

---

## Descripción general

Piedra Azul es el backend de un sistema de agendamiento de citas médicas que permite
gestionar pacientes, médicos, horarios y citas. Provee una API REST consumida por
el frontend desarrollado en Angular.

---

## Tecnologías

| Tecnología      | Versión  | Uso                              |
|-----------------|----------|----------------------------------|
| Java            | 17+      | Lenguaje principal               |
| Spring Boot     | 3.x      | Framework base                   |
| Spring Security | 3.x      | Seguridad y autorización         |
| Keycloak        | 22+      | Servidor de identidad (OAuth2)   |
| Spring Data JPA | 3.x      | Acceso a datos                   |
| PostgreSQL      | 15+      | Base de datos relacional         |
| Maven           | 3.8+     | Gestión de dependencias          |
| Docker          | 24+      | Contenedorización de Keycloak    |

---

## Arquitectura

El proyecto implementa una arquitectura de **monolito modular** sobre Spring Boot.
El código se organiza verticalmente por dominio de negocio, donde cada módulo
encapsula su propio controlador, servicio, repositorio, entidades y DTOs.

La autenticación y autorización se delegan completamente a **Keycloak** mediante
el protocolo OAuth 2.0 / OpenID Connect. Spring Boot actúa como Resource Server,
validando los tokens JWT en cada petición entrante.

Para más detalle sobre las decisiones arquitectónicas, consulta la carpeta
[`/docs/adr`](./docs/adr/).

---

## Estructura del proyecto

```
piedraAzul-back/
└── src/main/java/com/piedrazul/appointments/
    ├── auth/                  # Autenticación y configuración de seguridad
    │   ├── controller/
    │   └── dto/
    ├── cita/                  # Gestión de citas médicas
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── mapper/
    │   ├── repository/
    │   └── service/
    ├── configuracion/         # Configuración general del sistema
    │   ├── controller/
    │   ├── entity/
    │   ├── repository/
    │   └── service/
    ├── medico/                # Gestión de médicos
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── mapper/
    │   ├── repository/
    │   └── service/
    ├── paciente/              # Gestión de pacientes
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── mapper/
    │   ├── repository/
    │   └── service/
    ├── shared/                # Utilidades y componentes compartidos
    └── AppointmentsApplication.java
```

---

## Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) (para levantar Keycloak)
- [PostgreSQL 15+](https://www.postgresql.org/)

---

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/Alexmax404/PIEDRA-AZUL---SWIII-.git
cd PIEDRA-AZUL---SWIII-/piedraAzul-back
```

### 2. Levantar Keycloak con Docker

```bash
docker run -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev
```

Luego importa la configuración del Realm desde `/data/realm-export.json`
en la consola de administración de Keycloak (`http://localhost:8080`).

### 3. Configurar la base de datos

Crea una base de datos PostgreSQL:

```sql
CREATE DATABASE piedraazul;
```

### 4. Configurar variables de entorno

Copia el archivo de ejemplo y completa los valores:

```bash
cp src/main/resources/application.example.properties \
   src/main/resources/application.properties
```

### 5. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8081`.

---

## Variables de entorno

| Variable                        | Descripción                        | Ejemplo                          |
|---------------------------------|------------------------------------|----------------------------------|
| `SPRING_DATASOURCE_URL`         | URL de conexión a PostgreSQL       | `jdbc:postgresql://localhost/piedraazul` |
| `SPRING_DATASOURCE_USERNAME`    | Usuario de la base de datos        | `postgres`                       |
| `SPRING_DATASOURCE_PASSWORD`    | Contraseña de la base de datos     | `admin`                          |
| `KEYCLOAK_AUTH_SERVER_URL`      | URL del servidor Keycloak          | `http://localhost:8080`          |
| `KEYCLOAK_REALM`                | Nombre del Realm configurado       | `piedraazul`                     |
| `KEYCLOAK_CLIENT_ID`            | Client ID registrado en Keycloak   | `piedraazul-back`                |

---

## Endpoints principales

| Método | Endpoint                        | Descripción                        | Rol requerido   |
|--------|---------------------------------|------------------------------------|-----------------|
| GET    | `/api/citas`                    | Listar todas las citas             | ADMIN           |
| POST   | `/api/citas`                    | Crear una nueva cita               | PACIENTE        |
| GET    | `/api/citas/{id}`               | Obtener detalle de una cita        | ADMIN, MEDICO   |
| DELETE | `/api/citas/{id}`               | Cancelar una cita                  | ADMIN, PACIENTE |
| GET    | `/api/medicos`                  | Listar médicos disponibles         | PUBLIC          |
| GET    | `/api/medicos/{id}/horarios`    | Consultar horarios de un médico    | PUBLIC          |
| GET    | `/api/pacientes/{id}`           | Obtener perfil de paciente         | ADMIN, PACIENTE |

> ⚠️ Todos los endpoints protegidos requieren un token JWT válido emitido por Keycloak
> en el header `Authorization: Bearer <token>`.

---

## Autenticación

El sistema usa **Keycloak** como proveedor de identidad con OAuth 2.0 / OIDC.

**Roles disponibles:**

- `ADMIN` — acceso completo al sistema
- `MEDICO` — gestión de su agenda y citas asignadas
- `PACIENTE` — agendamiento y consulta de sus propias citas

Para obtener un token en desarrollo:

```bash
curl -X POST http://localhost:8080/realms/piedraazul/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=piedraazul-back" \
  -d "username=TU_USUARIO" \
  -d "password=TU_PASSWORD"
```

---

## Equipo

| Nombre              | Rol                  |
|---------------------|----------------------|
| Santiago Bastidas   | Backend / Base de datos |
| Sofia Cortes        | Frontend / Integración  |
| Jose Rodriguez      | Backend / Base de datos |
| Glenn Ward          | Frontend / Integración  |

---

> Proyecto académico — Ingeniería de Software III · Universidad del Cauca · 2026
