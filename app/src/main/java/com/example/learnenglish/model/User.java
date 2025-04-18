package com.example.learnenglish.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User extends Uid implements Serializable {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private int points;

    public User(){
        points = 0;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getPoints() {
        return points;
    }

    public User setPoints(int points) {
        this.points = points;
        return this;
    }
}
