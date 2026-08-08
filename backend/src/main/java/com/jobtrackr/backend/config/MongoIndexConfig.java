package com.jobtrackr.backend.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.stereotype.Component;

import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.reminder.model.Reminder;

@Component
public class MongoIndexConfig {

        private static final String USER_ID = "userId";
        private static final String UPDATED_AT = "updatedAt";
        private static final String STATUS = "status";
        private static final String PRIORITY = "priority";
        private static final String APPLIED_DATE = "appliedDate";
        private static final String DEADLINE = "deadline";

        private final MongoTemplate mongoTemplate;
        private static final String SCHEDULED_AT = "scheduledAt";

        public MongoIndexConfig(
                        MongoTemplate mongoTemplate) {

                this.mongoTemplate = mongoTemplate;
        }

        @EventListener(ContextRefreshedEvent.class)
        public void createJobApplicationIndexes() {

                IndexOperations indexOperations = mongoTemplate.indexOps(
                                JobApplication.class);

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(UPDATED_AT, Direction.DESC)
                                                .named(
                                                                "idx_job_application_user_updated_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(STATUS, Direction.ASC)
                                                .on(UPDATED_AT, Direction.DESC)
                                                .named(
                                                                "idx_job_application_user_status_updated_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(PRIORITY, Direction.ASC)
                                                .on(UPDATED_AT, Direction.DESC)
                                                .named(
                                                                "idx_job_application_user_priority_updated_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(APPLIED_DATE, Direction.ASC)
                                                .named(
                                                                "idx_job_application_user_applied_date"));

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(DEADLINE, Direction.ASC)
                                                .named(
                                                                "idx_job_application_user_deadline"));

                createReminderIndexes();
        }

        private void createReminderIndexes() {

                IndexOperations indexOperations = mongoTemplate.indexOps(
                                Reminder.class);

                indexOperations.createIndex(
                                new Index()
                                                .on(USER_ID, Direction.ASC)
                                                .on(SCHEDULED_AT, Direction.ASC)
                                                .named(
                                                                "idx_reminder_user_scheduled_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on(STATUS, Direction.ASC)
                                                .on(SCHEDULED_AT, Direction.ASC)
                                                .named(
                                                                "idx_reminder_status_scheduled_at"));
        }
}