package com.example.dermanation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VolunteerVerticalAdapter extends RecyclerView.Adapter<VolunteerVerticalAdapter.ViewHolder> {

    private Context context;
    private List<VolunteerCategory> categoryList;

    public VolunteerVerticalAdapter(Context context, List<VolunteerCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.volunteer_vertical_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VolunteerCategory category = categoryList.get(position);
        holder.categoryTitle.setText(category.getTitle());

        VolunteerHorizontalAdapter horizontalAdapter = new VolunteerHorizontalAdapter(context, category.getItems());
        holder.horizontalRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.horizontalRecyclerView.setAdapter(horizontalAdapter);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView horizontalRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
        }
    }
}
