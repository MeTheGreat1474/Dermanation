package com.example.dermanation;

public class VolunteerItem {
    private String id;
    private String title;
    private int imageResId;
    private int iconResId;
    private String organizationName;
    private int participantCount;

    public VolunteerItem(String id, String title, int imageResId, int iconResId, String organizationName, int participantCount) {
        this.id = id;
        this.title = title;
        this.imageResId = imageResId;
        this.iconResId = iconResId;
        this.organizationName = organizationName;
        this.participantCount = participantCount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int count) {
        this.participantCount = count;
    }
}

