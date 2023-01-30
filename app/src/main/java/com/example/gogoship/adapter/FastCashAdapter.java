package com.mobapps.gogoship.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.ItemClick;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.model.ZenCashModel;

import java.util.ArrayList;

public class FastCashAdapter extends RecyclerView.Adapter<FastCashAdapter.VH> {

    View view = null;
    Context context;
    ArrayList<ZenCashModel> zenCashList;
    ItemClick itemClick;
    private int row_index = 0;

    public FastCashAdapter(Context context, int row_index, ArrayList<ZenCashModel> zenCashList, ItemClick itemClick) {

        this.context = context;
        this.zenCashList = zenCashList;
        this.row_index = row_index;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public FastCashAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zencash_adapter, parent, false);
        return new FastCashAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FastCashAdapter.VH holder, int position) {

        holder.cash_amt.setText(zenCashList.get(position).getCasamt_txt());

        holder.cash_amt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                itemClick.ontemClick(position);
                notifyDataSetChanged();
                Log.e("MySav", String.valueOf(row_index));
            }
        });


        if (row_index == position) {
            holder.cash_back.setSelected(true);
        } else {
            holder.cash_back.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return zenCashList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView cash_amt;
        LinearLayout cash_back;

        public VH(@NonNull View itemView) {
            super(itemView);

            cash_amt = itemView.findViewById(R.id.txt_cash);
            cash_back = itemView.findViewById(R.id.lin_cash_back);
        }
    }
}
