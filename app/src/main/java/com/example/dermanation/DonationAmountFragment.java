package com.example.dermanation;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DonationAmountFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Button[] buttons;
    private EditText otherAmount;
    private Button currentlySelectedButton = null;

    Button makePaymentButton;
    Button paymentMethodButton;

    public DonationAmountFragment() {
    }

    public static DonationAmountFragment newInstance(String param1, String param2) {
        DonationAmountFragment fragment = new DonationAmountFragment();
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
        return inflater.inflate(R.layout.fragment_donation_amount, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button donationAmountButton = view.findViewById(R.id.donationAmountButton);
        makePaymentButton = view.findViewById(R.id.makePaymentButton);
        paymentMethodButton = view.findViewById(R.id.paymentMethodButton);

        donationAmountButton.setTextColor(Color.parseColor("#FF4081"));

        Bundle bundle = getArguments();

        double progress = bundle.getDouble("progress");
        bundle.putDouble("progress", progress);
        Log.d("DonationAmountFragment", "progress: " + progress);

        double targetAmount = bundle.getDouble("targetAmount");
        bundle.putDouble("targetAmount", targetAmount);
        Log.d("DonationAmountFragment", "targetAmount: " + targetAmount);

        String donationID = bundle.getString("donationId");
        bundle.putString("donationID", donationID);
        Log.d("DonationAmountFragment", "donationID: " + donationID);

        if (makePaymentButton != null) {
            makePaymentButton.setOnClickListener(v -> {
                Log.d("DonationAmountFragment", "Donate button clicked");

                String selectedAmount = getSelectedAmount();
                Log.d("DonationAmountFragment", "Selected Amount: " + selectedAmount);
                bundle.putString("selected_amount", selectedAmount);

                Navigation.findNavController(view).navigate(R.id.donationPaymentFragment, bundle);
            });
        }
//        if (paymentMethodButton != null) {
//            paymentMethodButton.setOnClickListener(v -> {
//                Log.d("DonationAmountFragment", "Donate button clicked");
//                Navigation.findNavController(view).navigate(R.id.donationPaymentFragment);
//            });
//        }
        makePaymentButton.setEnabled(false);
        paymentMethodButton.setEnabled(false);

        buttons = new Button[4];
        buttons[0] = view.findViewById(R.id.rm5button);
        buttons[1] = view.findViewById(R.id.rm10button);
        buttons[2] = view.findViewById(R.id.rm50button);
        buttons[3] = view.findViewById(R.id.rm100button);

        for (Button button : buttons) {
            button.setOnClickListener(v -> toggleButtons((Button) v));
        }

        otherAmount = view.findViewById(R.id.otherAmountEditText);
        otherAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    resetButtons();
                    currentlySelectedButton = null;
                    makePaymentButton.setEnabled(true);
                    paymentMethodButton.setEnabled(true);
                } else {
                    makePaymentButton.setEnabled(false);
                    paymentMethodButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        resetButtons();
    }

    private void toggleButtons(Button selectedButton) {
        otherAmount.getText().clear();

        if (selectedButton == currentlySelectedButton) {
            resetButtons();
            currentlySelectedButton = null;
            makePaymentButton.setEnabled(false);
            paymentMethodButton.setEnabled(false);
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
        makePaymentButton.setEnabled(true);
        paymentMethodButton.setEnabled(true);
    }

    private void resetButtons() {
        for (Button button : buttons) {
            button.setBackgroundColor(Color.parseColor("#A8A9AD"));
            button.setTextColor(Color.parseColor("#000000"));
        }
    }

    private String getSelectedAmount() {
        // Return the selected donation amount
        if (currentlySelectedButton != null) {
            if (currentlySelectedButton.getId() == R.id.rm5button) {
                return "5";
            } else if (currentlySelectedButton.getId() == R.id.rm10button) {
                return "10";
            } else if (currentlySelectedButton.getId() == R.id.rm50button) {
                return "50";
            } else if (currentlySelectedButton.getId() == R.id.rm100button) {
                return "100";
            }
        } else if (otherAmount.getText().length() > 0) {
            return otherAmount.getText().toString(); // For custom amounts
        }
        return "";
    }
}