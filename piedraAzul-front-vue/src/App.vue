<template>
  <AppTopbar />

  <AppNavTabs :activo="seccion" :tabs="TABS" @cambiar="seccion = $event" />

  <main class="contenido">
    <ListarCitas  v-show="seccion === 'listar'"   ref="listarRef" />
    <CrearCita    v-show="seccion === 'crear'"     @cita-creada="onCitaCreada" />
    <PortalPaciente v-show="seccion === 'paciente'" />
    <Configuracion  v-show="seccion === 'admin'"   @abrir-modal="modalVisible = true" />
  </main>

  <ModalMedico
    :visible="modalVisible"
    @cerrar="modalVisible = false"
    @guardado="onMedicoGuardado"
  />

  <AppToast />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppTopbar     from './components/organisms/AppTopbar.vue'
import AppNavTabs    from './components/organisms/AppNavTabs.vue'
import AppToast      from './components/organisms/AppToast.vue'
import ModalMedico   from './components/organisms/ModalMedico.vue'
import ListarCitas   from './views/ListarCitas.vue'
import CrearCita     from './views/CrearCita.vue'
import PortalPaciente from './views/PortalPaciente.vue'
import Configuracion from './views/Configuracion.vue'
import { useMedicos } from './composables/useMedicos.js'

const { cargarMedicos } = useMedicos()

const seccion      = ref('listar')
const modalVisible = ref(false)
const listarRef    = ref(null)

const TABS = [
  {
    id: 'listar', label: 'Consultar citas',
    icono: '<path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" stroke="currentColor" stroke-width="1.5"/><rect x="9" y="3" width="6" height="4" rx="1" stroke="currentColor" stroke-width="1.5"/><path d="M9 12h6M9 16h4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
  {
    id: 'crear', label: 'Nueva cita',
    icono: '<circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/><path d="M12 8v8M8 12h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
  {
    id: 'paciente', label: 'Portal paciente', proto: true,
    icono: '<circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.5"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
  {
    id: 'admin', label: 'Configuración', proto: true,
    icono: '<circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="1.5"/><path d="M12 2v3M12 19v3M4.22 4.22l2.12 2.12M17.66 17.66l2.12 2.12M2 12h3M19 12h3M4.22 19.78l2.12-2.12M17.66 6.34l2.12-2.12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
]

// Al crear una cita, ir a listar y pre-cargar resultados
async function onCitaCreada({ medicoId, fecha }) {
  seccion.value = 'listar'
  await new Promise(r => setTimeout(r, 50)) // esperar render
  if (listarRef.value) {
    listarRef.value.medicoId = String(medicoId)
    listarRef.value.fecha    = fecha
    listarRef.value.buscar()
  }
}

function onMedicoGuardado() {
  cargarMedicos()
}

onMounted(() => {
  cargarMedicos()
})
</script>
