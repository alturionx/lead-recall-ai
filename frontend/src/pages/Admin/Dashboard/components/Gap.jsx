export default function Gap({
    model,
    lead,
    budget,
    gap,
    score
}) {

    return (
        <div
            className="
                flex
                items-center
                justify-between
                rounded-xl
                border
                border-white/5
                bg-white/[0.02]
                p-4
            "
        >

            <div>

                <h4 className="font-medium text-white">
                    {model}
                </h4>

                <p className="text-xs text-zinc-500">
                    Lead: {lead}
                </p>

                <p className="mt-2 text-sm text-zinc-300">
                    Budget: {budget}
                </p>

                <p className="text-sm text-orange-400">
                    Gap: {gap}
                </p>

            </div>

            <div
                className="
                    flex
                    h-12
                    w-12
                    items-center
                    justify-center
                    rounded-full
                    border
                    border-emerald-500/30
                    text-emerald-400
                    font-bold
                "
            >
                {score}
            </div>

        </div>
    );
}