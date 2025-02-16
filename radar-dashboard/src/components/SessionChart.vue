    <template>
        <div class="p-6 bg-white shadow-lg rounded-lg">
            <h2 class="text-2xl font-semibold mb-4">Browser Usage</h2>
            <BaseChart v-if="chartDataReady" type="bar" :chartData="chartData" />
            <p v-else class="text-gray-500">Loading chart data...</p>
        </div>
    </template>
    
    <script>
    import { defineComponent, ref, onMounted } from "vue";
    import BaseChart from "./BaseChart.vue";
    import { getAllSessions } from "../api";
    
    export default defineComponent({
        components: { BaseChart },
        setup() {
        const chartDataReady = ref(false);
        const chartData = ref({
            labels: [],
            datasets: [],
        });
    
        onMounted(async () => {
            try {
            const sessions = await getAllSessions();
    
            if (!sessions || sessions.length === 0) {
                console.warn("No session data available.");
                return;
            }
    
            // Count browser usage
            const browserCounts = {};
            sessions.forEach((session) => {
                if (session.browser) {
                browserCounts[session.browser] = (browserCounts[session.browser] || 0) + 1;
                }
            });
    
            if (Object.keys(browserCounts).length === 0) {
                console.warn("No valid browser data found.");
                return;
            }
    
            // Correctly set chart data
            chartData.value = {
                labels: Object.keys(browserCounts),
                datasets: [
                {
                    label: "Users per Browser",
                    data: Object.values(browserCounts),
                    backgroundColor: ["#4CAF50", "#FF9800", "#2196F3", "#E91E63", "#9C27B0"],
                },
                ],
            };
    
            chartDataReady.value = true; // Mark data as ready for rendering
            } catch (error) {
                console.error("Error loading chart data:", error);
            }
        });
    
            return { chartData, chartDataReady };
        },
    });
    </script>
    