package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskReaction extends AppCompatActivity {


    TextView taskNameTV;
    TextView taskIDTV;

    ImageView taskImage;

    String taskname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_reaction);


        // hier werden die mit übergebenen Daten aus dem TaskListAdapter ausgenommen und in Shared Prefs gespeichert
        // OHNE Serveranfrage! / Aka weniger traffic
        Intent intent = getIntent();
        String value = intent.getExtras().getString("taskNameTest");

        taskNameTV = findViewById(R.id.taskReactionNameTV);
        taskImage = findViewById(R.id.taskImage);


        // TODO Image tauschen, wenn die Aufgabe als erledigt markiert wurde

        SharedPreferences SPTaskName = getSharedPreferences(String.valueOf(value), Activity.MODE_PRIVATE);
        String setTaskName = SPTaskName.getString("", String.valueOf(value));

        taskNameTV.setText(setTaskName);
        taskImage.setImageResource(R.drawable.ppp);
    }
}