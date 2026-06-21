import StatusBadge from "./StatusBadge";

export default function Opportunity({
    lead,
    vehicle,
    score,
    status
}) {

    return (
        <div
            className="
                rounded-xl
                border
                border-white/5
                bg-white/[0.02]
                p-4
            "
        >

            <div className="font-semibold text-white">
                {lead}
            </div>

            <div className="mt-1 text-zinc-400">
                {vehicle}
            </div>

            <div className="mt-3 flex items-center justify-between">

                <span
                    className="
                        rounded-lg
                        bg-emerald-500/10
                        px-3
                        py-1
                        text-sm
                        font-semibold
                        text-emerald-400
                    "
                >
                    {score}
                </span>

                <StatusBadge status={status} />

            </div>

        </div>
    );
}