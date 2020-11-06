package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TaskReaction extends AppCompatActivity {

    int loggedInTaskID;
    int taskIDVal;

    String taskImage;

    TextView taskNameTV;
    TextView taskIDTV;

    ImageView taskImageIV;
    Bitmap currentBitmap;

    String taskname;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_reaction);

        loggedInTaskID = PreferenceManager.getDefaultSharedPreferences(this).getInt("taskID", -1);

        taskNameTV = findViewById(R.id.taskReactionNameTV);
        taskImageIV = findViewById(R.id.taskReaktionIV);


        // TODO Image tauschen, wenn die Aufgabe als erledigt markiert wurde

        // hier werden die mit übergebenen Daten aus dem TaskListAdapter ausgenommen und in Shared Prefs gespeichert
        // OHNE Serveranfrage! / Aka weniger traffic
        Intent intent = getIntent();
        String value = intent.getExtras().getString("taskName");
        Bitmap taskBitmap = intent.getParcelableExtra("taskImage");

        SharedPreferences SPTaskName = getSharedPreferences(String.valueOf(value), Activity.MODE_PRIVATE);
        String setTaskName = SPTaskName.getString("", String.valueOf(value));


        taskNameTV.setText(setTaskName);
        taskImageIV.setImageBitmap(taskBitmap);




    }

}