package com.jobtrackr.backend.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.JobApplication;

public interface JobApplicationRepositoryCustom {

    Page<JobApplication> search(
            String userId,
            String keyword,
            ApplicationStatus status,
            ApplicationPriority priority,
            Pageable pageable);
}