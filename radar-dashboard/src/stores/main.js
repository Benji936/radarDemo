// stores/main.js
import { defineStore } from 'pinia';

export const useMainStore = defineStore('main', {
    state: () => ({
        count: 0,
        clusters: 2,
        message: "",
    }),
    actions: {
        increment() {
            this.count++;
        },
        updateMessage(newMessage) {
            this.message = newMessage;
        },
        updateClusters(n){
            this.clusters = n;
        }
    }
});
