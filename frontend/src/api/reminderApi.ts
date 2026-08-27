import apiClient from "./apiClient";

import type {
    CreateReminderRequest,
    Reminder,
    ReminderPage,
} from "../types/reminder";

export async function getReminders(
    page = 0,
    size = 10,
): Promise<ReminderPage> {
    const response =
        await apiClient.get<ReminderPage>(
            "/api/reminders",
            {
                params: {
                    page,
                    size,
                    sortBy: "scheduledAt",
                    direction: "asc",
                },
            },
        );

    return response.data;
}

export async function createReminder(
    request: CreateReminderRequest,
): Promise<Reminder> {
    const response =
        await apiClient.post<Reminder>(
            "/api/reminders",
            request,
        );

    return response.data;
}