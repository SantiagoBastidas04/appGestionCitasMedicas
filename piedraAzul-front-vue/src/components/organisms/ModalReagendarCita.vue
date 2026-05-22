<template>
  <div :class="['modal-bg', { abierto: visible }]" @click.self="$emit('cerrar')">
    <div class="modal">
      <div class="modal-header">
        <h2>Reagendar cita</h2>
        <p>Actualice la fecha y la hora de la cita seleccionada</p>
      </div>

      <div v-if="cita" class="detalle-actual">
        <div>
          <span class="k">Paciente</span>
          <span>{{ cita.nombrePaciente }}</span>
        </div>
        <div>
          <span class="k">Fecha actual</span>
          <span>{{ cita.fecha }}</span>
        </div>
        <div>
          <span class="k">Hora actual</span>
          <span>{{ cita.hora }}</span>
        </div>
      </div>

      <AppAlerta v-if="error" tipo="error" titulo="No se pudo reagendar">
        {{ error }}
      </AppAlerta>

      <div class="form-grid">
        <AppCampo
          label="Nueva fecha"
          tipo="date"
          :modelValue="fecha"
          :errorMsg="errores?.fecha"
          @update:modelValue="$emit('update:fecha', $event)"
        />
      </div>

      <div class="campo form-full" style="margin-top:16px;">
        <label>
          Nueva hora
          <span v-if="errores?.hora" class="err-inline">— {{ errores.hora }}</span>
        </label>
        <FranjasHorarias
          :medicoId="cita?.medicoId"
          :fecha="fecha"
          :seleccionada="hora"
          @seleccionar="h => $emit('update:hora', h)"
        />
      </div>

      <div class="modal-footer">
        <AppButton @click="$emit('confirmar')" :loading="loading">Reagendar cita</AppButton>
        <AppButton variante="contorno" @click="$emit('cerrar')">Cancelar</AppButton>
      </div>
    </div>
  </div>
</template>

<script setup>
import AppCampo from '../atoms/AppCampo.vue'
import AppButton from '../atoms/AppButton.vue'
import AppAlerta from '../atoms/AppAlerta.vue'
import FranjasHorarias from '../molecules/FranjasHorarias.vue'

defineProps({
  visible: { type: Boolean, default: false },
  cita: { type: Object, default: null },
  fecha: { type: String, default: '' },
  hora: { type: String, default: null },
  errores: { type: Object, default: () => ({}) },
  error: { type: String, default: '' },
  loading: { type: Boolean, default: false },
})

defineEmits(['cerrar', 'confirmar', 'update:fecha', 'update:hora'])
</script>

<style scoped>
.modal-bg {
  display: none;
  position: fixed;
  inset: 0;
  background: rgba(20,30,25,.45);
  backdrop-filter: blur(2px);
  z-index: 300;
  align-items: center;
  justify-content: center;
  padding: 24px;
}
.modal-bg.abierto { display: flex; }
.modal {
  background: var(--blanco);
  border-radius: var(--radio-xl);
  padding: 32px;
  width: 100%;
  max-width: 560px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0,0,0,.20);
  animation: modalIn .22s ease;
}

@keyframes modalIn {
  from { opacity: 0; transform: scale(.95) translateY(8px); }
  to   { opacity: 1; transform: scale(1) translateY(0); }
}

.modal-header { margin-bottom: 18px; }
.modal-header h2 {
  font-family: 'DM Serif Display', serif;
  font-size: 22px;
  font-weight: 400;
  color: var(--verde-oscuro);
}
.modal-header p { font-size: 13px; color: var(--texto-tenue); margin-top: 4px; }

.detalle-actual {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
  padding: 12px 14px;
  border: 1px solid var(--borde);
  border-radius: var(--radio-md);
  background: var(--crema);
  margin-bottom: 16px;
  font-size: 13px;
}
.detalle-actual .k {
  display: block;
  color: var(--texto-tenue);
  font-size: 11px;
  letter-spacing: .03em;
  text-transform: uppercase;
  margin-bottom: 2px;
}

.err-inline { color: var(--rojo); font-weight: 600; font-size: 12px; }

.modal-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 22px;
}

@media (max-width: 560px) {
  .modal { padding: 24px 18px; }
  .modal-footer { flex-direction: column; }
  .modal-footer > * { width: 100%; }
  .detalle-actual { grid-template-columns: 1fr; }
}
</style>
