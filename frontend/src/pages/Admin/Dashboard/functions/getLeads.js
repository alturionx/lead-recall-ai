import api from "../../../../../../infrastructure/baileys/api/api";

export default async function getLeads() {
    try {
        const { data } = await api.get("/leads");
        return data;
    } catch (error) {
        console.error("Erro ao buscar leads:", error);
        return [];
    }
}