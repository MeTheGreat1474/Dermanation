package com.example.dermanation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DonationPaymentFragment  extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Button[] buttons;

    private Dialog dialog;

    Button confirmButton;

    private Button currentlySelectedButton = null;

    public DonationPaymentFragment() {

    }

    public static DonationPaymentFragment newInstance(String param1, String param2) {
        DonationPaymentFragment fragment = new DonationPaymentFragment();
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
        return inflater.inflate(R.layout.fragment_donation_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button donationAmountButton = view.findViewById(R.id.donationAmountButton);
        Button paymentMethodButton = view.findViewById(R.id.paymentMethodButton);

        paymentMethodButton.setTextColor(Color.parseColor("#FF4081"));

        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setEnabled(false);

        if (donationAmountButton != null) {
            donationAmountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("DonationPaymentFragment", "Donate button clicked");
                    Navigation.findNavController(view).navigate(R.id.donationAmountFragment);
                }
            });
        }

        buttons = new Button[3];
        buttons[0] = view.findViewById(R.id.masterButton);
        buttons[1] = view.findViewById(R.id.visaButton);
        buttons[2] = view.findViewById(R.id.tngButton);


        for (Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButtons((Button) v);
                }
            });
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("DonationPaymentFragment", "Bundle is not null");

            // Retrieve the selected amount and check if it's null
            String selectedAmount = bundle.getString("selected_amount");
            if (selectedAmount != null) {
                Log.d("DonationPaymentFragment", "Selected Amount: " + selectedAmount);
            } else {
                Log.d("DonationPaymentFragment", "Selected Amount is null");
            }

            // Ensure donation amount is passed as a string and convert to double
            final double donationAmount = selectedAmount != null ? Double.parseDouble(selectedAmount) : 0;

            // Get the current progress and target amount from the bundle
            final double currentProgress = bundle.getDouble("progress");  // progress is in percentage
            final double targetAmount = bundle.getDouble("targetAmount");

            // Calculate the new progress after donation
            final double newProgress = (currentProgress/100)*targetAmount + donationAmount;

            // Ensure the new progress does not exceed the target amount
            final double finalNewProgress = (newProgress > targetAmount) ? targetAmount : newProgress;

            // Calculate the progress percentage (based on the target amount)
            final double progressPercentage = (finalNewProgress / targetAmount) * 100;

            Log.d("DonationPaymentFragment", "currentProgress: " + currentProgress);
            Log.d("DonationPaymentFragment", "targetAmount: " + targetAmount);
            Log.d("DonationPaymentFragment", "donationAmount: " + donationAmount);


            // Set up the confirm button click listener
            confirmButton.setOnClickListener(v -> {
                // Update the donation progress in Firebase
                updateDonationProgress(progressPercentage, new FirebaseCallback() {
                    @Override
                    public void onSuccess() {
                        // Navigate after successful database update
//                        Navigation.findNavController(requireView()).navigate(R.id.navigation_home);
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Firebase", "Error updating donation progress", e);
                    }
                });
            });
        } else {
            Log.d("DonationPaymentFragment", "Bundle is null");
        }

        popUpAds();
    }

    private void toggleButtons(Button selectedButton) {
        if (selectedButton == currentlySelectedButton) {
            resetButtons();
            currentlySelectedButton = null;
            confirmButton.setEnabled(false);
            return;
        }

        for (Button button : buttons) {
            if (button == selectedButton) {
                button.setBackgroundColor(Color.parseColor("#FF4081"));
                button.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                button.setBackgroundColor(Color.parseColor("#A8A9AD"));
                button.setTextColor(Color.parseColor("#000000"));
            }
        }

        currentlySelectedButton = selectedButton;
        confirmButton.setEnabled(true);
    }

    private void resetButtons() {
        for (Button button : buttons) {
            button.setBackgroundColor(Color.parseColor("#A8A9AD"));
            button.setTextColor(Color.parseColor("#000000"));
        }

    }
    private void popUpAds() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        if (currentUser == null) {
            Log.e("DonationPaymentFragment", "User is not logged in.");
            return;
        }

        userRef.child("subscribed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean isSubscribed = snapshot.getValue(Boolean.class);

                if (isSubscribed != null && isSubscribed) {
                    Log.d("DonationPaymentFragment", "User already subscribed. Ad will not appear.");
                } else {
                    if (!snapshot.exists()) {
                        userRef.child("subscribed").setValue(false).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("DonationPaymentFragment", "Initialized 'subscribed' field to false.");
                            } else {
                                Log.e("DonationPaymentFragment", "Failed to initialize 'subscribed' field.");
                                Toast.makeText(requireContext(), "Error initializing subscription status. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    try {
                        dialog = new Dialog(requireContext());
                        dialog.setContentView(R.layout.fragment_popup_ad);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton closeButton = dialog.findViewById(R.id.closeButton);
                        Button subscribeButton = dialog.findViewById(R.id.subButton);

                        if (closeButton != null && subscribeButton != null) {
                            closeButton.setOnClickListener(v -> dialog.dismiss());

                            subscribeButton.setOnClickListener(v -> {
                                userRef.child("subscribed").setValue(true).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("DonationPaymentFragment", "Subscription status updated.");
                                    } else {
                                        Log.e("DonationPaymentFragment", "Failed to update subscription status.");
                                        Toast.makeText(requireContext(), "Error updating subscription status. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            });
                        } else {
                            Log.e("DonationPaymentFragment", "Buttons not found in the dialog layout!");
                        }

                        dialog.show();
                    } catch (Exception e) {
                        Log.e("DonationPaymentFragment", "Error creating dialog: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DonationPaymentFragment", "Error reading subscription status: " + error.getMessage());
            }
        });
    }

    private void updateDonationProgress(double progressPercentage, FirebaseCallback callback) {
        Bundle bundle = getArguments();
        String donationID = bundle.getString("donationId");
        Log.d("DonationAmountFragment", "donationID: " + donationID);

        if (Double.isNaN(progressPercentage) || Double.isInfinite(progressPercentage)) {
            Log.e("Firebase", "Invalid progress percentage: " + progressPercentage);
            return; // Don't proceed if the value is invalid
        }

        // Get a reference to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference donationRef = database.getReference("Donations/" + donationID);

        // Create a map to store the updated progress
        Map<String, Object> updates = new HashMap<>();
        updates.put("progress", progressPercentage); // Update the progress percentage

        // Update the progress in the Firebase Realtime Database
        donationRef.updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Donation progress updated successfully");
                        callback.onSuccess(); // Notify success
                    } else {
                        Log.e("Firebase", "Error updating donation progress", task.getException());
                        callback.onFailure(task.getException()); // Notify failure
                    }
                });
    }
}