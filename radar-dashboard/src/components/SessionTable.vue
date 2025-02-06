<script>
    import { getSessions } from "../api";

    export default {
        data() {
        return {
            sessions: [],
            search: "",
        };
    },
    computed: {
        filteredSessions() {
            return this.sessions.filter((session) =>
            [session.browser, session.os, session.city]
                .join(" ")
                .toLowerCase()
                .includes(this.search.toLowerCase())
            );
        },
    },
    async mounted() {
            this.sessions = await getSessions();
        },
    };
</script>

<template>
    <div class="p-6 bg-white shadow-lg rounded-lg">
        <h2 class="text-2xl font-semibold mb-4">User Sessions</h2>
        <!-- Search Filter -->
        <input
            v-model="search"
            type="text"
            placeholder="Search by browser, OS, city..."
            class="border p-2 mb-4 w-full rounded-md"
        />

        <!-- Table -->
        <div class="overflow-x-auto">
        <table class="min-w-full border-collapse border border-gray-300">
            <thead class="bg-gray-100">
                <tr>
                    <th class="border p-2">User ID</th>
                    <th class="border p-2">Device</th>
                    <th class="border p-2">Browser</th>
                    <th class="border p-2">OS</th>
                    <th class="border p-2">City</th>
                    <th class="border p-2">Temperature (°C)</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="session in filteredSessions" :key="session.userId" class="hover:bg-gray-50">
                    <td class="border p-2">{{ session.userId }}</td>
                    <td class="border p-2">{{ session.deviceType }}</td>
                    <td class="border p-2">{{ session.browser }}</td>
                    <td class="border p-2">{{ session.os }}</td>
                    <td class="border p-2">{{ session.city }}</td>
                    <td class="border p-2">{{ session.temperature }}</td>
                </tr>
            </tbody>
        </table>
        </div>
    </div>
</template>


