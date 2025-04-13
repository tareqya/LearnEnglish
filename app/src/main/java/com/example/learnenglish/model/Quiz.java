package com.example.learnenglish.model;

import java.util.ArrayList;

public class Quiz {
    private ArrayList<MCQ> questions;

    public Quiz(){
        questions = new ArrayList<>();
    }

    public void addQuestion(MCQ question){
        this.questions.add(question);
    }

    public boolean isCorrectAnswer(MCQ question, String answer){
        return question.isCorrectAnswer(answer);
    }

    public ArrayList<MCQ> getQuestions(){
        return this.questions;
    }

}
