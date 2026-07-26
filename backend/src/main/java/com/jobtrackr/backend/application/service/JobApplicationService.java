package com.jobtrackr.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.dto.CreateJobApplicationRequest;
import com.jobtrackr.backend.application.dto.JobApplicationResponse;
import com.jobtrackr.backend.application.mapper.JobApplicationMapper;
import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final JobApplicationMapper mapper;

    public JobApplicationService(
            JobApplicationRepository repository,
            JobApplicationMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    public JobApplicationResponse create(
            CreateJobApplicationRequest request) {

        JobApplication application =
                mapper.toDocument(request);

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

        JobApplication savedApplication =
                repository.save(application);

        return mapper.toResponse(savedApplication);
    }

    public List<JobApplicationResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}