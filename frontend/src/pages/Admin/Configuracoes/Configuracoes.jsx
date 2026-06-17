import {
    Settings,
    User,
    Lock,
    Bell,
    Palette,
    Globe,
    Save,
} from "lucide-react";

export default function Configuracoes() {
    return (
        <div className="space-y-8">
            {/* Header */}
            <div>
                <h1 className="text-3xl font-bold text-white">
                    Configurações
                </h1>

                <p className="text-zinc-400 mt-2">
                    Gerencie as configurações da plataforma
                </p>
            </div>

            {/* Grid */}
            <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
                {/* Sidebar Settings */}
                <div className="bg-white/3 border border-white/10 rounded-3xl p-4 h-fit">
                    <div className="space-y-2">
                        {[
                            {
                                icon: User,
                                title: "Perfil",
                                active: true,
                            },
                            {
                                icon: Lock,
                                title: "Segurança",
                            },
                            {
                                icon: Bell,
                                title: "Notificações",
                            },
                            {
                                icon: Palette,
                                title: "Aparência",
                            },
                            {
                                icon: Globe,
                                title: "Sistema",
                            },
                        ].map((item, index) => {
                            const Icon = item.icon;

                            return (
                                <button
                                    key={index}
                                    className={`w-full flex items-center gap-3 px-4 py-4 rounded-2xl transition ${
                                        item.active
                                            ? "bg-cyan-500 text-black"
                                            : "text-zinc-400 hover:bg-white/5 hover:text-white"
                                    }`}
                                >
                                    <Icon size={20} />

                                    <span className="font-medium">
                                        {item.title}
                                    </span>
                                </button>
                            );
                        })}
                    </div>
                </div>

                {/* Content */}
                <div className="xl:col-span-2 space-y-5">
                    {/* Perfil */}
                    <div className="bg-white/3 border border-white/10 rounded-3xl p-6">
                        <div className="flex items-center gap-3 mb-8">
                            <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 flex items-center justify-center text-cyan-400">
                                <User size={24} />
                            </div>

                            <div>
                                <h2 className="text-white text-xl font-bold">
                                    Perfil
                                </h2>

                                <p className="text-zinc-400 text-sm mt-1">
                                    Atualize suas informações
                                </p>
                            </div>
                        </div>

                        <div className="space-y-5">
                            {/* Avatar */}
                            <div className="flex items-center gap-5">
                                <img
                                    src="https://i.pravatar.cc/100"
                                    alt="Avatar"
                                    className="w-20 h-20 rounded-3xl object-cover border border-white/10"
                                />

                                <button className="px-5 py-3 rounded-2xl bg-white/5 border border-white/10 text-white hover:bg-white/10 transition">
                                    Alterar Foto
                                </button>
                            </div>

                            {/* Inputs */}
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                                <div>
                                    <label className="text-zinc-400 text-sm mb-2 block">
                                        Nome
                                    </label>

                                    <input
                                        type="text"
                                        defaultValue="Admin"
                                        className="w-full h-12 bg-white/5 border border-white/10 rounded-2xl px-4 text-white outline-none focus:border-cyan-400"
                                    />
                                </div>

                                <div>
                                    <label className="text-zinc-400 text-sm mb-2 block">
                                        Email
                                    </label>

                                    <input
                                        type="email"
                                        defaultValue="admin@email.com"
                                        className="w-full h-12 bg-white/5 border border-white/10 rounded-2xl px-4 text-white outline-none focus:border-cyan-400"
                                    />
                                </div>
                            </div>

                            <div>
                                <label className="text-zinc-400 text-sm mb-2 block">
                                    Biografia
                                </label>

                                <textarea
                                    rows="4"
                                    placeholder="Digite uma descrição..."
                                    className="w-full bg-white/5 border border-white/10 rounded-2xl px-4 py-4 text-white outline-none resize-none focus:border-cyan-400"
                                />
                            </div>
                        </div>
                    </div>

                    {/* Segurança */}
                    <div className="bg-white/3 border border-white/10 rounded-3xl p-6">
                        <div className="flex items-center gap-3 mb-8">
                            <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 flex items-center justify-center text-cyan-400">
                                <Lock size={24} />
                            </div>

                            <div>
                                <h2 className="text-white text-xl font-bold">
                                    Segurança
                                </h2>

                                <p className="text-zinc-400 text-sm mt-1">
                                    Atualize sua senha
                                </p>
                            </div>
                        </div>

                        <div className="space-y-5">
                            <div>
                                <label className="text-zinc-400 text-sm mb-2 block">
                                    Senha Atual
                                </label>

                                <input
                                    type="password"
                                    className="w-full h-12 bg-white/5 border border-white/10 rounded-2xl px-4 text-white outline-none focus:border-cyan-400"
                                />
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                                <div>
                                    <label className="text-zinc-400 text-sm mb-2 block">
                                        Nova Senha
                                    </label>

                                    <input
                                        type="password"
                                        className="w-full h-12 bg-white/5 border border-white/10 rounded-2xl px-4 text-white outline-none focus:border-cyan-400"
                                    />
                                </div>

                                <div>
                                    <label className="text-zinc-400 text-sm mb-2 block">
                                        Confirmar Senha
                                    </label>

                                    <input
                                        type="password"
                                        className="w-full h-12 bg-white/5 border border-white/10 rounded-2xl px-4 text-white outline-none focus:border-cyan-400"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Preferências */}
                    <div className="bg-white/3 border border-white/10 rounded-3xl p-6">
                        <div className="flex items-center gap-3 mb-8">
                            <div className="w-12 h-12 rounded-2xl bg-cyan-500/10 flex items-center justify-center text-cyan-400">
                                <Settings size={24} />
                            </div>

                            <div>
                                <h2 className="text-white text-xl font-bold">
                                    Preferências
                                </h2>

                                <p className="text-zinc-400 text-sm mt-1">
                                    Configurações gerais da plataforma
                                </p>
                            </div>
                        </div>

                        <div className="space-y-5">
                            {/* Toggle */}
                            {[
                                "Receber notificações por email",
                                "Modo escuro ativado",
                                "Comentários automáticos",
                            ].map((item, index) => (
                                <div
                                    key={index}
                                    className="flex items-center justify-between border border-white/10 bg-black/20 rounded-2xl px-5 py-4"
                                >
                                    <span className="text-white">
                                        {item}
                                    </span>

                                    <button className="w-14 h-8 rounded-full bg-cyan-500 relative">
                                        <div className="absolute top-1 right-1 w-6 h-6 bg-white rounded-full" />
                                    </button>
                                </div>
                            ))}
                        </div>

                        {/* Save */}
                        <div className="mt-8 flex justify-end">
                            <button className="bg-cyan-500 hover:bg-cyan-400 transition px-6 py-3 rounded-2xl text-black font-semibold flex items-center gap-2">
                                <Save size={18} />

                                Salvar Alterações
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}