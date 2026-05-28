import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoginView from '../views/LoginView.vue'
import PortalPaciente from '../views/PortalPaciente.vue'
import ListarCitas from '../views/ListarCitas.vue'
import CrearCita from '../views/CrearCita.vue'
import Configuracion from '../views/Configuracion.vue'

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

export default router
