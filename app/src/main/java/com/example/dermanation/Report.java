package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Report extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LLReport), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up spinner with report options
        Spinner dropdown = findViewById(R.id.SPNOption);
        String[] items = new String[]{"Donation", "Volunteer", "Beneficiary", "Community", "Feedback"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        EditText editTextReport = findViewById(R.id.ETReport);
        Button buttonSubmit = findViewById(R.id.BtnSubmit);
        RatingBar RBRating = findViewById(R.id.RBRate);

        // Set up submit button with listener for sending report to database
        buttonSubmit.setOnClickListener(v -> {
            // Get report details
            String reportType = dropdown.getSelectedItem().toString();
            String reportContent = editTextReport.getText().toString();
            float reportRating = RBRating.getRating();

            // Validate report content
            if (reportContent.isEmpty()) {
                Toast.makeText(Report.this, "Please enter a report", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save report to database
            saveUserReport(reportType, reportContent, String.valueOf(reportRating));
            Toast.makeText(Report.this, "Report sent", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Report.this, Report.class);
            startActivity(intent);

        });
    }

    private void saveUserReport(String reportModule, String reportContent, String reportRating) {
        String userId = "0123";
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReportsRef = database.getReference("userReports");
        String reportId = userReportsRef.child(userId).push().getKey();
        UserReport report = new UserReport(reportModule, reportContent, reportRating);
        userReportsRef.child(reportId).setValue(report);
    }

}