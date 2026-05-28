<template>
  <!-- ── RUTAS PÚBLICAS (Home y Login) ── -->
  <router-view v-if="esRutaPublica" />

  <!-- ── PACIENTE: solo portal ── -->
  <template v-else-if="sesionActiva && esPaciente">
    <AppTopbar />
    <main class="contenido">
      <router-view />
    </main>
  </template>

  <!-- ── ADMIN / AGENDADOR / MÉDICO: app completa ── -->
  <template v-else-if="sesionActiva && !esPaciente">
    <AppTopbar />
    <AppNavTabs :activo="seccion" :tabs="tabsFiltradas" @cambiar="seccion = $event" />
    <main class="contenido">
      <ListarCitas   v-show="seccion === 'listar'"  ref="listarRef" />
      <CrearCita     v-show="seccion === 'crear'"    @cita-creada="onCitaCreada" />
      <Configuracion v-show="seccion === 'admin'"   @abrir-modal="modalVisible = true" />
    </main>
    <ModalMedico
      :visible="modalVisible"
      @cerrar="modalVisible = false"
      @guardado="onMedicoGuardado"
    />
  </template>

  <!-- ── Si no hay sesión, mostrar login ── -->
  <template v-else>
    <router-view />
  </template>

  <AppToast />
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppTopbar      from './components/organisms/AppTopbar.vue'
import AppNavTabs     from './components/organisms/AppNavTabs.vue'
import AppToast       from './components/organisms/AppToast.vue'
import ModalMedico    from './components/organisms/ModalMedico.vue'
import LoginView      from './views/LoginView.vue'
import ListarCitas    from './views/ListarCitas.vue'
import CrearCita      from './views/CrearCita.vue'
import Configuracion  from './views/Configuracion.vue'
import { useMedicos } from './composables/useMedicos.js'
import { useAuth }    from './composables/useAuth.js'

const route = useRoute()
const router = useRouter()
const { cargarMedicos } = useMedicos()
const { sesionActiva, esPaciente, usuario } = useAuth()

const seccion      = ref('listar')
const modalVisible = ref(false)
const listarRef    = ref(null)

// Rutas que son públicas (no requieren autenticación)
const rutasPublicas = ['Home', 'Login']

const esRutaPublica = computed(() => {
  return rutasPublicas.includes(route.name) && !sesionActiva.value
})

const TODAS_LAS_TABS = [
  {
    id: 'listar', label: 'Consultar citas', roles: ['ADMINISTRADOR', 'AGENDADOR', 'MEDICO_TERAPISTA'],
    icono: '<path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" stroke="currentColor" stroke-width="1.5"/><rect x="9" y="3" width="6" height="4" rx="1" stroke="currentColor" stroke-width="1.5"/><path d="M9 12h6M9 16h4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
  {
    id: 'crear', label: 'Nueva cita', roles: ['ADMINISTRADOR', 'AGENDADOR'],
    icono: '<circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/><path d="M12 8v8M8 12h8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
  {
    id: 'admin', label: 'Configuración', roles: ['ADMINISTRADOR'],
    icono: '<circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="1.5"/><path d="M12 2v3M12 19v3M4.22 4.22l2.12 2.12M17.66 17.66l2.12 2.12M2 12h3M19 12h3M4.22 19.78l2.12-2.12M17.66 6.34l2.12-2.12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>',
  },
]

// Filtrar tabs según el rol del usuario
const tabsFiltradas = computed(() => {
  const rol = usuario.value?.rol || ''
  return TODAS_LAS_TABS.filter(t => t.roles.includes(rol))
})

// Cuando cambia el usuario o las tabs, asegurar que la sección activa sea válida
watch(sesionActiva, (activa) => {
  if (activa) {
    cargarMedicos()
    // Si el usuario tiene sesión, redirigir desde login/home a su zona correspondiente
    if (route.name === 'Login' || route.name === 'Home') {
      if (esPaciente.value) {
        router.push('/portal')
      } else {
        router.push('/citas')
      }
    }
  } else {
    // Si pierde la sesión, redirigir a login
    if (route.meta?.requiresAuth) {
      router.push('/login')
    }
  }
})

watch(() => route.name, (newRouteName) => {
  // Si intenta acceder a ruta protegida sin sesión, redirigir a login
  if (route.meta?.requiresAuth && !sesionActiva.value) {
    router.push('/login')
  }
})

async function onCitaCreada({ medicoId, fecha }) {
  seccion.value = 'listar'
  await new Promise(r => setTimeout(r, 50))
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
  if (sesionActiva.value && !esPaciente.value) cargarMedicos()
})
</script>
