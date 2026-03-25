<template>
  <div class="card">
    <div v-if="titulo" class="card-titulo">
      <slot name="icono" />
      {{ titulo }}
    </div>
    <div v-if="subtitulo" class="card-sub">{{ subtitulo }}</div>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  titulo:   { type: String, default: '' },
  subtitulo: { type: String, default: '' },
})
</script>

<style scoped>
.card {
  background: var(--blanco);
  border: 1px solid var(--borde);
  border-radius: var(--radio-lg);
  padding: 28px;
  margin-bottom: 20px;
  box-shadow: var(--sombra-sm);
  transition: box-shadow var(--trans);
}
.card:hover { box-shadow: var(--sombra-md); }
.card-titulo {
  font-size: 15px;
  font-weight: 600;
  color: var(--verde-oscuro);
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.card-sub {
  font-size: 13px;
  color: var(--texto-tenue);
  margin-bottom: 22px;
}
@media (max-width: 680px) { .card { padding: 20px; } }
</style>
