package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    int loggedInUserID;
    int loggedInUserscore;
    int refreshedUserscore;
    String loggedInUsername;


    TextView currentUserscoreTV;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // speichert die Daten aus den sharedPreferences in den Variablen.
        // -1 wird zurückgegeben, wenn keine Daten gefunden werden
        loggedInUserID = PreferenceManager.getDefaultSharedPreferences(this).getInt("kundenID", -1);
        loggedInUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("userScore", -1);
        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");



        currentUserscoreTV = (TextView) findViewById(R.id.userScoreProfileTV);





        queue = Volley.newRequestQueue(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutProfile);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // legt fest, was beim swipen refresht wird
                loadUserProfileScore();

                // beendet die optische Laderückgabe - also den sich drehenden Preil
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        loadUserProfileScore();
    }


    public void loadUserProfileScore() {

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
                                PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putInt("userScore", jsonResponse.getInt("refreshed_score")).apply();

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



//        //dringend jemanden Fragen wieso das funktioniert
//        SharedPreferences SPUserscore = getSharedPreferences(String.valueOf(refreshedUserscore), Activity.MODE_PRIVATE);
//        // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
//        String setUserscore = SPUserscore.getString("", String.valueOf(refreshedUserscore));
//
//        currentUserscoreTV.setText(setUserscore);

        //dringend jemanden Fragen wieso das funktioniert
        SharedPreferences SPRefreshedUserscore = getSharedPreferences(String.valueOf(refreshedUserscore), Activity.MODE_PRIVATE);
        // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
        String setUserscore = SPRefreshedUserscore.getString("", String.valueOf(refreshedUserscore));

        currentUserscoreTV.setText(setUserscore);





        queue.add(postRequest);

    }
}