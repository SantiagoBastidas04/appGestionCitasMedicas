<template>
  <footer class="app-footer">
    <div class="footer-container">
      <div class="footer-grid">
        <!-- Columna 1: Sobre Piedrazul -->
        <div class="footer-section">
          <h3 class="footer-title">Sobre Piedrazul</h3>
          <p class="footer-text">
            Sistema integral de agendamiento médico que facilita la gestión eficiente de citas y pacientes.
          </p>
          <div class="footer-logo">
            <svg width="24" height="24" viewBox="0 0 32 32" fill="none">
              <circle cx="16" cy="16" r="14" stroke="var(--azul)" stroke-width="2"/>
              <path d="M16 8V24M8 16H24" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span>Piedrazul</span>
          </div>
        </div>

        <!-- Columna 2: Navegación -->
        <div class="footer-section">
          <h3 class="footer-title">Navegación</h3>
          <ul class="footer-links">
            <li><router-link to="/">Inicio</router-link></li>
            <li><router-link to="/login">Ingresar</router-link></li>
            <li><a href="#servicios">Servicios</a></li>
            <li><a href="#contacto">Contacto</a></li>
          </ul>
        </div>

        <!-- Columna 3: Servicios -->
        <div class="footer-section">
          <h3 class="footer-title">Servicios</h3>
          <ul class="footer-links">
            <li><a href="#consultas">Consultas Médicas</a></li>
            <li><a href="#reagendar">Reagendar Citas</a></li>
            <li><a href="#historial">Historial Médico</a></li>
            <li><a href="#notificaciones">Notificaciones</a></li>
          </ul>
        </div>

        <!-- Columna 4: Contacto -->
        <div class="footer-section">
          <h3 class="footer-title">Contacto</h3>
          <ul class="footer-links">
            <li>
              <a href="mailto:info@piedrazul.com">info@piedrazul.com</a>
            </li>
            <li>
              <a href="tel:+1234567890">+1 (234) 567-890</a>
            </li>
            <li>
              Disponible 24/7
            </li>
          </ul>
        </div>
      </div>

      <!-- Divisor -->
      <div class="footer-divider"></div>

      <!-- Copyright -->
      <div class="footer-bottom">
        <p class="copyright">
          &copy; {{ currentYear }} Piedrazul. Todos los derechos reservados.
        </p>
        <div class="footer-bottom-links">
          <a href="#privacidad">Política de Privacidad</a>
          <a href="#terminos">Términos de Servicio</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const currentYear = ref(new Date().getFullYear())
</script>

<style scoped>
.app-footer {
  background: var(--blanco);
  border-top: 1px solid var(--borde);
  margin-top: 4rem;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 1.5rem 2rem;
}

.footer-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.footer-title {
  font-size: 1rem;
  font-weight: 700;
  color: var(--texto);
  margin: 0;
  letter-spacing: -0.3px;
}

.footer-text {
  font-size: 0.9rem;
  color: var(--texto-suave);
  line-height: 1.5;
  margin: 0;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--azul);
  font-weight: 600;
  font-size: 0.95rem;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.footer-links li a {
  color: var(--texto-suave);
  text-decoration: none;
  font-size: 0.9rem;
  transition: color var(--trans);
}

.footer-links li a:hover {
  color: var(--azul);
}

.footer-links li {
  color: var(--texto-suave);
  font-size: 0.9rem;
}

.footer-divider {
  height: 1px;
  background: var(--borde);
  margin-bottom: 1.5rem;
}

.footer-bottom {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
  justify-content: space-between;
}

.copyright {
  color: var(--texto-tenue);
  font-size: 0.85rem;
  margin: 0;
  text-align: center;
}

.footer-bottom-links {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
  justify-content: center;
}

.footer-bottom-links a {
  color: var(--texto-suave);
  text-decoration: none;
  font-size: 0.85rem;
  transition: color var(--trans);
}

.footer-bottom-links a:hover {
  color: var(--azul);
}

/* Responsive */
@media (max-width: 768px) {
  .footer-container {
    padding: 2rem 1.5rem 1.5rem;
  }

  .footer-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1.5rem;
  }

  .footer-bottom {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>
