/* ═══════════════════════════════════════════
   PIEDRAZUL — composables/useToast.js
═══════════════════════════════════════════ */

import { ref } from 'vue'

const mensaje = ref('')
const tipo    = ref('ok')
const visible = ref(false)
let timer = null

export function useToast() {
  function toast(msg, t = 'ok') {
    mensaje.value = msg
    tipo.value    = t
    visible.value = true
    clearTimeout(timer)
    timer = setTimeout(() => { visible.value = false }, 3600)
  }

  return { mensaje, tipo, visible, toast }
}
