package com.jaya.financia.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaya.financia.Model.DataModel;
import com.jaya.financia.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private List<DataModel> listData;
    private Context context;

    public DataAdapter(List<DataModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel data = listData.get(position);

        holder.tvName.setText(data.getName());
        if(data.getType().equalsIgnoreCase("In")) {
            holder.tvType.setText("Income");
            holder.tvTotal.setText("+Rp" + data.getTotal());
            holder.tvTotal.setTextColor(Color.parseColor("#54B435"));
        } else {
            holder.tvType.setText("Expenses");
            holder.tvTotal.setText("-Rp" + data.getTotal());
            holder.tvTotal.setTextColor(Color.parseColor("#FF1E1E"));
        }
        holder.tvDate.setText(data.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvType, tvTotal, tvDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
