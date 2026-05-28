<template>
  <div class="login-fondo">
    <div class="login-card">

      <!-- Botón Volver al Inicio -->
      <router-link to="/" class="btn-volver">
        ← Volver al inicio
      </router-link>

      <!-- Logo clickeable para volver a Home -->
      <router-link to="/" class="login-logo-link">
        <div class="login-logo">
          <svg width="26" height="26" fill="none" viewBox="0 0 24 24">
            <path d="M12 2C8 2 4 5.5 4 10c0 6 8 12 8 12s8-6 8-12c0-4.5-4-8-8-8z"
                  stroke="#0D6B4E" stroke-width="1.5" fill="none"/>
            <circle cx="12" cy="10" r="2.5" stroke="#0D6B4E" stroke-width="1.5" fill="none"/>
          </svg>
        </div>
      </router-link>

      <h1>Piedrazul</h1>
      <p class="login-sub">Sistema de agendamiento médico</p>

      <!-- Tabs -->
      <div class="login-tabs">
        <button
          :class="['login-tab', { active: tab === 'login' }]"
          @click="tab = 'login'"
        >Iniciar sesión</button>
        <button
          :class="['login-tab', { active: tab === 'registro' }]"
          @click="tab = 'registro'"
        >Registrarse</button>
      </div>

      <!-- ── PANEL LOGIN ── -->
      <div v-show="tab === 'login'" class="login-panel">
        <div v-if="errorLogin" class="login-error">{{ errorLogin }}</div>

        <div class="campos-stack">
          <AppCampo
            label="Usuario"
            v-model="loginForm.username"
            placeholder="Su nombre de usuario"
            @enter="hacerLogin"
          />
          <AppCampo
            label="Contraseña"
            tipo="password"
            v-model="loginForm.password"
            placeholder="Su contraseña"
            @enter="hacerLogin"
          />
        </div>

        <AppButton
          style="width:100%; justify-content:center; margin-top:20px;"
          :loading="cargando"
          @click="hacerLogin"
        >
          Ingresar
        </AppButton>

        <div class="login-divider">solo pacientes nuevos</div>
        <p class="login-pie">
          ¿No tiene cuenta?
          <button class="link-btn" @click="tab = 'registro'">Regístrese aquí</button>
        </p>
      </div>

      <!-- ── PANEL REGISTRO ── -->
      <div v-show="tab === 'registro'" class="login-panel">
        <AppAlerta tipo="info" titulo="Solo para pacientes">
          Médicos y agendadores son creados por el administrador
        </AppAlerta>

        <div v-if="errorRegistro" class="login-error">{{ errorRegistro }}</div>

        <div class="form-grid">
          <AppCampo label="Usuario *"            v-model="regForm.username"    placeholder="usuario123"           :errorMsg="errReg.username" />
          <AppCampo label="Contraseña *"         v-model="regForm.password"    placeholder="Mín. 6 caracteres"    tipo="password"  :errorMsg="errReg.password" />
          <AppCampo label="Nombres *"            v-model="regForm.nombres"     placeholder="Nombres completos"    :errorMsg="errReg.nombres" />
          <AppCampo label="Apellidos *"          v-model="regForm.apellidos"   placeholder="Apellidos completos"  :errorMsg="errReg.apellidos" />
          <AppCampo label="Documento *"          v-model="regForm.documento"   placeholder="Número de identidad"  :errorMsg="errReg.documento" />
          <AppCampo label="Celular *"            v-model="regForm.celular"     placeholder="3001234567"           tipo="tel" :errorMsg="errReg.celular" />
          <AppCampo label="Género *"             v-model="regForm.genero"      tipo="select" :errorMsg="errReg.genero">
            <option value="MASCULINO">Hombre</option>
            <option value="FEMENINO">Mujer</option>
            <option value="OTRO">Otro</option>
          </AppCampo>
          <AppCampo label="Fecha nacimiento"     v-model="regForm.nacimiento"  tipo="date" :opcional="true" />
          <AppCampo
            label="Correo electrónico"
            v-model="regForm.correo"
            tipo="email"
            placeholder="correo@ejemplo.com"
            :opcional="true"
            class="form-full"
          />
        </div>

        <AppButton
          style="width:100%; justify-content:center; margin-top:18px;"
          :loading="cargando"
          @click="hacerRegistro"
        >
          Crear cuenta
        </AppButton>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import AppCampo  from '../components/atoms/AppCampo.vue'
import AppButton from '../components/atoms/AppButton.vue'
import AppAlerta from '../components/atoms/AppAlerta.vue'
import { useAuth }  from '../composables/useAuth.js'
import { useToast } from '../composables/useToast.js'

const router = useRouter()
const { login, registro, esPaciente } = useAuth()
const { toast }           = useToast()

const tab          = ref('login')
const cargando     = ref(false)
const errorLogin   = ref('')
const errorRegistro = ref('')

// ── Login ──────────────────────────────────
const loginForm = reactive({ username: '', password: '' })

async function hacerLogin() {
  errorLogin.value = ''
  if (!loginForm.username || !loginForm.password) {
    errorLogin.value = 'Ingrese usuario y contraseña'; return
  }
  cargando.value = true
  try {
    await login(loginForm.username, loginForm.password)
    toast('¡Bienvenido!')
    // Redirigir según el rol después de login exitoso
    setTimeout(() => {
      if (esPaciente.value) {
        router.push('/portal')
      } else {
        router.push('/citas')
      }
    }, 500)
  } catch (e) {
    errorLogin.value = e?.message || 'Usuario o contraseña incorrectos'
  } finally {
    cargando.value = false
  }
}

