import Vue from 'vue'
import Router from 'vue-router'

import IssueList from './views/IssueList.vue'
import Debug from './views/Debug'
import Login from './views/Login.vue'
import Profile from './views/Profile.vue'
import IssueDetail from './views/IssueDetail'
import ProjectList from './views/ProjectList'

import UserSession from './auth'

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: {
        name: 'projects',
      }
    },
    {
      path: '/projects',
      name: 'projects',
      component: ProjectList,
      meta: { requiresAuth: true }
    },
    {
      path: '/projects/:projectId',
      name: 'issueList',
      component: IssueList,
      props: true,
      meta: { requiresAuth: true }
    },
    {
      path: '/projects/:projectId/profile',
      name: 'profile',
      component: Profile,
      props: true,
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
      path: '/projects/:projectId/issue/:issueId',
      name: 'issue',
      component: IssueDetail,
      props: true,
      meta: { requiresAuth: true }
    }
  ]
});

UserSession.checkCookie(); // Prüft vorab, ob Cookie vorhanden und setzt dann ggf. den login-State

//Die Routen durchlaufen und jeweils auf Auth prüfen.
//Ist der Nutzer nicht eingeloggt werden alle Seiten die Auth benötigen auf /login umgeleitet um den Login durchzuführen
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth) && !UserSession.state.loggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else {
    next();
  }
});

export default router;
