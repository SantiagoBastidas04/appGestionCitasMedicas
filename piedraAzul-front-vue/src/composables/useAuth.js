/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useAuth.js
   Autenticación delegada a Keycloak
═══════════════════════════════════════════ */

import { ref, computed, watch } from 'vue'

// Helper para obtener usuario de localStorage
function obtenerUsuarioGuardado() {
  try {
    const guardado = localStorage.getItem('usuario')
    return guardado ? JSON.parse(guardado) : null
  } catch {
    return null
  }
}

// Crear el ref con el usuario guardado
const usuario = ref(obtenerUsuarioGuardado())
// usuario tiene forma: { token, username, rol, nombres, apellidos }

// Watcher para guardar cambios en localStorage
watch(usuario, (nuevoUsuario) => {
  if (nuevoUsuario) {
    localStorage.setItem('usuario', JSON.stringify(nuevoUsuario))
  } else {
    localStorage.removeItem('usuario')
  }
}, { deep: true })

const sesionActiva = computed(() => !!usuario.value)
const esPaciente = computed(() => usuario.value?.rol === 'PACIENTE')

// ── Configuración de Keycloak ─────────────────────────────────────────────
const KEYCLOAK_URL = import.meta.env.VITE_KEYCLOAK_URL ?? 'https://keycloak-production-8f01.up.railway.app'
const KEYCLOAK_REALM = import.meta.env.VITE_KEYCLOAK_REALM ?? 'piedrazul'
const KEYCLOAK_CLIENT = import.meta.env.VITE_KEYCLOAK_CLIENT ?? 'piedrazul-api'
const KEYCLOAK_SECRET = import.meta.env.VITE_KEYCLOAK_SECRET ?? 'lU5PljMWMfq0sM9rTIJeNFgjvYVHGQDa'
const API = import.meta.env.VITE_API_URL ?? 'https://appgestioncitasmedicas-production.up.railway.app/api'
// ─────────────────────────────────────────────────────────────────────────

function normalizeUsername(value) {
  return (value || '').trim().toLowerCase()
}

export function useAuth() {

  async function login(username, password) {
    // 1. Pedir token directamente a Keycloak
    const normalizedUsername = normalizeUsername(username)
    const params = new URLSearchParams()
    params.append('grant_type', 'password')
    params.append('client_id', KEYCLOAK_CLIENT)
    params.append('client_secret', KEYCLOAK_SECRET)
    params.append('username', normalizedUsername)
    params.append('password', password)

    const res = await fetch(
      `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token`,
      { method: 'POST', body: params }
    )

    if (!res.ok) {
      let payload = null
      try {
        payload = await res.json()
      } catch {
        payload = null
      }

      if (payload?.error === 'invalid_grant') {
        throw new Error('Usuario o contraseña incorrectos')
      }
      if (payload?.error === 'invalid_client') {
        throw new Error('Cliente de Keycloak inválido')
      }

      throw new Error(payload?.error_description || payload?.error || 'Error de autenticación')
    }

    const data = await res.json()
    const accessToken = data.access_token

    // 2. Decodificar el JWT para sacar username, rol, nombres
    const payload = JSON.parse(atob(accessToken.split('.')[1]))

    usuario.value = {
      token: accessToken,
      username: payload.preferred_username,
      rol: payload.roles?.find(r =>
        ['ADMINISTRADOR', 'AGENDADOR', 'MEDICO_TERAPISTA', 'PACIENTE'].includes(r)
      ) || null,   // primer rol asignado
      nombres: payload.given_name || payload.preferred_username,
      apellidos: payload.family_name || '',
    }
  }

  async function registro(form) {
    // El registro va al backend de Spring Boot (que a su vez crea en Keycloak)
    const payload = {
      ...form,
      username: normalizeUsername(form.username),
    }

    const res = await fetch(`${API}/auth/registro`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })

    if (!res.ok) {
      let payload = null
      try {
        payload = await res.json()
      } catch {
        payload = null
      }

      if (res.status === 409) {
        const err = new Error(payload?.mensaje || 'El usuario o documento ya existe')
        err.code = 'DUPLICATE'
        throw err
      }
      if (res.status === 400) {
        const err = new Error(payload?.mensaje || 'Datos inválidos')
        err.code = 'VALIDATION'
        err.fieldErrors = Array.isArray(payload?.campos) ? payload.campos : []
        throw err
      }

      throw new Error(payload?.mensaje || 'Error al registrar usuario')
    }

    // Login automático tras registro exitoso
    await login(payload.username, form.password)
  }

  function cerrarSesion() {
    usuario.value = null
    localStorage.removeItem('usuario')
  }

  const avatarLetra = computed(() => {
    if (!usuario.value) return 'U'
    return (usuario.value.nombres?.[0] || usuario.value.username?.[0] || 'U').toUpperCase()
  })

  const nombreCompleto = computed(() => {
    if (!usuario.value) return ''
    return usuario.value.nombres
      ? `${usuario.value.nombres} ${usuario.value.apellidos || ''}`.trim()
      : usuario.value.username
  })

  const rolLabel = computed(() => {
    const ROLES = {
      ADMINISTRADOR: 'Administrador',
      MEDICO_TERAPISTA: 'Médico',
      AGENDADOR: 'Agendador',
      PACIENTE: 'Paciente',
    }
    return ROLES[usuario.value?.rol] || usuario.value?.rol || ''
  })

  return {
    usuario,
    sesionActiva,
    esPaciente,
    avatarLetra,
    nombreCompleto,
    rolLabel,
    login,
    registro,
    cerrarSesion,
  }
}