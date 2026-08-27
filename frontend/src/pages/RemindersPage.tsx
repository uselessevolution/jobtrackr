import {
    useEffect,
    useState,
} from "react";

import axios from "axios";
import { Link } from "react-router-dom";

import {
    createReminder,
    getReminders,
} from "../api/reminderApi";

import type {
    Reminder,
    ReminderChannel,
} from "../types/reminder";

import type {
    ApiErrorResponse,
} from "../types/api";

export default function RemindersPage() {
    const [reminders, setReminders] =
        useState<Reminder[]>([]);

    const [page, setPage] =
        useState(0);

    const [totalPages, setTotalPages] =
        useState(0);

    const [isLoading, setIsLoading] =
        useState(true);

    const [isCreating, setIsCreating] =
        useState(false);

    const [error, setError] =
        useState("");

    const [applicationId, setApplicationId] =
        useState("");

    const [reminderType, setReminderType] =
        useState("INTERVIEW");

    const [scheduledAt, setScheduledAt] =
        useState("");

    const [channel, setChannel] =
        useState<ReminderChannel>("IN_APP");

    const [message, setMessage] =
        useState("");

    useEffect(() => {
        async function loadReminders() {
            setIsLoading(true);
            setError("");

            try {
                const response =
                    await getReminders(
                        page,
                        10,
                    );

                setReminders(
                    response.content,
                );

                setTotalPages(
                    response.totalPages,
                );
            } catch (caughtError) {
                if (
                    axios.isAxiosError<ApiErrorResponse>(
                        caughtError,
                    )
                ) {
                    setError(
                        caughtError.response?.data?.message
                        ?? "Failed to load reminders.",
                    );
                } else {
                    setError(
                        "Failed to load reminders.",
                    );
                }
            } finally {
                setIsLoading(false);
            }
        }

        void loadReminders();
    }, [page]);

    async function handleCreateReminder() {
        if (!applicationId.trim()) {
            setError(
                "Application ID is required.",
            );
            return;
        }

        if (!scheduledAt) {
            setError(
                "Scheduled date and time are required.",
            );
            return;
        }

        setIsCreating(true);
        setError("");

        try {
            await createReminder({
                applicationId:
                    applicationId.trim(),

                type: reminderType,

                scheduledAt:
                    new Date(
                        scheduledAt,
                    ).toISOString(),

                channels: [
                    channel,
                ],

                message:
                    message.trim() || null,
            });

            /*
             * Reload page 0 after creation because
             * reminders are sorted by scheduledAt.
             */
            const response =
                await getReminders(
                    0,
                    10,
                );

            setPage(0);
            setReminders(
                response.content,
            );
            setTotalPages(
                response.totalPages,
            );

            setScheduledAt("");
            setMessage("");
        } catch (caughtError) {
            if (
                axios.isAxiosError<ApiErrorResponse>(
                    caughtError,
                )
            ) {
                setError(
                    caughtError.response?.data?.message
                    ?? "Failed to create reminder.",
                );
            } else {
                setError(
                    "Failed to create reminder.",
                );
            }
        } finally {
            setIsCreating(false);
        }
    }

    return (
        <main>
            <p>
                <Link to="/">
                    Back to Home
                </Link>
            </p>

            <h1>Reminders</h1>

            {error && (
                <p role="alert">
                    {error}
                </p>
            )}

            <section>
                <h2>Create Reminder</h2>

                <div>
                    <label htmlFor="applicationId">
                        Application ID
                    </label>

                    <input
                        id="applicationId"
                        value={applicationId}
                        onChange={(event) =>
                            setApplicationId(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="reminderType">
                        Type
                    </label>

                    <select
                        id="reminderType"
                        value={reminderType}
                        onChange={(event) =>
                            setReminderType(
                                event.target.value,
                            )
                        }
                    >
                        <option value="INTERVIEW">
                            Interview
                        </option>

                        <option value="FOLLOW_UP">
                            Follow Up
                        </option>
                    </select>
                </div>

                <div>
                    <label htmlFor="scheduledAt">
                        Scheduled At
                    </label>

                    <input
                        id="scheduledAt"
                        type="datetime-local"
                        value={scheduledAt}
                        onChange={(event) =>
                            setScheduledAt(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="channel">
                        Channel
                    </label>

                    <select
                        id="channel"
                        value={channel}
                        onChange={(event) =>
                            setChannel(
                                event.target.value as ReminderChannel,
                            )
                        }
                    >
                        <option value="IN_APP">
                            In App
                        </option>

                        <option value="EMAIL">
                            Email
                        </option>
                    </select>
                </div>

                <div>
                    <label htmlFor="message">
                        Message
                    </label>

                    <textarea
                        id="message"
                        value={message}
                        onChange={(event) =>
                            setMessage(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <button
                    type="button"
                    onClick={
                        handleCreateReminder
                    }
                    disabled={isCreating}
                >
                    {isCreating
                        ? "Creating..."
                        : "Create Reminder"}
                </button>
            </section>

            <hr />

            <section>
                <h2>Your Reminders</h2>

                {isLoading ? (
                    <p>
                        Loading reminders...
                    </p>
                ) : reminders.length === 0 ? (
                    <p>
                        No reminders found.
                    </p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>Type</th>
                                <th>Scheduled</th>
                                <th>Channel</th>
                                <th>Status</th>
                                <th>Message</th>
                            </tr>
                        </thead>

                        <tbody>
                            {reminders.map(
                                (reminder) => (
                                    <tr
                                        key={
                                            reminder.id
                                        }
                                    >
                                        <td>
                                            {
                                                reminder.type
                                            }
                                        </td>

                                        <td>
                                            {
                                                reminder.scheduledAt
                                            }
                                        </td>

                                        <td>
                                            {
                                                reminder.channels.join(
                                                    ", ",
                                                )
                                            }
                                        </td>

                                        <td>
                                            {
                                                reminder.status
                                            }
                                        </td>

                                        <td>
                                            {
                                                reminder.message
                                                ?? "—"
                                            }
                                        </td>
                                    </tr>
                                ),
                            )}
                        </tbody>
                    </table>
                )}

                <div>
                    <button
                        type="button"
                        disabled={page === 0}
                        onClick={() =>
                            setPage(
                                (current) =>
                                    current - 1,
                            )
                        }
                    >
                        Previous
                    </button>

                    <span>
                        {" "}
                        Page {page + 1}
                        {" / "}
                        {Math.max(
                            totalPages,
                            1,
                        )}
                        {" "}
                    </span>

                    <button
                        type="button"
                        disabled={
                            totalPages === 0
                            || page
                            >= totalPages - 1
                        }
                        onClick={() =>
                            setPage(
                                (current) =>
                                    current + 1,
                            )
                        }
                    >
                        Next
                    </button>
                </div>
            </section>
        </main>
    );
}