package com.example.learnenglish.callback;

import com.example.learnenglish.model.Word;

import java.util.ArrayList;

public interface WordCallBack {
    void onFetchRandomWordComplete(Word word);

    void onFetchWordsComplete(ArrayList<Word> words);
}
