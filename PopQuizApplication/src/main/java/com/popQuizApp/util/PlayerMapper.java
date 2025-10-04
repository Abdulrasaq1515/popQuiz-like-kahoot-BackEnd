package com.popQuizApp.util;

import com.popQuizApp.data.model.Player;
import com.popQuizApp.dto.response.AnswerResponse;
import com.popQuizApp.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerMapper {

    public Player toEntity(String nickname, String quizId) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be blank");
        }
        return Player.builder()
                .id(UUID.randomUUID().toString())
                .nickname(nickname.trim())
                .quizId(quizId)
                .score(0)
                .build();
    }
    public PlayerResponse toResponse(Player player) {
        PlayerResponse response = new PlayerResponse();
        response.setNickname(player.getNickname());
        response.setScore(player.getScore());
        return response;
    }
    public boolean nicknameExists(List<Player> players, String nickname) {
        if (players == null || nickname == null) {
            return false;
        }
        for (Player player : players) {
            if (nickname.equals(player.getNickname())) {
                return true;
            }
        }
        return false;
    }
    public Optional<Player> findByNickname(List<Player> players, String nickname) {
        if (players == null || nickname == null) {
            return Optional.empty();
        }
        for (Player player : players) {
            if (nickname.equals(player.getNickname())) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
    public AnswerResponse toAnswerResponse(boolean correct, int scoreGained, int timeTaken, int correctIndex, Player player) {
        AnswerResponse response = new AnswerResponse();
        response.setCorrect(correct);
        response.setScoreGained(scoreGained);
        response.setTotalScore(player.getScore());
        response.setTimeTaken(timeTaken);
        response.setCorrectIndex(correctIndex);
        return response;
    }
    public List<Player> sortByScoreDesc(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return new ArrayList<>();
        }
        List<Player> sorted = new ArrayList<>(players);
        // Simple bubble sort descending by score
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                if (sorted.get(j).getScore() < sorted.get(j + 1).getScore()) {
                    Player temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }
        return sorted;
    }
}