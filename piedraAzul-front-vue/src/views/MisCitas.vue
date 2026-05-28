<template>
  <div class="mis-citas">

    <!-- Header -->
    <div class="topbar">
      <div class="topbar-marca">
        <svg width="20" height="20" fill="none" viewBox="0 0 24 24">
          <path d="M12 2C8 2 4 5.5 4 10c0 6 8 12 8 12s8-6 8-12c0-4.5-4-8-8-8z"
                stroke="#0D6B4E" stroke-width="1.5" fill="none"/>
          <circle cx="12" cy="10" r="2.5" stroke="#0D6B4E" stroke-width="1.5" fill="none"/>
        </svg>
        <span>Piedrazul</span>
      </div>
      <div class="topbar-botones">
        <button class="btn-volver" @click="volverAlPortal">
          <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
            <path d="M19 12H5M12 19l-7-7 7-7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          Volver
        </button>
        <button class="btn-salir" @click="salir">
          <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
            <path d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h6a2 2 0 012 2v1" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          Salir
        </button>
      </div>
    </div>

    <!-- Bienvenida -->
    <div class="bienvenida">
      <div class="avatar">{{ avatarLetra }}</div>
      <div>
        <p class="saludo">Hola,</p>
        <p class="nombre">{{ nombreCompleto }}</p>
      </div>
    </div>

    <!-- Título sección -->
    <div class="seccion-titulo">
      <svg width="18" height="18" fill="none" viewBox="0 0 24 24">
        <rect x="3" y="4" width="18" height="18" rx="3" stroke="#0D6B4E" stroke-width="1.5"/>
        <path d="M16 2v4M8 2v4M3 10h18" stroke="#0D6B4E" stroke-width="1.5" stroke-linecap="round"/>
      </svg>
      <h2>Mis citas</h2>
    </div>

    <!-- Loading -->
    <div v-if="cargando" class="estado-centro">
      <div class="spinner"></div>
      <p>Cargando tus citas...</p>
    </div>

    <!-- Error -->
    <div v-else-if="error" class="alerta-error">
      <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
        <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/>
        <path d="M12 8v4M12 16h.01" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
      </svg>
      {{ error }}
    </div>

    <!-- Sin citas -->
    <div v-else-if="!citas.length" class="empty-state">
      <div class="empty-icono">
        <svg width="28" height="28" fill="none" viewBox="0 0 24 24">
          <rect x="3" y="4" width="18" height="18" rx="3" stroke="#0D6B4E" stroke-width="1.5"/>
          <path d="M16 2v4M8 2v4M3 10h18" stroke="#0D6B4E" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
      </div>
      <p class="empty-titulo">Sin citas registradas</p>
      <p class="empty-sub">Aún no tienes citas agendadas</p>
    </div>

    <!-- Lista de citas -->
    <div v-else class="lista-citas">
      <div
        v-for="cita in citas"
        :key="cita.id"
        class="cita-card"
        :class="'estado-' + cita.estado.toLowerCase()"
      >
        <div class="cita-header">
          <div class="cita-fecha">
            <span class="dia">{{ formatDia(cita.fecha) }}</span>
            <span class="mes">{{ formatMes(cita.fecha) }}</span>
          </div>
          <div class="cita-badge" :class="'badge-' + cita.estado.toLowerCase()">
            {{ labelEstado(cita.estado) }}
          </div>
        </div>

        <div class="cita-body">
          <div class="cita-hora">
            <svg width="14" height="14" fill="none" viewBox="0 0 24 24">
              <circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/>
              <path d="M12 7v5l3 3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            </svg>
            {{ formatHora(cita.hora) }}
          </div>
          <div class="cita-medico">
            <svg width="14" height="14" fill="none" viewBox="0 0 24 24">
              <circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.5"/>
              <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
            </svg>
            {{ cita.nombreMedico }}
          </div>
          <div v-if="cita.observacion" class="cita-obs">
            <svg width="14" height="14" fill="none" viewBox="0 0 24 24">
              <path d="M8 12h8M8 8h5M5 4h14a1 1 0 011 1v10a1 1 0 01-1 1H8l-4 4V5a1 1 0 011-1z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            {{ cita.observacion }}
          </div>
        </div>
      </div>
    </div>

    <!-- Botón refrescar -->
    <button class="btn-refrescar" @click="cargar" :disabled="cargando">
      <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
        <path d="M1 4v6h6M23 20v-6h-6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M20.49 9A9 9 0 005.64 5.64L1 10M23 14l-4.64 4.36A9 9 0 013.51 15" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
      Actualizar
    </button>

  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth }   from '../composables/useAuth.js'
import { useToast }  from '../composables/useToast.js'
import { citaService, pacienteService } from '../services/api.js'

const router = useRouter()
const { usuario, sesionActiva, avatarLetra, nombreCompleto, cerrarSesion } = useAuth()
const { toast } = useToast()

const citas    = ref([])
const cargando = ref(false)
const error    = ref('')

// Watcher para redirigir al login cuando se cierre la sesión
watch(sesionActiva, (activa) => {
  if (!activa) {
    router.push('/login')
  }
})

async function cargar() {
  // Protección: si no hay usuario, no intentar cargar
  if (!usuario.value) {
    router.push('/login')
    return
  }
  
  error.value   = ''
  cargando.value = true
  try {
    // Obtener el paciente por username para sacar su id
    const paciente = await pacienteService.getByUsername(usuario.value.username)
    citas.value = await citaService.getByPaciente(paciente.id)
  } catch (e) {
    error.value = 'No se pudieron cargar tus citas'
    toast('Error al cargar citas', 'err')
  } finally {
    cargando.value = false
  }
}

