package com.jaya.financia.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            getCardTransactionIcon(holder, data.getType(), data.getCategory());
            holder.tvAmount.setText("+Rp" + data.getAmount());
            holder.tvAmount.setTextColor(Color.parseColor("#19AC27"));
        } else {
            getCardTransactionIcon(holder, data.getType(), data.getCategory());
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
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

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
            vibrator.vibrate(25);
            onItemLongClickListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void getCardTransactionIcon(ViewHolder holder, String type, String category) {
        if (type.equalsIgnoreCase("incomes")) {
            if (category.equalsIgnoreCase("Salary")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_incomes));
            } else if (category.equalsIgnoreCase("Business")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_work));
            } else if (category.equalsIgnoreCase("Gifts")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_gifts));
            } else if (category.equalsIgnoreCase("Extra Income")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_extraincome));
            } else if (category.equalsIgnoreCase("Loan")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_loan));
            } else if (category.equalsIgnoreCase("Investment")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_investment));
            } else if (category.equalsIgnoreCase("Insurance Payout")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_healthcare));
            } else {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_incomes));
            }
        } else {
            if (category.equalsIgnoreCase("Food & Drink")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_foodanddrink));
            } else if (category.equalsIgnoreCase("Shopping")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_shopping));
            } else if (category.equalsIgnoreCase("Transport")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_transport));
            } else if (category.equalsIgnoreCase("Home")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_home));
            } else if (category.equalsIgnoreCase("Bills & Fees")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_billsandfees));
            } else if (category.equalsIgnoreCase("Entertainment")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_entertainment));
            } else if (category.equalsIgnoreCase("Vehicle")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_vehicle));
            } else if (category.equalsIgnoreCase("Travel")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_travel));
            } else if (category.equalsIgnoreCase("Family & Personal")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_familyandpersonal2));
            } else if (category.equalsIgnoreCase("Healthcare")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_healthcare));
            } else if (category.equalsIgnoreCase("Education")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_education));
            } else if (category.equalsIgnoreCase("Groceries")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_grocery));
            } else if (category.equalsIgnoreCase("Gifts")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_gifts));
            } else if (category.equalsIgnoreCase("Sports & Hobby")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_sportandhobby));
            } else if (category.equalsIgnoreCase("Beauty")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_beauty));
            } else if (category.equalsIgnoreCase("Work")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_work));
            } else if (category.equalsIgnoreCase("Pet")) {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_pet));
            } else {
                holder.ivTransactionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tic_other));
            }
        }
    }
}
