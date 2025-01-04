package com.example.dermanation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerDetails extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference userRef;

    String eventId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_details);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not signed in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView itemImageView = findViewById(R.id.itemImageView);
        TextView itemTitleTextView = findViewById(R.id.itemTitleTextView);
        TextView itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        TextView itemLocationTextView = findViewById(R.id.itemLocationTextView);
        TextView itemDateTextView = findViewById(R.id.itemDateTextView);

        String itemId = getIntent().getStringExtra("ITEM_ID");
        String itemTitle = getIntent().getStringExtra("ITEM_TITLE");
        String organizationName = getIntent().getStringExtra("ITEM_ORGANIZATION_NAME");
        int organizationIcon = getIntent().getIntExtra("ITEM_ORGANIZATION_ICON", 0);

        itemTitleTextView.setText(itemTitle);
        ((TextView) findViewById(R.id.organizationNameTextView)).setText(organizationName);
        ((ImageView) findViewById(R.id.iconView)).setImageResource(organizationIcon);

        Button goButton = findViewById(R.id.go_button);

        // for when coming from joined events section
        boolean hideGoButton = getIntent().getBooleanExtra("HIDE_GO_BUTTON", false);
        if (hideGoButton) {
            goButton.setVisibility(View.GONE);
        }

        eventId = itemId;
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        fetchUserName();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.child("name").getValue(String.class);
                    checkIfUserJoined(eventId, userName, goButton); // Check participation status
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerDetails.this, "Failed to fetch user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (itemId.equals("1")) {
            itemImageView.setImageResource(R.drawable.blood_donation_pic);
            itemDescriptionTextView.setText("Assist in organizing blood donation events.");
            itemLocationTextView.setText("Pusat Darah Negara\nKuala Lumpur");
            itemDateTextView.setText("Thu, Nov 9, 2025\n9:00 AM - 5:00 PM (UTC+8)");
        } else if (itemId.equals("2")) {
            itemImageView.setImageResource(R.drawable.dental_image);
            itemDescriptionTextView.setText("Assist dentists during community health check-ups.");
            itemLocationTextView.setText("DentLove Clinic\nKuala Lumpur");
            itemDateTextView.setText("Sat, Nov 11, 2025\n9:00 AM - 3:00 PM (UTC+8)");
        } else if (itemId.equals("3")) {
            itemImageView.setImageResource(R.drawable.tutor_image);
            itemDescriptionTextView.setText("Help students with their academic needs.");
            itemLocationTextView.setText("Teach for Malaysia\nOnline & In-Person");
            itemDateTextView.setText("Fri, Nov 10, 2025\n4:00 PM - 6:00 PM (UTC+8)");
        } else if (itemId.equals("4")) {
            itemImageView.setImageResource(R.drawable.mentor_image);
            itemDescriptionTextView.setText("Guide young individuals in achieving their goals.");
            itemLocationTextView.setText("Youth Development Center\nCommunity Hall");
            itemDateTextView.setText("Sun, Nov 12, 2025\n2:00 PM - 5:00 PM (UTC+8)");
        } else if (itemId.equals("5")) {
            itemImageView.setImageResource(R.drawable.library_image);
            itemDescriptionTextView.setText("Organize library shelves and assist visitors.");
            itemLocationTextView.setText("National Library\nCity Center");
            itemDateTextView.setText("Mon, Nov 13, 2025\n10:00 AM - 4:00 PM (UTC+8)");
        } else if (itemId.equals("6")) {
            itemImageView.setImageResource(R.drawable.online_tutor_image);
            itemDescriptionTextView.setText("Provide virtual math tutoring to students.");
            itemLocationTextView.setText("Virtual Learning Platform\nOnline");
            itemDateTextView.setText("Tue, Nov 14, 2025\n7:00 PM - 9:00 PM (UTC+8)");
        } else if (itemId.equals("7")) {
            itemImageView.setImageResource(R.drawable.beach_cleanup_image);
            itemDescriptionTextView.setText("Participate in cleaning the local beach.");
            itemLocationTextView.setText("Green Earth Initiative\nCity Beach");
            itemDateTextView.setText("Sat, Nov 18, 2025\n8:00 AM - 12:00 PM (UTC+8)");
        } else if (itemId.equals("8")) {
            itemImageView.setImageResource(R.drawable.tree_planting_image);
            itemDescriptionTextView.setText("Assist in planting trees for conservation.");
            itemLocationTextView.setText("Plant A Future\nCity Park");
            itemDateTextView.setText("Sun, Nov 19, 2025\n9:00 AM - 3:00 PM (UTC+8)");
        } else if (itemId.equals("9")) {
            itemImageView.setImageResource(R.drawable.soup_kitchen_image);
            itemDescriptionTextView.setText("Prepare and serve meals at a community kitchen.");
            itemLocationTextView.setText("Community Kitchen\nDowntown");
            itemDateTextView.setText("Wed, Nov 15, 2025\n11:00 AM - 2:00 PM (UTC+8)");
        } else if (itemId.equals("10")) {
            itemImageView.setImageResource(R.drawable.shelter_image);
            itemDescriptionTextView.setText("Assist with activities at a homeless shelter.");
            itemLocationTextView.setText("Care For All\nDowntown");
            itemDateTextView.setText("Thu, Nov 16, 2025\n9:00 AM - 5:00 PM (UTC+8)");
        }

        ImageView shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            shareEventDetails(itemTitle, itemLocationTextView, itemDateTextView);
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String activityName = itemTitleTextView.getText().toString();

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.volunteer_custom_alert_dialog, null);

                Dialog dialog = new Dialog(VolunteerDetails.this);
                dialog.setContentView(dialogView);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);

                TextView dialogueMessage = dialogView.findViewById(R.id.dialog_message);
                Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
                Button confirmButton = dialogView.findViewById(R.id.dialog_confirm_button);
                Button goButton = findViewById(R.id.go_button);

                // Determine if the article should be "a" or "an"
                String article = " a ";
                if (activityName.matches("^[AEIOUaeiou].*")) {
                    article = " an ";
                }

                dialogueMessage.setText("Do you want to confirm your participation as" + article + activityName + "?");

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        joinEvent(eventId, userName, goButton);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }
    private void fetchUserName() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.child("name").getValue(String.class);
                } else {
                    Toast.makeText(VolunteerDetails.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerDetails.this, "Failed to fetch user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfUserJoined(String eventId, String userName, Button goButton) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Volunteer").child(eventId);

        eventRef.child("hasJoined").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && Boolean.TRUE.equals(snapshot.getValue(Boolean.class))) {
                    // User has already joined
                    goButton.setEnabled(false);
                    goButton.setBackgroundTintList(ContextCompat.getColorStateList(VolunteerDetails.this, R.color.dark_gray));
                    goButton.setText("Joined");
                } else {
                    // User has not joined
                    goButton.setEnabled(true);
                    goButton.setBackgroundTintList(ContextCompat.getColorStateList(VolunteerDetails.this, R.color.pink));
                    goButton.setText("Go");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerDetails.this, "Failed to check participation status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void joinEvent(String eventId, String userName, Button goButton) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Volunteer").child(eventId);

        // Mark user as joined
        eventRef.child("hasJoined").child(userName).setValue(true).addOnSuccessListener(aVoid -> {
            incrementParticipationCount(eventId);

            // Update button state
            goButton.setEnabled(false);
            goButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_gray));
            goButton.setText("Joined");

            Toast.makeText(this, "You have successfully joined the event!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to join event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void incrementParticipationCount(String eventId) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("Volunteer").child(eventId);

        eventRef.child("participationCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long count = snapshot.getValue(Long.class);
                    eventRef.child("participationCount").setValue(count + 1);
                } else {
                    eventRef.child("participationCount").setValue(1L); // Initialize count if not present
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerDetails.this, "Failed to increment participation count: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void shareEventDetails(String activityName, TextView locationTextView, TextView dateTextView) {
        String shareContent = "Join us for " + activityName + "!\n"
                + "Location: " + locationTextView.getText().toString() + "\n"
                + "Date & Time: " + dateTextView.getText().toString() + "\n"
                + "For more details, visit: https://Dermanation.com";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        startActivity(Intent.createChooser(shareIntent, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
