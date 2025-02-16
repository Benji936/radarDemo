<template>
    <div style="margin-top: 5%; text-align: left; ">
        <div style="display: flex; flex-direction: row; width: 80vw; justify-content: space-between;">
            <div >
                <div class="IDdetail">
                    <strong>Session ID:</strong> {{ session.id }}
                    <button @click="copyToClipboard(session.id,1)" class="copy-button">
                        <h6 v-if="copied[1]">copied !</h6>
                        <h6 v-else>copy</h6>
                    </button>
                </div>
                <div class="IDdetail">
                    <strong>User ID:</strong> {{ session.user_id }}
                    <button @click="copyToClipboard(session.user_id,0)" class="copy-button">
                        <h6 v-if="copied[0]">copied !</h6>
                        <h6 v-else>copy</h6>
                    </button>
                </div>
                <div class="IDdetail">
                    <strong>Timestamp:</strong> {{ formatDate(session.timestamp) }}
                </div>
            </div>

            <div id="map" class="h-96" style="height: 200px; width: 400px;"></div>
        </div>
        
        <div v-if="loading" class="loading">Loading...</div>
        <div v-else-if="error" class="error">{{ error }}</div>
        <div v-else class="session-details">
            
            
            <div>
                <h2>Device Information</h2>
                <div class="detail"><strong>Device Type:</strong> {{ session.device_type }}</div>
                <div class="detail"><strong>Browser:</strong> {{ session.browser }} ({{ session.browser_version }})</div>
                <div class="detail"><strong>Operating System:</strong> {{ session.os }} ({{ session.os_version }})</div>
            </div>

            <div>
                <h2>Screen & Environment</h2>
                <div class="detail"><strong>Screen Size:</strong> {{ session.screen_width }}x{{ session.screen_height }}</div>
                <div class="detail"><strong>Temperature:</strong> {{ session.temperature }}°C</div>
                <div class="detail"><strong>Humidity:</strong> {{ session.humidity }}%</div>
            </div>
            
            <div>
                <h2>Product & Store</h2>
                <div class="detail"><strong>Product ID:</strong> {{ session.product_id }}</div>
                <div class="detail"><strong>Store ID:</strong> {{ session.store_id }}</div>
            </div>

            
        </div>

        
        <button @click="$router.push('/sessions')" class="back-button">Back</button>
    </div>
</template>

<script>
import { getSessionById } from "../api";

export default {
    data() {
    return {
        session: {},
        loading: true,
        error: null,
        copied:{
            user_id_copied: false,
            id_copied: false,
        }
    };
    },
    async created() {
        const sessionId = this.$route.params.id;
        try {
            const response = await getSessionById(sessionId);
            this.session = response;
        } catch (err) {
            this.error = "Failed to load session data.";
        } finally {
            this.loading = false;
        }

        map.value = L.map("map").setView([this.session.latitude, this.session.longitude], 2); // Default view (world map)
    
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }).addTo(map.value);


        if (this.session.latitude && this.session.longitude) {
            L.marker([this.session.latitude, this.session.longitude])
            .addTo(map.value)
            .bindPopup(
                `<b>City:</b> ${this.session.city}<br>
                <b>Country:</b> ${this.session.country}<br>
                <b>Temperature:</b> ${this.session.temperature}°C`
            );
        }
    },
    async mounted(){

        
    },
    methods: {
        formatDate(dateString) {
            return new Date(dateString).toLocaleString();
        },

        async copyToClipboard(text,index) {
            try {
                this.copied[index] = true;
                await navigator.clipboard.writeText(text);
                this.copiedText = text;
                
                setTimeout(() => {
                    this.copiedText = "";
                    this.copied[index] = false // Clear message after 2 seconds
                }, 2000);
            } catch (err) {
                console.error("Failed to copy: ", err);
            }
        },
    }
};
</script>

<style scoped>

h1, h2 {
    font-weight: bold;
    margin-bottom: 10px;
}

.loading, .error {
    text-align: center;
    font-size: 18px;
    color: red;
}

.session-details {
    margin-top: 20px;
    display:flex;
    flex-wrap: wrap;
    flex-direction: row;
    gap: 50px;
}

.detail {
    margin-bottom: 10px;
}

.IDdetail {
    margin-bottom: 10px;
    font-size: 18px;
}

.copy-button {
    margin-left: 10px;
    background: none;
    border: none;
    cursor: pointer;
    font-size: 16px;
    color: #3498db;
}



.back-button:hover {
    background: #2980b9;
}
</style>
