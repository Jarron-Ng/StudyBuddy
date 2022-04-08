package com.example.myapplication.models;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView view;
    private TextView view_title;
    private TextView view_tag1;
    private TextView view_tag2;
    private TextView view_line;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.randomText);
        view_title = itemView.findViewById(R.id.taskTitle);
        view_tag1 = itemView.findViewById(R.id.tag1);
        view_tag2 = itemView.findViewById(R.id.tag2 );
        view_line = itemView.findViewById(R.id.line);
    }

    public TextView getView(){
        return view;
    }

    public TextView getView_line() { return view_line; }

    public TextView getView_title() { return view_title; }

    public TextView getView_tag1() { return view_tag1; }

    public TextView getView_tag2() { return view_tag2; }
}
