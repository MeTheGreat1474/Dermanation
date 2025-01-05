package com.example.dermanation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityPost extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button createPostButton = findViewById(R.id.CreatePostButton);
        EditText titleText = findViewById(R.id.TitleText);
        EditText contentText = findViewById(R.id.ContentText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = snapshot.child("name").getValue(String.class);
                    } else {
                        Toast.makeText(CommunityPost.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CommunityPost.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
        }

        createPostButton.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());

            if (!title.isEmpty() && !content.isEmpty()) {
                addNewPost(new Post(userName, title, content, date));
                finish();
            }
            else {
                Toast.makeText(CommunityPost.this, "Please fill the text boxes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("Posts");

        postsRef.push().setValue(post);
    }
}