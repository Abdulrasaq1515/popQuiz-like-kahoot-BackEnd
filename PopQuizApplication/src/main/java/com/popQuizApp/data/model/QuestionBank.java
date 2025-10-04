package com.popQuizApp.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "question_banks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBank {
    @Id
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
}