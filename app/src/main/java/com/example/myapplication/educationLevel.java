package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class educationLevel extends AppCompatActivity {

    Button mPrimary, mSecondary, mJcpoly, mUniversity, mSkip;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_edulevel);

        mPrimary = findViewById(R.id.primary);
        mSecondary = findViewById(R.id.secondary);
        mJcpoly = findViewById(R.id.jcpoly);
        mUniversity = findViewById(R.id.university);
        mSkip = findViewById(R.id.skip);
        mAuth = FirebaseAuth.getInstance();


        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
