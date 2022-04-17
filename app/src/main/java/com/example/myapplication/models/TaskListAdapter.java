package com.example.myapplication.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.explore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Random random;
    private String[] kode = {"d116df5",
            "36ffc75", "f5cfe78", "5b87628",
            "db8d14e", "9913dc4", "e120f96",
            "466251b"};
    public Tasks tasks;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TaskListAdapter(int seed, Tasks tasks) {

        this.random = new Random(seed);
        this.tasks = tasks;
        //TODO: code breaks when preparing hardcoded case, try not putting the init of the test case in the constructor of this class, try put it under onCreateView or smth else
//        ArrayList<String> tags = new ArrayList<String>();
//        tags.add("urgent");
//        tags.add("Science");
//
//        LocalDateTime rightnow = LocalDateTime.now();
//        Task task1 = new Task("Revise math", "I hate math", "orange", rightnow, rightnow, tags, "urgent");
//        Task task2 = new Task("test2", "i love math", "blue", rightnow, rightnow, tags, "due");
//        Task task3 = new Task("science is king", "e = mc^2", "red", rightnow, rightnow, tags, "overdue");
//        tasks.addTask(task1);
//        tasks.addTask(task2);
//        tasks.addTask(task3);

    }


    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view).linkaAdapter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if (position != tasks.getSize()) {
            UserTask userTask;
            //        holder.getView().setText(kode[position]);
            //        holder.getView_title().setText(String.valueOf(random.nextInt()));
            holder.getView_tag1().setText(String.valueOf(random.nextInt()));


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
            userTask = tasks.getTask(position);
            holder.getView().setText(userTask.getStartDateTime());
            holder.getView_title().setText(userTask.getTitle());
            holder.getView_tag1().setText(userTask.getTag());
            holder.getView_tag2().setText(userTask.getSubject());
        }

    }

    @Override
    public int getItemCount() {
        return tasks.getSize();
    }
}

class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private static final int MODE_PRIVATE = 0;
    private TextView view;
    private TextView view_title;
    private TextView view_tag1;
    private TextView view_tag2;
    private TextView view_line;
    private TaskListAdapter adapter;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
        view_title = itemView.findViewById(R.id.taskTitle);
        view_tag1 = itemView.findViewById(R.id.tag1);
        view_tag2 = itemView.findViewById(R.id.tag2 );
        view_line = itemView.findViewById(R.id.line);

        itemView.findViewById(R.id.delete).setOnClickListener(view ->  {
            UserTask task = adapter.tasks.getTask(getAdapterPosition());
            int position = getAdapterPosition();
            String pos = String.valueOf(position);
            Log.i("delete", "index pos: " + pos);
            Log.i("delete", "task title: " + task.getTitle());

            // firebase delete update
            // get user document uid from sharedpref
            SharedPreferences preferences = itemView.getContext().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
            String uid = preferences.getString("UID", "");


            // remove using arrayRemove at position index
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
            documentReference.update("task list", FieldValue.arrayRemove(position));

            // query firestore for updated arraylist of task and build tasksSave object to save to sharedPref
            //firebase query
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
                                // take tasks from firebase and convert from firebase hashmap, to apps tasks data structure to pass to recycler view later
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
                            }

                        } else {
                            Log.d("firebase", "No such document");
                        }
                    } else {
                        Log.d("firebase", "get failed with ", task.getException());
                    }
                }
            });

            // adapter remove task
            adapter.tasks.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
        });
    }

    public RecyclerViewHolder linkaAdapter(TaskListAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public TextView getView(){
        return view;
    }

    public TextView getView_line() { return view_line; }

    public TextView getView_title() { return view_title; }

    public TextView getView_tag1() { return view_tag1; }

    public TextView getView_tag2() { return view_tag2; }
}


