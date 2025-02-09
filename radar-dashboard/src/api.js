import axios from "axios";

const API_URL = "http://localhost:8080/api/sessions";

export const getSessions = async (page = 0, size = 10, search = "") => {
    try {
        const response = await axios.get(API_URL, {
        params: { page, size, search },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching sessions:", error);
        return { content: [], totalPages: 1 };
    }
};

export const getAllSessions = async() => {

    try{
        const response = await axios.get(API_URL+"/all");
        return response.data;
    }catch (error) {
        console.error("Error fetching sessions:", error);
        return { content: [] };
    }

}

export const fetchLogo = async (searchValue) => {
    try {
    const response = await axios.get('https://api.logo.dev/search?q='+searchValue, {
        headers: {
        'Authorization': 'Bearer sk_ZqBKmUrVSsGbfWYTyrbssw'
        }
    });
    
    console.log(response.data); // Handle the response data
    return response.data;
    } catch (error) {
    console.error('Error fetching logo:', error);
    }
};
