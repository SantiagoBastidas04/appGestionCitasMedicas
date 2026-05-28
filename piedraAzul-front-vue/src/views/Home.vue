<template>
  <div class="home-page">
    <AppHeader />

    <main class="home-main">
      <!-- Hero Section -->
      <section class="hero">
        <ImageCarousel :images="carouselImages" :autoplay="true" :autoplayDelay="5000" />
      </section>

      <!-- Sección de Características -->
      <section class="features" id="servicios">
        <div class="container">
          <h2 class="section-title">¿Por qué elegir Piedrazul?</h2>
          <p class="section-subtitle">Tu plataforma de confianza para gestionar tu salud</p>

          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M14 20L18 24L26 16" stroke="var(--azul)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Fácil de Usar</h3>
              <p class="feature-description">Interfaz intuitiva que te permite agendar citas en pocos clics</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M20 12V20L26 24" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Disponibilidad 24/7</h3>
              <p class="feature-description">Accede a nuestro sistema en cualquier momento del día</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M26 14L16 24M26 24L16 14" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Seguridad Garantizada</h3>
              <p class="feature-description">Tus datos médicos están protegidos con la máxima seguridad</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M20 14V26M14 20H26" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Notificaciones Automáticas</h3>
              <p class="feature-description">Recibe recordatorios de tus citas programadas</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M12 20H28M20 12V28" stroke="var(--azul)" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Soporte Profesional</h3>
              <p class="feature-description">Equipo experto disponible para ayudarte en todo momento</p>
            </div>

            <div class="feature-card">
              <div class="feature-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="18" stroke="var(--azul)" stroke-width="2"/>
                  <path d="M20 14L24 20L20 26L16 20Z" stroke="var(--azul)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <h3 class="feature-title">Historial Completo</h3>
              <p class="feature-description">Accede a tu historial médico en un solo lugar</p>
            </div>
          </div>
        </div>
      </section>

      <!-- CTA Section -->
      <section class="cta-section">
        <div class="container">
          <div class="cta-content">
            <h2 class="cta-title">¿Listo para comenzar?</h2>
            <p class="cta-text">Únete a miles de pacientes que confían en Piedrazul para su cuidado médico</p>
            <div class="cta-buttons">
              <router-link to="/login" class="btn btn-primary">Ingresar</router-link>
              <a href="#contacto" class="btn btn-secondary">Más Información</a>
            </div>
          </div>
        </div>
      </section>

      <!-- Sección de Contacto -->
      <section class="contact-section" id="contacto">
        <div class="container">
          <h2 class="section-title">Contacto</h2>
          <p class="section-subtitle">¿Tienes preguntas? Nos encantaría ayudarte</p>

          <div class="contact-grid">
            <div class="contact-card">
              <h3>Correo Electrónico</h3>
              <p><a href="mailto:info@piedrazul.com">info@piedrazul.com</a></p>
            </div>
            <div class="contact-card">
              <h3>Teléfono</h3>
              <p><a href="tel:+1234567890">+1 (234) 567-890</a></p>
            </div>
            <div class="contact-card">
              <h3>Disponibilidad</h3>
              <p>Lunes a Viernes: 8:00 AM - 6:00 PM<br>Sábado: 9:00 AM - 2:00 PM</p>
            </div>
          </div>
        </div>
      </section>
    </main>

    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from '../components/organisms/AppHeader.vue'
import AppFooter from '../components/organisms/AppFooter.vue'
import ImageCarousel from '../components/molecules/ImageCarousel.vue'
import imagenDisponibilidad from '../assets/images/carousel/disponibilidad-24-7.png'
import imagenMedico from '../assets/images/carousel/atencion.png'

