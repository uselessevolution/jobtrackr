package com.jobtrackr.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.dto.CreateJobApplicationRequest;
import com.jobtrackr.backend.application.dto.JobApplicationResponse;
import com.jobtrackr.backend.application.dto.PagedResponse;
import com.jobtrackr.backend.application.dto.UpdateJobApplicationRequest;
import com.jobtrackr.backend.application.mapper.JobApplicationMapper;
import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;
import com.jobtrackr.backend.common.exception.ResourceNotFoundException;
import com.jobtrackr.backend.user.service.CurrentUserService;

import java.util.ArrayList;
import com.jobtrackr.backend.application.model.StatusHistory;
import com.jobtrackr.backend.common.exception.InvalidStatusTransitionException;

@Service
public class JobApplicationService {

        private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
                        "createdAt",
                        "updatedAt",
                        "companyName",
                        "jobTitle",
                        "appliedDate",
                        "deadline",
                        "priority");

        private final JobApplicationRepository repository;
        private final JobApplicationMapper mapper;
        private final CurrentUserService currentUserService;
        private final JobApplicationStatusService statusService;

        public JobApplicationService(
                        JobApplicationRepository repository,
                        JobApplicationMapper mapper,
                        CurrentUserService currentUserService,
                        JobApplicationStatusService statusService) {

                this.repository = repository;
                this.mapper = mapper;
                this.currentUserService = currentUserService;
                this.statusService = statusService;
        }

        public JobApplicationResponse create(
                        CreateJobApplicationRequest request) {

                String currentUserId = currentUserService.getCurrentUserId();

                JobApplication application = mapper.toDocument(request);

                LocalDateTime now = LocalDateTime.now();

                application.setId(null);
                application.setUserId(currentUserId);
                application.setCreatedAt(now);
                application.setUpdatedAt(now);

                if (application.getStatus() == null) {
                        application.setStatus(ApplicationStatus.SAVED);
                }

                if (application.getPriority() == null) {
                        application.setPriority(ApplicationPriority.MEDIUM);
                }
                if (application.getStatusHistory() == null) {
                        application.setStatusHistory(new ArrayList<>());
                }
                JobApplication savedApplication = repository.save(application);

                return mapper.toResponse(savedApplication);
        }

        public PagedResponse<JobApplicationResponse> findAll(
                        String keyword,
                        ApplicationStatus status,
                        ApplicationPriority priority,
                        int page,
                        int size,
                        String sortBy,
                        String direction) {

                validatePaginationAndSorting(
                                page,
                                size,
                                sortBy,
                                direction);

                Sort sort = direction.equalsIgnoreCase("asc")
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(page, size, sort);

                String currentUserId = currentUserService.getCurrentUserId();

                Page<JobApplication> applicationPage = repository.search(
                                currentUserId,
                                normalizeKeyword(keyword),
                                status,
                                priority,
                                pageable);

                List<JobApplicationResponse> content = applicationPage.getContent()
                                .stream()
                                .map(mapper::toResponse)
                                .toList();

                return new PagedResponse<>(
                                content,
                                applicationPage.getNumber(),
                                applicationPage.getSize(),
                                applicationPage.getTotalElements(),
                                applicationPage.getTotalPages(),
                                applicationPage.isFirst(),
                                applicationPage.isLast());
        }

        public JobApplicationResponse findById(
                        String id) {

                String currentUserId = currentUserService.getCurrentUserId();

                JobApplication application = repository.findByIdAndUserId(
                                id,
                                currentUserId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Job application not found with id: "
                                                                + id));

                return mapper.toResponse(application);
        }

        public JobApplicationResponse update(
                        String id,
                        UpdateJobApplicationRequest request) {

                String currentUserId = currentUserService.getCurrentUserId();

                JobApplication existingApplication = repository.findByIdAndUserId(
                                id,
                                currentUserId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Job application not found with id: "
                                                                + id));

                ApplicationStatus currentStatus = existingApplication.getStatus();

                if (currentStatus == null) {
                        currentStatus = ApplicationStatus.SAVED;
                }

                ApplicationStatus requestedStatus = request.getStatus() == null
                                ? currentStatus
                                : request.getStatus();

                validateStatusTransition(
                                currentStatus,
                                requestedStatus);

                mapper.updateDocument(
                                request,
                                existingApplication);

                existingApplication.setStatus(requestedStatus);

                if (existingApplication.getPriority() == null) {
                        existingApplication.setPriority(
                                        ApplicationPriority.MEDIUM);
                }

                LocalDateTime now = LocalDateTime.now();

                if (currentStatus != requestedStatus) {
                        addStatusHistory(
                                        existingApplication,
                                        currentStatus,
                                        requestedStatus,
                                        now);
                }

                existingApplication.setUpdatedAt(now);

                JobApplication savedApplication = repository.save(existingApplication);

                return mapper.toResponse(savedApplication);
        }

        public void delete(
                        String id) {

                String currentUserId = currentUserService.getCurrentUserId();

                JobApplication existingApplication = repository.findByIdAndUserId(
                                id,
                                currentUserId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Job application not found with id: "
                                                                + id));

                repository.delete(existingApplication);
        }

        private void validatePaginationAndSorting(
                        int page,
                        int size,
                        String sortBy,
                        String direction) {

                if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
                        throw new IllegalArgumentException(
                                        "Unsupported sort field: " + sortBy);
                }

                if (page < 0) {
                        throw new IllegalArgumentException(
                                        "Page number must not be negative");
                }

                if (size < 1 || size > 100) {
                        throw new IllegalArgumentException(
                                        "Page size must be between 1 and 100");
                }

                if (!direction.equalsIgnoreCase("asc")
                                && !direction.equalsIgnoreCase("desc")) {

                        throw new IllegalArgumentException(
                                        "Sort direction must be asc or desc");
                }
        }

        private void validateStatusTransition(
                        ApplicationStatus currentStatus,
                        ApplicationStatus requestedStatus) {

                if (!statusService.isTransitionAllowed(
                                currentStatus,
                                requestedStatus)) {

                        throw new InvalidStatusTransitionException(
                                        "Cannot change job application status from "
                                                        + currentStatus
                                                        + " to "
                                                        + requestedStatus);
                }
        }

        private void addStatusHistory(
                        JobApplication application,
                        ApplicationStatus fromStatus,
                        ApplicationStatus toStatus,
                        LocalDateTime changedAt) {

                if (application.getStatusHistory() == null) {
                        application.setStatusHistory(
                                        new ArrayList<>());
                }

                StatusHistory history = new StatusHistory(
                                fromStatus,
                                toStatus,
                                changedAt);

                application.getStatusHistory().add(history);
        }

        private String normalizeKeyword(
                        String keyword) {

                if (keyword == null) {
                        return null;
                }

                String normalizedKeyword = keyword.trim();

                return normalizedKeyword.isEmpty()
                                ? null
                                : normalizedKeyword;
        }
}