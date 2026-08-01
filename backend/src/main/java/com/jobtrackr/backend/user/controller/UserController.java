package com.jobtrackr.backend.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.user.dto.UserResponse;
import com.jobtrackr.backend.user.service.CurrentUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "Operations for the authenticated user"
)
public class UserController {

    private final CurrentUserService currentUserService;

    public UserController(
            CurrentUserService currentUserService) {

        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get the current authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authenticated user returned"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication is required"
            )
    })
    public UserResponse getCurrentUser() {

        return currentUserService.getCurrentUserResponse();
    }
}