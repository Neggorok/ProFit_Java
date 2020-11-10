package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
    int currentStatus;

    TextView taskNameTV;
    ImageView taskImageIV;

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

        queue = Volley.newRequestQueue(this);





    }

    public void erledigtButton(View view){

        currentStatus = 1;

        String create_user_url = getString(R.string.XAMPP) + "/changeStatus.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                        Log.i("response", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // gibt die message aus der If-Abfrage der Php-Datei an Das Handy weiter und gibt sie da als sichtbaren Toast aus
                            // ein Toast ist ein kurz aufploppendes Fenster mit Informationen für den Nutzer
//                            Toast.makeText(TaskListActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                            int success = Integer.parseInt(jsonResponse.get("success").toString());
                            if (success == 1) {

                                // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt
                                PreferenceManager.getDefaultSharedPreferences(TaskReaction.this).edit().putInt("status", jsonResponse.getInt("current_status")).apply();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", String.valueOf(currentStatus));
                params.put("taskID", String.valueOf(loggedInTaskID));

                return params;
            }
        };


        queue.add(postRequest);

        Intent intent = new Intent(TaskReaction.this, TaskListActivity.class);
        startActivity(intent);
    }

    public void abbrechenButton(View view){

        currentStatus = 0;

        String create_user_url = getString(R.string.XAMPP) + "/changeStatus.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                        Log.i("response", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // gibt die message aus der If-Abfrage der Php-Datei an Das Handy weiter und gibt sie da als sichtbaren Toast aus
                            // ein Toast ist ein kurz aufploppendes Fenster mit Informationen für den Nutzer
//                            Toast.makeText(TaskListActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                            int success = Integer.parseInt(jsonResponse.get("success").toString());
                            if (success == 1) {

                                // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt
                                PreferenceManager.getDefaultSharedPreferences(TaskReaction.this).edit().putInt("status", jsonResponse.getInt("current_status")).apply();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", String.valueOf(currentStatus));
                params.put("taskID", String.valueOf(loggedInTaskID));


                return params;
            }
        };


        queue.add(postRequest);

        Intent intent = new Intent(TaskReaction.this, TaskListActivity.class);
        startActivity(intent);
    }

}