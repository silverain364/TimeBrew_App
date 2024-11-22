package com.t2f4.timebrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Arduino_Adapter extends RecyclerView.Adapter<Arduino_Adapter.ViewHolder> {
    private List<String> dummyData;

    public Arduino_Adapter(List<String> dummyData) {
        this.dummyData = dummyData;
    }

    @NonNull
    @Override
    public Arduino_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arduino_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Arduino_Adapter.ViewHolder holder, int position) {
        holder.textView.setText(dummyData.get(position) + " / " +dummyData.size());

    }

    @Override
    public int getItemCount() {
        return dummyData != null ? dummyData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}