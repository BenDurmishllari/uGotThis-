package com.example.ugotthis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_activity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        gobackhome = findViewById(R.id.linkbtngohomepage);

        gobackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginpage = new Intent(getApplicationContext(), LogIn_activity.class);
                startActivity(loginpage);
            }
        });

        btnRegister = findViewById(R.id.registerbtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginhome = new Intent(getApplicationContext(), LogIn_activity.class);
                startActivity(loginhome);
            }
        });
    }
}