const carouselImages = [
  {
    src: imagenMedico,
    alt: 'Cuidado médico profesional',
    title: 'Cuidado Médico Profesional',
    description: 'Servicios de salud de calidad con profesionales certificados'
  },
  {
    src: 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=1200&h=500&fit=crop',
    alt: 'Agendamiento fácil',
    title: 'Agendamiento Fácil',
    description: 'Reserva tus citas médicas en minutos desde tu dispositivo'
  },
  {
    src: imagenDisponibilidad,
    alt: 'Disponibilidad 24/7',
    title: 'Disponibilidad 24/7',
    description: 'Accede a tu información médica cuando y donde lo necesites'
  }
]
</script>

<style scoped>
.home-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.home-main {
  flex: 1;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* Hero Section */
.hero {
  padding: 2rem 0;
}

/* Features Section */
.features {
  padding: 4rem 0;
  background: var(--verde-claro);
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--texto);
  margin: 0 0 0.5rem 0;
  letter-spacing: -0.5px;
  text-align: center;
}

.section-subtitle {
  font-size: 1.1rem;
  color: var(--texto-suave);
  margin: 0 0 2.5rem 0;
  text-align: center;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: var(--blanco);
  padding: 2rem;
  border-radius: var(--radio-lg);
  text-align: center;
  transition: all var(--trans);
  border: 1px solid var(--borde);
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--sombra-md);
  border-color: var(--verde-borde);
}

.feature-icon {
  width: 70px;
  height: 70px;
  margin: 0 auto 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--azul-claro);
  border-radius: var(--radio-md);
}

.feature-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--texto);
  margin: 0 0 0.75rem 0;
  letter-spacing: -0.3px;
}

.feature-description {
  font-size: 0.95rem;
  color: var(--texto-suave);
  margin: 0;
  line-height: 1.5;
}

/* CTA Section */
.cta-section {
  padding: 4rem 0;
  background: linear-gradient(135deg, var(--azul) 0%, var(--verde-medio) 100%);
  color: var(--blanco);
}

.cta-content {
  text-align: center;
}

.cta-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  letter-spacing: -0.5px;
}

.cta-text {
  font-size: 1.1rem;
  margin: 0 0 2rem 0;
  opacity: 0.95;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 0.75rem 2rem;
  border-radius: var(--radio-md);
  border: none;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  text-decoration: none;
  display: inline-block;
  transition: all var(--trans);
}

.btn-primary {
  background: var(--blanco);
  color: var(--azul);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: var(--sombra-md);
}

.btn-secondary {
  background: transparent;
  color: var(--blanco);
  border: 2px solid var(--blanco);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

/* Contact Section */
.contact-section {
  padding: 4rem 0;
  background: var(--blanco);
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.contact-card {
  padding: 2rem;
  background: var(--verde-claro);
  border-radius: var(--radio-lg);
  text-align: center;
  border: 1px solid var(--verde-borde);
}

.contact-card h3 {
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--texto);
  margin: 0 0 1rem 0;
}

.contact-card p {
  color: var(--texto-suave);
  margin: 0;
  font-size: 1rem;
}

.contact-card a {
  color: var(--azul);
  text-decoration: none;
  font-weight: 600;
  transition: color var(--trans);
}

.contact-card a:hover {
  color: var(--verde-oscuro);
  text-decoration: underline;
}

/* Responsive */
@media (max-width: 768px) {
  .features {
    padding: 2.5rem 0;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .section-title {
    font-size: 1.75rem;
  }

  .section-subtitle {
    font-size: 1rem;
    margin-bottom: 1.5rem;
  }

  .cta-section {
    padding: 2.5rem 0;
  }

  .cta-title {
    font-size: 1.75rem;
  }

  .cta-text {
    font-size: 1rem;
  }

  .cta-buttons {
    gap: 0.75rem;
  }

  .btn {
    padding: 0.65rem 1.5rem;
    font-size: 0.95rem;
  }

  .contact-section {
    padding: 2.5rem 0;
  }

  .contact-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}

@media (max-width: 480px) {
  .cta-buttons {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
