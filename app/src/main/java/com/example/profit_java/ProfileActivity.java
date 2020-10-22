package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.prefs.PreferenceChangeEvent;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    int loggedInUserID;
    int loggedInUserscore;
    int loggedInUserTear;
    int refreshedUserscore;
    int refreshedUserTear;
    String loggedInUserEmail;
    String refreshedUserStudio;
    String refreshedUserEmail;
    String loggedInUsername;
    String loggedInUserStudio;

    Spinner userStudio;
    Button changeUserStudioButton;
    TextView currentUserEmail;
    TextView currentUserStudioTV;
    TextView currentUserscoreTV;
    TextView currentUserTearTV;
    TextView userName;
    EditText userEmail;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userStudio = findViewById(R.id.userStudioSpinner);
        changeUserStudioButton = findViewById(R.id.changeUserStudio);



        // speichert die Daten aus den sharedPreferences in den Variablen.
        // -1 wird zurückgegeben, wenn keine Daten gefunden werden
        loggedInUserID = PreferenceManager.getDefaultSharedPreferences(this).getInt("kundenID", -1);
        loggedInUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("userScore", -1);
        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        loggedInUserTear = PreferenceManager.getDefaultSharedPreferences(this).getInt("userTear", -1);
        loggedInUserStudio = PreferenceManager.getDefaultSharedPreferences(this).getString("userStudio", "-1");
        loggedInUserEmail = PreferenceManager.getDefaultSharedPreferences(this).getString("userEmail", "-1");

        currentUserscoreTV = (TextView) findViewById(R.id.userScoreProfileTV);
        currentUserTearTV = (TextView) findViewById(R.id.userTearProfileTV);
        currentUserStudioTV = (TextView) findViewById(R.id.currentUserStudioTV);
        currentUserEmail = (TextView) findViewById(R.id.currentUserEmailTV);
        userName = (TextView) findViewById(R.id.userNameProfile);
        userEmail = (EditText) findViewById(R.id.newUserEmail);


        queue = Volley.newRequestQueue(this);



        //erzeugt eine Variable "spinner", die als referenz genutzt wird
        Spinner spinnerProfile = findViewById(R.id.userStudioSpinner);
        // der Arrayadapter legt die in Strings abgelegten Items im Spinner ab
        // In den Klammern steht wo der Adapter die Daten ablegen soll, daher "this", danach folgt das Array, wo die Einträge zu finden sind,
        // und abschließend wird die Darstellungsart der einzelnen Items festgelegt
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.studiosProfile, android.R.layout.simple_spinner_item);
        //legt das Layout des Dropdown Menüs fest
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfile.setAdapter(adapter);
        // sorgt dafür, das der Spinner auf eine Auswahl reagiert
        spinnerProfile.setOnItemSelectedListener(this);

        // lädt den Score und das Tear
        loadUserProfileScore();




        SharedPreferences SPUserStudio = getSharedPreferences(loggedInUserStudio, Activity.MODE_PRIVATE);
        String setUserStudio = SPUserStudio.getString("", loggedInUserStudio);

        currentUserStudioTV.setText(setUserStudio);

        SharedPreferences SPUserName = getSharedPreferences(loggedInUsername, Activity.MODE_PRIVATE);
        String setUserName = SPUserName.getString("", loggedInUsername);

        userName.setText(setUserName);

        SharedPreferences SPUserEmail = getSharedPreferences(loggedInUserEmail, Activity.MODE_PRIVATE);
        String setUserEmail = SPUserEmail.getString("", loggedInUserEmail);

        currentUserEmail.setText(setUserEmail);
    }

    public void changeUserEmail(View view) {

        // die vorgelagerte If-Abfrage sorgt dafür, dass die Edittexte nicht leer bleiben dürfen
        // .matches prüft, die folgende Sucheleer ist und wenn ja, wird der DB Eintrag verhindert

        if (!userEmail.getText().toString().contains("@") || !userEmail.getText().toString().contains(".")) {
            Toast.makeText(ProfileActivity.this, "Bitte geben Sie eine gültige Emailadresse ein.", Toast.LENGTH_SHORT).show();

        }else{
            String create_user_url = getString(R.string.XAMPP) + "/changeEmail.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                                Log.i("response", response);
                                Toast.makeText(ProfileActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();


                                int success = Integer.parseInt(jsonResponse.get("success").toString());
                                if (success == 1) {

                                    // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                    // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                    // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt

                                    PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putString("useremail", jsonResponse.getString("new_email")).apply();


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", loggedInUsername);
                    params.put("useremail",userEmail.getText().toString());

                    return params;

                }
            };

            refreshedUserEmail = PreferenceManager.getDefaultSharedPreferences(this).getString("useremail", "-1");


            SharedPreferences SPUserEmail = getSharedPreferences(refreshedUserEmail, Activity.MODE_PRIVATE);
            String setUserEmail = SPUserEmail.getString("", refreshedUserEmail);

            currentUserEmail.setText(setUserEmail);

            queue.add(postRequest);


        }




    }

    public void changeUserStudio(View view) {

        // die vorgelagerte If-Abfrage sorgt dafür, dass die Edittexte nicht leer bleiben dürfen
        // .matches prüft, die folgende Sucheleer ist und wenn ja, wird der DB Eintrag verhindert

        if (!(userStudio.getSelectedItemId() == 0)) {
            String create_user_url = getString(R.string.XAMPP) + "/changeStudio.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                                Log.i("response", response);
                                Toast.makeText(ProfileActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();


                                int success = Integer.parseInt(jsonResponse.get("success").toString());
                                if (success == 1) {

                                    // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                    // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                    // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt

                                    PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putString("userstudio", jsonResponse.getString("refreshed_userStudio")).apply();


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userstudio", userStudio.getSelectedItem().toString());
                    params.put("username", loggedInUsername);

                    return params;

                }
            };

            refreshedUserStudio = PreferenceManager.getDefaultSharedPreferences(this).getString("userstudio", "-1");


            SharedPreferences SPUserStudio = getSharedPreferences(refreshedUserStudio, Activity.MODE_PRIVATE);
            String setUserStudio = SPUserStudio.getString("", refreshedUserStudio);

            currentUserStudioTV.setText(setUserStudio);

            queue.add(postRequest);

        }




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
                                PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putInt("userTear", jsonResponse.getInt("refreshed_tear")).apply();

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
        refreshedUserTear = PreferenceManager.getDefaultSharedPreferences(this).getInt("userTear", -1);


        //dringend jemanden Fragen wieso das funktioniert
        SharedPreferences SPRefreshedUserscore = getSharedPreferences(String.valueOf(refreshedUserscore), Activity.MODE_PRIVATE);
        // ("", String.valueOf(loggedInUserscore) Wieso gibt er immer des 2. Wert aus? der erste wird immer ignoriert...
        String setUserscore = SPRefreshedUserscore.getString("", String.valueOf(refreshedUserscore));

        SharedPreferences SPRefreshedUserTear = getSharedPreferences(String.valueOf(refreshedUserTear), Activity.MODE_PRIVATE);
        String setUserTear = SPRefreshedUserTear.getString("", String.valueOf(refreshedUserTear));

        currentUserscoreTV.setText(setUserscore);
        currentUserTearTV.setText(setUserTear);




        queue.add(postRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}