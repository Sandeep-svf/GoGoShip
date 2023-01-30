package com.gogoship.gogoship.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.response.MyOrderDetailList;
import com.gogoship.gogoship.webview.WebsiteWebviewActivity;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.VH> {
    List<MyOrderDetailList> myorderList;
    View view = null;
    Context context;
    String value = "";
    String order_status = "";
    SharedPreferences sharedPreferences;
    private int mRequestCode = 100;

    public MyOrderAdapter(Context context, List<MyOrderDetailList> myorderList) {
        this.context = context;
        this.myorderList = myorderList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_order, parent, false);
        return new MyOrderAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.VH holder, int position) {

        sharedPreferences = context.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);
        value = sharedPreferences.getString("order_no", "");
        order_status = myorderList.get(position).getOrderStatus();

        if (order_status.equals("New")) {
            holder.txt_trans.setText("New");
        } else if (order_status.equals("Process")) {
            holder.txt_trans.setText("Process");
        } else if (order_status.equals("Approved")) {
            holder.txt_trans.setText("Approved");
        } else if (order_status.equals("Purchase")) {
            holder.txt_trans.setText("Purchase");
        } else if (order_status.equals("Warehouse")) {
            holder.txt_trans.setText("Warehouse");
        } else if (order_status.equals("Shipped")) {
            holder.txt_trans.setText("Shipped");
        } else if (order_status.equals("Arrived")) {
            holder.txt_trans.setText("Arrived");
        } else if (order_status.equals("delivered")) {
            holder.txt_trans.setText("delivered");
        } else if (order_status.equals("Archive")) {
            holder.txt_trans.setText("Archive");
        } else if (order_status.equals("cancel")) {
            holder.txt_trans.setText("cancel");
        } else if (order_status.equals("refund")) {
            holder.txt_trans.setText("refund");
        }

        holder.order_id.setText(myorderList.get(position).getOrderNumber());
        holder.price.setText("" + myorderList.get(position).getPrice());
        holder.created_at.setText("" + myorderList.get(position).getCreatedAt());
        //Log.e("order_no","        "+value);
        Glide.with(context).load(myorderList.get(position).getAttachment()).placeholder(R.drawable.order_image_background).into(holder.order_image);
        Glide.with(context).load(myorderList.get(position).getAttachment()).placeholder(R.drawable.order_image_background).into(holder.order_detail_image);

        if (value.equals(myorderList.get(position).getOrderNumber())) {
            holder.order_detail.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = myorderList.get(position).getOrderStatus();
                if (order_status.equals("New")) {
                    // holder.total_right.setVisibility(View.VISIBLE);
                    holder.total.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.GONE);
                    holder.truck1.setVisibility(View.VISIBLE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Process")) {
                    // holder.total_right.setVisibility(View.VISIBLE);
                    holder.total.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.GONE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.VISIBLE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Approved")) {
                    // holder.total_right.setVisibility(View.VISIBLE);
                    holder.total.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.GONE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.VISIBLE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Purchase")) {
                    // holder.total_right.setVisibility(View.VISIBLE);
                    holder.total.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.GONE);


                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.VISIBLE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Warehouse")) {
                    // holder.total_right.setVisibility(View.VISIBLE);
                    holder.total.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.GONE);


                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.VISIBLE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Shipped")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.VISIBLE);
                    holder.truck4.setVisibility(View.GONE);

                } else if (order_status.equals("Arrived")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);


                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.VISIBLE);

                } else if (order_status.equals("delivered")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.VISIBLE);

                } else if (order_status.equals("Archive")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.VISIBLE);

                } else if (order_status.equals("cancel")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.VISIBLE);

                } else if (order_status.equals("refund")) {
                    holder.total.setVisibility(View.GONE);
                    holder.total_final_2.setVisibility(View.VISIBLE);
                    holder.approve.setVisibility(View.VISIBLE);

                    holder.truck1.setVisibility(View.GONE);
                    holder.truck2.setVisibility(View.GONE);
                    holder.truck3.setVisibility(View.GONE);
                    holder.truck4.setVisibility(View.VISIBLE);

                }


                holder.order_detail.setVisibility(View.VISIBLE);
                holder.status_value.setText("" + myorderList.get(position).getOrderStatus());
                holder.order_detail_order_no.setText("" + myorderList.get(position).getOrderNumber());
                holder.order_detail_qty.setText("" + myorderList.get(position).getQuantity());
                holder.order_detail_color.setText("" + myorderList.get(position).getColor());
                holder.order_detail_size.setText("" + myorderList.get(position).getSize());
                holder.order_detail_created_at.setText("" + myorderList.get(position).getCreatedAt());
                holder.order_detail_insurance.setText("" + myorderList.get(position).getInsurance());
                holder.order_detail_fees.setText("" + myorderList.get(position).getPrice());
                holder.order_detail_total_amt.setText("" + myorderList.get(position).getTotalAmount());
                holder.order_detail_name.setText(myorderList.get(position).getName());
                holder.order_detail_updated_at.setText(myorderList.get(position).getCreatedAt());
                holder.order_detail_track_detail.setText(myorderList.get(position).getShippedDate());

                Glide.with(context).load(myorderList.get(position).getAttachment()).placeholder(R.drawable.order_image_background).into(holder.order_detail_image);
                holder.prod_url.setText(myorderList.get(position).getWebsiteName());
            }
        });

        holder.order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.order_detail.setVisibility(View.GONE);


            }
        });

        holder.weblinkImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebsiteWebviewActivity.class);
                intent.putExtra("webview_url", myorderList.get(position).getLink());
                ((Activity) context).startActivityForResult(intent, mRequestCode);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myorderList.size();
    }

    public void setFilter(List<MyOrderDetailList> filterdNames) {
        this.myorderList = filterdNames;
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder {

        CardView order_detail, order;
        TextView order_id, created_at, price, txt_trans, txt_buynow, status_value;
        TextView order_detail_name, order_detail_qty, order_detail_color, order_detail_size, order_detail_created_at, order_detail_coupon, order_detail_insurance, order_detail_fees, order_detail_total_amt, order_detail_order_no, prod_url, order_detail_updated_at, order_detail_track_detail;
        ConstraintLayout total_right, total, total_final_2;
        TextView approve;
        ImageView order_image, order_detail_image, weblinkImg;
        ImageView truck1, truck2, truck3, truck4;

        public VH(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.txt_order_n0);
            created_at = itemView.findViewById(R.id.txt_date_time);
            price = itemView.findViewById(R.id.order_price);
            order_detail = itemView.findViewById(R.id.card_order_detail);
            order = itemView.findViewById(R.id.card_my_order);
            txt_trans = itemView.findViewById(R.id.txt_trans);
            txt_buynow = itemView.findViewById(R.id.txt_buynow);
            total_right = itemView.findViewById(R.id.card_total_right);
            total = itemView.findViewById(R.id.card_reg_final);
            approve = itemView.findViewById(R.id.tx_approve);
            total_final_2 = itemView.findViewById(R.id.card_reg_final2);
            status_value = itemView.findViewById(R.id.status_value);
            order_image = itemView.findViewById(R.id.turkey_logo1_copy);

            weblinkImg = itemView.findViewById(R.id.web_link);
            order_detail_order_no = itemView.findViewById(R.id.prod_no);
            order_detail_name = itemView.findViewById(R.id.product_name);
            order_detail_qty = itemView.findViewById(R.id.qty_value);
            order_detail_color = itemView.findViewById(R.id.color_lebel_value);
            order_detail_size = itemView.findViewById(R.id.size_lebel_value);
            order_detail_created_at = itemView.findViewById(R.id.txt_date_time2);
            order_detail_coupon = itemView.findViewById(R.id.txt_coupen);
            order_detail_insurance = itemView.findViewById(R.id.txt_insurance_val);
            order_detail_fees = itemView.findViewById(R.id.value_fees);
            order_detail_total_amt = itemView.findViewById(R.id.order_det_total_amt);
            order_detail_image = itemView.findViewById(R.id.turkey_logo2);
            prod_url = itemView.findViewById(R.id.txt_trendyol);
            order_detail_updated_at = itemView.findViewById(R.id.txt_date_time_double);
            order_detail_track_detail = itemView.findViewById(R.id.track_date);

            truck1 = itemView.findViewById(R.id.truck_1);
            truck2 = itemView.findViewById(R.id.truck_2);
            truck3 = itemView.findViewById(R.id.truck_3);
            truck4 = itemView.findViewById(R.id.truck_4);
        }
    }
}
