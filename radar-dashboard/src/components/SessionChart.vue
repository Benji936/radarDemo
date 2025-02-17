    <template>
        <div class="p-6 bg-white shadow-lg rounded-lg">
            <div >
                <select v-model="charType" style="padding: 10px; border-radius: 10px;">
                    <option>bar</option>
                    <option>pie</option>
                    <option>line</option>
                    <option>doughnut</option>
                </select>
                <select @change="updateChart" default="day_of_week" style="padding: 10px; border-radius: 10px;" v-model="specialKey">
                    <option :value=key v-for="(value,key) in sessions[0]" v-bind:key="key">{{ key }}</option>
                </select>                
            </div>
            
            <h2 class="text-2xl font-semibold mb-4">Browser Usage</h2>
            <BaseChart v-if="chartDataReady" :type="charType" :chartData="chartData" />
            <p v-else class="text-gray-500">Loading chart data...</p>
        </div>
    </template>
    
    <script>
    import { defineComponent, ref, onMounted } from "vue";
    import BaseChart from "./BaseChart.vue";
    import { getAllSessions } from "../api";
    
    export default defineComponent({
        components: { BaseChart },
        data(){
            return{
                charType: "bar",

            }
        },
        setup() {
            const chartDataReady = ref(false);
            const chartData = ref({
                labels: [],
                datasets: [],
            });

            let sessions = ref([])
            let specialKey = ref("day_of_week");

            const updateChart = () => {
                console.log("non")
                if (!sessions.value.length) return;
                console.log("oui")
                const dataCounts = {};
                sessions.value.forEach((session) => {
                    if (session[specialKey.value]) {
                        dataCounts[session[specialKey.value]] = (dataCounts[session[specialKey.value]] || 0) + 1;
                    }
                });

                if (Object.keys(dataCounts).length === 0) {
                    console.warn("No valid data found for", specialKey.value);
                    return;
                }

                chartData.value = {
                    labels: Object.keys(dataCounts),
                    datasets: [
                        {
                            label: `Users per ${specialKey.value}`,
                            data: Object.values(dataCounts),
                            backgroundColor: ["#4CAF50", "#FF9800", "#2196F3", "#E91E63", "#9C27B0"],
                        },
                    ],
                };

                chartDataReady.value = true;
            };

        
            onMounted(async () => {
                
                try {
                    sessions.value = await getAllSessions();
                    if (!sessions.value || sessions.value.length === 0) {
                        console.warn("No session data available.");
                        return;
                    }
            
                    // Count browser usage
                    const browserCounts = {};
                    sessions.value.forEach((session) => {
                        
                        if (session[specialKey.value]) {
                            browserCounts[session[specialKey.value]] = (browserCounts[session[specialKey.value]] || 0) + 1;
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
            //watch(specialKey, updateChart);
            return { chartData, chartDataReady, specialKey, sessions, updateChart};
        },
    });
    </script>
    