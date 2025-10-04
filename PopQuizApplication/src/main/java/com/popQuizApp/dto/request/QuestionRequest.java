package com.popQuizApp.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequest {
    private String questionText;
    private List<String> options;
    private Integer correctIndex;
    private String difficulty;
    private String category;
}
