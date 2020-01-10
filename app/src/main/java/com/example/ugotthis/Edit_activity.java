package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Task;
import UI.TaskRecycleViewAdapter;
import Util.TaskApi;


public class Edit_activity extends AppCompatActivity  implements  View.OnClickListener{

    private static final int GALLERY_CODE = 2;
    private static final String TAG = "PostTaskActivity";

    private Button btnTaskUpdate;
    private ImageView btnAddMedia;
    private EditText txtTaskTitle;
    private EditText txtTaskDescription;
    private TextView lblCurrentUser;
    private TextView TaskDate;
    private ImageView imageView;

    private String currentUserId;
    private String currentUserName;

    // firebase attributes
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = database.collection("Task");
    private Uri imageUri;

    private DocumentReference docRef;

    private List<Task> taskList;

    private RecyclerView recyclerView;
    private TaskRecycleViewAdapter taskRecycleViewAdapter;

    private AlertDialog alertDialog;

    public int TaskPosition;

    private TextView noTaskEntry;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);



        storageReference = FirebaseStorage.getInstance().getReference();

        imageView = findViewById(R.id.lblImageView2);

        firebaseAuth = FirebaseAuth.getInstance();
        txtTaskTitle = findViewById(R.id.txtTaskTitle);
        txtTaskDescription = findViewById(R.id.txtTaskDescription);
        btnTaskUpdate = findViewById(R.id.btnTaskUpdate);

        btnAddMedia = findViewById(R.id.btnCameraPost2);

        btnAddMedia.setOnClickListener(this);
        btnTaskUpdate.setOnClickListener(this);

        taskList = new ArrayList<>();

        noTaskEntry = findViewById(R.id.list_for_Tasks_View);

        recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (TaskApi.getInstance() != null)
        {
            currentUserId = TaskApi.getInstance().getUserId();
            currentUserName = TaskApi.getInstance().getUsername();

            //ToDo add the lbl with current user name
        }

        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    //ToDo add a toas
                }
                else
                {
                    //ToDo add a hot dog :D
                }
            }
        };




    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnTaskUpdate:
                //ToDo add the method
                updateTask();
                break;

            case R.id.btnCameraPost2:

                // post media file
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //anything that is image related
                startActivityForResult(galleryIntent, GALLERY_CODE);

                break;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void updateTask()
    {
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
                                    Toast.makeText(Edit_activity.this, "Can't load the data please restart your application", Toast.LENGTH_SHORT).show();
                                }
                            }

                            final Task tasks = taskList.get(TaskPosition);
                            final String title = txtTaskTitle.getText().toString().trim();
                            final String desc = txtTaskDescription.getText().toString().trim();

                            docRef = database.collection("Task").document(tasks.getTaskDocumentId());
//                            docRef.update("title", title);
//                            docRef.update("description", desc);
//                            startActivity(new Intent(Edit_activity.this, TaskList.class));

                            Map<String, Object> upMap = new HashMap<>();
                            upMap.put("title", title);
                            upMap.put("description", desc);

                            docRef.update(upMap).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(Edit_activity.this, currentUserName + " " + "Your task has been updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Edit_activity.this, TaskList.class));
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(Edit_activity.this, currentUserName + " " + "Please try again, internet disconnected", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Edit_activity.this, TaskList.class));
                                }
                            });


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

}

//        Task ts = new Task();
//
//        docRef = database.collection("Task").document(ts.getTaskDocumentId());
//
//        final String uTitle = txtTaskTitle.getText().toString().trim();
//        final String uDesc = txtTaskDescription.getText().toString().trim();
//
//
//        ts.setTitle(uTitle);
//        ts.setDescription(uDesc);
//
//
//        docRef.update("Task", ts).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(Edit_activity.this, "oeo", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Edit_activity.this, "paparia", Toast.LENGTH_SHORT).show();
//            }
//        });
