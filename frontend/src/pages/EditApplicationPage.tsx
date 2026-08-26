import {
    useEffect,
    useState,
    type FormEvent,
} from "react";
import axios from "axios";
import {
    Link,
    useNavigate,
    useParams,
} from "react-router-dom";

import {
    getApplicationById,
    updateApplication,
} from "../api/applicationApi";

import type {
    ApplicationPriority,
    ApplicationStatus,
    CreateApplicationRequest,
} from "../types/application";

import type {
    ApiErrorResponse,
} from "../types/api";

export default function EditApplicationPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [companyName, setCompanyName] =
        useState("");

    const [jobTitle, setJobTitle] =
        useState("");

    const [location, setLocation] =
        useState("");

    const [jobUrl, setJobUrl] =
        useState("");

    const [status, setStatus] =
        useState<ApplicationStatus>("SAVED");

    const [priority, setPriority] =
        useState<ApplicationPriority>("MEDIUM");

    const [skillsInput, setSkillsInput] =
        useState("");

    const [appliedDate, setAppliedDate] =
        useState("");

    const [deadline, setDeadline] =
        useState("");

    const [isLoading, setIsLoading] =
        useState(true);

    const [isSubmitting, setIsSubmitting] =
        useState(false);

    const [error, setError] =
        useState("");

    useEffect(() => {
        async function loadApplication() {
            if (!id) {
                setError(
                    "Application ID is missing.",
                );
                setIsLoading(false);
                return;
            }

            try {
                const application =
                    await getApplicationById(id);

                setCompanyName(
                    application.companyName,
                );

                setJobTitle(
                    application.jobTitle,
                );

                setLocation(
                    application.location ?? "",
                );

                setJobUrl(
                    application.jobUrl ?? "",
                );

                setStatus(
                    application.status,
                );

                setPriority(
                    application.priority,
                );

                setSkillsInput(
                    application.skills.join(", "),
                );

                setAppliedDate(
                    application.appliedDate ?? "",
                );

                setDeadline(
                    application.deadline ?? "",
                );
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

    async function handleSubmit(
        event: FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        if (!id) {
            return;
        }

        setError("");

        if (!companyName.trim()) {
            setError(
                "Company name is required.",
            );
            return;
        }

        if (!jobTitle.trim()) {
            setError(
                "Job title is required.",
            );
            return;
        }

        const request:
            CreateApplicationRequest = {
            companyName:
                companyName.trim(),

            jobTitle:
                jobTitle.trim(),

            location:
                location.trim() || null,

            jobUrl:
                jobUrl.trim() || null,

            status,

            priority,

            skills:
                skillsInput
                    .split(",")
                    .map(
                        (skill) =>
                            skill.trim(),
                    )
                    .filter(
                        (skill) =>
                            skill.length > 0,
                    ),

            appliedDate:
                appliedDate || null,

            deadline:
                deadline || null,
        };

        setIsSubmitting(true);

        try {
            const updated =
                await updateApplication(
                    id,
                    request,
                );

            navigate(
                `/applications/${updated.id}`,
                {
                    replace: true,
                },
            );
        } catch (caughtError) {
            if (
                axios.isAxiosError<ApiErrorResponse>(
                    caughtError,
                )
            ) {
                setError(
                    caughtError.response?.data?.message
                    ?? "Failed to update application.",
                );
            } else {
                setError(
                    "Failed to update application.",
                );
            }
        } finally {
            setIsSubmitting(false);
        }
    }

    if (isLoading) {
        return (
            <main>
                <h1>Edit Application</h1>
                <p>Loading application...</p>
            </main>
        );
    }

    return (
        <main>
            <h1>Edit Application</h1>

            <p>
                <Link
                    to={
                        id
                            ? `/applications/${id}`
                            : "/applications"
                    }
                >
                    Cancel
                </Link>
            </p>

            {error && (
                <p role="alert">
                    {error}
                </p>
            )}

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="companyName">
                        Company Name
                    </label>

                    <input
                        id="companyName"
                        value={companyName}
                        onChange={(event) =>
                            setCompanyName(
                                event.target.value,
                            )
                        }
                        required
                    />
                </div>

                <div>
                    <label htmlFor="jobTitle">
                        Job Title
                    </label>

                    <input
                        id="jobTitle"
                        value={jobTitle}
                        onChange={(event) =>
                            setJobTitle(
                                event.target.value,
                            )
                        }
                        required
                    />
                </div>

                <div>
                    <label htmlFor="location">
                        Location
                    </label>

                    <input
                        id="location"
                        value={location}
                        onChange={(event) =>
                            setLocation(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="jobUrl">
                        Job URL
                    </label>

                    <input
                        id="jobUrl"
                        type="url"
                        value={jobUrl}
                        onChange={(event) =>
                            setJobUrl(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="status">
                        Status
                    </label>

                    <select
                        id="status"
                        value={status}
                        onChange={(event) =>
                            setStatus(
                                event.target.value as ApplicationStatus,
                            )
                        }
                    >
                        <option value="SAVED">
                            Saved
                        </option>
                        <option value="APPLIED">
                            Applied
                        </option>
                        <option value="OA_RECEIVED">
                            OA Received
                        </option>
                        <option value="PHONE_SCREEN">
                            Phone Screen
                        </option>
                        <option value="INTERVIEWING">
                            Interviewing
                        </option>
                        <option value="OFFER">
                            Offer
                        </option>
                        <option value="ACCEPTED">
                            Accepted
                        </option>
                        <option value="REJECTED">
                            Rejected
                        </option>
                        <option value="WITHDRAWN">
                            Withdrawn
                        </option>
                    </select>
                </div>

                <div>
                    <label htmlFor="priority">
                        Priority
                    </label>

                    <select
                        id="priority"
                        value={priority}
                        onChange={(event) =>
                            setPriority(
                                event.target.value as ApplicationPriority,
                            )
                        }
                    >
                        <option value="LOW">
                            Low
                        </option>
                        <option value="MEDIUM">
                            Medium
                        </option>
                        <option value="HIGH">
                            High
                        </option>
                    </select>
                </div>

                <div>
                    <label htmlFor="skills">
                        Skills
                    </label>

                    <input
                        id="skills"
                        value={skillsInput}
                        onChange={(event) =>
                            setSkillsInput(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="appliedDate">
                        Applied Date
                    </label>

                    <input
                        id="appliedDate"
                        type="date"
                        value={appliedDate}
                        onChange={(event) =>
                            setAppliedDate(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <div>
                    <label htmlFor="deadline">
                        Deadline
                    </label>

                    <input
                        id="deadline"
                        type="date"
                        value={deadline}
                        onChange={(event) =>
                            setDeadline(
                                event.target.value,
                            )
                        }
                    />
                </div>

                <button
                    type="submit"
                    disabled={isSubmitting}
                >
                    {isSubmitting
                        ? "Saving..."
                        : "Save Changes"}
                </button>
            </form>
        </main>
    );
}