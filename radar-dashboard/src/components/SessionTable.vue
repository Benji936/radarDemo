    <template>
        <div  style="display:flex; flex-direction: column; gap: 48px; ">
            <h2 class="text-2xl font-semibold mb-4">User Sessions</h2>
    
            <div class="flex p-2" style="display:flex; flex-direction: row; gap: 24px; ">
                <input
                v-model="searchQuery"
                @input="update"
                type="text"
                placeholder="Search by browser, OS, city..."
                class="border p-2 mb-4 w-full rounded-md object-left"
                style="padding: 10px; border-radius: 10px;"
                />

                <select @change="update" v-model="maxPageUser" style="padding: 10px; border-radius: 10px;">
                    <option>5</option>
                    <option>10</option>
                    <option>20</option>
                    <option>50</option>
                    <option>100</option>
                </select>

                <select v-show="store.clusters != null" v-for="n in store.clusters" :key="n" @change="update" v-model="segment"  style="padding: 10px; border-radius: 10px;">
                    <option value="0">Cluster 0</option>
                    <option value="1">Cluster 1</option>
                    <option value="2">Cluster 2</option>
                    <option value="3">Cluster 3</option>
                    <option value="4">Cluster 4</option>
                    <option value="5">Cluster 5</option>
                    <option value="6">Cluster 6</option>
                </select>
            </div>

            <div class="flex justify-between h-100" style="display: flex; flex-direction: row; justify-content: space-between;">
                <button @click="prevPage" :disabled="page === 0" class="px-4 py-2 mx-5 bg-gray-300 rounded disabled:opacity-50">
                Previous
                </button>

                <span class="text-gray-700 ml-4 mr-4">Around {{ totalPages*maxPageUser}}</span>
                <span class="text-gray-700 ml-4 mr-4">Page {{ page + 1 }} of {{ totalPages }}</span>
                
                <button @click="nextPage" :disabled="page >= totalPages - 1" class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50">
                Next
                </button>
            </div>

            <div id="map" class="h-96"></div>

            <div class="overflow-x-auto mb-4">
                <table class="min-w-full border-collapse border border-gray-300">
                <thead class="bg-gray-100">
                    <!--<tr v-for="(value,key) in sessions[0]" v-bind:key="key" style="display: flex; flex-direction: row;">
                        <th>{{ key }}</th>-->
                    <tr>
                        <th class="text-left p-2">User ID</th>
                        <th class="text-left border p-2">Device</th>
                        <th class="text-left border p-2">Device Type</th>
                        <th class="text-left border p-2">Browser</th>
                        <th class="text-left border p-2">OS</th>
                        <th class="text-left border p-2">City</th>
                        <th class="text-left border p-2">Temperature (°C)</th>
                        <th class="text-left border p-2">Segment</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="session in sessions" :key="session.id" class="hover:bg-gray-50">
                        <td class="border p-2">{{ session.id }}</td>
                        <td class="border p-2">{{ session.device_brand }}</td>
                        <td class="border p-2">{{ session.device_type }}</td>
                        <td class="border p-2">{{ session.browser }}</td>
                        <td class="border p-2">{{ session.os }}</td>
                        <td class="border p-2">{{ session.city }}</td>
                        <td class="border p-2">{{ session.temperature }}</td>
                        <td class="border p-2">{{ session.userSegment }}</td>
                        <td class="border p-2">{{ session.day_of_week }}</td>
                    </tr>
                </tbody>
                </table>
            </div>

            
            
        
            
        </div>
        
    </template>
    
    <script>
    import { ref } from "vue";
    import { useMainStore } from '@/stores/main';
    import { runSegmentation, getSessions, getSegment } from "../api";
    import L from "leaflet";
    import "leaflet/dist/leaflet.css";
    
    export default {
        data() {
            return {
                store: useMainStore(),
                sessions: [],
                searchQuery: "",
                page: 0,
                totalPages: 1,
                map: ref(null),
                markersLayer: ref(null),
                maxPageUser: 10,
                segment: null,
            };
        },
        methods: {

        async run(){
            const response = await runSegmentation();
            console.log(response);
        },

        async updateSegment(){
            const data = await getSegment(this.page, this.maxPageUser, this.segment);

            this.sessions = data._embedded.userSessionList;
            this.totalPages = data.page.totalPages;

            if (this.markersLayer) {
                this.markersLayer.clearLayers(); // Remove old markers
            } else {
                this.markersLayer = L.layerGroup().addTo(this.map); // Create marker group if not exists
            }

            // Fetch user session data
            data._embedded.userSessionList.forEach((session) => {
                if (session.latitude && session.longitude) {
                    const marker = L.marker([session.latitude, session.longitude])
                    .bindPopup(
                        `<b>User:</b> ${session.id}<br>
                        <b>City:</b> ${session.city}<br>
                        <b>Country:</b> ${session.country}<br>
                        <b>Temperature:</b> ${session.temperature}°C`
                    );
                    this.markersLayer.addLayer(marker)
                }
            });
        },

        async update(){

            let data = null;
            if(this.segment != null){
                data = await getSegment(this.page, this.maxPageUser, this.segment);
            }else{
                data = await getSessions(this.page, this.maxPageUser, this.searchQuery);
            }
            

            this.sessions = data._embedded.userSessionList;
            this.totalPages = data.page.totalPages;

            if (this.markersLayer) {
                this.markersLayer.clearLayers(); // Remove old markers
            } else {
                this.markersLayer = L.layerGroup().addTo(this.map); // Create marker group if not exists
            }

            // Fetch user session data
            data._embedded.userSessionList.forEach((session) => {
                if (session.latitude && session.longitude) {
                    const marker = L.marker([session.latitude, session.longitude])
                    .bindPopup(
                        `<b>User:</b> ${session.id}<br>
                        <b>City:</b> ${session.city}<br>
                        <b>Country:</b> ${session.country}<br>
                        <b>Temperature:</b> ${session.temperature}°C`
                    );
                    this.markersLayer.addLayer(marker)
                }
            });
        },

        async fetchSessions() {
            const data = await getSessions(this.page, 10, this.searchQuery);

            this.sessions = data._embedded.userSessionList;
            this.totalPages = data.page.totalPages;

            
            this.map = L.map("map").setView([20, 0], 2); // Default view (world map)
    
            L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
            }).addTo(this.map);

            await this.update()
            
        },
        nextPage() {
            if (this.page < this.totalPages - 1) {
            this.page++;
            this.update();
            }
        },
        prevPage() {
            if (this.page > 0) {
            this.page--;
            this.update();
            }
        },
        },
        mounted() {
            this.fetchSessions();
        },
    };
    </script>
    