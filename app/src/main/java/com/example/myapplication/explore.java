package com.example.myapplication;

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
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class explore extends AppCompatActivity {

    private Tasks tasks = new Tasks();
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


        // take UID from shared pref and name of user
        SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
        String uid = preferences.getString(UID, "");
        String name = preferences.getString(NAME, "");

        //set user Display name
        TextView displayName = findViewById(R.id.textView6);
        displayName.setText(name);

        //firebase query
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Tasks tasksSave = new Tasks();
                    if (document.exists()) {
                        // take array list from firebase
                        ArrayList<String> tasks = (ArrayList<String>) document.getData().get("task list");


                        if (tasks != null) {
                            // take tasks from firebase and convert from firebase hashmap, to apps tasks data strcutre to pass to recycler view later
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
//                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                                    UserTask taskObj = new UserTask(title, description, color, startDateTime, endDateTime, subject, tag, status);

                                    tasksSave.addTask(taskObj);

                                }
                            }
                            // TODO: save the tasksSave tasks object to shared preferences folder
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
                        }

                    } else {
                        Log.d("firebase", "No such document");
                    }
                } else {
                    Log.d("firebase", "get failed with ", task.getException());
                }
            }
        });


        //on click listener for add button
        findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(explore.this, AddTask.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.quiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(explore.this, QuizMenu.class);
                startActivity(intent);
            }
        });

        // onClickListener for click events on items in recylcer view
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
                Intent intent = new Intent(explore.this, Timer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });

        // quiz button
        /*mQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(explore.this, QuizMenu.class);
                //startActivity(intent);
            }
        });*/


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
                        SharedPreferences preferences = getSharedPreferences("Shared_preferences_for_Jon", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = preferences.edit();
                        editor2.putString("Tasks", "");
                        editor2.commit();
                        finishAffinity(); // clears the back stack of page history
                        finish();
                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {

        super.onStart();

//        //FIXME: maybe a work around is that onStart of the explore page, i query firebase again just to be sure. and save it to sharedpref since query frm loginfragment and save to sharedpref doesnt work
//
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

        if ( tasksObj != null ) {
            recyclerView.setAdapter(new TaskListAdapter(1234, tasksObj));
        }
        else {
            recyclerView.setAdapter(new TaskListAdapter(1234, new Tasks()));
        }


        // greeting morning / afternoon / night
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);


        String greeting = null;
        if(timeOfDay >= 0 && timeOfDay < 12){
            greeting = "Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greeting = "Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greeting = "Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greeting = "Night";
        }
        TextView greetingView = findViewById(R.id.textView);
        greetingView.setText("Good " + greeting + ",");

    }

}