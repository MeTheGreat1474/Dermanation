package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class VolunteerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_main);

        Toolbar toolbar = findViewById(R.id.toolbarVolunteerMain);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Button buttonJoined = findViewById(R.id.joinedButton);
        buttonJoined.setOnClickListener(v -> {
            Intent intent = new Intent(VolunteerMain.this, VolunteerJoinedEvents.class);
            startActivity(intent);
        });

        List<VolunteerItem> healthItems = Arrays.asList(
                new VolunteerItem("1", "Blood Donation Assistant", R.drawable.blood_donation_pic, R.drawable.blood_donation_icon, "Pusat Darah Negara", 0),
                new VolunteerItem("2", "Dental Assistant", R.drawable.dental_image, R.drawable.dental_icon, "DentaLove Clinic", 0)
        );

        List<VolunteerItem> youthItems = Arrays.asList(
                new VolunteerItem("3", "Secondary Tutor", R.drawable.tutor_image, R.drawable.tutor_icon, "Teach for Malaysia", 0),
                new VolunteerItem("4", "Mentor", R.drawable.mentor_image, R.drawable.mentor_icon, "Youth Development Center", 0)
        );

        List<VolunteerItem> educationItems = Arrays.asList(
                new VolunteerItem("5", "Library Organizer", R.drawable.library_image, R.drawable.library_icon, "National Library", 0),
                new VolunteerItem("6", "Online Math Tutor", R.drawable.online_tutor_image, R.drawable.tutorv2_icon, "Virtual Learning Platform", 0)
        );

        List<VolunteerItem> environmentItems = Arrays.asList(
                new VolunteerItem("7", "Beach Cleanup Volunteer", R.drawable.beach_cleanup_image, R.drawable.environment_icon, "Green Earth Initiative", 0),
                new VolunteerItem("8", "Tree Planting Volunteer", R.drawable.tree_planting_image, R.drawable.tree_icon, "Plant A Future", 0)
        );

        List<VolunteerItem> communityItems = Arrays.asList(
                new VolunteerItem("9", "Soup Kitchen Helper", R.drawable.soup_kitchen_image, R.drawable.soup_icon, "Community Kitchen", 0),
                new VolunteerItem("10", "Homeless Shelter Assistant", R.drawable.shelter_image, R.drawable.shelter_icon, "Care For All", 0)
        );

        List<VolunteerCategory> categories = Arrays.asList(
                new VolunteerCategory("Health", healthItems),
                new VolunteerCategory("Youth and Child Services", youthItems),
                new VolunteerCategory("Education", educationItems),
                new VolunteerCategory("Environment", environmentItems),
                new VolunteerCategory("Community Service", communityItems)
        );

        RecyclerView verticalRecyclerView = findViewById(R.id.verticalRecyclerView);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        VolunteerVerticalAdapter verticalAdapter = new VolunteerVerticalAdapter(this, categories);
        verticalRecyclerView.setAdapter(verticalAdapter);

        DatabaseReference volunteerRef = FirebaseDatabase.getInstance().getReference("Volunteer");

        volunteerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (VolunteerItem item : healthItems) {
                        String id = item.getId();
                        if (snapshot.child(id).child("participationCount").exists()) {
                            int count = snapshot.child(id).child("participationCount").getValue(Integer.class);
                            item.setParticipantCount(count);
                        }
                    }

                    for (VolunteerItem item : youthItems) {
                        String id = item.getId();
                        if (snapshot.child(id).child("participationCount").exists()) {
                            int count = snapshot.child(id).child("participationCount").getValue(Integer.class);
                            item.setParticipantCount(count);
                        }
                    }

                    for (VolunteerItem item : educationItems) {
                        String id = item.getId();
                        if (snapshot.child(id).child("participationCount").exists()) {
                            int count = snapshot.child(id).child("participationCount").getValue(Integer.class);
                            item.setParticipantCount(count);
                        }
                    }

                    for (VolunteerItem item : environmentItems) {
                        String id = item.getId();
                        if (snapshot.child(id).child("participationCount").exists()) {
                            int count = snapshot.child(id).child("participationCount").getValue(Integer.class);
                            item.setParticipantCount(count);
                        }
                    }

                    for (VolunteerItem item : communityItems) {
                        String id = item.getId();
                        if (snapshot.child(id).child("participationCount").exists()) {
                            int count = snapshot.child(id).child("participationCount").getValue(Integer.class);
                            item.setParticipantCount(count);
                        }
                    }

                    verticalAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(VolunteerMain.this, "No data found in Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerMain.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
