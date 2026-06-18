import {
    Users,
    Target,
    TrendingUp,
    DollarSign,
    Car,
    Brain,
    MessageSquare,
    Activity,
    ArrowUpRight,
    Sparkles,
} from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import getLeads from "./services";

export default function Dashboard() {
    const [leads, setLeads] = useState([]);

    useEffect(() => {
        loadLeads();
    }, []);

    async function loadLeads() {
        try {
            const data = await getLeads();
            setLeads(data || []);
        } catch (error) {
            console.error(error);
        }
    }

    const metrics = useMemo(() => {
        const totalLeads = leads.length;

        const qualified = leads.filter(
            (lead) => Number(lead.score || 0) >= 80
        ).length;

        const warm = leads.filter(
            (lead) =>
                Number(lead.score || 0) >= 50 &&
                Number(lead.score || 0) < 80
        ).length;

        const avgScore =
            totalLeads > 0
                ? Math.round(
                    leads.reduce(
                        (acc, lead) => acc + Number(lead.score || 0),
                        0
                    ) / totalLeads
                )
                : 0;

        const estimatedRevenue = qualified * 120000;

        return {
            totalLeads,
            qualified,
            warm,
            avgScore,
            estimatedRevenue,
        };
    }, [leads]);

    function getStatus(score) {
        if (score >= 80) return "Qualificado";
        if (score >= 50) return "Em análise";
        return "Novo";
    }

    function getStatusColor(score) {
        if (score >= 80)
            return "bg-green-500/15 text-green-400 border-green-500/20";

        if (score >= 50)
            return "bg-yellow-500/15 text-yellow-400 border-yellow-500/20";

        return "bg-blue-500/15 text-blue-400 border-blue-500/20";
    }

    function getIntent(intent) {
        switch (intent) {
            case "BUY_CAR":
                return "COMPRA";

            default:
                return "DESCONHECIDO";
        }
    }

    const recentLeads = [...leads]
        .sort(
            (a, b) =>
                new Date(b.updatedAt || b.createdAt) -
                new Date(a.updatedAt || a.createdAt)
        )
        .slice(0, 8);

    return (
        <div className="min-h-screen">
            <div className="w-full space-y-6">

                {/* HEADER */}

                <div className="relative overflow-hidden rounded-[12px] border border-white/10 bg-gradient-to-br from-emerald-500/10 via-[#0B1220] to-cyan-500/10 p-8">

                    <div className="absolute top-0 right-0 h-72 w-72 rounded-[12px] bg-emerald-500/20 blur-3xl" />

                    <div className="absolute bottom-0 left-0 h-72 w-72 rounded-[12px] bg-cyan-500/10 blur-3xl" />

                    <div className="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">

                        <div>
                            <div className="inline-flex items-center gap-2 rounded-[12px] border border-emerald-500/20 bg-emerald-500/10 px-4 py-2 text-sm text-emerald-400">
                                <Sparkles size={16} />
                                IA ativa e processando leads
                            </div>

                            <h1 className="mt-4 text-5xl font-bold text-white">
                                Lead Recall AI
                            </h1>

                            <p className="mt-3 max-w-2xl text-zinc-400">
                                Plataforma inteligente de recuperação de leads,
                                matching de veículos e geração automática de
                                oportunidades.
                            </p>
                        </div>

                        <div className="grid grid-cols-2 gap-4">

                            <MiniStat
                                title="Score Médio"
                                value={`${metrics.avgScore}`}
                            />

                            <MiniStat
                                title="Conversão"
                                value="24%"
                            />

                            <MiniStat
                                title="IA Accuracy"
                                value="92%"
                            />

                            <MiniStat
                                title="Tempo"
                                value="320ms"
                            />

                        </div>
                    </div>
                </div>

                {/* KPI CARDS */}

                <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">

                    <KpiCard
                        title="Leads Capturados"
                        value={metrics.totalLeads}
                        growth="+12%"
                        icon={
                            <Users
                                size={24}
                                className="text-emerald-400"
                            />
                        }
                    />

                    <KpiCard
                        title="Leads Qualificados"
                        value={metrics.qualified}
                        growth="+18%"
                        icon={
                            <Target
                                size={24}
                                className="text-blue-400"
                            />
                        }
                    />

                    <KpiCard
                        title="Score Médio"
                        value={metrics.avgScore}
                        growth="+9%"
                        icon={
                            <Brain
                                size={24}
                                className="text-purple-400"
                            />
                        }
                    />

                    <KpiCard
                        title="Receita Potencial"
                        value={`R$ ${(metrics.estimatedRevenue / 1000).toFixed(0)}k`}
                        growth="+22%"
                        icon={
                            <DollarSign
                                size={24}
                                className="text-yellow-400"
                            />
                        }
                    />

                </div>

                {/* GRID 1 */}

                <div className="grid gap-4 xl:grid-cols-12">

                    <div className="xl:col-span-8 rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6 backdrop-blur-xl">

                        <div className="flex items-center justify-between mb-8">
                            <h3 className="text-xl font-semibold text-white">
                                Pipeline Comercial
                            </h3>

                            <Activity className="text-emerald-400" />
                        </div>

                        <div className="space-y-6">

                            <PipelineRow
                                label="Novos Leads"
                                value={100}
                                color="bg-blue-500"
                            />

                            <PipelineRow
                                label="Qualificados"
                                value={78}
                                color="bg-cyan-500"
                            />

                            <PipelineRow
                                label="Contato"
                                value={62}
                                color="bg-yellow-500"
                            />

                            <PipelineRow
                                label="Negociação"
                                value={38}
                                color="bg-purple-500"
                            />

                            <PipelineRow
                                label="Fechados"
                                value={24}
                                color="bg-emerald-500"
                            />

                        </div>

                        <div className="mt-10 h-64 rounded-[12px] border border-white/5 bg-gradient-to-t from-emerald-500/10 via-transparent to-transparent" />
                    </div>

                    <div className="xl:col-span-4 rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6 backdrop-blur-xl">

                        <h3 className="mb-8 text-xl font-semibold text-white">
                            Performance da IA
                        </h3>

                        <div className="space-y-8">

                            <PerformanceItem
                                label="Precisão"
                                value="92%"
                            />

                            <PerformanceItem
                                label="Extração"
                                value="89%"
                            />

                            <PerformanceItem
                                label="Sentimento"
                                value="87%"
                            />

                            <PerformanceItem
                                label="Matching"
                                value="91%"
                            />

                            <PerformanceItem
                                label="Tempo Médio"
                                value="320ms"
                            />

                        </div>
                    </div>

                </div>


                {/* GRID 2 */}

                <div className="grid gap-4 xl:grid-cols-12">

                    <div className="xl:col-span-8 rounded-[12px] border border-white/10 bg-[#0B1220]/70 backdrop-blur-xl overflow-hidden">

                        <div className="border-b border-white/10 p-6">
                            <h3 className="text-xl font-semibold text-white">
                                Leads Recentes
                            </h3>
                        </div>

                        <div className="overflow-x-auto">

                            <div className="overflow-x-auto">
                                <table className="min-w-[700px] w-full">
                                    <thead>
                                        <tr className="text-left text-zinc-500">
                                            <th className="px-6 py-4">Lead</th>
                                            <th className="px-4 py-4">Interesse</th>
                                            <th className="px-4 py-4">Intent</th>
                                            <th className="px-4 py-4">Score</th>
                                            <th className="px-4 py-4">Status</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {recentLeads.map((lead) => (
                                            <tr
                                                key={lead.id}
                                                className="border-t border-white/5 hover:bg-white/[0.02]"
                                            >
                                                <td className="px-6 py-4">
                                                    <div>
                                                        <p className="font-medium text-white whitespace-nowrap">
                                                            {lead.name || lead.phone}
                                                        </p>

                                                        <p className="text-sm text-zinc-500 whitespace-nowrap">
                                                            {lead.phone}
                                                        </p>
                                                    </div>
                                                </td>

                                                <td className="px-4 py-4 text-zinc-300 whitespace-nowrap">
                                                    {lead.vehicleInterest || "-"}
                                                </td>

                                                <td className="px-4 py-4 whitespace-nowrap">
                                                    <span className="rounded-xl bg-emerald-500/15 px-3 py-1 text-xs text-emerald-400">
                                                        {getIntent(lead.intent)}
                                                    </span>
                                                </td>

                                                <td className="px-4 py-4 font-semibold text-emerald-400 whitespace-nowrap">
                                                    {lead.score}
                                                </td>

                                                <td className="px-4 py-4 whitespace-nowrap">
                                                    <span
                                                        className={`rounded-xl border px-3 py-1 text-xs ${getStatusColor(
                                                            lead.score
                                                        )}`}
                                                    >
                                                        {getStatus(lead.score)}
                                                    </span>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>

                    <div className="xl:col-span-4 space-y-4">

                        <SideCard
                            title="Mensagens Processadas"
                            value="12.843"
                            icon={
                                <MessageSquare className="text-cyan-400" />
                            }
                        />

                        <SideCard
                            title="Eventos Processados"
                            value="31.220"
                            icon={
                                <Activity className="text-purple-400" />
                            }
                        />

                        <SideCard
                            title="Veículos Monitorados"
                            value="847"
                            icon={
                                <Car className="text-yellow-400" />
                            }
                        />

                        <SideCard
                            title="Oportunidades"
                            value="382"
                            icon={
                                <TrendingUp className="text-emerald-400" />
                            }
                        />

                    </div>

                </div>

                {/* GRID 3 */}

                <div className="grid gap-4 xl:grid-cols-12">

                    {/* MATCHING */}

                    <div className="xl:col-span-4 rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6">

                        <h3 className="text-xl font-semibold text-white mb-6">
                            Matching de Veículos
                        </h3>

                        <div className="space-y-5">

                            <div>
                                <div className="flex justify-between mb-2">
                                    <span className="text-zinc-400">
                                        Corolla
                                    </span>

                                    <span className="text-white">
                                        24 matches
                                    </span>
                                </div>

                                <div className="h-2 rounded-[12px] bg-white/5">
                                    <div className="h-2 w-[90%] rounded-[12px] bg-emerald-500" />
                                </div>
                            </div>

                            <div>
                                <div className="flex justify-between mb-2">
                                    <span className="text-zinc-400">
                                        Compass
                                    </span>

                                    <span className="text-white">
                                        18 matches
                                    </span>
                                </div>

                                <div className="h-2 rounded-[12px] bg-white/5">
                                    <div className="h-2 w-[75%] rounded-[12px] bg-cyan-500" />
                                </div>
                            </div>

                            <div>
                                <div className="flex justify-between mb-2">
                                    <span className="text-zinc-400">
                                        HB20
                                    </span>

                                    <span className="text-white">
                                        13 matches
                                    </span>
                                </div>

                                <div className="h-2 rounded-[12px] bg-white/5">
                                    <div className="h-2 w-[60%] rounded-[12px] bg-yellow-500" />
                                </div>
                            </div>

                            <div>
                                <div className="flex justify-between mb-2">
                                    <span className="text-zinc-400">
                                        Civic
                                    </span>

                                    <span className="text-white">
                                        9 matches
                                    </span>
                                </div>

                                <div className="h-2 rounded-[12px] bg-white/5">
                                    <div className="h-2 w-[40%] rounded-[12px] bg-purple-500" />
                                </div>
                            </div>

                        </div>

                    </div>

                    {/* IA INSIGHTS */}

                    <div className="xl:col-span-4 rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6">

                        <h3 className="text-xl font-semibold text-white mb-6">
                            Insights da IA
                        </h3>

                        <div className="space-y-4">

                            <div className="rounded-[12px] border border-emerald-500/10 bg-emerald-500/5 p-4">
                                <p className="text-emerald-400 text-sm">
                                    Tendência
                                </p>

                                <h4 className="text-white font-medium mt-1">
                                    Corolla lidera buscas
                                </h4>
                            </div>

                            <div className="rounded-[12px] border border-blue-500/10 bg-blue-500/5 p-4">
                                <p className="text-blue-400 text-sm">
                                    Oportunidade
                                </p>

                                <h4 className="text-white font-medium mt-1">
                                    18 leads aguardam SUV
                                </h4>
                            </div>

                            <div className="rounded-[12px] border border-purple-500/10 bg-purple-500/5 p-4">
                                <p className="text-purple-400 text-sm">
                                    Previsão
                                </p>

                                <h4 className="text-white font-medium mt-1">
                                    +12% oportunidades esta semana
                                </h4>
                            </div>

                        </div>

                    </div>

                    {/* EVENTOS */}

                    <div className="xl:col-span-4 rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6">

                        <h3 className="text-xl font-semibold text-white mb-6">
                            Eventos Recentes
                        </h3>

                        <div className="space-y-4">

                            <EventItem
                                title="Novo lead capturado"
                                subtitle="Corolla até R$ 80 mil"
                            />

                            <EventItem
                                title="Match encontrado"
                                subtitle="Lead × Corolla XEi"
                            />

                            <EventItem
                                title="WhatsApp enviado"
                                subtitle="Follow-up automático"
                            />

                            <EventItem
                                title="Lead qualificado"
                                subtitle="Score atualizado para 92"
                            />

                        </div>

                    </div>

                </div>
            </div>
        </div>
    );
}

function KpiCard({
    title,
    value,
    growth,
    icon,
}) {
    return (
        <div className="rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-6 backdrop-blur-xl">

            <div className="flex items-center justify-between">
                {icon}

                <div className="flex items-center gap-1 text-emerald-400 text-sm">
                    {growth}
                    <ArrowUpRight size={14} />
                </div>
            </div>

            <div className="mt-6 text-4xl font-bold text-white">
                {value}
            </div>

            <div className="mt-2 text-zinc-500">
                {title}
            </div>
        </div>
    );
}

function MiniStat({ title, value }) {
    return (
        <div className="rounded-[12px] border border-white/10 bg-white/5 p-4">
            <p className="text-xs text-zinc-500">
                {title}
            </p>

            <p className="mt-1 text-2xl font-bold text-white">
                {value}
            </p>
        </div>
    );
}

function PipelineRow({
    label,
    value,
    color,
}) {
    return (
        <div>
            <div className="mb-2 flex justify-between text-sm">
                <span className="text-zinc-400">
                    {label}
                </span>

                <span className="text-white">
                    {value}%
                </span>
            </div>

            <div className="h-3 rounded-[12px] bg-white/5 overflow-hidden">
                <div
                    className={`h-full ${color}`}
                    style={{
                        width: `${value}%`,
                    }}
                />
            </div>
        </div>
    );
}

function PerformanceItem({
    label,
    value,
}) {
    return (
        <div>
            <div className="mb-2 flex justify-between">
                <span className="text-zinc-400">
                    {label}
                </span>

                <span className="font-medium text-white">
                    {value}
                </span>
            </div>

            <div className="h-2 overflow-hidden rounded-[12px] bg-white/5">
                <div className="h-full w-[90%] rounded-[12px] bg-emerald-500" />
            </div>
        </div>
    );
}

function SideCard({
    title,
    value,
    icon,
}) {
    return (
        <div className="rounded-[12px] border border-white/10 bg-[#0B1220]/70 p-5 backdrop-blur-xl">

            <div className="flex items-center justify-between">
                {icon}
            </div>

            <div className="mt-4 text-3xl font-bold text-white">
                {value}
            </div>

            <div className="mt-1 text-zinc-500">
                {title}
            </div>
        </div>
    );

}

function EventItem({
    title,
    subtitle,
}) {
    return (
        <div className="flex gap-3">

            <div className="mt-1 h-2 w-2 rounded-[12px] bg-emerald-400" />

            <div>
                <p className="text-white text-sm font-medium">
                    {title}
                </p>

                <p className="text-zinc-500 text-xs">
                    {subtitle}
                </p>
            </div>

        </div>
    );
}