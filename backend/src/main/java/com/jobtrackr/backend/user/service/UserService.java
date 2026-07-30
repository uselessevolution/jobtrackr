package com.jobtrackr.backend.user.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobtrackr.backend.common.exception.DuplicateResourceException;
import com.jobtrackr.backend.user.dto.RegisterRequest;
import com.jobtrackr.backend.user.dto.UserResponse;
import com.jobtrackr.backend.user.model.User;
import com.jobtrackr.backend.user.model.UserRole;
import com.jobtrackr.backend.user.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {

        String normalizedEmail =
                request.getEmail()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException(
                    "An account already exists for this email"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setDisplayName(request.getDisplayName().trim());
        user.setRoles(new HashSet<>(Set.of(UserRole.USER)));
        user.setEnabled(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    private UserResponse toResponse(User user) {
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