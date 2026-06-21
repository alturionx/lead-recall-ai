export default function MetricCard({
    title,
    value,
    growth,
    icon,
    color = "blue"
}) {

    const colors = {
        blue: {
            box: "bg-blue-500 text-white",
            chart: "text-blue-400"
        },

        purple: {
            box: "bg-purple-600 text-white",
            chart: "text-purple-400"
        },

        green: {
            box: "bg-emerald-500 text-white",
            chart: "text-emerald-400"
        },

        orange: {
            box: "bg-orange-500 text-white",
            chart: "text-orange-400"
        }
    };

    return (
        <div
            className="
                relative
                overflow-hidden
                rounded-2xl
                border
                border-slate-800
                bg-[#091321]
                px-6
                py-5
                shadow-[0_0_30px_rgba(0,0,0,.25)]
            "
        >

            <div className="flex justify-between">

                <div>

                    <div
                        className={`
                            h-12
                            w-12
                            rounded-xl
                            flex
                            items-center
                            justify-center
                            shadow-lg
                            ${colors[color].box}
                        `}
                    >
                        {icon}
                    </div>

                    <div className="mt-4 text-zinc-300 text-sm">
                        {title}
                    </div>

                    <div className="mt-2 flex items-center gap-3">

                        <span className="text-5xl font-bold text-white">
                            {value}
                        </span>

                        <span
                            className="
                                rounded-lg
                                bg-emerald-500/15
                                px-2
                                py-1
                                text-sm
                                font-medium
                                text-emerald-400
                            "
                        >
                            ↑ {growth}
                        </span>

                    </div>

                    <div className="mt-2 text-zinc-500 text-sm">
                        vs ontem
                    </div>

                </div>

                {/* gráfico */}

                <div className="flex items-end">

                    <svg
                        width="100"
                        height="60"
                        viewBox="0 0 100 60"
                        className={colors[color].chart}
                    >
                        <path
                            d="M0 45
                               C15 50 20 20 35 25
                               C50 30 55 5 70 15
                               C85 25 90 10 100 12"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth="3"
                            strokeLinecap="round"
                        />
                    </svg>

                </div>

            </div>

            <div
                className="
                    absolute
                    right-0
                    top-0
                    h-32
                    w-32
                    rounded-full
                    bg-blue-500/5
                    blur-3xl
                "
            />

        </div>
    );
}