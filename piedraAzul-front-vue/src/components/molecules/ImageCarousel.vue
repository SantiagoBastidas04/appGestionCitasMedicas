<template>
  <div class="carousel-container">
    <div class="carousel-wrapper">
      <div class="carousel-track" :style="{ transform: `translateX(${-currentIndex * 100}%)` }">
        <div v-for="(image, index) in images" :key="index" class="carousel-slide">
          <img :src="image.src" :alt="image.alt" class="carousel-image" />
          <div class="carousel-overlay">
            <h3 class="slide-title">{{ image.title }}</h3>
            <p class="slide-description">{{ image.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Controles de navegación -->
    <button class="carousel-btn prev" @click="previous" aria-label="Imagen anterior">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
        <path d="M15 18L9 12L15 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </button>
    <button class="carousel-btn next" @click="next" aria-label="Imagen siguiente">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
        <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
    </button>

    <!-- Indicadores -->
    <div class="carousel-indicators">
      <button
        v-for="(_, index) in images"
        :key="index"
        class="indicator"
        :class="{ active: index === currentIndex }"
        @click="currentIndex = index"
        :aria-label="`Ir a imagen ${index + 1}`"
      ></button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const currentIndex = ref(0)
let autoplayInterval

const props = defineProps({
  images: {
    type: Array,
    required: true
  },
  autoplay: {
    type: Boolean,
    default: true
  },
  autoplayDelay: {
    type: Number,
    default: 5000
  }
})

const next = () => {
  currentIndex.value = (currentIndex.value + 1) % props.images.length
}

const previous = () => {
  currentIndex.value = (currentIndex.value - 1 + props.images.length) % props.images.length
}

const startAutoplay = () => {
  if (props.autoplay) {
    autoplayInterval = setInterval(next, props.autoplayDelay)
  }
}

const stopAutoplay = () => {
  if (autoplayInterval) {
    clearInterval(autoplayInterval)
  }
}

onMounted(() => {
  startAutoplay()
})

onUnmounted(() => {
  stopAutoplay()
})
</script>

<style scoped>
.carousel-container {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
  border-radius: var(--radio-lg);
  background: var(--verde-claro);
}

.carousel-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.carousel-track {
  display: flex;
  height: 100%;
  width: 100%;
  transition: transform 0.8s cubic-bezier(0.4, 0, 0.2, 1);
}

.carousel-slide {
  min-width: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(26, 95, 160, 0.4) 0%, rgba(26, 95, 160, 0.2) 100%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 3rem 2rem;
  color: var(--blanco);
}

.slide-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.slide-description {
  font-size: 1.1rem;
  margin: 0;
  opacity: 0.95;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.9);
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--azul);
  transition: all var(--trans);
  z-index: 10;
}

.carousel-btn:hover {
  background: var(--blanco);
  transform: translateY(-50%) scale(1.1);
  box-shadow: var(--sombra-md);
}

.carousel-btn.prev {
  left: 1.5rem;
}

.carousel-btn.next {
  right: 1.5rem;
}

.carousel-indicators {
  position: absolute;
  bottom: 1.5rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 0.75rem;
  z-index: 10;
}

.indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: none;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all var(--trans);
}

.indicator.active {
  background: var(--blanco);
  width: 30px;
  border-radius: 5px;
}

.indicator:hover {
  background: rgba(255, 255, 255, 0.8);
}

/* Responsive */
@media (max-width: 768px) {
  .carousel-container {
    height: 300px;
  }

  .slide-title {
    font-size: 1.75rem;
  }

  .slide-description {
    font-size: 0.9rem;
  }

  .carousel-overlay {
    padding: 2rem 1.5rem;
  }

  .carousel-btn {
    width: 40px;
    height: 40px;
  }

  .carousel-btn.prev {
    left: 1rem;
  }

  .carousel-btn.next {
    right: 1rem;
  }

  .carousel-indicators {
    bottom: 1rem;
    gap: 0.5rem;
  }

  .indicator {
    width: 8px;
    height: 8px;
  }

  .indicator.active {
    width: 24px;
  }
}

@media (max-width: 480px) {
  .carousel-container {
    height: 200px;
  }

  .slide-title {
    font-size: 1.25rem;
  }

  .slide-description {
    font-size: 0.8rem;
  }

  .carousel-overlay {
    padding: 1.5rem 1rem;
  }

  .carousel-btn {
    width: 35px;
    height: 35px;
  }

  .carousel-btn.prev {
    left: 0.75rem;
  }

  .carousel-btn.next {
    right: 0.75rem;
  }
}
</style>
