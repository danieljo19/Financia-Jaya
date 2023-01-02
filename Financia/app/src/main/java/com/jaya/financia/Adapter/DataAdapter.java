package com.jaya.financia.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Activities.MainActivity;
import com.jaya.financia.Model.DataModel;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<DataModel> listData;
    private Context context;
    private int id;
    private OnItemLongClickListener mOnItemLongClickListener;

    public DataAdapter(List<DataModel> listData, Context context, OnItemLongClickListener mOnItemLongClickListener) {
        this.listData = listData;
        this.context = context;
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnItemLongClickListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView tvCategory, tvNote, tvAmount, tvDate, tvId;
        private ImageView ivTransactionIcon;
        public OnItemLongClickListener onItemLongClickListener;

        public ViewHolder(@NonNull View itemView, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvNote = itemView.findViewById(R.id.tv_name);
            tvAmount = itemView.findViewById(R.id.tv_total);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivTransactionIcon = itemView.findViewById(R.id.iv_transaction_icon);
            this.onItemLongClickListener = onItemLongClickListener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            onItemLongClickListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
