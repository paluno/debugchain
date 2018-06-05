import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Debug from './views/Debug'
import Login from './views/Login.vue'
import Profile from './views/Profile.vue'
import IssueDetail from './views/IssueDetail'
import {UserSession} from './auth'

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: Profile,
      meta: { requiresAuth: true }
    },
    {
      path: '/debug',
      name: 'debug',
      component: Debug
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/issue',
      name: 'issue',
      component: IssueDetail,
      meta: { requiresAuth: true }      
    }
  ]
});

//Die Routen durchlaufen und jeweils auf Auth prüfen.
//Ist der Nutzer nicht eingeloggt werden alle Seiten die Auth benötigen auf /login umgeleitet um den Login durchzuführen
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth) && !UserSession.loggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
});

export default router;
