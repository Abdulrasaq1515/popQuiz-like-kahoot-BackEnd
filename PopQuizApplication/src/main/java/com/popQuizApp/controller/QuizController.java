package com.popQuizApp.controller;

import com.popQuizApp.dto.request.AnswerRequest;
import com.popQuizApp.dto.request.PlayerJoinRequest;
import com.popQuizApp.dto.request.QuizRequest;
import com.popQuizApp.dto.response.*;
import com.popQuizApp.services.quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizRequest request,
                                        @RequestParam String hostId) {
        try {
            QuizResponse response = quizService.createQuiz(request, hostId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/create-from-bank/{bankId}")
    public ResponseEntity<?> createQuizFromBank(@PathVariable String bankId,
                                                @RequestParam int n,     
                                                @RequestParam String hostId) {
        try {
            QuizResponse response = quizService.createQuizFromBank(bankId, n, hostId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinQuiz(@RequestBody PlayerJoinRequest request) {
        try {
            PlayerResponse response = quizService.joinQuiz(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/start/{quizCode}")
    public ResponseEntity<?> startQuiz(@PathVariable String quizCode) {
        try {
            QuizResponse response = quizService.startQuiz(quizCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/answer")
    public ResponseEntity<?> submitAnswer(@RequestBody AnswerRequest request) {
        try {
            AnswerResponse response = quizService.submitAnswer(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{quizCode}")
    public ResponseEntity<?> getQuiz(@PathVariable String quizCode) {
        try {
            QuizResponse response = quizService.getQuizByCode(quizCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{quizCode}/results")
    public ResponseEntity<?> getResults(@PathVariable String quizCode) {
        try {
            ResultsResponse response = quizService.getResults(quizCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<QuizResponse>> getActiveQuizzes() {
        List<QuizResponse> responses = quizService.getActiveQuizzes();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/")
    public ResponseEntity<List<QuizResponse>> getAllQuizzes() {
        List<QuizResponse> responses = quizService.getAllQuizzes();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{quizCode}/complete")
    public ResponseEntity<?> completeQuiz(@PathVariable String quizCode) {
        try {
            QuizResponse response = quizService.completeQuiz(quizCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}