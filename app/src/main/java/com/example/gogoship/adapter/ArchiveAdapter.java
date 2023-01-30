package com.mobapps.gogoship.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mobapps.gogoship.R;
import com.mobapps.gogoship.response.ArchiveResponse;

import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.VH> {

    Context context;
    List<ArchiveResponse> archiveResponseList;
    public ArchiveAdapter(Context context, List<ArchiveResponse> archiveResponseList) {
        this.context=context;
        this.archiveResponseList=archiveResponseList;
    }

    @NonNull
    @Override
    public ArchiveAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_check, parent, false);
        return new ArchiveAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveAdapter.VH holder, int position) {


        holder.order_no.setText(archiveResponseList.get(position).getOrderNumber());
        holder.order_from.setText(archiveResponseList.get(position).getOrderedFrom());
        holder.date_time.setText(archiveResponseList.get(position).getDateTime());
        holder.product_link.setText(archiveResponseList.get(position).getProductLink());
        holder.quantity.setText(""+archiveResponseList.get(position).getQuantity());
        holder.color.setText(""+archiveResponseList.get(position).getColor());
        holder.size.setText(""+archiveResponseList.get(position).getSize());
        holder.total.setText(""+archiveResponseList.get(position).getTotal());
        holder.cost.setText(""+archiveResponseList.get(position).getCost());
        holder.fees.setText(""+archiveResponseList.get(position).getFees());
        holder.couponcode.setText(archiveResponseList.get(position).getCouponCode());
        holder.insurance.setText(archiveResponseList.get(position).getInsurance());
        holder.status.setText(archiveResponseList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return archiveResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView order_no,order_from,date_time,product_link,quantity,color,size,total,cost,fees,couponcode,insurance,status;

        public VH(@NonNull View itemView) {
            super(itemView);

            order_no=itemView.findViewById(R.id.prod_no);
            order_from=itemView.findViewById(R.id.order_from_value);
            date_time=itemView.findViewById(R.id.date_time_value);
            product_link=itemView.findViewById(R.id.product_link_value);
            quantity=itemView.findViewById(R.id.quantity_value);
            color=itemView.findViewById(R.id.color_value);
            size=itemView.findViewById(R.id.size_value);
            total=itemView.findViewById(R.id.total_value);
            cost=itemView.findViewById(R.id.cost_value);
            fees=itemView.findViewById(R.id.fees_value);
            couponcode=itemView.findViewById(R.id.coupone_value);
            insurance=itemView.findViewById(R.id.insu_value);
            status=itemView.findViewById(R.id.status_val);

        }
    }
}
