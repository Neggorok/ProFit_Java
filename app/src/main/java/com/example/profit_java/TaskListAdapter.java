package com.example.profit_java;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter  {

    public ArrayList<Task> taskListe;
    public TaskListActivity activity;

    // der Adapter speichert die Activity und die Liste in den oben aufgesetzten Variablen
    public TaskListAdapter(TaskListActivity activity, List<Task> list){
        this.activity = activity;
        taskListe = (ArrayList) list;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public LinearLayout linearLayout;


        public TaskViewHolder (View view) {
            super(view);

            taskName = (TextView) view.findViewById(R.id.taskNameTextView);
            linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutTaskListItem);

        }


    }


    //---------------------------------------------------------------------------------------------------------------------------------
    //                                                          WICHTIG!
    // kommt die Fehlermeldung: "Method does not override method from its superclass" fehlt beim initialisieren einer classe ein extend.
    // Beispielsweise: "extends RecyclerView.Adapter"
    //                                                          WICHTIG!
    //---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // aktiviert das Layout des TaskItems
        View view = this.activity.getLayoutInflater().inflate(R.layout.task_list_item, parent, false);
        return new TaskListAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, final int position){

        // holt aus der Taskliste ein neues Task opjekt
        final Task task = taskListe.get(position);

        // castet den holder in einen TaskViewHolder und speichert ihn in der Variable taskHolder
        TaskListAdapter.TaskViewHolder taskHolder = (TaskViewHolder) holder;

        taskHolder.taskName.setText(task.getName());



        // Hier kommt der spätere Onclick Listener hin, wenn auf den einzelnen Task geklickt wird, also die popup activity
        // in der ein Haken für erledigt oder ein x für zurück angeklickt werden können
        taskHolder.linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Hier wird festgelegt was passiert, wenn der Task angeklickt wird
            }
        });
    }





    @Override
    public int getItemCount() {
        return taskListe.size();
    }


}