// ── Registro ───────────────────────────────
const regForm = reactive({
  username: '', password: '', nombres: '', apellidos: '',
  documento: '', celular: '', genero: '', nacimiento: '', correo: '',
})
const errReg = reactive({})

async function hacerRegistro() {
  errorRegistro.value = ''
  // Limpiar errores previos
  Object.keys(errReg).forEach(k => delete errReg[k])

  let ok = true
  const req = { username:'Usuario', password:'Contraseña', nombres:'Nombres',
                apellidos:'Apellidos', documento:'Documento', celular:'Celular', genero:'Género' }
  for (const [campo, label] of Object.entries(req)) {
    if (!regForm[campo]) { errReg[campo] = `${label} es obligatorio`; ok = false }
  }
  if (regForm.username && regForm.username.trim().length < 3) {
    errReg.username = 'El usuario debe tener al menos 3 caracteres'
    ok = false
  }
  if (regForm.password && regForm.password.length < 6) {
    errReg.password = 'Mínimo 6 caracteres'; ok = false
  }
  if (!ok) return

  cargando.value = true
  try {
    await registro({
      username:        regForm.username,
      password:        regForm.password,
      nombres:         regForm.nombres,
      apellidos:       regForm.apellidos,
      numeroDocumento: regForm.documento,
      celular:         regForm.celular,
      genero:          regForm.genero,
      fechaNacimiento:    regForm.nacimiento  || null,
      correoElectronico:  regForm.correo      || null,
    })
    toast('¡Cuenta creada! Bienvenido.')
    // Redirigir a portal del paciente después de registro exitoso
    setTimeout(() => {
      router.push('/portal')
    }, 500)
  } catch (e) {
    if (Array.isArray(e?.fieldErrors) && e.fieldErrors.length) {
      const fieldMap = {
        numeroDocumento: 'documento',
        correoElectronico: 'correo',
        fechaNacimiento: 'nacimiento',
      }
      e.fieldErrors.forEach(({ campo, mensaje }) => {
        const localField = fieldMap[campo] || campo
        errReg[localField] = mensaje
      })
    }
    errorRegistro.value = e?.message || 'Error al registrarse. Intente nuevamente.'
  } finally {
    cargando.value = false
  }
}
</script>

<style scoped>
/* Fondo */
.login-fondo {
  min-height: 100vh;
  background: var(--crema);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
}

/* Tarjeta */
.login-card {
  background: var(--blanco);
  border: 1px solid var(--borde);
  border-radius: var(--radio-xl);
  padding: 40px 36px;
  width: 100%;
  max-width: 480px;
  box-shadow: 0 8px 40px rgba(13,107,78,.10);
}

/* Botón Volver */
.btn-volver {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: var(--verde);
  font-weight: 500;
  text-decoration: none;
  margin-bottom: 20px;
  transition: color var(--trans);
}

.btn-volver:hover {
  color: var(--verde-oscuro);
  text-decoration: underline;
}

/* Logo */
.login-logo-link {
  display: flex;
  justify-content: center;
  text-decoration: none;
  cursor: pointer;
}

.login-logo {
  width: 52px; height: 52px;
  background: var(--verde-claro);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 16px;
  border: 1.5px solid var(--verde-borde);
  transition: transform var(--trans), background var(--trans);
}

.login-logo-link:hover .login-logo {
  transform: scale(1.08);
  background: var(--verde);
}

.login-card h1 {
  font-family: 'DM Serif Display', serif;
  font-size: 28px;
  font-weight: 400;
  color: var(--verde-oscuro);
  text-align: center;
  margin-bottom: 4px;
}
.login-sub {
  font-size: 13px;
  color: var(--texto-tenue);
  text-align: center;
  margin-bottom: 24px;
}

/* Tabs */
.login-tabs {
  display: flex;
  border-bottom: 1.5px solid var(--borde);
  margin-bottom: 24px;
}
.login-tab {
  flex: 1;
  padding: 10px;
  font-size: 14px;
  font-weight: 500;
  color: var(--texto-suave);
  background: none;
  border: none;
  border-bottom: 2.5px solid transparent;
  margin-bottom: -1.5px;
  transition: color var(--trans), border-color var(--trans);
}
.login-tab:hover { color: var(--verde); }
.login-tab.active {
  color: var(--verde-oscuro);
  border-bottom-color: var(--verde);
  font-weight: 600;
}

/* Panel */
.login-panel { animation: fadeIn .18s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(4px); } to { opacity: 1; transform: translateY(0); } }

/* Error */
.login-error {
  background: var(--rojo-claro);
  color: var(--rojo);
  border: 1px solid var(--rojo-borde);
  border-radius: var(--radio-md);
  padding: 10px 14px;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 16px;
}

.campos-stack { display: flex; flex-direction: column; gap: 14px; }

/* Divider */
.login-divider {
  text-align: center;
  font-size: 12px;
  color: var(--texto-tenue);
  margin: 20px 0 12px;
  position: relative;
}
.login-divider::before,
.login-divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 28%;
  height: 1px;
  background: var(--borde);
}
.login-divider::before { left: 0; }
.login-divider::after  { right: 0; }

.login-pie {
  text-align: center;
  font-size: 13px;
  color: var(--texto-tenue);
}
.link-btn {
  background: none; border: none;
  color: var(--verde); font-weight: 600;
  font-size: 13px; cursor: pointer;
  padding: 0;
}
.link-btn:hover { text-decoration: underline; }

/* Grid registro */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  margin-top: 4px;
}
.form-full { grid-column: 1 / -1; }

@media (max-width: 480px) {
  .login-card { padding: 28px 20px; }
  .form-grid  { grid-template-columns: 1fr; }
}
</style>
