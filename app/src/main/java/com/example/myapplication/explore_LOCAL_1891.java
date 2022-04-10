package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.models.RecyclerItemClickListener;
import com.example.myapplication.models.TaskListAdapter;
import com.example.myapplication.models.Tasks;
import com.example.myapplication.models.UserTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class explore extends AppCompatActivity {

    private Tasks tasks = new Tasks();
    // variables for shared preferences
    public static final String UID = "UID";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    private String TAG = "Firebase";

    // Add RecyclerView member
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        //firebase query
        // saves shared preferences in Shared_preferences_for_Jon.xml, private to editor
        SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String uid = preferences.getString(UID, "");


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Tasks tasksSave = new Tasks();
                    if (document.exists()) {
                        String name = (String) document.getData().get("Name");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Tasks", name);
//                                                editor.apply();
                        editor.commit();
                        ArrayList<String> tasks = (ArrayList<String>) document.getData().get("task list");


//                                                if (tasks != null) {
//                                                        for (int i = 0; i < tasks.getSize(); i++) {
//                                                            UserTask userTask = tasks.getTask(i);
//                                                            String title = (String) userTask.getTitle();
//                                                            String description = (String) userTask.getDescription();
//                                                            String color = (String) userTask.getColor();
//                                                            String startDateTime = (String) userTask.getStartDateTime();
//                                                            String endDateTime = (String) userTask.getEndDateTime();
//                                                            String subject = (String) userTask.getSubject();
//                                                            String tag = (String) userTask.getTag();
//                                                            String status = (String) userTask.getStatus();
//                                                        }
//                                                    }


                        if (tasks != null) {
                            for (Object i : tasks) {
                                if (i != null) {
                                    HashMap<String, String> taskTemp = (HashMap<String, String>) i;
                                    String title = (String) taskTemp.get("title");
                                    String description = (String) taskTemp.get("description");
                                    String color = (String) taskTemp.get("color");
                                    String startDateTime = (String) taskTemp.get("startDateTime");
                                    String endDateTime = (String) taskTemp.get("endDateTime");
                                    String subject = (String) taskTemp.get("subject");
                                    String tag = (String) taskTemp.get("tag");
                                    String status = (String) taskTemp.get("status");
                                    //                                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                                    UserTask taskObj = new UserTask(title, description, color, startDateTime, endDateTime, subject, tag, status);

                                    tasksSave.addTask(taskObj);

                                }
                            }
                            // TODO: save the tasksSave tasks object to shared preferences folder

                            // put details into the editor, saved as key-value pairs FIXME: i feel like its something to do with the sharedpreferences folder cannot be the same assignment as on top preferences variable
//                                                    SharedPreferences mPreferences = getActivity().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = preferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(tasksSave);
                            editor2.putString("Tasks", json);
                            editor2.commit();


                            Log.i("firebase ", "json: " + json);
                            Log.i("firebase", "Tasks" + tasks);
                            Log.d("firebase", "DocumentSnapshot data: " + document.getData());

                            // NOTE: shifted recycler view instantiation into the oncompletelister of firebase query. directly use firebase query to generate data
                            // Add the following lines to create RecyclerView
                            recyclerView = findViewById(R.id.recyclerview);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(explore.this));
                            recyclerView.setAdapter(new TaskListAdapter(1234, tasksSave));
                            Log.i("sharedPref","json " + json);


                        }

                    } else {
                        Log.d("firebase", "No such document");
                    }
                } else {
                    Log.d("firebase", "get failed with ", task.getException());
                }
            }
        });// NOTE: add extra curly bracket here to go to other method



        //to retrieve
//        SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Tasks", "");
        Tasks tasksObj = gson.fromJson(json, Tasks.class);

        //test getting uid and email frm sharedpref
//        String uid = preferences.getString(UID, "");
        String email = preferences.getString(EMAIL, "");
        Log.i("sharedPref", "UID: " + uid);
        Log.i("sharedPref", "email: " + email);

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
        LocalDateTime timenow = LocalDateTime.now();
        String rightnow = timenow.toString();
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


        // TODO: take the tasks object and pass it to TaskList adapter for recyclerview to display data

//        // Add the following lines to create RecyclerView
//        recyclerView = findViewById(R.id.recyclerview);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(explore.this));
//        recyclerView.setAdapter(new TaskListAdapter(1234, tasksObj));
//        Log.i("sharedPref","json " + json);
//        Log.i("sharedPref", "Tasklist adapter item no: " + tasksObj.getSize()); currently tasksObj is a null obj

        // change display name on ui
        TextView displayName = (TextView) findViewById(R.id.textView6);
        String name = preferences.getString(NAME, "");
        displayName.setText(name);




        //on click listener for add button
        findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(explore.this, AddTask.class);
                startActivity(intent);
            }
        });


//        // onClickListener for click events on items in recylcer view
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(explore.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        // do whatever TODO: implement click on task handler what it should do next
//                        Log.i("testTouch", "fragment touched");
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );


        // pomodoro button to bring user to pomodoro activity
        findViewById(R.id.pomodoro_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add in pomodoro activity intent here transition
            }
        });


    }

    // Creates a dialog on back button press to close the app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity(); // clears the back stack of page history
                        finish();
                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {

        super.onStart();

        //FIXME: maybe a work around is that onStart of the explore page, i query firebase again just to be sure. and save it to sharedpref since query frm loginfragment and save to sharedpref doesnt work

        //to retrieve Tasks obj from sharedpref folder
        SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Tasks", "");
        Tasks tasksObj = gson.fromJson(json, Tasks.class);

        // TODO: take the tasks object and pass it to TaskList adapter for recyclerview to display data

        // Add the following lines to create RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(explore.this));
        recyclerView.setAdapter(new TaskListAdapter(1234, tasksObj));

    }

}