import api from "../../../api/api";

export default async function getLeads() {
    const { data } = await api.get("/leads");
    return data;
}