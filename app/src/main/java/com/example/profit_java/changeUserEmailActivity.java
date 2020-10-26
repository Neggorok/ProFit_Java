package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Map;

public class changeUserEmailActivity extends AppCompatActivity {

    EditText userEmail;
    EditText userEmailRepeat;

    String loggedInUsername;
    RequestQueue queue;

    String refreshedUserEMail;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_email);

        userEmail = (EditText) findViewById(R.id.newUserEmail);
        userEmailRepeat = (EditText) findViewById(R.id.newUserEmailrepeat);

        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");

        queue = Volley.newRequestQueue(this);

    }

    public void changeUserEmail(View view) {

        // die vorgelagerte If-Abfrage sorgt dafür, dass die Edittexte nicht leer bleiben dürfen
        // .matches prüft, die folgende Sucheleer ist und wenn ja, wird der DB Eintrag verhindert

        if (userEmail.getText().toString().matches("")) {
            Toast.makeText(changeUserEmailActivity.this, "bitte füllen Sie das Feld 'Emailadresse' aus!", Toast.LENGTH_SHORT).show();

        }else if (userEmailRepeat.getText().toString().matches("")) {
            Toast.makeText(changeUserEmailActivity.this, "bitte wiederholen Sie ihre Emailadresse.", Toast.LENGTH_SHORT).show();

        }else if (!userEmail.getText().toString().contains("@") || !userEmail.getText().toString().contains(".")) {
            Toast.makeText(changeUserEmailActivity.this, "Bitte geben Sie eine gültige Emailadresse ein.", Toast.LENGTH_SHORT).show();


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
                                Toast.makeText(changeUserEmailActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();


                                int success = Integer.parseInt(jsonResponse.get("success").toString());
                                if (success == 1) {

                                    // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
                                    // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
                                    // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt

                                    PreferenceManager.getDefaultSharedPreferences(changeUserEmailActivity.this).edit().putString("userEmail", jsonResponse.getString("user_email")).apply();


                                    Intent intent = new Intent(changeUserEmailActivity.this, ProfileActivity.class);
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
                    params.put("username", loggedInUsername);
                    params.put("useremail",userEmail.getText().toString());

                    return params;

                }
            };


            queue.add(postRequest);


        }




    }
}