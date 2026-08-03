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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Job Applications", description = "Create, retrieve, update and delete job applications")

public class JobApplicationController {

        private final JobApplicationService service;

        public JobApplicationController(
                        JobApplicationService service) {
                this.service = service;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create a job application")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Job application created"),
                        @ApiResponse(responseCode = "400", description = "Request validation failed")
        })
        public JobApplicationResponse create(
                        @Valid @RequestBody CreateJobApplicationRequest request) {

                return service.create(request);
        }

        @GetMapping
        @Operation(summary = "Search job applications", description = """
                        Returns the authenticated user's job applications.
                        Supports keyword search, exact skill matching,
                        status and priority filters, applied-date and
                        deadline ranges, pagination and sorting.
                        """)
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Job applications returned"),
                        @ApiResponse(responseCode = "400", description = "Invalid query parameter"),
                        @ApiResponse(responseCode = "401", description = "Authentication is required")
        })
        public PagedResponse<JobApplicationResponse> findAll(
                        @RequestParam(required = false) String keyword,

                        @RequestParam(required = false) ApplicationStatus status,

                        @RequestParam(required = false) ApplicationPriority priority,

                        @RequestParam(required = false) String skill,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedFrom,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appliedTo,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadlineFrom,

                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadlineTo,

                        @RequestParam(defaultValue = "0") int page,

                        @RequestParam(defaultValue = "10") int size,

                        @RequestParam(defaultValue = "updatedAt") String sortBy,

                        @RequestParam(defaultValue = "desc") String direction) {

                return service.findAll(
                                keyword,
                                status,
                                priority,
                                skill,
                                appliedFrom,
                                appliedTo,
                                deadlineFrom,
                                deadlineTo,
                                page,
                                size,
                                sortBy,
                                direction);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get a job application by ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Job application found"),
                        @ApiResponse(responseCode = "404", description = "Job application not found")
        })
        public JobApplicationResponse findById(
                        @PathVariable String id) {

                return service.findById(id);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update a job application", description = "Replaces the editable fields of an existing job application")
        public JobApplicationResponse update(
                        @PathVariable String id,
                        @Valid @RequestBody UpdateJobApplicationRequest request) {

                return service.update(id, request);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Delete a job application", description = "Permanently deletes the job application with the provided ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Job application deleted"),
                        @ApiResponse(responseCode = "404", description = "Job application not found")
        })
        public void delete(
                        @PathVariable String id) {

                service.delete(id);
        }

}