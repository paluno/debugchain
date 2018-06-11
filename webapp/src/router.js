import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Debug from './views/Debug'
import Login from './views/Login.vue'
import Profile from './views/Profile.vue'
import IssueDetail from './views/IssueDetail'
import UserSession from './auth'

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    {
      // TODO replace with start page 
      path: '/',
      redirect: {
        name: 'home',
        params: {
          projectId: 1
        }
      }
    },
    {
      path: '/projects/:projectId',
      name: 'home',
      component: Home,
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
