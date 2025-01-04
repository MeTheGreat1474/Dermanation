package com.example.dermanation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationViewHolder> {
    private Context context;
    private List<Donation> donations;
    private DonationListener listener;

    public DonationAdapter(Context context, List<Donation> donations, DonationListener listener) {
        this.context = context;
        this.donations = donations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonationViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_donation_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        Donation donation = donations.get(position);

        holder.donationTitleTV.setText(donation.getDonationTitle());
        int currentAmount = (int) ((donation.getProgress() / 100.0) * donation.getTargetAmount());
        String formattedAmount = String.format("RM%d/%d", currentAmount, donation.getTargetAmount());
        holder.targetAmountTV.setText(formattedAmount);
        holder.progressPB.setProgress(donation.getProgress());
//        holder.donationImageIV.setImageResource(donation.getDonationImage());
        int resourceId = context.getResources().getIdentifier(
                donation.getDonationImage(), "drawable", context.getPackageName());

        if (resourceId != 0) {
            holder.donationImageIV.setImageResource(resourceId);
        } else {
            holder.donationImageIV.setImageResource(R.drawable.donation_img_8);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(donation, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public void filteredList(List<Donation> filteredList) {
        donations = filteredList;
        notifyDataSetChanged();
    }
}
