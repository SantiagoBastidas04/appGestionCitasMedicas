<template>
  <nav class="nav-tabs">
    <button
      v-for="tab in tabs"
      :key="tab.id"
      :class="['nav-tab', { active: activo === tab.id }]"
      @click="$emit('cambiar', tab.id)"
    >
      <svg class="tab-icon" fill="none" viewBox="0 0 24 24" v-html="tab.icono" />
      {{ tab.label }}
      <span v-if="tab.proto" class="proto-pill">Prototipo</span>
    </button>
  </nav>
</template>

<script setup>
defineProps({
  activo: { type: String, required: true },
  tabs:   { type: Array,  required: true },
})
defineEmits(['cambiar'])
</script>

<style scoped>
.nav-tabs {
  background: var(--blanco);
  border-bottom: 1.5px solid var(--borde);
  display: flex;
  padding: 0 28px;
  overflow-x: auto;
  scrollbar-width: none;
}
.nav-tabs::-webkit-scrollbar { display: none; }
.nav-tab {
  padding: 0 22px;
  height: 52px;
  font-size: 14px;
  font-weight: 500;
  color: var(--texto-suave);
  background: none;
  border: none;
  border-bottom: 2.5px solid transparent;
  margin-bottom: -1.5px;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 7px;
  transition: color var(--trans), border-color var(--trans);
}
.nav-tab:hover { color: var(--verde); }
.nav-tab.active { color: var(--verde-oscuro); border-bottom-color: var(--verde); font-weight: 600; }
.tab-icon { width: 16px; height: 16px; opacity: .7; }
.nav-tab.active .tab-icon { opacity: 1; }
.proto-pill {
  background: var(--amarillo-claro);
  color: var(--amarillo);
  border: 1px solid var(--amarillo-borde);
  border-radius: 20px;
  padding: 1px 8px;
  font-size: 10px;
  font-weight: 700;
  letter-spacing: .04em;
}
@media (max-width: 680px) { .nav-tab { padding: 0 14px; font-size: 13px; } }
</style>
