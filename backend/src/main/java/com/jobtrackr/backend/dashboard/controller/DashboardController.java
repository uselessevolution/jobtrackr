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
import org.springframework.web.bind.annotation.RequestParam;

import com.jobtrackr.backend.dashboard.dto.DashboardAnalyticsResponse;
import com.jobtrackr.backend.dashboard.service.DashboardAnalyticsService;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Aggregated dashboard data")
public class DashboardController {

    private final DashboardService dashboardService;
    private final DashboardAnalyticsService dashboardAnalyticsService;

    public DashboardController(
            DashboardService dashboardService,
            DashboardAnalyticsService dashboardAnalyticsService) {

        this.dashboardService = dashboardService;
        this.dashboardAnalyticsService = dashboardAnalyticsService;
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

    @GetMapping("/analytics")
    @Operation(summary = "Get dashboard trend analytics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard analytics returned"),
            @ApiResponse(responseCode = "400", description = "Invalid days parameter"),
            @ApiResponse(responseCode = "401", description = "Authentication is required")
    })
    public DashboardAnalyticsResponse getAnalytics(
            @RequestParam(required = false) Integer days) {

        return dashboardAnalyticsService
                .getAnalytics(days);
    }
}