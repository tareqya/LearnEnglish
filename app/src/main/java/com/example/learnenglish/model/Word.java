package com.example.learnenglish.model;

import java.io.Serializable;

public class Word extends Uid implements Serializable {

    private String word;
    private String mean;
    private String sentence;
    public Word(){}

    public String getWord() {
        return word;
    }

    public Word setWord(String word) {
        this.word = word;
        return this;
    }

    public String getMean() {
        return mean;
    }

    public Word setMean(String mean) {
        this.mean = mean;
        return this;
    }

    public String getSentence() {
        return sentence;
    }

    public Word setSentence(String sentence) {
        this.sentence = sentence;
        return this;
    }
}
