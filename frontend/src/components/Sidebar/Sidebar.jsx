import {
    LayoutDashboard,
    Users,
    MessageSquare,
    Brain,
    Upload,
    Workflow,
    Bot,
    Calendar,
    BarChart3,
    TrendingUp,
    Activity,
    Plug,
    Settings,
    ChevronRight,
    X,
} from "lucide-react";

import { NavLink } from "react-router-dom";

export default function Sidebar({
    sidebarOpen,
    setSidebarOpen,
}) {
    const sections = [
        {
            title: "Dashboard",
            items: [
                {
                    name: "Dashboard",
                    icon: LayoutDashboard,
                    path: "/",
                },
            ],
        },

        {
            title: "Leads",
            items: [
                {
                    name: "Leads",
                    icon: Users,
                    path: "/",
                },
                {
                    name: "Conversas",
                    icon: MessageSquare,
                    path: "/admin/simulator",
                },
                {
                    name: "Insights",
                    icon: Brain,
                    path: "/",
                },
                {
                    name: "Importações",
                    icon: Upload,
                    path: "/",
                },
            ],
        },

        {
            title: "Automações",
            items: [
                {
                    name: "Fluxos",
                    icon: Workflow,
                    path: "/",
                },
                {
                    name: "Respostas IA",
                    icon: Bot,
                    path: "/",
                },
                {
                    name: "Agendamentos",
                    icon: Calendar,
                    path: "/",
                },
            ],
        },

        {
            title: "Relatórios",
            items: [
                {
                    name: "Performance",
                    icon: BarChart3,
                    path: "/",
                },
                {
                    name: "Conversões",
                    icon: TrendingUp,
                    path: "/",
                },
                {
                    name: "Atividades",
                    icon: Activity,
                    path: "/",
                },
            ],
        },

        {
            title: "Configurações",
            items: [
                {
                    name: "Integrações",
                    icon: Plug,
                    path: "/",
                },
                {
                    name: "Configurações",
                    icon: Settings,
                    path: "/admin/configuracoes",
                },
            ],
        },
    ];

    return (
        <>
            {/* Overlay Mobile */}
            <div
                onClick={() => setSidebarOpen(false)}
                className={`
                    fixed inset-0 bg-black/70 backdrop-blur-sm z-40 lg:hidden
                    transition-all duration-300
                    ${
                        sidebarOpen
                            ? "opacity-100 visible"
                            : "opacity-0 invisible"
                    }
                `}
            />

            <aside
                className={`
                    fixed lg:sticky top-0 left-0 z-50
                    h-screen w-72
                    bg-[#050816]
                    border-r border-[#1B2B22]
                    flex flex-col
                    transition-transform duration-300

                    ${
                        sidebarOpen
                            ? "translate-x-0"
                            : "-translate-x-full lg:translate-x-0"
                    }
                `}
            >
                {/* Glow */}
                <div className="absolute inset-0 pointer-events-none">
                    <div className="absolute top-0 left-0 w-full h-64 bg-green-500/5 blur-3xl" />
                </div>

                {/* Top */}
                <div className="relative p-6 border-b border-[#1B2B22]">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                            {/* Logo */}
                            <div
                                className="
                                w-12 h-12
                                rounded-2xl
                                bg-linear-to-br
                                from-green-400
                                to-green-700
                                flex items-center justify-center
                                shadow-lg shadow-green-500/20
                            "
                            >
                                <Brain
                                    size={24}
                                    className="text-white"
                                />
                            </div>

                            <div>
                                <h1 className="text-white font-black text-lg leading-none">
                                    LEAD RECALL
                                    <span className="text-green-500">
                                        {" "}
                                        AI
                                    </span>
                                </h1>

                                <p className="text-xs text-zinc-500 mt-1">
                                    by AlturionX
                                </p>
                            </div>
                        </div>

                        <button
                            onClick={() =>
                                setSidebarOpen(false)
                            }
                            className="lg:hidden text-zinc-500 hover:text-white"
                        >
                            <X size={22} />
                        </button>
                    </div>
                </div>

                {/* Menu */}
                <div className="flex-1 overflow-y-auto p-4">
                    {sections.map((section) => (
                        <div
                            key={section.title}
                            className="mb-8"
                        >
                            <h3
                                className="
                                text-[11px]
                                uppercase
                                tracking-wider
                                text-zinc-600
                                font-semibold
                                mb-3
                                px-3
                            "
                            >
                                {section.title}
                            </h3>

                            <div className="space-y-1">
                                {section.items.map((item) => {
                                    const Icon = item.icon;

                                    return (
                                        <NavLink
                                            key={item.path}
                                            to={item.path}
                                            onClick={() =>
                                                setSidebarOpen(
                                                    false
                                                )
                                            }
                                            className={({
                                                isActive,
                                            }) =>
                                                `
                                                group
                                                flex
                                                items-center
                                                justify-between
                                                px-4
                                                py-3
                                                rounded-xl
                                                transition-all
                                                duration-200

                                                ${
                                                    isActive
                                                        ? `
                                                            bg-green-500/10
                                                            text-green-400
                                                            border
                                                            border-green-500/20
                                                            shadow-lg
                                                            shadow-green-500/10
                                                        `
                                                        : `
                                                            text-zinc-400
                                                            hover:bg-green-500/5
                                                            hover:text-green-400
                                                        `
                                                }
                                            `
                                            }
                                        >
                                            {({
                                                isActive,
                                            }) => (
                                                <>
                                                    <div className="flex items-center gap-3">
                                                        <Icon
                                                            size={
                                                                18
                                                            }
                                                        />

                                                        <span className="font-medium text-sm">
                                                            {
                                                                item.name
                                                            }
                                                        </span>
                                                    </div>

                                                    <ChevronRight
                                                        size={
                                                            16
                                                        }
                                                        className={`
                                                            transition-all
                                                            ${
                                                                isActive
                                                                    ? "translate-x-1 opacity-100"
                                                                    : "opacity-0 group-hover:opacity-100 group-hover:translate-x-1"
                                                            }
                                                        `}
                                                    />
                                                </>
                                            )}
                                        </NavLink>
                                    );
                                })}
                            </div>
                        </div>
                    ))}
                </div>

                {/* Bottom */}
                <div className="p-4 border-t border-[#1B2B22] space-y-4">
                    {/* Status IA */}
                    <div
                        className="
                        flex items-center gap-3
                        bg-green-500/5
                        border border-green-500/10
                        rounded-xl
                        px-4 py-3
                    "
                    >
                        <div
                            className="
                            w-2.5 h-2.5
                            rounded-full
                            bg-green-500
                            animate-pulse
                        "
                        />

                        <span className="text-green-400 text-sm font-medium">
                            Sistema Online
                        </span>
                    </div>

                    {/* User */}
                    <div
                        className="
                        bg-[#0B1220]
                        border border-[#1B2B22]
                        rounded-2xl
                        p-4
                    "
                    >
                        <div className="flex items-center gap-3">
                            <div
                                className="
                                w-12 h-12
                                rounded-xl
                                bg-linear-to-br
                                from-green-500
                                to-green-700
                                flex items-center justify-center
                                text-white
                                font-bold
                            "
                            >
                                A
                            </div>

                            <div className="flex-1">
                                <h3 className="text-white font-semibold">
                                    AlturionX
                                </h3>

                                <p className="text-zinc-500 text-sm">
                                    admin@leadrecall.ai
                                </p>
                            </div>
                        </div>
                    </div>

                    {/* Footer */}
                    <div className="text-center">
                        <p className="text-xs text-zinc-600">
                            Lead Recall AI
                        </p>

                        <p className="text-xs text-zinc-700 mt-1">
                            Every lead remembered.
                        </p>
                    </div>
                </div>
            </aside>
        </>
    );
}