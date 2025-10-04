package com.popQuizApp.util;

import com.popQuizApp.data.model.Player;
import com.popQuizApp.data.model.Question;
import com.popQuizApp.data.model.Quiz;
import com.popQuizApp.dto.request.QuizRequest;
import com.popQuizApp.dto.response.QuizResponse;
import com.popQuizApp.dto.response.ResultsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class QuizMapper {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PlayerMapper playerMapper;

    public Quiz toEntity(QuizRequest request, List<Question> questions, String hostId, String code) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz title cannot be blank");
        }

        return Quiz.builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle().trim())
                .code(code)
                .hostId(hostId)
                .status("WAITING")
                .questions(questions)
                .players(new ArrayList<>())
                .build();
    }

    public QuizResponse toResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setCode(quiz.getCode());
        response.setStatus(quiz.getStatus());
        response.setQuestions(questionMapper.toResponseList(quiz.getQuestions()));

        List<com.popQuizApp.dto.response.PlayerResponse> playerResponses = new ArrayList<>();
        if (quiz.getPlayers() != null) {
            for (Player player : quiz.getPlayers()) {
                playerResponses.add(playerMapper.toResponse(player));
            }
        }
        response.setPlayers(playerResponses);

        return response;
    }

    public ResultsResponse toResultsResponse(Quiz quiz, List<Player> sortedPlayers) {
        ResultsResponse response = new ResultsResponse();
        response.setQuizTitle(quiz.getTitle());
        response.setQuizCode(quiz.getCode());

        List<com.popQuizApp.dto.response.PlayerResponse> leaderboard = new ArrayList<>();
        for (Player player : sortedPlayers) {
            leaderboard.add(playerMapper.toResponse(player));
        }
        response.setLeaderboard(leaderboard);

        return response;
    }
}