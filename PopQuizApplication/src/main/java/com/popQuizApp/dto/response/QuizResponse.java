package com.popQuizApp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private String id;
    private String title;
    private String code;
    private String status;
    private List<QuestionResponse> questions;
    private List<PlayerResponse> players;
}
