export default function Card({
    children,
    className = ""
}) {

    return (
        <div
            className={`
                relative
                overflow-hidden
                rounded-2xl
                border
                border-slate-800
                bg-gradient-to-br
                from-[#07101D]
                via-[#091423]
                to-[#0B1220]
                p-6
                shadow-[0_0_40px_rgba(0,0,0,0.35)]
                backdrop-blur-xl
                ${className}
            `}
        >
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_right,rgba(59,130,246,0.08),transparent_40%)]" />

            <div className="relative z-10">
                {children}
            </div>
        </div>
    );
}