package com.example.profit_java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

// TrainingsabschnittActivity
public class TaskListActivity extends AppCompatActivity {



    int loggedInUserID;
    int loggedInUserscore;
    int refreshedUserscore;
    int refreshedUserTear;
    int loggedInUserTear;
    String loggedInUsername;




    private TaskListAdapter adapter;
    private List<Task> taskList;

    TextView currentUserscoreTV;
    TextView currentUserTearTV;
    RecyclerView taskRecyclerView;

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
        loggedInUserTear = PreferenceManager.getDefaultSharedPreferences(this).getInt("userTear", -1);

        currentUserscoreTV = (TextView) findViewById(R.id.userScoreTV);
        currentUserTearTV = (TextView) findViewById(R.id.userTearTV);

        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(this, taskList);
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskListRecyclerView);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);


        loadUserScore();
        loadTaskList();
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

                                // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt
                                PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("userScore", jsonResponse.getInt("refreshed_score")).apply();
                                PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("userTear", jsonResponse.getInt("refreshed_tear")).apply();

                                

                                refreshedUserscore = PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).getInt("userScore", -1);
//                                refreshedUserscore = jsonResponse.getInt("userScore");


                                refreshedUserTear = PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).getInt("userTear", -1);

//                                refreshedUserTear = jsonResponse.getInt("userTear");


                                //dringend jemanden Fragen wieso das funktioniert
                                SharedPreferences SPRefreshedUserscore = getSharedPreferences(String.valueOf(refreshedUserscore), Activity.MODE_PRIVATE);
                                // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
                                String setUserscore = SPRefreshedUserscore.getString("", String.valueOf(refreshedUserscore));

                                SharedPreferences SPRefreshedUserTear = getSharedPreferences(String.valueOf(refreshedUserTear), Activity.MODE_PRIVATE);
                                String setUserTear = SPRefreshedUserTear.getString("", String.valueOf(refreshedUserTear));

//                                currentUserscoreTV.setText(setUserscore);
                                currentUserscoreTV.setText(setUserscore);
//                                currentUserTearTV.setText(setUserTear);
                                currentUserTearTV.setText(setUserTear);


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


        queue.add(postRequest);


    }

    public void loadTaskList() {

        taskList.clear();
        String create_user_url = getString(R.string.XAMPP) + "/GetAllTasks.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                        Log.i("response", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // holt das Response Array der PHP Datei und verpackt es in ein JSON Array
                            JSONArray taskArray = (JSONArray) jsonResponse.get("task");


                                for (int taskObjekt = 0; taskObjekt < taskArray.length(); taskObjekt++) {
                                    JSONObject taskJson = taskArray.getJSONObject(taskObjekt);

                                    if (taskJson.getString("image").length() > 0 && taskJson.getString("status").matches("0") ) {

                                        String bitmapString = taskJson.getString("image");
                                        Bitmap imageBitmap = Util.getBitmapFromBase64String(bitmapString);

                                        taskList.add(new Task(taskJson.getString("taskname"), taskJson.getInt("taskID"), imageBitmap));

                                    }else if (taskJson.getString("imagetwo").length() > 0 && taskJson.getString("status").matches("1")){

                                        String bitmapStringtwo = taskJson.getString("imagetwo");
                                        Bitmap imageTwoBitmap = Util.getBitmapFromBase64String(bitmapStringtwo);

                                        taskList.add(new Task(taskJson.getString("taskname"), taskJson.getInt("taskID"), imageTwoBitmap));
                                    }else{

                                        Bitmap standartImageBitmap = Util.getBitmapFromDrawable(TaskListActivity.this, R.drawable.ppp);

                                        taskList.add(new Task(taskJson.getString("taskname"), taskJson.getInt("taskID"), standartImageBitmap));

                                    }

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
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usertear", String.valueOf(loggedInUserTear));

                return params;
            }
        };

        queue.add(postRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.profile) {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}