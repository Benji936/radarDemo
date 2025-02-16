import axios from "axios";

const API_SESSION_URL = "http://localhost:8080/api/sessions";
const API_SEGMENTATION_URL = "http://localhost:8080/api/segmentation";

export const getSessions = async (page = 0, size = 10, search = "") => {
    try {
        const response = await axios.get(API_SESSION_URL, {
        params: { page, size, search },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching sessions:", error);
        return { content: [], totalPages: 1 };
    }
};


export const getSegment = async (page = 0, size = 10, segment = 0) => {
    try {
        const response = await axios.get(API_SESSION_URL, {
        params: { page, size, segment },
        });
        console.log(response)
        return response.data;
    } catch (error) {
        console.error("Error fetching sessions:", error);
        return { content: [], totalPages: 1 };
    }
};

export const getAllSessions = async() => {

    try{
        const response = await axios.get(API_SESSION_URL+"/all");
        return response.data;
    }catch (error) {
        console.error("Error fetching sessions:", error);
        return { content: [] };
    }
}

export const getSessionById = async(sessionId) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/sessions/${sessionId}`);
        return response.data;
    } catch (err) {
        return "Failed to load session data.";
    }
}

export const runSegmentation = async() => {
    try{
        const response = await axios.post(API_SEGMENTATION_URL+"/run");
        return response.data;
    }catch(error){
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
    
    //console.log(response.data); // Handle the response data
    return response.data;
    } catch (error) {
    console.error('Error fetching logo:', error);
    }
};
