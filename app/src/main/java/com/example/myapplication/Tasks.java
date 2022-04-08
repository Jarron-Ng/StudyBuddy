package com.example.myapplication;

import java.util.ArrayList;

public class Tasks {
    private ArrayList<Task> tasks;

    //getters and setters
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    //constructor
    public Tasks() {
        this.tasks = new ArrayList<Task>();
    }

    //methods
    public void addTask(Task task) {
        this.tasks.add(task);
    }
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }
    public Task getTask(int position) { return this.tasks.get(position); }
    public int getSize() { return this.tasks.size(); }

}