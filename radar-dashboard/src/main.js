import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import router from './router/index.js'
import Home from './views/Home.vue'

const piniata = createPinia();

createApp(Home).use(piniata).use(router).mount('#app')

export const useMainStore = defineStore('main', {
    state: () => ({
        message: ''
    }),
    actions: {
        setMessage(newMessage) {
        this.message = newMessage;
        }
    }
});
