package com.example.profit_java;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

// TrainingsabschnittActivity
public class TaskListActivity extends AppCompatActivity {

    private TaskListAdapter adapter;
    private List<Task> taskList;

    RecyclerView taskRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(this, taskList);
        taskRecyclerView = (RecyclerView) findViewById(R.id.taskListRecyclerView);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);

//        loadTaskList();
//        loadUserScore();


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // legt fest, was beim swipen refresht wird
//                loadTaskList();
//                loadUserScore();

                // beendet die optische Laderückgabe - also den sich drehenden Preil
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}