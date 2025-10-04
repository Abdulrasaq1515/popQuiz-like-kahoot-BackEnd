package com.popQuizApp.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "quizzes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    private String id;
    private String title;
    private String code;
    private String hostId;
    private String status;
    private List<Question> questions;
    private List<Player> players;
}