package com.popQuizApp.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    private String id;
    private String nickname;
    private String quizId;
    private int score;
}