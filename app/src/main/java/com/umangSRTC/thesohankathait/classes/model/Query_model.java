package com.umangSRTC.thesohankathait.umang.classes.model;

public class Query_model {
    String question,answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Query_model() {

    }

    public Query_model(String question, String answer) {

        this.question = question;
        this.answer = answer;
    }
}
