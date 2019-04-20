import Vue from 'vue';
import Vuex from 'vuex';
import newdev from './modules/newdev';
import getters from './getters';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        newdev
    },
    getters
});
export default store;