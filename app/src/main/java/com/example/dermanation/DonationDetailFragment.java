package com.example.dermanation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DonationDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public DonationDetailFragment() {

    }

    public static DonationDetailFragment newInstance(String param1, String param2) {
        DonationDetailFragment fragment = new DonationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donation_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("DonationDetailFragment", "Bundle is not null");

            String donationID = bundle.getString("donationId");
            int receiverImage = bundle.getInt("receiverImage");
            String receiverName = bundle.getString("receiverName");
            String donationDetail = bundle.getString("donationDetail");
            String donationTitle = bundle.getString("donationTitle");
            int donationImage = bundle.getInt("donationImage");
            double targetAmount = bundle.getDouble("targetAmount");
            double progress = bundle.getDouble("progress");

            bundle.putDouble("progress", progress);
            Log.d("DonationDetailFragment", "progress: " + progress);
            bundle.putDouble("targetAmount", targetAmount);
            Log.d("DonationDetailFragment", "targetAmount: " + targetAmount);
            bundle.putString("donationID", donationID);
            Log.d("DonationDetailFragment", "donationID: " + donationID);

            Button donateButton = view.findViewById(R.id.donateButton);
            TextView donationTitleTextView = view.findViewById(R.id.donationTitleTV);
            ImageView donationImageView = view.findViewById(R.id.donationImageIV);
            TextView targetAmountTextView = view.findViewById(R.id.targetAmountTV);
            ProgressBar progressBar = view.findViewById(R.id.progressPB);
            TextView receiverNameTextView = view.findViewById(R.id.receiverNameTV);
            TextView donationDetailTextView = view.findViewById(R.id.donationDetailTV);
            ImageView receiverImageView = view.findViewById(R.id.receiverImageIV);

            // Set the data to the UI elements
            donationTitleTextView.setText(donationTitle);
            donationImageView.setImageResource(donationImage);

            // Set target amount and progress
            int currentAmount = (int) ((progress / 100.0) * targetAmount);
            String formattedAmount = String.format("RM%d/%d", currentAmount, (int) targetAmount);
            targetAmountTextView.setText(formattedAmount);
            progressBar.setProgress((int) progress);

            // Set receiver data
            receiverImageView.setImageResource(receiverImage);
            receiverNameTextView.setText(receiverName);
            donationDetailTextView.setText(donationDetail);

            // Set up the donate button action
            if (donateButton != null) {
                donateButton.setOnClickListener(v -> {
                    Log.d("DonationDetailFragment", "Donate button clicked");
                    Navigation.findNavController(view).navigate(R.id.action_donationDetailFragment_to_donationAmountFragment, bundle);
                });
            }
        } else {
            Log.d("DonationDetailFragment", "Bundle is null");
        }
    }
}
