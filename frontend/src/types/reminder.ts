export type ReminderChannel =
    | "IN_APP"
    | "EMAIL";

export type ReminderStatus =
    | "PENDING"
    | "PROCESSING"
    | "COMPLETED"
    | "CANCELLED"
    | "FAILED";

export interface CreateReminderRequest {
    applicationId: string;
    type: string;
    scheduledAt: string;
    channels: ReminderChannel[];
    message?: string | null;
}

export interface Reminder {
    id: string;
    applicationId: string;
    type: string;
    scheduledAt: string;
    channels: ReminderChannel[];
    status: ReminderStatus;
    message: string | null;
    attempts: number;
    createdAt: string;
    updatedAt: string;
    processingStartedAt: string | null;
}

export interface ReminderPage {
    content: Reminder[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
}