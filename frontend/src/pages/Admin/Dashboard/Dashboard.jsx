import {
    Users,
    Target,
    TrendingUp,
    Car,
    Brain,
    MessageCircle,
    Phone,
    Bell,
    RefreshCw,
    Flame,
    Trophy,
    DollarSign,
    Activity,
    Sparkles
} from "lucide-react";

import {
    Card,
    MetricCard,
    Opportunity,
    Progress,
    Gap,
    Insight,
    Timeline,
    Follow,
    StatusBadge
} from "./components";
import { useState, useEffect } from "react";
import getLeads from "./functions/getLeads";

export default function Dashboard() {
    const [hotLeads, setHotLeads] = useState([])

    useEffect(() => {
        async function loadLeads() {
            try {
                const leads = await getLeads();
                setHotLeads(leads);
            } catch (error) {
                console.error(error);
            }
        }

        loadLeads();
    }, []);

    /*     const hotLeads = [
            { name: "João Santos", interest: "Duster", score: 87, last: "10 min atrás" },
            { name: "Maria Oliveira", interest: "Corolla", score: 82, last: "5 min atrás" },
            { name: "Pedro Almeida", interest: "HB20", score: 79, last: "30 min atrás" },
            { name: "Carlos Lima", interest: "T-Cross", score: 76, last: "2h atrás" },
        ]; */

    return (
        <div className="min-h-screen bg-gradient-to-br from-[#030712] via-[#07101D] to-[#020617] text-white">
            <div className="space-y-6">
                <div className="relative overflow-hidden rounded-xl border border-white/10 bg-linear-to-br from-emerald-500/10 via-[#0B1220] to-cyan-500/10 p-8">
                    <div className="absolute top-0 right-0 h-72 w-72 rounded-xl bg-emerald-500/20 blur-3xl" />
                    <div className="absolute bottom-0 left-0 h-72 w-72 rounded-xl bg-cyan-500/10 blur-3xl" />
                    <div className="relative z-10 flex flex-col lg:flex-row lg:items-center lg:justify-between gap-6">
                        <div>
                            <div className="inline-flex items-center gap-2 rounded-xl border border-emerald-500/20 bg-emerald-500/10 px-4 py-2 text-sm text-emerald-400"><Sparkles size={16} />IA ativa e processando leads</div>
                            <h1 className="mt-4 text-5xl font-bold text-white">
                                Lead Recall AI
                            </h1>

                            <p className="mt-3 max-w-2xl text-zinc-400">
                                Plataforma inteligente de recuperação de leads,
                                matching de veículos e geração automática de
                                oportunidades.
                            </p>
                        </div>
                        <div className="flex items-center justify-between">

                            <div className="flex gap-3">
                                <button className="rounded-xl border border-white/10 bg-[#0B1220] px-4 py-2">
                                    Hoje, 24 de Maio
                                </button>

                                <button className="rounded-xl border border-white/10 bg-[#0B1220] px-4 py-2 flex items-center gap-2">
                                    <RefreshCw size={16} />
                                    Atualizar
                                </button>
                            </div>
                        </div>

                    </div>
                </div>

                <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
                    <MetricCard
                        title="Leads Hoje"
                        value="42"
                        growth="16%"
                        color="blue"
                        icon={<Users size={24} />}
                    />

                    <MetricCard
                        title="Oportunidades"
                        value="18"
                        growth="20%"
                        color="purple"
                        icon={<Target size={24} />}
                    />

                    <MetricCard
                        title="Conversão"
                        value="21%"
                        growth="4%"
                        color="green"
                        icon={<TrendingUp size={24} />}
                    />

                    <MetricCard
                        title="Estoque Ativo"
                        value="128"
                        growth="8%"
                        color="orange"
                        icon={<Car size={24} />}
                    />
                </div>

                <div className="grid gap-4 xl:grid-cols-12">
                    <Card className="xl:col-span-6">
                        <h3 className="mb-4 flex items-center gap-2 text-xl font-semibold">
                            <Flame className="text-orange-400" />
                            Leads Quentes
                        </h3>

                        <table className="w-full text-sm">
                            <thead className="text-zinc-500">
                                <tr>
                                    <th className="py-2 text-left">Lead</th>
                                    <th className="text-left">Interesse</th>
                                    <th>Score</th>
                                    <th>Último contato</th>
                                </tr>
                            </thead>

                            <tbody>
                                {hotLeads.map((lead) => (
                                    <tr key={lead.id} className="border-t border-white/5">
                                        <td className="py-3">{lead.name}</td>
                                        <td>{lead.vehicle_interest}</td>
                                        <td className="text-center">

                                            <span
                                                className="
        px-3
        py-1
        rounded-lg
        border
        border-emerald-500/30
        bg-emerald-500/10
        text-emerald-400
        "
                                            >
                                                {lead.score}
                                            </span>

                                        </td>
                                        <td className="text-center text-orange-400">{lead.last_interaction_at}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </Card>

                    <Card className="xl:col-span-6">
                        <h3 className="mb-4 text-xl font-semibold">Oportunidades Recentes</h3>

                        <div className="space-y-3">
                            <Opportunity lead="João Santos" vehicle="Duster 1.6 2022" score="82" status="NOVO" />
                            <Opportunity lead="Maria Oliveira" vehicle="Corolla XEi 2023" score="90" status="CONTATADO" />
                            <Opportunity lead="Pedro Almeida" vehicle="HB20 Comfort" score="74" status="NEGOCIANDO" />
                        </div>
                    </Card>
                </div>

                <div className="grid gap-4 xl:grid-cols-3">
                    <Card>
                        <h3 className="mb-4 flex items-center gap-2 text-xl font-semibold">
                            <Trophy className="text-yellow-400" />
                            Estoque Mais Vendável
                        </h3>

                        <Progress label="Duster" value={81} />
                        <Progress label="Corolla" value={88} />
                        <Progress label="HB20" value={75} />
                        <Progress label="T-Cross" value={72} />
                    </Card>

                    <Card>
                        <h3 className="mb-4 text-xl font-semibold">Gap de Negociação</h3>

                        <div className="space-y-4">
                            <Gap model="Duster 132k" gap="R$12.000" score="82" />
                            <Gap model="Corolla 145k" gap="R$5.000" score="90" />
                            <Gap model="HB20 95k" gap="R$0" score="74" />
                        </div>
                    </Card>

                    <Card>
                        <h3 className="mb-4 flex items-center gap-2 text-xl font-semibold">
                            <Brain className="text-purple-400" />
                            Insights da IA
                        </h3>

                        <Insight text="Duster é o modelo mais procurado hoje" />
                        <Insight text="Leads até R$120k convertem melhor" />
                        <Insight text="Resposta em até 5 min gera 3x mais vendas" />
                        <Insight text="Automação possui +32% de match rate" />
                    </Card>
                </div>

                <div className="grid gap-4 xl:grid-cols-3">
                    <Card>
                        <h3 className="mb-4 flex items-center gap-2 text-xl font-semibold">
                            <Activity />
                            Atividades em Tempo Real
                        </h3>

                        <Timeline time="11:45" text="Novo lead interessado em Duster" />
                        <Timeline time="11:44" text="IA detectou intenção BUY_CAR" />
                        <Timeline time="11:43" text="Match criado automaticamente" />
                        <Timeline time="11:42" text="Oportunidade criada" />
                    </Card>

                    <Card>
                        <h3 className="mb-4 text-xl font-semibold">Follow-ups Sugeridos</h3>

                        <Follow title="Ligar para João Santos" icon={<Phone size={16} />} />
                        <Follow title="Enviar proposta para Maria Oliveira" icon={<MessageCircle size={16} />} />
                        <Follow title="Follow-up com Pedro Almeida" icon={<Bell size={16} />} />
                    </Card>

                    <Card>
                        <h3 className="mb-4 text-xl font-semibold">Metas do Dia</h3>

                        <div className="flex items-center justify-center">
                            <div className="flex h-40 w-40 items-center justify-center rounded-full border-[14px] border-emerald-500">
                                <div className="text-center">
                                    <div className="text-4xl font-bold">70%</div>
                                    <div className="text-zinc-400">Atingido</div>
                                </div>
                            </div>
                        </div>
                    </Card>
                </div>

            </div>
        </div>
    );
}









