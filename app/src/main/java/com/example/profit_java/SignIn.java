package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// "implements AdapterView" sorgt dafür, das die Klasse in der Lage ist, einen SelectListener  zu nutzen
public class SignIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText userName;
    EditText userPassword;
    EditText userEmail;
    Spinner userStudio;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userName = findViewById(R.id.editText_Nickname);
        userPassword = findViewById(R.id.editText_password_Signin);
        userEmail = findViewById(R.id.editText_Email);
        userStudio = findViewById(R.id.Spinner_Studio);
        queue = Volley.newRequestQueue(this);




        // Die Einträge des Spinners liegen in Res/values/strings

        //erzeugt eine Variable "spinner", die als referenz genutzt wird
        Spinner spinner = findViewById(R.id.Spinner_Studio);
        // der Arrayadapter legt die in Strings abgelegten Items im Spinner ab
        // In den Klammern steht wo der Adapter die Daten ablegen soll, daher "this", danach folgt das Array, wo die Einträge zu finden sind,
        // und abschließend wird die Darstellungsart der einzelnen Items festgelegt
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.studios, android.R.layout.simple_spinner_item);
        //legt das Layout des Dropdown Menüs fest
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // sorgt dafür, das der Spinner auf eine Auswahl reagiert
        spinner.setOnItemSelectedListener(this);
    }


    public void registrieren(View view) {

        // die vorgelagerte If-Abfrage sorgt dafür, dass die Edittexte nicht leer bleiben dürfen
        // .matches prüft, die folgende Sucheleer ist und wenn ja, wird der DB Eintrag verhindert

        if (userName.getText().toString().matches("") || userPassword.getText().toString().matches("") || userEmail.getText().toString().matches("")) {
            Toast.makeText(SignIn.this, "bitte füllen Sie alle Felder aus!", Toast.LENGTH_SHORT).show();

            // .contains sucht spezielle Zeichen innerhalb der Edittexte, damit keine Email adresse ohne @ erzeugt wird.
            // so könnten auch Zeichen oder Wörter verhindert nwerden?
            }else if (!userEmail.getText().toString().contains("@") || !userEmail.getText().toString().contains(".")) {
                    Toast.makeText(SignIn.this, "Bitte geben Sie eine gültige Emailadresse ein!", Toast.LENGTH_SHORT).show();


            }else if ( userStudio.getSelectedItemId() == 0) {
            Toast.makeText(SignIn.this, "Bitte wählen Sie ein Studio!", Toast.LENGTH_SHORT).show();

            }else {


                String create_user_url = getString(R.string.XAMPP) + "/registrieren_test.php";

                StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {



                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                                    Log.i("response", response);

                                    int success = Integer.parseInt(jsonResponse.get("success").toString());


                                    if (success == 1) {
                                        // der Intent startet die neue Activity
                                        // dabei wird zuerst die Activity angegeben, in der wir uns befinden
                                        // und anschließend die Zielactivity
                                        Intent intent = new Intent(SignIn.this, SignIn.class);
                                        startActivity(intent);
                                    }

                                    // gibt die message aus der If-Abfrage der Php-Datei an Das Handy weiter und gibt sie da als sichtbaren Toast aus
                                    // ein Toast ist ein kurz aufploppendes Fenster mit Informationen für den Nutzer
                                    Toast.makeText(SignIn.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                        params.put("email", userEmail.getText().toString());
                        params.put("userstudio", userStudio.getSelectedItem().toString());

                        return params;

                    }
                };

                queue.add(postRequest);

            }
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