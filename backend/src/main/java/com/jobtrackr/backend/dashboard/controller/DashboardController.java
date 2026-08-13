package com.jobtrackr.backend.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.dashboard.dto.DashboardSummaryResponse;
import com.jobtrackr.backend.dashboard.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Aggregated dashboard data")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard summary returned"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    public DashboardSummaryResponse getSummary() {

        return dashboardService
                .getSummary();
    }
}