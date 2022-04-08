package com.example.myapplication.models;

import java.time.LocalDateTime;

// NOTE: for backend database to use and capture
public class UserTask {
    private String title;
    private String description;
    private String color;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status = "Not Done"; // either "Not Done" or "Completed"
    private String subject;
    private String tag;

    //constructor
    public UserTask(String title, String description, String color, LocalDateTime startDateTime, LocalDateTime endDateTime, String subject, String tag, String status) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.status = status;
        this.subject = subject;
        this.tag = tag;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getTag() { return this.tag; }

    public void getTag(String tag) { this.tag = tag; }

}
