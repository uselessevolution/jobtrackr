import {
    useEffect,
    useState,
} from "react";
import axios from "axios";
import {
    Link,
    useNavigate,
    useParams,
} from "react-router-dom";

import {
    deleteApplication,
    getApplicationById,
} from "../api/applicationApi";
import {
    createInterview,
} from "../api/interviewApi";

import type {
    JobApplication,
} from "../types/application";

import type {
    ApiErrorResponse,
} from "../types/api";

export default function ApplicationDetailPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [application, setApplication] =
        useState<JobApplication | null>(null);

    const [isLoading, setIsLoading] =
        useState(true);

    const [isDeleting, setIsDeleting] =
        useState(false);

    const [error, setError] =
        useState("");
    const [interviewType, setInterviewType] =
        useState("HR");

    const [interviewScheduledAt, setInterviewScheduledAt] =
        useState("");

    const [interviewDuration, setInterviewDuration] =
        useState("60");

    const [interviewLocation, setInterviewLocation] =
        useState("");

    const [interviewMeetingLink, setInterviewMeetingLink] =
        useState("");

    const [interviewerName, setInterviewerName] =
        useState("");

    const [interviewNotes, setInterviewNotes] =
        useState("");

    const [isAddingInterview, setIsAddingInterview] =
        useState(false);
    useEffect(() => {
        async function loadApplication() {
            if (!id) {
                setError("Application ID is missing.");
                setIsLoading(false);
                return;
            }

            try {
                const response =
                    await getApplicationById(id);

                setApplication(response);
            } catch (caughtError) {
                if (
                    axios.isAxiosError<ApiErrorResponse>(
                        caughtError,
                    )
                ) {
                    setError(
                        caughtError.response?.data?.message
                        ?? "Failed to load application.",
                    );
                } else {
                    setError(
                        "Failed to load application.",
                    );
                }
            } finally {
                setIsLoading(false);
            }
        }

        void loadApplication();
    }, [id]);
    async function handleAddInterview() {
        if (!id || !application) {
            return;
        }

        if (!interviewScheduledAt) {
            setError(
                "Interview date and time are required.",
            );
            return;
        }

        setIsAddingInterview(true);
        setError("");

        try {
            const createdInterview =
                await createInterview(
                    id,
                    {
                        type: interviewType,

                        scheduledAt:
                            new Date(
                                interviewScheduledAt,
                            ).toISOString(),

                        durationMinutes:
                            Number(interviewDuration),

                        location:
                            interviewLocation.trim()
                            || null,

                        meetingLink:
                            interviewMeetingLink.trim()
                            || null,

                        interviewerName:
                            interviewerName.trim()
                            || null,

                        notes:
                            interviewNotes.trim()
                            || null,
                    },
                );

            setApplication({
                ...application,

                interviews: [
                    ...application.interviews,
                    createdInterview,
                ],
            });

            setInterviewScheduledAt("");
            setInterviewDuration("60");
            setInterviewLocation("");
            setInterviewMeetingLink("");
            setInterviewerName("");
            setInterviewNotes("");
        } catch (caughtError) {
            if (
                axios.isAxiosError<ApiErrorResponse>(
                    caughtError,
                )
            ) {
                setError(
                    caughtError.response?.data?.message
                    ?? "Failed to add interview.",
                );
            } else {
                setError(
                    "Failed to add interview.",
                );
            }
        } finally {
            setIsAddingInterview(false);
        }
    }
    async function handleDelete() {
        if (!id) {
            return;
        }

        const confirmed =
            window.confirm(
                "Delete this application?",
            );

        if (!confirmed) {
            return;
        }

        setIsDeleting(true);
        setError("");

        try {
            await deleteApplication(id);

            navigate("/applications", {
                replace: true,
            });
        } catch (caughtError) {
            if (
                axios.isAxiosError<ApiErrorResponse>(
                    caughtError,
                )
            ) {
                setError(
                    caughtError.response?.data?.message
                    ?? "Failed to delete application.",
                );
            } else {
                setError(
                    "Failed to delete application.",
                );
            }
        } finally {
            setIsDeleting(false);
        }
    }

    if (isLoading) {
        return (
            <main>
                <h1>Application</h1>
                <p>Loading application...</p>
            </main>
        );
    }

    if (error && !application) {
        return (
            <main>
                <h1>Application</h1>

                <p role="alert">
                    {error}
                </p>

                <Link to="/applications">
                    Back to Applications
                </Link>
            </main>
        );
    }

    if (!application) {
        return null;
    }

    return (
        <main>
            <p>
                <Link to="/applications">
                    Back to Applications
                </Link>
            </p>

            <h1>
                {application.jobTitle}
            </h1>

            <h2>
                {application.companyName}
            </h2>

            {error && (
                <p role="alert">
                    {error}
                </p>
            )}

            <dl>
                <dt>Status</dt>
                <dd>
                    {application.status}
                </dd>

                <dt>Priority</dt>
                <dd>
                    {application.priority}
                </dd>

                <dt>Location</dt>
                <dd>
                    {application.location ?? "—"}
                </dd>

                <dt>Applied Date</dt>
                <dd>
                    {application.appliedDate ?? "—"}
                </dd>

                <dt>Deadline</dt>
                <dd>
                    {application.deadline ?? "—"}
                </dd>

                <dt>Skills</dt>
                <dd>
                    {application.skills.length > 0
                        ? application.skills.join(", ")
                        : "—"}
                </dd>
            </dl>

            {application.jobUrl && (
                <p>
                    <a
                        href={application.jobUrl}
                        target="_blank"
                        rel="noreferrer"
                    >
                        View Job Posting
                    </a>
                </p>
            )}

            <section>
                <h2>Interviews</h2>

                {application.interviews.length === 0 ? (
                    <p>
                        No interviews have been added yet.
                    </p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>Type</th>
                                <th>Scheduled</th>
                                <th>Duration</th>
                                <th>Location</th>
                                <th>Interviewer</th>
                            </tr>
                        </thead>

                        <tbody>
                            {application.interviews.map((interview) => (
                                <tr key={interview.id}>
                                    <td>
                                        {interview.type}
                                    </td>

                                    <td>
                                        {interview.scheduledAt}
                                    </td>

                                    <td>
                                        {interview.durationMinutes
                                            ? `${interview.durationMinutes} min`
                                            : "—"}
                                    </td>

                                    <td>
                                        {interview.location ?? "—"}
                                    </td>

                                    <td>
                                        {interview.interviewerName ?? "—"}
                                    </td>
                                </tr>
                            ),
                            )}
                        </tbody>
                    </table>
                )}
                <h3>Add Interview</h3>

                <div>
                    <label htmlFor="interviewType">
                        Type
                    </label>

                    <select
                        id="interviewType"
                        value={interviewType}
                        onChange={(event) =>
                            setInterviewType(
                                event.target.value,
                            )
                        }
                    >
                        <option value="HR">
                            HR
                        </option>
                    </select>
                </div>

                <div>
                    <label htmlFor="interviewScheduledAt">
                        Scheduled At
                    </label>

                    <input
                        id="interviewScheduledAt"
                        type="datetime-local"
                        value={interviewScheduledAt}
                        onChange={(event) =>
                            setInterviewScheduledAt(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="interviewDuration">
                        Duration (minutes)
                    </label>

                    <input
                        id="interviewDuration"
                        type="number"
                        min="1"
                        value={interviewDuration}
                        onChange={(event) =>
                            setInterviewDuration(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="interviewLocation">
                        Location
                    </label>

                    <input
                        id="interviewLocation"
                        value={interviewLocation}
                        onChange={(event) =>
                            setInterviewLocation(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="interviewMeetingLink">
                        Meeting Link
                    </label>

                    <input
                        id="interviewMeetingLink"
                        type="url"
                        value={interviewMeetingLink}
                        onChange={(event) =>
                            setInterviewMeetingLink(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="interviewerName">
                        Interviewer
                    </label>

                    <input
                        id="interviewerName"
                        value={interviewerName}
                        onChange={(event) =>
                            setInterviewerName(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="interviewNotes">
                        Notes
                    </label>

                    <textarea
                        id="interviewNotes"
                        value={interviewNotes}
                        onChange={(event) =>
                            setInterviewNotes(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <button
                    type="button"
                    onClick={handleAddInterview}
                    disabled={isAddingInterview}
                >
                    {isAddingInterview
                        ? "Adding..."
                        : "Add Interview"}
                </button>
            </section>


            <p>
                <Link
                    to={`/applications/${application.id}/edit`}
                >
                    Edit Application
                </Link>
            </p>


            <button
                type="button"
                onClick={handleDelete}
                disabled={isDeleting}
            >
                {isDeleting
                    ? "Deleting..."
                    : "Delete Application"}
            </button>
        </main>
    );
}