package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskReaction extends AppCompatActivity {


    String taskImage;

    TextView taskNameTV;
    TextView taskIDTV;

    ImageView taskImageIV;
    Bitmap currentBitmap;

    String taskname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_reaction);

        taskImage = PreferenceManager.getDefaultSharedPreferences(this).getString("taskImageString", "-1");


        // hier werden die mit übergebenen Daten aus dem TaskListAdapter ausgenommen und in Shared Prefs gespeichert
        // OHNE Serveranfrage! / Aka weniger traffic
        Intent intent = getIntent();
        String value = intent.getExtras().getString("taskNameTest");

        taskNameTV = findViewById(R.id.taskReactionNameTV);
        taskImageIV = findViewById(R.id.taskReaktionIV);


        // TODO Image tauschen, wenn die Aufgabe als erledigt markiert wurde
        // TODO Image wird schwarz angezeigt - der Base64 String scheint zwischen Tasklistadapter und Taskreaction verändert zu werden

        SharedPreferences SPTaskName = getSharedPreferences(String.valueOf(value), Activity.MODE_PRIVATE);
        String setTaskName = SPTaskName.getString("", String.valueOf(value));


        taskNameTV.setText(setTaskName);
//        taskImage.setImageBitmap(Bitmap.createScaledBitmap(task.getTaskImage(), 40, 40, false));

//
        if(taskImage != null && taskImage.length() > 0) {
            taskImageIV.setImageBitmap(Bitmap.createScaledBitmap(Util.getBitmapFromBase64String(taskImage), 40, 40, false));
            currentBitmap = Util.getBitmapFromBase64String(taskImage);
        }else{
            taskImageIV.setImageResource(R.drawable.ppp);
            currentBitmap = Util.getBitmapFromDrawable(this, R.drawable.ppp);

        }



    }
}