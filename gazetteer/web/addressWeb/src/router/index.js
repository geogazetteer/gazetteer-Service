import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: resolve => require(['@/views/home'], resolve),
      children:[
        {
          path: 'siteSearch/',
          name: 'siteSearch',
          component: resolve => require(['@/views/siteSearch'], resolve)
        },
        {
          path: 'siteEdit/',
          name: 'siteEdit',
          component: resolve => require(['@/views/siteEdit'], resolve)
        },
      ]
    },
    {
      path: '/AddressWebService',
      name: 'webService',
      component:  resolve => require(['@/views/webService'], resolve),
    },
    {
      path: '/batchHandle',
      name: 'batchHandle',
      component:  resolve => require(['@/views/batchHandle'], resolve),
    },
  ]
})
