package com.jobtrackr.backend.dashboard.service;

import java.time.LocalDateTime;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
}