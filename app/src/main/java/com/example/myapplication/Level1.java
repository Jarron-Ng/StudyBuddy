package com.example.project1d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Level1 extends AppCompatActivity implements View.OnClickListener  {
    private QuestionBank questionBank = new QuestionBank();
    private TextView questionTextView;
    private ImageView imageView;
    private Button option1, option2, option3, option4, nextButton, prevButton;
    private String correctAnswer;
    private int currentQsIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level1);

        questionTextView = (TextView)findViewById(R.id.qsTextView);
        imageView =(ImageView) findViewById(R.id.imageView);
        option1 = (Button)findViewById(R.id.option1);
        option2 = (Button)findViewById(R.id.option2);
        option3 = (Button)findViewById(R.id.option3);
        option4 = (Button)findViewById(R.id.option4);
        nextButton = (Button) findViewById(R.id.next_button);
        prevButton = (Button) findViewById(R.id.prev_button);

        //7 clickable buttons in total
        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
    }


    //Check which button is clicked by the user
    public void onClick(View v) {
        switch (v.getId()) {
            //if users clicks next button, it will take them to next qs (by increasing qs index and use the updateQS function)
            case R.id.next_button:
                currentQsIndex += 1;
                updateQuestion(currentQsIndex);
                break;
            //if users clicks previous button, it decreases and take them to the previous qs
            case R.id.prev_button:
                currentQsIndex -= 1;
                updateQuestion(currentQsIndex);
                break;
            case R.id.option1:
                checkAnswer(currentQsIndex, option1.getText().toString());
                break;
            case R.id.option2:
                checkAnswer(currentQsIndex, option2.getText().toString());
                break;
            case R.id.option3:
                checkAnswer(currentQsIndex, option3.getText().toString());
                break;
            case R.id.option4:
                checkAnswer(currentQsIndex, option4.getText().toString());
                break;
        }
    }

    private void checkAnswer(int currentQsIndex, String userChoice) {

        int toastMessageId;
        String correctAnswer = questionBank.getCorrectAnswer(currentQsIndex);
        if (userChoice.equals(correctAnswer)) {
            score +=1;
            toastMessageId = R.string.correct_answer;
        } else {
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(Level1.this,toastMessageId,Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion(int currentQsIndex) {
        questionTextView.setText(questionBank.getQuestion(currentQsIndex));
        imageView.setImageResource(questionBank.getImage(currentQsIndex));
        option1.setText(questionBank.getOption1(currentQsIndex));
        option2.setText(questionBank.getOption2(currentQsIndex));
        option3.setText(questionBank.getOption3(currentQsIndex));
        option4.setText(questionBank.getOption4(currentQsIndex));
        correctAnswer = questionBank.getCorrectAnswer(currentQsIndex);
        if (currentQsIndex == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else if (currentQsIndex == questionBank.getLength() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}
