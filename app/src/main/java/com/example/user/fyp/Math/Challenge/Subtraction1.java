package com.example.user.fyp.Math.Challenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.fyp.Activities.MathTopicActivity;
import com.example.user.fyp.Activities.TestOverActivity;
import com.example.user.fyp.R;

import java.util.Random;

public class Subtraction1 extends AppCompatActivity {

    private static final String TAG = "Summ1";
    protected TextView x;
    protected TextView y;
    protected EditText c;
    protected TextView answer_textView;
    protected TextView timeLeft_textView;
    protected TextView score_textView;
    protected Button confirm;
    private int finalScore;
    CountDownTimer timerSubtraction;

    public static final String SUBTRACTION1_HIGHSCORE_KEY = "SUBTRACTION1_HIGHSCORE_KEY";
    private SharedPreferences sharedPreferences;
    protected TextView highScore;

    protected void establish() {
        x = findViewById(R.id.subtraction1_firstNumber);
        y = findViewById(R.id.subtraction1_secondNumber);
        c = findViewById(R.id.subtraction1_answer);
        answer_textView = findViewById(R.id.subtraction1_hiddenAnswer);
        confirm = findViewById(R.id.subtraction1_buttonConfirm);
        timeLeft_textView = findViewById(R.id.subtraction1_timeLeft);
        score_textView = findViewById(R.id.subtraction1_score);
        score_textView.setText("Score: -");
        finalScore = 0;
        highScore = findViewById(R.id.subtraction1_tvHighscore2);
    }

    protected void randomise() {
        int a, b, answer;
        Random rand = new Random();
        a = rand.nextInt(10);
        b = rand.nextInt(10);
        if (a < b) {
            answer = b;
            b = a;
            a = answer;
        }
        answer = a - b;

        x.setText(Integer.toString(a));
        y.setText(Integer.toString(b));
        answer_textView.setText(Integer.toString(answer));
    }

    protected boolean checkAnswer(int a, int b) {
        return a == b;
    }

    protected boolean checkBlankAnswer(String a) {
        return !a.equals("");
    }

    protected void clearAnswer() {
        c.setText("");
    }

    protected void showSnackbar(View v, String text) {
        Snackbar bar = Snackbar.make(v, text, Snackbar.LENGTH_LONG);
        bar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerSubtraction.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MathTopicActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);// animation
    }

    protected void correctAnswer() {
        finalScore += 10;
        score_textView.setText("Score: " + finalScore);
    }

    protected void wrongAnswer() {
        finalScore -= 5;
        score_textView.setText("Score: " + finalScore);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subtraction1);
        establish();
        randomise();

        sharedPreferences = getSharedPreferences("MySharedPreMain", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(SUBTRACTION1_HIGHSCORE_KEY)){
            highScore.setText(Integer.toString(sharedPreferences.getInt(SUBTRACTION1_HIGHSCORE_KEY,0)));
        }

         timerSubtraction = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeft_textView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    if (sharedPreferences.getInt(SUBTRACTION1_HIGHSCORE_KEY,0)<finalScore) {
                        editor.putInt(SUBTRACTION1_HIGHSCORE_KEY, finalScore);
                        editor.commit();
                    }
                }
                catch (Exception e){
                    editor.putInt(SUBTRACTION1_HIGHSCORE_KEY, finalScore);
                    editor.commit();
                }
                timeLeft_textView.setText("done!");
                Intent intent = new Intent(getApplicationContext(), TestOverActivity.class);
                intent.putExtra("HighScore", Integer.toString(finalScore));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);// animation
            }
        }.start();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBlankAnswer(c.getText().toString())) {
                    if (checkAnswer(Integer.parseInt(c.getText().toString()), Integer.parseInt(answer_textView.getText().toString()))) {
                        showSnackbar(v, "Correct!!");
                        correctAnswer();
                        randomise();
                        clearAnswer();
                    } else {
                        showSnackbar(v, "Incorrect, Try Again\nYou Can!!!");
                        wrongAnswer();
                    }
                } else {
                    showSnackbar(v, "Blank Answer");
                }
            }
        });

    }
}