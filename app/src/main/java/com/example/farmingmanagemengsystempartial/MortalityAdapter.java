package com.example.farmingmanagemengsystempartial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MortalityAdapter extends RecyclerView.Adapter<MortalityAdapter.ViewHolder> {
    private List<MortalityData> dataList;

    public MortalityAdapter(List<MortalityData> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateView, deathsView, sickView, notesView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.dateItem);
            deathsView = itemView.findViewById(R.id.deathsItem);
            sickView = itemView.findViewById(R.id.sickItem);
            notesView = itemView.findViewById(R.id.notesItem);
        }
    }

    @Override
    public MortalityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mortality_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MortalityData data = dataList.get(position);
        holder.dateView.setText(data.getDate());
        holder.deathsView.setText(data.getDeaths());
        holder.sickView.setText(data.getSick());
        holder.notesView.setText(data.getNotes());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
