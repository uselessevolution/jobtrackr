package com.jobtrackr.backend.application.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.application.model.JobApplication;

public interface JobApplicationRepository
        extends MongoRepository<JobApplication, String>,
        JobApplicationRepositoryCustom {

    Page<JobApplication> findAllByUserId(
            String userId,
            Pageable pageable);

    Optional<JobApplication> findByIdAndUserId(
            String id,
            String userId);

    boolean existsByIdAndUserId(
            String id,
            String userId);
}