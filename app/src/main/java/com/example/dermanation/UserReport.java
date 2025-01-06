package com.example.dermanation;

public class UserReport {
    public String email;

    public String reportModule;
    public String reportContent;
    public String reportRating;

//     Constructor
    public UserReport( String email, String reportModule, String reportContent, String reportRating) {
        this.email = email;
        this.reportModule = reportModule;
        this.reportContent = reportContent;
        this.reportRating = reportRating;
    }

    public UserReport() {
    }


}
