package com.example.dermanation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

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
//                R.drawable.img_9,
//                2000,
//                20,
//                R.drawable.img_10,
//                "Peduli Insan",
//                "Care For Malaysia is a charity organization, your generous donations enable the Care For Malaysia Center to cover monthly living and operational expenses. Your kind support can help to cover the monthly expenses of Care for Malaysia Society "
//        );
//
//        DonationRepository repository = new DonationRepository();
//        repository.addDonation(donation);
    }
}
