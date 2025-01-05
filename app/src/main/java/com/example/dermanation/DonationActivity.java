package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class DonationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_activity);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        DonationFirebase donation = new DonationFirebase(
//                null,
//                "Help Welfare House",
//                "donation_img_9",
//                2000,
//                20,
//                "donation_img_10",
//                "Peduli Insan",
//                "Care For Malaysia is a charity organization, your generous donations enable the Care For Malaysia Center to cover monthly living and operational expenses. Your kind support can help to cover the monthly expenses of Care for Malaysia Society "
//        );
//
//        DonationRepository repository = new DonationRepository();
//        repository.addDonation(donation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_donate);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(DonationActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_setting) {
                    startActivity(new Intent(DonationActivity.this, Setting.class));
                    return true;
                } else if (itemId == R.id.navigation_donate) {
                    startActivity(new Intent(DonationActivity.this, DonationActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(DonationActivity.this, ProfilePage.class));
                    return true;
                }  else if (itemId == R.id.navigation_community) {
                    startActivity(new Intent(DonationActivity.this, Community.class));
                    return true;
                }
                return false;
            }
        });

    }
}
