package com.example.dermanation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    TextView user_name, user_email, user_phone_number, user_address, user_birth_date, user_gender, edit_profile, return_icon, receiver_status;

    Button btn_verify_receiver;

    FirebaseUser user;

    FirebaseAuth auth;

    DatabaseReference userRef;

    Boolean isReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        btn_verify_receiver = findViewById(R.id.verify_receiver);
        return_icon = findViewById(R.id.return_icon);
        edit_profile = findViewById(R.id.edit_profile);
        user_name = findViewById(R.id.user_name);
        receiver_status = findViewById(R.id.receiver_status);
        user_email = findViewById(R.id.user_email);
        user_birth_date = findViewById(R.id.user_birth_date);
        user_gender = findViewById(R.id.user_gender);
        user_phone_number = findViewById(R.id.user_phone_number);
        user_address = findViewById(R.id.user_address);

        return_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
                finish();
            }
        });

        btn_verify_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VerifyReceiver.class);
                startActivity(intent);
                finish();
            }
        });

        if (user==null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        } else {
            // Reference to the current user's data
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve user details
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        Boolean isReceiverValue = snapshot.child("applyReceiver").getValue(Boolean.class);
                        String receiverStatus = snapshot.child("receiverStatus").getValue(String.class);

                        // Handle other fields
                        String phoneNumber = snapshot.child("phone_number").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String birthDate = snapshot.child("birth_date").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);

                        // Assign to TextViews
                        user_name.setText(name != null ? name : "N/A");
                        user_email.setText(email != null ? email : "N/A");
                        if (isReceiverValue != null && isReceiverValue) {
                            receiver_status.setText(receiverStatus != null ? receiverStatus : "Unverified");
                        } else {
                            receiver_status.setText("Unappliccable");
                        }
//                        receiver_status.setText(isReceiverValue != null && isReceiverValue ? "Receiver" : "Not Receiver");
                        user_phone_number.setText(phoneNumber != null ? phoneNumber : "-");
                        user_address.setText(address != null ? address : "-");
                        user_birth_date.setText(birthDate != null ? birthDate : "-");
                        user_gender.setText(gender != null ? gender : "-");

                        // Show or hide the button based on isReceiverValue
                        if (isReceiverValue != null && isReceiverValue) {
                            btn_verify_receiver.setVisibility(View.VISIBLE); // Hide the button
                        } else {
                            btn_verify_receiver.setVisibility(View.GONE); // Show the button
                        }
                    } else {
                        Toast.makeText(ProfilePage.this, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfilePage.this, "Failed to fetch user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(ProfilePage.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_setting) {
                    startActivity(new Intent(ProfilePage.this, Setting.class));
                    return true;
                } else if (itemId == R.id.navigation_donate) {
                    startActivity(new Intent(ProfilePage.this, DonationActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(ProfilePage.this, ProfilePage.class));
                    return true;
                }
                return false;
            }
        });

    }
}