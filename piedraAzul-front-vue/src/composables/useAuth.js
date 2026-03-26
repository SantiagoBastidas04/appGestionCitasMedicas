/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useAuth.js
   Estado global de sesión del usuario
═══════════════════════════════════════════ */

import { ref, computed } from 'vue'

const usuario = ref(null)
// usuario tiene forma: { token, username, rol, nombres, apellidos }

const sesionActiva = computed(() => !!usuario.value)
const esPaciente   = computed(() => usuario.value?.rol === 'PACIENTE')

const API = 'http://localhost:8080/api'

async function http(url, opts = {}) {
  const res = await fetch(API + url, {
    headers: { 'Content-Type': 'application/json', ...(opts.headers || {}) },
    ...opts,
  })
  if (!res.ok) {
    const txt = await res.text().catch(() => '')
    throw new Error(txt || `Error ${res.status}`)
  }
  const ct = res.headers.get('Content-Type') || ''
  return ct.includes('json') ? res.json() : null
}

export function useAuth() {

  async function login(username, password) {
    // LoginResponse: { token, username, rol, nombres, apellidos }
    const data = await http('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    })
    usuario.value = data
  }

  async function registro(form) {
    // Registro devuelve PacienteResponse (sin token), así que hacemos login después
    await http('/auth/registro', {
      method: 'POST',
      body: JSON.stringify(form),
    })
    // Login automático tras registro exitoso
    await login(form.username, form.password)
  }

  function cerrarSesion() {
    usuario.value = null
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
      ADMINISTRADOR:    'Administrador',
      MEDICO_TERAPISTA: 'Médico',
      AGENDADOR:        'Agendador',
      PACIENTE:         'Paciente',
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
