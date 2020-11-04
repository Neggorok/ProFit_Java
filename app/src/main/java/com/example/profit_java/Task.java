package com.example.profit_java;

import android.graphics.Bitmap;

public class Task {
    String name;
    int taskID;
    Bitmap taskImage;


    public Task(String name, int taskID, Bitmap taskImage) {
        this.name = name;
        this.taskID = taskID;
        this.taskImage = taskImage;

    }

    public Bitmap getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(Bitmap taskImage) {
        this.taskImage = taskImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
