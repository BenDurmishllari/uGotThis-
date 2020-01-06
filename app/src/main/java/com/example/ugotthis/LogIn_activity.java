package com.example.ugotthis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LogIn_activity extends AppCompatActivity {

    Intent register;
    Intent taskList;
    TextView signUp;
    Button login;

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

        login = findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskList = new Intent(getApplicationContext(), TaskList.class);
                startActivity(taskList);
            }
        });

    }
}
