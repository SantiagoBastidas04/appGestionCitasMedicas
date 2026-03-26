/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useFormValidation.js
═══════════════════════════════════════════ */

import { ref } from 'vue'

export function useFormValidation() {
  const errores = ref({})

  function marcarError(campo, msg) {
    errores.value[campo] = msg
  }

  function limpiarError(campo) {
    delete errores.value[campo]
  }

  function limpiarErrores(...campos) {
    campos.forEach(c => delete errores.value[c])
  }

  function limpiarTodos() {
    errores.value = {}
  }

  function tieneError(campo) {
    return !!errores.value[campo]
  }

  function mensajeError(campo) {
    return errores.value[campo] || ''
  }

  return { errores, marcarError, limpiarError, limpiarErrores, limpiarTodos, tieneError, mensajeError }
}
