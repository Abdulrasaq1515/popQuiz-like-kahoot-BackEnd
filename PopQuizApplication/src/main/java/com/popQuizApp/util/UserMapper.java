package com.popQuizApp.util;

import com.popQuizApp.data.model.User;
import com.popQuizApp.dto.request.UserRequest;
import com.popQuizApp.dto.response.UserResponse;
import com.popQuizApp.exception.ApiException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.UUID;
import java.util.HashSet;

public class UserMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User toEntity(UserRequest req) {
        if (req == null) throw new ApiException("User request required");
        if (req.getUsername() == null || req.getUsername().isBlank()) throw new ApiException("Username required");
        if (req.getEmail() == null || req.getEmail().isBlank()) throw new ApiException("Email required");
        if (req.getPassword() == null || req.getPassword().isBlank()) throw new ApiException("Password required");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(req.getUsername().trim());
        user.setEmail(req.getEmail().trim());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add("HOST");
        user.setRoles(roles);
        return user;
    }

    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
