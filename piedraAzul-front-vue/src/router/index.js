import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoginView from '../views/LoginView.vue'
import PortalPaciente from '../views/PortalPaciente.vue'
import ListarCitas from '../views/ListarCitas.vue'
import CrearCita from '../views/CrearCita.vue'
import Configuracion from '../views/Configuracion.vue'
import MisCitas from '../views/MisCitas.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: { requiresAuth: false }
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginView,
        meta: { requiresAuth: false }
    },
    {
        path: '/portal',
        name: 'Portal',
        component: PortalPaciente,
        meta: { requiresAuth: true }
    },
    {
        path: '/citas',
        name: 'Citas',
        component: ListarCitas,
        meta: { requiresAuth: true }
    },
    {
        path: '/crear-cita',
        name: 'CrearCita',
        component: CrearCita,
        meta: { requiresAuth: true }
    },
    {
        path: '/configuracion',
        name: 'Configuracion',
        component: Configuracion,
        meta: { requiresAuth: true }
    },
    {
        path: '/mis-citas',
        name: 'MisCitas',
        component: MisCitas,
        meta: { requiresAuth: true }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return { top: 0 }
        }
    }
})

// Guard para redirigir pacientes logueados a /portal cuando intenten acceder a /
router.beforeEach((to, from, next) => {
    const usuarioGuardado = localStorage.getItem('usuario')
    if (usuarioGuardado) {
        const usuario = JSON.parse(usuarioGuardado)
        // Si es paciente y está en la raíz, redirigir a /portal
        if (usuario.rol === 'PACIENTE' && to.path === '/') {
            next('/portal')
            return
        }
    }
    next()
})

export default router
