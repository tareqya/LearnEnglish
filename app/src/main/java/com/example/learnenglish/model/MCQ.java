package com.example.learnenglish.model;

public class MCQ {
    private String question;
    private String answer;
    private String [] options;

    public MCQ(String question, String answer, String [] options){
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public boolean isCorrectAnswer(String answer) {
        return answer.equals(this.answer);
    }

    public String getQuestion(){
        return question;
    }


    public String[] getRandomOptionsWithAnswer(){
        String[] optionsWithAnswer = new String[options.length + 1];
        optionsWithAnswer[0] = answer;
        for(int i = 1; i < options.length + 1; i++){
            optionsWithAnswer[i] = options[i - 1];
        }
        for(int i = 0; i < optionsWithAnswer.length; i++){
            int j = (int)(Math.random() * optionsWithAnswer.length);
            String temp = optionsWithAnswer[i];
            optionsWithAnswer[i] = optionsWithAnswer[j];
            optionsWithAnswer[j] = temp;
        }
        return optionsWithAnswer;
    }

}
