    <template>
        <div class="p-6 bg-white shadow-lg rounded-lg flex-column" style="padding: 24px; gap: 24px;">

            <h4>{{ store.message }}</h4>

            <!-- Choose Cluster Count -->
            <label class="block font-medium mt-4" style="text-align: left;">Number of Clusters:</label>
            <input type="number" v-model="numClusters" min="2" max="10" class="border p-2 mb-4 w-24" style="width: 200px; padding: 10px; border-radius: 10px;">
        
            <!-- Select Attributes -->
            <label class="block font-medium" style="text-align: left;">Select Attributes:</label>
            <div class="flex-row"  style=" gap: 48px;">
                <div v-for="(attributes,Family) in allAttributes" v-bind:key="Family" class="flex-column" style="align-items: baseline; ">
                    <p style="margin-bottom: 16px;">{{Family}}</p>

                    <label v-for="attr in attributes" :key="attr" class="mr-4">
                        <input type="checkbox" @change="selected($event ,attr)" :checked="store.selectedAttributes.includes(attr)" class="mr-1">
                        {{ attr }}
                    </label>
                </div>
            </div>
        
            <!-- Run Segmentation -->
            <button @update="updateStore" @click="runSegmentation" class="bg-blue-500 text-white px-4 py-2 rounded">
                Run Segmentation
            </button>

            <button @click="store.clearAttributes()">Clear</button>

            <p style="color: red;">{{ errorMessage }}</p>
            <!--<p> {{ store.selectedAttributes }}</p>-->
        </div>
    </template>
    
    <script>
    import axios from "axios";
    import { useMainStore } from '@/stores/main';

    export default {
        data() {
            return {
                allAttributes:{
                    "Time & Date": ["dayOfWeek","dayOfMonth","sesionHour","season","isHolyday"],
                    "Referer": ["refererUrl","refererDomain","productId","productTag"],
                    "Device": ["deviceType","deviceBrand","os","screenDimensions"],
                    "Others": ["isDay","isTouchCapable","browser","language"]
                },
                attrSelected: "",
                numClusters: useMainStore().clusters,
                store: useMainStore(),
                errorMessage: "",
            };
        },

        methods: {

            selected(event, selection){
                if(event.target.checked && !this.store.selectedAttributes.includes(selection)){
                    this.store.addAttribute(selection);
                    
                }else{
                    console.log("filter")
                    let index = this.store.selectedAttributes.indexOf(selection)
                    if(index != -1){
                        this.store.selectedAttributes.splice(index,1)
                    }
                }
            },

            updateStoreClusters() {
                this.store.updateClusters(this.numClusters)
            },

            updateStore() {
                this.store.updateMessage(this.message);
            },

            async runSegmentation() {
                if (this.store.selectedAttributes.length === 0) {
                    this.errorMessage = "Please select at least one attribute.";
                    return;
                }
        
                try {
                    this.store.updateMessage("Loading...");
                    const response = await axios.post(
                        `http://localhost:8080/api/segmentation/run?attributes=${this.store.selectedAttributes.join(",")}&clusters=${this.numClusters}`
                    );
                    this.updateStoreClusters()
                    this.store.updateMessage(response.data);
                } catch (error) {
                    this.errorMessage = "Error running segmentation.";
                    console.error(error);
                }
            },

            mounted() {

            },
        },
    };
    </script>
    