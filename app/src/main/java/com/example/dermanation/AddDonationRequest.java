package com.example.dermanation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class AddDonationRequest extends AppCompatActivity {

    Button createDonationRequestButton;
    EditText donationTitle, donationDescription, receiverName, targetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation_request);

        donationDescription = findViewById(R.id.uploadDonationDetails);
        donationTitle = findViewById(R.id.uploadDonationTitle);
        receiverName = findViewById(R.id.uploadReceiverName);
        targetAmount = findViewById(R.id.uploadTargetAmount);
        createDonationRequestButton = findViewById(R.id.createDonationRequestButton);

        createDonationRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Donation Request")
                .setMessage("Are you sure you want to create this donation?")
                .setPositiveButton("Yes", (dialog, which) -> saveDonationRequest())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


    public void saveDonationRequest() {

        String desc = donationDescription.getText().toString();
        String title = donationTitle.getText().toString();
        String receiver = receiverName.getText().toString();
        String amount = targetAmount.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty() || receiver.isEmpty() || amount.isEmpty()) {
            Toast.makeText(AddDonationRequest.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // convert amount to integer
            int amountInt = Integer.parseInt(amount);

            // Generate unique donationId
            String donationId = UUID.randomUUID().toString();

            // Create a donation request object with matching structure
            DataDonationRequest donationRequest = new DataDonationRequest(
                    donationId,
                    title,
                    desc,
                    0.0f, // Default progress
                    "donation_img_1", // Example receiver image resource ID
                    receiver,
                    "donation_img_8", // Example donation image resource ID
                    amountInt
            );

            FirebaseDatabase.getInstance().getReference("Donations").child(donationId)
                    .setValue(donationRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddDonationRequest.this, "Donation Created", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddDonationRequest.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(AddDonationRequest.this, "Invalid target amount", Toast.LENGTH_SHORT).show();
        }

    }
}