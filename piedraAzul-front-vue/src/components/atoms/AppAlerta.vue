<template>
  <div :class="['alerta', `alerta-${tipo}`]">
    <span class="alerta-icono">{{ icono }}</span>
    <div>
      <strong v-if="titulo">{{ titulo }}</strong>
      <span v-if="$slots.default"><slot /></span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  tipo:   { type: String, default: 'info' }, // info | aviso | error
  titulo: { type: String, default: '' },
  icono:  { type: String, default: '' },
})

const icono = computed(() => {
  if (props.icono) return props.icono
  return { info: '✓', aviso: '⚠', error: '✕' }[props.tipo] || 'ℹ'
})
</script>

<style scoped>
.alerta {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 18px;
  border-radius: var(--radio-md);
  font-size: 14px;
  margin-bottom: 18px;
  border: 1px solid;
}
.alerta-icono { font-size: 18px; flex-shrink: 0; line-height: 1.4; }
.alerta strong { display: block; font-size: 14px; margin-bottom: 2px; }
.alerta span   { font-size: 13px; line-height: 1.5; }

.alerta-info  { background: var(--verde-claro); color: #084D38; border-color: var(--verde-borde); }
.alerta-aviso { background: var(--amarillo-claro); color: var(--amarillo); border-color: var(--amarillo-borde); }
.alerta-error { background: var(--rojo-claro); color: var(--rojo); border-color: var(--rojo-borde); }
</style>
