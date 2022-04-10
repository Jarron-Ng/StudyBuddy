package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Timer extends AppCompatActivity {

    EditText hoursEdit;
    EditText minEdit;
    EditText secEdit;
    TextView hrslefttext;
    TextView minlefttext;
    TextView seclefttext;

    Button start;
    Button end;

    int startTime;
    int secleft;
    int minleft;
    int hrsleft;
    int totalsecleft;
    long duration = TimeUnit.MINUTES.toMillis(0);
    CountDownTimer timer;

    long addTimeStart;
    long addTimeEnd;
    long TimeToday; //TOTAL TIME SPENT STUDYING IN MILLISECONDS EXTRACT THIS OUT
    String KEY = "TimeToday";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        hoursEdit = findViewById(R.id.hrs_select);
        minEdit = findViewById(R.id.min_select);
        secEdit = findViewById(R.id.sec_select);
        hrslefttext = findViewById(R.id.hrview);
        minlefttext = findViewById(R.id.minview);
        seclefttext = findViewById(R.id.secview);

        start = findViewById(R.id.startbtn);
        end = findViewById(R.id.endbtn);

        // so that user can't press end without starting
        end.setEnabled(false);

        hoursEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    minEdit.requestFocus();
                }
            }
        });

        minEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    secEdit.requestFocus();
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime= 0;
                try {
                    startTime += Integer.parseInt(secEdit.getText().toString()) * 1000;
                    startTime += Integer.parseInt(minEdit.getText().toString()) * 60 * 1000;
                    startTime += Integer.parseInt(hoursEdit.getText().toString()) * 60 * 60 * 1000;

                    secEdit.setEnabled(false);
                    minEdit.setEnabled(false);
                    hoursEdit.setEnabled(false);
                    start.setEnabled(false);
                    end.setEnabled(true);

                    addTimeStart = System.currentTimeMillis();

                    timer = new CountDownTimer(startTime, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            totalsecleft = (int) millisUntilFinished / 1000;
                            hrsleft = totalsecleft / 3600;
                            minleft = (totalsecleft % 3600) / 60;
                            secleft = totalsecleft % 60;
                            hrslefttext.setText(String.format("%02d", hrsleft));
                            minlefttext.setText(String.format("%02d", minleft));
                            seclefttext.setText(String.format("%02d", secleft));
                        }

                        @Override
                        public void onFinish() {
                            Context context = getApplicationContext();
                            Toast.makeText(context, "Timer up!", Toast.LENGTH_LONG).show();
                            secEdit.setEnabled(true);
                            minEdit.setEnabled(true);
                            hoursEdit.setEnabled(true);
                            start.setEnabled(true);
                            start.setEnabled(true);
                            end.setEnabled(false);

                            addTimeEnd = System.currentTimeMillis();
                            TimeToday += (addTimeEnd - addTimeStart);
                        }
                    }.start();
                }
                catch (NumberFormatException ex) {
                    Toast.makeText(Timer.this, "Please field in all fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();

                Context context = getApplicationContext();
                Toast.makeText(context,"Timer Cancelled",Toast.LENGTH_LONG).show();
                secEdit.setEnabled(true);
                minEdit.setEnabled(true);
                hoursEdit.setEnabled(true);
                start.setEnabled(true);
                end.setEnabled(false);

                addTimeEnd = System.currentTimeMillis();
                TimeToday += (addTimeEnd - addTimeStart);
                Log.i("gettime",String.valueOf(TimeToday));
            }
        });

        //Intent intent = new Intent(Timer.this, MainActivity.class); //EDIT MAINACTIVITY
        //intent.putExtra(KEY, TimeToday);
        //startActivity(intent);
    }

}