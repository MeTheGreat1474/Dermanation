package com.example.dermanation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class VolunteerJoinedEvents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyStateMessage;
    private VolunteerJoinedEventsAdapter adapter;
    private List<VolunteerItem> joinedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteer_joined_events);

        Toolbar toolbar1 = findViewById(R.id.toolbarJoined);
        setSupportActionBar(toolbar1);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerViewJoinedEvents);
        emptyStateMessage = findViewById(R.id.emptyStateMessage);

        joinedEvents = new ArrayList<>();
        adapter = new VolunteerJoinedEventsAdapter(joinedEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchJoinedEvents();
    }

    private void fetchJoinedEvents() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference volunteerRef = FirebaseDatabase.getInstance().getReference("Volunteer");

        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String loggedInUser = snapshot.getValue(String.class);

                    if (loggedInUser == null || loggedInUser.isEmpty()) {
                        Toast.makeText(VolunteerJoinedEvents.this, "User name not found!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    volunteerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            joinedEvents.clear();

                            for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                                String eventId = eventSnapshot.getKey();

                                DataSnapshot hasJoinedSnapshot = eventSnapshot.child("hasJoined").child(loggedInUser);
                                Boolean hasJoined = hasJoinedSnapshot.getValue(Boolean.class);

                                if (hasJoined != null && hasJoined) {
                                    VolunteerItem volunteerItem = null;

                                    if (eventId.equals("1")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Blood Donation Assistant",
                                                R.drawable.blood_donation_pic,
                                                R.drawable.blood_donation_icon,
                                                "Pusat Darah Negara",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("2")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Dental Assistant",
                                                R.drawable.dental_image,
                                                R.drawable.dental_icon,
                                                "DentaLove Clinic",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("3")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Secondary Tutor",
                                                R.drawable.tutor_image,
                                                R.drawable.tutor_icon,
                                                "Teach for Malaysia",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("4")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Mentor",
                                                R.drawable.mentor_image,
                                                R.drawable.mentor_icon,
                                                "Youth Development Center",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("5")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Library Organizer",
                                                R.drawable.library_image,
                                                R.drawable.library_icon,
                                                "National Library",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("6")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Online Math Tutor",
                                                R.drawable.online_tutor_image,
                                                R.drawable.tutorv2_icon,
                                                "Virtual Learning Platform",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("7")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Beach Cleanup Volunteer",
                                                R.drawable.beach_cleanup_image,
                                                R.drawable.environment_icon,
                                                "Green Earth Initiative",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("8")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Tree Planting Volunteer",
                                                R.drawable.tree_planting_image,
                                                R.drawable.tree_icon,
                                                "Plant A Future",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("9")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Soup Kitchen Helper",
                                                R.drawable.soup_kitchen_image,
                                                R.drawable.soup_icon,
                                                "Community Kitchen",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    } else if (eventId.equals("10")) {
                                        volunteerItem = new VolunteerItem(
                                                eventId,
                                                "Homeless Shelter Assistant",
                                                R.drawable.shelter_image,
                                                R.drawable.shelter_icon,
                                                "Care For All",
                                                eventSnapshot.child("participationCount").getValue(Integer.class)
                                        );
                                    }

                                    if (volunteerItem != null) {
                                        joinedEvents.add(volunteerItem);
                                    }
                                }
                            }

                            if (joinedEvents.isEmpty()) {
                                recyclerView.setVisibility(View.GONE);
                                emptyStateMessage.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyStateMessage.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(VolunteerJoinedEvents.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(VolunteerJoinedEvents.this, "User name not found in database!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerJoinedEvents.this, "Failed to fetch user name: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
