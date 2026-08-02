package com.jobtrackr.backend.application.mapper;

import org.springframework.stereotype.Component;

import com.jobtrackr.backend.application.dto.CreateInterviewRequest;
import com.jobtrackr.backend.application.dto.InterviewResponse;
import com.jobtrackr.backend.application.dto.UpdateInterviewRequest;
import com.jobtrackr.backend.application.model.Interview;

@Component
public class InterviewMapper {

    public Interview toDocument(
            CreateInterviewRequest request) {

        Interview interview = new Interview();

        interview.setType(request.getType());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setDurationMinutes(
                request.getDurationMinutes());
        interview.setLocation(request.getLocation());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setInterviewerName(
                request.getInterviewerName());
        interview.setNotes(request.getNotes());

        return interview;
    }

    public void updateDocument(
            UpdateInterviewRequest request,
            Interview interview) {

        interview.setType(request.getType());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setDurationMinutes(
                request.getDurationMinutes());
        interview.setLocation(request.getLocation());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setInterviewerName(
                request.getInterviewerName());
        interview.setStatus(request.getStatus());
        interview.setResult(request.getResult());
        interview.setNotes(request.getNotes());
    }

    public InterviewResponse toResponse(
            Interview interview) {

        InterviewResponse response =
                new InterviewResponse();

        response.setId(interview.getId());
        response.setType(interview.getType());
        response.setScheduledAt(
                interview.getScheduledAt());
        response.setDurationMinutes(
                interview.getDurationMinutes());
        response.setLocation(interview.getLocation());
        response.setMeetingLink(
                interview.getMeetingLink());
        response.setInterviewerName(
                interview.getInterviewerName());
        response.setStatus(interview.getStatus());
        response.setResult(interview.getResult());
        response.setNotes(interview.getNotes());
        response.setCreatedAt(
                interview.getCreatedAt());
        response.setUpdatedAt(
                interview.getUpdatedAt());

        return response;
    }
}