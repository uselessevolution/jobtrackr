package com.jobtrackr.backend.application.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.JobApplication;
import java.time.LocalDate;

@Repository
public class JobApplicationRepositoryImpl
                implements JobApplicationRepositoryCustom {

        private final MongoTemplate mongoTemplate;

        public JobApplicationRepositoryImpl(
                        MongoTemplate mongoTemplate) {

                this.mongoTemplate = mongoTemplate;
        }

        @Override
        public Page<JobApplication> search(
                        String userId,
                        String keyword,
                        ApplicationStatus status,
                        ApplicationPriority priority,
                        String skill,
                        LocalDate appliedFrom,
                        LocalDate appliedTo,
                        LocalDate deadlineFrom,
                        LocalDate deadlineTo,
                        Pageable pageable) {

                Criteria criteria = buildCriteria(
                                userId,
                                keyword,
                                status,
                                priority,
                                skill,
                                appliedFrom,
                                appliedTo,
                                deadlineFrom,
                                deadlineTo);

                Query countQuery = new Query(criteria);

                long total = mongoTemplate.count(
                                countQuery,
                                JobApplication.class);

                Query dataQuery = new Query(criteria)
                                .with(pageable);

                List<JobApplication> applications = mongoTemplate.find(
                                dataQuery,
                                JobApplication.class);

                return new PageImpl<>(
                                applications,
                                pageable,
                                total);
        }

        private Criteria buildCriteria(
                        String userId,
                        String keyword,
                        ApplicationStatus status,
                        ApplicationPriority priority,
                        String skill,
                        LocalDate appliedFrom,
                        LocalDate appliedTo,
                        LocalDate deadlineFrom,
                        LocalDate deadlineTo) {

                List<Criteria> conditions = new ArrayList<>();

                conditions.add(
                                Criteria.where("userId").is(userId));

                if (StringUtils.hasText(keyword)) {

                        String safeKeyword = Pattern.quote(keyword.trim());

                        Criteria keywordCriteria = new Criteria().orOperator(
                                        Criteria.where("companyName")
                                                        .regex(safeKeyword, "i"),
                                        Criteria.where("jobTitle")
                                                        .regex(safeKeyword, "i"),
                                        Criteria.where("location")
                                                        .regex(safeKeyword, "i"),
                                        Criteria.where("skills")
                                                        .regex(safeKeyword, "i"));

                        conditions.add(keywordCriteria);
                }

                if (status != null) {
                        conditions.add(
                                        Criteria.where("status").is(status));
                }

                if (priority != null) {
                        conditions.add(
                                        Criteria.where("priority").is(priority));
                }

                addExactSkillCriteria(
                                conditions,
                                skill);

                addDateRangeCriteria(
                                conditions,
                                "appliedDate",
                                appliedFrom,
                                appliedTo);

                addDateRangeCriteria(
                                conditions,
                                "deadline",
                                deadlineFrom,
                                deadlineTo);

                return new Criteria().andOperator(
                                conditions.toArray(Criteria[]::new));
        }

        private void addExactSkillCriteria(
                        List<Criteria> conditions,
                        String skill) {

                if (!StringUtils.hasText(skill)) {
                        return;
                }

                Pattern exactSkillPattern = Pattern.compile(
                                "^" + Pattern.quote(skill.trim()) + "$",
                                Pattern.CASE_INSENSITIVE);

                conditions.add(
                                Criteria.where("skills")
                                                .regex(exactSkillPattern));
        }

        private void addDateRangeCriteria(
                        List<Criteria> conditions,
                        String fieldName,
                        LocalDate from,
                        LocalDate to) {

                if (from == null && to == null) {
                        return;
                }

                Criteria dateCriteria = Criteria.where(fieldName);

                if (from != null) {
                        dateCriteria.gte(from);
                }

                if (to != null) {
                        dateCriteria.lte(to);
                }

                conditions.add(dateCriteria);
        }
}