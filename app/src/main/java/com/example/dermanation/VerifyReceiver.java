package com.example.dermanation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VerifyReceiver extends AppCompatActivity {

    TextView return_to_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_receiver);

        return_to_profile = findViewById(R.id.return_to_profile);

        // Redirect to ProfilePage if cancel is clicked
        return_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}