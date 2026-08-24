import apiClient from "./apiClient";
import type {
    ApplicationPriority,
    ApplicationStatus,
    CreateApplicationRequest,
    JobApplication,
    PagedResponse,
} from "../types/application";

export interface GetApplicationsParams {
    keyword?: string;
    status?: ApplicationStatus;
    priority?: ApplicationPriority;

    page?: number;
    size?: number;

    sortBy?: string;
    direction?: "asc" | "desc";
}

export async function getApplications(
    params: GetApplicationsParams = {},
): Promise<PagedResponse<JobApplication>> {
    const response =
        await apiClient.get<
            PagedResponse<JobApplication>
        >("/api/applications", {
            params: {
                keyword:
                    params.keyword || undefined,

                status:
                    params.status || undefined,

                priority:
                    params.priority || undefined,

                page: params.page ?? 0,

                size: params.size ?? 10,

                sortBy:
                    params.sortBy ?? "updatedAt",

                direction:
                    params.direction ?? "desc",
            },
        });

    return response.data;

}
export async function createApplication(
    request: CreateApplicationRequest,
): Promise<JobApplication> {
    const response =
        await apiClient.post<JobApplication>(
            "/api/applications",
            request,
        );

    return response.data;
}