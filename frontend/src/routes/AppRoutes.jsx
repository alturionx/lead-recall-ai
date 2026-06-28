import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import PrivateLayout from "../components/PrivateLayout/PrivateLayout";

// ADMIN
import Dashboard from "../pages/Admin/Dashboard/Dashboard";
import Configuracoes from "../pages/Admin/Configuracoes/Configuracoes";
import Login from "../pages/Admin/Login/Login";


// SECURITY
import AdminRoute from "../security/AdminRoute";
import Simulator from "../pages/Admin/Simulator/Simulator";

export default function AppRoutes() {
    const isLogged = !!localStorage.getItem("token");

    return (
        <Router>
            <Routes>

                {/* 🔁 ROOT REDIRECT INTELIGENTE */}
                <Route
                    path="/" element={<Dashboard/>}
 /*                    element={
                        isLogged
                            ? <Navigate to="/admin/dashboard" replace />
                            : <Navigate to="/admin/login" replace />
                    } */
                />

                {/* 🔐 LOGIN (fora do guard) */}
                <Route path="/admin/login" element={<Login />} />

                {/* 🔐 ADMIN PROTEGIDO */}
                <Route element={<AdminRoute />}>
                    <Route element={<PrivateLayout />}>

                        <Route path="/admin/dashboard" element={<Dashboard />} />
                        <Route path="/admin/configuracoes" element={<Configuracoes />} />
                        <Route path="/admin/simulator" element={<Simulator />} />

                    </Route>
                </Route>
            </Routes>
        </Router>
    );
}