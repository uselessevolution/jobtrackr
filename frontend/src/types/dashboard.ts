export interface UpcomingInterview {
    applicationId: string;
    interviewId: string;
    companyName: string;
    jobTitle: string;
    scheduledAt: string;
    type: string;
}

export interface UpcomingReminder {
    reminderId: string;
    applicationId: string;
    companyName: string;
    jobTitle: string;
    type: string;
    scheduledAt: string;
}

export interface DashboardSummary {
    totalApplications: number;
    applicationStatusCounts: Record<string, number>;
    pendingReminders: number;
    unreadNotifications: number;
    upcomingInterviews: number;
    nextInterviews: UpcomingInterview[];
    nextReminders: UpcomingReminder[];
}

export interface DailyCount {
    date: string;
    count: number;
}

export interface DashboardAnalytics {
    days: number;
    startDate: string;
    endDate: string;
    applicationsInPeriod: number;
    interviewsInPeriod: number;
    applicationTrend: DailyCount[];
    interviewTrend: DailyCount[];
}

export interface DashboardFunnel {
    applied: number;
    interviewed: number;
    offered: number;
    accepted: number;
    interviewRate: number;
    offerRate: number;
    acceptedRate: number;
    interviewToOfferRate: number;
    offerToAcceptedRate: number;
}