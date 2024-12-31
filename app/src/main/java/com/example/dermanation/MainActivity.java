package com.example.dermanation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.text.LoginFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button button, profile_page;

    TextView textView;

    FirebaseUser user;

    private List<Story> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
//        profile_page = findViewById(R.id.profile_page);
        user = auth.getCurrentUser();

//        if (user==null) {
//            Intent intent = new Intent(getApplicationContext(), SignIn.class);
//            startActivity(intent);
//            finish();
//        } else {
//            textView.setText(user.getEmail());
//        }
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), SignIn.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        profile_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
//                startActivity(intent);
//                finish();
//            }
//        });

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