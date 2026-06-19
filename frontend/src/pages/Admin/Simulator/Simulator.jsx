import { useState } from "react";
import { Send, Bot, User } from "lucide-react";
import axios from "axios";

export default function Simulator() {
    const [phone, setPhone] = useState("5524999999999");

    const [message, setMessage] = useState("");

    const [messages, setMessages] = useState([
        {
            type: "system",
            text: "Simulador WhatsApp • Lead Recall AI",
        },
    ]);

    async function handleSend() {
        if (!message.trim()) return;

        const userMessage = {
            type: "user",
            text: message,
        };

        setMessages((prev) => [...prev, userMessage]);

        try {
            const response = await axios.post(
                "http://localhost:8080/messages",
                {
                    phone,
                    content: message,

                }
            );

            setMessages((prev) => [
                ...prev,
                {
                    type: "bot",
                    text:
                        response.data?.message ||
                        "Mensagem processada com sucesso.",
                },
            ]);
        } catch (error) {
            console.error(error);

            setMessages((prev) => [
                ...prev,
                {
                    type: "bot",
                    text: "Erro ao processar mensagem.",
                },
            ]);
        }

        setMessage("");
    }

    function handleKeyDown(e) {
        if (e.key === "Enter") {
            handleSend();
        }
    }

    return (
        <div className="h-[83vh] flex justify-center">
            <div
                className="
      w-full
      overflow-hidden
      rounded-3xl
      border
      border-[#1B2B22]
      bg-[#050816]
      flex
      shadow-[0_0_80px_rgba(34,197,94,0.05)]
    "
            >

                {/* Sidebar */}

                <div className="w-80 border-r border-white/10 bg-[#202c33]">

                    <div className="p-5 border-b border-white/10">
                        <h2 className="text-white text-xl font-semibold">
                            Lead Recall AI
                        </h2>

                        <p className="text-zinc-400 text-sm mt-1">
                            Simulador WhatsApp
                        </p>
                    </div>

                    <div className="p-4">
                        <label className="text-zinc-400 text-sm">
                            Telefone
                        </label>

                        <input
                            value={phone}
                            onChange={(e) =>
                                setPhone(e.target.value)
                            }
                            className="mt-2 w-full rounded-xl bg-[#111b21] border border-white/10 px-4 py-3 text-white"
                        />
                    </div>
                    

                </div>

                {/* Chat */}

                <div className="flex-1 flex flex-col">

                    {/* Header */}

                    <div className="h-18 px-6 flex items-center border-b border-white/10 bg-[#202c33]">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 rounded-xl bg-green-500/10 flex items-center justify-center">
                                <Bot
                                    size={18}
                                    className="text-green-400"
                                />
                            </div>

                            <div>
                                <h3 className="text-white font-semibold">
                                    Assistente IA
                                </h3>

                                <p className="text-green-400 text-xs">
                                    Online • Processando intenções
                                </p>
                            </div>
                        </div>
                    </div>

                    {/* Messages */}

                    <div className="flex-1 overflow-y-auto p-6 space-y-4 bg-[#050816]">
                        <div className="absolute top-0 right-0 w-96 h-96 bg-green-500/5 blur-3xl rounded-full" />

                        {messages.map((msg, index) => (
                            <div
                                key={index}
                                className={`flex ${msg.type === "user"
                                    ? "justify-end"
                                    : "justify-start"
                                    }`}
                            >
                                <div
                                    className={`max-w-lg rounded-2xl px-4 py-3 flex gap-3 ${msg.type === "user"
                                        ? "bg-green-500/15 border border-green-500/20 text-white"
                                        : "bg-[#0B1220] border border-[#1B2B22] text-white"
                                        }`}
                                >
                                    {msg.type === "user" ? (
                                        <User size={18} />
                                    ) : (
                                        <Bot size={18} />
                                    )}

                                    <span>{msg.text}</span>
                                </div>
                            </div>
                        ))}

                    </div>

                    {/* Input */}

                    <div className="p-4 bg-[#202c33] border-t border-white/10 flex gap-3">

                        <input
                            value={message}
                            onChange={(e) =>
                                setMessage(e.target.value)
                            }
                            onKeyDown={handleKeyDown}
                            placeholder="Digite uma mensagem..."
                            className="flex-1 rounded-full bg-[#0B1220] focus:border-green-500/30 border border-[#1B2B22] px-5 py-3 text-white outline-none"
                        />

                        <button
                            onClick={handleSend}
                            className="w-12 h-12 rounded-full bg-gradient-to-r
from-green-500
to-green-600 shadow-lg
shadow-green-500/20 flex items-center justify-center hover:bg-emerald-600 transition"
                        >
                            <Send size={18} />
                        </button>

                    </div>

                </div>
            </div>
        </div>
    );
}