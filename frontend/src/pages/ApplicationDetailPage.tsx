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