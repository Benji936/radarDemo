    <template>
        <div class="p-6 bg-white shadow-lg rounded-lg">
        <h2 class="text-2xl font-semibold mb-4">Dynamic User Segmentation</h2>

        <h4>{{ store.message }}</h4>
    
        <!-- Select Attributes -->
        <label class="block font-medium">Select Attributes:</label>
        <div class="flex flex-wrap">
            <label v-for="attr in availableAttributes" :key="attr" class="mr-4">
            <input type="checkbox" :value="attr" v-model="selectedAttributes" class="mr-1">
            {{ attr }}
            </label>
        </div>
    
        <!-- Choose Cluster Count -->
        <label class="block font-medium mt-4">Number of Clusters:</label>
        <input type="number" v-model="numClusters" min="2" max="10" class="border p-2 mb-4 w-24">
    
        <!-- Run Segmentation -->
        <button @update="updateStore" @click="runSegmentation" class="bg-blue-500 text-white px-4 py-2 rounded">
            Run Segmentation
        </button>
    
        <!-- Show Response -->
        <p v-if="message" class="mt-4 text-green-600">{{ message }}</p>
        </div>
    </template>
    
    <script>
    import axios from "axios";
    import { useMainStore } from '@/stores/main';


    


    export default {
        data() {
            return {
                availableAttributes: ["dayOfWeek", "sessionMonth", "dayOfMonth", "sessionHour", "isHoliday", "language", "device_brand", "referer_domain", "isCapital"],
                selectedAttributes: [],
                numClusters: useMainStore().clusters,
                store: useMainStore(),
            };
        },

        methods: {
            updateStoreClusters() {
                store.updateClusters(this.numClusters)
            },

            updateStore() {
                store.updateMessage(this.message);
            },

            async runSegmentation() {
                if (this.selectedAttributes.length === 0) {
                    this.message = "Please select at least one attribute.";
                    return;
                }
        
                try {
                const response = await axios.post(
                    `http://localhost:8080/api/segmentation/run?attributes=${this.selectedAttributes.join(",")}&clusters=${this.numClusters}`
                );
                    useMainStore().updateMessage(response.data);
                } catch (error) {
                    this.message = "Error running segmentation.";
                    console.error(error);
                }
            },

            mounted() {
                
            },
        },
    };
    </script>
    