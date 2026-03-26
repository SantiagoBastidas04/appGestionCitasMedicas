/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useMedicos.js
   Estado global de médicos (compartido entre vistas)
═══════════════════════════════════════════ */

import { ref } from 'vue'
import { medicoService } from '../services/api.js'

const medicos = ref([])
const cargando = ref(false)

export function useMedicos() {
  async function cargarMedicos() {
    cargando.value = true
    try {
      medicos.value = await medicoService.getAll()
    } finally {
      cargando.value = false
    }
  }

  function formatEsp(e) {
    return { FISIOTERAPIA: 'Fisioterapia', TERAPIA_NEURAL: 'Terapia Neural', QUIROPRAXIA: 'Quiropraxia' }[e] || e
  }

  return { medicos, cargando, cargarMedicos, formatEsp }
}
