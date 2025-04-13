package com.example.learnenglish.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnenglish.R;
import com.example.learnenglish.callback.WordCallBack;
import com.example.learnenglish.database.WordController;
import com.example.learnenglish.model.User;
import com.example.learnenglish.model.Word;

import java.util.ArrayList;
import java.util.Locale;


public class HomeFragment extends Fragment {
    public static final String USER_INFO = "user_info";
    private Context context;
    private TextToSpeech textToSpeech;

    private WordController wordController;
    private TextView homeFrag_TV_word;
    private TextView homeFrag_TV_wordMean;
    private TextView homeFrag_TV_wordSentences;
    private ImageView homeFrag_IV_speak;
    private Button homeFrag_BTN_newWord;
    private Button homeFrag_BTN_startQuizButton;
    private Word currentWord;
    private User currentUser;

    public HomeFragment(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void findViews(View view) {
        homeFrag_TV_word = view.findViewById(R.id.homeFrag_TV_word);
        homeFrag_TV_wordMean = view.findViewById(R.id.homeFrag_TV_wordMean);
        homeFrag_TV_wordSentences = view.findViewById(R.id.homeFrag_TV_wordSentences);
        homeFrag_IV_speak = view.findViewById(R.id.homeFrag_IV_speak);
        homeFrag_BTN_newWord = view.findViewById(R.id.homeFrag_BTN_newWord);
        homeFrag_BTN_startQuizButton = view.findViewById(R.id.homeFrag_BTN_startQuizButton);
    }

    private void initVars() {
        initTextToSpeech(this.context);
        wordController = new WordController();
        wordController.setWordCallBack(new WordCallBack() {
            @Override
            public void onFetchRandomWordComplete(Word word) {
                currentWord = word;
                homeFrag_TV_word.setText(word.getWord());
                homeFrag_TV_wordMean.setText(word.getMean());
                homeFrag_TV_wordSentences.setText(word.getSentence());
            }

            @Override
            public void onFetchWordsComplete(ArrayList<Word> words) {

            }
        });

        wordController.fetchRandomWord();


        homeFrag_BTN_newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordController.fetchRandomWord();
            }
        });

        homeFrag_IV_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakText(currentWord.getWord());
            }
        });

        homeFrag_BTN_startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuizActivity.class);
                intent.putExtra(USER_INFO, currentUser);
                startActivity(intent);
            }
        });
    }


    private void initTextToSpeech(Context context) {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.ENGLISH);

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            } else {
                Log.e("TTS", "Initialization failed");
            }
        });
    }

    public void speakText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.e("TTS", "TextToSpeech not initialized");
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
}