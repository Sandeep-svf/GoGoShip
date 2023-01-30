package com.gogoship.gogoship.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.response.TransactionResponse;

import java.util.List;

public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionAdapter.VH> {

    View view = null;
    Context context ;
    List<TransactionResponse> transactionResponseList;

    public WalletTransactionAdapter(Context context, List<TransactionResponse> transactionResponseList) {

        this.context=context;
        this.transactionResponseList=transactionResponseList;
    }

    @NonNull
    @Override
    public WalletTransactionAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wallet_transaction, parent, false);
        return new WalletTransactionAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletTransactionAdapter.VH holder, int position) {

        holder.creat_at.setText(transactionResponseList.get(position).getDate());
        holder.price.setText("$"+""+transactionResponseList.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return transactionResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView creat_at,price;

        public VH(@NonNull View itemView) {
            super(itemView);
            creat_at=itemView.findViewById(R.id.transaction_create_at);
            price=itemView.findViewById(R.id.transaction_text);
        }
    }
}
