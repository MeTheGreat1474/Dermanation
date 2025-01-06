package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Setting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

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

        LinearLayout accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(v -> {
            startActivity(new Intent(Setting.this, ProfilePage.class));
        });

        LinearLayout signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(Setting.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView usernameTV = findViewById(R.id.usernameTV);
        TextView emailTV = findViewById(R.id.emailTV);

        if (currentUser != null) {
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        usernameTV.setText(name != null ? name : "Name not available");
                        emailTV.setText(email != null ? email : "Email not available");
                    } else {
                        Toast.makeText(Setting.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Setting.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    Log.e("Setting", "Database error: " + error.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_setting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(Setting.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_setting) {
                    startActivity(new Intent(Setting.this, Setting.class));
                    return true;
                } else if (itemId == R.id.navigation_donate) {
                    startActivity(new Intent(Setting.this, DonationActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(Setting.this, ProfilePage.class));
                    return true;
                } else if (itemId == R.id.navigation_community) {
                    startActivity(new Intent(Setting.this, Community.class));
                    return true;
                }
                return false;
            }
        });

    }
}
