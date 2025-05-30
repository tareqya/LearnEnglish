package com.example.learnenglish.database;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.home.LeaderboardModel;
import com.example.learnenglish.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class UserController extends DatabaseManager{
    public static final String USERS_TABLE = "Users";
    private UserCallBack userCallBack;

    public UserController(){

    }

    public void setUserCallBack(UserCallBack userCallBack){
        this.userCallBack = userCallBack;
    }
    public Task<Void> saveUser(User user){
        return this.db.collection(USERS_TABLE).document(user.getUid()).set(user);
    }


    public void getUserInfo(String uid){


            this.db.collection(USERS_TABLE).document(uid)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore", "Error while fetching user", error);
                                return;
                            }

                            if (value != null && value.exists()) {
                                User user = value.toObject(User.class);
                                if (user != null) {
                                    user.setUid(uid);
                                    if (userCallBack != null) {
                                        userCallBack.onUserInfoFetchComplete(user);
                                    }
                                } else {
                                    Log.e("Firestore", "User object is null (toObject failed)");
                                }
                            } else {
                                Log.e("Firestore", "Document does not exist");
                            }
                        }
                    });
        }



    public void fetchTopUsers() {
        this.db.collection(USERS_TABLE).
                orderBy("points", Query.Direction.DESCENDING).limit(3).
                addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Listen failed.", error);
                        return;
                    }

                    List<LeaderboardModel> topPlayers = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        LeaderboardModel player = doc.toObject(LeaderboardModel.class);
                        topPlayers.add(player);
                    }
                    if (userCallBack != null) {
                        userCallBack.onUserInfoFetchComplete(topPlayers);
                    }
                });
    }
}
