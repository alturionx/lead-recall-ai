export default function StatusBadge({ status }) {

    const styles = {
        NOVO:
            "bg-blue-500/20 text-blue-400 border-blue-500/20",

        CONTATADO:
            "bg-purple-500/20 text-purple-400 border-purple-500/20",

        NEGOCIANDO:
            "bg-orange-500/20 text-orange-400 border-orange-500/20",
    };

    return (
        <span
            className={`
                rounded-lg
                border
                px-3
                py-1
                text-xs
                font-semibold
                ${styles[status]}
            `}
        >
            {status}
        </span>
    );
}