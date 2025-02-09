    <template>
        <div class="p-6 bg-white shadow-lg rounded-lg">
        <h2 class="text-2xl font-semibold mb-4">User Locations</h2>
        <div id="map" class="h-96"></div>
        </div>
    </template>
    
    <script>
    import { defineComponent, onMounted, ref } from "vue";
    import L from "leaflet";
    import "leaflet/dist/leaflet.css";
    import { getAllSessions } from "../api";
    
    export default defineComponent({
        setup() {
        const map = ref(null);
    
        onMounted(async () => {
            map.value = L.map("map").setView([20, 0], 2); // Default view (world map)
    
            L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
            }).addTo(map.value);
    
            // Fetch user session data
            const sessions = await getAllSessions(0, 50); // Load only first 50 sessions
            sessions.forEach((session) => {
            if (session.latitude && session.longitude) {
                L.marker([session.latitude, session.longitude])
                .addTo(map.value)
                .bindPopup(
                    `<b>User:</b> ${session.userId}<br>
                    <b>City:</b> ${session.city}<br>
                    <b>Country:</b> ${session.country}<br>
                    <b>Temperature:</b> ${session.temperature}°C`
                );
            }
            });
        });
    
        return { map };
        },
    });
    </script>
    
    <style>
    /* Set map height */
    #map {
        width: 100%;
        height: 400px;
    }
    </style>
    