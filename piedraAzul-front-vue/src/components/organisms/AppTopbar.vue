<template>
  <header class="topbar">
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
  </header>
</template>

<style scoped>
.topbar {
  background: var(--verde-oscuro);
  color: var(--blanco);
  padding: 0 32px;
  height: 64px;
  display: flex;
  align-items: center;
  gap: 14px;
  position: sticky;
  top: 0;
  z-index: 200;
  box-shadow: 0 2px 12px rgba(0,0,0,.18);
}
.topbar-logo {
  width: 38px; height: 38px;
  background: rgba(255,255,255,.15);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.topbar-info h1 {
  font-family: 'DM Serif Display', serif;
  font-size: 18px;
  font-weight: 400;
  line-height: 1.2;
  letter-spacing: .01em;
}
.topbar-info p { font-size: 12px; opacity: .65; font-weight: 300; }
@media (max-width: 680px) { .topbar { padding: 0 18px; } }
</style>
