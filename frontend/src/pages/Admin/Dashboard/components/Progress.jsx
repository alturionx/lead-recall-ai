export default function Progress({
    label,
    value
}) {

    return (
        <div>

            <div className="mb-2 flex justify-between">

                <span className="text-zinc-300">
                    {label}
                </span>

                <span className="text-white font-medium">
                    {value}
                </span>

            </div>

            <div className="h-2 rounded-full bg-white/5">

                <div
                    className="
                        h-2
                        rounded-full
                        bg-gradient-to-r
                        from-indigo-500
                        to-blue-400
                    "
                    style={{
                        width: `${value}%`
                    }}
                />

            </div>

        </div>
    );
}