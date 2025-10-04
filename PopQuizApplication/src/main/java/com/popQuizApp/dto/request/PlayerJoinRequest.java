package com.popQuizApp.dto.request;

import lombok.Data;

@Data
public class PlayerJoinRequest {
    private String quizCode;
    private String nickname;
}
