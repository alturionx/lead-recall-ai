export default function Timeline({
    time,
    text,
    color = "bg-emerald-400"
}) {

    return (
        <div className="flex gap-4">

            <div className="flex flex-col items-center">

                <span
                    className={`h-2.5 w-2.5 rounded-full ${color}`}
                />

                <div className="h-8 w-px bg-white/10" />

            </div>

            <div>

                <span className="text-xs text-zinc-500">
                    {time}
                </span>

                <p className="text-sm text-white">
                    {text}
                </p>

            </div>

        </div>
    );
}