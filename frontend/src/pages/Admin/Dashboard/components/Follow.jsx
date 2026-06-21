export default function Follow({
    title,
    subtitle,
    action
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

                <p className="font-medium text-white">
                    {title}
                </p>

                {subtitle && (
                    <p className="text-xs text-zinc-500">
                        {subtitle}
                    </p>
                )}

            </div>

            {action}

        </div>
    );
}