    <template>
        <div  style="display:flex; flex-direction: column; gap: 48px; ">
    
            <div class="flex p-2" style="display:flex; flex-direction: row; gap: 24px; margin-top: 24px;">
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

                <select default="Aucun" v-show="store.clusters != null" @change="update" v-model="segment"  style="padding: 10px; border-radius: 10px;">
                    <option :value=null>Aucun</option>
                    <option v-for="n in store.clusters" :key="n" :value="n-1">{{ n-1 }}</option>
                </select>
            </div>

            <div class="flex justify-between h-100" style="display: flex; flex-direction: row; justify-content: space-between;">
                <button @click="prevPage" :disabled="page === 0" class="px-4 py-2 mx-5 bg-gray-300 rounded disabled:opacity-50">
                Previous
                </button>

                <!--<span class="text-gray-700 ml-4 mr-4">Around {{ totalPages*maxPageUser}}</span>-->
                <span class="text-gray-700 ml-4 mr-4">Page {{ page + 1 }} of {{ totalPages }}</span>
                
                <button @click="nextPage" :disabled="page >= totalPages - 1" class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50">
                Next
                </button>
            </div>

            <div id="map" class="h-96" style="height: 50vh;"></div>


            <div class="overflow-x-auto mb-4">
                <table class="min-w-full border-collapse border border-gray-300" style="overflow:hidden;">
                <thead class="bg-gray-100">
                    <tr style=" display: flex; text-align: left; gap: 24px; width: 80vw;">
                        <!--<th v-for="(value,key) in sessions[0]" v-bind:key="key"  style="width: 150px; height: 100px;">{{ key }}</th>-->
                        <th class="text-left border p-2" style="width: 150px; height: 100px;">Device</th>
                        <th class="text-left border p-2" style="width: 150px; height: 100px;">Browser</th>
                        <th class="text-left border p-2" style="width: 150px; height: 100px;">OS</th>
                        <th class="text-left border p-2" style="width: 150px; height: 100px;">City</th>
                        <th class="text-left border p-2" style="width: 150px; height: 100px;  margin-right:50px;">
                            <select default="day_of_week" style="padding: 10px; border-radius: 10px;" v-model="specialKey">
                                <option :value=key v-for="(value,key) in sessions[0]" v-bind:key="key">{{ key }}</option>
                            </select>
                        </th>

                        <th class="text-left border p-2" style="width: 150px; height: 100px;">
                            <select default="day_of_week" style="padding: 10px; border-radius: 10px;" v-model="specialKey2">
                                <option :value=key v-for="(value,key) in sessions[0]" v-bind:key="key">{{ key }}</option>
                            </select>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="session in sessions" :key="session.id" class="hover:bg-gray-50" style=" display: flex; text-align: left; gap: 24px; width: 80vw;">
                        <!--<td v-show="store.selectedAttributes.includes(key)" v-for="(value,key) in session" :key="key" class="border p-2">{{ value }}</td>-->
                        <td class="border p-2" style="width: 150px; height: 100px;">{{ session.device_brand }}</td>
                        <td class="border p-2" style="width: 150px; height: 100px;">{{ session.browser }}</td>
                        <td class="border p-2" style="width: 150px; height: 100px;">{{ session.os }}</td>
                        <td class="border p-2" style="width: 150px; height: 100px;">{{ session.city }}</td>
                        <td class="border p-2" style="width: 150px; height: 100px; margin-right:50px;">{{ session[specialKey] }}</td>
                        <td class="border p-2" style="width: 150px; height: 100px;">{{ session[specialKey2] }}</td>
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
                specialKey: "day_of_week",
                specialKey2: "day_of_month",
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
    