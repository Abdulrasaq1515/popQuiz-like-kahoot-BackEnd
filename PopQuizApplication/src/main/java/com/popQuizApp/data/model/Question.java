package com.popQuizApp.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    private String id;
    private String text;
    private List<String> options;
    private int correctIndex;
    private String difficulty;
    private String category;
}