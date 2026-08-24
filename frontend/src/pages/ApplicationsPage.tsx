import {
    useEffect,
    useState,
    type FormEvent,
} from "react";
import { Link } from "react-router-dom";
import {
    getApplications,
} from "../api/applicationApi";

import type {
    ApplicationPriority,
    ApplicationStatus,
    JobApplication,
    PagedResponse,
} from "../types/application";

type SortDirection =
    | "asc"
    | "desc";

export default function ApplicationsPage() {
    const [page, setPage] =
        useState(0);

    const [data, setData] =
        useState<
            PagedResponse<JobApplication> | null
        >(null);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState("");

    // Form values
    const [keywordInput, setKeywordInput] =
        useState("");

    const [statusInput, setStatusInput] =
        useState("");

    const [priorityInput, setPriorityInput] =
        useState("");

    // Applied filters
    const [keyword, setKeyword] =
        useState("");

    const [status, setStatus] =
        useState("");

    const [priority, setPriority] =
        useState("");

    const [sortBy, setSortBy] =
        useState("updatedAt");

    const [direction, setDirection] =
        useState<SortDirection>("desc");

    useEffect(() => {
        async function loadApplications() {
            setIsLoading(true);
            setError("");

            try {
                const response =
                    await getApplications({
                        keyword:
                            keyword || undefined,

                        status:
                            status
                                ? status as ApplicationStatus
                                : undefined,

                        priority:
                            priority
                                ? priority as ApplicationPriority
                                : undefined,

                        page,

                        size: 10,

                        sortBy,

                        direction,
                    });

                setData(response);
            } catch {
                setError(
                    "Failed to load job applications.",
                );
            } finally {
                setIsLoading(false);
            }
        }

        void loadApplications();
    }, [
        page,
        keyword,
        status,
        priority,
        sortBy,
        direction,
    ]);

    function handleFilterSubmit(
        event: FormEvent<HTMLFormElement>,
    ) {
        event.preventDefault();

        setKeyword(keywordInput.trim());
        setStatus(statusInput);
        setPriority(priorityInput);

        setPage(0);
    }

    function handleClearFilters() {
        setKeywordInput("");
        setStatusInput("");
        setPriorityInput("");

        setKeyword("");
        setStatus("");
        setPriority("");

        setPage(0);
    }

    return (
        <main>
            <h1>Applications</h1>
            <p>
                <Link to="/applications/new">
                    Create Application
                </Link>
            </p>
            <form
                onSubmit={handleFilterSubmit}
            >
                <div>
                    <label htmlFor="keyword">
                        Search
                    </label>

                    <input
                        id="keyword"
                        type="search"
                        placeholder="Company or job title"
                        value={keywordInput}
                        onChange={(event) =>
                            setKeywordInput(
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
                        value={statusInput}
                        onChange={(event) =>
                            setStatusInput(
                                event.target.value,
                            )
                        }
                    >
                        <option value="">
                            All statuses
                        </option>

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
                        value={priorityInput}
                        onChange={(event) =>
                            setPriorityInput(
                                event.target.value,
                            )
                        }
                    >
                        <option value="">
                            All priorities
                        </option>

                        <option value="HIGH">
                            High
                        </option>

                        <option value="MEDIUM">
                            Medium
                        </option>

                        <option value="LOW">
                            Low
                        </option>
                    </select>
                </div>

                <button type="submit">
                    Apply Filters
                </button>

                <button
                    type="button"
                    onClick={handleClearFilters}
                >
                    Clear
                </button>
            </form>

            <hr />

            <div>
                <label htmlFor="sortBy">
                    Sort by
                </label>

                <select
                    id="sortBy"
                    value={sortBy}
                    onChange={(event) => {
                        setSortBy(
                            event.target.value,
                        );

                        setPage(0);
                    }}
                >
                    <option value="updatedAt">
                        Last Updated
                    </option>

                    <option value="createdAt">
                        Created Date
                    </option>

                    <option value="appliedDate">
                        Applied Date
                    </option>

                    <option value="deadline">
                        Deadline
                    </option>

                    <option value="companyName">
                        Company
                    </option>
                </select>

                <select
                    aria-label="Sort direction"
                    value={direction}
                    onChange={(event) => {
                        setDirection(event.target.value as SortDirection);
                        setPage(0);
                    }}
                >
                    <option value="desc">
                        Descending
                    </option>

                    <option value="asc">
                        Ascending
                    </option>
                </select>
            </div>

            <hr />

            {isLoading && (
                <p>
                    Loading applications...
                </p>
            )}

            {!isLoading && error && (
                <p role="alert">
                    {error}
                </p>
            )}

            {!isLoading
                && !error
                && data
                && data.content.length === 0
                && (
                    <p>
                        No applications match
                        your current filters.
                    </p>
                )}

            {!isLoading
                && !error
                && data
                && data.content.length > 0
                && (
                    <>
                        <p>
                            {data.totalElements}
                            {" "}
                            application
                            {data.totalElements === 1
                                ? ""
                                : "s"}
                            {" "}
                            found
                        </p>

                        <table>
                            <thead>
                                <tr>
                                    <th>Company</th>
                                    <th>Job Title</th>
                                    <th>Status</th>
                                    <th>Priority</th>
                                    <th>
                                        Applied Date
                                    </th>
                                    <th>Deadline</th>
                                </tr>
                            </thead>

                            <tbody>
                                {data.content.map(
                                    (application) => (
                                        <tr
                                            key={
                                                application.id
                                            }
                                        >
                                            <td>
                                                {
                                                    application
                                                        .companyName
                                                }
                                            </td>

                                            <td>
                                                {
                                                    application
                                                        .jobTitle
                                                }
                                            </td>

                                            <td>
                                                {
                                                    application
                                                        .status
                                                }
                                            </td>

                                            <td>
                                                {
                                                    application
                                                        .priority
                                                }
                                            </td>

                                            <td>
                                                {
                                                    application
                                                        .appliedDate
                                                    ?? "—"
                                                }
                                            </td>

                                            <td>
                                                {
                                                    application
                                                        .deadline
                                                    ?? "—"
                                                }
                                            </td>
                                        </tr>
                                    ),
                                )}
                            </tbody>
                        </table>

                        <div>
                            <button
                                type="button"
                                disabled={data.first}
                                onClick={() =>
                                    setPage(
                                        (current) =>
                                            Math.max(
                                                current - 1,
                                                0,
                                            ),
                                    )
                                }
                            >
                                Previous
                            </button>

                            <span>
                                {" "}
                                Page {data.page + 1}
                                {" "}
                                of {data.totalPages}
                                {" "}
                            </span>

                            <button
                                type="button"
                                disabled={data.last}
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
                    </>
                )}
        </main>
    );
}