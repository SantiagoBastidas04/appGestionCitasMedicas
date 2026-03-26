<template>
  <button
    :class="['btn', `btn-${variante}`, { 'btn-sm': small }]"
    :disabled="loading || disabled"
    @click="$emit('click')"
  >
    <span v-if="loading" class="spinner" :class="{ 'spinner-verde': variante !== 'primario' }"></span>
    <slot />
  </button>
</template>

<script setup>
defineProps({
  variante: { type: String, default: 'primario' }, // primario | contorno | peligro
  loading:  { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  small:    { type: Boolean, default: false },
})
defineEmits(['click'])
</script>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  padding: 11px 24px;
  font-size: 14px;
  font-weight: 600;
  border-radius: var(--radio-md);
  border: 1.5px solid transparent;
  transition: background var(--trans), transform var(--trans), box-shadow var(--trans);
  letter-spacing: .01em;
  white-space: nowrap;
}
.btn:active { transform: scale(.975); }
.btn:disabled { opacity: .5; cursor: not-allowed; pointer-events: none; }

.btn-primario {
  background: var(--verde);
  color: var(--blanco);
  border-color: var(--verde);
  box-shadow: 0 2px 8px rgba(13,107,78,.25);
}
.btn-primario:hover { background: var(--verde-oscuro); border-color: var(--verde-oscuro); box-shadow: 0 4px 14px rgba(13,107,78,.30); }

.btn-contorno {
  background: transparent;
  color: var(--verde);
  border-color: var(--verde-borde);
}
.btn-contorno:hover { background: var(--verde-claro); border-color: var(--verde-medio); }

.btn-peligro {
  background: var(--rojo-claro);
  color: var(--rojo);
  border-color: var(--rojo-borde);
}
.btn-peligro:hover { background: #fae0e0; }

.btn-sm { padding: 7px 16px; font-size: 13px; }

.spinner {
  display: inline-block;
  width: 16px; height: 16px;
  border: 2.5px solid rgba(255,255,255,.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin .7s linear infinite;
  flex-shrink: 0;
}
.spinner-verde {
  border-color: rgba(13,107,78,.2);
  border-top-color: var(--verde);
}
@keyframes spin { to { transform: rotate(360deg); } }
</style>
