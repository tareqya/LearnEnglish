package com.example.learnenglish.home;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnenglish.R;
import com.example.learnenglish.callback.WordCallBack;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.database.WordController;
import com.example.learnenglish.model.MCQ;
import com.example.learnenglish.model.Quiz;
import com.example.learnenglish.model.User;
import com.example.learnenglish.model.Word;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private Quiz quiz;
    public static final int QUIZ_SIZE = 5;
    public static final int FinalExam_SIZE = 50;
    private WordController wordController;
    private TextView quiz_TV_question;
    private RadioGroup quiz_RG_options;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private User currentUser;
    private static final int SCORE_PER_QUESTION = 2;
    private UserController userController;
    private ArrayList<Word> allWords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        currentUser = (User) getIntent().getSerializableExtra(HomeFragment.USER_INFO);
        findViews();
        initVars();
    }

    private void findViews() {
        quiz_RG_options = findViewById(R.id.quiz_RG_options);
        quiz_TV_question = findViewById(R.id.quiz_TV_question);
    }

    private void initVars() {
        userController = new UserController();
        wordController = new WordController();
        wordController.setWordCallBack(new WordCallBack() {
            @Override
            public void onFetchRandomWordComplete(Word word) {

            }

            @Override
            public void onFetchWordsComplete(ArrayList<Word> words) {
                allWords = words;
                ArrayList<Word> savedWords = new ArrayList<>();
                for(String wordKey : currentUser.getWordsKeys()){
                    for (Word word : words){
                        if(word.getUid().equals(wordKey)){
                            savedWords.add(word);
                        }
                    }
                }

                ArrayList<Word> randomWords = getRandomWords(savedWords, QUIZ_SIZE);
                buildQuiz(randomWords);
                displayQuestion();
            }
        });

        wordController.fetchWords();

        quiz_RG_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                MCQ mcq = quiz.getQuestions().get(currentQuestionIndex);
                String userAnswer = radioButton.getText().toString();
                if (quiz.isCorrectAnswer(mcq, userAnswer)) {
                    score += SCORE_PER_QUESTION;
                    Toast.makeText(QuizActivity.this, "Good job!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuizActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
                }
                currentQuestionIndex++;
                displayQuestion();
            }
        });
    }

    private void buildQuiz(ArrayList<Word> words) {
        quiz = new Quiz();

        for (Word word : words) {
            String[] options = getRandomOptions(allWords, 2);
            MCQ mcq = new MCQ(word.getWord(), word.getMean(), options);
            quiz.addQuestion(mcq);
        }
    }

    private void displayQuestion(){
        if (currentQuestionIndex == quiz.getQuestions().size()) {
            // update user score in database
            currentUser.setPoints(currentUser.getPoints() + score);
            userController.saveUser(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(QuizActivity.this, "Quiz finished", Toast.LENGTH_SHORT).show();
                        // close the screen
                        finish();
                    }else{
                        String err = task.getException().getMessage().toString();
                        Toast.makeText(QuizActivity.this, "Failed to save score" + ", " + err, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            return;
        }
        MCQ mcq = quiz.getQuestions().get(currentQuestionIndex);
        quiz_TV_question.setText("What the meaning of '"+mcq.getQuestion()+"'");
        String[] questionOptions = mcq.getRandomOptionsWithAnswer();
        quiz_RG_options.removeAllViews();
        for (String option : questionOptions) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            quiz_RG_options.addView(radioButton);
        }
    }

    public static ArrayList<Word> getRandomWords(ArrayList<Word> words, int size) {
        ArrayList<Word> randomWords = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(words.size());
            randomWords.add(words.get(randomIndex));
        }
        return randomWords;
    }

    public static String[] getRandomOptions(ArrayList<Word> options, int size) {
        String[] randomOptions = new String[size];
        Random random = new Random();
        int count = 0;

        while (count < size && count < options.size()) {
            int randomIndex = random.nextInt(options.size());
            String mean = options.get(randomIndex).getMean();

            boolean alreadyExists = false;
            for (int i = 0; i < count; i++) {
                if (randomOptions[i].equals(mean)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                randomOptions[count] = mean;
                count++;
            }
        }

        return randomOptions;
    }
}