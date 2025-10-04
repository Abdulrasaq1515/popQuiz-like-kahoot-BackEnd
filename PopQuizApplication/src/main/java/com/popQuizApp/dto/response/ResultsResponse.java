package com.popQuizApp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsResponse {
    private String quizTitle;
    private String quizCode;
    private List<PlayerResponse> leaderboard;
}
