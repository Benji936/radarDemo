<template>
  <div class="min-h-screen bg-gray-100 p-6">
    <h1 class="text-3xl font-bold mb-6 text-center">User Sessions Dashboard</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <SessionTable />
      <!-- <UserMap/>-->
      <!--<SessionChart />-->
    </div>
  </div>
</template>

<script>
import SessionTable from "../components/SessionTable.vue";
import SessionChart from "../components/SessionChart.vue";
import UserMap from "../components/UserMap.vue";

export default {
  components: { SessionTable, SessionChart, UserMap },
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
