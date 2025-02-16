    <template>
        <div>
        <component v-if="chartDataReady" :data="chartData" :options="chartOptions" :is="chartComponent"/>
        <p v-else class="text-gray-500">Loading chart...</p>
        </div>
    </template>
    
    <script>
    import { defineComponent, ref, watch } from "vue";
    import { Bar, Line, Pie, Doughnut } from "vue-chartjs";
    import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale, BarElement, LineElement, PointElement } from "chart.js";

    ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale, BarElement, LineElement, PointElement);
    
    export default defineComponent({
        props: {
        type: {
            type: String,
            required: true,
            validator: (value) => ["bar", "line", "pie", "doughnut"].includes(value),
            },
            chartData: Object,
            title: String,
        },
        computed: {
            chartComponent() {
            const chartMap = {
                bar: Bar,
                line: Line,
                pie: Pie,
                doughnut: Doughnut,
            };
            return chartMap[this.type] || Bar; // Default to Bar Chart
            },
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
    