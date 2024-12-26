package com.example.dermanation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

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

        LinearLayout lvStories = findViewById(R.id.LVStories);
        for (int i = 0; i < 10; i++) { // Add 10 CardViews for example
            lvStories.addView(createCardView("Title " + (i + 1), "Description " + (i + 1)));
        }
    }

    private CardView createCardView(String title, String description) {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView cardView = (CardView) inflater.inflate(R.layout.card_view_layout, null, false);

        TextView tvTitle = cardView.findViewById(R.id.TVCardTitle);
        TextView tvDescription = cardView.findViewById(R.id.TVCardDesc);
        ImageView imgCard = cardView.findViewById(R.id.IMGCard);

        tvTitle.setText(title);
        tvDescription.setText(description);
        imgCard.setImageResource(R.drawable.dream); // Set your image resource

        // Set bottom margin
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 20); // 16dp bottom margin
        cardView.setLayoutParams(layoutParams);

        return cardView;
    }
}