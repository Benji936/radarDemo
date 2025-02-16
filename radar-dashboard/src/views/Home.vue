<template>
  <div class="min-h-screen bg-gray-100 p-6">
    <h1 class="text-3xl font-bold mb-6 text-center">Demo</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      
      <nav style="display: flex; flex-direction: row; gap: 24px;">
        <router-link to="/sessions">Sessions</router-link>
        <router-link to="/k-mean">K-mean</router-link>
      </nav>
      
      <RouterView/>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

  export default {
    data(){
      return {
        store: ref(null),
        message: ref("")
      }
    },

    methods: {
      mounted() {
        console.log("Bonjour")
        //this.store = useMainStore();
      },
    },
  };

  

  
</script>




<style scoped>
  .logo {
    height: 6em;
    padding: 1.5em;
    will-change: filter;
    transition: filter 300ms;
  }
  .logo:hover {
    filter: drop-shadow(0 0 2em #646cffaa);
  }
  .logo.vue:hover {
    filter: drop-shadow(0 0 2em #42b883aa);
  }
</style>
