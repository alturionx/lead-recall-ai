import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../../api/api";

function Login() {
    const [usuario, setUsuario] = useState("");
    const [senha, setSenha] = useState("");
    const navigate = useNavigate();

    async function handleLogin(e) {
        e.preventDefault();

        if (!usuario || !senha) {
            alert("Preencha todos os campos");
            return;
        }

        try {
            const response = await api.post("/auth/login", {
                username: usuario,
                password: senha,
            });

            const token = response.data.token;

            // 🔐 PADRÃO DO SISTEMA
            localStorage.setItem("token", token);

            alert("Login realizado com sucesso 🚀");

            // 👉 vai direto pro dashboard
            navigate("/admin/dashboard");

        } catch (error) {
            if (error.response?.status === 401) {
                alert("Usuário ou senha inválidos");
            } else {
                alert("Erro no servidor");
            }

            console.error("Erro no Login:", error);
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-zinc-950">
            <form
                onSubmit={handleLogin}
                className="bg-stone-900/70 border border-stone-800 p-8 rounded-3xl w-87.5 space-y-5"
            >
                <div className="flex justify-center">
                    {/* <img src={logo} width={120} /> */}
                </div>

                <div>
                    <label className="text-stone-300 text-sm">Usuário</label>
                    <input
                        type="text"
                        onChange={(e) => setUsuario(e.target.value)}
                        className="w-full mt-2 h-12 rounded-2xl bg-stone-950 border border-stone-700 text-white px-4 outline-none focus:border-amber-500"
                        placeholder="Digite seu usuário"
                    />
                </div>

                <div>
                    <label className="text-stone-300 text-sm">Senha</label>
                    <input
                        type="password"
                        onChange={(e) => setSenha(e.target.value)}
                        className="w-full mt-2 h-12 rounded-2xl bg-stone-950 border border-stone-700 text-white px-4 outline-none focus:border-amber-500"
                        placeholder="Digite sua senha"
                    />
                </div>

                <button
                    type="submit"
                    className="w-full h-12 rounded-2xl bg-linear-to-r from-amber-500 to-red-500 text-black font-bold"
                >
                    Entrar
                </button>

                <small className="text-stone-500 text-xs text-center block">
                    Sistema de gestão para restaurantes
                </small>
            </form>
        </div>
    );
}

export default Login;