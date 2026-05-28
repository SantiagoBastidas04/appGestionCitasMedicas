<template>
  <header class="app-header">
    <div class="header-container">
      <div class="logo-section">
        <div class="logo-icon">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
            <circle cx="16" cy="16" r="14" stroke="var(--azul)" stroke-width="2"/>
            <path d="M16 8V24M8 16H24" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <h1 class="logo-text">Piedrazul</h1>
      </div>

      <nav class="nav-desktop">
        <router-link to="/" class="nav-link">Inicio</router-link>
        <router-link to="/login" class="nav-link">Ingresar</router-link>
        <a href="#servicios" class="nav-link">Servicios</a>
        <a href="#contacto" class="nav-link">Contacto</a>
      </nav>

      <button class="menu-toggle" @click="menuAbierto = !menuAbierto" :aria-expanded="menuAbierto">
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>

    <!-- Menú móvil -->
    <nav v-show="menuAbierto" class="nav-mobile">
      <router-link to="/" class="nav-link-mobile" @click="menuAbierto = false">Inicio</router-link>
      <router-link to="/login" class="nav-link-mobile" @click="menuAbierto = false">Ingresar</router-link>
      <a href="#servicios" class="nav-link-mobile" @click="menuAbierto = false">Servicios</a>
      <a href="#contacto" class="nav-link-mobile" @click="menuAbierto = false">Contacto</a>
    </nav>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const menuAbierto = ref(false)

const cerrarMenu = () => {
  menuAbierto.value = false
}
</script>

<style scoped>
.app-header {
  background: var(--blanco);
  border-bottom: 1px solid var(--borde);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--sombra-sm);
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  text-decoration: none;
  color: var(--azul);
  cursor: pointer;
}

.logo-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--azul-claro);
  border-radius: var(--radio-md);
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--azul);
  margin: 0;
  letter-spacing: -0.5px;
}

.nav-desktop {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-link {
  color: var(--texto);
  text-decoration: none;
  font-weight: 500;
  font-size: 0.95rem;
  transition: color var(--trans);
  position: relative;
}

.nav-link:hover {
  color: var(--azul);
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--azul);
  transition: width var(--trans);
}

.nav-link:hover::after {
  width: 100%;
}

.nav-link.router-link-active {
  color: var(--azul);
}

.nav-link.router-link-active::after {
  width: 100%;
}

.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
}

.menu-toggle span {
  width: 24px;
  height: 2px;
  background: var(--texto);
  border-radius: 1px;
  transition: all var(--trans);
}

.menu-toggle[aria-expanded="true"] span:nth-child(1) {
  transform: rotate(45deg) translate(8px, 8px);
}

.menu-toggle[aria-expanded="true"] span:nth-child(2) {
  opacity: 0;
}

.menu-toggle[aria-expanded="true"] span:nth-child(3) {
  transform: rotate(-45deg) translate(7px, -7px);
}

.nav-mobile {
  display: none;
  flex-direction: column;
  padding: 1rem 1.5rem;
  background: var(--verde-claro);
  border-bottom: 1px solid var(--borde);
}

.nav-link-mobile {
  color: var(--texto);
  text-decoration: none;
  font-weight: 500;
  padding: 0.75rem 0;
  display: block;
  transition: color var(--trans);
}

.nav-link-mobile:hover {
  color: var(--azul);
}

/* Responsive */
@media (max-width: 768px) {
  .nav-desktop {
    display: none;
  }

  .menu-toggle {
    display: flex;
  }

  .nav-mobile {
    display: flex;
  }

  .logo-text {
    font-size: 1.25rem;
  }
}
</style>
