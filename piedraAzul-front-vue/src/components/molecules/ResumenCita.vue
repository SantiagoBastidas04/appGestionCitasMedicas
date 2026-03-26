<template>
  <div v-if="nombreMedico && fecha && hora" class="resumen-cita">
    <h4>Resumen de la cita</h4>
    <div class="resumen-fila">
      <span class="et">Profesional</span>
      <span class="val">{{ nombreMedico }}</span>
    </div>
    <div class="resumen-fila">
      <span class="et">Fecha</span>
      <span class="val">{{ fechaFormato }}</span>
    </div>
    <div class="resumen-fila">
      <span class="et">Hora</span>
      <span class="val">{{ hora }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  nombreMedico: { type: String, default: '' },
  fecha:        { type: String, default: '' },
  hora:         { type: String, default: '' },
})

const fechaFormato = computed(() => {
  if (!props.fecha) return ''
  return new Date(props.fecha + 'T00:00:00')
    .toLocaleDateString('es-CO', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })
})
</script>

<style scoped>
.resumen-cita {
  background: var(--verde-claro);
  border: 1px solid var(--verde-borde);
  border-radius: var(--radio-lg);
  padding: 18px 22px;
  margin-top: 18px;
}
.resumen-cita h4 {
  font-size: 13px;
  font-weight: 700;
  color: var(--verde-oscuro);
  text-transform: uppercase;
  letter-spacing: .06em;
  margin-bottom: 12px;
}
.resumen-fila {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 14px;
  border-bottom: 1px solid var(--verde-borde);
}
.resumen-fila:last-child { border-bottom: none; }
.resumen-fila .et  { color: var(--texto-suave); }
.resumen-fila .val { font-weight: 600; color: var(--verde-oscuro); }
</style>
