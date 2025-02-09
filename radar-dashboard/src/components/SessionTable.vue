    <template>
        <h2 class="text-2xl font-semibold mb-4">User Sessions</h2>
    
        <!-- Search Input -->
        <input
            v-model="searchQuery"
            @input="fetchSessions"
            type="text"
            placeholder="Search by browser, OS, city..."
            class="border p-2 mb-4 w-full rounded-md"
        />
    
        <!-- Table -->
        <div class="overflow-x-auto">
            <table class="min-w-full border-collapse border border-gray-300">
            <thead class="bg-gray-100">
                <tr>
                <th class="text-left border p-2">User ID</th>
                <th class="text-left border p-2">Device</th>
                <th class="text-left border p-2">Browser</th>
                <th class="text-left border p-2">OS</th>
                <th class="text-left border p-2">City</th>
                <th class="text-left border p-2">Temperature (°C)</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="session in sessions" :key="session.id" class="hover:bg-gray-50">
                <td class="border p-2">{{ session.id }}</td>
                <td class="fixed top-0 right-0 left-0 border p-2">{{ session.device }}</td>
                <td class="fixed top-0 right-0 left-0 border p-2">{{ session.browser }}</td>
                <td class="border p-2">{{ session.os }}</td>
                <td class="border p-2">{{ session.city }}</td>
                <td class="border p-2">{{ session.temperature }}</td>
                </tr>
            </tbody>
            </table>
        </div>
    
        <div class="mt-4 flex justify-between">
            <button
            @click="prevPage"
            :disabled="page === 0"
            class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
            >
            Previous
            </button>
            <span class="text-gray-700 ml-4 mr-4">Page {{ page + 1 }} of {{ totalPages }}</span>
            <button
            @click="nextPage"
            :disabled="page >= totalPages - 1"
            class="px-4 py-2 bg-gray-300 rounded disabled:opacity-50"
            >
            Next
            </button>
        </div>
    </template>
    
    <script>
    import { getSessions } from "../api";
    
    export default {
        data() {
        return {
            sessions: [],
            searchQuery: "",
            page: 0,
            totalPages: 1,
        };
        },
        methods: {
        async fetchSessions() {
            const data = await getSessions(this.page, 10, this.searchQuery);
            this.sessions = data._embedded.userSessionList;
            this.totalPages = data.page.totalPages;
        },
        nextPage() {
            if (this.page < this.totalPages - 1) {
            this.page++;
            this.fetchSessions();
            }
        },
        prevPage() {
            if (this.page > 0) {
            this.page--;
            this.fetchSessions();
            }
        },
        },
        mounted() {
            this.fetchSessions();
        },
    };
    </script>
    