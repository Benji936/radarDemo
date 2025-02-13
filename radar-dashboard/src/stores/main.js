// stores/main.js
import { defineStore } from 'pinia';

export const useMainStore = defineStore('main', {
    state: () => ({
        count: 0,
        clusters: 2,
        selectedAttributes: [],
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
        },
        addAttribute(attr){
            this.selectedAttributes.push(attr);
        },
        clearAttributes(){
            this.selectedAttributes = [];
        }
    },
    persist: true,
});
