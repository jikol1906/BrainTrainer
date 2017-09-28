package com.example.borisgrunwald.braintrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    CountDownTimer timer;
    Button startBtn;
    Button resetBtn;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    int[] answers;
    int locationOfCorrectAnswer;
    TextView resultTextView;
    TextView scoreBoard;
    int score = 0;
    int numberOfQuestions = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();





        resetBtn = (Button) findViewById(R.id.resetButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        scoreBoard = (TextView) findViewById(R.id.scoreBoard);
        startBtn = (Button) findViewById(R.id.startButton);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        newQuestion();


    }

    public void start(View view) {

        startBtn.setVisibility(View.INVISIBLE);

        startTimer();


    }

    private void startTimer() {

        final TextView countDown = (TextView) findViewById(R.id.secondsLeftTextview);

        Log.i("Info: ","Starting timer");

        timer = new CountDownTimer(30100,1000) {

            @Override
            public void onTick(long l) {


                countDown.setText(String.valueOf(l / 1000));

            }

            @Override
            public void onFinish() {

                countDown.setText("0");
                resetBtn.setVisibility(View.VISIBLE);
                enableAsnwerButtons(false);
                saveScore(String.valueOf(score));
                printScores();

            }
        }.start();

    }

    private void enableAsnwerButtons(boolean b) {

        button0.setEnabled(b);
        button1.setEnabled(b);
        button2.setEnabled(b);
        button3.setEnabled(b);

    }


    public void chooseAnswer(View view) {

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            resultTextView.setText("Correct!");
            score++;
            newQuestion();
        } else {
            resultTextView.setText("Wrong!");
            newQuestion();
        }

        numberOfQuestions++;
        scoreBoard.setText(String.valueOf(score) + "/" + String.valueOf(numberOfQuestions));

    }




    private void newQuestion() {

        answers = getAnswers();

        Random rand = new Random();

        TextView sumTextView = (TextView) findViewById(R.id.sumTextView);

        sumTextView.setText(generateQuestion(answers));

        button0.setText(Integer.toString(answers[0]));
        button1.setText(Integer.toString(answers[1]));
        button2.setText(Integer.toString(answers[2]));
        button3.setText(Integer.toString(answers[3]));



    }


    private int[] getAnswers() {

        List<Integer> randNumbers = getRandNumbers();

        return new int[]{randNumbers.get(0),randNumbers.get(1),randNumbers.get(2),randNumbers.get(3)};


    }

    private List<Integer> getRandNumbers() {

        List<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= 40; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers;

    }

    public String generateQuestion(int[] answers) {

        Random rand = new Random();

        locationOfCorrectAnswer = rand.nextInt(4);

        int chosenAnswer = answers[locationOfCorrectAnswer];

        int firstNumber = rand.nextInt(chosenAnswer)+1;
        int secondNumber = chosenAnswer-firstNumber;

        return String.valueOf(firstNumber) + "+" + String.valueOf(secondNumber);


    }

    public void reset(View view) {

        timer.cancel();
        startTimer();
        newQuestion();
        score = 0;
        numberOfQuestions = 0;
        scoreBoard.setText("0/0");
        enableAsnwerButtons(true);
        resetBtn.setVisibility(View.INVISIBLE);


    }

    public void saveScore(String score) {

        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = spref.edit();

        String highscores = spref.getString("HIGH_SCORES", "");

        editor.putString("HIGH_SCORES", highscores + score + " ");

        editor.apply();


    }

    public void printScores() {

        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);
        String[] highScores = spref.getString("HIGH_SCORES","").split(" ");
        Arrays.sort(highScores);

        for(String score : highScores) {
            Log.i("Scores", score);
        }



    }
}

