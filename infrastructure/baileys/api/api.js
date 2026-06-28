import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

export async function postMessage(payload) {
    try {
        const { data } = await api.post("/messages", payload);
        console.log("✅ Nova Mensagem enviada para o backend.");
        return data;

    } catch (error) {
        console.error(
            "❌ Erro ao enviar mensagem:",
            error.response?.data || error.message
        );

        throw error;
    }
}

export default api;