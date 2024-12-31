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
//                "Help Flood Victim",
//                R.drawable.img,
//                5000,
//                50,
//                R.drawable.img_1,
//                "JomDonate",
//                "Tabung Kecemasan Untuk Mangsa Banjir" +
//                        "Assalammualaikum dan Salam Sejahtera," +
//                        "Sehingga hari ini, kami masih menerima banyak aduan dan rintihan dari mangsa banjir yang memerlukan pelbagai bentuk bantuan daripada kita semua. Ramai yang terjejas serta mengalami kemusnahan tempat tinggal serta harta benda. Tidak kurang juga yang terputus bekalan makanan, kehilangan wang ringgit dan ada yang terpaksa meninggalkan kediaman sehelai sepinggang." +
//                        "Atas keprihatinan semua, kami ingin membuka ruang kepada anda untuk menderma dan menjayakan kempen Derma Untuk Mangsa Banjir melalui platform Jomdonate.com."
//        );
//
//        DonationRepository repository = new DonationRepository();
//        repository.addDonation(donation);
    }
}
