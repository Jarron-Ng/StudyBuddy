package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.models.TaskListAdapter;
import com.example.myapplication.models.Tasks;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class AddTask extends AppCompatActivity {
    public static final String UID = "UID";

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

                UserTask taskTemp = new UserTask(taskTitle,"","", taskTime, taskTime, taskSubject, taskPriority, "Not Done");


                // TODO: from shared preferences get the login session id of current user
                SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
                String uid = preferences.getString(UID, "");

                // TODO: retrieve tasksObj from sharedPref and add a task to it and store back into sharedpref
                Gson gson = new Gson();
                String json = preferences.getString("Tasks", "");
                Tasks tasksObj = gson.fromJson(json, Tasks.class);
                tasksObj.addTask(taskTemp);

                SharedPreferences.Editor editor2 = preferences.edit();
                Gson gson1 = new Gson();
                String json1 = gson1.toJson(tasksObj);
                editor2.putString("Tasks", json);
                editor2.commit();



                // TODO: from current user id, update firestore database arrayUnion new task into task list. build a HashMap taskUpload obj first
                HashMap<String, String> taskUpload = new HashMap<String, String>();
                taskUpload.put("title", taskTitle);
                taskUpload.put("description", "");
                taskUpload.put("color","");
                taskUpload.put("startDateTime",taskTime);
                taskUpload.put("endDateTime",taskTime);
                taskUpload.put("subject", taskSubject);
                taskUpload.put("tag", taskPriority);
                taskUpload.put("status","Not Done");
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
                documentReference.update("task list", FieldValue.arrayUnion(taskUpload));



                // go back to explore page
                Intent intent = new Intent(AddTask.this, explore.class);
                startActivity(intent);

            }
        });
    }
}