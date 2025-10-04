package com.popQuizApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Mode is required")
    private String mode;
    @NotNull(message = "Number of questions is required")
    private Integer numberOfQuestions;
    private List<String> categories;
    private String difficulty;
    @Size(min = 1, message = "At least one question is required")
    private List<QuestionRequest> questions;
}
