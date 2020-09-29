package com.example.profit_java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TrainingsabschnittActivity
public class TaskListActivity extends AppCompatActivity {



    int loggedInUserID;
    int loggedInUserscore;
    int refreshedUserscore;
    String loggedInUsername;



    private TaskListAdapter adapter;
    private List<Task> taskList;

    TextView currentUserscoreTV;
    RecyclerView taskRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // speichert die Daten aus den sharedPreferences in den Variablen.
        // -1 wird zurückgegeben, wenn keine Daten gefunden werden
        loggedInUserID = PreferenceManager.getDefaultSharedPreferences(this).getInt("kundenID", -1);
        loggedInUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("userScore", -1);
        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");

//        refreshedUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("refreshedScore", -1);


        currentUserscoreTV = (TextView) findViewById(R.id.userScoreTV);



        // legt die shared Pref fest, um das TextView des beim create geladenen Userscores darzustellen
        SharedPreferences SPUserscore = getSharedPreferences(String.valueOf(loggedInUserscore), Activity.MODE_PRIVATE);
        String startUserscore = SPUserscore.getString("", String.valueOf(loggedInUserscore));
        // befüllt den TextView
        currentUserscoreTV.setText(startUserscore);


        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(this, taskList);
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskListRecyclerView);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);




        loadTaskList();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // legt fest, was beim swipen refresht wird
                loadUserScore();

                // beendet die optische Laderückgabe - also den sich drehenden Preil
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadTaskList() {

        taskList.clear();
        String create_user_url = getString(R.string.XAMPP) + "/GetAllTasks.php";

        StringRequest postRequest = new StringRequest(Request.Method.GET, create_user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                        Log.i("response", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // holt das Response Array der PHP Datei und verpackt es in ein JSON Array
                            JSONArray taskArray = (JSONArray) jsonResponse.get("task");


                            for (int taskObjekte = 0; taskObjekte < taskArray.length(); taskObjekte++) {
                                JSONObject taskJson = taskArray.getJSONObject(taskObjekte);

                                taskList.add(new Task(taskJson.getString("taskname")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(postRequest);
    }

    public void loadUserScore() {

        String create_user_url = getString(R.string.XAMPP) + "/ScoreRefresh.php";

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

//                                PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("userScore", jsonResponse.getInt("refreshed_score")).apply();

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
                params.put("aUsername", loggedInUsername);

                return params;
            }
        };


        refreshedUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("userScore", -1);

        //dringend jemanden Fragen wieso das funktioniert
        SharedPreferences SPRefreshedUserscore = getSharedPreferences(String.valueOf(refreshedUserscore), Activity.MODE_PRIVATE);
        // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
        String setUserscore = SPRefreshedUserscore.getString("", String.valueOf(refreshedUserscore));

        currentUserscoreTV.setText(setUserscore);





        queue.add(postRequest);

    }
}