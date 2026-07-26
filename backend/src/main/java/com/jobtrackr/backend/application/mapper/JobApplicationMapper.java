package com.jobtrackr.backend.application.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.jobtrackr.backend.application.dto.CreateJobApplicationRequest;
import com.jobtrackr.backend.application.dto.JobApplicationResponse;
import com.jobtrackr.backend.application.model.JobApplication;

import com.jobtrackr.backend.application.dto.UpdateJobApplicationRequest;
@Component
public class JobApplicationMapper {

    public JobApplication toDocument(
            CreateJobApplicationRequest request) {

        JobApplication application = new JobApplication();

        application.setCompanyName(request.getCompanyName());
        application.setJobTitle(request.getJobTitle());
        application.setLocation(request.getLocation());
        application.setJobUrl(request.getJobUrl());
        application.setStatus(request.getStatus());
        application.setPriority(request.getPriority());

        if (request.getSkills() == null) {
            application.setSkills(new ArrayList<>());
        } else {
            application.setSkills(
                    new ArrayList<>(request.getSkills()));
        }

        application.setAppliedDate(request.getAppliedDate());
        application.setDeadline(request.getDeadline());

        return application;
    }

    public JobApplicationResponse toResponse(
            JobApplication application) {

        JobApplicationResponse response =
                new JobApplicationResponse();

        response.setId(application.getId());
        response.setCompanyName(application.getCompanyName());
        response.setJobTitle(application.getJobTitle());
        response.setLocation(application.getLocation());
        response.setJobUrl(application.getJobUrl());
        response.setStatus(application.getStatus());
        response.setPriority(application.getPriority());

        if (application.getSkills() == null) {
            response.setSkills(new ArrayList<>());
        } else {
            response.setSkills(
                    new ArrayList<>(application.getSkills()));
        }

        response.setAppliedDate(application.getAppliedDate());
        response.setDeadline(application.getDeadline());
        response.setCreatedAt(application.getCreatedAt());
        response.setUpdatedAt(application.getUpdatedAt());

        return response;
    }
    public void updateDocument(
        UpdateJobApplicationRequest request,
        JobApplication application) {

        application.setCompanyName(request.getCompanyName());
        application.setJobTitle(request.getJobTitle());
        application.setLocation(request.getLocation());
        application.setJobUrl(request.getJobUrl());
        application.setStatus(request.getStatus());
        application.setPriority(request.getPriority());
            
        if (request.getSkills() == null) {
            application.setSkills(new ArrayList<>());
        } else {
            application.setSkills(
                    new ArrayList<>(request.getSkills()));
        }
    
        application.setAppliedDate(request.getAppliedDate());
        application.setDeadline(request.getDeadline());
    }
}