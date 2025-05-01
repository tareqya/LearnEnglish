package com.example.learnenglish.database;

import androidx.annotation.NonNull;

import com.example.learnenglish.callback.WordCallBack;
import com.example.learnenglish.model.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class WordController extends DatabaseManager{
    public static final String WORDS_TABLE = "Words";
    private WordCallBack wordCallBack;
    public WordController(){

    }

    public void setWordCallBack(WordCallBack wordCallBack){
        this.wordCallBack = wordCallBack;
    }

    public void fetchRandomWord(){
        this.db.collection(WORDS_TABLE).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int wordsLength = task.getResult().getDocuments().size();
                Random random = new Random();
                int randomWordIndex = random.nextInt(wordsLength);
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(randomWordIndex);

                Word word = documentSnapshot.toObject(Word.class);
                word.setUid(documentSnapshot.getId());

                wordCallBack.onFetchRandomWordComplete(word);
            }
        });
    }

    public void fetchWords(){
        this.db.collection(WORDS_TABLE).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Word> words = new ArrayList<>();
                        for (int i = 0 ; i < task.getResult().getDocuments().size(); i ++){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(i);
                            Word word = documentSnapshot.toObject(Word.class);
                            words.add(word);
                            word.setUid(documentSnapshot.getId());
                        }
                        wordCallBack.onFetchWordsComplete(words);
                    }
                });
    }
}
