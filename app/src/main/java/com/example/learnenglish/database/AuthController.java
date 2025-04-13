package com.example.learnenglish.database;

import com.example.learnenglish.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthController {
    private FirebaseAuth mAuth;

    public AuthController(){
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> createNewUser(User user){
        return this.mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public Task<AuthResult> loginUser(String email, String password){
        return this.mAuth.signInWithEmailAndPassword(email, password);
    }

    public FirebaseUser getCurrentUser(){
        return this.mAuth.getCurrentUser();
    }

    public void logout(){
        this.mAuth.signOut();
    }


}
