import { Outlet } from "react-router-dom";
import { useState } from "react";

import Sidebar from "../Sidebar/Sidebar";
import Header from "../Header/Header";
import AnimatedOutlet from "../AnimatedOutlet/AnimatedOutlet";

export default function PrivateLayout() {
    const [sidebarOpen, setSidebarOpen] =
        useState(false);

    return (
        <div className="h-screen overflow-hidden bg-[#050816] flex">
            {/* Sidebar */}
            <Sidebar
                sidebarOpen={sidebarOpen}
                setSidebarOpen={setSidebarOpen}
            />

            {/* Área principal */}
            <div className="flex-1 flex flex-col overflow-hidden">
                {/* Header */}
                <Header
                    setSidebarOpen={setSidebarOpen}
                />

                {/* Conteúdo com scroll */}
                <main className="flex-1 overflow-y-auto p-1 lg:p-8">
                    <AnimatedOutlet>
                        <Outlet />
                    </AnimatedOutlet>
                </main>
            </div>
        </div>
    );
}