<template>
  <span
    class="spinner"
    :class="{ 'spinner-verde': verde }"
    :style="{ width: size + 'px', height: size + 'px', borderWidth: borderW + 'px' }"
  ></span>
</template>

<script setup>
defineProps({
  verde:   { type: Boolean, default: false },
  size:    { type: Number, default: 16 },
  borderW: { type: Number, default: 2.5 },
})
</script>

<style scoped>
.spinner {
  display: inline-block;
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
