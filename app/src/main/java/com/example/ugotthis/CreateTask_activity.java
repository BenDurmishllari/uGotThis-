package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import Util.TaskApi;
import Model.Task;

public class CreateTask_activity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "PostTaskActivity";

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
            case R.id.btnTaskSave:
                //ToDo add the method
                saveTask();

                break;
            case R.id.btnCameraPost:

                // post media file
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //anything that is image related
                startActivityForResult(galleryIntent, GALLERY_CODE);

                break;
        }
    }

    private void saveTask()
    {
        final String title = txtTaskTitle.getText().toString().trim();
        final String description = txtTaskDescription.getText().toString().trim();


        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri != null)
        {
            //save the file && make the file unique by timestamp
            final StorageReference filepath = storageReference.child("task_file").child("my_file" + Timestamp.now().getSeconds());

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            String imageUrl = uri.toString();

                            // create the Task object and collected by the collectionRef and save instances
                            Task task = new Task();
                            task.setTitle(title);
                            task.setDescription(description);
                            task.setUserName(currentUserName);
                            task.setUserId(currentUserId);
                            task.setImageUrl(imageUrl);
                            task.setTimeAdded(new Timestamp(new Date()));


                            collectionReference.add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                            {
                                @Override
                                public void onSuccess(DocumentReference documentReference)
                                {
                                    startActivity(new Intent(CreateTask_activity.this, TaskList.class));
                                    Toast.makeText(CreateTask_activity.this,  currentUserName + " " + "you have create new task", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    //ToDo add the progressbar
                }
            });

        }
        else
        {
            Toast.makeText(this, "oups", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onStart()
    {
        super.onStart();

        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
