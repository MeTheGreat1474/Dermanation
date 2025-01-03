package com.example.dermanation;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private List<Story> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LLDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView ivSupport = findViewById(R.id.IVSupport);
        ivSupport.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Help.class);
            startActivity(intent);
        });

        ImageView ivVolunteer = findViewById(R.id.IVVolunteer);
        ivVolunteer.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, VolunteerMain.class);
            startActivity(intent);
        });

//        ImageView ivDonate = findViewById(R.id.IVDonate);
//        ivDonate.setOnClickListener(v -> {
//            Intent intent = new Intent(Dashboard.this, DonationActivity.class);
//            startActivity(intent);
//        });


        stories = parseStoriesXml();

        LinearLayout lvStories = findViewById(R.id.LVStories);
        for (Story story : stories) {
            lvStories.addView(createCardView(story.getTitle(), story.getDescription(), story.getImageUrl(), story.getUrl()));
        }

        // Load BottomNavigationFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new BottomNavigationFragment());
        fragmentTransaction.commit();

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