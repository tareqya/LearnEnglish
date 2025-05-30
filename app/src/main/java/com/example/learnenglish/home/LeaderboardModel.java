package com.example.learnenglish.home;


import com.example.learnenglish.model.Uid;

public class LeaderboardModel {
    public String username;
    public int score;
    public boolean isCurrentUser;

    public LeaderboardModel() {
    }

    public LeaderboardModel(String username, int score) {
        this.username = username;
        this.score = score;
    }
}
