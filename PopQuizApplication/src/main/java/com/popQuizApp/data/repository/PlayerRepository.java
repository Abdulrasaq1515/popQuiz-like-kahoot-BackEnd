package com.popQuizApp.data.repository;

import com.popQuizApp.data.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {

    List<Player> findByQuizId(String quizId);
    boolean existsByNicknameAndQuizId(String nickname, String quizId);
}