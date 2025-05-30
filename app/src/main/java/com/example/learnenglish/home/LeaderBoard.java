package com.example.learnenglish.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.learnenglish.R;
import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.database.AuthController;
import com.example.learnenglish.database.DatabaseManager;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.model.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private UserController userController ;
    private Context context;


    public LeaderBoard(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        recyclerView=view.findViewById(R.id.rc1);
        userController = new UserController();
        fetchLeaderboard();

        return view;


    }

    private void fetchLeaderboard() {
userController.setUserCallBack(new UserCallBack() {
    @Override
    public void onUserInfoFetchComplete(User user) {

    }

    @Override
    public void onUserInfoFetchComplete(ArrayList<LeaderboardModel> topPlayers) {
        if (topPlayers != null) {
            LeaderboardAdapter adapter = new LeaderboardAdapter(context, topPlayers);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        } else {
           Toast.makeText(getContext(),"fail loading",Toast.LENGTH_SHORT).show();
        }

    }
});
        String uid = new AuthController().getCurrentUser().getUid();
        userController.fetchTopUsers(uid);
    }
}