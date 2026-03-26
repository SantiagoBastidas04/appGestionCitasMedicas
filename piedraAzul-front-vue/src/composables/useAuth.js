/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useAuth.js
   Estado global de sesión del usuario
═══════════════════════════════════════════ */

import { ref, computed } from 'vue'

const usuario  = ref(null)   // { username, nombres, apellidos, rol, id }
const sesionActiva = computed(() => !!usuario.value)

// ── Servicios de auth (apuntan a Spring Boot) ──
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
    const data = await http('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    })
    // Se espera: { id, username, nombres, apellidos, rol }
    usuario.value = data
  }

  async function registro(form) {
    // Solo pacientes se auto-registran
    const data = await http('/auth/registro', {
      method: 'POST',
      body: JSON.stringify(form),
    })
    usuario.value = data
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
      ADMIN:      'Administrador',
      MEDICO:     'Médico',
      AGENDADOR:  'Agendador',
      PACIENTE:   'Paciente',
    }
    return ROLES[usuario.value?.rol] || usuario.value?.rol || ''
  })

  return {
    usuario,
    sesionActiva,
    avatarLetra,
    nombreCompleto,
    rolLabel,
    login,
    registro,
    cerrarSesion,
  }
}
