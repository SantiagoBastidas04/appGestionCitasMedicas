<template>
  <div>
    <!-- Cargando -->
    <div v-if="cargando" class="estado-msg">
      <AppSpinner :verde="true" :size="16" />
      Consultando disponibilidad...
    </div>

    <!-- Pendiente de selección -->
    <AppAlerta v-else-if="!medicoId || !fecha" tipo="aviso" titulo="Seleccione médico y fecha">
      Las franjas disponibles aparecerán aquí
    </AppAlerta>

    <!-- Sin disponibilidad -->
    <AppAlerta v-else-if="!cargando && franjas.length === 0" tipo="error" titulo="Sin disponibilidad">
      El profesional no atiende este día o no tiene franjas libres
    </AppAlerta>

    <!-- Error -->
    <AppAlerta v-else-if="error" tipo="error" titulo="Error al cargar franjas">
      {{ error }}
    </AppAlerta>

    <!-- Franjas disponibles -->
    <div v-else class="franjas-grid">
      <button
        v-for="hora in franjas"
        :key="hora"
        :class="['franja', { sel: hora === seleccionada }]"
        @click="$emit('seleccionar', hora)"
      >
        {{ hora }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { watch, ref } from 'vue'
import AppSpinner from '../atoms/AppSpinner.vue'
import AppAlerta  from '../atoms/AppAlerta.vue'
import { citaService } from '../../services/api.js'

const props = defineProps({
  medicoId:    { default: null },
  fecha:       { type: String, default: '' },
  seleccionada:{ type: String, default: null },
})
defineEmits(['seleccionar'])

const franjas  = ref([])
const cargando = ref(false)
const error    = ref('')

watch([() => props.medicoId, () => props.fecha], async ([mId, f]) => {
  franjas.value = []
  error.value   = ''
  if (!mId || !f) return

  cargando.value = true
  try {
    franjas.value = await citaService.getFranjas(mId, f)
  } catch (e) {
    error.value = e.message
  } finally {
    cargando.value = false
  }
})
</script>

<style scoped>
.estado-msg {
  padding: 12px 0;
  color: var(--texto-tenue);
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.franjas-grid { display: flex; flex-wrap: wrap; gap: 10px; margin-top: 12px; }
.franja {
  padding: 10px 18px;
  border: 1.5px solid var(--borde);
  border-radius: var(--radio-md);
  font-size: 15px;
  font-weight: 600;
  background: var(--blanco);
  color: var(--texto);
  transition: all var(--trans);
  font-family: 'DM Sans', monospace;
}
.franja:hover {
  border-color: var(--verde-medio);
  background: var(--verde-claro);
  color: var(--verde-oscuro);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(13,107,78,.12);
}
.franja.sel {
  border-color: var(--verde);
  background: var(--verde);
  color: var(--blanco);
  box-shadow: 0 2px 10px rgba(13,107,78,.30);
}
</style>
