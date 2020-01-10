package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.common.reflect.Reflection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.List;

import Model.Task;
import UI.TaskRecycleViewAdapter;
import UI.TaskRecycleViewAdapter.ViewHolder;
import Util.TaskApi;

public class TaskList extends AppCompatActivity {



    private Button btnLogout;
    private Button btnCreateTask;

    private CardView cardView;
    private Intent editpage;
    private AlertDialog alertDialog;

    private String currentUserName;
    private String currentUserId;

    // Firebase attributes
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private static FirebaseFirestore database = FirebaseFirestore.getInstance();

    private StorageReference storageReference;
    private FirebaseStorage vStorage;
    private static Task tasks;

    private List<Task> taskList;
    private RecyclerView recyclerView;
    private TaskRecycleViewAdapter taskRecycleViewAdapter;

    private CollectionReference collectionReference = database.collection("Task");

    private TextView noTaskEntry;

    public static String TaskDocId = "";

    public ViewHolder viewHolder;

    public DocumentReference docRef;

    public int TaskPosition;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        vStorage = FirebaseStorage.getInstance();



        btnCreateTask = findViewById(R.id.btn_add_tasks);
        btnCreateTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(TaskList.this, CreateTask_activity.class));
                taskList.clear();
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

        if (TaskApi.getInstance() != null)
        {
            currentUserId = TaskApi.getInstance().getUserId();
            currentUserName = TaskApi.getInstance().getUsername();

            //ToDo add the lbl with current user name
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", TaskApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (QueryDocumentSnapshot tasks : queryDocumentSnapshots)
                            {
                                try
                                {
                                    Task task = tasks.toObject(Task.class);
                                    // save the id of the document
                                    task.setTaskDocumentId(tasks.getId());
                                    taskList.add(task);

                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(TaskList.this, "Can't load the data please restart your application", Toast.LENGTH_SHORT).show();
                                }
                            }

                            //invoke recycler view
                            taskRecycleViewAdapter = new TaskRecycleViewAdapter(TaskList.this, taskList, new TaskRecycleViewAdapter.TaskListener() {
                                @Override
                                public void onTaskComplete(int taskPosition)
                                {

                                    final Task tasks = taskList.get(taskPosition);
                                    docRef = database.collection("Task").document(tasks.getTaskDocumentId());

                                    alertDialog = new AlertDialog.Builder(TaskList.this)
                                            .setIcon(android.R.drawable.ic_menu_edit)
                                            .setTitle("Manage Your Task" + " " + TaskApi.getInstance().getUsername())
                                            .setMessage("Are you sure?")
                                            .setPositiveButton(Html.fromHtml("<font color = '#0083FF'> Update Task </font>"),
                                                    new DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which)
                                                        {
                                                            startActivity(new Intent(TaskList.this, Edit_activity.class));
                                                        }
                                                    })
                                            .setNegativeButton(Html.fromHtml("<font color = '#ff0000'> Delete Task </font>"),
                                                    new DialogInterface.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which)
                                                        {
                                                            try
                                                            {
                                                                taskList.remove(tasks);
                                                                StorageReference storageReference = vStorage.getReferenceFromUrl(tasks.getImageUrl());
                                                                storageReference.delete();
                                                                docRef.update(tasks.getDescription(), FieldValue.delete());
                                                                docRef.delete();

                                                                taskRecycleViewAdapter.notifyDataSetChanged();
                                                                taskRecycleViewAdapter.notifyItemRemoved(TaskPosition);
                                                                Toast.makeText(TaskList.this, currentUserName + " " + "Your task has been deleted", Toast.LENGTH_SHORT).show();
                                                            }
                                                            catch (Exception e)
                                                            {
                                                                Toast.makeText(TaskList.this, currentUserName + " " + "Please try again, internet disconnected", Toast.LENGTH_SHORT).show();
                                                            }


                                                        }
                                                    })
                                            .show();
                                }
                            });
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
