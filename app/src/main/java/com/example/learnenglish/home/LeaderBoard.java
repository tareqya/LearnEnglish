package com.example.learnenglish.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.learnenglish.R;
import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.database.DatabaseManager;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.model.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<LeaderboardModel> leaderboardList = new ArrayList<>();
    private LeaderboardAdapter adapter;
    UserController userController ;


    public LeaderBoard() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        recyclerView=view.findViewById(R.id.rc1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
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
    public void onUserInfoFetchComplete(List<LeaderboardModel> topPlayers) {
        if (topPlayers != null) {
            leaderboardList.clear();
            leaderboardList.addAll(topPlayers);
            adapter.notifyDataSetChanged();
            Log.d("Firestore", "Loaded " + topPlayers.size() + " Users");
        } else {
           Toast.makeText(getContext(),"fail loading",Toast.LENGTH_SHORT).show();
        }

    }
});

        userController.fetchTopUsers();
    }
}