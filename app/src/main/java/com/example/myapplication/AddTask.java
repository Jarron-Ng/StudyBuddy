package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.models.User;
import com.example.myapplication.models.UserTask;

import java.time.LocalDateTime;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // cancel button
        findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTask.this, explore.class);
                startActivity(intent);
            }
        });

        // add button
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                // referencing user input as strings
                EditText taskTitleView = (EditText) findViewById(R.id.title_field);
                EditText taskTimeView = findViewById(R.id.time_field);
                EditText taskPriorityView = findViewById(R.id.priority_field);
                EditText taskSubjectView = findViewById(R.id.subject_field);

                String taskTitle = taskTitleView.getText().toString().trim();
                String taskTime = taskTimeView.getText().toString().trim();
                String taskPriority = taskPriorityView.getText().toString().trim();
                String taskSubject = taskSubjectView.getText().toString().trim();

                LocalDateTime rightnow = LocalDateTime.now();

                UserTask taskTemp = new UserTask(taskTitle,"","", LocalDateTime.parse(taskTime), LocalDateTime.parse(taskTime), taskSubject, taskPriority, "Not Done");


                // TODO: from shared preferences get the login session id of current user

                // TODO: from current user id, query firestore database for the tasks object

                // TODO: take the tasks object, add task to it then reupload back to firestore


                // go back to explore page
                Intent intent = new Intent(AddTask.this, explore.class);
                startActivity(intent);

            }
        });
    }
}