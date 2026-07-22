package com.jobtrackr.backend.application.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.application.model.JobApplication;

public interface JobApplicationRepository
        extends MongoRepository<JobApplication, String> {
}