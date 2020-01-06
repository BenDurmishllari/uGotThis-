package com.example.ugotthis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;

public class TaskList extends AppCompatActivity {



    ListView itemlist;
    private ListView listview;
    private ArrayList<String> arraylist;
    private ArrayAdapter adapter;
    Intent editpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);


        listview = findViewById(R.id.item_list);
        arraylist = new ArrayList<String>();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int current_item = position;
                new AlertDialog.Builder(TaskList.this)
                                        .setIcon(android.R.drawable.ic_menu_edit)
                                        .setTitle("Do to you edit this?")
                                        .setMessage("Are you sure")
                                        .setPositiveButton(Html.fromHtml("<font color = '#0083FF'> Edit </font>"),
                                                            new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                editpage = new Intent(getApplicationContext(), Edit_activity.class);
                                                startActivity(editpage);
                                            }
                                        })
                                        .setNegativeButton(Html.fromHtml("<font color = '#ff0000'> Delete </font>") , null)
                                        .show();
            }
        });

    }


}
