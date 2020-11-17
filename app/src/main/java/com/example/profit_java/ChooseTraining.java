package com.example.profit_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class ChooseTraining extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {


    EditText trainTime;
    Spinner trainName;
    ImageView trainIV;


    RequestQueue queue;
    String loggedInUsername;
    int loggedInUserscore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_training);

        trainTime = findViewById(R.id.traintimeET);
        trainName = findViewById(R.id.trainNameSpinner);
        trainIV = findViewById(R.id.trainIV);


        loggedInUsername = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "-1");
        loggedInUserscore = PreferenceManager.getDefaultSharedPreferences(this).getInt("userScore", -1);






        // Die Einträge des Spinners liegen in Res/values/strings

        //erzeugt eine Variable "spinner", die als referenz genutzt wird
        Spinner spinner = findViewById(R.id.trainNameSpinner);
        // der Arrayadapter legt die in Strings abgelegten Items im Spinner ab
        // In den Klammern steht wo der Adapter die Daten ablegen soll, daher "this", danach folgt das Array, wo die Einträge zu finden sind,
        // und abschließend wird die Darstellungsart der einzelnen Items festgelegt
        ArrayAdapter<CharSequence> adapterCT = ArrayAdapter.createFromResource(this, R.array.trainings, android.R.layout.simple_spinner_item);
        //legt das Layout des Dropdown Menüs fest
        adapterCT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCT);
        // sorgt dafür, das der Spinner auf eine Auswahl reagiert
        spinner.setOnItemSelectedListener(this);

        queue = Volley.newRequestQueue(this);


    }

    public void saveButton(View view){

        if (trainTime.getText().toString().matches("")) {
            Toast.makeText(ChooseTraining.this, "Bitte tragen Sie ihre Trainingszeit ein!", Toast.LENGTH_SHORT).show();

        }else if ( trainName.getSelectedItemId() == 0) {
            Toast.makeText(ChooseTraining.this, "Bitte wählen Sie ein Training aus!", Toast.LENGTH_SHORT).show();

        }else{

            String create_user_url = getString(R.string.XAMPP) + "/scoreUpTraining.php";

            StringRequest postRequest = new StringRequest(Request.Method.POST, create_user_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                // gibt die jeweilige Informationen aus der If-Abfrage der response-Variable der php Datei an die console von AS aus
                                Log.i("response", response);

                            int success = jsonResponse.getInt("success");
                                Toast.makeText(ChooseTraining.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();


                            if (success == 1) {
                                // der Intent startet die neue Activity
                                // dabei wird zuerst die Activity angegeben, in der wir uns befinden
                                // und anschließend die Zielactivity
                                Intent intent = new Intent(ChooseTraining.this, TaskListActivity.class);
                                startActivity(intent);
                            }

                                // gibt die message aus der If-Abfrage der Php-Datei an Das Handy weiter und gibt sie da als sichtbaren Toast aus
                                // ein Toast ist ein kurz aufploppendes Fenster mit Informationen für den Nutzer
                                Toast.makeText(ChooseTraining.this, jsonResponse.get("message").toString(), Toast.LENGTH_SHORT).show();
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
                    params.put("trainTime", trainTime.getText().toString());
                    params.put("userscore", String.valueOf(loggedInUserscore));
                    params.put("trainName", trainName.getSelectedItem().toString());



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
