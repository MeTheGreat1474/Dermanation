package com.example.dermanation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class VerifyReceiver extends AppCompatActivity {

    TextView return_to_profile;

    EditText editFullname, editNRIC, editHouseholdIncome;

    RadioGroup radioGroupFamilytype;

    RadioButton radioButtonB40, radioButtonM40;

    Button btn_verify_receiver;

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_receiver);

        // Initialize Firebase Auth and User
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize Firebase Database reference
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        return_to_profile = findViewById(R.id.return_to_profile);
        editFullname = findViewById(R.id.editFullname);
        editNRIC = findViewById(R.id.editNRIC);
        editHouseholdIncome = findViewById(R.id.editHouseholdIncome);
        radioGroupFamilytype = findViewById(R.id.radioGroupFamilytype);
        radioButtonB40 = findViewById(R.id.radioButtonB40);
        radioButtonM40 = findViewById(R.id.radioButtonM40);
        btn_verify_receiver = findViewById(R.id.btn_verify_receiver);

        // Redirect to ProfilePage if cancel is clicked
        return_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

        // Submit details when the button is clicked
        btn_verify_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails();
            }
        });
    }

    private void submitDetails() {
        String fullname = editFullname.getText().toString().trim();
        String nric = editNRIC.getText().toString().trim();
        String householdIncome = editHouseholdIncome.getText().toString().trim();
        int selectedFamilyTypeId = radioGroupFamilytype.getCheckedRadioButtonId();
        String familyType = selectedFamilyTypeId == R.id.radioButtonB40 ? "B40" : "M40";

        // Validate input fields
        if (TextUtils.isEmpty(fullname)) {
            editFullname.setError("Full name is required");
            return;
        }
        if (TextUtils.isEmpty(nric)) {
            editNRIC.setError("NRIC is required");
            return;
        }
        if (TextUtils.isEmpty(householdIncome)) {
            editHouseholdIncome.setError("Household income is required");
            return;
        }
        if (selectedFamilyTypeId == -1) {
            Toast.makeText(this, "Please select a family type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold only the new fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("fullname", fullname);
        updates.put("nric", nric);
        updates.put("householdIncome", householdIncome);
        updates.put("familyType", familyType);
        updates.put("receiverStatus", "Verified");

        // Update the specific fields in the database
        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(VerifyReceiver.this, "Verification details submitted successfully", Toast.LENGTH_SHORT).show();
                // Redirect to ProfilePage
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(VerifyReceiver.this, "Failed to submit details: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}