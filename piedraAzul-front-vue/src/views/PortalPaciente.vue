<template>
  <div>
    <div class="sec-header">
      <div class="sec-header-texto">
        <h2>Bienvenido, {{ nombreCompleto }}</h2>
        <p>Aquí puede agendar su cita médica</p>
      </div>
    </div>

    <!-- ═══ FORMULARIO NUEVA CITA ═══ -->
    <template v-if="!citaConfirmada">

      <!-- Paso 1: Especialidad y médico -->
      <AppCard titulo="Paso 1 — Seleccione el profesional" subtitulo="Escoja la especialidad y el médico de su preferencia">
        <template #icono>
          <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
            <circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.5"/>
            <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </template>
        <div class="form-grid">
          <AppCampo label="Especialidad" tipo="select" v-model="sel.especialidad" @change="onEspecialidadChange" :errorMsg="errores.especialidad">
            <option value="FISIOTERAPIA">Fisioterapia</option>
            <option value="TERAPIA_NEURAL">Terapia Neural</option>
            <option value="QUIROPRAXIA">Quiropraxia</option>
          </AppCampo>

          <AppCampo label="Médico / Terapista" tipo="select" v-model="sel.medicoId" :errorMsg="errores.medico"
                    :disabled="!sel.especialidad || cargandoMedicos">
            <option v-if="cargandoMedicos" value="">Cargando...</option>
            <option v-else-if="!medicosFiltrados.length" value="">Sin profesionales disponibles</option>
            <option v-for="m in medicosFiltrados" :key="m.id" :value="m.id">
              {{ m.nombres }} {{ m.apellidos }}
            </option>
          </AppCampo>
        </div>
      </AppCard>

      <!-- Paso 2: Fecha y franja -->
      <AppCard titulo="Paso 2 — Elija fecha y hora" subtitulo="Seleccione el día y la franja que más le convenga">
        <template #icono>
          <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
            <rect x="3" y="4" width="18" height="18" rx="3" stroke="currentColor" stroke-width="1.5"/>
            <path d="M16 2v4M8 2v4M3 10h18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
          </svg>
        </template>
        <div class="form-grid">
          <AppCampo label="Fecha deseada" tipo="date" v-model="sel.fecha" :errorMsg="errores.fecha" />
        </div>

        <div class="campo form-full" style="margin-top:16px;">
          <label>Horas disponibles <span v-if="errores.hora" class="err-inline">— {{ errores.hora }}</span></label>
          <FranjasHorarias
            :medicoId="sel.medicoId"
            :fecha="sel.fecha"
            :seleccionada="sel.hora"
            @seleccionar="h => sel.hora = h"
          />
        </div>

        <AppCampo label="Observaciones" tipo="textarea" v-model="sel.obs"
                  placeholder="Motivo de consulta u otras observaciones..." :opcional="true"
                  style="margin-top:16px;" />

        <ResumenCita :nombreMedico="nombreMedicoSel" :fecha="sel.fecha" :hora="sel.hora" />

        <div class="btn-fila">
          <AppButton @click="confirmarCita" :loading="guardando">
            <svg width="15" height="15" fill="none" viewBox="0 0 24 24">
              <path d="M5 12l5 5L20 7" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            Confirmar mi cita
          </AppButton>
          <AppButton variante="contorno" @click="limpiar">Limpiar</AppButton>
        </div>
      </AppCard>

    </template>

    <!-- ═══ CITA CONFIRMADA → MIS CITAS ═══ -->
    <template v-else>
      <!-- Banner de éxito -->
      <div class="banner-exito">
        <div class="banner-icono">
          <svg width="28" height="28" fill="none" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="10" fill="#0D6B4E"/>
            <path d="M7 12l4 4 6-7" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div>
          <h3>¡Cita agendada exitosamente!</h3>
          <p>Su cita quedó registrada. Puede ver el detalle a continuación.</p>
        </div>
        <AppButton variante="contorno" @click="agendarOtra" style="margin-left:auto; flex-shrink:0;">
          Agendar otra cita
        </AppButton>
      </div>

      <!-- Mis citas -->
      <AppCard titulo="Mis citas" subtitulo="Historial de citas registradas a su nombre">
        <template #icono>
          <svg width="16" height="16" fill="none" viewBox="0 0 24 24">
            <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" stroke="currentColor" stroke-width="1.5"/>
            <rect x="9" y="3" width="6" height="4" rx="1" stroke="currentColor" stroke-width="1.5"/>
          </svg>
        </template>

        <div v-if="cargandoCitas" class="estado-centro">
          <AppSpinner :verde="true" :size="28" />
          <p>Cargando sus citas...</p>
        </div>

        <AppAlerta v-else-if="!misCitas.length" tipo="info" titulo="Sin citas registradas">
          Aún no tiene citas en el sistema.
        </AppAlerta>

        <div v-else class="tabla-wrap">
          <table>
            <caption>Listado de citas programadas</caption>
            <thead>
              <tr>
                <th>Profesional</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Estado</th>
                <th>Observación</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="c in misCitas" :key="c.id">
                <td><strong>{{ c.nombreMedico }}</strong></td>
                <td>{{ formatFecha(c.fecha) }}</td>
                <td><strong class="hora">{{ c.hora }}</strong></td>
                <td><AppBadge :estado="c.estado" :texto="c.estado" /></td>
                <td class="obs">{{ c.observacion || '—' }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </AppCard>
    </template>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import AppCard        from '../components/molecules/AppCard.vue'
import AppCampo       from '../components/atoms/AppCampo.vue'
import AppButton      from '../components/atoms/AppButton.vue'
import AppAlerta      from '../components/atoms/AppAlerta.vue'
import AppBadge       from '../components/atoms/AppBadge.vue'
import AppSpinner     from '../components/atoms/AppSpinner.vue'
import FranjasHorarias from '../components/molecules/FranjasHorarias.vue'
import ResumenCita    from '../components/molecules/ResumenCita.vue'
import { useAuth }   from '../composables/useAuth.js'
import { useToast }  from '../composables/useToast.js'
import { medicoService, citaService, pacienteService } from '../services/api.js'

const { usuario, nombreCompleto } = useAuth()
const { toast } = useToast()

const hoy = () => new Date().toISOString().split('T')[0]

// Estado
const todosLosMedicos  = ref([])
const cargandoMedicos  = ref(false)
const medicosFiltrados = ref([])
const citaConfirmada   = ref(false)
const guardando        = ref(false)
const cargandoCitas    = ref(false)
const misCitas         = ref([])
const pacienteId       = ref(null)
const errores          = ref({})

const sel = reactive({
  especialidad: '',
  medicoId: '',
  fecha: hoy(),
  hora: null,
  obs: '',
})

// Médico seleccionado para mostrar en resumen
const nombreMedicoSel = computed(() => {
  const m = medicosFiltrados.value.find(m => String(m.id) === String(sel.medicoId))
  return m ? `${m.nombres} ${m.apellidos}` : ''
})

// Al cambiar especialidad, filtrar médicos
async function onEspecialidadChange() {
  sel.medicoId = ''
  sel.hora = null
  medicosFiltrados.value = []
  if (!sel.especialidad) return

  cargandoMedicos.value = true
  try {
    medicosFiltrados.value = await medicoService.getByEspecialidad(sel.especialidad)
  } catch (e) {
    toast('Error cargando profesionales: ' + e.message, 'err')
  } finally {
    cargandoMedicos.value = false
  }
}

// Confirmar cita
async function confirmarCita() {
  errores.value = {}
  let ok = true
  if (!sel.especialidad) { errores.value.especialidad = 'Seleccione una especialidad'; ok = false }
  if (!sel.medicoId)     { errores.value.medico = 'Seleccione un profesional'; ok = false }
  if (!sel.fecha)        { errores.value.fecha = 'Seleccione la fecha'; ok = false }
  if (!sel.hora)         { errores.value.hora = 'Seleccione una franja horaria'; ok = false }
  if (!ok) return

  guardando.value = true
  try {
    // Buscar el id del paciente por username si no lo tenemos
    if (!pacienteId.value) {
      const p = await pacienteService.getByUsername(usuario.value.username)
      pacienteId.value = p.id
    }

    await citaService.create({
      medicoId:    parseInt(sel.medicoId),
      pacienteId:  pacienteId.value,
      fecha:       sel.fecha,
      hora:        sel.hora,
      observacion: sel.obs || null,
    })

    toast('¡Cita registrada exitosamente!')
    citaConfirmada.value = true
    await cargarMisCitas()

  } catch (e) {
    toast('Error al confirmar: ' + e.message, 'err')
  } finally {
    guardando.value = false
  }
}

async function cargarMisCitas() {
  if (!pacienteId.value) return
  cargandoCitas.value = true
  try {
    misCitas.value = await citaService.getByPaciente(pacienteId.value)
  } catch (e) {
    toast('Error cargando sus citas', 'err')
  } finally {
    cargandoCitas.value = false
  }
}

function limpiar() {
  sel.especialidad = ''
  sel.medicoId = ''
  sel.fecha = hoy()
  sel.hora = null
  sel.obs = ''
  medicosFiltrados.value = []
  errores.value = {}
}

function agendarOtra() {
  citaConfirmada.value = false
  limpiar()
}

function formatFecha(fecha) {
  if (!fecha) return ''
  return new Date(fecha + 'T00:00:00')
    .toLocaleDateString('es-CO', { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric' })
}

onMounted(async () => {
  // Pre-cargar id del paciente
  try {
    const p = await pacienteService.getByUsername(usuario.value.username)
    pacienteId.value = p.id
  } catch {}
})
</script>

<style scoped>
.sec-header { margin-bottom: 28px; }
.sec-header-texto h2 { font-family: 'DM Serif Display', serif; font-size: 28px; font-weight: 400; color: var(--texto); line-height: 1.2; }
.sec-header-texto p  { font-size: 14px; color: var(--texto-tenue); margin-top: 4px; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.form-full  { grid-column: 1 / -1; }
.btn-fila   { display: flex; gap: 12px; flex-wrap: wrap; margin-top: 22px; }
.campo      { display: flex; flex-direction: column; gap: 5px; }

.err-inline { color: var(--rojo); font-size: 12px; font-weight: 500; }

/* Banner éxito */
.banner-exito {
  display: flex;
  align-items: center;
  gap: 16px;
  background: var(--verde-claro);
  border: 1px solid var(--verde-borde);
  border-radius: var(--radio-lg);
  padding: 20px 24px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.banner-icono { flex-shrink: 0; }
.banner-exito h3 { font-family: 'DM Serif Display', serif; font-size: 18px; font-weight: 400; color: var(--verde-oscuro); margin-bottom: 2px; }
.banner-exito p  { font-size: 13px; color: #2A6E58; }

/* Tabla */
.estado-centro { text-align: center; padding: 40px; }
.estado-centro p { margin-top: 10px; font-size: 14px; color: var(--texto-tenue); }
.tabla-wrap { overflow-x: auto; border-radius: var(--radio-md); border: 1px solid var(--borde); }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
thead tr { background: var(--verde-claro); }
thead th { padding: 12px 16px; text-align: left; font-size: 12px; font-weight: 700; color: var(--verde-oscuro); white-space: nowrap; letter-spacing: .04em; text-transform: uppercase; }
tbody tr { border-top: 1px solid var(--borde); transition: background var(--trans); }
tbody tr:hover { background: #F5FBF8; }
tbody td { padding: 12px 16px; vertical-align: middle; }
.hora { font-size: 15px; font-family: 'DM Sans', monospace; }
.obs  { font-size: 13px; color: var(--texto-suave); max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

@media (max-width: 600px) {
  .form-grid { grid-template-columns: 1fr; }
  .banner-exito { flex-direction: column; align-items: flex-start; }
}
</style>
