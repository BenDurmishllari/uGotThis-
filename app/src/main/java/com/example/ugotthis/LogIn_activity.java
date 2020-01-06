package com.example.ugotthis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Util.TaskApi;

public class LogIn_activity extends AppCompatActivity {

    Intent register;
    Intent taskList;
    TextView signUp;

    Button btnLogin;

    private EditText txtEmail;
    private EditText txtPassword;

    // Firebase attributes
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    // database collection
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    // animation initialise layout
    RelativeLayout rellativeLayout1, rellativeLayout2;
    Handler hand = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run()
        {
            rellativeLayout1.setVisibility(View.VISIBLE);
            rellativeLayout2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // define by id the layout
        rellativeLayout1 = findViewById(R.id.relative1_1);
        rellativeLayout2 = findViewById(R.id.relative2);

        // 2000 is the timeout for splash
        hand.postDelayed(run,2000);

        signUp = findViewById(R.id.linkbtnsignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                register = new Intent(getApplicationContext(), Register_activity.class);
                startActivity(register);
            }
        });

        // instantiate the firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnlogin);
        txtEmail = findViewById(R.id.txtLoginEmail);
        txtPassword = findViewById(R.id.txtLoginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //ToDo add login method
                loginEmailPasswordUser(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());
            }
        });

    }

    private void loginEmailPasswordUser (String email, String password)
    {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    String currentUserId = user.getUid();

                    collectionReference.whereEqualTo("userId", currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>()
                    {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
                        {
                            if (e != null)
                            {
                                Toast.makeText(LogIn_activity.this, "hiiiii", Toast.LENGTH_SHORT).show();
                            }

                            assert queryDocumentSnapshots != null;
                            if (!queryDocumentSnapshots.isEmpty())
                            {
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                                {
                                    TaskApi taskApi = TaskApi.getInstance();
                                    taskApi.setUsername(snapshot.getString("username"));
                                    taskApi.setUserId(snapshot.getString("userId"));

                                    startActivity(new Intent(LogIn_activity.this, TaskList.class));
                                    Toast.makeText(LogIn_activity.this, "Welcome"+ " " + taskApi.getUsername(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    //ToDo add progress bar in the end
                    Toast.makeText(LogIn_activity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please enter Email and Password to Login", Toast.LENGTH_SHORT).show();
        }
    }
}
