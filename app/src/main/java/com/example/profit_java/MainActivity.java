package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.edittext_Nutzername);
        userPassword = findViewById(R.id.edittext_passwort);

        queue = Volley.newRequestQueue(this);
    }

    public void signUp_start(View view) {

        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);

    }

    public void logIn(View view) {

        if (userName.getText().toString().matches("") || userPassword.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "bitte füllen Sie alle Felder aus!", Toast.LENGTH_SHORT).show();

            // .contains sucht spezielle Zeichen innerhalb der Edittexte, damit keine Email adresse ohne @ erzeugt wird.
            // so könnten auch Zeichen oder Wörter verhindert nwerden?

            } else {


                String create_user_url = getString(R.string.XAMPP) + "/Login_ProFit.php";

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
                                    Toast.makeText(MainActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();

                                    int success = Integer.parseInt(jsonResponse.get("success").toString());
                                    if (success == 1) {
                                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("kundenID", jsonResponse.getInt("user_id")).apply();

                                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("userScore", jsonResponse.getInt("user_score")).apply();

//                                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("username", userName.getText().toString()).apply();
                                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("username", jsonResponse.getString("user_name")).apply();


                                        // der Intent startet die neue Activity
                                        // dabei wird zuerst die Activity angegeben, in der wir uns befinden
                                        // und anschließend die Zielactivity
                                        Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                                        startActivity(intent);
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
                        params.put("username", userName.getText().toString());
                        params.put("password", userPassword.getText().toString());

                        return params;
                    }
                };

                queue.add(postRequest);

            }

        }


}