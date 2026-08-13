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
import com.jobtrackr.backend.notification.model.Notification;
import com.jobtrackr.backend.email.model.EmailDelivery;

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
                createNotificationIndexes();
                createEmailDeliveryIndexes();
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
                indexOperations.createIndex(
                                new Index()
                                                .on("userId", Direction.ASC)
                                                .on("status", Direction.ASC)
                                                .on("scheduledAt", Direction.ASC)
                                                .named(
                                                                "idx_reminder_user_status_scheduled_at"));
        }

        private void createNotificationIndexes() {

                IndexOperations indexOperations = mongoTemplate.indexOps(
                                Notification.class);

                indexOperations.createIndex(
                                new Index()
                                                .on("userId", Direction.ASC)
                                                .on("createdAt", Direction.DESC)
                                                .named(
                                                                "idx_notification_user_created_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on("userId", Direction.ASC)
                                                .on("read", Direction.ASC)
                                                .on("createdAt", Direction.DESC)
                                                .named(
                                                                "idx_notification_user_read_created_at"));

                indexOperations.createIndex(
                                new Index()
                                                .on("reminderId", Direction.ASC)
                                                .unique()
                                                .named(
                                                                "idx_notification_reminder_unique"));
                indexOperations.createIndex(
                                new Index()
                                                .on("userId", Direction.ASC)
                                                .on("type", Direction.ASC)
                                                .on("createdAt", Direction.DESC)
                                                .named(
                                                                "idx_notification_user_type_created_at"));
        }

        private void createEmailDeliveryIndexes() {

                IndexOperations indexOperations = mongoTemplate.indexOps(
                                EmailDelivery.class);

                indexOperations.createIndex(
                                new Index()
                                                .on("reminderId", Direction.ASC)
                                                .on("status", Direction.ASC)
                                                .named(
                                                                "idx_email_delivery_reminder_status"));

                indexOperations.createIndex(
                                new Index()
                                                .on("userId", Direction.ASC)
                                                .on("createdAt", Direction.DESC)
                                                .named(
                                                                "idx_email_delivery_user_created_at"));
                indexOperations.createIndex(
                                new Index()
                                                .on("reminderId", Direction.ASC)
                                                .on("attemptNumber", Direction.ASC)
                                                .named(
                                                                "idx_email_delivery_reminder_attempt"));
        }

}