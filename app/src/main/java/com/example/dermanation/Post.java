package com.example.dermanation;

public class Post {
    public String name;
    public String title;
    public String content;
    public String date;

    // Constructor
    public Post(String name, String title, String content, String date) {
        this.name = name;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Post() {

        // You can initialize fields with default values here if needed

    }


    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
