package com.example.dermanation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;

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

        Spinner dropdown = findViewById(R.id.SPNOption);
        String[] items = new String[]{"Donation", "Volunteer", "Beneficiary", "Community", "Feedback"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        EditText editTextReport = findViewById(R.id.ETReport);
        Button buttonSubmit = findViewById(R.id.BtnSubmit);

        buttonSubmit.setOnClickListener(v -> {
            String reportType = dropdown.getSelectedItem().toString();
            String reportContent = editTextReport.getText().toString();

            if (reportContent.isEmpty()) {
                Toast.makeText(Report.this, "Please enter a report", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                createTxtFile(reportType, reportContent);
                Toast.makeText(Report.this, "Report saved as TXT", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(Report.this, "Failed to save report", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    // report saved at user's internal storage /data/user/0/com.example.dermanation/files/report/report.txt
    private void createTxtFile(String reportType, String reportContent) throws IOException {
        File reportDir = new File(getFilesDir(), "report");
        if (!reportDir.exists()) {
            reportDir.mkdir();
        }
        File reportFile = new File(reportDir, "report.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(reportFile);
        String report = "Type: " + reportType + "\nContent: " + reportContent;
        fileOutputStream.write(report.getBytes());
        fileOutputStream.close();
        Log.d("Report", "Report saved at: " + reportFile.getAbsolutePath());
    }

}