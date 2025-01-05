package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Community extends AppCompatActivity {

    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>();

    private String filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Spinner stuff
        Spinner spinner = findViewById(R.id.Filter);
        String[] items = new String[]{"Newest", "Oldest"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = parent.getItemAtPosition(position).toString();

                if (filter.equals("Newest")) {
                    Collections.reverse(postList);
                }
                if (filter.equals("Oldest")) {
                    Collections.reverse(postList);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Bring to volunteer page button
        Button volunteerButton = findViewById(R.id.VolunteerButton);

        volunteerButton.setOnClickListener(v -> {
            startActivity(new Intent(Community.this, VolunteerMain.class));
        });

        // New Post Button
        Button newPostButton = findViewById(R.id.NewPostButton);

        newPostButton.setOnClickListener(v -> {
            startActivity(new Intent(Community.this, CommunityPost.class));
        });


        /*
        RECYCLERVIEW STUFF
         */

        RecyclerView postRecyclerView = findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        postRecyclerView.setAdapter(postAdapter);

        // Get a reference to the "posts" node in your Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("Posts");


        // Attach a listener to read data from the database
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                // Get the data as a Post object
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Get the data as a Post object
                    Post post = postSnapshot.getValue(Post.class);
                    // Add the Post object to the postList
                    if (postList != null)
                        postList.add(post);
                }
                Collections.reverse(postList);

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}