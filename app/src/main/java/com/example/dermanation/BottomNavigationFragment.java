package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dermanation.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Navigate to Home
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_setting) {
                    // Navigate to Help later replace with Settings
                    startActivity(new Intent(getActivity(), Setting.class));
                    return true;
                } else if (itemId == R.id.navigation_donate) {
                    // Navigate to Help later replace with Settings
                    startActivity(new Intent(getActivity(), DonationActivity.class));
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    // Navigate to Help later replace with Settings
                    startActivity(new Intent(getActivity(), ProfilePage.class));
                    return true;
                } else if (itemId == R.id.navigation_community) {
                    // Navigate to Help later replace with Settings
                    startActivity(new Intent(getActivity(), Community.class));
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}