# Piedrazul — Ejecucion Backend con Docker

De esta forma se levanta Keycloak y el backend con un solo comando. No necesitas tener Java ni Maven instalados.

**1. Levanta los servicios:**

```bash
docker-compose up -d
```

Esto levanta automáticamente:
- **Keycloak** en `http://localhost:8180` — con el realm `piedrazul` ya configurado
- **Backend** en `http://localhost:8080` — con la BD H2 poblada con datos de prueba
  Espera unos 30 segundos mientras los servicios arrancan completamente.

**2. Verifica que todo esté corriendo:**

```bash
docker-compose ps
```

Debes ver los dos contenedores con estado `Up`.

**3. Para detener los servicios:**

```bash
docker-compose down
```
 
---
**4. Ya podras hacer uso de las funcionalidades del backend**
## Opción B — Correr el backend sin Docker

Usa esta opción si prefieres correr el backend directamente en tu máquina para desarrollo.

**1. Levanta solo Keycloak con Docker:**

```bash
docker-compose up -d keycloak
```

Espera ~30 segundos.

**2. Ejecuta el backend desde el editor de codigo (Visual Studio Code o IntelliJ)**


El backend queda disponible en `http://localhost:8080`.
 
---
**3. Ya podras hacer uso de las funcionalidades del backend**
