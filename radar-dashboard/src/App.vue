<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center p-6">
    <SessionTable />
  </div>
</template>

<script>
import SessionTable from "./components/SessionTable.vue";

export default {
  components: {
    SessionTable,
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
