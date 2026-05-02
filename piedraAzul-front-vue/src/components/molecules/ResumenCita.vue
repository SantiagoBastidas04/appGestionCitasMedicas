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
      <span class="val">{{ horaFormato }}</span>
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

// Quita los segundos si vienen en formato HH:MM:SS
const horaFormato = computed(() => {
  if (!props.hora) return ''
  const partes = props.hora.split(':')
  return partes.length >= 2 ? `${partes[0]}:${partes[1]}` : props.hora
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
  align-items: baseline;
  gap: 8px;
  padding: 8px 0;
  font-size: 14px;
  border-bottom: 1px solid var(--verde-borde);
}

.resumen-fila:last-child {
  border-bottom: none;
}

.resumen-fila .et {
  color: var(--texto-suave);
  white-space: nowrap;   /* la etiqueta nunca se parte */
  flex-shrink: 0;
}

.resumen-fila .val {
  font-weight: 600;
  color: var(--verde-oscuro);
  text-align: right;
}

/* ── Responsive: pantallas pequeñas ─────────────────────── */
@media (max-width: 400px) {
  .resumen-cita {
    padding: 14px 16px;
  }

  .resumen-fila {
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
    padding: 10px 0;
  }

  .resumen-fila .et {
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: .04em;
    color: var(--texto-tenue);
  }

  .resumen-fila .val {
    font-size: 15px;
    text-align: left;
  }
}
</style>