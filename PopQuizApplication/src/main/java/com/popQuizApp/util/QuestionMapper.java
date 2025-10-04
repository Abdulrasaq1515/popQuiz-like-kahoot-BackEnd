package com.popQuizApp.util;

import com.popQuizApp.data.model.Question;
import com.popQuizApp.dto.request.QuestionRequest;
import com.popQuizApp.dto.response.QuestionResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class QuestionMapper {

    public Question toEntity(QuestionRequest request) {
        if (request.getQuestionText() == null || request.getQuestionText().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be blank");
        }

        if (request.getOptions() == null || request.getOptions().size() < 2) {
            throw new IllegalArgumentException("Question must have at least 2 options");
        }

        if (request.getCorrectIndex() == null ||
                request.getCorrectIndex() < 0 ||
                request.getCorrectIndex() >= request.getOptions().size()) {
            throw new IllegalArgumentException("Invalid correct index");
        }

        return Question.builder()
                .id(UUID.randomUUID().toString())
                .text(request.getQuestionText().trim())
                .options(request.getOptions())
                .correctIndex(request.getCorrectIndex())
                .difficulty(request.getDifficulty())
                .category(request.getCategory())
                .build();
    }

    public List<Question> toEntityList(List<QuestionRequest> requests) {
        List<Question> questions = new ArrayList<>();

        if (requests != null) {
            for (QuestionRequest request : requests) {
                questions.add(toEntity(request));
            }
        }
        return questions;
    }
    public QuestionResponse toResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setText(question.getText());
        response.setOptions(question.getOptions());
        return response;
    }
    public List<QuestionResponse> toResponseList(List<Question> questions) {
        List<QuestionResponse> responses = new ArrayList<>();
        if (questions != null) {
            for (Question question : questions) {
                responses.add(toResponse(question));
            }
        }
        return responses;
    }
}