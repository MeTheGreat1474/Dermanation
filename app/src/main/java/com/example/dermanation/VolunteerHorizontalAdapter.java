package com.example.dermanation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VolunteerHorizontalAdapter extends RecyclerView.Adapter<VolunteerHorizontalAdapter.ViewHolder> {

    private Context context;
    private List<VolunteerItem> itemList;

    public VolunteerHorizontalAdapter(Context context, List<VolunteerItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.volunteer_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VolunteerItem item = itemList.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemImage.setImageResource(item.getImageResId());
        holder.iconView.setImageResource(item.getIconResId());
        holder.organizationNameTextView.setText(item.getOrganizationName());
        holder.participantCountTextView.setText(String.valueOf(item.getParticipantCount()));

        // Navigate to DetailsActivity on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VolunteerDetails.class);
            intent.putExtra("ITEM_ID", item.getId());
            intent.putExtra("ITEM_TITLE", item.getTitle());
            intent.putExtra("ITEM_ORGANIZATION_NAME", item.getOrganizationName());
            intent.putExtra("ITEM_ORGANIZATION_ICON", item.getIconResId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        ImageView iconView;
        TextView organizationNameTextView;
        TextView participantCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            iconView = itemView.findViewById(R.id.iconView);
            organizationNameTextView = itemView.findViewById(R.id.organizationNameTextView);
            participantCountTextView = itemView.findViewById(R.id.participantCountTextView);
        }
    }
}
