package com.example.ugotthis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register_activity extends AppCompatActivity {

    TextView gobackhome;
    Intent loginpage;
    Button btnloginpage;
    Intent loginhome;

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

        btnloginpage = findViewById(R.id.registerbtn);

        btnloginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginhome = new Intent(getApplicationContext(), LogIn_activity.class);
                startActivity(loginhome);
            }
        });
    }
}
