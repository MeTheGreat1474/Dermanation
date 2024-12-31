package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        LinearLayout helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Help.class));
        });

        LinearLayout feedbackButton = findViewById(R.id.feedbackButton);
        feedbackButton.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Report.class));
        });

        LinearLayout supportButton = findViewById(R.id.supportButton);
        supportButton.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, Support.class));
        });

        LinearLayout signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {

        });
    }
}
