package com.example.dermanation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VolunteerJoinedEventsAdapter extends RecyclerView.Adapter<VolunteerJoinedEventsAdapter.ViewHolder> {

    private final List<VolunteerItem> events;

    public VolunteerJoinedEventsAdapter(List<VolunteerItem> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VolunteerItem item = events.get(position);

        holder.eventImage.setImageResource(item.getImageResId());
        holder.iconView.setImageResource(item.getIconResId());
        holder.eventTitle.setText(item.getTitle());
        holder.eventOrganizer.setText(item.getOrganizationName());
        holder.joinedDate.setText("" + item.getParticipantCount());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), VolunteerDetails.class);
            intent.putExtra("ITEM_ID", item.getId());
            intent.putExtra("ITEM_TITLE", item.getTitle());
            intent.putExtra("ITEM_ORGANIZATION_NAME", item.getOrganizationName());
            intent.putExtra("ITEM_ORGANIZATION_ICON", item.getIconResId());
            intent.putExtra("HIDE_GO_BUTTON", true); // Pass the flag to hide the Go button
            holder.itemView.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage, iconView;
        TextView eventTitle, eventOrganizer, joinedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.itemImage);
            iconView = itemView.findViewById(R.id.iconView);
            eventTitle = itemView.findViewById(R.id.itemTitle);
            eventOrganizer = itemView.findViewById(R.id.organizationNameTextView);
            joinedDate = itemView.findViewById(R.id.participantCountTextView);
        }
    }
}