function volverAlPortal() {
  router.push('/portal')
}

function salir() {
  cerrarSesion()
}

function formatDia(fecha) {
  return new Date(fecha + 'T00:00:00').getDate()
}

function formatMes(fecha) {
  const meses = ['Ene','Feb','Mar','Abr','May','Jun','Jul','Ago','Sep','Oct','Nov','Dic']
  return meses[new Date(fecha + 'T00:00:00').getMonth()]
}

function formatHora(hora) {
  return hora ? hora.substring(0, 5) : ''
}

function labelEstado(estado) {
  return { CONFIRMADA: 'Confirmada', CANCELADA: 'Cancelada', COMPLETADA: 'Completada' }[estado] || estado
}

onMounted(cargar)
</script>

<style scoped>
.mis-citas {
  min-height: 100vh;
  background: var(--crema);
  padding-bottom: 32px;
}

/* Topbar */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: var(--blanco);
  border-bottom: 1px solid var(--borde);
  position: sticky;
  top: 0;
  z-index: 10;
}
.topbar-marca {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'DM Serif Display', serif;
  font-size: 18px;
  color: var(--verde-oscuro);
}
.topbar-botones {
  display: flex;
  align-items: center;
  gap: 8px;
}
.btn-volver {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--texto-suave);
  background: none;
  border: 1px solid var(--borde);
  border-radius: 8px;
  padding: 6px 12px;
  cursor: pointer;
  transition: all 0.15s;
}
.btn-volver:hover { color: var(--verde-oscuro); border-color: var(--verde-borde); }
.btn-salir {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--texto-suave);
  background: none;
  border: 1px solid var(--borde);
  border-radius: 8px;
  padding: 6px 12px;
  cursor: pointer;
  transition: all 0.15s;
}
.btn-salir:hover { color: var(--rojo); border-color: var(--rojo-borde); }

/* Bienvenida */
.bienvenida {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px;
  background: var(--blanco);
  border-bottom: 1px solid var(--borde);
}
.avatar {
  width: 44px;
  height: 44px;
  background: var(--verde-claro);
  border: 1.5px solid var(--verde-borde);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 700;
  color: var(--verde-oscuro);
  flex-shrink: 0;
}
.saludo { font-size: 12px; color: var(--texto-tenue); margin: 0; }
.nombre { font-size: 16px; font-weight: 600; color: var(--texto); margin: 0; }

/* Sección título */
.seccion-titulo {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 20px 20px 12px;
}
.seccion-titulo h2 {
  font-family: 'DM Serif Display', serif;
  font-size: 22px;
  font-weight: 400;
  color: var(--texto);
  margin: 0;
}

/* Estados */
.estado-centro {
  text-align: center;
  padding: 48px 24px;
}
.estado-centro p { margin-top: 12px; color: var(--texto-tenue); font-size: 14px; }
.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--verde-claro);
  border-top-color: var(--verde);
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  margin: 0 auto;
}
@keyframes spin { to { transform: rotate(360deg); } }

.alerta-error {
  margin: 16px 20px;
  background: var(--rojo-claro);
  color: var(--rojo);
  border: 1px solid var(--rojo-borde);
  border-radius: 10px;
  padding: 12px 16px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.empty-state {
  text-align: center;
  padding: 56px 24px;
}
.empty-icono {
  width: 60px; height: 60px;
  background: var(--verde-claro);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 16px;
}
.empty-titulo { font-size: 17px; font-weight: 600; color: var(--texto); margin: 0 0 6px; }
.empty-sub { font-size: 14px; color: var(--texto-tenue); margin: 0; }

/* Tarjetas de cita */
.lista-citas {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 16px;
}

.cita-card {
  background: var(--blanco);
  border: 1px solid var(--borde);
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,.04);
}
.cita-card.estado-cancelada { opacity: 0.65; }

.cita-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  border-bottom: 1px solid var(--borde);
  background: var(--verde-claro);
}
.cita-fecha {
  display: flex;
  align-items: baseline;
  gap: 4px;
}
.dia {
  font-size: 24px;
  font-weight: 700;
  color: var(--verde-oscuro);
  line-height: 1;
}
.mes {
  font-size: 13px;
  font-weight: 600;
  color: var(--verde);
  text-transform: uppercase;
}

/* Badges */
.cita-badge {
  font-size: 11px;
  font-weight: 700;
  padding: 4px 10px;
  border-radius: 20px;
  text-transform: uppercase;
  letter-spacing: .04em;
}
.badge-confirmada { background: #D1FAE5; color: #065F46; }
.badge-cancelada  { background: #FEE2E2; color: #991B1B; }
.badge-completada { background: #DBEAFE; color: #1E40AF; }

.cita-body {
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.cita-hora, .cita-medico, .cita-obs {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--texto);
}
.cita-hora { font-weight: 700; font-size: 16px; }
.cita-medico { color: var(--texto-suave); }
.cita-obs { color: var(--texto-tenue); font-size: 13px; font-style: italic; }

/* Botón refrescar */
.btn-refrescar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: calc(100% - 32px);
  margin: 20px 16px 0;
  padding: 14px;
  background: none;
  border: 1.5px dashed var(--borde);
  border-radius: 12px;
  color: var(--texto-tenue);
  font-size: 14px;
  cursor: pointer;
  transition: all 0.15s;
}
.btn-refrescar:hover:not(:disabled) {
  border-color: var(--verde);
  color: var(--verde);
}
.btn-refrescar:disabled { opacity: 0.5; cursor: not-allowed; }
</style>