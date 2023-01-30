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
import com.mobapps.gogoship.model.TicketPositionModel;
import com.mobapps.gogoship.response.SupportListResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateTicketAdapter extends RecyclerView.Adapter<CreateTicketAdapter.VH> {

    View view = null;
    Context context;
    List<SupportListResponse> supportListResponseList;
    ArrayList<TicketPositionModel> ticketPositionModels = new ArrayList<>();

    public CreateTicketAdapter(Context context, List<SupportListResponse> supportListResponseList) {

        this.context = context;
        this.supportListResponseList = supportListResponseList;
    }

    @NonNull
    @Override
    public CreateTicketAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_ticket_adapter, parent, false);
        return new CreateTicketAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateTicketAdapter.VH holder, int position) {

        int pos = position;
        holder.detail.setVisibility(View.VISIBLE);
        ticketPositionModels.add(new TicketPositionModel(pos, false));

        holder.txtCategory.setText(supportListResponseList.get(position).getCategory());
        holder.txtopening.setText(supportListResponseList.get(position).getCreatedAt());
        holder.txtUpdated.setText(supportListResponseList.get(position).getUpdatedAt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ticketPositionModels.get(position).isCheck() == false) {
                    ticketPositionModels.get(position).setCheck(true);
                    holder.ticket_order_no.setVisibility(View.VISIBLE);
                    holder.ticket_order_value.setVisibility(View.VISIBLE);
                    holder.ticket_msg.setVisibility(View.VISIBLE);
                    holder.ticket_msg_value.setVisibility(View.VISIBLE);
                    holder.detail.setVisibility(View.GONE);
                    holder.ticket_order_value.setText(supportListResponseList.get(position).getOrderNumber());
                    holder.ticket_msg_value.setText(supportListResponseList.get(position).getMessage());
                } else {
                    ticketPositionModels.get(position).setCheck(false);
                    holder.ticket_order_no.setVisibility(View.GONE);
                    holder.ticket_order_value.setVisibility(View.GONE);
                    holder.ticket_msg.setVisibility(View.GONE);
                    holder.ticket_msg_value.setVisibility(View.GONE);
                    holder.detail.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return supportListResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView txtCategory, txtopening, txtUpdated, ticket_order_no, ticket_order_value, ticket_msg, ticket_msg_value, detail;

        public VH(@NonNull View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.tick_cate_val);
            txtopening = itemView.findViewById(R.id.tick_opn_val);
            txtUpdated = itemView.findViewById(R.id.last_upd_val);
            ticket_order_no = itemView.findViewById(R.id.ticket_order_no);
            ticket_order_value = itemView.findViewById(R.id.order_no_value);
            ticket_msg = itemView.findViewById(R.id.ticket_order_msg);
            ticket_msg_value = itemView.findViewById(R.id.ticket_order_msg_value);
            detail = itemView.findViewById(R.id.ticket_detail);

        }
    }
}
