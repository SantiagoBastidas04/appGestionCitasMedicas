<template>
  <div>
    <div class="sec-header">
      <div class="sec-header-texto">
        <h2>Portal del paciente</h2>
        <p>Vista que verá el paciente para agendar su cita desde la web</p>
      </div>
    </div>

    <AppAlerta tipo="aviso" titulo="Pantalla de prototipo">
      La funcionalidad completa requiere autenticación de usuarios (Spring Security + JWT). Se implementa en la próxima iteración.
    </AppAlerta>

    <AppCard titulo="Paso 1 — Ingrese con su cuenta" subtitulo="Use su correo y contraseña. Si no tiene cuenta puede registrarse.">
      <div class="form-grid">
        <AppCampo label="Correo electrónico" tipo="email" placeholder="su.correo@ejemplo.com" />
        <AppCampo label="Contraseña"         tipo="password" placeholder="Su contraseña" />
      </div>
      <div class="btn-fila">
        <AppButton>Ingresar</AppButton>
        <AppButton variante="contorno">Crear cuenta nueva</AppButton>
      </div>
    </AppCard>

    <AppCard titulo="Paso 2 — Seleccione especialidad y profesional" subtitulo="Escoja el tipo de atención que necesita">
      <div class="form-grid">
        <AppCampo label="Especialidad" tipo="select">
          <option>Fisioterapia</option>
          <option>Terapia Neural</option>
          <option>Quiropraxia</option>
        </AppCampo>
        <AppCampo label="Médico / Terapista" tipo="select" />
      </div>
    </AppCard>

    <AppCard titulo="Paso 3 — Elija fecha y hora" subtitulo="Seleccione el día y la franja que más le convenga">
      <div class="form-grid">
        <AppCampo label="Fecha deseada" tipo="date" />
      </div>
      <div class="franjas-section">
        <label class="franjas-label">Horas disponibles</label>
        <div class="franjas-grid">
          <button class="franja">08:00</button>
          <button class="franja sel">09:00</button>
          <button class="franja">10:00</button>
          <button class="franja ocupada">11:00</button>
          <button class="franja">14:00</button>
          <button class="franja">15:00</button>
        </div>
      </div>
      <ResumenCita
        nombreMedico="Dr. Juan Pérez"
        fecha="2026-03-25"
        hora="09:00"
      />
      <div class="btn-fila">
        <AppButton>Confirmar mi cita</AppButton>
      </div>
    </AppCard>
  </div>
</template>

<script setup>
import AppCard    from '../components/molecules/AppCard.vue'
import AppCampo   from '../components/atoms/AppCampo.vue'
import AppButton  from '../components/atoms/AppButton.vue'
import AppAlerta  from '../components/atoms/AppAlerta.vue'
import ResumenCita from '../components/molecules/ResumenCita.vue'
</script>

<style scoped>
.sec-header { margin-bottom: 28px; }
.sec-header-texto h2 { font-family: 'DM Serif Display', serif; font-size: 28px; font-weight: 400; color: var(--texto); line-height: 1.2; }
.sec-header-texto p  { font-size: 14px; color: var(--texto-tenue); margin-top: 4px; }
.btn-fila { display: flex; gap: 12px; flex-wrap: wrap; margin-top: 22px; }
.franjas-section { margin-top: 16px; }
.franjas-label { font-size: 13px; font-weight: 600; display: block; margin-bottom: 10px; }
.franjas-grid { display: flex; flex-wrap: wrap; gap: 10px; }
.franja { padding: 10px 18px; border: 1.5px solid var(--borde); border-radius: var(--radio-md); font-size: 15px; font-weight: 600; background: var(--blanco); color: var(--texto); transition: all var(--trans); }
.franja:hover { border-color: var(--verde-medio); background: var(--verde-claro); }
.franja.sel { border-color: var(--verde); background: var(--verde); color: var(--blanco); }
.franja.ocupada { opacity: .4; cursor: not-allowed; text-decoration: line-through; }
</style>
