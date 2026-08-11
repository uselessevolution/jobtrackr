package com.jobtrackr.backend.notification.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.jobtrackr.backend.notification.model.Notification;
import com.jobtrackr.backend.notification.model.NotificationType;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.query.Update;

@Repository
public class NotificationRepositoryImpl
                implements NotificationRepositoryCustom {

        private final MongoTemplate mongoTemplate;

        public NotificationRepositoryImpl(
                        MongoTemplate mongoTemplate) {

                this.mongoTemplate = mongoTemplate;
        }

        @Override
        public Page<Notification> search(
                        String userId,
                        Boolean read,
                        NotificationType type,
                        Pageable pageable) {

                Criteria criteria = buildCriteria(
                                userId,
                                read,
                                type);

                Query countQuery = new Query(criteria);

                long total = mongoTemplate.count(
                                countQuery,
                                Notification.class);

                Query dataQuery = new Query(criteria)
                                .with(pageable);

                List<Notification> notifications = mongoTemplate.find(
                                dataQuery,
                                Notification.class);

                return new PageImpl<>(
                                notifications,
                                pageable,
                                total);
        }

        @Override
        public long markAllAsRead(
                        String userId,
                        LocalDateTime readAt) {

                Query query = new Query(
                                Criteria.where("userId")
                                                .is(userId)
                                                .and("read")
                                                .is(false));

                Update update = new Update()
                                .set("read", true)
                                .set("readAt", readAt);

                return mongoTemplate
                                .updateMulti(
                                                query,
                                                update,
                                                Notification.class)
                                .getModifiedCount();
        }

        private Criteria buildCriteria(
                        String userId,
                        Boolean read,
                        NotificationType type) {

                List<Criteria> conditions = new ArrayList<>();

                conditions.add(
                                Criteria.where("userId")
                                                .is(userId));

                if (read != null) {
                        conditions.add(
                                        Criteria.where("read")
                                                        .is(read));
                }

                if (type != null) {
                        conditions.add(
                                        Criteria.where("type")
                                                        .is(type));
                }

                return new Criteria()
                                .andOperator(
                                                conditions.toArray(
                                                                Criteria[]::new));
        }
}