import {
    useEffect,
    useState,
} from "react";

import {
    getDashboardAnalytics,
    getDashboardFunnel,
    getDashboardSummary,
} from "../api/dashboardApi";

import type {
    DashboardAnalytics,
    DashboardFunnel,
    DashboardSummary,
} from "../types/dashboard";

export default function DashboardPage() {
    const [summary, setSummary] =
        useState<DashboardSummary | null>(null);

    const [analytics, setAnalytics] =
        useState<DashboardAnalytics | null>(null);

    const [funnel, setFunnel] =
        useState<DashboardFunnel | null>(null);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState("");

    useEffect(() => {
        async function loadDashboard() {
            setIsLoading(true);
            setError("");

            try {
                const [
                    summaryResponse,
                    analyticsResponse,
                    funnelResponse,
                ] = await Promise.all([
                    getDashboardSummary(),
                    getDashboardAnalytics(30),
                    getDashboardFunnel(),
                ]);

                setSummary(summaryResponse);
                setAnalytics(analyticsResponse);
                setFunnel(funnelResponse);
            } catch {
                setError(
                    "Failed to load dashboard data.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadDashboard();
    }, []);

    if (isLoading) {
        return (
            <main>
                <h1>Dashboard</h1>
                <p>Loading dashboard...</p>
            </main>
        );
    }

    if (
        error
        || !summary
        || !analytics
        || !funnel
    ) {
        return (
            <main>
                <h1>Dashboard</h1>

                <p role="alert">
                    {error
                        || "Dashboard data is unavailable."}
                </p>
            </main>
        );
    }

    return (
        <main>
            <h1>Dashboard</h1>

            <section>
                <h2>Overview</h2>

                <div className="dashboard-grid">
                    <article>
                        <h3>Total Applications</h3>
                        <p>
                            {summary.totalApplications}
                        </p>
                    </article>

                    <article>
                        <h3>
                            Pending Reminders
                        </h3>
                        <p>
                            {summary.pendingReminders}
                        </p>
                    </article>

                    <article>
                        <h3>
                            Unread Notifications
                        </h3>
                        <p>
                            {summary.unreadNotifications}
                        </p>
                    </article>

                    <article>
                        <h3>
                            Upcoming Interviews
                        </h3>
                        <p>
                            {summary.upcomingInterviews}
                        </p>
                    </article>
                </div>
            </section>

            <hr />

            <section>
                <h2>
                    Last {analytics.days} Days
                </h2>

                <p>
                    Applications:
                    {" "}
                    {analytics.applicationsInPeriod}
                </p>

                <p>
                    Interviews:
                    {" "}
                    {analytics.interviewsInPeriod}
                </p>

                <h3>Application Trend</h3>

                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Applications</th>
                        </tr>
                    </thead>

                    <tbody>
                        {analytics.applicationTrend.map(
                            (item) => (
                                <tr key={item.date}>
                                    <td>{item.date}</td>
                                    <td>{item.count}</td>
                                </tr>
                            ),
                        )}
                    </tbody>
                </table>

                <h3>Interview Trend</h3>

                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Interviews</th>
                        </tr>
                    </thead>

                    <tbody>
                        {analytics.interviewTrend.map(
                            (item) => (
                                <tr key={item.date}>
                                    <td>{item.date}</td>
                                    <td>{item.count}</td>
                                </tr>
                            ),
                        )}
                    </tbody>
                </table>
            </section>

            <hr />

            <section>
                <h2>Conversion Funnel</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Stage</th>
                            <th>Count</th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr>
                            <td>Applied</td>
                            <td>{funnel.applied}</td>
                        </tr>

                        <tr>
                            <td>Interviewed</td>
                            <td>{funnel.interviewed}</td>
                        </tr>

                        <tr>
                            <td>Offered</td>
                            <td>{funnel.offered}</td>
                        </tr>

                        <tr>
                            <td>Accepted</td>
                            <td>{funnel.accepted}</td>
                        </tr>
                    </tbody>
                </table>

                <p>
                    Application → Interview:
                    {" "}
                    {funnel.interviewRate}%
                </p>

                <p>
                    Application → Offer:
                    {" "}
                    {funnel.offerRate}%
                </p>

                <p>
                    Application → Accepted:
                    {" "}
                    {funnel.acceptedRate}%
                </p>

                <p>
                    Interview → Offer:
                    {" "}
                    {funnel.interviewToOfferRate}%
                </p>

                <p>
                    Offer → Accepted:
                    {" "}
                    {funnel.offerToAcceptedRate}%
                </p>
            </section>

            <hr />

            <section>
                <h2>Next Interviews</h2>

                {summary.nextInterviews.length === 0 ? (
                    <p>
                        No upcoming interviews.
                    </p>
                ) : (
                    <ul>
                        {summary.nextInterviews.map(
                            (interview) => (
                                <li
                                    key={
                                        interview.interviewId
                                    }
                                >
                                    {
                                        interview.companyName
                                    }
                                    {" — "}
                                    {interview.jobTitle}
                                    {" — "}
                                    {interview.scheduledAt}
                                </li>
                            ),
                        )}
                    </ul>
                )}
            </section>

            <section>
                <h2>Next Reminders</h2>

                {summary.nextReminders.length === 0 ? (
                    <p>
                        No upcoming reminders.
                    </p>
                ) : (
                    <ul>
                        {summary.nextReminders.map(
                            (reminder) => (
                                <li
                                    key={
                                        reminder.reminderId
                                    }
                                >
                                    {
                                        reminder.companyName
                                    }
                                    {" — "}
                                    {reminder.type}
                                    {" — "}
                                    {reminder.scheduledAt}
                                </li>
                            ),
                        )}
                    </ul>
                )}
            </section>
        </main>
    );
}