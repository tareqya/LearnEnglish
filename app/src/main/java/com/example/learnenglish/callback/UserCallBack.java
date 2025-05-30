package com.example.learnenglish.callback;

import com.example.learnenglish.home.LeaderboardModel;
import com.example.learnenglish.model.User;

import java.util.List;

public interface UserCallBack {
    void onUserInfoFetchComplete(User user);
    void onUserInfoFetchComplete(List<LeaderboardModel> topPlayers);
}
