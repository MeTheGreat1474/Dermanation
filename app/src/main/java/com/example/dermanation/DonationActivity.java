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
//                "Help Palestine",
//                R.drawable.img_6,
//                5000,
//                50,
//                R.drawable.img_7,
//                "Peduli Insan",
//                "Tabung Kecemasan Untuk Rakyat Palestine" +
//                        "Assalammualaikum dan Salam Sejahtera," +
//                        "Ayuh semua, sokongan dan sumbangan kita bukan sahaja menyelamatkan mereka, malah menjadikan musim sejuk mereka, musim yang sangat indah." +
//                        "wKita mungkin belum mampu untuk menyelesaikan krisis yang berlaku di sana. Sekurang-kurangnya, kehidupan mereka hari ini, adalah lebih baik daripada yang sebelumnya."
//        );
//
//        DonationRepository repository = new DonationRepository();
//        repository.addDonation(donation);
    }
}
