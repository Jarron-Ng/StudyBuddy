package com.example.myapplication.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    private TextView view_title;
    private TextView view_tag1;
    private TextView view_tag2;
    private TextView view_line;
    private TextView view_date;
    private ImageButton menuIcon;
    private Switch switchbutton;
    public static final String UID = "UID";
    FirebaseFirestore mStore;


    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
        view_title = itemView.findViewById(R.id.taskTitle);
        view_tag1 = itemView.findViewById(R.id.tag1);
        view_tag2 = itemView.findViewById(R.id.tag2);
        view_line = itemView.findViewById(R.id.line);
        view_date = itemView.findViewById(R.id.date);
        menuIcon = itemView.findViewById(R.id.imageButton);
        mStore = FirebaseFirestore.getInstance();
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = getAdapterPosition();
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                SharedPreferences preferences = v.getContext().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
                String uid = preferences.getString(UID, "");
                menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(v.getContext(), "Task deleted, please refresh tasks", Toast.LENGTH_SHORT).show();
                        DocumentReference docRef = mStore.collection("users").document(uid);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // take array list from firebase
                                        ArrayList<String> tasks = (ArrayList<String>) document.getData().get("task list");
                                        docRef.update("task list", FieldValue.arrayRemove(tasks.get(i)));
                                    }
                                }
                            }
                        });
                        return false;
                    }
                });
                menu.show();

            }
        });


    }


    // for time
    public TextView getView(){
        return view;
    }

    public TextView getView_line() { return view_line; }

    public TextView getView_title() { return view_title; }

    public TextView getView_tag1() { return view_tag1; }

    public TextView getView_tag2() { return view_tag2; }

    public TextView getView_date() { return view_date; }
}
