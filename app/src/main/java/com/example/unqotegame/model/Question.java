package com.example.unqotegame.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Question {

    private int imageId;
    private String questionText;
    private String answerZero;
    private String answerOne;
    private String answerTwo;
    private String answerThree;
    private int correctAnswer;

    private int playerAnswer;

    public Question(int imageId, String questionText, String answerZero, String answerOne, String answerTwo,
                    String answerThree, int correctAnswer) {
        this.imageId = imageId;
        this.questionText = questionText;
        this.answerZero = answerZero;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.correctAnswer = correctAnswer;
        this.playerAnswer = -1;
    }

    public boolean isCorrect() {
        return playerAnswer == correctAnswer;
    }


}
