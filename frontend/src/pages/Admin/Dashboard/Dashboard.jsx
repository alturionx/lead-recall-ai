import {
    Users,
    Target,
    TrendingUp,
    DollarSign,
} from "lucide-react";
import getLeads from "./services";
import { useEffect, useState } from "react";

export default function Dashboard() {

    const [leads, setLeads] = useState([]);

    useEffect(() => {
        loadLeads();
    }, []);

    async function loadLeads() {
        try {
            const data = await getLeads();
            setLeads(data);
        } catch (error) {
            console.error(error);
        }
    }

    function getStatus(score) {
        if (score >= 80) return "Qualificado";
        if (score >= 50) return "Em análise";
        return "Novo";
    }

    function getIntent(intent) {
        switch (intent) {
            case "BUY_CAR":
                return "COMPRA";
            default:
                return "DESCONHECIDO";
        }
    }
    return (
        <div className="space-y-2">

            {/* KPIs */}
            <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-2">

                <div className="rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6 shadow-[0_0_40px_rgba(34,197,94,0.08)]">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-zinc-400 text-sm">
                                Leads Capturados
                            </p>

                            <div className="flex flex-wrap items-center gap-2 sm:gap-3 mt-4">
                                <h2 className="text-3xl sm:text-4xl xl:text-5xl font-semibold text-white">
                                    {leads.length}
                                </h2>

                                <span className="text-green-400 text-lg">
                                    ↑ 0%
                                </span>
                            </div>

                            <p className="text-zinc-500 text-sm mt-2">
                                vs período anterior
                            </p>
                        </div>

                        <div className="w-14 h-14 rounded-2xl bg-green-500/20 flex items-center justify-center">
                            <Users className="text-green-400" size={26} />
                        </div>
                    </div>

                    <div className="h-16 mt-5 bg-linear-to-r from-green-500/20 via-green-500/5 to-transparent rounded-xl" />
                </div>

                <div className="rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6 shadow-[0_0_40px_rgba(59,130,246,0.08)]">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-zinc-400 text-sm">
                                Leads Qualificados
                            </p>

                            <div className="flex flex-wrap items-center gap-2 sm:gap-3 mt-4">
                                <h2 className="text-3xl sm:text-4xl xl:text-5xl font-semibold text-white">
                                    0
                                </h2>

                                <span className="text-green-400 text-lg">
                                    ↑ 0%
                                </span>
                            </div>

                            <p className="text-zinc-500 text-sm mt-2">
                                vs período anterior
                            </p>
                        </div>

                        <div className="w-14 h-14 rounded-2xl bg-blue-500/20 flex items-center justify-center">
                            <Target className="text-blue-400" size={26} />
                        </div>
                    </div>

                    <div className="h-16 mt-5 bg-linear-to-r from-blue-500/20 via-blue-500/5 to-transparent rounded-xl" />
                </div>

                <div className="rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6 shadow-[0_0_40px_rgba(168,85,247,0.08)]">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-zinc-400 text-sm">
                                Taxa de Conversão
                            </p>

                            <div className="flex flex-wrap items-center gap-2 sm:gap-3 mt-4">
                                <h2 className="text-3xl sm:text-4xl xl:text-5xl font-semibold text-white">
                                    0%
                                </h2>

                                <span className="text-green-400 text-lg">
                                    ↑ 0%
                                </span>
                            </div>

                            <p className="text-zinc-500 text-sm mt-2">
                                vs período anterior
                            </p>
                        </div>

                        <div className="w-14 h-14 rounded-2xl bg-purple-500/20 flex items-center justify-center">
                            <TrendingUp className="text-purple-400" size={26} />
                        </div>
                    </div>

                    <div className="h-16 mt-5 bg-linear-to-r from-purple-500/20 via-purple-500/5 to-transparent rounded-xl" />
                </div>

                <div className="rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6 shadow-[0_0_40px_rgba(234,179,8,0.08)]">
                    <div className="flex justify-between items-start">
                        <div>
                            <p className="text-zinc-400 text-sm">
                                Receita Estimada
                            </p>

                            <div className="flex flex-wrap items-center gap-2 sm:gap-3 mt-4">
                                <h2 className="text-3xl sm:text-4xl xl:text-5xl font-semibold text-white">
                                    R$ 0
                                </h2>

                                <span className="text-green-400 text-lg">
                                    ↑ 0%
                                </span>
                            </div>

                            <p className="text-zinc-500 text-sm mt-2">
                                vs período anterior
                            </p>
                        </div>

                        <div className="w-14 h-14 rounded-2xl bg-yellow-500/20 flex items-center justify-center">
                            <DollarSign className="text-yellow-400" size={26} />
                        </div>
                    </div>

                    <div className="h-16 mt-5 bg-linear-to-r from-yellow-500/20 via-yellow-500/5 to-transparent rounded-xl" />
                </div>
            </div>

            {/* Linha 2 */}
            <div className="grid grid-cols-1 xl:grid-cols-12 gap-2">

                <div className="xl:col-span-7 rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6">
                    <div className="flex flex-col sm:flex-row gap-4 sm:gap-0 justify-between sm:items-center mb-8">
                        <h3 className="text-xl text-white font-medium">
                            Leads ao longo do tempo
                        </h3>

                        <select className="bg-[#111827] border border-white/10 rounded-xl px-4 py-2 text-white">
                            <option>Diário</option>
                        </select>
                    </div>

                    <div className="h-80 rounded-xl bg-linear-to-t from-green-500/15 to-transparent border border-white/5" />
                </div>

                <div className="xl:col-span-5 rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6">
                    <h3 className="text-xl text-white font-medium mb-8">
                        Origem dos Leads
                    </h3>

                    <div className="flex items-center justify-center h-62.5 sm:h-80">
                        <div className="w-48 h-48 sm:w-64 sm:h-64 rounded-full border-30 border-green-500 relative">
                            <div className="absolute inset-0 flex flex-col items-center justify-center">
                                <h2 className="text-4xl font-semibold text-white">
                                    0
                                </h2>

                                <p className="text-zinc-500">
                                    Total
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

            {/* Linha 3 */}
            <div className="grid grid-cols-1 xl:grid-cols-12 gap-2">

                <div className="xl:col-span-8 rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl overflow-hidden">

                    <div className="p-6 border-b border-white/10">
                        <h3 className="text-xl text-white font-medium">
                            Leads Recentes
                        </h3>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full min-w-225">
                            <thead>
                                <tr className="text-zinc-500 text-left">
                                    <th className="px-6 py-4">Lead</th>
                                    <th>Origem</th>
                                    <th>Intent</th>
                                    <th>Score</th>
                                    <th>Status</th>
                                    <th>Última Interação</th>
                                </tr>
                            </thead>

                            <tbody>
                                {leads.map((lead) => (
                                    <tr
                                        key={lead.id}
                                        className="border-t border-white/5"
                                    >
                                        <td className="px-6 py-5">
                                            <div>
                                                <p className="text-white font-medium">
                                                    {lead.name || lead.phone}
                                                </p>

                                                <p className="text-zinc-500 text-sm">
                                                    {lead.vehicleInterest}
                                                </p>
                                            </div>
                                        </td>

                                        <td className="text-green-400">
                                            WhatsApp
                                        </td>

                                        <td>
                                            <span className="bg-green-500/20 text-green-400 px-3 py-1 rounded-lg text-sm">
                                                {getIntent(lead.intent)}
                                            </span>
                                        </td>

                                        <td className="text-green-400 font-medium">
                                            {lead.score}
                                        </td>

                                        <td>
                                            <span className="bg-blue-500/20 text-blue-400 px-3 py-1 rounded-lg text-sm">
                                                {getStatus(lead.score)}
                                            </span>
                                        </td>

                                        <td className="text-zinc-400">
                                            {new Date(
                                                lead.updatedAt
                                            ).toLocaleDateString("pt-BR")}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                </div>

                <div className="xl:col-span-4 rounded-xl border border-white/10 bg-[#0B1220]/60 backdrop-blur-xl p-6">
                    <h3 className="text-xl text-white font-medium mb-8">
                        Performance da IA
                    </h3>

                    <div className="space-y-6 sm:space-y-8">

                        {[
                            ["Precisão da Classificação", "92.4"],
                            ["Extração de Informações", "89.1"],
                            ["Análise de Sentimento", "87.3"],
                            ["Tempo Médio de Resposta", "1.2s"],
                        ].map(([label, value]) => (
                            <div key={label}>
                                <div className="flex justify-between mb-2">
                                    <span className="text-zinc-300">
                                        {label}
                                    </span>

                                    <span className="text-white font-medium">
                                        {value}
                                    </span>
                                </div>

                                <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                                    <div className="h-full w-[90%] bg-green-500 rounded-full" />
                                </div>
                            </div>
                        ))}

                    </div>
                </div>

            </div>

        </div>
    );
}