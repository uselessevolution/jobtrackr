package com.jobtrackr.backend.dashboard.service;

import java.time.LocalDateTime;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.InterviewType;
import com.jobtrackr.backend.dashboard.dto.UpcomingInterviewResponse;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DashboardQueryService {

    private final MongoTemplate mongoTemplate;

    public DashboardQueryService(
            MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    public long countUpcomingInterviews(
            String userId,
            LocalDateTime now) {

        Document matchUser = new Document(
                "$match",
                new Document(
                        "userId",
                        userId));

        Document unwindInterviews = new Document(
                "$unwind",
                "$interviews");

        Document interviewConditions = new Document()
                .append(
                        "interviews.status",
                        "SCHEDULED")
                .append(
                        "interviews.scheduledAt",
                        new Document(
                                "$gte",
                                now));

        Document matchUpcoming = new Document(
                "$match",
                interviewConditions);

        Document count = new Document(
                "$count",
                "total");

        Document result = mongoTemplate
                .getCollection(
                        "job_applications")
                .aggregate(
                        java.util.List.of(
                                matchUser,
                                unwindInterviews,
                                matchUpcoming,
                                count))
                .first();

        if (result == null) {
            return 0;
        }

        Number total = result.get(
                "total",
                Number.class);

        return total == null
                ? 0
                : total.longValue();
    }

    public Map<ApplicationStatus, Long> getApplicationStatusCounts(
            String userId) {

        List<Document> pipeline = List.of(
                new Document(
                        "$match",
                        new Document(
                                "userId",
                                userId)),

                new Document(
                        "$group",
                        new Document(
                                "_id",
                                "$status")
                                .append(
                                        "count",
                                        new Document(
                                                "$sum",
                                                1))));

        Map<ApplicationStatus, Long> counts = new EnumMap<>(
                ApplicationStatus.class);

        for (ApplicationStatus status : ApplicationStatus.values()) {

            counts.put(status, 0L);
        }

        for (Document result : mongoTemplate
                .getCollection(
                        "job_applications")
                .aggregate(pipeline)) {

            String statusValue = result.getString("_id");

            Number count = result.get(
                    "count",
                    Number.class);

            if (statusValue == null
                    || count == null) {
                continue;
            }

            ApplicationStatus status = ApplicationStatus.valueOf(
                    statusValue);

            counts.put(
                    status,
                    count.longValue());
        }

        return counts;
    }

    public List<UpcomingInterviewResponse> findUpcomingInterviews(
            String userId,
            LocalDateTime now,
            int limit) {

        if (limit < 1 || limit > 20) {
            throw new IllegalArgumentException(
                    "Upcoming interview limit must be between 1 and 20");
        }

        List<Document> pipeline = List.of(

                new Document(
                        "$match",
                        new Document(
                                "userId",
                                userId)),

                new Document(
                        "$unwind",
                        "$interviews"),

                new Document(
                        "$match",
                        new Document(
                                "interviews.status",
                                "SCHEDULED")
                                .append(
                                        "interviews.scheduledAt",
                                        new Document(
                                                "$gte",
                                                now))),

                new Document(
                        "$sort",
                        new Document(
                                "interviews.scheduledAt",
                                1)),

                new Document(
                        "$limit",
                        limit),

                new Document(
                        "$project",
                        new Document(
                                "_id",
                                0)
                                .append(
                                        "applicationId",
                                        new Document(
                                                "$toString",
                                                "$_id"))
                                .append(
                                        "companyName",
                                        1)
                                .append(
                                        "jobTitle",
                                        1)
                                .append(
                                        "interviewId",
                                        "$interviews.id")
                                .append(
                                        "type",
                                        "$interviews.type")
                                .append(
                                        "scheduledAt",
                                        "$interviews.scheduledAt")
                                .append(
                                        "durationMinutes",
                                        "$interviews.durationMinutes")
                                .append(
                                        "location",
                                        "$interviews.location")
                                .append(
                                        "meetingLink",
                                        "$interviews.meetingLink")));

        List<UpcomingInterviewResponse> responses = new ArrayList<>();

        for (Document document : mongoTemplate
                .getCollection(
                        "job_applications")
                .aggregate(pipeline)) {

            UpcomingInterviewResponse response = new UpcomingInterviewResponse();

            response.setApplicationId(
                    document.getString(
                            "applicationId"));

            response.setCompanyName(
                    document.getString(
                            "companyName"));

            response.setJobTitle(
                    document.getString(
                            "jobTitle"));

            response.setInterviewId(
                    document.getString(
                            "interviewId"));

            String type = document.getString(
                    "type");

            if (type != null) {
                response.setType(
                        InterviewType.valueOf(
                                type));
            }

            Date scheduledAtDate = document.getDate(
                    "scheduledAt");

            LocalDateTime scheduledAt = scheduledAtDate == null
                    ? null
                    : scheduledAtDate
                            .toInstant()
                            .atZone(
                                    ZoneId.systemDefault())
                            .toLocalDateTime();

            response.setScheduledAt(
                    scheduledAt);

            response.setDurationMinutes(
                    document.getInteger(
                            "durationMinutes"));

            response.setLocation(
                    document.getString(
                            "location"));

            response.setMeetingLink(
                    document.getString(
                            "meetingLink"));

            responses.add(response);
        }

        return responses;
    }
}