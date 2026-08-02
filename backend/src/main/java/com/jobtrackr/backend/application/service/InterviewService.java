package com.jobtrackr.backend.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.dto.CreateInterviewRequest;
import com.jobtrackr.backend.application.dto.InterviewResponse;
import com.jobtrackr.backend.application.dto.UpdateInterviewRequest;
import com.jobtrackr.backend.application.mapper.InterviewMapper;
import com.jobtrackr.backend.application.model.Interview;
import com.jobtrackr.backend.application.model.InterviewResult;
import com.jobtrackr.backend.application.model.InterviewStatus;
import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;
import com.jobtrackr.backend.common.exception.InvalidInterviewStateException;
import com.jobtrackr.backend.common.exception.ResourceNotFoundException;
import com.jobtrackr.backend.user.service.CurrentUserService;

@Service
public class InterviewService {

    private final JobApplicationRepository applicationRepository;
    private final InterviewMapper interviewMapper;
    private final CurrentUserService currentUserService;

    public InterviewService(
            JobApplicationRepository applicationRepository,
            InterviewMapper interviewMapper,
            CurrentUserService currentUserService) {

        this.applicationRepository = applicationRepository;
        this.interviewMapper = interviewMapper;
        this.currentUserService = currentUserService;
    }

    public InterviewResponse create(
            String applicationId,
            CreateInterviewRequest request) {

        JobApplication application =
                getOwnedApplication(applicationId);

        Interview interview =
                interviewMapper.toDocument(request);

        LocalDateTime now = LocalDateTime.now();

        interview.setId(UUID.randomUUID().toString());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setResult(InterviewResult.PENDING);
        interview.setCreatedAt(now);
        interview.setUpdatedAt(now);

        if (application.getInterviews() == null) {
            application.setInterviews(new ArrayList<>());
        }

        application.getInterviews().add(interview);
        application.setUpdatedAt(now);

        applicationRepository.save(application);

        return interviewMapper.toResponse(interview);
    }

    public List<InterviewResponse> findAll(
            String applicationId) {

        JobApplication application =
                getOwnedApplication(applicationId);

        if (application.getInterviews() == null) {
            return List.of();
        }

        return application.getInterviews()
                .stream()
                .map(interviewMapper::toResponse)
                .toList();
    }

    public InterviewResponse findById(
            String applicationId,
            String interviewId) {

        JobApplication application =
                getOwnedApplication(applicationId);

        Interview interview =
                getInterview(application, interviewId);

        return interviewMapper.toResponse(interview);
    }

    public InterviewResponse update(
            String applicationId,
            String interviewId,
            UpdateInterviewRequest request) {

        JobApplication application =
                getOwnedApplication(applicationId);

        Interview interview =
                getInterview(application, interviewId);

        validateInterviewState(
                request.getStatus(),
                request.getResult());

        interviewMapper.updateDocument(
                request,
                interview);

        LocalDateTime now = LocalDateTime.now();

        interview.setUpdatedAt(now);
        application.setUpdatedAt(now);

        applicationRepository.save(application);

        return interviewMapper.toResponse(interview);
    }

    public void delete(
            String applicationId,
            String interviewId) {

        JobApplication application =
                getOwnedApplication(applicationId);

        Interview interview =
                getInterview(application, interviewId);

        application.getInterviews().remove(interview);
        application.setUpdatedAt(LocalDateTime.now());

        applicationRepository.save(application);
    }

    private JobApplication getOwnedApplication(
            String applicationId) {

        String currentUserId =
                currentUserService.getCurrentUserId();

        return applicationRepository
                .findByIdAndUserId(
                        applicationId,
                        currentUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job application not found with id: "
                                        + applicationId));
    }

    private Interview getInterview(
            JobApplication application,
            String interviewId) {

        if (application.getInterviews() == null) {
            throw interviewNotFound(interviewId);
        }

        return application.getInterviews()
                .stream()
                .filter(interview ->
                        interviewId.equals(interview.getId()))
                .findFirst()
                .orElseThrow(() ->
                        interviewNotFound(interviewId));
    }

    private ResourceNotFoundException interviewNotFound(
            String interviewId) {

        return new ResourceNotFoundException(
                "Interview not found with id: "
                        + interviewId);
    }

    private void validateInterviewState(
            InterviewStatus status,
            InterviewResult result) {

        if (status == InterviewStatus.SCHEDULED
                && result != InterviewResult.PENDING) {

            throw new InvalidInterviewStateException(
                    "A scheduled interview must have a PENDING result");
        }

        if (status == InterviewStatus.CANCELLED
                && result != InterviewResult.PENDING) {

            throw new InvalidInterviewStateException(
                    "A cancelled interview must have a PENDING result");
        }
    }
}