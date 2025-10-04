package com.popQuizApp.services.quiz;

import com.popQuizApp.dto.request.AnswerRequest;
import com.popQuizApp.dto.request.PlayerJoinRequest;
import com.popQuizApp.dto.request.QuizRequest;
import com.popQuizApp.dto.response.*;

import java.util.List;

public interface QuizService {
    QuizResponse createQuiz(QuizRequest request, String hostId);
    QuizResponse createQuizFromBank(String bankId, int questionCount, String hostId);
    QuizResponse startQuiz(String quizCode);
    QuizResponse completeQuiz(String quizCode);
    QuizResponse getQuizByCode(String quizCode);
    ResultsResponse getResults(String quizCode);
    PlayerResponse joinQuiz(PlayerJoinRequest request);
    AnswerResponse submitAnswer(AnswerRequest request);
    List<QuizResponse> getActiveQuizzes();
    List<QuizResponse> getAllQuizzes();
    void deleteQuiz(String quizId);
}
