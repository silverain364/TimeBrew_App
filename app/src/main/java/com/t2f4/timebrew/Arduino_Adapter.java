package com.t2f4.timebrew;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Arduino_Adapter extends RecyclerView.Adapter<Arduino_Adapter.ViewHolder> {
    private List<Integer> ids ;
    private String prefix;
    private TextView selectTv;
    private int selectNumber = -1;
    private View selectItemView = null;

    public Arduino_Adapter(String prefix, List<Integer> ids, TextView selectTv) {
        this.ids = ids;
        this.selectTv = selectTv;
        this.prefix = prefix;
    }

    @NonNull
    @Override
    public Arduino_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arduino_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Arduino_Adapter.ViewHolder holder, int position) {
        holder.textView.setText(prefix + " : " + ids.get(position));

        holder.itemView.setOnClickListener(v -> {
            if(selectItemView != null)
                selectItemView.setBackgroundColor(Color.rgb(255,255,255));

            holder.itemView.setBackgroundColor(Color.rgb(240,240,240));
            selectItemView = holder.itemView;
            selectNumber = holder.getAdapterPosition();

            selectTv.setText(holder.textView.getText());
        });
    }

    public Integer getSelectNumber(){
        return selectNumber;
    }
    public Integer removeSelectList(){
        if(selectNumber == -1)
            return null;

        Integer remove = ids.remove(selectNumber);
        selectItemView.setBackgroundColor(Color.rgb(255,255,255));


        notifyItemRemoved(selectNumber);
        selectNumber = -1;
        selectItemView = null;

        return remove;
    }

    @Override
    public int getItemCount() {
        return ids != null ? ids.size() : 0;
    }

    public void addItem(Integer id){
        ids.add(id);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}