package com.example.learnenglish.database;

import androidx.annotation.Nullable;

import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
        this.db.collection(USERS_TABLE).document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value == null) return;
                User user = value.toObject(User.class);
                user.setUid(uid);
                userCallBack.onUserInfoFetchComplete(user);
            }
        });
    }
}
