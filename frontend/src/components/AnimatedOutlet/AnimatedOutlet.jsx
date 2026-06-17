import { motion } from "framer-motion";
import { useLocation } from "react-router-dom";

export default function AnimatedOutlet({ children }) {
    const location = useLocation();

    return (
        <motion.div
            key={location.pathname}
            initial={{
                opacity: 0,
                y: 15,
            }}
            animate={{
                opacity: 1,
                y: 0,
            }}
            exit={{
                opacity: 0,
                y: -15,
            }}
            transition={{
                duration: 0.25,
            }}
            className="h-full"
        >
            {children}
        </motion.div>
    );
}