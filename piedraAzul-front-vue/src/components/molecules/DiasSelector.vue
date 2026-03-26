<template>
  <div class="dias-grid">
    <button
      v-for="d in dias"
      :key="d.valor"
      :class="['dia-btn', { on: modelValue.includes(d.valor) }]"
      type="button"
      @click="toggle(d.valor)"
    >
      {{ d.label }}
    </button>
  </div>
</template>

<script setup>
const DIAS = [
  { valor: 'MONDAY',    label: 'Lunes'     },
  { valor: 'TUESDAY',   label: 'Martes'    },
  { valor: 'WEDNESDAY', label: 'Miércoles' },
  { valor: 'THURSDAY',  label: 'Jueves'    },
  { valor: 'FRIDAY',    label: 'Viernes'   },
  { valor: 'SATURDAY',  label: 'Sábado'    },
  { valor: 'SUNDAY',    label: 'Domingo'   },
]

const props  = defineProps({ modelValue: { type: Array, default: () => [] } })
const emit   = defineEmits(['update:modelValue'])
const dias   = DIAS

function toggle(valor) {
  const set = new Set(props.modelValue)
  set.has(valor) ? set.delete(valor) : set.add(valor)
  emit('update:modelValue', [...set])
}
</script>

<style scoped>
.dias-grid { display: flex; flex-wrap: wrap; gap: 8px; margin-top: 8px; }
.dia-btn {
  padding: 8px 16px;
  border: 1.5px solid var(--borde);
  border-radius: var(--radio-sm);
  font-size: 13px;
  font-weight: 600;
  background: var(--blanco);
  color: var(--texto-suave);
  transition: all var(--trans);
}
.dia-btn.on {
  border-color: var(--verde);
  background: var(--verde-claro);
  color: var(--verde-oscuro);
}
</style>
