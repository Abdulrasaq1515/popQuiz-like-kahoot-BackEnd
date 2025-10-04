package com.popQuizApp.data.repository;

import com.popQuizApp.data.model.QuestionBank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {
    List<QuestionBank> findByTitleContainingIgnoreCase(String title);
}
