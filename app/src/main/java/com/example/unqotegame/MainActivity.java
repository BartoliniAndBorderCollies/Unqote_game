package com.example.unqotegame;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unqotegame.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int currentQuestionIndex;
    int totalCorrect;
    int totalQuestions;
    List<Question> questions;
    ImageView questionImageView;
    TextView questionTextView;
    TextView questionsRemainingTextView;
    Button answer0Button;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
    Button submitButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Take resources into variables
        questionImageView = findViewById(R.id.mainPicture);
        questionTextView = findViewById(R.id.mainQuestion);
        questionsRemainingTextView = findViewById(R.id.questionsLeft);
        answer0Button = findViewById(R.id.answer0);
        answer1Button = findViewById(R.id.answer1);
        answer2Button = findViewById(R.id.answer2);
        answer3Button = findViewById(R.id.answer3);
        submitButton = findViewById(R.id.submit);

        //For all answer buttons, highlight the selected answer
        Button[] answerButtons = new Button[]{answer0Button, answer1Button, answer2Button, answer3Button};

        //set the listener to each button
        for (int i = 0; i < answerButtons.length; i++) {
            final int index = i;
            answerButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //make the action
                    onAnswerSelected(index);
                }
            });
        }

        //listen to submitButton and make the appropriate action
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAnswerSubmission();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startNewGame();
    }


    private void startNewGame() {
        questions = new ArrayList<>();

        Question question0 = new Question(R.drawable.img_quote_0, "Pretty good advice, " +
                "and perhaps a scientist did say it… Who actually did??", "Albert Einstein ",
                "Isaac Newton", "Rita Mae Brown", "Rosalind Franklin",
                2);

        Question question1 = new Question(R.drawable.img_quote_1, "Was honest Abe honestly quoted? " +
                "Who authored this pithy bit of wisdom?", "Edward Stieglitz", "Maya Angelou",
                "Abraham Lincoln", "Ralph Waldo Emerson", 0);

        Question question2 = new Question(R.drawable.img_quote_2, "Easy advice to read, " +
                "difficult advice to follow — who actually said it?", "Martin Luther King Jr",
                "Mother Teresa", "Fred Rogers", "Oprah Winrey", 1);

        questions.add(question0);
        questions.add(question1);
        questions.add(question2);

        totalCorrect = 0;
        totalQuestions = questions.size();

        Question firstQuestion = chooseNewQuestion();
        displayQuestionsRemaining(questions.size());

        displayQuestion(firstQuestion);
    }

    private Question chooseNewQuestion() {
        currentQuestionIndex = generateRandomNumber(questions.size());
        return questions.get(currentQuestionIndex);
    }

    private int generateRandomNumber(int maxNumber) {
        double mathNumber = Math.random();
        double randomNumber = mathNumber * maxNumber;

        return (int) randomNumber;
    }

    private Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    private void displayQuestionsRemaining(int remainingQuestions) {
        questionsRemainingTextView.setText(String.valueOf(remainingQuestions));
    }

    private void displayQuestion(Question question) {
        questionImageView.setImageResource(question.getImageId());
        questionTextView.setText(question.getQuestionText());
        answer0Button.setText(question.getAnswerZero());
        answer1Button.setText(question.getAnswerOne());
        answer2Button.setText(question.getAnswerTwo());
        answer3Button.setText(question.getAnswerThree());
    }

    @SuppressLint("SetTextI18n")
    private void onAnswerSelected(int playerAnswer) {
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.setPlayerAnswer(playerAnswer);

        // Reset all button texts before setting the selected one
        answer0Button.setText(currentQuestion.getAnswerZero());
        answer1Button.setText(currentQuestion.getAnswerOne());
        answer2Button.setText(currentQuestion.getAnswerTwo());
        answer3Button.setText(currentQuestion.getAnswerThree());

        // Highlight the selected button
        switch (playerAnswer) {
            case 0:
                answer0Button.setText("✔ " + currentQuestion.getAnswerZero());
                break;
            case 1:
                answer1Button.setText("✔ " + currentQuestion.getAnswerOne());
                break;
            case 2:
                answer2Button.setText("✔ " + currentQuestion.getAnswerTwo());
                break;
            case 3:
                answer3Button.setText("✔ " + currentQuestion.getAnswerThree());
                break;
        }
    }

    private void onAnswerSubmission() {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion.isCorrect())
            totalCorrect++;
        questions.remove(currentQuestion);
        displayQuestionsRemaining(questions.size());
        if (questions.isEmpty()) {
            String gameOverMessage = getGameOverMessage(totalCorrect, totalQuestions);
            AlertDialog.Builder gameOverDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            gameOverDialogBuilder.setCancelable(false);
            gameOverDialogBuilder.setTitle("OH NO!!! THE GAME IS OVER!!");
            gameOverDialogBuilder.setMessage(gameOverMessage);

           // AlertDialog can present positive, neutral, and negative buttons (indicating their position from left-to-right or top-to-down, depending on the device).
            gameOverDialogBuilder.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNewGame();
                }
            });
            gameOverDialogBuilder.create().show();

        } else {
            chooseNewQuestion();
            displayQuestion(getCurrentQuestion());
        }
    }

    private String getGameOverMessage(int correct, int totalQuestions) {
        if (correct == totalQuestions) {
            return "Well done! You answered correctly for all questions!!!";
        } else {
            return "Try again! " +
                    "You answered correctly on: " + correct + " from total of: " +
                    totalQuestions;
        }
    }
}//TODO: fix the bug if player submits no answer
//TODO: add option to close the app
