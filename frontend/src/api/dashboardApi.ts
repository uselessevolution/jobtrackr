import apiClient from "./apiClient";

import type {
    DashboardAnalytics,
    DashboardFunnel,
    DashboardSummary,
} from "../types/dashboard";

export async function getDashboardSummary():
    Promise<DashboardSummary> {
    const response =
        await apiClient.get<DashboardSummary>(
            "/api/dashboard/summary",
        );

    return response.data;
}

export async function getDashboardAnalytics(
    days = 30,
): Promise<DashboardAnalytics> {
    const response =
        await apiClient.get<DashboardAnalytics>(
            "/api/dashboard/analytics",
            {
                params: {
                    days,
                },
            },
        );

    return response.data;
}

export async function getDashboardFunnel():
    Promise<DashboardFunnel> {
    const response =
        await apiClient.get<DashboardFunnel>(
            "/api/dashboard/funnel",
        );

    return response.data;
}