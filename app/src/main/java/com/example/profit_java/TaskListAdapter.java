package com.example.profit_java;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
        public ImageView taskImage;
        public LinearLayout linearLayout;


        public TaskViewHolder (View view) {
            super(view);

            taskName = (TextView) view.findViewById(R.id.taskNameTextView);
            taskImage = (ImageView) view.findViewById(R.id.taskImage);
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


        if (task.getTaskImage() != null){
            taskHolder.taskImage.setImageBitmap(Bitmap.createScaledBitmap(task.getTaskImage(), 40, 40, false));

        }else{
            Bitmap standartImage = Util.getBitmapFromDrawable(activity, R.drawable.kreuz);
            task.setTaskImage(standartImage);

            taskHolder.taskImage.setImageBitmap(Bitmap.createScaledBitmap(standartImage, 40, 40, false));

        }



        // Hier kommt der spätere Onclick Listener hin, wenn auf den einzelnen Task geklickt wird, also die popup activity
        // in der ein Haken für erledigt oder ein x für zurück angeklickt werden können
        taskHolder.linearLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Hier wird festgelegt was passiert, wenn der Task angeklickt wird
                Intent i = new Intent(view.getContext(), TaskReaction.class);

                // hier werden Daten aus der aktuellen Aktivity, an die Zielactivity mit übergeben
                i.putExtra("taskNameTest", taskListe.get(position).getName());
                i.putExtra("taskID", taskListe.get(position).getTaskID());
                // es können aber nur sehr kleine Daten mitgegeben werden, große Strings, wie Beispielsweise Bitmaps, gehen nicht
                String bitmapString = Util.getBase64StringFromBitmap(task.getTaskImage());
                // daher muss der Base64 String des TaskBildes in den Shared Prefs gespeichert werden
                // dazu wird die gerade angelegte Variable bitmapString in die shared Pref "taskImageString" gespeichert
                PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("taskImageString", bitmapString).apply();


                view.getContext().startActivity(i);

            }
        });
    }







    @Override
    public int getItemCount() {
        return taskListe.size();
    }


}
