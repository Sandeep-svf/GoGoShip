package com.mobapps.gogoship.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.R;
import com.mobapps.gogoship.fragment.MyOrderFragment;
import com.mobapps.gogoship.response.NotificationResponse;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {

    View view =null;
    Context context;
    List<NotificationResponse> notificationResponseList;
    SharedPreferences sharedPreferences;
    public NotificationAdapter(Context context, List<NotificationResponse> notificationResponseList) {

        this.context=context;
        this.notificationResponseList=notificationResponseList;
    }

    @NonNull
    @Override
    public NotificationAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification, parent, false);
        return new NotificationAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.VH holder, int position) {

        sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);

        holder.order_no.setText(""+notificationResponseList.get(position).getOrderNumber());
        holder.order_text.setText(""+notificationResponseList.get(position).getOrderStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderNo=notificationResponseList.get(position).getOrderNumber();
                MyOrderFragment myOrderFragment = new MyOrderFragment();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("order_no", orderNo);
                editor.apply();

                Bundle args = new Bundle();
                args.putString("notification_key", "notification_value");
                myOrderFragment.setArguments(args);

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment_container,myOrderFragment);
                transaction.commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView order_no,order_text;

        public VH(@NonNull View itemView) {
            super(itemView);

            order_no=itemView.findViewById(R.id.order_value);
            order_text=itemView.findViewById(R.id.order_txt);
        }
    }
}
