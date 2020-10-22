package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class changeUserEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_email);
    }

    public void changeUserEmailA(View view) {

//        // die vorgelagerte If-Abfrage sorgt dafür, dass die Edittexte nicht leer bleiben dürfen
//        // .matches prüft, die folgende Sucheleer ist und wenn ja, wird der DB Eintrag verhindert
//
//        if (!userEmail.getText().toString().contains("@") || !userEmail.getText().toString().contains(".")) {
//            Toast.makeText(ProfileActivity.this, "Bitte geben Sie eine gültige Emailadresse ein.", Toast.LENGTH_SHORT).show();
//
//        }else{
//            String create_user_url = getString(R.string.XAMPP) + "/changeEmail.php";
//
//            StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//
//
//                            try {
//                                JSONObject jsonResponse = new JSONObject(response);
//                                // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
//                                Log.i("response", response);
//                                Toast.makeText(ProfileActivity.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();
//
//
//                                int success = Integer.parseInt(jsonResponse.get("success").toString());
//                                if (success == 1) {
//
//                                    // PreferenceManager.getDefaultSharedPreferences(TaskListActivity.this).edit().putInt("refreshedScore", jsonResponse.getInt("refreshed_score")).apply();
//                                    // Der Bug des alten Werte ladens tritt auf, wenn die alte userScore Variable, die bereits in der Activity geladen wurde,
//                                    // nicht neu befüllt wird sondern eine 2. angelegt wird, da die Erste sonst mitgeladen und als erstes darstellt wird bis man ein 2. mal lädt
//
//                                    PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit().putString("useremail", jsonResponse.getString("new_email")).apply();
//
//
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("username", loggedInUsername);
//                    params.put("useremail",userEmail.getText().toString());
//
//                    return params;
//
//                }
//            };
//
//            refreshedUserEmail = PreferenceManager.getDefaultSharedPreferences(this).getString("useremail", "-1");
//
//
//            SharedPreferences SPUserEmail = getSharedPreferences(refreshedUserEmail, Activity.MODE_PRIVATE);
//            String setUserEmail = SPUserEmail.getString("", refreshedUserEmail);
//
//            currentUserEmail.setText(setUserEmail);
//
//            queue.add(postRequest);


//        }




    }
}