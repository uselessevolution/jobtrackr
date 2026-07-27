package com.jobtrackr.backend.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.application.dto.CreateJobApplicationRequest;
import com.jobtrackr.backend.application.dto.JobApplicationResponse;
import com.jobtrackr.backend.application.service.JobApplicationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.jobtrackr.backend.application.dto.UpdateJobApplicationRequest;

import org.springframework.web.bind.annotation.RequestParam;

import com.jobtrackr.backend.application.dto.PagedResponse;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(
            JobApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse create(
            @Valid
            @RequestBody CreateJobApplicationRequest request) {

        return service.create(request);
    }

    @GetMapping
    public PagedResponse<JobApplicationResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return service.findAll(
                page,
                size,
                sortBy,
                direction
        );
    }
    @GetMapping("/{id}")
    public JobApplicationResponse findById(
            @PathVariable String id) {

        return service.findById(id);
    }
    @PutMapping("/{id}")
    public JobApplicationResponse update(
            @PathVariable String id,
            @Valid
            @RequestBody UpdateJobApplicationRequest request) {

        return service.update(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String id) {

        service.delete(id);
    }
}