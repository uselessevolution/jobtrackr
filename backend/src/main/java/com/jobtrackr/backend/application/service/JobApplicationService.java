package com.jobtrackr.backend.application.service;

import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(
            JobApplicationRepository repository) {
        this.repository = repository;
    }

    public JobApplication create(
            JobApplication application) {

        LocalDateTime now = LocalDateTime.now();

        application.setId(null);
        application.setCreatedAt(now);
        application.setUpdatedAt(now);

        if (application.getStatus() == null) {
            application.setStatus(ApplicationStatus.SAVED);
        }

        if (application.getPriority() == null) {
            application.setPriority(ApplicationPriority.MEDIUM);
        }

        return repository.save(application);
    }

    public List<JobApplication> findAll() {
        return repository.findAll();
    }
}