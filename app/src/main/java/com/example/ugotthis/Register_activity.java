package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Util.TaskApi;

public class Register_activity extends AppCompatActivity {

    // Buttons and intents
    TextView gobackhome;
    Intent loginpage;
    Button btnRegister;
    Intent loginhome;

    // Firebase attributes
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // Firestore connection
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    // database collection
    private CollectionReference collectionReference = database.collection("Users");

    // txtBoxes for data collection to realise the create account
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Button Link to redirect on home/login page
        gobackhome = findViewById(R.id.linkbtngohomepage);
        gobackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginpage = new Intent(getApplicationContext(), LogIn_activity.class);
                startActivity(loginpage);
            }
        });


        // instantiate the firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // instantiate  widgets
        btnRegister = findViewById(R.id.registerbtn);
        txtUsername = findViewById(R.id.txtCreateAccountUsername);
        txtEmail = findViewById(R.id.txtCreateAccountEmail);
        txtPassword = findViewById(R.id.txtCreateAccountPassword);

        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null)
                {
                    Toast.makeText(Register_activity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Register_activity.this, "Log in First", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!TextUtils.isEmpty(txtUsername.getText().toString()) &&
                        !TextUtils.isEmpty(txtEmail.getText().toString()) &&
                        !TextUtils.isEmpty(txtPassword.getText().toString()))
                {
                    String username = txtUsername.getText().toString().trim();
                    String email = txtEmail.getText().toString().trim();
                    String password = txtPassword.getText().toString().trim();

                    //ToDo add the method for create user account
                    createUserAccount(username,email,password);
                }
                else
                {
                    Toast.makeText(Register_activity.this, "Please fill the empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void createUserAccount (final String username, String email, String password)
    {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        // get current user attributes from firebase
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserId = currentUser.getUid();

                        // create a user map to add to user collection
                        Map<String, String> userObject = new HashMap<>();
                        userObject.put("userId", currentUserId);
                        userObject.put("username", username);

                        collectionReference.add(userObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                    {
                                        if (Objects.requireNonNull(task.getResult()).exists())
                                        {
                                            String name = task.getResult().getString("username");

                                            TaskApi taskApi = TaskApi.getInstance(); // global API
                                            taskApi.setUserId(currentUserId);
                                            taskApi.setUsername(name);

                                            Intent register = new Intent(Register_activity.this, TaskList.class);
                                            register.putExtra("username", name);
                                            register.putExtra("userId", currentUserId);

                                            startActivity(register);
                                        }
                                        else
                                        {
                                            //ToDo add progress bar in the end
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(Register_activity.this, "Something went wrong 1", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Register_activity.this, "Something went wrong 2", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(Register_activity.this, "Something went wrong 3", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
