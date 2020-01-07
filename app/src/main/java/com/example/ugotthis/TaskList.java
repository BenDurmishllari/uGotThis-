package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.Reflection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Model.Task;
import UI.TaskRecycleViewAdapter;
import Util.TaskApi;

public class TaskList extends AppCompatActivity {



//    ListView itemlist;
//    private ListView listview;
//    private ArrayList<String> arraylist;
//    private ArrayAdapter adapter;
//    Intent editpage;

    private Button btnLogout;
    private Button btnCreateTask;

    // Firebase attributes
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private List<Task> taskList;
    private RecyclerView recyclerView;
    private TaskRecycleViewAdapter taskRecycleViewAdapter;

    private CollectionReference collectionReference = database.collection("Task");
    private TextView noTaskEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        btnCreateTask = findViewById(R.id.btn_add_tasks);
        btnCreateTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(TaskList.this, CreateTask_activity.class));
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TaskList.this, LogIn_activity.class));
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        taskList = new ArrayList<>();
        noTaskEntry = findViewById(R.id.list_for_Tasks_View);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        listview = findViewById(R.id.item_list);
//        arraylist = new ArrayList<String>();
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                final int current_item = position;
//                new AlertDialog.Builder(TaskList.this)
//                                        .setIcon(android.R.drawable.ic_menu_edit)
//                                        .setTitle("Do to you edit this?")
//                                        .setMessage("Are you sure")
//                                        .setPositiveButton(Html.fromHtml("<font color = '#0083FF'> Edit </font>"),
//                                                            new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which)
//                                            {
//                                                editpage = new Intent(getApplicationContext(), Edit_activity.class);
//                                                startActivity(editpage);
//                                            }
//                                        })
//                                        .setNegativeButton(Html.fromHtml("<font color = '#ff0000'> Delete </font>") , null)
//                                        .show();
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", TaskApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot tasks : queryDocumentSnapshots){
                                Task task = tasks.toObject(Task.class);
                                taskList.add(task);
                            }

                            //invoke recyler view
                            taskRecycleViewAdapter = new TaskRecycleViewAdapter(TaskList.this, taskList);
                            recyclerView.setAdapter(taskRecycleViewAdapter);
                            taskRecycleViewAdapter.notifyDataSetChanged();
                        }
                        else{
                            noTaskEntry.setVisibility(View.VISIBLE);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
