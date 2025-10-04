package com.popQuizApp.dto.request;

import lombok.Data;

@Data
public class AnswerRequest {
    private String quizCode;
    private String nickname;
    private Integer questionIndex;
    private Integer chosenIndex;
    private Integer timeTaken;
}
