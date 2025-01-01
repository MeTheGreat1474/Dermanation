package com.example.dermanation;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DonationViewHolder extends RecyclerView.ViewHolder {
    public TextView donationTitleTV;
    public ImageView donationImageIV;
    public TextView targetAmountTV;
    public ProgressBar progressPB;
    public CardView cardView;

    public DonationViewHolder(View itemView) {
        super(itemView);
        donationTitleTV = itemView.findViewById(R.id.donationTitleTV);
        donationImageIV = itemView.findViewById(R.id.donationImageIV);
        targetAmountTV = itemView.findViewById(R.id.targetAmountTV);
        progressPB = itemView.findViewById(R.id.progressPB);
        cardView = itemView.findViewById(R.id.mainContainer);
    }
}
