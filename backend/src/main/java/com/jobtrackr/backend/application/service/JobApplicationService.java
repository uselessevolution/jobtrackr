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
import com.jobtrackr.backend.application.dto.UpdateJobApplicationRequest;
import com.jobtrackr.backend.common.exception.ResourceNotFoundException;

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
    public JobApplicationResponse findById(String id) {

        JobApplication application = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job application not found with id: " + id
                        )
                );

        return mapper.toResponse(application);
    }
    public JobApplicationResponse update(
        String id,
        UpdateJobApplicationRequest request) {

        JobApplication existingApplication =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job application not found with id: " + id
                                )
                        );

        mapper.updateDocument(request, existingApplication);

        if (existingApplication.getStatus() == null) {
            existingApplication.setStatus(ApplicationStatus.SAVED);
        }

        if (existingApplication.getPriority() == null) {
            existingApplication.setPriority(ApplicationPriority.MEDIUM);
        }

        existingApplication.setUpdatedAt(LocalDateTime.now());

        JobApplication savedApplication =
                repository.save(existingApplication);

        return mapper.toResponse(savedApplication);
    }
    public void delete(String id) {
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Job application not found with id: " + id
            );
        }
    
        repository.deleteById(id);
    }
}