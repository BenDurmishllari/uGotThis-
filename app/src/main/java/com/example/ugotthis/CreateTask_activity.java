package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Util.TaskApi;

public class CreateTask_activity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "PostReflectionActivity";

    // main widgets
    private Button btnSaveTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_activity);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        txtTaskTitle = findViewById(R.id.txtTaskTitle);
        txtTaskDescription = findViewById(R.id.txtTaskDescription);
        btnSaveTask = findViewById(R.id.btnTaskSave);
        //ToDo current user label
        //lblCurrentUser = findViewById(R.id.)

        btnAddMedia = findViewById(R.id.btnCameraPost);

        //ToDo add image view
        imageView = findViewById(R.id.lblImageView);

        btnSaveTask.setOnClickListener(this);
        btnAddMedia.setOnClickListener(this);

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
                    //ToDo add a sanduic :D
                }
            }
        };


    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnTaskSave:
                //ToDo add the method
                saveTask();

                break;
            case R.id.btnCameraPost:

                // post media file
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //anything that is image related
                galleryIntent.setType("video/*");
                galleryIntent.setType("general files/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);

                break;
        }
    }

    private void saveTask()
    {
        final String title = txtTaskTitle.getText().toString().trim();
        final String description = txtTaskDescription.getText().toString().trim();


    }
}
