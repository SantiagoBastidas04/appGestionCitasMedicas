# Guía de Despliegue — Desarrollo Local vs Producción en Railway

## 📋 Resumen

El proyecto tiene **un único `docker-compose.yml`** que funciona en ambos entornos:

- **Desarrollo Local** → `docker compose --profile local up` (PostgreSQL local)
- **Producción en Railway** → `docker compose up` (PostgreSQL en Railway)

La magia está en `profiles: ["local"]` — solo se levanta PostgreSQL si lo especificas.

---

## ✅ Desarrollo Local

### Paso 1: Configurar variables de entorno

Copia el archivo de ejemplo:
```bash
cp .env.example .env
```

El `.env` ya tiene valores por defecto para desarrollo local. **No es necesario modificarlo** salvo que cambies puertos o credenciales.

### Paso 2: Levantar con Docker Compose

```bash
docker compose --profile local up --build
```

Este comando inicia:
- ✅ **PostgreSQL** (contenedor local, acceso en `localhost:5432`) — `--profile local` lo habilita
- ✅ **Keycloak** (en `localhost:8180`)
- ✅ **Backend** (en `localhost:8080`)
- ✅ **Frontend** (en `localhost:5173`)

**Nota:** El `--profile local` es importante. Sin él, PostgreSQL no se levanta, pero Keycloak, Backend y Frontend sí.

Para detener todo:
```bash
docker compose --profile local down
```

### Paso 3: Verificar conexión a BD

El backend **automáticamente** creará las tablas en PostgreSQL (por el `ddl-auto=update` en `application.properties`).

Para ver la BD manualmente:
```bash
docker exec -it db-piedrazul psql -U admin -d piedrazul
```

---

## 🚀 Producción en Railway

### ⚠️ IMPORTANTE

Railway **automáticamente** ejecuta `docker compose up --build`. El `--profile local` NO se incluye, por lo que:

- ❌ PostgreSQL local **NO se levanta**
- ✅ El backend conecta a PostgreSQL en Railway usando variables de entorno

### Paso 1: Configurar variables en Railway

En el panel de Railway, establece estas variables de entorno **antes de redeploy**:

```
DATABASE_URL=jdbc:postgresql://HOST:PUERTO/NOMBRE_BD
DB_USERNAME=usuario_railway
DB_PASSWORD=password_railway
VITE_API_URL=https://tu-dominio.com/api
VITE_KEYCLOAK_URL=https://tu-dominio-keycloak.com
VITE_KEYCLOAK_SECRET=tu-secret-produccion
```

### Paso 2: Hacer push y redeploy

```bash
git push origin main
```

Railway detectará los cambios y hará redeploy automáticamente. **No necesitas hacer nada más.**

---

## 🔑 Diferencias clave

| Aspecto | Local (`--profile local`) | Railway (`sin profile`) |
|--------|-----|-----|
| **BD PostgreSQL** | ✅ Contenedor local | ❌ No (usa Railway) |
| **Volumen persistente** | ✅ `postgres_data` | ❌ No aplica |
| **Variables de BD** | `${DB_HOST:-db}` (apunta al contenedor) | `${DATABASE_URL}` (apunta a Railway) |
| **Inicialización automática** | ✅ Crea tablas al inicio | ✅ Solo conecta si ya existen |
| **Comando a ejecutar** | `docker compose --profile local up` | `docker compose up` |

---

## 🐛 Troubleshooting

### Error: "Connection refused" al intentar conectar a PostgreSQL

**Causa**: Olvidaste incluir `--profile local` al ejecutar docker compose.

**Solución**: 
```bash
# Correcto
docker compose --profile local up --build

# Incorrecto (sin profile)
docker compose up --build
```

### El backend intenta conectarse a `db` en lugar de Railway

**Causa**: Las variables de entorno `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` no están configuradas.

**Solución**: En Railway, configura estas variables en el panel antes de redeploy.

### Error: "pg_isready" fallando en desarrollo local

### Las tablas no se crean automáticamente en Railway

**Causa**: `spring.jpa.hibernate.ddl-auto=update` requiere que Hibernate tenga permisos.

**Solución**: Asegúrate de que el usuario de Railway tiene permisos de `ALTER TABLE` y `CREATE TABLE`.

---

## 📝 Notas importantes

1. **Nunca subas `.env` a git** — Ya está en `.gitignore`
2. **`.env.example` es público** — Contiene solo valores de ejemplo/desarrollo
3. **En Railway, las variables se configuran en el panel** — No hay un `.env` físico
4. **PostgreSQL en producción es más confiable que H2** — La migración está completa

---

## ✨ Resumen para el equipo

**Para trabajar localmente:**
```bash
# Solo ejecuta esto una vez
cp .env.example .env

# Luego para levantar el proyecto (con PostgreSQL local)
docker compose --profile local up --build
```

**Para desplegar en Railway:**
- Railway automáticamente ejecuta `docker compose up --build`
- El `--profile local` NO está incluido, por lo que PostgreSQL no se levanta
- El backend conecta a PostgreSQL en Railway usando `DATABASE_URL`, `DB_USERNAME` y `DB_PASSWORD`
- Configura estas variables en el panel de Railway
