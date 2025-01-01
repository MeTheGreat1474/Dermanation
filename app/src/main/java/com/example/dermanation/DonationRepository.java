package com.example.dermanation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonationRepository {
    private final DatabaseReference databaseReference;

    public DonationRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
    }

    public void addDonation(DonationFirebase donation) {
        String donationId = databaseReference.push().getKey(); // Generate unique ID
        if (donationId != null) {
            donation.setDonationId(donationId);
            databaseReference.child(donationId).setValue(donation)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully added
                        System.out.println("Donation added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // Failed to add
                        System.err.println("Error: " + e.getMessage());
                    });
        }
    }
}
