<template>
  <header class="topbar">
    <!-- Izquierda: logo + nombre -->
    <div class="topbar-izq">
      <div class="topbar-logo">
        <svg width="20" height="20" fill="none" viewBox="0 0 24 24">
          <path d="M12 2C8 2 4 5.5 4 10c0 6 8 12 8 12s8-6 8-12c0-4.5-4-8-8-8z"
                stroke="white" stroke-width="1.5" fill="none"/>
          <circle cx="12" cy="10" r="2.5" stroke="white" stroke-width="1.5" fill="none"/>
        </svg>
      </div>
      <div class="topbar-info">
        <h1>Piedrazul</h1>
        <p>Sistema de agendamiento médico</p>
      </div>
    </div>

    <!-- Derecha: usuario + salir -->
    <div class="topbar-der">
      <div class="topbar-usuario">
        <div class="topbar-avatar">{{ avatarLetra }}</div>
        <div class="topbar-datos">
          <span class="top-nombre">{{ nombreCompleto }}</span>
          <span class="rol-pill">{{ rolLabel }}</span>
        </div>
      </div>
      <button class="btn-salir" @click="cerrarSesion">
        <svg width="14" height="14" fill="none" viewBox="0 0 24 24">
          <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9"
                stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        Salir
      </button>
    </div>
  </header>
</template>

<script setup>
import { useAuth } from '../../composables/useAuth.js'
const { avatarLetra, nombreCompleto, rolLabel, cerrarSesion } = useAuth()
</script>

<style scoped>
.topbar {
  background: var(--verde-oscuro);
  color: var(--blanco);
  padding: 0 32px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  position: sticky;
  top: 0;
  z-index: 200;
  box-shadow: 0 2px 12px rgba(0,0,0,.18);
}

.topbar-izq {
  display: flex;
  align-items: center;
  gap: 14px;
}
.topbar-logo {
  width: 38px; height: 38px;
  background: rgba(255,255,255,.15);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.topbar-info h1 {
  font-family: 'DM Serif Display', serif;
  font-size: 18px; font-weight: 400;
  line-height: 1.2; letter-spacing: .01em;
}
.topbar-info p { font-size: 12px; opacity: .65; font-weight: 300; }

/* Derecha */
.topbar-der {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}
.topbar-usuario {
  display: flex;
  align-items: center;
  gap: 10px;
}
.topbar-avatar {
  width: 34px; height: 34px;
  background: rgba(255,255,255,.18);
  border: 1.5px solid rgba(255,255,255,.3);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 14px; font-weight: 700;
}
.topbar-datos {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}
.top-nombre { font-size: 13px; font-weight: 500; }
.rol-pill {
  background: rgba(255,255,255,.15);
  border: 1px solid rgba(255,255,255,.25);
  border-radius: 20px;
  padding: 1px 9px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: .04em;
}

.btn-salir {
  display: flex; align-items: center; gap: 6px;
  background: rgba(255,255,255,.12);
  border: 1.5px solid rgba(255,255,255,.25);
  color: white;
  border-radius: var(--radio-md);
  padding: 7px 14px;
  font-size: 13px; font-weight: 500;
  transition: background var(--trans);
}
.btn-salir:hover { background: rgba(255,255,255,.22); }

@media (max-width: 680px) {
  .topbar { padding: 0 18px; }
  .topbar-datos { display: none; }
}
</style>
