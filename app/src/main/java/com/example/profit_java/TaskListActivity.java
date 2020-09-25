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


    public static final String TEXT = "text";

    int loggedInUserID;
    int loggedInUserscore;
    String loggedInUsername;



    private TaskListAdapter adapter;
    private List<Task> taskList;

    TextView currentUserscoreTV;
    RecyclerView taskRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    private String userscore;


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

        currentUserscoreTV = (TextView) findViewById(R.id.userScoreTV);

        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(this, taskList);
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskListRecyclerView);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);



        loadTaskList();
        loadUserScore();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // legt fest, was beim swipen refresht wird
                loadTaskList();
                loadUserScore();

                // beendet die optische Laderückgabe - also den sich drehenden Preil
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadTaskList() {
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


        if (loggedInUsername != null )
        {
            Toast.makeText(TaskListActivity.this, loggedInUsername, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(TaskListActivity.this, "Daten NICHT vorhanden", Toast.LENGTH_SHORT).show();
        }

        //dringend jemanden Fragen wieso das funktioniert
        SharedPreferences sharedPreferencUserscore = getSharedPreferences(String.valueOf(loggedInUserscore), Activity.MODE_PRIVATE);
        // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
        String setUserscore = sharedPreferencUserscore.getString("", String.valueOf(loggedInUserscore));

        currentUserscoreTV.setText(setUserscore);

















//        String create_user_url = getString(R.string.XAMPP) + "/GetUserScore.php";
//
//        StringRequest postRequest = new StringRequest(Request.Method.GET, create_user_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                        // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
//                        Log.i("response", response);
//
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            JSONArray scoreArray = (JSONArray) jsonResponse.get("user");
//
//
//                            for (int taskObjekte = 0; taskObjekte < scoreArray.length(); taskObjekte++) {
//                                JSONObject taskJson = taskArray.getJSONObject(taskObjekte);
//
//                                taskList.add(new Task(taskJson.getString("taskname")));
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        adapter.notifyDataSetChanged();
//
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        queue.add(postRequest);
    }
}