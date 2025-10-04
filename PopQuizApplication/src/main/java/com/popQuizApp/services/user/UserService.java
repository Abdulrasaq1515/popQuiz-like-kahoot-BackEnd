package com.popQuizApp.services.user;

import com.popQuizApp.dto.request.UserRequest;
import com.popQuizApp.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    UserResponse register(UserRequest request);
    Optional<UserResponse> findById(String userId);
    UserResponse getByUsername(String username);
}
