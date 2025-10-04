package com.popQuizApp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionResponse {
    private String text;
    private List<String> options;
}
