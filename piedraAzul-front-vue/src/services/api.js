/* ═══════════════════════════════════════════
   PIEDRAZUL — services/api.js
   Capa de comunicación con el backend
   Spring Boot en localhost:8080
═══════════════════════════════════════════ */

import { useAuth } from '../composables/useAuth.js'

const API = 'http://localhost:8080/api'

function getToken() {
  const { usuario } = useAuth()
  return usuario.value?.token || null
}

async function http(url, opts = {}) {
  const token = getToken()

  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(opts.headers || {}),
  }

  const res = await fetch(API + url, { ...opts, headers })

  if (!res.ok) {
    const txt = await res.text().catch(() => '')
    throw new Error(txt || `Error ${res.status}`)
  }
  const ct = res.headers.get('Content-Type') || ''
  return ct.includes('json') ? res.json() : null
}

// ── Médicos ──────────────────────────────
export const medicoService = {
  getAll:           ()               => http('/medicos'),
  getById:          (id)             => http(`/medicos/${id}`),
  getByEspecialidad:(especialidad)   => http(`/medicos/especialidad/${especialidad}`),
  create:           (body)           => http('/medicos', { method: 'POST', body: JSON.stringify(body) }),
  update:           (id, body)       => http(`/medicos/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  toggleEstado:     (id, activo)     => http(`/medicos/${id}/estado?activo=${activo}`, { method: 'PUT' }),
}

// ── Pacientes ────────────────────────────
export const pacienteService = {
  getByDocumento: (doc)      => http(`/pacientes/documento/${doc}`),
  getByUsername:  (username) => http(`/pacientes/username/${username}`),
  create:         (body)     => http('/pacientes', { method: 'POST', body: JSON.stringify(body) }),
  update:         (id, body) => http(`/pacientes/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
}

// ── Citas ────────────────────────────────
export const citaService = {
  getByMedicoFecha: (medicoId, fecha) => http(`/citas/medico/${medicoId}/fecha/${fecha}`),
  getByPaciente:    (pacienteId)      => http(`/citas/paciente/${pacienteId}`),
  getFranjas:       (medicoId, fecha) => http(`/citas/${medicoId}/franjas?fecha=${fecha}`),
  create:           (body)            => http('/citas', { method: 'POST', body: JSON.stringify(body) }),
  reagendar:        (id, body)        => http(`/citas/${id}/reagendar`, { method: 'PUT', body: JSON.stringify(body) }),
  cancel:           (id)              => http(`/citas/${id}`, { method: 'DELETE' }),
}

// ── Configuración ─────────────────────────
export const configuracionService = {
  get:        ()         => http('/configuracion'),
  setVentana: (semanas)  => http('/configuracion/ventana', { method: 'PUT', body: JSON.stringify({ ventanaSemanas: semanas }) }),
  setMedico:  (id, body) => http(`/configuracion/medico/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
}