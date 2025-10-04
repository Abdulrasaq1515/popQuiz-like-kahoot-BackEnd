package com.popQuizApp.services.quiz;

import com.popQuizApp.data.model.Player;
import com.popQuizApp.data.model.Question;
import com.popQuizApp.data.model.Quiz;
import com.popQuizApp.data.model.QuestionBank;
import com.popQuizApp.data.repository.QuizRepository;
import com.popQuizApp.data.repository.QuestionBankRepository;
import com.popQuizApp.dto.request.AnswerRequest;
import com.popQuizApp.dto.request.PlayerJoinRequest;
import com.popQuizApp.dto.request.QuizRequest;
import com.popQuizApp.dto.response.*;
import com.popQuizApp.util.PlayerMapper;
import com.popQuizApp.util.QuestionMapper;
import com.popQuizApp.util.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public QuizResponse createQuiz(QuizRequest request, String hostId) {
        List<Question> questions = questionMapper.toEntityList(request.getQuestions());
        if (questions.isEmpty()) {
            throw new IllegalStateException("Quiz must have at least one question");
        }
        String code = generateUniqueCode();
        Quiz quiz = quizMapper.toEntity(request, questions, hostId, code);
        Quiz saved = quizRepository.save(quiz);
        return quizMapper.toResponse(saved);
    }
    @Override
    public QuizResponse createQuizFromBank(String bankId, int questionCount, String hostId) {
        QuestionBank bank = questionBankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalStateException("Question bank not found"));

        if (bank.getQuestions().size() < questionCount) {
            throw new IllegalStateException("Not enough questions in bank");
        }
        // Simple selection - take first N questions
        List<Question> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < questionCount && i < bank.getQuestions().size(); i++) {
            Question original = bank.getQuestions().get(i);
            // Create new Question with new ID
            Question copy = Question.builder()
                    .id(UUID.randomUUID().toString())
                    .text(original.getText())
                    .options(new ArrayList<>(original.getOptions()))
                    .correctIndex(original.getCorrectIndex())
                    .difficulty(original.getDifficulty())
                    .category(original.getCategory())
                    .build();
            selectedQuestions.add(copy);
        }

        String code = generateUniqueCode();
        Quiz quiz = Quiz.builder()
                .id(UUID.randomUUID().toString())
                .title(bank.getTitle() + " Quiz")
                .code(code)
                .hostId(hostId != null ? hostId : "SYSTEM")
                .status("WAITING")
                .questions(selectedQuestions)
                .players(new ArrayList<>())
                .build();

        Quiz saved = quizRepository.save(quiz);
        return quizMapper.toResponse(saved);
    }

    @Override
    public PlayerResponse joinQuiz(PlayerJoinRequest request) {
        Quiz quiz = quizRepository.findByCode(request.getQuizCode())
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));

        if (!"WAITING".equals(quiz.getStatus())) {
            throw new IllegalStateException("Quiz is not accepting new players");
        }

        if (playerMapper.nicknameExists(quiz.getPlayers(), request.getNickname())) {
            throw new IllegalArgumentException("Nickname already taken");
        }

        Player player = playerMapper.toEntity(request.getNickname(), quiz.getId());
        quiz.getPlayers().add(player);

        quizRepository.save(quiz);
        return playerMapper.toResponse(player);
    }

    @Override
    public QuizResponse startQuiz(String quizCode) {
        Quiz quiz = quizRepository.findByCode(quizCode)
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));

        if (!"WAITING".equals(quiz.getStatus())) {
            throw new IllegalStateException("Quiz cannot be started");
        }

        if (quiz.getPlayers() == null || quiz.getPlayers().isEmpty()) {
            throw new IllegalStateException("At least one player required to start quiz");
        }

        quiz.setStatus("ACTIVE");
        Quiz saved = quizRepository.save(quiz);
        return quizMapper.toResponse(saved);
    }

    @Override
    public AnswerResponse submitAnswer(AnswerRequest request) {
        Quiz quiz = quizRepository.findByCode(request.getQuizCode())
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));

        if (!"ACTIVE".equals(quiz.getStatus())) {
            throw new IllegalStateException("Quiz is not active");
        }

        if (request.getQuestionIndex() < 0 || request.getQuestionIndex() >= quiz.getQuestions().size()) {
            throw new IllegalArgumentException("Invalid question index");
        }

        Question question = quiz.getQuestions().get(request.getQuestionIndex());

        if (request.getChosenIndex() < 0 || request.getChosenIndex() >= question.getOptions().size()) {
            throw new IllegalArgumentException("Invalid answer choice");
        }

        Optional<Player> playerOpt = playerMapper.findByNickname(quiz.getPlayers(), request.getNickname());
        if (!playerOpt.isPresent()) {
            throw new IllegalStateException("Player not found in quiz");
        }

        Player player = playerOpt.get();
        boolean correct = question.getCorrectIndex() == request.getChosenIndex();

        // Scoring logic
        int baseScore = 10;
        int timeTaken = request.getTimeTaken() != null ? request.getTimeTaken() : 15;
        int bonus = Math.max(0, 15 - timeTaken);
        int scoreGained = correct ? baseScore + bonus : 0;

        player.setScore(player.getScore() + scoreGained);

        quizRepository.save(quiz);

        return playerMapper.toAnswerResponse(correct, scoreGained, timeTaken, question.getCorrectIndex(), player);
    }

    @Override
    public QuizResponse getQuizByCode(String quizCode) {
        Quiz quiz = quizRepository.findByCode(quizCode)
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));
        return quizMapper.toResponse(quiz);
    }

    @Override
    public ResultsResponse getResults(String quizCode) {
        Quiz quiz = quizRepository.findByCode(quizCode)
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));

        List<Player> sortedPlayers = playerMapper.sortByScoreDesc(quiz.getPlayers());
        return quizMapper.toResultsResponse(quiz, sortedPlayers);
    }

    @Override
    public QuizResponse completeQuiz(String quizCode) {
        Quiz quiz = quizRepository.findByCode(quizCode)
                .orElseThrow(() -> new IllegalStateException("Quiz not found"));

        quiz.setStatus("COMPLETED");
        Quiz saved = quizRepository.save(quiz);
        return quizMapper.toResponse(saved);
    }

    @Override
    public List<QuizResponse> getActiveQuizzes() {
        List<Quiz> allQuizzes = quizRepository.findAll();
        List<QuizResponse> activeQuizzes = new ArrayList<>();

        for (Quiz quiz : allQuizzes) {
            if ("ACTIVE".equals(quiz.getStatus()) || "WAITING".equals(quiz.getStatus())) {
                activeQuizzes.add(quizMapper.toResponse(quiz));
            }
        }

        return activeQuizzes;
    }

    @Override
    public List<QuizResponse> getAllQuizzes() {
        List<Quiz> allQuizzes = quizRepository.findAll();
        List<QuizResponse> responses = new ArrayList<>();

        for (Quiz quiz : allQuizzes) {
            responses.add(quizMapper.toResponse(quiz));
        }

        return responses;
    }

    @Override
    public void deleteQuiz(String quizId) {
        quizRepository.deleteById(quizId);
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (quizRepository.existsByCode(code));
        return code;
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}