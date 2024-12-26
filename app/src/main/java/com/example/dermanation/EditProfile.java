package com.example.dermanation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

    TextView cancelEditProfile, saveEditProfile;
    EditText editBirthDate, editTextPhone;
    TextInputEditText editTextAddress;
    RadioGroup radioGroupGender;
    RadioButton radioButtonMale, radioButtonFemale;

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Auth and User
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Initialize UI elements
        cancelEditProfile = findViewById(R.id.cancelEditProfile);
        saveEditProfile = findViewById(R.id.saveEditProfile);
        editBirthDate = findViewById(R.id.editBirthDate);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        // Set up DatePickerDialog for editBirthDate
        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar calendar = Calendar.getInstance();

                // Show DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Format the date as dd/MM/yyyy
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                editBirthDate.setText(dateFormat.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.show();
            }
        });

        // Redirect to ProfilePage if cancel is clicked
        cancelEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
            finish();
        } else {
            // Reference to the current user's data in Firebase Database
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

            // Fetch and display existing user data
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve user details
                        String phone = snapshot.child("phone_number").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String birthDate = snapshot.child("birth_date").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);

                        // Populate the fields
                        editTextPhone.setText(phone != null ? phone : "");
                        editTextAddress.setText(address != null ? address : "");
                        editBirthDate.setText(birthDate != null ? birthDate : "");

                        // Set the gender radio button
                        if ("Male".equalsIgnoreCase(gender)) {
                            radioGroupGender.check(R.id.radioButtonMale);
                        } else if ("Female".equalsIgnoreCase(gender)) {
                            radioGroupGender.check(R.id.radioButtonFemale);
                        }
                    } else {
                        Toast.makeText(EditProfile.this, "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfile.this, "Failed to fetch user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Save updated data back to Firebase
            saveEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = editTextPhone.getText().toString().trim();
                    String address = editTextAddress.getText().toString().trim();
                    String birthDate = editBirthDate.getText().toString().trim();

                    // Determine selected gender
                    String gender = null;
                    if (radioButtonMale.isChecked()) {
                        gender = "Male";
                    } else if (radioButtonFemale.isChecked()) {
                        gender = "Female";
                    }

                    // Prepare data for update
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("phone_number", phone);
                    updates.put("address", address);
                    updates.put("birth_date", birthDate);
                    updates.put("gender", gender);

                    // Update Firebase Database
                    userRef.updateChildren(updates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditProfile.this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
