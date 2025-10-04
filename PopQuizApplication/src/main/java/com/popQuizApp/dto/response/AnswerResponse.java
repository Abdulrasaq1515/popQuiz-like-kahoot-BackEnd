package com.popQuizApp.dto.response;

import lombok.Data;

@Data
public class AnswerResponse {
    private boolean correct;
    private int scoreGained;
    private int totalScore;
    private int timeTaken;
    private int correctIndex;
}
