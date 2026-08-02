package com.jobtrackr.backend.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.application.dto.CreateInterviewRequest;
import com.jobtrackr.backend.application.dto.InterviewResponse;
import com.jobtrackr.backend.application.dto.UpdateInterviewRequest;
import com.jobtrackr.backend.application.service.InterviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(
        "/api/applications/{applicationId}/interviews"
)
@Tag(
        name = "Interviews",
        description = "Manage interviews belonging to a job application"
)
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(
            InterviewService interviewService) {

        this.interviewService = interviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add an interview to a job application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Interview created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job application not found"
            )
    })
    public InterviewResponse create(
            @PathVariable String applicationId,
            @Valid @RequestBody
            CreateInterviewRequest request) {

        return interviewService.create(
                applicationId,
                request);
    }

    @GetMapping
    @Operation(
            summary = "Get all interviews for a job application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interviews returned"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job application not found"
            )
    })
    public List<InterviewResponse> findAll(
            @PathVariable String applicationId) {

        return interviewService.findAll(applicationId);
    }

    @GetMapping("/{interviewId}")
    @Operation(
            summary = "Get one interview"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview returned"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application or interview not found"
            )
    })
    public InterviewResponse findById(
            @PathVariable String applicationId,
            @PathVariable String interviewId) {

        return interviewService.findById(
                applicationId,
                interviewId);
    }

    @PutMapping("/{interviewId}")
    @Operation(
            summary = "Update an interview"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Interview updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request validation failed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application or interview not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Interview status and result conflict"
            )
    })
    public InterviewResponse update(
            @PathVariable String applicationId,
            @PathVariable String interviewId,
            @Valid @RequestBody
            UpdateInterviewRequest request) {

        return interviewService.update(
                applicationId,
                interviewId,
                request);
    }

    @DeleteMapping("/{interviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete an interview"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Interview deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application or interview not found"
            )
    })
    public void delete(
            @PathVariable String applicationId,
            @PathVariable String interviewId) {

        interviewService.delete(
                applicationId,
                interviewId);
    }
}