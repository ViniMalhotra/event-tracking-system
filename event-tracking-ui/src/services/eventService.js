import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/events';

export const eventService = {
    getAllEvents: async () => {
        try {
            const response = await axios.get(API_BASE_URL);
            return response.data;
        } catch (error) {
            console.error('Error fetching events:', error);
            throw error;
        }
    },

    getEventById: async (id) => {
        try {
            const response = await axios.get(`${API_BASE_URL}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching event ${id}:`, error);
            throw error;
        }
    },

    createEvent: async (event) => {
        try {
            const response = await axios.post(API_BASE_URL, event);
            return response.data;
        } catch (error) {
            console.error('Error creating event:', error);
            throw error;
        }
    },

    updateEvent: async (id, event) => {
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, event);
            return response.data;
        } catch (error) {
            console.error(`Error updating event ${id}:`, error);
            throw error;
        }
    },

    deleteEvent: async (id) => {
        try {
            await axios.delete(`${API_BASE_URL}/${id}`);
        } catch (error) {
            console.error(`Error deleting event ${id}:`, error);
            throw error;
        }
    }
};
