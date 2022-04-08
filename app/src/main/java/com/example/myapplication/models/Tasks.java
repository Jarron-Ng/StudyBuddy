package com.example.myapplication.models;

import java.util.ArrayList;

public class Tasks {
    private ArrayList<UserTask> userTasks;

    //getters and setters
    public ArrayList<UserTask> getTasks() {
        return userTasks;
    }
    public void setTasks(ArrayList<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    //constructor
    public Tasks() {
        this.userTasks = new ArrayList<UserTask>();
    }

    //methods
    public void addTask(UserTask userTask) {
        this.userTasks.add(userTask);
    }
    public void removeTask(UserTask userTask) {
        this.userTasks.remove(userTask);
    }
    public UserTask getTask(int position) { return this.userTasks.get(position); }
    public int getSize() { return this.userTasks.size(); }

}
