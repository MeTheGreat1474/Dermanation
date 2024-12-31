package com.example.dermanation;

import java.util.List;

public class VolunteerCategory {
    private String title;
    private List<VolunteerItem> items;

    public VolunteerCategory(String title, List<VolunteerItem> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<VolunteerItem> getItems() {
        return items;
    }
}
