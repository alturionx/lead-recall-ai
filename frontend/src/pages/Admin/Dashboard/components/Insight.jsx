export default function Insight({
    icon = "🧠",
    text,
    subtitle
}) {

    return (
        <div
            className="
                flex
                items-start
                gap-4
                rounded-xl
                border
                border-white/5
                bg-white/[0.02]
                p-4
                my-2
            "
        >

            <div
                className="
                    h-10
                    w-10
                    rounded-full
                    bg-purple-500/20
                    flex
                    items-center
                    justify-center
                "
            >
                {icon}
            </div>

            <div>

                <p className="text-white">
                    {text}
                </p>

                {subtitle && (
                    <p className="mt-1 text-xs text-zinc-500">
                        {subtitle}
                    </p>
                )}

            </div>

        </div>
    );
}