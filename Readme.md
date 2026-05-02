# Piedra Azul — Sistema de Gestión de Citas Médicas

> Ingeniería de Software III · Universidad del Cauca · 2026

Sistema de agendamiento de citas médicas para la clínica Piedra Azul. Compuesto por un backend en Spring Boot, un frontend en Vue 3, y Keycloak como servidor de identidad — los tres completamente dockerizados.

---

## Tabla de contenidos

- [Descripción general](#descripción-general)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Requisitos previos](#requisitos-previos)
- [Levantar el proyecto con Docker Compose](#levantar-el-proyecto-con-docker-compose)
- [Desarrollo local (sin Docker)](#desarrollo-local-sin-docker)
- [Variables de entorno](#variables-de-entorno)
- [Endpoints principales](#endpoints-principales)
- [Autenticación](#autenticación)
- [Equipo](#equipo)

---

## Descripción general

Piedra Azul permite a pacientes agendar, reagendar y cancelar citas médicas a través de un portal web. Los administradores gestionan médicos, horarios y configuración del sistema. La autenticación se delega a Keycloak con OAuth 2.0 / OpenID Connect.

---

## Tecnologías

### Backend
| Tecnología      | Versión | Uso                            |
|-----------------|---------|--------------------------------|
| Java            | 21      | Lenguaje principal             |
| Spring Boot     | 3.x     | Framework base                 |
| Spring Security | 3.x     | Seguridad y autorización       |
| Spring Data JPA | 3.x     | Acceso a datos                 |
| H2 Database     | —       | Base de datos embebida         |
| Maven           | 3.9+    | Gestión de dependencias        |

### Frontend
| Tecnología | Versión | Uso                        |
|------------|---------|----------------------------|
| Vue 3      | 3.4+    | Framework UI               |
| Vite       | 5.x     | Bundler y dev server       |
| nginx      | stable  | Servidor en producción     |

### Infraestructura
| Tecnología | Versión | Uso                               |
|------------|---------|-----------------------------------|
| Keycloak   | 24.0.3  | Servidor de identidad (OAuth2)    |
| Docker     | 24+     | Contenedorización                 |

---

## Arquitectura

El sistema está compuesto por tres servicios independientes que se comunican entre sí:

```
Navegador
    │
    ▼
[frontend-piedrazul]  :5173 → nginx :80
    │  llamadas REST con JWT
    ▼
[backend-piedrazul]   :8080
    │  valida tokens JWT
    ▼
[keycloak-piedrazul]  :8180 → KC :8080
```

El backend implementa una arquitectura de **monolito modular**: el código se organiza verticalmente por dominio de negocio (cita, médico, paciente, configuración), donde cada módulo encapsula su propio controlador, servicio, repositorio, entidades y DTOs.

Spring Boot actúa como Resource Server, validando los tokens JWT emitidos por Keycloak en cada petición entrante.

---

## Estructura del proyecto

```
PIEDRA-AZUL---SWIII-/
├── docker-compose.yml               ← orquesta los 3 servicios
├── .gitignore                       ← ignora BD, node_modules, target, etc.
├── docs/
│   ├── ADR-001.md
│   ├── ADR-002.md
│   └── ADR-003.md
│
├── piedraAzul-back/                 ── BACKEND ──
│   ├── Dockerfile
│   ├── pom.xml
│   ├── keycloak/
│   │   ├── piedrazul-realm.json     ← realm pre-configurado
│   │   └── data/                   ← BD H2 de Keycloak (ignorada en git)
│   └── src/main/java/com/piedrazul/appointments/
│       ├── auth/
│       ├── cita/
│       ├── configuracion/
│       ├── medico/
│       ├── paciente/
│       └── shared/
│
└── piedraAzul-front-vue/            ── FRONTEND ──
    ├── Dockerfile
    ├── vite.config.js
    ├── package.json
    └── src/
        ├── main.js
        ├── App.vue
        ├── composables/
        │   ├── useAuth.js
        │   ├── useMedicos.js
        │   ├── useToast.js
        │   └── useFormValidation.js
        ├── services/
        │   └── api.js               ← capa HTTP hacia el backend
        ├── components/
        │   ├── atoms/
        │   ├── molecules/
        │   └── organisms/
        └── views/
            ├── LoginView.vue
            ├── PortalPaciente.vue
            ├── CrearCita.vue
            ├── ListarCitas.vue
            └── Configuracion.vue
```

---

## Requisitos previos

Solo necesitas tener instalado:

- [Docker](https://www.docker.com/) con Docker Compose v2

Para desarrollo local también necesitas:
- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Node.js 20+](https://nodejs.org/)

---

## Levantar el proyecto con Docker Compose

### 1. Clonar el repositorio

```bash
git clone https://github.com/Alexmax404/PIEDRA-AZUL---SWIII-.git
cd PIEDRA-AZUL---SWIII-
```

### 2. Levantar los tres servicios

```bash
docker compose up --build
```

El primer arranque tarda varios minutos porque Maven descarga dependencias y compila el JAR dentro del contenedor.

### 3. Acceder a la aplicación

| Servicio   | URL                        |
|------------|----------------------------|
| Frontend   | http://localhost:5173       |
| Backend    | http://localhost:8080       |
| Keycloak   | http://localhost:8180       |

Para detener todo:

```bash
docker compose down
```

> **Nota:** los archivos `*.mv.db`, `*.trace.db` y la carpeta `data/` que Docker genera localmente están en el `.gitignore` raíz y no deben subirse al repositorio.

---

## Desarrollo local (sin Docker)

### Backend

```bash
cd piedraAzul-back
mvn spring-boot:run
```

El backend arranca en `http://localhost:8080`. Keycloak sigue necesitando estar corriendo; puedes levantarlo solo con:

```bash
docker compose up keycloak
```

### Frontend

```bash
cd piedraAzul-front-vue
npm install
npm run dev
```

El dev server de Vite arranca en `http://localhost:5173`.

La URL del backend se configura con la variable de entorno `VITE_API_URL`. Si no se define, el frontend apunta por defecto a `http://localhost:8080/api`.

```bash
# Ejemplo con URL personalizada
VITE_API_URL=http://mi-servidor:8080/api npm run dev
```

---

## Variables de entorno

### Backend (docker-compose.yml)

| Variable                                                    | Descripción                              | Valor por defecto en Docker            |
|-------------------------------------------------------------|------------------------------------------|----------------------------------------|
| `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI`     | URI de las claves públicas de Keycloak   | `http://keycloak:8080/realms/piedrazul/...` |
| `KEYCLOAK_SERVER_URL`                                       | URL interna de Keycloak                  | `http://keycloak:8080`                 |
| `KEYCLOAK_REALM`                                            | Nombre del realm                         | `piedrazul`                            |
| `KEYCLOAK_ADMIN_USERNAME`                                   | Usuario administrador de Keycloak        | `admin`                                |
| `KEYCLOAK_ADMIN_PASSWORD`                                   | Contraseña administrador de Keycloak     | `admin`                                |

### Frontend

| Variable        | Descripción              | Valor por defecto          |
|-----------------|--------------------------|----------------------------|
| `VITE_API_URL`  | URL base del backend     | `http://localhost:8080/api` |

---

## Endpoints principales

| Método | Endpoint                                        | Descripción                         | Rol requerido   |
|--------|-------------------------------------------------|-------------------------------------|-----------------|
| GET    | `/api/citas/paciente/{id}`                      | Citas de un paciente                | PACIENTE        |
| GET    | `/api/citas/medico/{medicoId}/fecha/{fecha}`    | Citas de un médico en una fecha     | ADMIN, MEDICO   |
| POST   | `/api/citas`                                    | Crear nueva cita                    | PACIENTE        |
| PUT    | `/api/citas/{id}/reagendar`                     | Reagendar una cita                  | PACIENTE        |
| DELETE | `/api/citas/{id}`                               | Cancelar una cita                   | ADMIN, PACIENTE |
| GET    | `/api/citas/{medicoId}/franjas`                 | Franjas horarias disponibles        | PUBLIC          |
| GET    | `/api/medicos`                                  | Listar todos los médicos            | PUBLIC          |
| POST   | `/api/medicos`                                  | Crear médico                        | ADMIN           |
| PUT    | `/api/medicos/{id}/estado`                      | Activar / desactivar médico         | ADMIN           |
| GET    | `/api/pacientes/username/{username}`            | Perfil por username de Keycloak     | PACIENTE        |
| GET    | `/api/configuracion`                            | Configuración del sistema           | ADMIN           |
| PUT    | `/api/configuracion/ventana`                    | Actualizar ventana de agendamiento  | ADMIN           |

> Todos los endpoints protegidos requieren el header `Authorization: Bearer <token>` con un JWT válido emitido por Keycloak.

---

## Autenticación

El sistema usa **Keycloak** como proveedor de identidad con OAuth 2.0 / OIDC. El realm `piedrazul` se importa automáticamente al levantar Docker Compose desde `piedraAzul-back/keycloak/piedrazul-realm.json`.

**Roles disponibles:**

- `ADMIN` — acceso completo al sistema
- `MEDICO` — consulta de su agenda y citas asignadas
- `PACIENTE` — agendamiento y consulta de sus propias citas

Para obtener un token manualmente en desarrollo:

```bash
curl -X POST http://localhost:8180/realms/piedrazul/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=piedrazul-back" \
  -d "username=TU_USUARIO" \
  -d "password=TU_PASSWORD"
```

---

## Equipo

| Nombre            | Rol                       |
|-------------------|---------------------------|
| Santiago Bastidas | Backend / Base de datos   |
| Sofia Cortes      | Frontend / Integración    |
| Jose Rodriguez    | Backend / Base de datos   |
| Glenn Ward        | Frontend / Integración    |

---

> Ingeniería de Software III · Universidad del Cauca · 2026
