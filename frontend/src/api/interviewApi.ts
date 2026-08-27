import apiClient from "./apiClient";

import type {
    CreateInterviewRequest,
    Interview,
} from "../types/application";

export async function createInterview(
    applicationId: string,
    request: CreateInterviewRequest,
): Promise<Interview> {
    const response =
        await apiClient.post<Interview>(
            `/api/applications/${applicationId}/interviews`,
            request,
        );

    return response.data;
}