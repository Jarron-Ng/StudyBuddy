package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.models.RecyclerItemClickListener;
import com.example.myapplication.models.TaskListAdapter;
import com.example.myapplication.models.Tasks;
import com.example.myapplication.models.UserTask;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.time.LocalDateTime;

public class explore extends AppCompatActivity {

    private Tasks tasks = new Tasks();

    // Add RecyclerView member
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // initialise some data for testing
        //TODO: for actual implementaion, app should read firebase data that returns a object of class Task, as implemented in the Models folder
        String tag1 = "urgent";
        String tag2 = "due";
        String tag3 = "overdue";
        String tag4 = "Low";
        String tag5 = "Mid";
        String tag6 = "High";
        String status1 = "Not Done";
        String status2 = "Completed";
        LocalDateTime rightnow = LocalDateTime.now();
        UserTask userTask1 = new UserTask("Revise math", "I hate math", "orange", rightnow, rightnow, "Science", tag1, "Not Done");
        UserTask userTask2 = new UserTask("test2", "i love math", "blue", rightnow, rightnow, "Math", tag2, "Not Done");
        UserTask userTask3 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask4 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask5 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask6 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask7 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask8 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask9 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask10 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask11 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask12 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
//        UserTask userTask13 = new UserTask("jefifjeioajfoej", "feijfoiajfngi", "blue", rightnow, rightnow, "physics", tag3, "Not Done");
        tasks.addTask(userTask1);
        tasks.addTask(userTask2);
        tasks.addTask(userTask3);
//        tasks.addTask(task4);
//        tasks.addTask(task5);
//        tasks.addTask(task6);
//        tasks.addTask(task7);
//        tasks.addTask(task8);
//        tasks.addTask(task9);
//        tasks.addTask(task10);
//        tasks.addTask(task11);
//        tasks.addTask(task12);
//        tasks.addTask(task13);


        // TODO: from shared preferences, get the user login session id

        // TODO: from current user id, query firestore database for the tasks object

        // TODO: take the tasks object and pass it to TaskList adapter for recyclerview to display data

        // Add the following lines to create RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(explore.this));
        recyclerView.setAdapter(new TaskListAdapter(1234, tasks));

        //on click listener for add button
        findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(explore.this, AddTask.class);
                startActivity(intent);
            }
        });


        // onClickListener for click events on items in recylcer view
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(explore.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever TODO: implement click on task handler what it should do next
                        Log.i("testTouch", "fragment touched");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        // pomodoro button to bring user to pomodoro activity
        findViewById(R.id.pomodoro_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add in pomodoro activity intent here transition
            }
        });
    }

}