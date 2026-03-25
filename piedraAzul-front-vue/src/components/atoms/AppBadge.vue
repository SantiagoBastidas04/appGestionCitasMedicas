<template>
  <span :class="['badge', `badge-${estado}`]">{{ texto }}</span>
</template>

<script setup>
defineProps({
  estado: { type: String, required: true }, // CONFIRMADA | CANCELADA | COMPLETADA | activo | inactivo
  texto:  { type: String, required: true },
})
</script>

<style scoped>
.badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: .02em;
}
.badge-CONFIRMADA { background: #E0F4EC; color: #0D6B4E; }
.badge-CANCELADA  { background: var(--rojo-claro); color: var(--rojo); }
.badge-COMPLETADA { background: var(--azul-claro); color: var(--azul); }
.badge-activo     { background: #E0F4EC; color: #0D6B4E; }
.badge-inactivo   { background: var(--crema-oscuro); color: var(--texto-tenue); }
</style>
