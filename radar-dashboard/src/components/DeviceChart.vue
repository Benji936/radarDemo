<template>
    <div class="p-6 bg-white shadow-lg rounded-lg">
    <h2 class="text-2xl font-semibold mb-4">Device Type Distribution</h2>
    <PieChart v-if="chartDataReady" :chart-data="chartData" :chart-options="chartOptions" />
    <p v-else class="text-gray-500">Loading chart...</p>
    </div>
</template>
    
<script>
    import { defineComponent, ref, onMounted } from "vue";
    import { Pie } from "vue-chartjs";
    import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
    import { getSessions } from "../api";
    
    ChartJS.register(ArcElement, Tooltip, Legend);
    
    export default defineComponent({
        components: { PieChart: Pie },
        props: {
            chartData: Object,
        },
        setup(props) {
            

            watch(
                () => props.chartData,
                (newData) => {
                    if (newData && newData.labels && newData.datasets) {
                        chartDataReady.value = true; // Mark as ready when data is available
                    }
                },
                { immediate: true }
            );


            const chartDataReady = ref(false);
            const chartOptions = {
                    responsive: true,
                    plugins: {
                    legend: { display: true },
                },
            };
            const chartData = ref({ labels: [], datasets: [] });
        
            onMounted(async () => {
                try {
                    const sessions = await getSessions();
            
                    if (!sessions || sessions.length === 0) {
                        console.warn("No session data available.");
                        return;
                    }
            
                    // Count device types
                    const deviceCounts = {};
                    sessions.forEach((session) => {
                        if (session.deviceType) {
                        deviceCounts[session.deviceType] = (deviceCounts[session.deviceType] || 0) + 1;
                        }
                    });
            
                    if (Object.keys(deviceCounts).length === 0) {
                        console.warn("No valid device data found.");
                        return;
                    }
            
                    chartData.value = {
                        labels: Object.keys(deviceCounts),
                        datasets: [
                        {
                            label: "Users per Device",
                            data: Object.values(deviceCounts),
                            backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                        },
                        ],
                    };
            
                    chartDataReady.value = true;
                } catch (error) {
                    console.error("Error loading chart data:", error);
                }
            });
        
            return { chartData, chartDataReady, chartOptions };
        },
    });
</script>
    