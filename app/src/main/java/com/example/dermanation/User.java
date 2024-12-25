package com.example.dermanation;

public class User {
    public String name;
    public String email;
    public boolean applyReceiver;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, boolean applyReceiver) {
        this.name = name;
        this.email = email;
        this.applyReceiver = applyReceiver;
    }
}
