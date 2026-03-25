<template>
  <div :class="['toast', { show: visible }, tipo]">{{ mensaje }}</div>
</template>

<script setup>
import { useToast } from '../../composables/useToast.js'
const { mensaje, tipo, visible } = useToast()
</script>

<style scoped>
.toast {
  position: fixed;
  bottom: 28px;
  right: 28px;
  padding: 13px 20px;
  border-radius: var(--radio-md);
  font-size: 14px;
  font-weight: 600;
  box-shadow: 0 6px 24px rgba(0,0,0,.18);
  transform: translateY(80px);
  opacity: 0;
  transition: all .28s cubic-bezier(.34,1.56,.64,1);
  z-index: 9999;
  pointer-events: none;
  max-width: 360px;
}
.toast.show { transform: translateY(0); opacity: 1; }
.toast.ok  { background: var(--verde-oscuro); color: var(--blanco); }
.toast.err { background: var(--rojo); color: var(--blanco); }
</style>
