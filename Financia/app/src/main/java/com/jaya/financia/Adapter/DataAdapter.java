package com.jaya.financia.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jaya.financia.Model.DataModel;
import com.jaya.financia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
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
        String date = data.getDate();
        Date dateSQL = null;
        String dateCard;

        holder.tvCategory.setText(data.getCategory());
        holder.tvNote.setText(data.getNote());
        if (data.getType().equalsIgnoreCase("incomes")) {
            holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_incomes));
            holder.tvAmount.setText("+Rp" + data.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#19AC27"));
        } else {
            holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_foodanddrink));
            holder.tvAmount.setText("-Rp" + data.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#C72E2E"));
        }

        // Tanggal
        SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateSQL = dateFormatSQL.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatCard = new SimpleDateFormat("dd MMM yyyy");
        dateCard = dateFormatCard.format(dateSQL);
        holder.tvDate.setText(dateCard);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategory, tvNote, tvAmount, tvDate;
        private ImageView ivTransactionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvNote = itemView.findViewById(R.id.tv_name);
            tvAmount = itemView.findViewById(R.id.tv_total);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivTransactionIcon = itemView.findViewById(R.id.iv_transaction_icon);
        }
    }
}
