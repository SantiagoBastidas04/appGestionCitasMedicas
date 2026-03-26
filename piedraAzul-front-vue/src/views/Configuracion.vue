<template>
  <div>
    <div class="sec-header">
      <div class="sec-header-texto">
        <h2>Configuración del sistema</h2>
        <p>Administre parámetros de disponibilidad de cada profesional</p>
      </div>
    </div>

    <AppAlerta tipo="aviso" titulo="Solo administradores">
      Requiere autenticación con perfil administrador. Se implementa en la próxima iteración.
    </AppAlerta>

    <!-- Ventana global -->
    <AppCard titulo="Ventana global de agendamiento" subtitulo="Cuántas semanas hacia adelante se habilitarán las citas">
      <template #icono>
        <svg width="16" height="16" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/><path d="M12 7v5l3 3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
      </template>
      <div class="form-grid-3">
        <AppCampo label="Semanas habilitadas" tipo="number" v-model="semanas" ayuda="Rango: 1 a 12 semanas" />
      </div>
      <div class="btn-fila">
        <AppButton @click="guardarSemanas">Guardar</AppButton>
      </div>
    </AppCard>

    <!-- Configurar médico -->
    <AppCard titulo="Configurar profesional" subtitulo="Defina horario, días e intervalo entre citas">
      <template #icono>
        <svg width="16" height="16" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="1.5"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
      </template>
      <div class="form-grid">
        <AppCampo label="Profesional" tipo="select" v-model="configMedicoId" @change="cargarConfig">
          <option v-for="m in medicos" :key="m.id" :value="m.id">
            {{ m.nombres }} {{ m.apellidos }}
          </option>
        </AppCampo>
        <AppCampo label="Intervalo entre citas (minutos)" tipo="number" v-model="config.intervalo" ayuda="Mínimo 5 minutos" />
        <AppCampo label="Hora de inicio" tipo="time" v-model="config.inicio" />
        <AppCampo label="Hora de fin"   tipo="time" v-model="config.fin"    />
        <div class="campo form-full">
          <label>Días de atención</label>
          <span class="ayuda" style="margin-bottom:8px;">Haga clic para activar o desactivar un día</span>
          <DiasSelector v-model="config.dias" />
        </div>
      </div>
      <div class="btn-fila">
        <AppButton @click="guardarConfig">Guardar configuración</AppButton>
        <AppButton variante="contorno" @click="cargarConfig">Descartar cambios</AppButton>
      </div>
    </AppCard>

    <!-- Tabla médicos -->
    <AppCard titulo="Gestión de profesionales" subtitulo="Lista de médicos y terapistas registrados">
      <template #icono>
        <svg width="16" height="16" fill="none" viewBox="0 0 24 24"><path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2" stroke="currentColor" stroke-width="1.5"/><rect x="9" y="3" width="6" height="4" rx="1" stroke="currentColor" stroke-width="1.5"/></svg>
      </template>
      <div class="tabla-wrap">
        <table>
          <thead>
            <tr>
              <th>Nombre</th><th>Especialidad</th><th>Horario</th>
              <th>Intervalo</th><th>Estado</th><th>Acción</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!medicos.length">
              <td colspan="6" class="vacia">Sin profesionales registrados</td>
            </tr>
            <tr v-for="m in medicos" :key="m.id">
              <td><strong>{{ m.nombres }} {{ m.apellidos }}</strong></td>
              <td>{{ formatEsp(m.especialidad) }}</td>
              <td class="mono">{{ m.horaInicio }} – {{ m.horaFin }}</td>
              <td>{{ m.intervaloCitas }} min</td>
              <td><AppBadge :estado="m.activo ? 'activo' : 'inactivo'" :texto="m.activo ? 'Activo' : 'Inactivo'" /></td>
              <td>
                <AppButton variante="peligro" :small="true" @click="toggleMedico(m.id, !m.activo)">
                  {{ m.activo ? 'Desactivar' : 'Activar' }}
                </AppButton>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="btn-fila">
        <AppButton @click="$emit('abrir-modal')">
          <svg width="14" height="14" fill="none" viewBox="0 0 24 24"><circle cx="12" cy="12" r="9" stroke="white" stroke-width="2"/><path d="M12 8v8M8 12h8" stroke="white" stroke-width="2" stroke-linecap="round"/></svg>
          Agregar profesional
        </AppButton>
      </div>
    </AppCard>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import AppCard      from '../components/molecules/AppCard.vue'
import AppCampo     from '../components/atoms/AppCampo.vue'
import AppButton    from '../components/atoms/AppButton.vue'
import AppAlerta    from '../components/atoms/AppAlerta.vue'
import AppBadge     from '../components/atoms/AppBadge.vue'
import DiasSelector from '../components/molecules/DiasSelector.vue'
import { useMedicos }   from '../composables/useMedicos.js'
import { useToast }     from '../composables/useToast.js'
import { medicoService } from '../services/api.js'

defineEmits(['abrir-modal'])

const { medicos, formatEsp, cargarMedicos } = useMedicos()
const { toast } = useToast()

const semanas      = ref(4)
const configMedicoId = ref('')
const config = reactive({ intervalo: 30, inicio: '08:00', fin: '17:00', dias: ['MONDAY','TUESDAY','WEDNESDAY','FRIDAY'] })

function guardarSemanas() {
  toast('Para habilitar esta función agrega el endpoint al backend')
}

async function cargarConfig() {
  if (!configMedicoId.value) return
  try {
    const m = await medicoService.getById(configMedicoId.value)
    config.intervalo = m.intervaloCitas
    config.inicio    = m.horaInicio
    config.fin       = m.horaFin
    config.dias      = m.diasAtencion || []
  } catch (e) {
    toast('Error cargando configuración: ' + e.message, 'err')
  }
}

function guardarConfig() {
  toast('Para habilitar esta función agrega PUT /api/medicos/{id} al backend')
}

async function toggleMedico(id, activo) {
  try {
    await medicoService.toggleEstado(id, activo)
    toast(`Profesional ${activo ? 'activado' : 'desactivado'}`)
    cargarMedicos()
  } catch (e) {
    toast('Error: ' + e.message, 'err')
  }
}
</script>

<style scoped>
.sec-header { margin-bottom: 28px; }
.sec-header-texto h2 { font-family: 'DM Serif Display', serif; font-size: 28px; font-weight: 400; color: var(--texto); line-height: 1.2; }
.sec-header-texto p  { font-size: 14px; color: var(--texto-tenue); margin-top: 4px; }
.btn-fila { display: flex; gap: 12px; flex-wrap: wrap; margin-top: 22px; }
.campo { display: flex; flex-direction: column; gap: 5px; }
.ayuda { font-size: 12px; color: var(--texto-tenue); }
.tabla-wrap { overflow-x: auto; border-radius: var(--radio-md); border: 1px solid var(--borde); }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
thead tr { background: var(--verde-claro); }
thead th { padding: 12px 16px; text-align: left; font-size: 12px; font-weight: 700; color: var(--verde-oscuro); white-space: nowrap; letter-spacing: .04em; text-transform: uppercase; }
tbody tr { border-top: 1px solid var(--borde); transition: background var(--trans); }
tbody tr:hover { background: #F5FBF8; }
tbody td { padding: 13px 16px; vertical-align: middle; }
.mono { font-family: monospace; font-size: 13px; }
.vacia { text-align: center; padding: 48px 24px; color: var(--texto-tenue); font-size: 15px; }
</style>
