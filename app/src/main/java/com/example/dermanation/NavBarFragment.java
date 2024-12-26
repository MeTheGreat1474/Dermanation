package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

public class NavBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_bar, container, false);

        AppCompatButton helpButton = view.findViewById(R.id.BTHelp);
        AppCompatButton reportButton = view.findViewById(R.id.BTReport);
        AppCompatButton supportButton = view.findViewById(R.id.BTSupport);

        helpButton.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        supportButton.setBackgroundColor(getResources().getColor(R.color.dark_grey));

        if (getActivity() instanceof Help){
            helpButton.setBackgroundColor(getResources().getColor(R.color.pink));
        } else if (getActivity() instanceof Report){
            reportButton.setBackgroundColor(getResources().getColor(R.color.pink));
        } else if (getActivity() instanceof Support){
            supportButton.setBackgroundColor(getResources().getColor(R.color.pink));
        }

        helpButton.setOnClickListener(v -> {
            if (!(getActivity() instanceof Help)) {
                startActivity(new Intent(getActivity(), Help.class));
            }
        });

        reportButton.setOnClickListener(v -> {
            if (!(getActivity() instanceof Report)) {
                startActivity(new Intent(getActivity(), Report.class));
            }
        });

        supportButton.setOnClickListener(v -> {
            if (!(getActivity() instanceof Support)) {
                startActivity(new Intent(getActivity(), Support.class));
            }
        });

        return view;
    }
}