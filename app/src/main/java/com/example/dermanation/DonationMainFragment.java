package com.example.dermanation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationMainFragment extends Fragment implements DonationListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public static String DonationTitle;
    public static int DonationImage;
    public static int TargetAmount;
    public static int Progress;

    private List<Donation> donationList = new ArrayList<>();
    private DonationAdapter donationAdapter;

    public DonationMainFragment() {
    }

    public static DonationMainFragment newInstance(String param1, String param2) {
        DonationMainFragment fragment = new DonationMainFragment();
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
        return inflater.inflate(R.layout.fragment_donation_main, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.donationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchDonationsFromFirebase();

        donationAdapter = new DonationAdapter(getContext(), donationList, this);
        recyclerView.setAdapter(donationAdapter);

        SearchView searchView = view.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }

            private void filter(String newText) {
                List<Donation> filteredList = new ArrayList<>();
                for (Donation donation : donationList) {
                    if (donation.getDonationTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(donation);
                    }
                }
                donationAdapter.filteredList(filteredList);
            }
        });

        // Set up the FloatingActionButton to navigate to AddDonationRequest.class
        View fab = view.findViewById(R.id.fab);

        // Check the current user's receiverStatus from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(getCurrentUserId());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // applyReceiver is Boolean in the database
                    Boolean applyReceiver = dataSnapshot.child("applyReceiver").getValue(Boolean.class);
                    if (applyReceiver != null && applyReceiver) {
                        String receiverStatus = dataSnapshot.child("receiverStatus").getValue(String.class);
                        if (!"Verified".equals(receiverStatus)) {
                            // Hide the FloatingActionButton if the receiverStatus is not "Verified"
                            fab.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DonationMainFragment", "Failed to read user data", databaseError.toException());
            }
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddDonationRequest.class);
            startActivity(intent);
        });

    }

    // Helper method to get the current user's ID
    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onItemClick(Donation donation, View view) {
        Log.d("DonationDetailFragment", "Donate button clicked");

        Bundle bundle = new Bundle();
        bundle.putString("donationId", donation.getDonationId());
        bundle.putInt("receiverImage", donation.getReceiverImage());
        bundle.putString("receiverName", donation.getReceiverName());
        bundle.putString("donationDetail", donation.getDonationDetail());
        bundle.putString("donationTitle", donation.getDonationTitle());
        bundle.putInt("donationImage", donation.getDonationImage());
        bundle.putDouble("targetAmount", donation.getTargetAmount());
        bundle.putDouble("progress", donation.getProgress());

        Navigation.findNavController(view).navigate(R.id.donationDetailFragment, bundle);
    }

    private void fetchDonationsFromFirebase() {
        DatabaseReference donationsRef = FirebaseDatabase.getInstance().getReference("Donations");

        donationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donation donation = snapshot.getValue(Donation.class);
                    if (donation != null) {
                        donationList.add(donation);
                    }
                }

                donationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DonationMainFragment", "Failed to read data from Firebase", databaseError.toException());
            }
        });
    }

}
