package com.example.dermanation;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, profile_page;
    TextView textView;
    FirebaseUser user;
    DatabaseReference userRef;

    private List<Story> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        TextView tvName = findViewById(R.id.TVName);

        if (user==null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        } else {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve user name
                        String name = snapshot.child("name").getValue(String.class);
                        tvName.setText(name != null ? name : "N/A");
                    } else {
                        Toast.makeText(MainActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to fetch user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        ImageView ivSupport = findViewById(R.id.IVSupport);
        ivSupport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Help.class);
            startActivity(intent);
        });

        ImageView ivVolunteer = findViewById(R.id.IVVolunteer);
        ivVolunteer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VolunteerMain.class);
            startActivity(intent);
        });

        ImageView ivProfile = findViewById(R.id.IVProfile);
        ivProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfilePage.class);
            startActivity(intent);
        });

        ImageView ivDonate = findViewById(R.id.IVDonate);
        ivDonate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DonationActivity.class);
            startActivity(intent);
        });


        stories = parseStoriesXml();

        LinearLayout lvStories = findViewById(R.id.LVStories);
        for (Story story : stories) {
            lvStories.addView(createCardView(story.getTitle(), story.getDescription(), story.getImageUrl(), story.getUrl()));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_setting) {
                    startActivity(new Intent(MainActivity.this, Setting.class));
                    return true;
                } else if (itemId == R.id.navigation_donate) {
                    startActivity(new Intent(MainActivity.this, DonationActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(MainActivity.this, ProfilePage.class));
                    return true;
                }
                return false;
            }
        });
    }

    private CardView createCardView(String title, String description, String imageUrl, String url) {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView cardView = (CardView) inflater.inflate(R.layout.card_view_layout, null, false);

        TextView tvTitle = cardView.findViewById(R.id.TVCardTitle);
        TextView tvDescription = cardView.findViewById(R.id.TVCardDesc);
        ImageView imgCard = cardView.findViewById(R.id.IMGCard);

        tvTitle.setText(title);
        tvDescription.setText(description);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        );
        layoutParams.setMargins(0, 0, 0, 20);
        cardView.setLayoutParams(layoutParams);

        imgCard.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int imageResource = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
        imgCard.setImageResource(imageResource);

        LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                300,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        imgCard.setLayoutParams(imgLayoutParams);

        // opening chrome cause crashes idk why
        cardView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        return cardView;
    }

    private List<Story> parseStoriesXml() {
        List<Story> stories = new ArrayList<>();
        try {
            XmlResourceParser parser = getResources().getXml(R.xml.stories);
            int eventType = parser.getEventType();
            Story currentStory = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("story")) {
                            currentStory = new Story();
                        } else if (currentStory != null) {
                            if (name.equals("title")) {
                                currentStory.setTitle(parser.nextText());
                            } else if (name.equals("description")) {
                                currentStory.setDescription(parser.nextText());
                            } else if (name.equals("imageUrl")) {
                                currentStory.setImageUrl(parser.nextText());
                            } else if (name.equals("url")) {
                                currentStory.setUrl(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("story") && currentStory != null) {
                            stories.add(currentStory);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stories;
    }
}