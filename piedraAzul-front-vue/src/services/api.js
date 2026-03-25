/* ═══════════════════════════════════════════
   PIEDRAZUL — services/api.js
   Capa de comunicación con el backend
   Spring Boot en localhost:8080
═══════════════════════════════════════════ */

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

// ── Médicos ──────────────────────────────
export const medicoService = {
  getAll:       ()           => http('/medicos'),
  getById:      (id)         => http(`/medicos/${id}`),
  create:       (body)       => http('/medicos', { method: 'POST', body: JSON.stringify(body) }),
  toggleEstado: (id, activo) => http(`/medicos/${id}/estado?activo=${activo}`, { method: 'PUT' }),
}

// ── Pacientes ────────────────────────────
export const pacienteService = {
  getByDocumento: (doc)  => http(`/pacientes/documento/${doc}`),
  create:         (body) => http('/pacientes', { method: 'POST', body: JSON.stringify(body) }),
}

// ── Citas ────────────────────────────────
export const citaService = {
  getByMedicoFecha: (medicoId, fecha) => http(`/citas/medico/${medicoId}/fecha/${fecha}`),
  getFranjas:       (medicoId, fecha) => http(`/citas/${medicoId}/franjas?fecha=${fecha}`),
  create:           (body)            => http('/citas', { method: 'POST', body: JSON.stringify(body) }),
  cancel:           (id)              => http(`/citas/${id}`, { method: 'DELETE' }),
}
