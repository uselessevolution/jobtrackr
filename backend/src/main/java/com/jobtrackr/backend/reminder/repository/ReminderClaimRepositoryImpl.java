package com.jobtrackr.backend.reminder.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;

@Repository
public class ReminderClaimRepositoryImpl
        implements ReminderClaimRepository {

    private final MongoTemplate mongoTemplate;

    public ReminderClaimRepositoryImpl(
            MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Reminder> claimNextDueReminder(
            LocalDateTime now) {

        Query query = new Query(
                Criteria.where("status")
                        .is(ReminderStatus.PENDING)
                        .and("scheduledAt")
                        .lte(now)
        );

        query.with(
                Sort.by(
                        Sort.Direction.ASC,
                        "scheduledAt"));

        Update update = new Update()
                .set(
                        "status",
                        ReminderStatus.PROCESSING)
                .set(
                        "processingStartedAt",
                        now)
                .set(
                        "updatedAt",
                        now);

        FindAndModifyOptions options =
                FindAndModifyOptions.options()
                        .returnNew(true);

        Reminder claimedReminder =
                mongoTemplate.findAndModify(
                        query,
                        update,
                        options,
                        Reminder.class);

        return Optional.ofNullable(
                claimedReminder);
    }
}