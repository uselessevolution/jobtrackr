package com.jobtrackr.backend.user.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jobtrackr.backend.user.dto.UserResponse;
import com.jobtrackr.backend.user.model.User;
import com.jobtrackr.backend.user.repository.UserRepository;

import java.util.Locale;
import java.util.Set;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new AuthenticationCredentialsNotFoundException(
                    "Authentication is required"
            );
        }

        String normalizedEmail = authentication.getName()
                .trim()
                .toLowerCase(Locale.ROOT);

        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new AuthenticationCredentialsNotFoundException(
                                "Authenticated user no longer exists"
                        )
                );
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public UserResponse getCurrentUserResponse() {

        User user = getCurrentUser();

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                Set.copyOf(user.getRoles()),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}