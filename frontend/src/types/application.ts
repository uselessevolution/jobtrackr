
export type ApplicationStatus =
    | "SAVED"
    | "APPLIED"
    | "OA_RECEIVED"
    | "PHONE_SCREEN"
    | "INTERVIEWING"
    | "OFFER"
    | "ACCEPTED"
    | "REJECTED"
    | "WITHDRAWN";
export interface Interview {
    id: string;
    type: string;
    scheduledAt: string;
    durationMinutes: number | null;
    location: string | null;
    meetingLink: string | null;
    interviewerName: string | null;
    notes: string | null;
}
export interface CreateInterviewRequest {
    type: string;
    scheduledAt: string;
    durationMinutes: number;
    location?: string | null;
    meetingLink?: string | null;
    interviewerName?: string | null;
    notes?: string | null;
}
export type ApplicationPriority =
    | "LOW"
    | "MEDIUM"
    | "HIGH";

export interface JobApplication {
    id: string;
    interviews: Interview[];
    companyName: string;
    jobTitle: string;
    location: string | null;
    jobUrl: string | null;
    status: ApplicationStatus;
    priority: ApplicationPriority;
    skills: string[];
    appliedDate: string | null;
    deadline: string | null;
    createdAt: string;
    updatedAt: string;
}

export interface PagedResponse<T> {
    content: T[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
}
export interface CreateApplicationRequest {
    companyName: string;
    jobTitle: string;
    location?: string | null;
    jobUrl?: string | null;
    status: ApplicationStatus;
    priority: ApplicationPriority;
    skills: string[];
    appliedDate?: string | null;
    deadline?: string | null;
}