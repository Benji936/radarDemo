    <template>
        <div class="analytics-container">
    
        
        <SessionChart style="width: 50vw;"/>
    
        <button @click="$router.push('/sessions')" class="back-button">Back</button>
        </div>
    </template>
    
    <script>

    import SessionChart from "../components/SessionChart.vue";
    
    export default {
        components: {
            SessionChart,
        },
        data() {
            return {
                sessions: [],
                loading: true,
                error: null,
                sessionsOverTimeData: null,
                deviceData: null,
                browserData: null,
                productData: null,
            };
        },
    };
    </script>
    
    <style scoped>
    
    .loading, .error {
        text-align: center;
        font-size: 18px;
        color: red;
    }
    
    .back-button {
        padding: 10px 20px;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    
    .back-button:hover {
        background: #2980b9;
    }
    </style>
    