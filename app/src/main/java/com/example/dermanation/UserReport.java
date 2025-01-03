package com.example.dermanation;

public class UserReport {
    public String reportModule;
    public String reportContent;
    public String reportRating;

    // Constructor
    public UserReport(String reportModule, String reportContent, String reportRating) {
        this.reportModule = reportModule;
        this.reportContent = reportContent;
        this.reportRating = reportRating;
    }
}
