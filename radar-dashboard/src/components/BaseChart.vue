    <template>
        <div>
        <Bar v-if="chartDataReady" :data="chartData" :options="chartOptions" />
        <p v-else class="text-gray-500">Loading chart...</p>
        </div>
    </template>
    
    <script>
    import { defineComponent, ref, watch } from "vue";
    import { Bar } from "vue-chartjs";
    import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Tooltip, Legend } from "chart.js";
    
    ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);
    
    export default defineComponent({
        components: { Bar },
        props: {
        chartData: Object, // Expecting chartData as a prop
        },
        setup(props) {
        const chartDataReady = ref(false);
    
        watch(
            () => props.chartData,
            (newData) => {
                if (newData && newData.labels && newData.datasets) {
                    chartDataReady.value = true; // Mark as ready when data is available
                }
            },
            { immediate: true }
        );
    
        const chartOptions = {
            responsive: true,
            plugins: {
            legend: { display: true },
            },
        };
    
            return { chartDataReady, chartOptions };
        },
    });
    </script>
    