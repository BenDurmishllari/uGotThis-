package com.example.ugotthis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Edit_activity extends AppCompatActivity {

    Button btnupdate;
    Intent backtolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        btnupdate = findViewById(R.id.btnupdate);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                backtolist = new Intent(getApplicationContext(), TaskList.class);
                startActivity(backtolist);
            }
        });

    }
}
