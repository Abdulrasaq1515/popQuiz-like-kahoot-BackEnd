package com.popQuizApp.data.repository;

import com.popQuizApp.data.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizRepository extends MongoRepository<Quiz, String> {
    Optional<Quiz> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}